/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import APS.Entity.Schedule;
import DCS.Entity.CheckInRecord;
import DCS.Session.SummarySessionBeanLocal;
import Inventory.Entity.Booking;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "summaryManagedBean")
@SessionScoped
@ManagedBean
public class SummaryManagedBean implements Serializable {

    @EJB
    private SummarySessionBeanLocal summarySessionBean;

    private String flightNumber;
    private Date startDateTime;
    private Schedule reqSchedule; //search schedule
    private List<CheckInRecord> records;
    private  List<Booking> bookings;

    public SummaryManagedBean() {
    }

    public void searchSchdule(ActionEvent event) {
      records = new ArrayList<CheckInRecord>();
      
        reqSchedule = summarySessionBean.retrieveSchedule(flightNumber, startDateTime);
        if (reqSchedule == null) {

        } else {
             bookings = reqSchedule.getSeatAvailability().getBookings();
            records = retrieveRecords(bookings);
        }

    }
    
    public List<CheckInRecord> retrieveRecords(List<Booking> bookings){
        return (summarySessionBean.retrieveRecords(bookings));
    }

    /**
     * @return the flightNumber
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * @param flightNumber the flightNumber to set
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = "MA" + flightNumber;
    }

    /**
     * @return the startDateTime
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the reqSchedule
     */
    public Schedule getReqSchedule() {
        return reqSchedule;
    }

    /**
     * @param reqSchedule the reqSchedule to set
     */
    public void setReqSchedule(Schedule reqSchedule) {
        this.reqSchedule = reqSchedule;
    }

    /**
     * @return the records
     */
    public List<CheckInRecord> getRecords() {
        return records;
    }

    /**
     * @param records the records to set
     */
    public void setRecords(List<CheckInRecord> records) {
        this.records = records;
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * @param bookings the bookings to set
     */
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

}
