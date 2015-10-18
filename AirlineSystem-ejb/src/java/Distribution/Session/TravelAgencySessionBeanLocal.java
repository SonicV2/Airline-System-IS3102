/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.TravelAgency;
import java.security.NoSuchAlgorithmException;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.mail.NoSuchProviderException;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface TravelAgencySessionBeanLocal {

    public String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException;

    public String getSecurePassword(String passwordToHash, String salt);

    public String addTravelAgency(String name, double maxCredit, double currentCredit, double commission, String email, String address, String contactNo, String password, String primaryContact);

    public boolean isSameHash(String email, String pwd);

    public TravelAgency getAgencyUseEmail(String agencyEmail);

    public void persistTravelAgency (TravelAgency travelAgency);
    public void resetCreditsAndCommission (TravelAgency travelAgency);
    public void changeCreditLimit (TravelAgency travelAgency, double newLimit);
    public List<PNR> retrievePendingPNRs (TravelAgency travelAgency);
    public int noOfDaysSinceDate (Date date);
    public void deletePendingPNRs ();
    public void deletePNR(PNR pnr);
    public void deleteTravelAgency (TravelAgency travelAgency);
    public int noOfConfirmedBookings (TravelAgency travelAgency);
    
    public List<TravelAgency> getAllTravelAgencies();

    
}
