/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.DepartmentSessionBeanLocal;
import CI.Session.EmployeeSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
@Named(value = "departmentManageBean")
public class departmentManagedBean {
    @EJB
    private DepartmentSessionBeanLocal departmentSessionBean;

    public departmentManagedBean(DepartmentSessionBeanLocal departmentSessionBean) {
        this.departmentSessionBean = departmentSessionBean;
    }

   
    
    
    String departmentName;
    String departmentLocation;

    private String department; // for dropdown selection
    private List<String> departments=new ArrayList();
    
    
    
    
    public departmentManagedBean() {
    
    }
    
    /*This is for admin to create new department*/
    public void addDepartment(ActionEvent event){
    
        departmentSessionBean.addDepartment(departmentName, departmentLocation);
        clear();
    }
    
    public void clear(){
        setDepartmentName("");
        setDepartmentLocation("");
    }
    @PostConstruct
    public void retrive(){
        
        
        setDepartments(departmentSessionBean.retrive());
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }
    
    
     
         
   
    
    
}
