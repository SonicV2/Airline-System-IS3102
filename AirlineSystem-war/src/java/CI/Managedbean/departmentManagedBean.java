/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Entity.OrganizationUnit;
import CI.Session.DepartmentSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

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
    private List<OrganizationUnit> orgUnits;
    
    
    
    
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
        System.out.println("set org units");
       
        
        setOrgUnits(departmentSessionBean.retrieveAllDepts());
       
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
    
  
    
    
     public List<OrganizationUnit> getOrgUnits(){
         return orgUnits;
     }
     
    public void setOrgUnits(List<OrganizationUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }
}
         
 