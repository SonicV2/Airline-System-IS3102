/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import Distribution.Entity.PNR;

/**
 *
 * @author parthasarthygupta
 */
public class PendingPNR {
    
    private PNR pnr;
    private int noOfDaysSinceBooking;

    public PNR getPnr() {
        return pnr;
    }

    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }

    public int getNoOfDaysSinceBooking() {
        return noOfDaysSinceBooking;
    }

    public void setNoOfDaysSinceBooking(int noOfDaysSinceBooking) {
        this.noOfDaysSinceBooking = noOfDaysSinceBooking;
    }
    
    
    
}
