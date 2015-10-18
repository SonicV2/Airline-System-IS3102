/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface TravelAgencySessionBeanLocal {
    public void persistTravelAgency (TravelAgency travelAgency);
    public void resetCreditsAndCommission (TravelAgency travelAgency);
    public void changeCreditLimit (TravelAgency travelAgency, double newLimit);
    public List<PNR> retrievePendingPNRs (TravelAgency travelAgency);
    public int noOfDaysSinceDate (Date date);
    public void deletePendingPNRs ();
    public void deletePNR(PNR pnr);
    
    public List<TravelAgency> getAllTravelAgencies();
    
}
