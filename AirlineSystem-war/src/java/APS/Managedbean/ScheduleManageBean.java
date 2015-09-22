/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import APS.Session.FleetSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import FOS.Entity.Team;
import java.util.Date;
import java.util.List;
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
@Named(value = "scheduleManageBean")
@ManagedBean
@SessionScoped
public class ScheduleManageBean {

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
    
    @EJB
    private FlightSessionBeanLocal flightSessionBean;
    
    @EJB
    private FleetSessionBeanLocal fleetSessionBean;

    private Long scheduleId;
    private Date startDate;
    private Date newStartDate;
    private Date endDate;
    private Flight flight;
    private List<Flight> flights;
    private Team team;
    
    private String flightNo;
    private String flightDays;
    private double flightDuration;
    private double basicFare;
    private List<Schedule> schedules;
    
    private String flightDaysString;
    
    FacesMessage message = null;
    
    private Schedule selectedSchedule;
    
    @PostConstruct
    public void retrieve(){
        setFlights(flightSessionBean.getFlights());
        setSchedules(scheduleSessionBean.getSchedules());
        scheduleSessionBean.changeFlightDays(flights);
        
    }
    
    public void addSchedule(ActionEvent event){
        
        if (flightNo.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Flight Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (startDate == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter new date and time of flight!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (flightSessionBean.getFlight(flightNo) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such flight number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        for (int i=0; i<flightSessionBean.getFlight(flightNo).getSchedule().size(); i++) {
        if (flightSessionBean.getFlight(flightNo).getSchedule().get(i).getStartDate().equals(startDate)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Schedule already exists!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
            }
        
        scheduleSessionBean.addSchedule(startDate, flightNo);
    }
    
    public void deleteSchedule(ActionEvent event){
        schedules.remove(selectedSchedule);
        
        //remove the Flight linked to the Schedule
        List<Schedule> temp1 = selectedSchedule.getFlight().getSchedule();
        temp1.remove(selectedSchedule);
        selectedSchedule.getFlight().setSchedule(temp1);
        selectedSchedule.setFlight(null);
        
        //remove the Team linked to the Schedule
//        List<Schedule> temp = selectedSchedule.getTeam().getSchedule();
//        temp.remove(selectedSchedule);
//        selectedSchedule.getTeam().setSchedule(temp);
//        selectedSchedule.setTeam(null);

        scheduleSessionBean.deleteSchedule(selectedSchedule.getScheduleId());
        selectedSchedule = null;
        
    }
    
    public void onRowEdit(RowEditEvent event) {
        Schedule edited = (Schedule)event.getObject();
        Long id = edited.getScheduleId();
        Schedule original= scheduleSessionBean.getSchedule(scheduleId);
        if(!edited.equals(original)){
            scheduleSessionBean.edit(edited);
            FacesMessage msg = new FacesMessage("Schedule Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else{
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
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

}
