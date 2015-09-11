/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Session.FleetSessionBeanLocal;
import CI.Entity.Role;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Yunna
 */
@Named(value = "fleetManageBean")
@ManagedBean
@SessionScoped

public class FleetManageBean {
@EJB
    private FleetSessionBeanLocal fleetSessionBean;

    Date datePurchased;
    Date lastMaintained;
    String aircraftTypeId;
    Long tailNo;
    
    
    public FleetManageBean() {
    }
    
    /*This is for admin to acquire new aircraft*/
    public void acquireAircraft(ActionEvent event){
        fleetSessionBean.acquireAircraft(datePurchased, lastMaintained, aircraftTypeId);
    }

    public void retireAircraft(ActionEvent event){
        fleetSessionBean.retireAircraft(tailNo);
    }
    
    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }
    
    public Date getLastMaintained() {
        return lastMaintained;
    }

    public void setLastMaintained(Date lastMaintained) {
        this.lastMaintained = lastMaintained;
    }
    
    public String getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(String aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }
    
    public Long getTailNo() {
        return tailNo;
    }
    
    public void setTailNo(Long tailNo) {
        this.tailNo = tailNo;
    }
}
