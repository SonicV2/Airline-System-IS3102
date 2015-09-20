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
    

    private boolean accessAdd;
    private boolean accessDelete;
    private boolean accessCreate;
    private boolean accessAssign;
    private boolean accessView;
    
    public void create(){
       this.accessAdd=false;
       this.accessAssign=false;
       this.accessCreate=false;
       this.accessDelete=false;
       this.accessView=false;
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
     * @return the accessAdd
     */
    public boolean isAccessAdd() {
        return accessAdd;
    }

    /**
     * @param accessAdd the accessAdd to set
     */
    public void setAccessAdd(boolean accessAdd) {
        this.accessAdd = accessAdd;
    }

    /**
     * @return the accessDelete
     */
    public boolean isAccessDelete() {
        return accessDelete;
    }

    /**
     * @param accessDelete the accessDelete to set
     */
    public void setAccessDelete(boolean accessDelete) {
        this.accessDelete = accessDelete;
    }

    /**
     * @return the accessCreate
     */
    public boolean isAccessCreate() {
        return accessCreate;
    }

    /**
     * @param accessCreate the accessCreate to set
     */
    public void setAccessCreate(boolean accessCreate) {
        this.accessCreate = accessCreate;
    }

    /**
     * @return the accessAssign
     */
    public boolean isAccessAssign() {
        return accessAssign;
    }

    /**
     * @param accessAssign the accessAssign to set
     */
    public void setAccessAssign(boolean accessAssign) {
        this.accessAssign = accessAssign;
    }

    /**
     * @return the accessView
     */
    public boolean isAccessView() {
        return accessView;
    }

    /**
     * @param accessView the accessView to set
     */
    public void setAccessView(boolean accessView) {
        this.accessView = accessView;
    }

    
}


