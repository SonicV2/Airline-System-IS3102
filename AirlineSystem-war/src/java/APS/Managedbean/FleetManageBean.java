/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Session.FleetSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Yunna
 */
@Named(value = "fleetManageBean")
@ManagedBean
@SessionScoped

public class FleetManageBean {
@EJB
    private FleetSessionBeanLocal fleetSessionBean;

    Date datePurchased;
    Date lastMaintained;
    String aircraftTypeId;
    Long tailNo;
    String status;

    AircraftType aircraftType = new AircraftType();
    
    String id;
    double speed;
    int travelRange;
    int firstSeats;
    int businessSeats;
    int economySeats;
    int totalStaff;
    double cost;
    double fuelCost;
    FacesMessage message;
    
    
    private List<AircraftType> aircraftTypes = new ArrayList<AircraftType>();
    private List<Aircraft> aircrafts;
    private Aircraft selectedAircraft;
    private List<String> aircraftTypeIds = new ArrayList<String> ();
    
    
    public FleetManageBean() {
    }
    
    @PostConstruct
    public void retrieve(){
        
        setAircraftTypes(fleetSessionBean.retrieveAircraftTypes());
        setAircrafts(fleetSessionBean.retrieveAircrafts());
        aircraftTypeIds.add("Airbus A330-300");
        aircraftTypeIds.add("Airbus A380-800");
        aircraftTypeIds.add("Boeing 777-200");
        aircraftTypeIds.add("Boeing 777-200ER");
        aircraftTypeIds.add("Boeing 777-300");
        aircraftTypeIds.add("Boeing 777-300ER");
        
        
    }
    
    /*This is for admin to acquire new aircraft*/
    public void acquireAircraft(ActionEvent event){

        if (fleetSessionBean.getAircraftType(aircraftTypeId) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such aircraft type!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        fleetSessionBean.acquireAircraft(datePurchased, lastMaintained, aircraftTypeId);
    }

    public void retireAircraft(ActionEvent event){
        
        if (!selectedAircraft.getStatus().equals("Stand-By")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft is not in a status to delete!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        aircrafts.remove(selectedAircraft);
        fleetSessionBean.retireAircraft(selectedAircraft.getTailNo());
        selectedAircraft = null;
    }
    
    public List<String> getAircraftTypeIds(){
         return aircraftTypeIds;
     }
     
    public void setAircraftTypeIds(List<String> aircraftTypeIds) {
        this.aircraftTypeIds = aircraftTypeIds;
    }
   
    
    public List<Aircraft> getAircrafts(){
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
    
    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }
    
    public String getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(String aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }
    
    public Long getTailNo() {
        return tailNo;
    }
    
    public void setTailNo(Long tailNo) {
        this.tailNo = tailNo;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<AircraftType> getAircraftTypes(){
         return aircraftTypes;
     }
     
    public void setAircraftTypes(List<AircraftType> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getTravelRange() {
        return travelRange;
    }

    public void setTravelRange(int travelRange) {
        this.travelRange = travelRange;
    }

    public int getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(int totalStaff) {
        this.totalStaff = totalStaff;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public int getFirstSeats() {
        return firstSeats;
    }

    public void setFirstSeats(int firstSeats) {
        this.firstSeats = firstSeats;
    }
    
    public int getBusinessSeats() {
        return businessSeats;
    }

    public void setBusinessSeats(int businessSeats) {
        this.businessSeats = businessSeats;
    }
    
    public int getEconomySeats() {
        return economySeats;
    }

    public void setEconomySeats(int economySeats) {
        this.economySeats = economySeats;
    }

}
