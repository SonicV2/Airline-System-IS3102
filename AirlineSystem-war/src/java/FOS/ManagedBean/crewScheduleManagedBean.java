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
import FOS.Session.A380PairingSessionBeanLocal;
import FOS.Session.PairingSessionBeanLocal;
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
    private A380PairingSessionBeanLocal a380PairingSessionBean;
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

    private List<Pairing> restPairing;
    private List<Pairing> slnA380;
    private List<Pairing> restPairingA380;

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
        Date date = new Date();
        pairingSessionBean.legMain(selectMonth);
        setSlns(pairingSessionBean.getPairings());

        restPairing = new ArrayList<Pairing>();

        for (Pairing ppp : slns) {

            String formattedDate2 = new SimpleDateFormat("dd/MM/yyyy").format(date);
            System.out.println("Schedule Date2: " + formattedDate2);
            System.out.println("ppp.getdate: " + ppp.getFDate());

            if (checkTime(ppp.getFDate(), formattedDate2) <= 0) {
                if (ppp.isPaired() == false) {
                    restPairing.add(ppp);
                }
            }

        }

    }

    public long checkTime(String time1, String time2) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date1;
        Date date2;
        try {
            date1 = formatter.parse(time1);
            date2 = formatter.parse(time2);
            long diff = (date2.getTime() - date1.getTime());
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long result = diffDays * 24 * 60 + diffHours * 60 + diffMinutes;
            System.out.println("REsult: " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public void getA380Slns(ActionEvent event) {
        a380PairingSessionBean.legMain(selectMonth);
        setSlns(a380PairingSessionBean.getPairings());

        restPairingA380 = new ArrayList<Pairing>();
        FacesMessage message = null;
        if (slnA380 == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No flights for this month!", "");
        } else {
            for (Pairing ppp : slnA380) {
                if (ppp.isPaired() == false) {
                    restPairingA380.add(ppp);
                }
            }

        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void getSelectPairingID(ActionEvent event) {

        String pairingID = sln1.substring(sln1.indexOf("=") + 1, sln1.indexOf("]") - 1);
        System.out.println("--------" + sln1.substring(sln1.indexOf("=") + 1, sln1.indexOf("]")));
        sln = pairingSessionBean.getPairingByID(pairingID);

    }

    public void generateTeam(ActionEvent event) {
        setTeam(pairingSessionBean.generateTeam(sln));
    }

    public void generateA380Team(ActionEvent event) {
        setTeam(a380PairingSessionBean.generateA380Team(sln));
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

    public List<Pairing> getRestPairing() {
        return restPairing;
    }

    public void setRestPairing(List<Pairing> restPairing) {
        this.restPairing = restPairing;
    }

    /**
     * @return the slnA380
     */
    public List<Pairing> getSlnA380() {
        return slnA380;
    }

    /**
     * @param slnA380 the slnA380 to set
     */
    public void setSlnA380(List<Pairing> slnA380) {
        this.slnA380 = slnA380;
    }

    /**
     * @return the restPairingA380
     */
    public List<Pairing> getRestPairingA380() {
        return restPairingA380;
    }

    /**
     * @param restPairingA380 the restPairingA380 to set
     */
    public void setRestPairingA380(List<Pairing> restPairingA380) {
        this.restPairingA380 = restPairingA380;
    }

}
