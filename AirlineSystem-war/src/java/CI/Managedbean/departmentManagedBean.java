/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.DepartmentSessionBeanLocal;
import CI.Session.EmployeeSessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author HOULIANG
 */

@ManagedBean
@SessionScoped
@Named(value = "departmentManageBean")
public class departmentManagedBean {
    @EJB
    private DepartmentSessionBeanLocal departmentSessionBean;

    public departmentManagedBean(DepartmentSessionBeanLocal departmentSessionBean) {
        this.departmentSessionBean = departmentSessionBean;
    }

   
    
    
    String departmentName;
    String departmentLocation;

    public departmentManagedBean() {
    }

    public void addDepartment(ActionEvent event){
    
        departmentSessionBean.addDepartment(departmentName, departmentLocation);
    
    }
    
    
    
    
    
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentLocation() {
        return departmentLocation;
    }

    public void setDepartmentLocation(String departmentLocation) {
        this.departmentLocation = departmentLocation;
    }
    
     public DepartmentSessionBeanLocal getDepartmentSessionBean() {
        return departmentSessionBean;
    }

    public void setDepartmentSessionBean(DepartmentSessionBeanLocal departmentSessionBean) {
        this.departmentSessionBean = departmentSessionBean;
    }
    
    
     
         
   
    
    
}
