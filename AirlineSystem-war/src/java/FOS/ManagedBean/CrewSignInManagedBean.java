/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Entity.Pilot;
import CI.Managedbean.LoginManageBean;
import CI.Session.EmployeeSessionBeanLocal;
import FOS.Entity.Pairing;
import FOS.Entity.Team;
import FOS.Session.CrewSignInSessionBeanLocal;
import FOS.Session.PairingSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "crewSignInManagedBean")
@ManagedBean
@SessionScoped
public class CrewSignInManagedBean {

    @EJB
    private PairingSessionBeanLocal pairingSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    @EJB
    private CrewSignInSessionBeanLocal crewSignInSessionBean;

    @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;

    private Team team;
    private List<CabinCrew> ccs;
    private List<Pilot> pilots;
    private List<String> messages;
    private List<Schedule> schedules;
    private List<Pairing> pairings;
    private String selectSchedule;
    private Date signInDate;
    private Pairing selectPairing;
    private String msg;
    private Schedule firstSchedule;
    private List<String> scheduleResult;
    private List<String> scheduleResultDisabled;
    private String selectedPairing; // select pairingID from webpage

    private String pairingID;

    /**
     * Creates a new instance of CrewSignInManagedBean
     */
    public CrewSignInManagedBean() {

    }

    @PostConstruct
    public void getCCTeam() {
        FacesMessage message = null;
        String crewName = loginManageBean.getEmployeeUserName();
        if (crewSignInSessionBean.getCCTeam(crewName) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not assigned to a team", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setTeam(getCrewSignInSessionBean().getCCTeam(crewName));
            viewMembers();
            getCurrentPairing();
            scheduleNew();
        }
    }

    public void scheduleNew() {
        signInDate = new Date();
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(signInDate);
        String tempString = "";
        String tempDisable = "";

        scheduleResult = new ArrayList<String>();
        scheduleResultDisabled = new ArrayList<String>();

        for (int i = 0; i < pairings.size(); i++) {

            //Bug -->> e.g 24/9/2015 23:00  -- 25/9/2015 00;30 ForDISABLED pairings
            getFirstPairingSchedule(pairings.get(i));

            String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(firstSchedule.getStartDate());

            String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

            if (checkTime(formattedDate2, formattedDate1) > 60 || checkTime(formattedDate2, formattedDate1) < 0) {

                tempString += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                        + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                        + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";
                tempString += "\u26F3 Pairing ID: [" + pairings.get(i).getId() + "] <br /> \uD83D\uDCC6 Pairing Start Date: " + pairings.get(i).getFDate() + "   " + "<br /> \uD83D\uDD50 Pairing Total Flight Hours: " + pairings.get(i).getFlightHour() + "<br />";

                for (int j = 0; j < pairings.get(i).getFlightNumbers().size(); j++) {
                    if (j == 0) {

                        tempString += "<br /> Flight Number: " + pairings.get(i).getFlightNumbers().get(j)
                                + "<br />" + "Flight Route: "
                                + pairings.get(i).getFlightCities().get(j) + " " + "\u2708".toUpperCase() + " " + pairings.get(i).getFlightCities().get(j + 1)
                                + "<br />" + " Flight Schedules: " + pairings.get(i).getFlightTimes().get(j);
                        tempString += "<br /> ----------------------------------------------------------- <br />";
                    }
                    if (j != 0) {
                        String temp1 = pairings.get(i).getFlightTimes().get(j).substring(10);

                        String temp2 = pairings.get(i).getFlightTimes().get(j - 1).substring(10);

                        if (!temp1.equals(temp2)) {
                            tempString += "<br /> ----------------------------------------------------------- <br />";
                        }

                        tempString += "<br />Flight Number: " + pairings.get(i).getFlightNumbers().get(j)
                                + "<br /> " + "Flight Route: "
                                + pairings.get(i).getFlightCities().get(j) + " " + "\u2708".toUpperCase() + " " + pairings.get(i).getFlightCities().get(j + 1)
                                + "<br />" + " Flight Schedules: " + pairings.get(i).getFlightTimes().get(j);

                    }

                }

            } else { // Not for disable pairing 
                tempDisable += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                        + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                        + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";
                tempDisable += "\u26F3 Pairing ID: [" + pairings.get(i).getId() + "] <br /> \uD83D\uDCC6 Pairing Start Date: " + pairings.get(i).getFDate() + "   " + "<br /> \uD83D\uDD50 Pairing Total Flight Hours: " + pairings.get(i).getFlightHour() + "<br />";

                for (int j = 0; j < pairings.get(i).getFlightNumbers().size(); j++) {
                    if (j == 0) {

                        tempDisable += "<br /> Flight Number: " + pairings.get(i).getFlightNumbers().get(j)
                                + "<br />" + "Flight Route: "
                                + pairings.get(i).getFlightCities().get(j) + " " + "\u2708".toUpperCase() + " " + pairings.get(i).getFlightCities().get(j + 1)
                                + "<br />" + " Flight Schedules: " + pairings.get(i).getFlightTimes().get(j);
                        tempString += "<br /> ----------------------------------------------------------- <br />";
                    }
                    if (j != 0) {
                        String temp1 = pairings.get(i).getFlightTimes().get(j).substring(10);

                        String temp2 = pairings.get(i).getFlightTimes().get(j - 1).substring(10);

                        if (!temp1.equals(temp2)) {
                            tempDisable += "<br /> ----------------------------------------------------------- <br />";
                        }

                        tempDisable += "<br />Flight Number: " + pairings.get(i).getFlightNumbers().get(j)
                                + "<br /> " + "Flight Route: "
                                + pairings.get(i).getFlightCities().get(j) + " " + "\u2708".toUpperCase() + " " + pairings.get(i).getFlightCities().get(j + 1)
                                + "<br />" + " Flight Schedules: " + pairings.get(i).getFlightTimes().get(j);

                    }

                }

            }

        }

        String[] sps = tempDisable.split("<br /> ----------------------------------------------------------- <br />");
        scheduleResultDisabled.add(sps[0]);
        for (int ii = 1; ii < sps.length; ii++) {
            scheduleResult.add(sps[ii]);
        }

        String[] spss = tempString.split("<br /> ----------------------------------------------------------- <br />");

        for (int ii = 0; ii < spss.length; ii++) {
            scheduleResult.add(spss[ii]);
        }

    }

