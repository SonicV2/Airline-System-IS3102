package APS.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
    private String flightDay;
    private String timeslot;
    private double flightDuration;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private int weeks;
    private Route route = new Route();
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    private List<Schedule> schedule = new ArrayList<Schedule>();
    
    //@OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    //private List<Aircraft> aircraft = new ArrayList<Aircraft>();
    
    public void createFlight(String flightNo, String flightDay, String timeslot, double flightDuration, Date startDate, int weeks){
        this.flightNo = flightNo;
        this.flightDay = flightDay;
        this.timeslot = timeslot;
        this.flightDuration = flightDuration;
        this.weeks = weeks;
        this.startDate = startDate;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.WEEK_OF_YEAR, weeks);
        Date endDate = cal.getTime();
        
        Date counter = startDate;
        
        for (int i = 0; i<weeks; i++){
             cal.setTime(counter);
        }        
     }
    
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightDay() {
        return flightDay;
    }

    public void setFlightDay(String flightDay) {
        this.flightDay = flightDay;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
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