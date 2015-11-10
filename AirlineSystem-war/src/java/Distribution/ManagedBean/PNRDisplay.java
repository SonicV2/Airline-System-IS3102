/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;

/**
 *
 * @author parthasarthygupta
 */
public class PNRDisplay {
    private String id;
    private int noOfTravellers;
    private List<Schedule> uniqueSchedules;
    private String serviceType;
    private List<String> travellerNames;
    int noOfBags;
    Date bookingDate;
    boolean refundable;
    private double totalPrice;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNoOfTravellers() {
        return noOfTravellers;
    }

    public void setNoOfTravellers(int noOfTravellers) {
        this.noOfTravellers = noOfTravellers;
    }

    public List<Schedule> getUniqueSchedules() {
        return uniqueSchedules;
    }

    public void setUniqueSchedules(List<Schedule> uniqueSchedules) {
        this.uniqueSchedules = uniqueSchedules;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<String> getTravellerNames() {
        return travellerNames;
    }

    public void setTravellerNames(List<String> travellerNames) {
        this.travellerNames = travellerNames;
    }

    public int getNoOfBags() {
        return noOfBags;
    }

    public void setNoOfBags(int noOfBags) {
        this.noOfBags = noOfBags;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    /**
     * @return the totalPrice
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    
    
    
}