    public void viewMembers() {
        messages = new ArrayList<String>();
        setCcs(team.getCabinCrews());
        setPilots(team.getPilots());
        messages.add("Pilots ");
        messages.add("Cabin Crew: ");
    }

    public void viewPairing(ActionEvent event) {
        FacesMessage message = null;
        pairings = new ArrayList<Pairing>();
        if (team == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Pairing is selected!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setPairings(team.getPairing());
        }
    }

    public void getCurrentPairing() {
        signInDate = new Date();
        pairings = new ArrayList<Pairing>();
        setPairings(team.getPairing());
        for (Pairing p : pairings) {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(signInDate);

            if (p.getFDate().equals(formattedDate)) {    //Bug -->> e.g 24/9/2015 23:00  -- 25/9/2015 00;30
                getFirstPairingSchedule(p);
                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(firstSchedule.getStartDate());

                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

                if (checkTime(formattedDate2, formattedDate1) > 60 || checkTime(formattedDate2, formattedDate1) < 0) {
                    setSelectPairing(null);
                } else {
                    setSelectPairing(p);
                }

            }
        }

    }

    public void getFirstPairingSchedule(Pairing p) {
        setFirstSchedule(crewSignInSessionBean.getFirstPairingSchedule(p));
    }

    public void viewSchedule(ActionEvent event) {
        schedules = new ArrayList<Schedule>();
        List<Schedule> schedules = team.getSchedule();
        for (Schedule s : schedules) {
            String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
            System.out.println("Schedule Date1: " + formattedDate1);

            String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);
            System.out.println("Schedule Date2: " + formattedDate2);

            if (s.getStartDate().after(signInDate) && checkTime(formattedDate1, formattedDate2) > 300) {
                schedules.add(s);
            }
        }
        // setSchedules(team.getSchedule());

    }

    public void scheduleSignIn(ActionEvent event) {
        FacesMessage message;

        

        if (selectedPairing==null) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Please select a pairing! ", "");
        } else {
            pairingID = selectedPairing.substring(selectedPairing.indexOf("[") + 1, selectedPairing.indexOf("]"));
            setSelectPairing(pairingSessionBean.getPairingByID(pairingID));

            String firstScheduleID = firstSchedule.getScheduleId() + "";

            if (selectPairing == null) {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Please sign in 1 hour before the flight!", "");
            } else {
                setMsg(crewSignInSessionBean.crewSignIn(loginManageBean.getEmployeeUserName(), team, firstScheduleID));
                if (msg.equals("Signed")) {
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "You Have Signed In This Schedule", "");
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Signed In", "");
                }

                System.out.println("++++++UserName: " + loginManageBean.getEmployeeUserName());
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public long checkTime(String time1, String time2) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
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

    /**
     * @return the loginManageBean
     */
    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManageBean loginManageBean) {
        this.loginManageBean = loginManageBean;
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
     * @return the crewSignInSessionBean
     */
    public CrewSignInSessionBeanLocal getCrewSignInSessionBean() {
        return crewSignInSessionBean;
    }

    /**
     * @param crewSignInSessionBean the crewSignInSessionBean to set
     */
    public void setCrewSignInSessionBean(CrewSignInSessionBeanLocal crewSignInSessionBean) {
        this.crewSignInSessionBean = crewSignInSessionBean;
    }

    /**
     * @return the ccs
     */
    public List<CabinCrew> getCcs() {
        return ccs;
    }

    /**
     * @param ccs the ccs to set
     */
    public void setCcs(List<CabinCrew> ccs) {
        this.ccs = ccs;
    }

    /**
     * @return the pilots
     */
    public List<Pilot> getPilots() {
        return pilots;
    }

    /**
     * @param pilots the pilots to set
     */
    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    /**
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * @return the schedules
     */
    public List<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * @param schedules the schedules to set
     */
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    /**
     * @return the pairings
     */
    public List<Pairing> getPairings() {
        return pairings;
    }

    /**
     * @param pairings the pairings to set
     */
    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }

    /**
     * @return the selectSchedule
     */
    public String getSelectSchedule() {
        return selectSchedule;
    }

    /**
     * @param selectSchedule the selectSchedule to set
     */
    public void setSelectSchedule(String selectSchedule) {
        this.selectSchedule = selectSchedule;
    }

    /**
     * @return the signInDate
     */
    public Date getSignInDate() {
        return signInDate;
    }

    /**
     * @param signInDate the signInDate to set
     */
    public void setSignInDate(Date signInDate) {
        this.signInDate = signInDate;
    }

    /**
     * @return the selectPairing
     */
    public Pairing getSelectPairing() {
        return selectPairing;
    }

    /**
     * @param selectPairing the selectPairing to set
     */
    public void setSelectPairing(Pairing selectPairing) {
        this.selectPairing = selectPairing;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the firstSchedule
     */
    public Schedule getFirstSchedule() {
        return firstSchedule;
    }

    /**
     * @param firstSchedule the firstSchedule to set
     */
    public void setFirstSchedule(Schedule firstSchedule) {
        this.firstSchedule = firstSchedule;
    }

    public List<String> getScheduleResult() {
        return scheduleResult;
    }

    public void setScheduleResult(List<String> scheduleResult) {
        this.scheduleResult = scheduleResult;
    }

    /**
     * @return the scheduleResultDisabled
     */
    public List<String> getScheduleResultDisabled() {
        return scheduleResultDisabled;
    }

    /**
     * @param scheduleResultDisabled the scheduleResultDisabled to set
     */
    public void setScheduleResultDisabled(List<String> scheduleResultDisabled) {
        this.scheduleResultDisabled = scheduleResultDisabled;
    }

    /**
     * @return the selectedPairing
     */
    public String getSelectedPairing() {
        return selectedPairing;
    }

    /**
     * @param selectedPairing the selectedPairing to set
     */
    public void setSelectedPairing(String selectedPairing) {
        this.selectedPairing = selectedPairing;
    }

    public String getPairingID() {
        return pairingID;
    }

    public void setPairingID(String pairingID) {
        this.pairingID = pairingID;
    }

}
