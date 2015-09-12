/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
   
    public Pilot() {
        super();
    }
    
    public void create(String experience, String position, List<String> skillsets){
        this.setExperience(experience);
        this.position=position;
        this.skillsets=skillsets;
       
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

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the skillsets
     */
    public List<String> getSkillsets() {
        return skillsets;
    }

    /**
     * @param skillsets the skillsets to set
     */
    public void setSkillsets(List<String> skillsets) {
        this.skillsets = skillsets;
    }
    
}
