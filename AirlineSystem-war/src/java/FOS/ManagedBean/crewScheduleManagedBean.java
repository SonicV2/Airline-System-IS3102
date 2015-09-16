/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import FOS.Session.PairingSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "crewScheduleManagedBean")
@ManagedBean
@SessionScoped
public class crewScheduleManagedBean {
    @EJB
    private PairingSessionBeanLocal pairingSessionBean;

     private int time_scale_min; 
     private int num_max_legs;
     private int hours_max_flight;
    
    /**
     * Creates a new instance of crewScheduleManagedBean
     */
    public crewScheduleManagedBean() {
    }
    
    
    public void showPolicy(ActionEvent event){
        pairingSessionBean.setPolicy();
        
    }
    
    public void crewPairing(ActionEvent event){
        pairingSessionBean.legMain();
    }

    /**
     * @return the time_scale_min
     */
    public int getTime_scale_min() {
        return time_scale_min;
    }

    /**
     * @param time_scale_min the time_scale_min to set
     */
    public void setTime_scale_min(int time_scale_min) {
        this.time_scale_min = time_scale_min;
    }

    /**
     * @return the num_max_legs
     */
    public int getNum_max_legs() {
        return num_max_legs;
    }

    /**
     * @param num_max_legs the num_max_legs to set
     */
    public void setNum_max_legs(int num_max_legs) {
        this.num_max_legs = num_max_legs;
    }

    /**
     * @return the hours_max_flight
     */
    public int getHours_max_flight() {
        return hours_max_flight;
    }

    /**
     * @param hours_max_flight the hours_max_flight to set
     */
    public void setHours_max_flight(int hours_max_flight) {
        this.hours_max_flight = hours_max_flight;
    }
    
}
