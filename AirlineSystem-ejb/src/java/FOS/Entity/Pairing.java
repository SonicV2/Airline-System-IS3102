/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author smu
 */
@Entity
public class Pairing implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fDate;
    private String flightHour;
    private List<String> flightNumbers;
    private List<String> flightCities;
    private List<String> flightTimes;
    private boolean paired;
    
    private boolean isA380;
    
   @ManyToOne(cascade={CascadeType.PERSIST})
   private Team team = new Team();
   
    public Pairing(){}
    
    public void create(String fDate, String flightHour, List<String> flightNumbers, List<String> flightCities, List<String> flightTimes){
        this.fDate=fDate;
        this.flightHour = flightHour;
        this.flightCities = flightCities;
        this.flightNumbers = flightNumbers;
        this.flightTimes = flightTimes;
        this.paired=false;
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
        if (!(object instanceof Pairing)) {
            return false;
        }
        Pairing other = (Pairing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FOS.Entity.Pairing[ id=" + id + " ]";
    }

    /**
     * @return the fDate
     */
    public String getFDate() {
        return fDate;
    }

    /**
     * @param fDate the fDate to set
     */
    public void setFDate(String fDate) {
        this.fDate = fDate;
    }

    /**
     * @return the flightHour
     */
    public String getFlightHour() {
        return flightHour;
    }

    /**
     * @param flightHour the flightHour to set
     */
    public void setFlightHour(String flightHour) {
        this.flightHour = flightHour;
    }

    /**
     * @return the flightNumbers
     */
    public List<String> getFlightNumbers() {
        return flightNumbers;
    }

    /**
     * @param flightNumbers the flightNumbers to set
     */
    public void setFlightNumbers(List<String> flightNumbers) {
        this.flightNumbers = flightNumbers;
    }

    /**
     * @return the flightCities
     */
    public List<String> getFlightCities() {
        return flightCities;
    }

    /**
     * @param flightCities the flightCities to set
     */
    public void setFlightCities(List<String> flightCities) {
        this.flightCities = flightCities;
    }

    /**
     * @return the flightTimes
     */
    public List<String> getFlightTimes() {
        return flightTimes;
    }

    /**
     * @param flightTimes the flightTimes to set
     */
    public void setFlightTimes(List<String> flightTimes) {
        this.flightTimes = flightTimes;
    }

    /**
     * @return the paired
     */
    public boolean isPaired() {
        return paired;
    }

    /**
     * @param paired the paired to set
     */
    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    /**
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isIsA380() {
        return isA380;
    }

    public void setIsA380(boolean isA380) {
        this.isA380 = isA380;
    }
    
    
    
}
