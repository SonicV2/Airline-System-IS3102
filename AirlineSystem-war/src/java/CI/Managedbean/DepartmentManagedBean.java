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
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author HOULIANG
 */
@ManagedBean
@ViewScoped
@Named(value = "departmentManagedBean")
public class DepartmentManagedBean {

    @EJB
    private DepartmentSessionBeanLocal departmentSessionBean;

    public DepartmentManagedBean(DepartmentSessionBeanLocal departmentSessionBean) {
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
    private String deleteOUName;
    
    private OrganizationUnit newOrgUnit;
    

    public DepartmentManagedBean() {

    }
    
    public void onRowEdit(RowEditEvent event) {
        
        OrganizationUnit orgUnit = (OrganizationUnit) event.getObject();

        
        OrganizationUnit oldOrgUnit = departmentSessionBean.getDepartmentUseID(orgUnit.getDepartmentID());
        
        if (!orgUnit.getDepartmentName().equals(oldOrgUnit.getDepartmentName()) || !orgUnit.getLocation().equals(oldOrgUnit.getLocation())){
            departmentSessionBean.updateOrgUnit(oldOrgUnit, orgUnit);
            
            FacesMessage msg = new FacesMessage("Edited for: ", ((OrganizationUnit) event.getObject()).getDepartmentName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
     
        }
            else{
            
            FacesMessage msg = new FacesMessage("Nothing edited for: ", ((OrganizationUnit) event.getObject()).getDepartmentName());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
     public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((OrganizationUnit) event.getObject()).getDepartmentName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /*This is for admin to create new department*/
    public void addDepartment(ActionEvent event) {
        FacesMessage message = null;

            
        List<OrganizationUnit> depts = departmentSessionBean.retrieveAllDepts();

        if (depts != null) {
            for (OrganizationUnit ou : depts) {
                if (ou.getDepartmentName().equals(departmentName.toUpperCase()) && ou.getLocation().equals(departmentLocation.toUpperCase())) {

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Department already exists!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return;
                }
            }
        }
        departmentSessionBean.addDepartment(departmentName, departmentLocation);
        clear();
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

    public String deleteDepartment(String ouName) {
//        setDeleteOUName(ouName);
        setErrorMsg(departmentSessionBean.deleteOrgUnit(ouName));
//        setErrorMsg(roleSessionBean.deleteRole(deleteRoleName));
//        setDeleteRoleName("");
//        setDelete

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, errorMsg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "CreateDepartment";

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
   

    public void retriveCC() {
        //setDepartments(departmentSessionBean.retrive());
        if (departments != null) {

            for (String s : departments) {
                if (s.substring(0, s.indexOf("(")).equals("FLIGHT CREW") || (s.substring(0, s.indexOf("(")).equals("GROUND CREW"))) {
                    CCdepartments.add(s);
                }
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

    /**
     * @return the deleteOUName
     */
    public String getDeleteOUName() {
        return deleteOUName;
    }

    /**
     * @param deleteOUName the deleteOUName to set
     */
    public void setDeleteOUName(String deleteOUName) {
        this.deleteOUName = deleteOUName;
    }

    /**
     * @return the newOrgUnit
     */
    public OrganizationUnit getNewOrgUnit() {
        return newOrgUnit;
    }

    /**
     * @param newOrgUnit the newOrgUnit to set
     */
    public void setNewOrgUnit(OrganizationUnit newOrgUnit) {
        this.newOrgUnit = newOrgUnit;
    } 
}
