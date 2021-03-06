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
    public void resetCreditsAndCommission(TravelAgency travelAgency, double currentSettlement);
    public void changeCreditLimit (TravelAgency travelAgency, double newLimit);
    public List<PNR> retrievePendingPNRs (TravelAgency travelAgency);
    public int noOfDaysSinceDate (Date date);
    public int deletePendingPNRs ();
    public void deletePNR(PNR pnr);
    public void deleteTravelAgency (TravelAgency travelAgency);
    public int noOfConfirmedBookings (TravelAgency travelAgency);
    public TravelAgency getTravelAgencyById(Long id);
    public void cancelExpiredPendingPNRs();
    public double getCurrentMonthSettlement(TravelAgency travelAgency, Date date);
    
    public List<TravelAgency> getAllTravelAgencies();

    public String validateUser(String agencyEmail, Long id);

    public void hashNewPwd(String agencyEmail, String pwd);

    public String newPassword(String email, String password);

    public void updateAgencyProfile(TravelAgency travelAgency);

    public Boolean emailExists(String agencyEmail);

    public void deductCredit(TravelAgency travelAgency, double price);

    public void linkPNR(TravelAgency travelAgency, PNR pnr);
    
    public void cancelPNR(TravelAgency travelAgency, PNR pnr, double price);

    public void confirmPNR(TravelAgency travelAgency, PNR pnr, double price);


    
}
