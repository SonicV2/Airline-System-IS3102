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
import FOS.Entity.Pairing;
import FOS.Entity.Team;
import FOS.Session.CrewSignInSessionBeanLocal;
import java.util.Calendar;
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
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author smu
 */
@Named(value = "scheduleViewManagedBean")
@ManagedBean
@SessionScoped
public class ScheduleViewManagedBean {

    @EJB
    private CrewSignInSessionBeanLocal crewSignInSessionBean;

    @ManagedProperty(value = "#{loginManageBean}")
    private LoginManagedBean loginManageBean;

    private ScheduleModel eventModel;
    private ScheduleModel pilotEventModel;
    private ScheduleModel eventReservedCrewModel;
    private ScheduleModel eventReservedPilotModel;
    private Team team;

    /**
     * Creates a new instance of ScheduleViewManagedBean
     */
    public ScheduleViewManagedBean() {
    }

    @PostConstruct
    public void init() {

        getCCTeam();
        addEvent();
        addPilotEvent();
        addReservedCrewEvent();
        addReservedPilotEvent() ;
    }

    public void getCCTeam() {
        FacesMessage message = null;
        String crewName = getLoginManageBean().getEmployeeUserName();

        if (getCrewSignInSessionBean().getCCTeam(crewName) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not assigned to a team", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setTeam(getCrewSignInSessionBean().getCCTeam(crewName));

        }
    }

    public void addEvent() {
        FacesMessage message = null;
        eventModel = new DefaultScheduleModel();
        if (team == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not assigned to a team", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            String crewName = getLoginManageBean().getEmployeeUserName();
            CabinCrew crew = crewSignInSessionBean.getCabinCrew(crewName);
            if (crew == null) {
                return;
            }

            if (!crew.getPosition().contains("Reserved") && !crew.getSchedule().equals("N.A")) {
                String scheduleId = crew.getStatus().split("-")[0].split("Leave: ")[1];
                if (scheduleId.contains("/")) {
                    String temps[] = scheduleId.split("/");
                    for (int i = 0; i < temps.length; i++) {
                        Schedule sch = crewSignInSessionBean.getScheduleByID(temps[i]);
                        eventModel.addEvent(new DefaultScheduleEvent("Leave For This Schedule " + sch.getFlight().getFlightNo(), sch.getStartDate(), sch.getEndDate()));
                    }
                } else {
                    Schedule sch = crewSignInSessionBean.getScheduleByID(scheduleId);
                    eventModel.addEvent(new DefaultScheduleEvent("Leave For This Schedule " + sch.getFlight().getFlightNo(), sch.getStartDate(), sch.getEndDate()));
                }
            }
            List<Schedule> schedules = team.getSchedule();
            for (Schedule s : schedules) {

                eventModel.addEvent(new DefaultScheduleEvent("Flight Duty " + s.getFlight().getFlightNo(), s.getStartDate(), s.getEndDate()));

            }
        }
    }

    public void addPilotEvent() {
        FacesMessage message = null;
        pilotEventModel = new DefaultScheduleModel();
        if (team == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You are not assigned to a team", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            String crewName = getLoginManageBean().getEmployeeUserName();
            Pilot crew = crewSignInSessionBean.getPilot(crewName);
            if (crew == null) {
                return;
            }

            if (!crew.getPosition().contains("Reserved") && !crew.getSchedule().equals("N.A")) {
                String scheduleId = crew.getStatus().split("-")[0].split("Leave: ")[1];
                if (scheduleId.contains("/")) {
                    String temps[] = scheduleId.split("/");
                    for (int i = 0; i < temps.length; i++) {
                        Schedule sch = crewSignInSessionBean.getScheduleByID(temps[i]);
                        pilotEventModel.addEvent(new DefaultScheduleEvent("Leave For This Schedule " + sch.getFlight().getFlightNo(), sch.getStartDate(), sch.getEndDate()));
                    }
                } else {
                    Schedule sch = crewSignInSessionBean.getScheduleByID(scheduleId);
                    pilotEventModel.addEvent(new DefaultScheduleEvent("Leave For This Schedule " + sch.getFlight().getFlightNo(), sch.getStartDate(), sch.getEndDate()));
                }
            }
            List<Schedule> schedules = team.getSchedule();
            for (Schedule s : schedules) {

                pilotEventModel.addEvent(new DefaultScheduleEvent("Flight Duty " + s.getFlight().getFlightNo(), s.getStartDate(), s.getEndDate()));

            }
        }
    }

