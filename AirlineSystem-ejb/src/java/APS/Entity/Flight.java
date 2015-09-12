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
    @Temporal(TemporalType.DATE)
    private Date flightDate;
    private Route route = new Route();
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    private List<Schedule> schedule = new ArrayList<Schedule>();
    
    //@OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    //private List<Aircraft> aircraft = new ArrayList<Aircraft>();
    
    public void createFlight(String flightNo, Date flghtDate){
        this.flightNo = flightNo;
        this.flightDate = flightDate;
    }
    
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
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