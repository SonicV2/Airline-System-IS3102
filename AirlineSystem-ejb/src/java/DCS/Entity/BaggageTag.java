/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smu
 */
@Entity
public class BaggageTag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String baggageTagSeqNumber;

    private String flightNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;

    private String arrivalCity;
    private String serviceClass;
    
    private String bagStatus;

    public BaggageTag() {
    }

    public void create(String baggageTagSeqNumber, String flightNumber, Date departureDate, String arrivalCity, String serviceClass) {
        this.baggageTagSeqNumber = baggageTagSeqNumber;
        this.arrivalCity = arrivalCity;
        this.flightNumber = flightNumber;
        this.departureDate = departureDate;
        this.serviceClass = serviceClass;
        this.bagStatus = "N.A";
    }

    /**
     * @return the baggageTagSeqNumber
     */
    public String getBaggageTagSeqNumber() {
        return baggageTagSeqNumber;
    }

    /**
     * @param baggageTagSeqNumber the baggageTagSeqNumber to set
     */
    public void setBaggageTagSeqNumber(String baggageTagSeqNumber) {
        this.baggageTagSeqNumber = baggageTagSeqNumber;
    }

    /**
     * @return the flightNumber
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * @param flightNumber the flightNumber to set
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * @return the departureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return the arrivalCity
     */
    public String getArrivalCity() {
        return arrivalCity;
    }

    /**
     * @param arrivalCity the arrivalCity to set
     */
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    /**
     * @return the serviceClass
     */
    public String getServiceClass() {
        return serviceClass;
    }

    /**
     * @param serviceClass the serviceClass to set
     */
    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof BaggageTag)) {
            return false;
        }
        BaggageTag other = (BaggageTag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DCS.Entity.BaggageTag[ id=" + id + " ]";
    }

    /**
     * @return the bagStatus
     */
    public String getBagStatus() {
        return bagStatus;
    }

    /**
     * @param bagStatus the bagStatus to set
     */
    public void setBagStatus(String bagStatus) {
        this.bagStatus = bagStatus;
    }

}
