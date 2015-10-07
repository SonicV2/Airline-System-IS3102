/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import Inventory.Session.BookingSessionBeanLocal;
import javax.ejb.EJB;

/**
 *
 * @author YiQuan
 */
@Named(value = "flightDataManageBean")
@ManagedBean
@SessionScoped
public class flightDataManageBean {

    /**
     * Creates a new instance of flightDataManageBean
     */
    public flightDataManageBean() {
    }
    
    @EJB
    private BookingSessionBeanLocal bs;
    private String flightNo;
    
    public  void createData(){
        bs.bookSeats(flightNo);
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }
    
}