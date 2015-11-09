/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import APS.Entity.Schedule;
import CI.Entity.EmployeeCalendar;
import CI.Session.EmployeeCalendarSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author smu
 */
@Named(value = "employeeCalendarManagedBean")
@SessionScoped
@ManagedBean
public class EmployeeCalendarManagedBean {

    @EJB
    private EmployeeCalendarSessionBeanLocal employeeCalendarSessionBean;

    @ManagedProperty(value = "#{loginManagedBean}")
    private LoginManagedBean loginManageBean;

    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();

    private List<EmployeeCalendar> calendars;

    private EmployeeCalendar employeeCalendar; // to retrieve speific event by eventId;

    private ScheduleEvent selectedEvent = new DefaultScheduleEvent();
    
    private EmployeeCalendar selectedCalendar;

    public EmployeeCalendarManagedBean() {
    }

    @PostConstruct
    public void retrieveCalendar() {
        calendars = new ArrayList<EmployeeCalendar>();

        setCalendars(employeeCalendarSessionBean.retrieveCalendar(getLoginManageBean().getEmployeeUserName()));
        addExistingCalendar();
    }

    public void addExistingCalendar() {
        FacesMessage message = null;
        eventModel = new DefaultScheduleModel();

        if (calendars==null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No activities available", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            for (EmployeeCalendar e : calendars) {
                eventModel.addEvent(new DefaultScheduleEvent(e.getTitle(), e.getStartDate(), e.getEndDate()));
            }
        }

    }

    public void addEvent(ActionEvent actionEvent) {
        if (event.getId() == null) {
            eventModel.addEvent(event);

            employeeCalendarSessionBean.addCalendar(event.getId(), getLoginManageBean().getEmployeeUserName(), event.getStartDate(), event.getEndDate(), event.getTitle());
        } else {

            employeeCalendarSessionBean.addCalendar(event.getId(), getLoginManageBean().getEmployeeUserName(), event.getStartDate(), event.getEndDate(), event.getTitle());

            employeeCalendarSessionBean.removeCalendar(getLoginManageBean().getEmployeeUserName(), selectedCalendar.getStartDate(), selectedCalendar.getEndDate(), selectedCalendar.getTitle());

            eventModel.updateEvent(event);
        }

        event = new DefaultScheduleEvent();
    }

    public void deleteCalendar(ActionEvent eventAction) {

        employeeCalendarSessionBean.removeCalendar(getLoginManageBean().getEmployeeUserName(), selectedCalendar.getStartDate(), selectedCalendar.getEndDate(), selectedCalendar.getTitle());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete Successfully", "");

        addMessage(message);
        eventModel.deleteEvent(event);
        eventModel.updateEvent(event);
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();

        selectedCalendar = employeeCalendarSessionBean.retrieveCalendarBy(event.getStartDate(), event.getEndDate(), event.getTitle());

    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());

    }

    public void onEventMove(ScheduleEntryMoveEvent event1) {
        employeeCalendarSessionBean.updateCalendar(getLoginManageBean().getEmployeeUserName(), selectedEvent.getId(), selectedEvent.getStartDate(), selectedEvent.getEndDate(), selectedEvent.getTitle());

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event1.getDayDelta() + ", Minute delta:" + event1.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
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
     * @return the event
     */
    public ScheduleEvent getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    /**
     * @return the calendars
     */
    public List<EmployeeCalendar> getCalendars() {
        return calendars;
    }

    /**
     * @param calendars the calendars to set
     */
    public void setCalendars(List<EmployeeCalendar> calendars) {
        this.calendars = calendars;
    }

    /**
     * @return the employeeCalendar
     */
    public EmployeeCalendar getEmployeeCalendar() {
        return employeeCalendar;
    }

    /**
     * @param employeeCalendar the employeeCalendar to set
     */
    public void setEmployeeCalendar(EmployeeCalendar employeeCalendar) {
        this.employeeCalendar = employeeCalendar;
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
     * @return the selectedEvent
     */
    public ScheduleEvent getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * @param selectedEvent the selectedEvent to set
     */
    public void setSelectedEvent(ScheduleEvent selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    /**
     * @return the selectedCalendar
     */
    public EmployeeCalendar getSelectedCalendar() {
        return selectedCalendar;
    }

    /**
     * @param selectedCalendar the selectedCalendar to set
     */
    public void setSelectedCalendar(EmployeeCalendar selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

}
