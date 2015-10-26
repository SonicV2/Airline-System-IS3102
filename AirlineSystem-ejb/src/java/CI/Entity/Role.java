/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author HOULIANG
 */
@Entity
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleID;
    
    //private static long roleNo=10;
    
    private String roleName;
    
    @ManyToMany(cascade = {CascadeType.PERSIST}, mappedBy="roles")
    private List<Employee> employees= new ArrayList<Employee>(); 
    
    
//    @OneToOne(cascade = {CascadeType.PERSIST})
//    private AccessRight access=new AccessRight();
//    
    @ManyToMany (cascade = {CascadeType.PERSIST})
    private List<AccessRight> accessRights = new ArrayList<AccessRight>();

    
    public Role(){
    }
    
    public void create(String roleName){
        this.roleName=roleName;
      //  roleID=roleNo;
       // roleNo=roleNo+1;
    }
    
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleName != null ? roleName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roleName fields are not set
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.roleName == null && other.roleName != null) || (this.roleName != null && !this.roleName.equals(other.roleName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CI.Entity.Role[ id=" + roleName + " ]";
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }


    /**
     * @return the roleID
     */
    public Long getRoleID() {
        return roleID;
    }

    /**
     * @param roleID the roleID to set
     */
    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    /**
     * @return the accessRights
     */
    public List<AccessRight> getAccessRights() {
        return accessRights;
    }

    /**
     * @param accessRights the accessRights to set
     */
    public void setAccessRights(List<AccessRight> accessRights) {
        this.accessRights = accessRights;
    }
    
    
    
    
}
