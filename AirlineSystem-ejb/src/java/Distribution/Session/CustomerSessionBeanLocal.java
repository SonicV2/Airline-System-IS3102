/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.Customer;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Yuqing
 */
@Local
public interface CustomerSessionBeanLocal {

    public String addCustomer(String firstName, String lastName, String hpNumber, String homeNumber, String email, String password, String address, String gender, Date DOB);
    public Boolean emailExists(String customerEmail);
    public Customer getCustomerUseEmail(String customerEmail);
    public Customer getCustomerUseID(String customerID);
    public boolean isSameHash(String userEmail, String pwd);
}
