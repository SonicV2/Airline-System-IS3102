/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import FOS.Entity.PairingPolicy;
import FOS.Session.PairingSessionBeanLocal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
     private PairingPolicy pp;
    private ArrayList<ArrayList<String>> slns;
    private String sln;
    /**
     * Creates a new instance of crewScheduleManagedBean
     */
    public crewScheduleManagedBean() {
        
    }
    
    
    public void changePolicy(ActionEvent event){
         FacesMessage message = null;
        if(num_max_legs==0 || hours_max_flight==0 ||  time_scale_min==0){
            
              message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Field cannot be 0!", "");
        }else{
        pairingSessionBean.changePolicy(num_max_legs, hours_max_flight, time_scale_min);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change successfully!", "");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void crewPairing(ActionEvent event){
        pairingSessionBean.legMain();
    }

    public void retrivePolicy(ActionEvent event){
        setPp(pairingSessionBean.retrievePolicy());
    }
    
    public void getSlns(ActionEvent event){
        setSlns(pairingSessionBean.legMain());
    }
    
    public void getSln(ActionEvent event){
        System.out.println("Look Here! " +sln);
        String[] str = sln.split(",");
        System.out.println("1: " +str[0].substring(1)); //to remove the [
        System.out.println("2: "+ str[3].substring(1)); // to remove the space
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

    /**
     * @return the pp
     */
    public PairingPolicy getPp() {
        return pp;
    }

    /**
     * @param pp the pp to set
     */
    public void setPp(PairingPolicy pp) {
        this.pp = pp;
    }

    public ArrayList<ArrayList<String>> getSlns() {
        return slns;
    }

    public void setSlns(ArrayList<ArrayList<String>> slns) {
        this.slns = slns;
    }

    /**
     * @return the sln
     */
    public String getSln() {
        return sln;
    }

    /**
     * @param sln the sln to set
     */
    public void setSln(String sln) {
        this.sln = sln;
    }

 
    
}
