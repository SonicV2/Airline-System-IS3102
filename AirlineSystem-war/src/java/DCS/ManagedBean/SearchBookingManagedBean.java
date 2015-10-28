/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import APS.Entity.Schedule;
import DCS.Session.PassengerNameRecordSessionBeanLocal;
import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
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
    private PassengerNameRecordSessionBeanLocal passengerNameRecordSessionBean;

    private String pnrNumber;
    private String passportNumber;
    private Booking reqBooking; // use passport number & pnr number from searchbooking.xhtml
    private Schedule checkinSchedule;
    private List<Schedule> restSchedules; // not valid for checkin

    public SearchBookingManagedBean() {
    }

    public void getBooking(ActionEvent event) {
        FacesMessage message = null;
        List<Booking> b = passengerNameRecordSessionBean.getBooking(pnrNumber, passportNumber);

        System.out.println("DDDDD: " + b.size());

        if (b.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid PNR Number / Invalid Passport Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            setPnrNumber("");
            setPassportNumber("");
        } else {
            restSchedules = new ArrayList<Schedule>();
            setReqBooking(b.get(0));
            setCheckinSchedule(passengerNameRecordSessionBean.getCheckinSchedule(pnrNumber, passportNumber));
            
            if (b.size() > 1) {
                for (int i = 1; i < b.size(); i++) {
                    System.out.println("FFFFFFF: "+b.get(i).getSeatAvail().getSchedule());
                    restSchedules.add(b.get(i).getSeatAvail().getSchedule());
                }
            }
        }
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

}
