/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Session;

import Distribution.Entity.Customer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface AnalyticsSessionBeanLocal {

    public void createPsuedoCustomers();

    public void pseudoLink();

    public List<CustomerScore> getRecency(int from, int to);


    public List<CustomerScore> getFrequency(int from, int to);

    public List<CustomerScore> getMonetary(int from, int to);
    
    public List<Customer> getLostCustomers();
    
    public double getRetentionRate();
    
    public List<CustomerScore> getCLV(int from, int to, double retention);


}
