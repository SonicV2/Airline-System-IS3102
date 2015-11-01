/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

import CI.Entity.CabinCrew;
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
public class MaintainanceTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int maintainCrewNo;
    private String status;

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "mTeam")
    private List<GroundCrew> groundCrews = new ArrayList<GroundCrew>();

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "mTeam")
    private List<MaintainSchedule> mSchedules = new ArrayList<MaintainSchedule>();

    public MaintainanceTeam() {
    }

    public void create(int maintainCrewNo) {
        this.setMaintainCrewNo(maintainCrewNo);
        this.setStatus("N.A");
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
        if (!(object instanceof MaintainanceTeam)) {
            return false;
        }
        MaintainanceTeam other = (MaintainanceTeam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FOS.Entity.MaintainanceTeam[ id=" + id + " ]";
    }

    /**
     * @return the maintainCrewNo
     */
    public int getMaintainCrewNo() {
        return maintainCrewNo;
    }

    /**
     * @param maintainCrewNo the maintainCrewNo to set
     */
    public void setMaintainCrewNo(int maintainCrewNo) {
        this.maintainCrewNo = maintainCrewNo;
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
     * @return the groundCrews
     */
    public List<GroundCrew> getGroundCrews() {
        return groundCrews;
    }

    /**
     * @param groundCrews the groundCrews to set
     */
    public void setGroundCrews(List<GroundCrew> groundCrews) {
        this.groundCrews = groundCrews;
    }

    /**
     * @return the mSchedules
     */
    public List<MaintainSchedule> getmSchedules() {
        return mSchedules;
    }

    /**
     * @param mSchedules the mSchedules to set
     */
    public void setmSchedules(List<MaintainSchedule> mSchedules) {
        this.mSchedules = mSchedules;
    }

}
