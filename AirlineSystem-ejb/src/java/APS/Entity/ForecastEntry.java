/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Yanlong
 */
@Entity
public class ForecastEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date entryDate;
    private Double forecastValue;
    private Double originalValue;
    
    @ManyToOne
    Forecast forecast = new Forecast();

    public void createEntry(Date entryDate, Double forecastValue, Double originalValue){
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        Calendar result = Calendar.getInstance(tz);
        tmp.setTime(entryDate);
        result.set(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH), 1, 0,0);
        this.entryDate = result.getTime();
        this.forecastValue = forecastValue;
        this.originalValue = originalValue;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Double getForecastValue() {
        return forecastValue;
    }

    public void setForecastValue(Double forecastValue) {
        this.forecastValue = forecastValue;
    }

    public Double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ForecastEntry)) {
            return false;
        }
        ForecastEntry other = (ForecastEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "APS.Entity.ForecastEntry[ id=" + id + " ]";
    }
    
}
