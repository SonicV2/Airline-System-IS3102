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
    String originCountry;
    String originCity;
    String originIATA;
    String destinationCountry;
    String destinationCity;
    String destinationIATA;
    
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
        routeSessionBean.addRoute(originIATA, destinationIATA);
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
    public String getOriginCountry() {
        return originCountry;
    }
    
    public void setOriginCountry(String originCountry){
        this.originCountry = originCountry;
    }
    
    public String getDestinationCountry() {
        return destinationCountry;
    }
    
    public void setDestinationCountry(String destinationCountry){
        this.destinationCountry = destinationCountry;
    }
    
    public String getOriginCity() {
        return originCity;
    }
    
    public void setOriginCity(String originCity){
        this.originCity = originCity;
    }
    
    public String getDestinationCity() {
        return destinationCity;
    }
    
    public void setDestinationCity(String destinationCity){
        this.destinationCity = destinationCity;
    }
    
    public String getOriginIATA() {
        return originIATA;
    }
    
    public void setOriginIATA(String originIATA){
        this.originIATA = originIATA;
    }
    
    public String getDestinationIATA() {
        return destinationIATA;
    }
    
    public void setDestinationIATA(String destinationIATA){
        this.destinationIATA = destinationIATA;
    }
    
    
    
  
}
