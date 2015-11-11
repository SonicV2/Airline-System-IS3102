/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Session.FleetSessionBeanLocal;
import Administration.Session.AccountingSessionBeanLocal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yunna
 */
@Named(value = "fleetManagedBean")
@ManagedBean
@SessionScoped

public class FleetManagedBean {

    @EJB
    private FleetSessionBeanLocal fleetSessionBean;

    @EJB
    private AccountingSessionBeanLocal accountingSessionBean;
    
    Date datePurchased;
    Date lastMaintained;
    String aircraftTypeId;
    String tailNo;
    String status;
    String hub;

    FacesMessage message;
    
    private List<AircraftType> aircraftTypes = new ArrayList<AircraftType>();
    private List<Aircraft> aircrafts;
    private Aircraft selectedAircraft;
    private List<String> aircraftTypeIds = new ArrayList<String>();

    private String userID;
    private FileHandler fh;

    public FleetManagedBean() {

    }

    //Construct a table of aircraft types with their details
    @PostConstruct
    public void retrieve() {

        setAircraftTypes(fleetSessionBean.retrieveAircraftTypes());
        setAircrafts(fleetSessionBean.retrieveAircrafts());

        //For dropdown list of aircraft types
        aircraftTypeIds.add("Airbus A330-300");
        aircraftTypeIds.add("Airbus A380-800");
        aircraftTypeIds.add("Boeing 777-200");
        aircraftTypeIds.add("Boeing 777-200ER");
        aircraftTypeIds.add("Boeing 777-300");
        aircraftTypeIds.add("Boeing 777-300ER");

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        userID = (String) session.getAttribute("isLogin");
    }

    //Acquire new aircraft
    public void acquireAircraft(ActionEvent event) {

        if (tailNo == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Aircraft Tail Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (aircraftTypeId == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select aircraft type!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (hub == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select location of the aircraft!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (datePurchased == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter date of purchase!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (lastMaintained == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter date of last maintenance!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (status == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select status of the aircraft!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        fleetSessionBean.acquireAircraft(tailNo, datePurchased, lastMaintained, aircraftTypeId, hub, status);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft is Acquired Successfully!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        setAircrafts(fleetSessionBean.retrieveAircrafts());
        AircraftType aircraftType = fleetSessionBean.getAircraftType(aircraftTypeId);
        accountingSessionBean.makeTransaction("Acquire Aircraft", aircraftType.getCost()*1000000);
        clear();

        Logger logger = Logger.getLogger(FleetManagedBean.class.getName());
        try {
            fh = new FileHandler("%h/acquireAircraft.txt", 99999, 1, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("User: " + userID
                + "has added Aircraft of Type: " + String.valueOf(aircraftTypeId));
        fh.close();
    }

    //Retire existing aircraft
    public String retireAircraft(String tailNo) {

        aircrafts = fleetSessionBean.retrieveAircrafts();
        selectedAircraft = new Aircraft();
        for (int i = 0; i < aircrafts.size(); i++) {
            if (aircrafts.get(i).getSchedules().isEmpty()) {
                selectedAircraft = aircrafts.get(i);
            }
        }

        if (selectedAircraft == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No available aircrafts to take over current aircraft! Please purchase new aircrafts first!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "RetireAircraft";
        }

        aircrafts.remove(fleetSessionBean.getAircraft(tailNo));
        fleetSessionBean.retireAircraft(tailNo, selectedAircraft.getTailNo());
        selectedAircraft = null;

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft Retired", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        setAircrafts(fleetSessionBean.retrieveAircrafts());

        Logger logger = Logger.getLogger(FleetManagedBean.class.getName());
        try {
            fh = new FileHandler("%h/retireAircraft.txt", 99999, 1, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("User: " + userID
                + "has retired Aircraft: " + tailNo);
        fh.close();

        return "RetireAircraft";
    }

    public void clear() {
        setAircraftTypeId("");
        setHub("");
        setDatePurchased(null);
        setLastMaintained(null);
        setStatus("");
    }

    //Change the format of displayed dates
    public String getDateFormat(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
        return df.format(date);
    }

    public List<String> getAircraftTypeIds() {
        return aircraftTypeIds;
    }

    public void setAircraftTypeIds(List<String> aircraftTypeIds) {
        this.aircraftTypeIds = aircraftTypeIds;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public Aircraft getSelectedAircraft() {
        return selectedAircraft;
    }

    public void setSelectedAircraft(Aircraft selectedAircraft) {
        this.selectedAircraft = selectedAircraft;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public Date getLastMaintained() {
        return lastMaintained;
    }

    public void setLastMaintained(Date lastMaintained) {
        this.lastMaintained = lastMaintained;
    }

    public String getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(String aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    public String getTailNo() {
        return tailNo;
    }

    public void setTailNo(String tailNo) {
        this.tailNo = tailNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public List<AircraftType> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftType> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }
}
