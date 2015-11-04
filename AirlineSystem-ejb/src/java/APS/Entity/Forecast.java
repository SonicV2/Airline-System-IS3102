/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Forecast implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long forecastId;

    private int forecastYear;
    @Temporal(TemporalType.DATE)
    private Date dateOfCreation;
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "forecast")
    private List<ForecastEntry> forecastEntry = new ArrayList<ForecastEntry>();
    
    @ManyToOne
    private Route route = new Route();

    public void createForecastReport(int forecastYear) {
        this.forecastYear = forecastYear;
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        tmp.set(Calendar.SECOND, 0);
        tmp.set(Calendar.MILLISECOND, 0);
        dateOfCreation = tmp.getTime();        
    }

    public Long getForecastId() {
        return forecastId;
    }

    public void setForecastId(Long id) {
        this.forecastId = id;
    }

    public int getForecastYear() {
        return forecastYear;
    }

    public void setForecastYear(int forecastYear) {
        this.forecastYear = forecastYear;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public List<ForecastEntry> getForecastEntry() {
        return forecastEntry;
    }

    public void setForecastEntry(List<ForecastEntry> forecastEntry) {
        this.forecastEntry = forecastEntry;
    }  
    
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.forecastId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Forecast other = (Forecast) obj;
        if (!Objects.equals(this.forecastId, other.forecastId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Forecast{" + "forecastId=" + forecastId + '}';
    }
}
