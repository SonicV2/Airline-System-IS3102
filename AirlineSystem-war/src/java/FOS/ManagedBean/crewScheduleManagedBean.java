/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import CI.Entity.Pilot;
import FOS.Entity.Pairing;
import FOS.Entity.PairingPolicy;
import FOS.Entity.Team;
import FOS.Session.PairingSessionBeanLocal;
import FOS.Session.testSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
    private testSessionBeanLocal testSessionBean;

    @EJB
    private PairingSessionBeanLocal pairingSessionBean;
    

    private int time_scale_min;
    private int num_max_legs;
    private int hours_max_flight;
    private PairingPolicy pp;
    private List<Pairing> slns;
    private Pairing sln;  //selected from the webpage
    private String sln1;
    private Team team;
    private String selectMonth; //month choosen to get pairing

    /**
     * Creates a new instance of crewScheduleManagedBean
     */
    public crewScheduleManagedBean() {

    }

    public void getAssignedTeam() {
        setTeam(pairingSessionBean.generateTeam(sln));

    }

    public void changePolicy(ActionEvent event) {
        FacesMessage message = null;
        if (num_max_legs == 0 || hours_max_flight == 0 || time_scale_min == 0) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Field cannot be 0!", "");
        } else {
            pairingSessionBean.changePolicy(num_max_legs, hours_max_flight, time_scale_min);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Change successfully!", "");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

//    public void crewPairing(ActionEvent event){
//        pairingSessionBean.legMain(selectMonth);
//    }
    public void retrivePolicy(ActionEvent event) {
        setPp(pairingSessionBean.retrievePolicy());
    }

// crew pairing.html --> display all legs
    public void getSlns(ActionEvent event) {
        pairingSessionBean.legMain(selectMonth);
        setSlns(pairingSessionBean.getPairings());
    }

    public void getSelectPairingID(ActionEvent event) {

        String pairingID = sln1.substring(sln1.indexOf("=") + 1, sln1.indexOf("]") - 1);
        System.out.println("--------" + sln1.substring(sln1.indexOf("=") + 1, sln1.indexOf("]")));
        sln = pairingSessionBean.getPairingByID(pairingID);

    }

    public void generateTeam(ActionEvent event) {
        setTeam(pairingSessionBean.generateTeam(sln));
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

    /**
     * @return the slns
     */
    public List<Pairing> getSlns() {
        return slns;
    }

    /**
     * @param slns the slns to set
     */
    public void setSlns(List<Pairing> slns) {
        this.slns = slns;
    }

    /**
     * @return the sln
     */
    public Pairing getSln() {
        return sln;
    }

    /**
     * @param sln the sln to set
     */
    public void setSln(Pairing sln) {
        this.sln = sln;
    }

    /**
     * @return the sln1
     */
    public String getSln1() {
        return sln1;
    }

    /**
     * @param sln1 the sln1 to set
     */
    public void setSln1(String sln1) {
        this.sln1 = sln1;
    }

    /**
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * @return the selectMonth
     */
    public String getSelectMonth() {
        return selectMonth;
    }

    /**
     * @param selectMonth the selectMonth to set
     */
    public void setSelectMonth(String selectMonth) {
        this.selectMonth = selectMonth;
    }
    
    public void test(ActionEvent event){
        testSessionBean.test();
    }

}
