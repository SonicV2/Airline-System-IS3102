/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.RoleSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author HOULIANG
 */
@Named(value = "roleManagedBean")
@ManagedBean
@ViewScoped
public class RoleManagedBean {
    @EJB
    private RoleSessionBeanLocal roleSessionBean;

    String roleName; //fro admin create
    
    String role; //for receiving value from employeeManagement.xhtml
    
     private List<String> roles=new ArrayList();
    
    public RoleManagedBean() {
    }
    
    //for admin to create new roles
    public void addRole(ActionEvent event){
        roleSessionBean.addRole(roleName);
    }
    
    
     @PostConstruct
    public void retrive(){
        
        
        setRoles(roleSessionBean.retrive());
    }
    
    
    
    public RoleSessionBeanLocal getRoleSessionBean() {
        return roleSessionBean;
    }

    public void setRoleSessionBean(RoleSessionBeanLocal roleSessionBean) {
        this.roleSessionBean = roleSessionBean;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    
    
}
