package APS.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    private double flightDuration;
    private Double basicFare;
    private String flightDaysString;
    private Long archiveData;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Route route = new Route();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private AircraftType aircraftType = new AircraftType();

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "flight")
    private List<Schedule> schedule = new ArrayList<Schedule>();

    public void createFlight(String flightNo, String flightDays, Double basicFare, Date startDateTime) {
        this.flightNo = flightNo;
        this.flightDays = flightDays;
        flightDuration = 0.0;
        this.basicFare = basicFare;
        this.startDateTime = startDateTime;
    }

    public Date getLatestScheduleDate() {
        //Create comparator for sorting of Schedules according to starting time
        Comparator<Schedule> comparator = new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                int result = o1.getStartDate().compareTo(o2.getStartDate());
                if (result == 0) {
                    return o1.getStartDate().before(o2.getStartDate()) ? -1 : 1;
                } else {
                    return result;
                }
            }
        };
        
        Collections.sort(schedule,comparator);
        return schedule.get(schedule.size()-1).getStartDate();
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

    public String getFlightDaysString() {
        return flightDaysString;
    }

    public void setFlightDaysString(String flightDaysString) {
        this.flightDaysString = flightDaysString;
    }

    public double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Double getBasicFare() {
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

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public Long getArchiveData() {
        return archiveData;
    }

    public void setArchiveData(Long archiveData) {
        this.archiveData = archiveData;
    }

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
