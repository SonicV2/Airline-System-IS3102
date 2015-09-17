/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Entity.Location;
import APS.Entity.Route;
import APS.Session.RouteSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    
    String name;
    String city;
    String country;
    String IATA;
    String searchCountry;
    String searchCity;
    
    FacesMessage message = null;
    
    private List<Location> locations;
    private List<Route> routes;
    private Route selectedRoute;

    List<Location> searchedLocations;

    
    public RouteManageBean() {
    }
    
    @PostConstruct
    public void retrieve(){
        
        setLocations(routeSessionBean.retrieveLocations());
        setRoutes(routeSessionBean.retrieveRoutes());
        
    }
    
    public void searchByCountry(ActionEvent event) {
        
        setSearchedLocations(routeSessionBean.searchLocationsByCountry(searchCountry));

    }
    
    public void searchByCity(ActionEvent event) {
        
        setSearchedLocations(routeSessionBean.searchLocationsByCity(searchCity));

    }

    
    /*This is for admin to create new route*/
    public void addRoute(ActionEvent event){
        if (routeSessionBean.findLocation(originIATA) == null || routeSessionBean.findLocation(destinationIATA) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such IATA!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        routeSessionBean.addRoute(originIATA, destinationIATA);
    }

    public void removeRoute(ActionEvent event){
        routes.remove(selectedRoute);
        routeSessionBean.deleteRoute(selectedRoute.getRouteId());
        selectedRoute = null;
    }
    
    public String getSearchCountry() {
        return searchCountry;
    }

    public void setSearchCountry(String searchCountry) {
        this.searchCountry = searchCountry;
    }
    
    public String getSearchCity() {
        return searchCity;
    }

    public void setSearchCity(String searchCity) {
        this.searchCity = searchCity;
    }
    
    public List<Route> getRoutes(){
         return routes;
     }
     
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Route getSelectedRoute() {
        return selectedRoute;
    }
 
    public void setSelectedRoute(Route selectedRoute) {
        this.selectedRoute = selectedRoute;
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
    
    public List<Location> getSearchedLocations(){
         return searchedLocations;
     }
     
    public void setSearchedLocations(List<Location> searchedLocations) {
        this.searchedLocations = searchedLocations;
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
