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
import java.util.HashSet;
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
public class CrewScheduleManagedBean {

    @EJB
    private A380PairingSessionBeanLocal a380PairingSessionBean;
    @EJB
    private PairingSessionBeanLocal pairingSessionBean;

    private int time_scale_min;
    private int num_max_legs;
    private int hours_max_flight;
    private PairingPolicy pp;

    String max_hour;

    private List<Pairing> slns;
    private Pairing sln;  //selected from the webpage
    private String sln1;
    private PairingDetail selectPairingDetail;
    private Team team;
    private String selectMonth; //month choosen to get pairing
    private String selectYear;

    private List<Pairing> restPairing;
    private List<Pairing> slnA380;
    private List<Pairing> restPairingA380;

    private List<String> scheduleResult;

    private List<String> months;
    
      private List<PairingDetail> details = new ArrayList<PairingDetail>();
      
      private String pairingInformation;

    /**
     * Creates a new instance of crewScheduleManagedBean
     */
    public CrewScheduleManagedBean() {

    }

    @PostConstruct
    public void setMonth() {
        months = new ArrayList<String>();
        months.add("01");
        months.add("02");
        months.add("03");
        months.add("04");
        months.add("05");
        months.add("06");
        months.add("07");
        months.add("08");
        months.add("09");
        months.add("10");
        months.add("11");
        months.add("12");
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

        clear();
    }

    public void retrivePolicy(ActionEvent event) {
        setPp(pairingSessionBean.retrievePolicy());
        max_hour = (pp.getHours_max_flight() / 100) + "";

    }

// crew pairing.html --> display all legs
    public void getSlns(ActionEvent event) {
        FacesMessage message = null;
        Date date = new Date();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("MM").format(date));

