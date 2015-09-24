/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

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
 * @author parthasarthygupta
 */
@Entity
public class Checklist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    public String name;
    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<ChecklistItem> checklistItems = new ArrayList<ChecklistItem>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } 

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

  
    public void addChecklistItem (ChecklistItem checklistItem){
        checklistItems.add(checklistItem);
    }
    
     public void removeChecklistItem (ChecklistItem item){
        checklistItems.remove(item);
    }
    

    @Override
    public String toString() {
        return "FOS.Entity.Checklist[ id=" + id + " ]";
    }
    
}
