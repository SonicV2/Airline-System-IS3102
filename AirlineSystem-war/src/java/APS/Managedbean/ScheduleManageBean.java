/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import FOS.Entity.Team;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 *
 * @author Yanlong
 */
@Named(value = "scheduleManageBean")
@ManagedBean
@SessionScoped
public class ScheduleManageBean {

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
    
    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    private Long scheduleId;
    private Date startDate;
    private Date endDate;
    private Flight flight;
    private List<Flight> flights;
    private Team team;
    
    private String flightNo;
    private String flightDays;
    private double flightDuration;
    private double basicFare;
    private List<Schedule> schedules;
    
    @PostConstruct
    public void retrieve(){
        setFlights(flightSessionBean.getFlights());
        setSchedules(scheduleSessionBean.getSchedules());
    }    
    
    public void addSchedule(ActionEvent event){
        scheduleSessionBean.addSchedule(startDate, flightNo);
    }
    
    public void deleteSchedule(ActionEvent event){
        scheduleSessionBean.deleteSchedule(scheduleId);
    }    
    
    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

}
