package APS.Managedbean;

import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.RouteSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Yanlong
 */
@Named(value = "flightManageBean")
@ManagedBean
@SessionScoped

public class FlightManageBean {

    @EJB
    private FlightSessionBeanLocal flightSessionBean;
    private RouteSessionBeanLocal routeSessionBean;

    private String flightNo;
    private String flightDays;
    private double flightDuration;
    private double basicFare;
    private Date startDateTime;
    
    private Long routeId;
    private String originCountry;
    private String originCity;
    private String originIATA;
    private String destinationCountry;
    private String destinationCity;
    private String destinationIATA;
    private double distance;
    
    private List<Route> routes;
    private List<Schedule> schedule;
    

    public FlightManageBean() {
    }

    public void createFlight(ActionEvent event){
        flightSessionBean.addFlight(flightNo, flightDays, flightDuration, basicFare, startDateTime,routeId);
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

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginIATA() {
        return originIATA;
    }

    public void setOriginIATA(String originIATA) {
        this.originIATA = originIATA;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationIATA() {
        return destinationIATA;
    }

    public void setDestinationIATA(String destinationIATA) {
        this.destinationIATA = destinationIATA;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}
