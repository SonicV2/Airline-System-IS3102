/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

import CI.Entity.Employee;
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
public class GroundCrew extends Employee implements Serializable {
    private static final long serialVersionUID = 1L;

  
   private String position;
   private boolean assigned;
   private String status;
   private String experience;

   @ManyToOne(cascade={CascadeType.PERSIST})
   private MaintainanceTeam mTeam = new MaintainanceTeam();
   
   
    public GroundCrew() {
        super();
    }

    public void create(String position) {

        this.position=position;
        this.assigned=false;
        this.status="N.A";
        
    }

   
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
     * @return the experience
     */
    public String getExperience() {
        return experience;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }
    
    

    
}
