package APS.Managedbean;

import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightScheduleSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;

    private String flightNo;
    private String flightDays = "";
    private String[] selectedFlightDays;
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

    FacesMessage message = null;

    private List<Route> routes;
    private List<Schedule> schedule;

    public FlightManageBean() {
    }

    public void createFlight(ActionEvent event) {

        if (flightSessionBean.getRoute(routeId) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such route!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

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

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Monday")) {
                setString("1");
            }
        }

        if (flightDays.isEmpty()) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Tuesday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 1) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Wednesday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 2) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Thursday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 3) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Friday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 4) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Saturday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 5) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Sunday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 6) {
            setString("0");
        }

        flightSessionBean.addFlight(flightNo, flightDays, basicFare, startDateTime, routeId);
        System.out.println(flightNo);
        flightScheduleSessionBean.scheduleFlights(flightNo);
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