        if (Integer.parseInt(selectYear) == currentYear) {
            if (Integer.parseInt(selectMonth) == currentMonth + 1 || Integer.parseInt(selectMonth) == currentMonth) {
                pairingSessionBean.legMain(selectMonth, selectYear);
                setSlns(pairingSessionBean.filterPairings(selectYear, selectMonth));
         
         
                restPairing = new ArrayList<Pairing>();

                for (Pairing ppp : slns) {

                    String formattedDate2 = new SimpleDateFormat("dd/MM/yyyy").format(date);

                    if (checkTime(ppp.getFDate(), formattedDate2) <= 0) {
                        if (ppp.isPaired() == false) {
                            restPairing.add(ppp);
                        }
                    }

                }
                scheduleNew();

            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot gerenate pairings for select month!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot gerenate pairings for select year ", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void clearPairing() {
        pairingSessionBean.clearPairing();
    }

    public void scheduleNew() {
        String temp = "";

        scheduleResult = new ArrayList<String>();

        for (int i = 0; i < restPairing.size(); i++) {
            temp += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                    + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                    + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";
            temp += "\u26F3 Pairing ID: " + restPairing.get(i).getId() + " <br /> \uD83D\uDCC6 Pairing Start Date: " + restPairing.get(i).getFDate() + "   " + "<br /> \uD83D\uDD50 Pairing Total Flight Hours: " + restPairing.get(i).getFlightHour() + "<br />";

            for (int j = 0; j < restPairing.get(i).getFlightNumbers().size(); j++) {
                
                
                if (j == 0) {

                    temp += "<br />Flight Number: " + restPairing.get(i).getFlightNumbers().get(j)
                            + "<br /> " + "Flight Route: "
                            + restPairing.get(i).getFlightCities().get(j) + " " + "\u2708".toUpperCase() + " " + restPairing.get(i).getFlightCities().get(j + 1)
                            + "<br />" + " Flight Schedules: " + restPairing.get(i).getFlightTimes().get(j) + "<br />";
                }
                if (j != 0) {
                    String temp1 = restPairing.get(i).getFlightTimes().get(j).substring(10);

                    String temp2 = restPairing.get(i).getFlightTimes().get(j - 1).substring(10);

                    if (!temp1.equals(temp2)) {
                        temp += "<br /> ----------------------------------------------------------- <br />";
                    }

                  
                    temp += "<br />Flight Number: " + restPairing.get(i).getFlightNumbers().get(j)
                            + "<br /> " + "Flight Route: "
                            + restPairing.get(i).getFlightCities().get(j) + " " + "\u2708".toUpperCase() + " " + restPairing.get(i).getFlightCities().get(j + 1)
                            + "<br />" + " Flight Schedules: " + restPairing.get(i).getFlightTimes().get(j) + "<br />";
                    

                }

            }

        }

        String[] sps = temp.split("\u26F3 Pairing ID: ");

        for (int i = 1; i < sps.length; i++) {
            addPairingDetail(sps[i]);
            scheduleResult.add("\u26F3 Pairing ID: " + sps[i]);
        }

    }
    
     public void addPairingDetail(String pairs){
    
       String title = pairs.substring(0,pairs.indexOf("Flight Number:"));
       String content = pairs.substring(pairs.indexOf("Flight Number:"));
       
        PairingDetail pd = new PairingDetail();
        pd.create("Pairing ID: "+title, content);
        
        getDetails().add(pd);
    }
     
     public String viewPairingInfo(String info){
         pairingInformation = info;
         return "PairingInformation";
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
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public void getA380Slns(ActionEvent event) {
        a380PairingSessionBean.legMain(selectMonth, selectYear);
        setSlnA380(a380PairingSessionBean.getPairings());

        restPairingA380 = new ArrayList<Pairing>();
        FacesMessage message = null;
        if (slnA380 == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No flights for this month!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            for (Pairing ppp : slnA380) {
                if (ppp.isPaired() == false) {
                    restPairingA380.add(ppp);
                }
            }

        }

    }

//    public void getSelectPairingID(ActionEvent event) {
//
//        FacesMessage message = null;
//        System.out.println("SLN: "+ sln1.toString());
//
//        if (sln1 == null) {
//            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Pairing is selected!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        } else {
//            String select = sln1.split("<br /> \uD83D\uDCC6 Pairing Start Date:")[0];
//
//            System.out.println("SELECT: "+ select);
//            String pairingID = select.split("Pairing ID: ")[1];
//
//            sln = pairingSessionBean.getPairingByID(pairingID.trim());
//        }
//    }
    
    
    
    public void getSelectPairingID(ActionEvent event) {

        FacesMessage message = null;
    

        if (selectPairingDetail == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Pairing is selected!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            String select = selectPairingDetail.getTitle().split("<br /> \uD83D\uDCC6 Pairing Start Date:")[0];

            System.out.println("SELECT: "+ select);
            
            String pairingID = select.split("Pairing ID: ")[1];

            sln = pairingSessionBean.getPairingByID(pairingID.trim());
            sln1 = "Successful";
        }
    }
    
    

    public void generateTeam(ActionEvent event) {
        FacesMessage message = null;
        if (sln1 == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Generate Team!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setTeam(pairingSessionBean.generateTeam(sln));
            if (team == null) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not Enough Cabin Crews or Pilots!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void generateA380Team(ActionEvent event) {
        FacesMessage message = null;
        if (sln1 == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Generate A380 Team!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {

            setTeam(a380PairingSessionBean.generateA380Team(sln));
            if (team == null) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not Enough Cabin Crews or Pilots!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void clear() {
        this.hours_max_flight = 0;
        this.num_max_legs = 0;
        this.time_scale_min = 0;
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
        this.hours_max_flight = hours_max_flight * 100;
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

//    /**
//     * @return the lists
//     */
//    public List<List<Pairing>> getLists() {
//        return lists;
//    }
//
//    /**
//     * @param lists the lists to set
//     */
//    public void setLists(List<List<Pairing>> lists) {
//        this.lists = lists;
//    }
    /**
     * @return the selectYear
     */
    public String getSelectYear() {
        return selectYear;
    }

    /**
     * @param selectYear the selectYear to set
     */
    public void setSelectYear(String selectYear) {
        this.selectYear = selectYear;
    }

    /**
     * @return the scheduleResult
     */
    public List<String> getScheduleResult() {
        return scheduleResult;
    }

    /**
     * @param scheduleResult the scheduleResult to set
     */
    public void setScheduleResult(List<String> scheduleResult) {
        this.scheduleResult = scheduleResult;
    }

    public String getMax_hour() {
        return max_hour;
    }

    public void setMax_hour(String max_hour) {
        this.max_hour = max_hour;
    }

    /**
     * @return the months
     */
    public List<String> getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(List<String> months) {
        this.months = months;
    }

    /**
     * @return the details
     */
    public List<PairingDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<PairingDetail> details) {
        this.details = details;
    }

    /**
     * @return the pairingInformation
     */
    public String getPairingInformation() {
        return pairingInformation;
    }

    /**
     * @param pairingInformation the pairingInformation to set
     */
    public void setPairingInformation(String pairingInformation) {
        this.pairingInformation = pairingInformation;
    }

    /**
     * @return the selectPairingDetail
     */
    public PairingDetail getSelectPairingDetail() {
        return selectPairingDetail;
    }

    /**
     * @param selectPairingDetail the selectPairingDetail to set
     */
    public void setSelectPairingDetail(PairingDetail selectPairingDetail) {
        this.selectPairingDetail = selectPairingDetail;
    }

}
