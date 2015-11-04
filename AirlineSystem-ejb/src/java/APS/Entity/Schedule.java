/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Entity;

import FOS.Entity.Checklist;
import FOS.Entity.Team;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import javax.persistence.CascadeType;
import Inventory.Entity.SeatAvailability;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

/**
 *
 * @author Yanlong
 */
@Entity
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scheduleId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private boolean Assigned;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Flight flight = new Flight();

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Team team = new Team();

    @OneToOne(mappedBy="schedule",cascade = {CascadeType.PERSIST})
    private SeatAvailability seatAvail;

    @ManyToOne
    private Aircraft aircraft = new Aircraft();

    @OneToMany(cascade = {CascadeType.REMOVE})
    private List<Checklist> checklists = new ArrayList<Checklist>();
        
    private List<String> selectedSeatsF = new ArrayList<String>();
    
    private List<String> selectedSeatsB = new ArrayList<String>();
    
    private List<String> selectedSeatsE = new ArrayList<String>();
    
    public void createSchedule(Date startDate, Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        Assigned = false;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isAssigned() {
        return Assigned;
    }

    public void setAssigned(boolean Assigned) {
        this.Assigned = Assigned;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Schedule)) {
            return false;
        }
        Schedule other = (Schedule) object;
        if ((this.scheduleId == null && other.scheduleId != null) || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "APS.Entity.Schedule[ id=" + scheduleId + " ]";
    }

    /**
     * @return the seatAvailability
     */
    public SeatAvailability getSeatAvailability() {
        return seatAvail;
    }

    /**
     * @param seatAvailability the seatAvailability to set
     */
    public void setSeatAvailability(SeatAvailability seatAvailability) {
        this.seatAvail = seatAvailability;
    }

    public List<Checklist> getChecklists() {
        return checklists;
}

    public void setChecklists(List<Checklist> checklists) {
        this.checklists = checklists;
    }

    /**
     * @return the selectedSeatsF
     */
    public List<String> getSelectedSeatsF() {
        return selectedSeatsF;
    }

    /**
     * @param selectedSeatsF the selectedSeatsF to set
     */
    public void setSelectedSeatsF(List<String> selectedSeatsF) {
        this.selectedSeatsF = selectedSeatsF;
    }

    /**
     * @return the selectedSeatsB
     */
    public List<String> getSelectedSeatsB() {
        return selectedSeatsB;
    }

    /**
     * @param selectedSeatsB the selectedSeatsB to set
     */
    public void setSelectedSeatsB(List<String> selectedSeatsB) {
        this.selectedSeatsB = selectedSeatsB;
    }

    /**
     * @return the selectedSeatsE
     */
    public List<String> getSelectedSeatsE() {
        return selectedSeatsE;
    }

    /**
     * @param selectedSeatsE the selectedSeatsE to set
     */
    public void setSelectedSeatsE(List<String> selectedSeatsE) {
        this.selectedSeatsE = selectedSeatsE;
    }

}
