/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Location;
import APS.Entity.Route;
import APS.Session.RouteSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
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
    
    
    Long routeId;
    String origin;
    String destination;
    
    private List<Location> locations;
    private List<Route> routes;
    
    String name;
    String city;
    String country;
    String IATA;
    
    public RouteManageBean() {
    }
    
    @PostConstruct
    public void retrieve(){
        
        setLocations(routeSessionBean.retrieveLocations());
        setRoutes(routeSessionBean.retrieveRoutes());
        
    }
    
    /*This is for admin to create new route*/
    public void addRoute(ActionEvent event){
        routeSessionBean.addRoute(origin, destination);
    }

    public void removeRoute(ActionEvent event){
        routeSessionBean.deleteRoute(routeId);
    }
    
    public List<Route> getRoutes(){
         return routes;
     }
     
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
    
    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
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
    
    public List<Location> getLocations(){
         return locations;
     }
     
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }
    
    
    
    
}
