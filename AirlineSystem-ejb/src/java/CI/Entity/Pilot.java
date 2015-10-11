/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Entity;

import FOS.Entity.Team;
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
public class Pilot extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String experience;
    private String position;
    private List<String> skillsets;
    private boolean assigned;
    private String status;
    private String schedule;
    private String signInStatus;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Team team = new Team();

    public Pilot() {
        super();
    }

    public void create(String experience, List<String> skillsets, String position) {
        this.experience = experience;
        this.position = position;
        this.skillsets = skillsets;
        this.assigned = false;

        this.status = "N.S";
        this.schedule = "N.A";
        this.signInStatus="N.S";

    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<String> getSkillsets() {
        return skillsets;
    }

    public void setSkillsets(List<String> skillsets) {
        this.skillsets = skillsets;
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

    /**
     * @return the assigned
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     * @param assigned the assigned to set
     */
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
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
     * @return the schedule
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * @param schedule the schedule to set
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    /**
     * @return the signInStatus
     */
    public String getSignInStatus() {
        return signInStatus;
    }

    /**
     * @param signInStatus the signInStatus to set
     */
    public void setSignInStatus(String signInStatus) {
        this.signInStatus = signInStatus;
    }

}
