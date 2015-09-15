package APS.Managedbean;

import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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

    private String flightNo;
    private String flightDays;
    private double flightDuration;
    private double basicFare;
    private Date startDateTime;
    private Route route = new Route();
    private List<Schedule> schedule = new ArrayList<Schedule>();

    public FlightManageBean() {
    }

    public FlightSessionBeanLocal getFlightSessionBean() {
        return flightSessionBean;
    }

    public void setFlightSessionBean(FlightSessionBeanLocal flightSessionBean) {
        this.flightSessionBean = flightSessionBean;
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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    
}
