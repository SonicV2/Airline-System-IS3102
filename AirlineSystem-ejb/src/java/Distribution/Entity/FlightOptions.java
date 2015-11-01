/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Entity;

import APS.Entity.Schedule;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Yunna
 */
@Entity
public class FlightOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long flightOptionsId;
    private Schedule legOne;
    private Schedule legTwo;
    private String layover;
    private String duration;
    private double price;
    
    public void createFlightOptions(Schedule legOne, Schedule legTwo, String layover, String duration, double price) {
        this.legOne = legOne;
        this.legTwo = legTwo;
        this.layover = layover;
        this.duration = duration;
        this.price = price;
    }


    public Schedule getLegOne() {
        return legOne;
    }

    public void setLegOne(Schedule legOne) {
        this.legOne = legOne;
    }

    public Schedule getLegTwo() {
        return legTwo;
    }

    public void setLegTwo(Schedule legTwo) {
        this.legTwo = legTwo;
    }

    public String getLayover() {
        return layover;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the flightOptionsId
     */
    public Long getFlightOptionsId() {
        return flightOptionsId;
    }

    /**
     * @param flightOptionsId the flightOptionsId to set
     */
    public void setFlightOptionsId(Long flightOptionsId) {
        this.flightOptionsId = flightOptionsId;
    }
    
        @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightOptionsId != null ? flightOptionsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlightOptions)) {
            return false;
        }
        FlightOptions other = (FlightOptions) object;
        if ((this.flightOptionsId == null && other.flightOptionsId != null) || (this.flightOptionsId != null && !this.flightOptionsId.equals(other.flightOptionsId))) {
            return false;
        }
        return true;
    }
    
}
