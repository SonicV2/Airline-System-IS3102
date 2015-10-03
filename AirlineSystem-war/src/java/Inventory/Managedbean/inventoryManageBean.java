/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import Inventory.Session.BookingSessionBeanLocal;
import Inventory.Session.PricingManagementLocal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import java.util.List;


/**
 *
 * @author HOULIANG
 */
@Named(value = "inventoryManageBean")
@ManagedBean
@SessionScoped

public class inventoryManageBean {
    @EJB
    private PricingManagementLocal rm; 
    private String flightNo;
    private String flightTime;
    private String flightDate;
    
    private int[] realSold;

   @EJB
    private BookingSessionBeanLocal bm;
   
  
    public void createFlightData(String Flightno){
        bm.bookSeats(Flightno);     
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
    

    /**
     * @return the realSold
     */
    public int[] getRealSold() {
        return realSold;
    }

    /**
     * @param realSold the realSold to set
     */
    public void setRealSold(int[] realSold) {
        this.realSold = realSold;
    }

   
    
}
