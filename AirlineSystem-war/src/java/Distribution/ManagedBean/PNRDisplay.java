/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Schedule;
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
    
    
    
    
}
