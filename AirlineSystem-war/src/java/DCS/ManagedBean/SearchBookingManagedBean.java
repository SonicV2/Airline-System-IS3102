/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import APS.Entity.Schedule;
import DCS.Session.BaggageSessionBeanLocal;
import DCS.Session.BoardingPassSessionBeanLocal;
import DCS.Session.CheckInRecordSessionBeanLocal;
import DCS.Session.PassengerNameRecordSessionBeanLocal;
import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "searchBookingManagedBean")
@SessionScoped
@ManagedBean
public class SearchBookingManagedBean implements Serializable {
    @EJB
    private BoardingPassSessionBeanLocal boardingPassSessionBean;
    @EJB
    private CheckInRecordSessionBeanLocal checkInRecordSessionBean;

    @EJB
    private BaggageSessionBeanLocal baggageSessionBean;

    @EJB
    private PassengerNameRecordSessionBeanLocal passengerNameRecordSessionBean;
    
    
    

    private String pnrNumber;
    private String passportNumber;
    private Booking reqBooking; // use passport number & pnr number from searchbooking.xhtml
    private Schedule checkinSchedule;
    private List<Schedule> restSchedules; // not valid for checkin
    private String serviceClass;
    private String boardingTime;
    private String aircraftType;
    private String totalWeightAllowed;
    
    private boolean online=false;

    public SearchBookingManagedBean() {
    }

   public void getBookingOnline(ActionEvent event) {
        FacesMessage message = null;
        List<Booking> b = passengerNameRecordSessionBean.getBooking(pnrNumber, passportNumber);

        if (b.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid PNR Number / Invalid Passport Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            setPnrNumber("");
            setPassportNumber("");
        } else {
            restSchedules = new ArrayList<Schedule>();
            setReqBooking(b.get(0));
            setCheckinSchedule(passengerNameRecordSessionBean.getCheckinSchedule(pnrNumber, passportNumber));
            calBoardingTime();
            
            checkInRecordSessionBean.addBooking(reqBooking);
            boardingPassSessionBean.addBooking(reqBooking);
            
            this.online=true;
            if (b.size() > 1) {
                for (int i = 1; i < b.size(); i++) {
                    restSchedules.add(b.get(i).getSeatAvail().getSchedule());
                }
            }
        }
    }
    
    
    
    
    
    
    
    
    public void getBooking(ActionEvent event) {
        FacesMessage message = null;
        List<Booking> b = passengerNameRecordSessionBean.getBooking(pnrNumber, passportNumber);

        if (b.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid PNR Number / Invalid Passport Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            setPnrNumber("");
            setPassportNumber("");
        } else {
            restSchedules = new ArrayList<Schedule>();
            setReqBooking(b.get(0));
            setCheckinSchedule(passengerNameRecordSessionBean.getCheckinSchedule(pnrNumber, passportNumber));
            calBoardingTime();
            
            checkInRecordSessionBean.addBooking(reqBooking);
            boardingPassSessionBean.addBooking(reqBooking);
            if (b.size() > 1) {
                for (int i = 1; i < b.size(); i++) {
                    restSchedules.add(b.get(i).getSeatAvail().getSchedule());
                }
            }
        }
    }
    


    public String retrieveFlightType() {
        retrieveClass();
        retrieveNumberofBaggageAllowed();
        aircraftType = checkinSchedule.getFlight().getAircraftType().getId();

        if (checkinSchedule.getFlight().getAircraftType().getId().equals("Airbus A330-300")) {
            return "A330_300.xhtml";
        } else if (checkinSchedule.getFlight().getAircraftType().getId().equals("Airbus A380-800")) {
            return "A380_800.xhtml";
        } else if (checkinSchedule.getFlight().getAircraftType().getId().equals("Boeing 777-200")) {
            return "B777_200.xhtml";
        } else if (checkinSchedule.getFlight().getAircraftType().getId().equals("Boeing 777-200ER")) {
            return "B777_200ER.xhtml";
        } else if (checkinSchedule.getFlight().getAircraftType().getId().equals("Boeing 777-300")) {
            return "B777_300.xhtml";
        } else if (checkinSchedule.getFlight().getAircraftType().getId().equals("Boeing 777-300ER")) {
            return "B777_300ER.xhtml";
        } else {
            return "/Login.xhtml";
        }

    }

    public void calBoardingTime() {
        Date departTime = checkinSchedule.getStartDate();

        departTime.setTime(departTime.getTime() - 1800 * 1000); // half an hour before
        setBoardingTime(departTime.toString());

        departTime.setTime(departTime.getTime() + 1800 * 1000);

    }

    public void retrieveNumberofBaggageAllowed() {
        int i = baggageSessionBean.retrieveNumberOfBaggageAllowed(getReqBooking().getClassCode());
        setTotalWeightAllowed((i * 15) + "");
    }

    public void retrieveClass() {
        setServiceClass(passengerNameRecordSessionBean.retrieveClass(reqBooking.getClassCode()));
    }

    /**
     * @return the pnrNumber
     */
    public String getPnrNumber() {
        return pnrNumber;
    }

    /**
     * @param pnrNumber the pnrNumber to set
     */
    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    /**
     * @return the passportNumber
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * @param passportNumber the passportNumber to set
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    /**
     * @return the reqBooking
     */
    public Booking getReqBooking() {
        return reqBooking;
    }

    /**
     * @param reqBooking the reqBooking to set
     */
    public void setReqBooking(Booking reqBooking) {
        this.reqBooking = reqBooking;
    }

    /**
     * @return the checkinSchedule
     */
    public Schedule getCheckinSchedule() {
        return checkinSchedule;
    }

    /**
     * @param checkinSchedule the checkinSchedule to set
     */
    public void setCheckinSchedule(Schedule checkinSchedule) {
        this.checkinSchedule = checkinSchedule;
    }

    /**
     * @return the restSchedules
     */
    public List<Schedule> getRestSchedules() {
        return restSchedules;
    }

    /**
     * @param restSchedules the restSchedules to set
     */
    public void setRestSchedules(List<Schedule> restSchedules) {
        this.restSchedules = restSchedules;
    }

    /**
     * @return the serviceClass
     */
    public String getServiceClass() {
        return serviceClass;
    }

    /**
     * @param serviceClass the serviceClass to set
     */
    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    /**
     * @return the boardingTime
     */
    public String getBoardingTime() {
        return boardingTime;
    }

    /**
     * @param boardingTime the boardingTime to set
     */
    public void setBoardingTime(String boardingTime) {
        this.boardingTime = boardingTime;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    /**
     * @return the totalWeightAllowed
     */
    public String getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    /**
     * @param totalWeightAllowed the totalWeightAllowed to set
     */
    public void setTotalWeightAllowed(String totalWeightAllowed) {
        this.totalWeightAllowed = totalWeightAllowed;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    
    

}