    public void addReservedCrewEvent() {
        FacesMessage message = null;
        eventReservedCrewModel = new DefaultScheduleModel();

        Date today = new Date();
        String crewName = getLoginManageBean().getEmployeeUserName();
        CabinCrew crew = crewSignInSessionBean.getCabinCrew(crewName);

        if (crew == null) {
            return;
        }

        if (crew.getPosition().contains("Reserved")) {
            String schedule = crew.getSchedule();
            if (schedule.equals("N.A")) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Schedule Assigned", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (crew.getSchedule().contains("&")) {

                    String[] yearMonths = crew.getSchedule().split("&");

                    for (int i = 0; i < yearMonths.length; i++) {
                        Calendar calendar = Calendar.getInstance();
                        Calendar calendar1 = Calendar.getInstance();
                        String oneWholeSchedule = yearMonths[i];
                        String yearMonth = oneWholeSchedule.split("-")[0];
                        int year = Integer.parseInt(yearMonth.substring(0, 4));
                        int month = Integer.parseInt(yearMonth.substring(4));
                        String schedulePattern = oneWholeSchedule.split("-")[1];

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, (month - 1));

                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, (month - 1));

                        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                        System.out.println("DDDFFFFF: " + days);

                        for (int j = 1; j <= days; j++) {
                            calendar.set(Calendar.DAY_OF_MONTH, j);
                            calendar.set(Calendar.HOUR, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.AM_PM, Calendar.AM);

                            calendar1.set(Calendar.DAY_OF_MONTH, j);
                            calendar1.set(Calendar.HOUR, 0);
                            calendar1.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.AM_PM, Calendar.PM);

                            if (Character.toString(schedulePattern.charAt(j - 1)).equals("1")) {

                                eventReservedCrewModel.addEvent(new DefaultScheduleEvent("On Standby Duty ", calendar.getTime(), calendar1.getTime()));
                            }
                        }

                    }
                } else {

                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();

                    int year = Integer.parseInt(crew.getSchedule().split("-")[0].substring(0, 4));
                    int month = Integer.parseInt(crew.getSchedule().split("-")[0].substring(4));
                    String pattern = crew.getSchedule().split("-")[1];

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, (month - 1));

                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, (month - 1));

                    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                    for (int i = 1; i <= days; i++) {
                        calendar.set(Calendar.DAY_OF_MONTH, i);
                        calendar.set(Calendar.HOUR, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.AM_PM, Calendar.AM);

                        calendar1.set(Calendar.DAY_OF_MONTH, i);
                        calendar1.set(Calendar.HOUR, 12);
                        calendar1.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.AM_PM, Calendar.PM);

                        if (Character.toString(pattern.charAt(i - 1)).equals("1")) {

                            eventReservedCrewModel.addEvent(new DefaultScheduleEvent("On Standby Duty ", calendar.getTime(), calendar1.getTime()));
                        }
                    }
                }
            }
        }
        if (crew.getPosition().contains("Reserved") && !crew.getStatus().equals("N.S")) {
            String scheduleId = crew.getStatus();
            if (scheduleId.contains("/")) {
                String[] schLists = scheduleId.split("/");
                for (int i = 0; i < schLists.length; i++) {
                    Schedule sch = crewSignInSessionBean.getScheduleByID(schLists[i]);
                    eventReservedCrewModel.addEvent(new DefaultScheduleEvent("On Flight Duty ", sch.getStartDate(), sch.getEndDate()));
                }
            } else {
                Schedule sch = crewSignInSessionBean.getScheduleByID(scheduleId);
                eventReservedCrewModel.addEvent(new DefaultScheduleEvent("On Flight Duty ", sch.getStartDate(), sch.getEndDate()));
            }
        }

    }

    
    
    public void addReservedPilotEvent() {
        FacesMessage message = null;
        eventReservedPilotModel = new DefaultScheduleModel();

        Date today = new Date();
        String crewName = getLoginManageBean().getEmployeeUserName();
        Pilot crew = crewSignInSessionBean.getPilot(crewName);

        if (crew == null) {
            return;
        }

        if (crew.getPosition().contains("Reserved")) {
            String schedule = crew.getSchedule();
            if (schedule.equals("N.A")) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Schedule Assigned", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                if (crew.getSchedule().contains("&")) {

                    String[] yearMonths = crew.getSchedule().split("&");

                    for (int i = 0; i < yearMonths.length; i++) {
                        Calendar calendar = Calendar.getInstance();
                        Calendar calendar1 = Calendar.getInstance();
                        String oneWholeSchedule = yearMonths[i];
                        String yearMonth = oneWholeSchedule.split("-")[0];
                        int year = Integer.parseInt(yearMonth.substring(0, 4));
                        int month = Integer.parseInt(yearMonth.substring(4));
                        String schedulePattern = oneWholeSchedule.split("-")[1];

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, (month - 1));

                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, (month - 1));

                        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                        System.out.println("DDDFFFFF: " + days);

                        for (int j = 1; j <= days; j++) {
                            calendar.set(Calendar.DAY_OF_MONTH, j);
                            calendar.set(Calendar.HOUR, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.AM_PM, Calendar.AM);

                            calendar1.set(Calendar.DAY_OF_MONTH, j);
                            calendar1.set(Calendar.HOUR, 0);
                            calendar1.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.AM_PM, Calendar.PM);

                            if (Character.toString(schedulePattern.charAt(j - 1)).equals("1")) {

                                eventReservedPilotModel.addEvent(new DefaultScheduleEvent("On Standby Duty ", calendar.getTime(), calendar1.getTime()));
                            }
                        }

                    }
                } else {

                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();

                    int year = Integer.parseInt(crew.getSchedule().split("-")[0].substring(0, 4));
                    int month = Integer.parseInt(crew.getSchedule().split("-")[0].substring(4));
                    String pattern = crew.getSchedule().split("-")[1];

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, (month - 1));

                    calendar1.set(Calendar.YEAR, year);
                    calendar1.set(Calendar.MONTH, (month - 1));

                    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                    for (int i = 1; i <= days; i++) {
                        calendar.set(Calendar.DAY_OF_MONTH, i);
                        calendar.set(Calendar.HOUR, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.AM_PM, Calendar.AM);

                        calendar1.set(Calendar.DAY_OF_MONTH, i);
                        calendar1.set(Calendar.HOUR, 12);
                        calendar1.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.AM_PM, Calendar.PM);

                        if (Character.toString(pattern.charAt(i - 1)).equals("1")) {

                            eventReservedPilotModel.addEvent(new DefaultScheduleEvent("On Standby Duty ", calendar.getTime(), calendar1.getTime()));
                        }
                    }
                }
            }
        }
        if (crew.getPosition().contains("Reserved") && !crew.getStatus().equals("N.S")) {
            String scheduleId = crew.getStatus();
            if (scheduleId.contains("/")) {
                String[] schLists = scheduleId.split("/");
                for (int i = 0; i < schLists.length; i++) {
                    Schedule sch = crewSignInSessionBean.getScheduleByID(schLists[i]);
                    eventReservedPilotModel.addEvent(new DefaultScheduleEvent("On Flight Duty ", sch.getStartDate(), sch.getEndDate()));
                }
            } else {
                Schedule sch = crewSignInSessionBean.getScheduleByID(scheduleId);
                eventReservedPilotModel.addEvent(new DefaultScheduleEvent("On Flight Duty ", sch.getStartDate(), sch.getEndDate()));
            }
        }

    }

    /**
     * @return the eventModel
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    /**
     * @param eventModel the eventModel to set
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
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
     * @return the eventReservedCrewModel
     */
    public ScheduleModel getEventReservedCrewModel() {
        return eventReservedCrewModel;
    }

    /**
     * @param eventReservedCrewModel the eventReservedCrewModel to set
     */
    public void setEventReservedCrewModel(ScheduleModel eventReservedCrewModel) {
        this.eventReservedCrewModel = eventReservedCrewModel;
    }

    /**
     * @return the pilotEventModel
     */
    public ScheduleModel getPilotEventModel() {
        return pilotEventModel;
    }

    /**
     * @param pilotEventModel the pilotEventModel to set
     */
    public void setPilotEventModel(ScheduleModel pilotEventModel) {
        this.pilotEventModel = pilotEventModel;
    }

    /**
     * @return the eventReservedPilotModel
     */
    public ScheduleModel getEventReservedPilotModel() {
        return eventReservedPilotModel;
    }

    /**
     * @param eventReservedPilotModel the eventReservedPilotModel to set
     */
    public void setEventReservedPilotModel(ScheduleModel eventReservedPilotModel) {
        this.eventReservedPilotModel = eventReservedPilotModel;
    }

}
