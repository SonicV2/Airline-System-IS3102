/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smu
 */
@Entity
public class AMaintainChecklist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String comments;
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEditDate;

    private String listStatus; // for backup

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<MaintainChecklistItem> maintainChecklistItems = new ArrayList<MaintainChecklistItem>();
    
    @OneToOne(mappedBy="amaintainChecklist")
    private MaintainSchedule maintainSchedule;
    
    public AMaintainChecklist() {
    }

    public void create(String name, String type, String comments, Date lastEditDate) {
        this.name = name;
        this.comments = comments;
        this.lastEditDate = lastEditDate;
        this.type = type;
        this.listStatus ="N.A";
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
        if (!(object instanceof AMaintainChecklist)) {
            return false;
        }
        AMaintainChecklist other = (AMaintainChecklist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FOS.Entity.MaintenanceChecklist[ id=" + id + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the lastEditDate
     */
    public Date getLastEditDate() {
        return lastEditDate;
    }

    /**
     * @param lastEditDate the lastEditDate to set
     */
    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
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
     * @return the maintainChecklistItems
     */
    public List<MaintainChecklistItem> getMaintainChecklistItems() {
        return maintainChecklistItems;
    }

    /**
     * @param maintainChecklistItems the maintainChecklistItems to set
     */
    public void setMaintainChecklistItems(List<MaintainChecklistItem> maintainChecklistItems) {
        this.maintainChecklistItems = maintainChecklistItems;
    }

    /**
     * @return the maintainSchedule
     */
    public MaintainSchedule getMaintainSchedule() {
        return maintainSchedule;
    }

    /**
     * @param maintainSchedule the maintainSchedule to set
     */
    public void setMaintainSchedule(MaintainSchedule maintainSchedule) {
        this.maintainSchedule = maintainSchedule;
    }

    /**
     * @return the listStatus
     */
    public String getListStatus() {
        return listStatus;
    }

    /**
     * @param listStatus the listStatus to set
     */
    public void setListStatus(String listStatus) {
        this.listStatus = listStatus;
    }

}
