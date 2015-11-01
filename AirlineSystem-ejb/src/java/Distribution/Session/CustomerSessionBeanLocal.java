/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yuqing
 */
@Local
public interface CustomerSessionBeanLocal {

    public String addCustomer(String firstName, String lastName, String hpNumber, String homeNumber, String email, String password, String address, String gender, Date DOB, String title, String nationality, String passportNumber);
    public Boolean emailExists(String customerEmail);
    public Customer getCustomerUseEmail(String customerEmail);
    public Customer getCustomerUseID(String customerID);
    public boolean isSameHash(String userEmail, String pwd);
    public void hashNewPwd(String customerEmail, String pwd);
    public List<PNR> retrieveCustomerPNRs (Customer customer);
    public void updateCustomerProfile (Customer customer);
    public String validateUser(String customerEmail, String passportNum);
    public String newPassword(String email, String password);
}
