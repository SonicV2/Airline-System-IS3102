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
@Named(value = "inventoryManageBean")
@ManagedBean
@SessionScoped

public class inventoryManageBean {
    @EJB
    private RevenueManagementLocal rm;
   
    
    
    private String flightNo;
    private String flightTime;
    private String flightDate;
   
    
    public void createFlightSession(ActionEvent event){
        System.out.println("Managed Bean reached!");
        rm.createAvailability(flightNo, flightDate, flightTime);
    }

    /**
     * @return the flightNo
     */
    public String getFlightNo() {
        return flightNo;
    }

    /**
     * @param flightNo the flightNo to set
     */
    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    /**
     * @return the flightTime
     */
    public String getFlightTime() {
        return flightTime;
    }

    /**
     * @param flightTime the flightTime to set
     */
    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    /**
     * @return the flightDate
     */
    public String getFlightDate() {
        return flightDate;
    }

    /**
     * @param flightDate the flightDate to set
     */
    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }
    
    
    
    
    
}
