/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import APS.Entity.Location;
import Inventory.Session.SeasonSessionBeanLocal;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author YiQuan
 */
@Named(value = "seasonManageBean")
@ManagedBean
@SessionScoped
public class seasonManageBean {

    /**
     * Creates a new instance of seasonManageBean
     */
    @EJB
    private SeasonSessionBeanLocal sm;
    private Date start;
    private Date end;
    private boolean origin;
    private boolean destination;
    private String demand;
    private Location location;
    private String remarks;
    private String IATA;

    public void addSeason() {
        if (start == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter Start Date", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (end == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter End Date", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (origin == false && destination == false) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Indicate Origin or Destination", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (demand==null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter Season Demand", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (remarks.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter Remarks", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            location = sm.findLocation(IATA);
            if (location == null) {
                FacesMessage msg = new FacesMessage("Please Enter a Valid IATA code");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
            String message = sm.addSeason(start, end, origin, destination, demand, location, remarks);
            FacesMessage msg = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isOrigin() {
        return origin;
    }

    public void setOrigin(boolean origin) {
        this.origin = origin;
    }

    public boolean isDestination() {
        return destination;
    }

    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
