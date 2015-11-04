
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smu
 */
@Entity
public class MaintainSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date mainStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date mainEndDate;

    private String tailNumber;
    private String teamId;
    private String type;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private MaintainanceTeam mTeam = new MaintainanceTeam();

    @OneToOne(cascade={CascadeType.PERSIST})
    private AMaintainChecklist amaintainChecklist;
    
    public MaintainSchedule() {
    }

  

    public void create(Date startDate, Date endDate, String tailNumber, String teamId, String type) {
        this.mainStartDate = startDate;
        this.mainEndDate = endDate;
        this.setTailNumber(tailNumber);
        this.teamId = teamId;
        this.type = type;
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
        if (!(object instanceof MaintainSchedule)) {
            return false;
        }
        MaintainSchedule other = (MaintainSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FOS.Entity.MaintenanceSchedule[ id=" + id + " ]";
    }

    /**
     * @return the mainStartDate
     */
    public Date getMainStartDate() {
        return mainStartDate;
    }

    /**
     * @param mainStartDate the mainStartDate to set
     */
    public void setMainStartDate(Date mainStartDate) {
        this.mainStartDate = mainStartDate;
    }

    /**
     * @return the mainEndDate
     */
    public Date getMainEndDate() {
        return mainEndDate;
    }

    /**
     * @param mainEndDate the mainEndDate to set
     */
    public void setMainEndDate(Date mainEndDate) {
        this.mainEndDate = mainEndDate;
    }

    /**
     * @return the teamId
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     * @param teamId the teamId to set
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the tailNumber
     */
    public String getTailNumber() {
        return tailNumber;
    }

    /**
     * @param tailNumber the tailNumber to set
     */
    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    /**
     * @return the mTeam
     */
    public MaintainanceTeam getmTeam() {
        return mTeam;
    }

    /**
     * @param mTeam the mTeam to set
     */
    public void setmTeam(MaintainanceTeam mTeam) {
        this.mTeam = mTeam;
    }

    /**
     * @return the amaintainChecklist
     */
    public AMaintainChecklist getAmaintainChecklist() {
        return amaintainChecklist;
    }

    /**
     * @param amaintainChecklist the amaintainChecklist to set
     */
    public void setAmaintainChecklist(AMaintainChecklist amaintainChecklist) {
        this.amaintainChecklist = amaintainChecklist;
    }

}
