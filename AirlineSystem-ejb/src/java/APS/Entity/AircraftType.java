/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Yanlong
 */
@Entity
public class AircraftType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    
    private double speed;
    private int travelRange;
    private int firstSeats;
    private int businessSeats;
    private int economySeats;
    private int totalStaff;
    private double cost;
    private double fuelCost;
    private int numCabin;
    private int numCockpit;
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "aircraftType")
    private List<Aircraft> aircrafts = new ArrayList<Aircraft>();
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "aircraftType")
    private List<Flight> flights = new ArrayList<Flight>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getTravelRange() {
        return travelRange;
    }

    public void setTravelRange(int travelRange) {
        this.travelRange = travelRange;
    }

    public int getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(int totalStaff) {
        this.totalStaff = totalStaff;
    }
    
    public int getNumCabin() {
        return numCabin;
    }

    public void setNumCabin(int numCabin) {
        this.numCabin = numCabin;
    }
    
    public int getNumCockpit() {
        return numCockpit;
    }

    public void setNumCockpit(int numCockpit) {
        this.numCockpit = numCockpit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double cost) {
        this.fuelCost = fuelCost;
    }

    public int getFirstSeats() {
        return firstSeats;
    }

    public void setFirstSeats(int firstSeats) {
        this.firstSeats = firstSeats;
    }
    
    public int getBusinessSeats() {
        return businessSeats;
    }

    public void setBusinessSeats(int businessSeats) {
        this.businessSeats = businessSeats;
    }
    
    public int getEconomySeats() {
        return economySeats;
    }

    public void setEconomySeats(int economySeats) {
        this.economySeats = economySeats;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
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
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "APS.Entity.AircraftType[ id=" + id + " ]";
    }
    
}
