package APS.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Yanlong
 */
@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String flightNo;
    private String flightDays;
    private String timeslot;
    private double flightDuration;
    private double basicFare;

    @Temporal(TemporalType.DATE)
    private Date planStartDate;

    private Route route = new Route();

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    private List<Schedule> schedule = new ArrayList<Schedule>();

    //@OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    //private List<Aircraft> aircraft = new ArrayList<Aircraft>();
    public void createFlight(String flightNo, String flightDays, String timeslot, double flightDuration, double basicFare, Date planStartDate) {
        this.flightNo = flightNo;
        this.flightDays = flightDays;
        this.timeslot = timeslot;
        this.flightDuration = flightDuration;
        this.basicFare = basicFare;
        this.planStartDate = planStartDate;
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

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
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

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date startDate) {
        this.planStartDate = startDate;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    /*public List<Aircraft> getAircraft() {
     return aircraft;
     }

     public void setAircraft(List<Aircraft> aircraft) {
     this.aircraft = aircraft;
     }*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightNo != null ? flightNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightNo == null && other.flightNo != null) || (this.flightNo != null && !this.flightNo.equals(other.flightNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "APS.Entity.Flight[ flightNo=" + flightNo + " ]";
    }

}
