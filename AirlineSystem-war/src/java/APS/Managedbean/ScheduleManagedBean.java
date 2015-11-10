/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Aircraft;
import APS.Entity.Flight;
import APS.Entity.Schedule;
import APS.Session.FleetSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import FOS.Entity.Team;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yanlong
 */
@Named(value = "scheduleManagedBean")
@ManagedBean
@SessionScoped
public class ScheduleManagedBean {

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private FleetSessionBeanLocal fleetSessionBean;

    private Long scheduleId;
    private int duration;
    private Date newStartDate;
    private Date endDate;
    private Flight flight;
    private List<Flight> flights;
    private Team team;
    private Aircraft aircraft;

    private String flightNo;
    private String flightDays;
    private double flightDuration;
    private double basicFare;
    private List<Schedule> schedules;
    private List<Schedule> futureSchedules;
    private List<Schedule> pastSchedules;

    private List<Aircraft> aircraftlist;
    private List<Schedule> checklistSchedules;

    private String flightDaysString;

    FacesMessage message = null;

    private Schedule selectedSchedule;

    @PostConstruct
    public void retrieve() {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar currTime = Calendar.getInstance(tz);

        setFlights(flightSessionBean.retrieveActiveFlights());
        setSchedules(scheduleSessionBean.getScheduleAfter(currTime.getTime()));
        scheduleSessionBean.displayFlightDays(flights);
        setAircraftlist(fleetSessionBean.getReserveAircrafts("Stand-By"));
        setFutureSchedules(scheduleSessionBean.filterForFutureSchedules(schedules));
        setPastSchedules(scheduleSessionBean.filterForPastSchedules(schedules));
        checklistSchedules = new ArrayList();
        checklistSchedules = scheduleSessionBean.filterForPastSchedules(scheduleSessionBean.getSchedules());
//        try {
//            checklistSchedules.add(scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-10 02:00:00")));
//        } catch (ParseException ex) {
//            Logger.getLogger(ScheduleManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void addSchedules(ActionEvent event) {

        if (flightNo.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Flight Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (flightSessionBean.getFlight(flightNo) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such flight number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (duration == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Number of Months to be added!!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        scheduleSessionBean.addSchedules(duration, flightNo, true);
        setFlights(flightSessionBean.retrieveActiveFlights());
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedules Added Successfully!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void updateSchedules() {
        setSchedules(scheduleSessionBean.getSchedules());
        setFutureSchedules(scheduleSessionBean.filterForFutureSchedules(schedules));
        setPastSchedules(scheduleSessionBean.filterForPastSchedules(schedules));
    }

    public String deleteSchedule(Long scheduleId) {

        selectedSchedule = scheduleSessionBean.getSchedule(scheduleId);

        schedules.remove(selectedSchedule);
        
        //NOTE: PUT ALL THESE INTO SESSION BEAN!!!
        //remove the Flight linked to the Schedule
        List<Schedule> temp1 = selectedSchedule.getFlight().getSchedule();
        temp1.remove(selectedSchedule);
        selectedSchedule.getFlight().setSchedule(temp1);
        selectedSchedule.setFlight(null);

        //remove the Team linked to the Schedule
        selectedSchedule.setTeam(null);

        //remove the Aircraft linked to the Schedule
        List<Schedule> temp2 = selectedSchedule.getAircraft().getSchedules();
        temp2.remove(selectedSchedule);
        selectedSchedule.getAircraft().setSchedules(temp2);
        selectedSchedule.setAircraft(null);

        scheduleSessionBean.deleteSchedule(selectedSchedule.getScheduleId());
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedule deleted", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        selectedSchedule = null;
        setSchedules(scheduleSessionBean.getSchedules());
        updateSchedules();
        return "DeleteSchedule";

    }

    public void onRowEdit(RowEditEvent event) {
        Schedule edited = (Schedule) event.getObject();
        Long id = edited.getScheduleId();
        Schedule original = scheduleSessionBean.getSchedule(id);

        if (edited.getStartDate().equals(original.getStartDate()) && edited.getAircraft().getTailNo().equals(original.getAircraft().getTailNo())) {

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Cancelled", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            clear(original);
            return;
        }

        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar temp = Calendar.getInstance(tz);
        temp.setTime(original.getStartDate());
        temp.add(Calendar.HOUR, 1);

        if (edited.getStartDate().after(temp.getTime())) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "You cannot delay flight more than one hour!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            setSchedules(scheduleSessionBean.getSchedules());
            clear(original);
            return;
        }

        scheduleSessionBean.edit(edited, original);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedule Edited", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void onRowCancel(RowEditEvent event) {
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Cancelled", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getDateFormat(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm z");
        return df.format(date);
    }

    /*clear input after submit*/
    public void clear(Schedule original) {
        setAircraft(original.getAircraft());
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getNewStartDate() {
        return newStartDate;
    }

    public void setNewStartDate(Date newStartDate) {
        this.newStartDate = newStartDate;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightDays() {
        return flightDays;
    }

    public void setFlightDays(String flightDays) {
        this.flightDays = flightDays;
    }

    public double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public double getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(double basicFare) {
        this.basicFare = basicFare;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<Aircraft> getAircraftlist() {
        return aircraftlist;
    }

    public void setAircraftlist(List<Aircraft> aircraftlist) {
        this.aircraftlist = aircraftlist;
    }

    public Schedule getSelectedSchedule() {
        return selectedSchedule;
    }

    public void setSelectedSchedule(Schedule selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    public String getFlightDaysString() {
        return flightDaysString;
    }

    public void setFlightDaysString(String flightDaysString) {
        this.flightDaysString = flightDaysString;
    }

    public List<Schedule> getFutureSchedules() {
        return futureSchedules;
    }

    public void setFutureSchedules(List<Schedule> futureSchedules) {
        this.futureSchedules = futureSchedules;
    }

    public List<Schedule> getPastSchedules() {
        return pastSchedules;
    }

    public void setPastSchedules(List<Schedule> pastSchedules) {
        this.pastSchedules = pastSchedules;
    }

    public List<Schedule> getChecklistSchedules() {
        return checklistSchedules;
    }

    public void setChecklistSchedules(List<Schedule> checklistSchedules) {
        this.checklistSchedules = checklistSchedules;
    }

}
