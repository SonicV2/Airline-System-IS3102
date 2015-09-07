/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Session.RouteSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author HOULIANG
 */
@Named(value = "routeManageBean")
@ManagedBean
@SessionScoped

public class RouteManageBean {
    @EJB
    private RouteSessionBeanLocal routeSessionBean;
    
    
    
    String origin;
    String destination;
    
    public RouteManageBean() {
    }
    
    /*This is for admin to create new route*/
    public void addRoute(ActionEvent event){
        routeSessionBean.addRoute(origin, destination);
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    
    
    
    
}
