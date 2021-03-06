/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Entity;

import Inventory.Entity.Booking;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Yunna
 */
@Entity
public class Baggage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double baggageWeight;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Booking booking = new Booking();
    
    public void createBaggage(double baggageWeight, Booking booking){
        this.setBaggageWeight(baggageWeight);
        this.setBooking(booking);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBaggageWeight() {
        return baggageWeight;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBaggageWeight(double baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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
        if (!(object instanceof Baggage)) {
            return false;
        }
        Baggage other = (Baggage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Distribution.Entity.Baggage[ id=" + id + " ]";
    }
    
}
