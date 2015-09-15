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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
    private List<String> departments = new ArrayList();
    
     private List<String> CCdepartments = new ArrayList();
    
    private List<OrganizationUnit> orgUnits;

    private String deleteDeptName;
    private String staffID;
    private String employeeDept; // dept from seachEmployee function in sessionbean
    private String changeDeptName; // change department name
    private List<String> restDepts;

    private String errorMsg;

    public departmentManagedBean() {

    }

    /*This is for admin to create new department*/
    public void addDepartment(ActionEvent event) {
        FacesMessage message = null;

         departmentSessionBean.addDepartment(departmentName, departmentLocation);
           clear();
       
       //Comment out if first time set up     
       /* List<OrganizationUnit> depts = departmentSessionBean.retrieveAllDepts();
        
        System.out.println("----------All Dept size: " + depts.size());

        for (OrganizationUnit ou : depts) {
            if (ou.getDepartmentName().equals(departmentName.toUpperCase()) && ou.getLocation().equals(departmentLocation.toUpperCase())) {

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Department Exists", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }
        departmentSessionBean.addDepartment(departmentName, departmentLocation);
        clear();*/
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Department Created Successfully!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void searchEmployee(ActionEvent event) {
        setEmployeeDept(departmentSessionBean.searchEmployee(staffID));
        if (employeeDept.equals("User Not Exist!")) {
            setErrorMsg("No User Exist!");
        } else {
            getRestDepartment();
        }
    }

    public void changeDepartment(ActionEvent event) {
        departmentSessionBean.changeDepartment(staffID, changeDeptName, employeeDept);
        setStaffID("");
        setRestDepts(null);
        setEmployeeDept("");
    }

    public void clear() {
        setDepartmentName("");
        setDepartmentLocation("");
    }

    public void getRestDepartment() {
        restDepts = new ArrayList();
        String deptName = employeeDept.substring(0, employeeDept.indexOf("("));
        System.out.println("DeparmentName: " + deptName);
        int index = employeeDept.indexOf("(") + 1;
        String deptLocation = employeeDept.substring(index, employeeDept.indexOf(")"));
        System.out.println("DeparmentLocation: " + deptLocation);
        for (String s : departments) {
            if (!s.substring(0, s.indexOf("(")).equals(deptName) && !s.substring(s.indexOf("("), s.indexOf(")")).equals(deptLocation)) {
                restDepts.add(s);
            }
        }
    }

   
    @PostConstruct
    public void retrive() {
        setDepartments(departmentSessionBean.retrive());
        System.out.println("set org units");

        setOrgUnits(departmentSessionBean.retrieveAllDepts());
        
        retriveCC();  //comment out first time adding
    }
    
    public void retriveCC(){
        //setDepartments(departmentSessionBean.retrive());
        for(String s : departments){
            if(s.substring(0,s.indexOf("(")).equals("FLIGHT CREW")){
                CCdepartments.add(s);
            }
        }
        
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

    /**
     * @return the deleteDeptName
     */
    public String getDeleteDeptName() {
        return deleteDeptName;
    }

    /**
     * @param deleteDeptName the deleteDeptName to set
     */
    public void setDeleteDeptName(String deleteDeptName) {
        this.deleteDeptName = deleteDeptName;
    }

    /**
     * @return the staffID
     */
    public String getStaffID() {
        return staffID;
    }

    /**
     * @param staffID the staffID to set
     */
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    /**
     * @return the employeeDept
     */
    public String getEmployeeDept() {
        return employeeDept;
    }

    /**
     * @param employeeDept the employeeDept to set
     */
    public void setEmployeeDept(String employeeDept) {
        this.employeeDept = employeeDept;
    }

    /**
     * @return the changeDeptName
     */
    public String getChangeDeptName() {
        return changeDeptName;
    }

    /**
     * @param changeDeptName the changeDeptName to set
     */
    public void setChangeDeptName(String changeDeptName) {
        this.changeDeptName = changeDeptName;
    }

    /**
     * @return the restDepts
     */
    public List<String> getRestDepts() {
        return restDepts;
    }

    /**
     * @param restDepts the restDepts to set
     */
    public void setRestDepts(List<String> restDepts) {
        this.restDepts = restDepts;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<OrganizationUnit> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrganizationUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public List<String> getCCdepartments() {
        return CCdepartments;
    }

    public void setCCdepartments(List<String> CCdepartments) {
        this.CCdepartments = CCdepartments;
    }
    
    
}
