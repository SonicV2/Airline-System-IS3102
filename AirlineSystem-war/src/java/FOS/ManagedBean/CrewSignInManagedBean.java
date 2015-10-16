/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Entity.Pilot;
import CI.Managedbean.LoginManagedBean;
import CI.Session.EmployeeSessionBeanLocal;
import FOS.Entity.Pairing;
import FOS.Entity.Team;
import FOS.Session.CrewSignInSessionBeanLocal;
import FOS.Session.PairingSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
import static java.util.Comparator.comparing;

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
    private LoginManagedBean loginManageBean;

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

    private List<String> scheduleResult;//not enabled for crew signin
    private List<String> scheduleResultDisabled; // display for crew signin
    private List<String> reservedCrewScheduleResult; // display for reserved crew signin
    private List<String> reservedCrewScheduleResultDisabled; // not enabled schedule for reserved crew 

    private String selectedPairing; // select pairingID from webpage

    private String pairingID;

    private List<Schedule> validSchedulesForSubmit;
    private List<Schedule> inValidSchedulesForSubmit;

    private List<Schedule> validPilotSchedulesForSubmit;
    private List<Schedule> inValidPilotSchedulesForSubmit;

    private String selectLeaveSchedule;
    private List<String> leaveReasons = new ArrayList<String>();
    private String selectLeaveReason;

    private String reservedCrewSelectSignInSchedule;

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
            leaveReasons.add("Medical Issue");
            leaveReasons.add("Emergence Calls");
            leaveReasons.add("Others");

            setTeam(getCrewSignInSessionBean().getCCTeam(crewName));

            viewMembers();
            getCurrentPairing();
            scheduleNew();
            signInScheduleForReservedCrew();
            viewValidSchedule();
            viewPilotValidSchedule();

        }
    }

    public void scheduleNew() {
        signInDate = new Date();
        String tempString = "";
        String tempDisable = "";

        scheduleResult = new ArrayList<String>();
        scheduleResultDisabled = new ArrayList<String>();

        scheduleResult.clear();
        scheduleResultDisabled.clear();

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

    public void signInScheduleForReservedCrew() {
        signInDate = new Date();
        String tempString = "";
        String tempDisable = "";
        String crewName = loginManageBean.getEmployeeUserName();

        CabinCrew crew = crewSignInSessionBean.getCabinCrew(crewName);

        if (crew == null) {// for reason if the crew is pilot
            return;
        }

        reservedCrewScheduleResult = new ArrayList<String>();
        reservedCrewScheduleResultDisabled = new ArrayList<String>();

        if (crew.getPosition().contains("Reserved") && !crew.getStatus().equals("N.S")) {
            String scheduleId = crew.getStatus();
            if (scheduleId.contains("/")) {
                String[] schLists = scheduleId.split("/");
                for (int i = 0; i < schLists.length; i++) {
                    Schedule sch = crewSignInSessionBean.getScheduleByID(schLists[i]);
                    String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sch.getStartDate());
                    String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

                    String date1 = new SimpleDateFormat("dd/MM/yyyy").format(sch.getStartDate());
                    String date2 = new SimpleDateFormat("dd/MM/yyyy").format(signInDate);

                    if (date1.equals(date2) && checkTime(formattedDate2, formattedDate1) > 60 || checkTime(formattedDate2, formattedDate1) < 0) {
                        tempString += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                                + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                                + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";

                        tempString += "\u26F3 Flight Number: [" + sch.getFlight().getFlightNo() + "] <br /> \uD83D\uDCC6 Schedule Start Date: " + sch.getStartDate() + "   "
                                + "<br /> \uD83D\uDD50 Schedule End Date: " + sch.getEndDate() + "<br />"
                                + "<br /> \uD83D\uDD50 Flight Origin City: " + sch.getFlight().getRoute().getOriginCity() + "<br />"
                                + "<br /> \uD83D\uDD50 Flight Destination City " + sch.getFlight().getRoute().getDestinationCity() + "<br />";

                    } else {
                        tempDisable += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                                + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                                + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";
                        tempDisable += "\u26F3 Flight Number: [" + sch.getFlight().getFlightNo() + "] <br /> \uD83D\uDCC6 Schedule Start Date: " + sch.getStartDate() + "   "
                                + "<br /> \uD83D\uDD50 Schedule End Date: " + sch.getEndDate() + "<br />"
                                + "<br /> \uD83D\uDD50 Flight Origin City: " + sch.getFlight().getRoute().getOriginCity() + "<br />"
                                + "<br /> \uD83D\uDD50 Flight Destination City " + sch.getFlight().getRoute().getDestinationCity() + "<br />";

                    }
                }

                if (!tempString.equals("")) {
                    String[] tempEnable = tempString.split("<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />");
                    for (int i = 1; i < tempEnable.length; i++) {
                        reservedCrewScheduleResult.add(tempEnable[i]);
                    }

                }
                if (!tempDisable.equals("")) {
                    String[] tDisable = tempDisable.split("<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />");
                    for (int i = 1; i < tDisable.length; i++) {
                        reservedCrewScheduleResultDisabled.add(tDisable[i]);
                    }
                }
//                reservedCrewScheduleResult.add(tempString);
//                reservedCrewScheduleResultDisabled.add(tempDisable);

            } else {
                Schedule sch = crewSignInSessionBean.getScheduleByID(scheduleId);

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sch.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

                String date1 = new SimpleDateFormat("dd/MM/yyyy").format(sch.getStartDate());
                String date2 = new SimpleDateFormat("dd/MM/yyyy").format(signInDate);

                if (date1.equals(date2) && checkTime(formattedDate2, formattedDate1) > 60 || checkTime(formattedDate2, formattedDate1) < 0) {
                    tempString += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";

                    tempString += "\u26F3 Flight Number: [" + sch.getFlight().getFlightNo() + "] <br /> \uD83D\uDCC6 Schedule Start Date: " + sch.getStartDate() + "   "
                            + "<br /> \uD83D\uDD50 Schedule End Date: " + sch.getEndDate() + "<br />"
                            + "<br /> \uD83D\uDD50 Flight Origin City: " + sch.getFlight().getRoute().getOriginCity() + "<br />"
                            + "<br /> \uD83D\uDD50 Flight Destination City " + sch.getFlight().getRoute().getDestinationCity() + "<br />";
                    reservedCrewScheduleResult.add(tempString);

                } else {
                    tempDisable += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";
                    tempDisable += "\u26F3 Flight Number: [" + sch.getFlight().getFlightNo() + "] <br /> \uD83D\uDCC6 Schedule Start Date: " + sch.getStartDate() + "   "
                            + "<br /> \uD83D\uDD50 Schedule End Date: " + sch.getEndDate() + "<br />"
                            + "<br /> \uD83D\uDD50 Flight Origin City: " + sch.getFlight().getRoute().getOriginCity() + "<br />"
                            + "<br /> \uD83D\uDD50 Flight Destination City " + sch.getFlight().getRoute().getDestinationCity() + "<br />";
                    reservedCrewScheduleResultDisabled.add(tempDisable);
                }
            }
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

            String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

            if (s.getStartDate().after(signInDate) && checkTime(formattedDate1, formattedDate2) > 300) {
                schedules.add(s);
            }
        }

    }

    public void scheduleSignIn(ActionEvent event) {
        FacesMessage message;

        if (selectedPairing == null) {
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
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    // for crew to see which schedules could submit leave
    public void viewValidSchedule() {

        signInDate = new Date();

        String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

        validSchedulesForSubmit = new ArrayList<Schedule>();
        inValidSchedulesForSubmit = new ArrayList<Schedule>();

        FacesMessage message = null;
        String crewName = loginManageBean.getEmployeeUserName();
        String signInScheduleId = "";
        CabinCrew crew = crewSignInSessionBean.getCabinCrew(crewName);

        if (crew == null) { // for reason if the crew is pilot
            return;
        }

        if (crew.getTeam() != null && (crew.getPosition().contains("Reserved") || crew.getStatus().equals("N.S"))) {

            if (crew.getSignInStatus().contains("SignIn")) {

                signInScheduleId = crew.getSignInStatus().substring(6);
            }

            List<Schedule> schedules = team.getSchedule();

            for (Schedule s : schedules) {
                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());

                if (s.getFlight().getRoute().getOriginCountry().toUpperCase().trim().equals(crew.getOrganizationUnit().getLocation().trim()) && checkTime(formattedDate2, formattedDate1) > 120
                        && !s.getScheduleId().toString().equals(signInScheduleId)) {
                    validSchedulesForSubmit.add(s);

                }
            }

        } else {

            String submitScheduleId = crew.getStatus().split("-")[0].split("Leave: ")[1];

            List<Schedule> schedules = team.getSchedule();

            List<Schedule> tempS = team.getSchedule();

            if (submitScheduleId.contains("/")) {
                String[] temps = submitScheduleId.split("/");

                for (int i = 0; i < temps.length; i++) {
                    Iterator<Schedule> it = tempS.iterator();

                    while (it.hasNext()) {
                        Schedule sc = it.next();
                        if (sc.getScheduleId().toString().equals(temps[i])) {
                            inValidSchedulesForSubmit.add(sc);
                            //it.remove();
                        }
                    }

                }

                for (Schedule s : tempS) {

                    String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                    if (s.getFlight().getRoute().getOriginCountry().toUpperCase().trim().equals(crew.getOrganizationUnit().getLocation().trim()) && checkTime(formattedDate2, formattedDate1) > 120
                            && !inValidSchedulesForSubmit.contains(s) && !s.getScheduleId().toString().equals(signInScheduleId)) {

                        validSchedulesForSubmit.add(s);

                    }
                }

            } else {

                for (Schedule s : schedules) {
                    String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());

                    if (s.getFlight().getRoute().getOriginCountry().toUpperCase().trim().equals(crew.getOrganizationUnit().getLocation().trim()) && checkTime(formattedDate2, formattedDate1) > 120
                            && !s.getScheduleId().toString().equals(submitScheduleId) && !s.getScheduleId().toString().equals(signInScheduleId)) {
                        validSchedulesForSubmit.add(s);

                    }

                    if (s.getScheduleId().toString().equals(submitScheduleId)) {
                        inValidSchedulesForSubmit.add(s);
                    }
                }

            }
        }

    }

    // for pilot to see which schedules could submit leave
    public void viewPilotValidSchedule() {

        signInDate = new Date();

        String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

        validPilotSchedulesForSubmit = new ArrayList<Schedule>();
        inValidPilotSchedulesForSubmit = new ArrayList<Schedule>();

        FacesMessage message = null;
        String crewName = loginManageBean.getEmployeeUserName();
        String signInScheduleId = "";
        Pilot crew = crewSignInSessionBean.getPilot(crewName);

        if (crew == null) { // for crew is cabin crew
            return;
        }

        if (crew.getTeam() != null && (crew.getPosition().contains("Reserved") || crew.getStatus().equals("N.S"))) {

            if (crew.getSignInStatus().contains("SignIn")) {

                signInScheduleId = crew.getSignInStatus().substring(6);
            }

            List<Schedule> schedules = team.getSchedule();

            for (Schedule s : schedules) {
                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());

                if (s.getFlight().getRoute().getOriginCountry().toUpperCase().trim().equals(crew.getOrganizationUnit().getLocation().trim()) && checkTime(formattedDate2, formattedDate1) > 120
                        && !s.getScheduleId().toString().equals(signInScheduleId)) {
                    validPilotSchedulesForSubmit.add(s);

                }
            }

        } else {

            String submitScheduleId = crew.getStatus().split("-")[0].split("Leave: ")[1];

            List<Schedule> schedules = team.getSchedule();

            List<Schedule> tempS = team.getSchedule();

            if (submitScheduleId.contains("/")) {
                String[] temps = submitScheduleId.split("/");

                for (int i = 0; i < temps.length; i++) {
                    Iterator<Schedule> it = tempS.iterator();

                    while (it.hasNext()) {
                        Schedule sc = it.next();
                        if (sc.getScheduleId().toString().equals(temps[i])) {
                            inValidPilotSchedulesForSubmit.add(sc);
                            //it.remove();
                        }
                    }

                }

                for (Schedule s : tempS) {

                    String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                    if (s.getFlight().getRoute().getOriginCountry().toUpperCase().trim().equals(crew.getOrganizationUnit().getLocation().trim()) && checkTime(formattedDate2, formattedDate1) > 120
                            && !inValidPilotSchedulesForSubmit.contains(s) && !s.getScheduleId().toString().equals(signInScheduleId)) {

                        validPilotSchedulesForSubmit.add(s);

                    }
                }

            } else {

                for (Schedule s : schedules) {
                    String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());

                    if (s.getFlight().getRoute().getOriginCountry().toUpperCase().trim().equals(crew.getOrganizationUnit().getLocation().trim()) && checkTime(formattedDate2, formattedDate1) > 120
                            && !s.getScheduleId().toString().equals(submitScheduleId) && !s.getScheduleId().toString().equals(signInScheduleId)) {
                        validPilotSchedulesForSubmit.add(s);

                    }

                    if (s.getScheduleId().toString().equals(submitScheduleId)) {
                        inValidPilotSchedulesForSubmit.add(s);
                    }
                }

            }
        }

    }

    // to give if subsequent schedules will return to crew base
    public List<Schedule> checkSchedule(String scheduleId) {
        List<Schedule> schs = new ArrayList<Schedule>();
        Schedule submitSchedule = crewSignInSessionBean.getScheduleByID(scheduleId);

        String submitCity = submitSchedule.getFlight().getRoute().getOriginCity();
        String submitFlightNumber = submitSchedule.getFlight().getFlightNo();

        List<Pairing> teamPairings = team.getPairing();

        String scheduleDay = new SimpleDateFormat("dd").format(submitSchedule.getStartDate());
        String scheduleMonth = new SimpleDateFormat("MM").format(submitSchedule.getStartDate());
        String scheduleYear = new SimpleDateFormat("yyyy").format(submitSchedule.getStartDate());

        Pairing targetPairing = new Pairing();

        //get the pairing which crew submit the leave
        for (Pairing p : teamPairings) {
            String pMonth = p.getFDate().split("/")[1];
            String pYear = p.getFDate().split("/")[2];
            if (pMonth.equals(scheduleMonth) && pYear.equals(scheduleYear)) {
                targetPairing = p;
            }
        }

        System.out.println("::::::::::::::Pairing: "+ targetPairing.getFlightTimes() +"::::::::::::Flight Number: "+targetPairing.getFlightNumbers());
        for (int i = 0; i < targetPairing.getFlightNumbers().size(); i++) {
            if (targetPairing.getFlightNumbers().size() == 1) {
                schs.add(crewSignInSessionBean.getScheduleBy(submitFlightNumber, targetPairing.getFDate()));
            } //for case that the select schedule is the last one in the pairing --> too complicate need to consider the next month pairing
            //           else if(targetPairing.getFlightNumbers().get(targetPairing.getFlightNumbers().size()-1).equals(submitFlightNumber)){
            //               
            //               schs.add(crewSignInSessionBean.getScheduleBy(submitFlightNumber, targetPairing.getFDate()));
            //           }
            else {
                //String tDate1 = targetPairing.getFlightTimes().get(i + 1).substring(targetPairing.getFlightTimes().get(i).indexOf("(") + 1, targetPairing.getFlightTimes().get(i).indexOf(")"));

                if (targetPairing.getFlightNumbers().get(i).equals(submitFlightNumber.substring(2))) {

                    if (targetPairing.getFlightCities().get(i + 2).equals(submitCity)) {

                        String tDate = targetPairing.getFlightTimes().get(i + 1).substring(targetPairing.getFlightTimes().get(i).indexOf("(") + 1, targetPairing.getFlightTimes().get(i).indexOf(")"));

                        int tDay = Integer.parseInt(tDate.split("/")[0]);
                        int tMonth = Integer.parseInt(tDate.split("/")[1]);
                        int tYear = Integer.parseInt(tDate.split("/")[2]);

                        int submitDay = Integer.parseInt(scheduleDay);
                        int submitMonth = Integer.parseInt(scheduleMonth);
                        int submitYear = Integer.parseInt(scheduleYear);

                        if (tYear == submitYear && tMonth == submitMonth && tDay == submitDay) {

                            schs.add(crewSignInSessionBean.getScheduleBy(targetPairing.getFlightNumbers().get(i + 1), tDate));
                            break; // once found the schedule returns to base, stop searching
                        } else if (tYear == submitYear && tMonth == submitMonth && tDay > submitDay) {
                            schs.add(crewSignInSessionBean.getScheduleBy(targetPairing.getFlightNumbers().get(i + 1), tDate));
                            break;

                        }

                    } else {

                        String tDate = targetPairing.getFlightTimes().get(i).substring(targetPairing.getFlightTimes().get(i).indexOf("(") + 1, targetPairing.getFlightTimes().get(i).indexOf(")"));

                        schs.add(crewSignInSessionBean.getScheduleBy(submitFlightNumber, tDate));
                    }
                }
            }

        }
        return schs;
    }

    public void submitLeave(ActionEvent event) {

        FacesMessage message = null;
        String crewName = loginManageBean.getEmployeeUserName();
        CabinCrew cc = crewSignInSessionBean.getCabinCrew(crewName);
        if (cc != null) {
            String msg = "";
            List<Schedule> hiddenSchedules = checkSchedule(selectLeaveSchedule);

            String scheduleLists = selectLeaveSchedule;
            if (hiddenSchedules.isEmpty()) {
                msg = crewSignInSessionBean.submitLeave(crewName, selectLeaveReason, scheduleLists);
            } else {
                for (Schedule s : hiddenSchedules) {
                    scheduleLists += "/" + s.getScheduleId().toString();
                }
                msg = crewSignInSessionBean.submitLeave(crewName, selectLeaveReason, scheduleLists);
            }

            if (msg.equals("done")) {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "You have submit leave for this schedule! ", "");
            } else {
                setSelectLeaveSchedule("");
                setSelectLeaveReason("");
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Submit Successfully ", "");
            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void submitPilotLeave(ActionEvent event) {

        FacesMessage message = null;
        String crewName = loginManageBean.getEmployeeUserName();
        Pilot pilot = crewSignInSessionBean.getPilot(crewName);
        if (pilot != null) {
            String msg = "";
            List<Schedule> hiddenSchedules = checkSchedule(selectLeaveSchedule);

            String scheduleLists = selectLeaveSchedule;
            if (hiddenSchedules.isEmpty()) {
                msg = crewSignInSessionBean.submitPilotLeave(crewName, selectLeaveReason, scheduleLists);
            } else {
                for (Schedule s : hiddenSchedules) {
                    scheduleLists += "/" + s.getScheduleId().toString();
                }
                msg = crewSignInSessionBean.submitPilotLeave(crewName, selectLeaveReason, scheduleLists);
            }

            if (msg.equals("done")) {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "You have submit leave for this schedule! ", "");
            } else {
                setSelectLeaveSchedule("");
                setSelectLeaveReason("");
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Submit Successfully ", "");
            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void refresh(ActionEvent event) {
        viewValidSchedule();
        viewPilotValidSchedule();
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
    public LoginManagedBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManagedBean loginManageBean) {
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

    /**
     * @return the validSchedulesForSubmit
     */
    public List<Schedule> getValidSchedulesForSubmit() {
        return validSchedulesForSubmit;
    }

    /**
     * @param validSchedulesForSubmit the validSchedulesForSubmit to set
     */
    public void setValidSchedulesForSubmit(List<Schedule> validSchedulesForSubmit) {
        this.validSchedulesForSubmit = validSchedulesForSubmit;
    }

    /**
     * @return the selectLeaveSchedule
     */
    public String getSelectLeaveSchedule() {
        return selectLeaveSchedule;
    }

    /**
     * @param selectLeaveSchedule the selectLeaveSchedule to set
     */
    public void setSelectLeaveSchedule(String selectLeaveSchedule) {
        this.selectLeaveSchedule = selectLeaveSchedule;
    }

    /**
     * @return the leaveReasons
     */
    public List<String> getLeaveReasons() {
        return leaveReasons;
    }

    /**
     * @param leaveReasons the leaveReasons to set
     */
    public void setLeaveReasons(List<String> leaveReasons) {
        this.leaveReasons = leaveReasons;
    }

    /**
     * @return the selectLeaveReason
     */
    public String getSelectLeaveReason() {
        return selectLeaveReason;
    }

    /**
     * @param selectLeaveReason the selectLeaveReason to set
     */
    public void setSelectLeaveReason(String selectLeaveReason) {
        this.selectLeaveReason = selectLeaveReason;
    }

    /**
     * @return the inValidSchedulesForSubmit
     */
    public List<Schedule> getInValidSchedulesForSubmit() {
        return inValidSchedulesForSubmit;
    }

    /**
     * @param inValidSchedulesForSubmit the inValidSchedulesForSubmit to set
     */
    public void setInValidSchedulesForSubmit(List<Schedule> inValidSchedulesForSubmit) {
        this.inValidSchedulesForSubmit = inValidSchedulesForSubmit;
    }

    /**
     * @return the reservedCrewScheduleResult
     */
    public List<String> getReservedCrewScheduleResult() {
        return reservedCrewScheduleResult;
    }

    /**
     * @param reservedCrewScheduleResult the reservedCrewScheduleResult to set
     */
    public void setReservedCrewScheduleResult(List<String> reservedCrewScheduleResult) {
        this.reservedCrewScheduleResult = reservedCrewScheduleResult;
    }

    /**
     * @return the reservedCrewScheduleResultDisabled
     */
    public List<String> getReservedCrewScheduleResultDisabled() {
        return reservedCrewScheduleResultDisabled;
    }

    /**
     * @param reservedCrewScheduleResultDisabled the
     * reservedCrewScheduleResultDisabled to set
     */
    public void setReservedCrewScheduleResultDisabled(List<String> reservedCrewScheduleResultDisabled) {
        this.reservedCrewScheduleResultDisabled = reservedCrewScheduleResultDisabled;
    }

    /**
     * @return the reservedCrewSelectSignInSchedule
     */
    public String getReservedCrewSelectSignInSchedule() {
        return reservedCrewSelectSignInSchedule;
    }

    /**
     * @param reservedCrewSelectSignInSchedule the
     * reservedCrewSelectSignInSchedule to set
     */
    public void setReservedCrewSelectSignInSchedule(String reservedCrewSelectSignInSchedule) {
        this.reservedCrewSelectSignInSchedule = reservedCrewSelectSignInSchedule;
    }

    /**
     * @return the validPilotSchedulesForSubmit
     */
    public List<Schedule> getValidPilotSchedulesForSubmit() {
        return validPilotSchedulesForSubmit;
    }

    /**
     * @param validPilotSchedulesForSubmit the validPilotSchedulesForSubmit to
     * set
     */
    public void setValidPilotSchedulesForSubmit(List<Schedule> validPilotSchedulesForSubmit) {
        this.validPilotSchedulesForSubmit = validPilotSchedulesForSubmit;
    }

    /**
     * @return the inValidPilotSchedulesForSubmit
     */
    public List<Schedule> getInValidPilotSchedulesForSubmit() {
        return inValidPilotSchedulesForSubmit;
    }

    /**
     * @param inValidPilotSchedulesForSubmit the inValidPilotSchedulesForSubmit
     * to set
     */
    public void setInValidPilotSchedulesForSubmit(List<Schedule> inValidPilotSchedulesForSubmit) {
        this.inValidPilotSchedulesForSubmit = inValidPilotSchedulesForSubmit;
    }

}
