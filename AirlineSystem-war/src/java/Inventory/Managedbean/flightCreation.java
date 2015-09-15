/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import Inventory.Session.RevenueManagementLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author HOULIANG
 */
@Named(value = "inventoryManagedBean")
@ManagedBean
@SessionScoped

public class flightCreation {
    @EJB
    private RevenueManagementLocal rm;
    
    
    String flightNo;
    String flightTime;
    String flightDate;
   
    
    public void createFlightSession(){
        rm.createAvailability(flightNo, flightDate,flightTime);
    }
    
    
    
    
    
}
