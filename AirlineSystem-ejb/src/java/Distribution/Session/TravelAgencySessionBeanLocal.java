/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.TravelAgency;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.Local;
import javax.mail.NoSuchProviderException;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface TravelAgencySessionBeanLocal {

    public List<TravelAgency> retrieveTravelAgencies();

    public String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException;

    public String getSecurePassword(String passwordToHash, String salt);

    public String addTravelAgency(String name, double maxCredit, double currentCredit, double commission, String email, String address, String contactNo, String password, String primaryContact);

    public boolean isSameHash(String email, String pwd);

    public TravelAgency getAgencyUseEmail(String agencyEmail);
    
}
