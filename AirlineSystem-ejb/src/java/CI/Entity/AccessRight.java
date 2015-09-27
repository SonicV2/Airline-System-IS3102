/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class AccessRight implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String accessRightName;
//    private List<String> accessRights;
    private Boolean accessCreate;
    private Boolean accessDelete;
    private Boolean accessUpdate;
    private Boolean accessView;
    
//    public void create(String name, List<String> accessRights){
//        this.setAccessRightName(name);
//       this.setAccessRights(accessRights);
//    }
    
    public void create(String name){
        this.setAccessRightName(name);
        this.setAccessCreate(false);
        this.setAccessDelete(false);
        this.setAccessUpdate(false);
        this.setAccessView(false);
      
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
        if (!(object instanceof AccessRight)) {
            return false;
        }
        AccessRight other = (AccessRight) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CI.Entity.AccessRight[ id=" + id + " ]";
    }


    /**
     * @param accessRightName the accessRightName to set
     */
    public void setAccessRightName(String accessRightName) {
        this.accessRightName = accessRightName;
    }

//    /**
//     * @return the accessRights
//     */
//    public List<String> getAccessRights() {
//        return accessRights;
//    }
//
//    /**
//     * @param accessRights the accessRights to set
//     */
//    public void setAccessRights(List<String> accessRights) {
//        this.accessRights = accessRights;
//    }

    /**
     * @return the accessRightName
     */
    public String getAccessRightName() {
        return accessRightName;
    }

    /**
     * @return the accessCreate
     */
    public Boolean getAccessCreate() {
        return accessCreate;
    }

    /**
     * @param accessCreate the accessCreate to set
     */
    public void setAccessCreate(Boolean accessCreate) {
        this.accessCreate = accessCreate;
    }

    /**
     * @return the accessDelete
     */
    public Boolean getAccessDelete() {
        return accessDelete;
    }

    /**
     * @param accessDelete the accessDelete to set
     */
    public void setAccessDelete(Boolean accessDelete) {
        this.accessDelete = accessDelete;
    }

    /**
     * @return the accessUpdate
     */
    public Boolean getAccessUpdate() {
        return accessUpdate;
    }

    /**
     * @param accessUpdate the accessUpdate to set
     */
    public void setAccessUpdate(Boolean accessUpdate) {
        this.accessUpdate = accessUpdate;
    }

    /**
     * @return the accessView
     */
    public Boolean getAccessView() {
        return accessView;
    }

    /**
     * @param accessView the accessView to set
     */
    public void setAccessView(Boolean accessView) {
        this.accessView = accessView;
    }

    
}


