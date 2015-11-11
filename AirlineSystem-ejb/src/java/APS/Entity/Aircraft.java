/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Author
package APS.Entity;

import FOS.Entity.MaintainSchedule;
import java.io.Serializable;
import java.util.ArrayList;
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
public class Aircraft implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String tailNo;
    @Temporal(TemporalType.DATE)
    private Date datePurchased;
    @Temporal(TemporalType.DATE)
    private Date lastMaintained;
    private String hub;
    private String status;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private AircraftType aircraftType = new AircraftType();
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "aircraft")
    private List<Schedule> schedules = new ArrayList<Schedule>();
    

    public void createAircraft(String tailNo, Date datePurchased, Date lastMaintained, String hub, String status) {
        this.tailNo = tailNo;
        this.datePurchased = datePurchased;
        this.lastMaintained = lastMaintained;
        this.status = status;
        this.hub = hub;
        this.status = status;
    }
    
    public String getTailNo() {
        return tailNo;
    }

    public void setTailNo(String tailNo) {
        this.tailNo = tailNo;
    }
    
    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }
    
    public Date getLastMaintained() {
        return lastMaintained;
    }

    public void setLastMaintained(Date lastMaintained) {
        this.lastMaintained = lastMaintained;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tailNo != null ? tailNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tailNo fields are not set
        if (!(object instanceof Aircraft)) {
            return false;
        }
        Aircraft other = (Aircraft) object;
        if ((this.tailNo == null && other.tailNo != null) || (this.tailNo != null && !this.tailNo.equals(other.tailNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "APS.Entity.Aircraft[ tailNo=" + tailNo + " ]";
    }
    
}
