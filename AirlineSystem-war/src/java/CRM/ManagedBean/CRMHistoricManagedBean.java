/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.ManagedBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import CRM.Session.AnalyticsSessionBeanLocal;
import Inventory.Session.ClassManagementLocal;
import javax.ejb.EJB;

/**
 *
 * @author YiQuan
 */
@Named(value = "cRMHistoricManagedBean")
@ManagedBean
@SessionScoped
public class CRMHistoricManagedBean implements Serializable {
    @EJB
    private AnalyticsSessionBeanLocal am;
    /**
     * Creates a new instance of CRMHistoricManagedBean
     */
    public CRMHistoricManagedBean() {
        
    }
    
    public void createCustomers(){
        am.createPsuedoCustomers();
    }
    
    public void linkCustomertoBooking(){
        am.pseudoLink();
    }
    
}
