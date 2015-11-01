/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Schedule;
import APS.Session.ScheduleSessionBeanLocal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yanlong
 */
@Named(value = "listAircraftManageBean")
@ManagedBean
@SessionScoped
public class ListAircraftManagedBean {

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private Long tailNo;
    private String hub;
    private String status;
    private List<Schedule> schedules;
    private Long scheduleId;
    private Date startDate;
    private Date endDate;
    private boolean Assigned;
    private String OriginIATA;
    private String DestinationIATA;
    
    public ListAircraftManagedBean() {
    }
    
    public void getSchedules(ActionEvent event) {
        setSchedules(scheduleSessionBean.getSchedules(tailNo));
    }

    public Long getTailNo() {
        return tailNo;
    }

    public void setTailNo(Long tailNo) {
        this.tailNo = tailNo;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
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

    public boolean isAssigned() {
        return Assigned;
    }

    public void setAssigned(boolean Assigned) {
        this.Assigned = Assigned;
    }

    public String getOriginIATA() {
        return OriginIATA;
    }

    public void setOriginIATA(String OriginIATA) {
        this.OriginIATA = OriginIATA;
    }

    public String getDestinationIATA() {
        return DestinationIATA;
    }

    public void setDestinationIATA(String DestinationIATA) {
        this.DestinationIATA = DestinationIATA;
    }
    
    
}
