/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.RouteSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import Distribution.Session.DistributionSessionBeanLocal;
import java.util.Date;
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
 * @author Yunna
 */
@Named(value = "MARSManagedBean")
@ManagedBean
@SessionScoped
public class MARSManagedBean {

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;
     @EJB
    private FlightSessionBeanLocal flightSessionBean;
    @EJB
    private RouteSessionBeanLocal routeSessionBean;
    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
    
    private String flightNo;
    private Double flightDuration;
    private Double price;
    
    private Date departureDate;
    private Date returnDate;

    private Long routeId;
    private String originCountry;
    private String originCity;
    private String originIATA;
    private String destinationCountry;
    private String destinationCity;
    private String destinationIATA;
    private Double distance;
    
    private int adults;
    private int children;
    private String serviceType;
    

    private List<Route> routes;
    private List<Schedule> schedule;
    private List<Flight> flights;
    private List<String> transitHubs;
    private List<Schedule> legOneSchedules;
    private List<Schedule> legTwoSchedules;
    private List<Schedule> directFlightSchedules;
    private List<Schedule> oneStopFlightSchedules;
    
    public MARSManagedBean() {
    }
    
    @PostConstruct
    public void retrieve() {
        
        //Retrieve all the available flights into a list of flights
        setFlights(flightSessionBean.retrieveFlights());
        //For each flight, add the available routes to the list of routes
        for (Flight eachFlight: flights){
                routes.add(eachFlight.getRoute());
            }
    }
    
    public void displayFlights(ActionEvent event){
        
        //Check whether there is direct flight
        if (distributionSessionBean.existsDirectFlight(originIATA, destinationIATA)){
            setDirectFlightSchedules(distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, departureDate, serviceType, adults, children));
        }
        else { //Retrieve one stop flights
            legOneSchedules = null;
            legTwoSchedules = null;
            transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);
            for (int i=0; i<transitHubs.size(); i++) {
              addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(i), departureDate, serviceType, adults, children));
              addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, distributionSessionBean.addOneDayToDate(departureDate), serviceType, adults, children));
              addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, departureDate, serviceType, adults, children));
        }
        setOneStopFlightSchedules(distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules));
        }
    }
    
    public void addSchedulesToLegOne (List<Schedule> originalSchedules, List<Schedule> schedulesToAdd){
        for (Schedule eachScheduleToAdd: schedulesToAdd){
            originalSchedules.add(eachScheduleToAdd);
        }
        setLegOneSchedules (originalSchedules);
    }
    
    public void addSchedulesToLegTwo (List<Schedule> originalSchedules, List<Schedule> schedulesToAdd){
        for (Schedule eachScheduleToAdd: schedulesToAdd){
            originalSchedules.add(eachScheduleToAdd);
        }
        setLegTwoSchedules (originalSchedules);
    }
    
    
    

    public String getFlightNo() {
        return flightNo;
    }

    public Double getFlightDuration() {
        return flightDuration;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getOriginCity() {
        return originCity;
    }

    public String getOriginIATA() {
        return originIATA;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getDestinationIATA() {
        return destinationIATA;
    }

    public Double getDistance() {
        return distance;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public void setOriginIATA(String originIATA) {
        this.originIATA = originIATA;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public void setDestinationIATA(String destinationIATA) {
        this.destinationIATA = destinationIATA;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<String> getTransitHubs() {
        return transitHubs;
    }

    public List<Schedule> getLegOneSchedules() {
        return legOneSchedules;
    }

    public List<Schedule> getLegTwoSchedules() {
        return legTwoSchedules;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setTransitHubs(List<String> transitHubs) {
        this.transitHubs = transitHubs;
    }

    public void setLegOneSchedules(List<Schedule> legOneSchedules) {
        this.legOneSchedules = legOneSchedules;
    }

    public void setLegTwoSchedules(List<Schedule> legTwoSchedules) {
        this.legTwoSchedules = legTwoSchedules;
    }

    public List<Schedule> getDirectFlightSchedules() {
        return directFlightSchedules;
    }

    public void setDirectFlightSchedules(List<Schedule> directFlightSchedules) {
        this.directFlightSchedules = directFlightSchedules;
    }

    public List<Schedule> getOneStopFlightSchedules() {
        return oneStopFlightSchedules;
    }

    public void setOneStopFlightSchedules(List<Schedule> oneStopFlightSchedules) {
        this.oneStopFlightSchedules = oneStopFlightSchedules;
    }
    
    
    
    
}
