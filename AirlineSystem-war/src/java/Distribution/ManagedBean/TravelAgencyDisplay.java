/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import Distribution.Entity.TravelAgency;

/**
 *
 * @author parthasarthygupta
 */
public class TravelAgencyDisplay {
    
    
    private TravelAgency travelAgency;
    private int noOfConfirmedBookings;

    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }

    public int getNoOfConfirmedBookings() {
        return noOfConfirmedBookings;
    }

    public void setNoOfConfirmedBookings(int noOfConfirmedBookings) {
        this.noOfConfirmedBookings = noOfConfirmedBookings;
    }
    
    
    
}
