package APS.Managedbean;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightScheduleSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.RouteSessionBeanLocal;
import APS.Session.FleetSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
    @EJB
    private RouteSessionBeanLocal routeSessionBean;
    @EJB
    private FleetSessionBeanLocal fleetSessionBean;
    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
    
//    @ManagedProperty(value="#{scheduleManageBean}")
//    private ScheduleManageBean scheduleManageBean;
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    

    private String flightNo;
    private String[] selectedFlightDays;
    private Double flightDuration;
    private Double basicFare;
    private Date startDateTime;
    String flightDays = "";

    private Long routeId;
    private String originCountry;
    private String originCity;
    private String originIATA;
    private String destinationCountry;
    private String destinationCity;
    private String destinationIATA;
    private Double distance;

    FacesMessage message = null;

    private List<Route> routes;
    private List<Schedule> schedule;
    private List<Flight> flights;
    
    private Flight selectedFlight;

    public FlightManageBean() {
    }
    
    @PostConstruct
    public void retrieve(){
        
        setFlights(flightSessionBean.retrieveFlights());
        
    }

    public void createFlight(ActionEvent event) {
        
        if (routeId == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Route ID!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (flightNo.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Flight Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (selectedFlightDays.length == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select Flight Days!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (startDateTime == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter starting day and time!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (basicFare == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Basic Fare!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (flightSessionBean.getRoute(routeId) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such route!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        if (flights!=null) {
        /*for (int i=0; i<flights.size(); i++) {
            
            Flight temp = flightSessionBean.retrieveFlights().get(i);
   
            if (temp.getRoute().equals(flightSessionBean.getRoute(routeId))) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight already exists!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
            
        }*/

        if (flightSessionBean.getFlight(flightNo) != null) {
            if (flightSessionBean.getFlight(flightNo).getRoute() == flightSessionBean.getRoute(routeId)) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight already exists!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight number is already in use!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }
        }
        
        flightDays = "";
        
        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Sunday")) {
                setString("1");
            }
        }

        if (flightDays.isEmpty()) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Monday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 1) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Tuesday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 2) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Wednesday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 3) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Thursday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 4) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Friday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 5) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Saturday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 6) {
            setString("0");
        }

        flightSessionBean.addFlight(flightNo, flightDays, basicFare, startDateTime, routeId);
        flightScheduleSessionBean.scheduleFlights(flightNo);
        flightScheduleSessionBean.rotateFlights();
        flightDays = "";
        FacesMessage msg = new FacesMessage("Flight Added Successfully!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        setFlights(flightSessionBean.retrieveFlights());
        setSchedule(scheduleSessionBean.getSchedules());
        clear();
    }
    
    public String deleteFlight(String flightNo) {
        
        selectedFlight = flightSessionBean.getFlight(flightNo);
        flights.remove(selectedFlight);
        
        //search for Flight Num in Flight lists linked to the Route and remove the Flight
        List<Flight> temp = selectedFlight.getRoute().getFlights();
        temp.remove(selectedFlight);
        selectedFlight.getRoute().setFlights(temp);
        selectedFlight.setRoute(null);

        //search for Flight Num in Flight lists linked to the Aircraft Type and remove the Flight
        List<Flight> temp1 = selectedFlight.getAircraftType().getFlights();
        temp1.remove(selectedFlight);
        selectedFlight.getAircraftType().setFlights(temp1);
        selectedFlight.setAircraftType(null);
        
        //search for Team in Schedule lists linked to the Schedule and remove
        for (int i=0; i<selectedFlight.getSchedule().size(); i++) {
            selectedFlight.getSchedule().get(i).setTeam(null);
        }
        
        //delete schedule
        for (int j=0; j<selectedFlight.getSchedule().size(); j++) {
            flightSessionBean.deleteSchedule(selectedFlight.getSchedule().get(j).getScheduleId());
        }

        flightSessionBean.deleteFlight(selectedFlight.getFlightNo());
        selectedFlight = null;
        
        FacesMessage msg = new FacesMessage("Flight Removed");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        setFlights(flightSessionBean.retrieveFlights());
        setSchedule(scheduleSessionBean.getSchedules());
        
        return "DeleteFlight";
        
    }
    
     /*clear input after submit*/
    public void clear() {
        setRouteId(null);
        setFlightNo("");
        setSelectedFlightDays(null);
        setStartDateTime(null);
        setBasicFare(null);
        
    }

    public void setString(String value) {

        flightDays = flightDays + value;

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

    public String[] getSelectedFlightDays() {
        return selectedFlightDays;
    }

    public void setSelectedFlightDays(String[] selectedFlightDays) {
        this.selectedFlightDays = selectedFlightDays;
    }

    public Double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Double getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(Double basicFare) {
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
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
    
    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    
    public Flight getSelectedFlight() {
        return selectedFlight;
    }
 
    public void setSelectedFlight(Flight selectedFlight) {
        this.selectedFlight = selectedFlight;
    }  
}
