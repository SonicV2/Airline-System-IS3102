/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Entity.Pilot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author smu
 */
@Entity
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int pilotNo;
    private int cCrewNo; //cabin crew
    private String status;
    private String location;    // all default location is at Singapore
    
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="team")
    private List<CabinCrew> cabinCrews = new ArrayList<CabinCrew>();
    
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="team")
    private List<Pilot> pilots = new ArrayList<Pilot>();
    
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="team")
    private List<Pairing> pairing = new ArrayList<Pairing>();
    
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="team")
    private List<Schedule> schedule= new ArrayList<Schedule>();
    
    
    public Team(){}
    
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
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FOS.Entity.Team[ id=" + id + " ]";
    }

    /**
     * @return the pilotNo
     */
    public int getPilotNo() {
        return pilotNo;
    }

    /**
     * @param pilotNo the pilotNo to set
     */
    public void setPilotNo(int pilotNo) {
        this.pilotNo = pilotNo;
    }

    /**
     * @return the cCrewNo
     */
    public int getcCrewNo() {
        return cCrewNo;
    }

    /**
     * @param cCrewNo the cCrewNo to set
     */
    public void setcCrewNo(int cCrewNo) {
        this.cCrewNo = cCrewNo;
    }

    /**
     * @return the cabinCrews
     */
    public List<CabinCrew> getCabinCrews() {
        return cabinCrews;
    }

    /**
     * @param cabinCrews the cabinCrews to set
     */
    public void setCabinCrews(List<CabinCrew> cabinCrews) {
        this.cabinCrews = cabinCrews;
    }

    /**
     * @return the pilots
     */
    public List<Pilot> getPilots() {
        return pilots;
    }

    /**
     * @param pilots the pilots to set
     */
    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the pairing
     */
    public List<Pairing> getPairing() {
        return pairing;
    }

    /**
     * @param pairing the pairing to set
     */
    public void setPairing(List<Pairing> pairing) {
        this.pairing = pairing;
    }

    /**
     * @return the schedule
     */
    public List<Schedule> getSchedule() {
        return schedule;
    }

    /**
     * @param schedule the schedule to set
     */
    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

   
    
}


