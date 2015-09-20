/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Entity.Employee;
import CI.Entity.OrganizationUnit;
import CI.Session.DepartmentSessionBeanLocal;
import CI.Session.EmployeeSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yuqing
 */



@Named(value = "adminManagedBean")
@ManagedBean
@SessionScoped
public class AdminManagedBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    @EJB
    private DepartmentSessionBeanLocal departmentSessionBean;
    
    private List<Employee> allEmployees;
    private List<Employee> allActiveEmployees;
    private List<OrganizationUnit> allOUs;
    private List<String> allGenders;
    private List<String> allDepts = new ArrayList();
    private List<String> allDeptLocations = new ArrayList();
    private List<String> allRoles = new ArrayList();
    private String employeeNewDeptName;
    private String employeeNewRole;
    private String employeeNewDeptLocation;
    private Employee anEmployee;
    /**
     * Creates a new instance of adminManagedBean
     */
    public AdminManagedBean() {    
    }
    
    @PostConstruct
    public void retreive(){
//        setAllEmployees(employeeSessionBean.retrieveAllEmployees());
        setAllActiveEmployees(employeeSessionBean.retrieveAllActiveEmployees());
        setAllOUs(departmentSessionBean.retrieveAllDepts());
    }
    
    public void onRowEdit(RowEditEvent event) {
        Employee employee = (Employee) event.getObject();

        //if the department is changed:
        if (!employee.getOrganizationUnit().getDepartmentName().equals(employeeNewDeptName)){
            departmentSessionBean.adminChangeDepartment(employee.getEmployeeID(), employeeNewDeptName, employee.getOrganizationUnit().getDepartmentName());
            setAllActiveEmployees(employeeSessionBean.retrieveAllActiveEmployees());
            System.out.println("changed department successful");
        }
        
        
//        departmentSessionBean.changeDepartment(employee.getEmployeeID(), employee.getOrganizationUnit().getDepartmentName(), );
        FacesMessage msg = new FacesMessage("Employee edited for: ", ((Employee) event.getObject()).getEmployeeDisplayFirstName() + " " +  ((Employee) event.getObject()).getEmployeeDisplayLastName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
     public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit cancelled for: ", ((Employee) event.getObject()).getEmployeeDisplayFirstName() + " " +  ((Employee) event.getObject()).getEmployeeDisplayLastName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String lockoutEmployee(String employeeID){
        System.out.println("managed bean - lock out employee name:" + employeeID);
        Boolean lockSuccess = employeeSessionBean.lockoutEmployee(employeeID);
        
        System.out.println("lockout success:" + lockSuccess);
        if (lockSuccess){
            setAllActiveEmployees(employeeSessionBean.retrieveAllActiveEmployees());
            return "allEmployeeInfo";
        }
        
        else{
            return "singleEmployeeInfo";
        }
    }
     
    public String getEmployeeInfo(String employeeID){
        anEmployee = employeeSessionBean.getEmployeeUseID(employeeID);
        
        System.out.println("From admin managed bean: " + anEmployee.getEmployeeUserName());
        return "singleEmployeeInfo";
    }
    
    /**
     * @return the allEmployees
     */
    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    /**
     * @param allEmployees the allEmployees to set
     */
    public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }
    
    public List<String> getAllGenders(){  
        allGenders = new ArrayList();
        allGenders.add("Female");
        allGenders.add("Male");
        return allGenders;
    }

    /**
     * @param allGenders the allGenders to set
     */
    public void setAllGenders(List<String> allGenders) {
        this.allGenders = allGenders;
    }
    
   public void onCellEdit(CellEditEvent event) {
       
        
        Object oldValue = event.getOldValue();
        System.out.println("OLD VALUE: ON CELL EDIT" + oldValue.toString());
                
        Object newValue = event.getNewValue();
         System.out.println("NEW VALUE: ON CELL EDIT" + newValue.toString());
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    } 

    /**
     * @return the allDepts
     */
    public List<String> getAllDepts() {
        
        for (OrganizationUnit ou: allOUs ){
            if (!allDepts.contains(ou.getDepartmentName())){
            allDepts.add(ou.getDepartmentName());
            }
        }
        return allDepts;
    }

    /**
     * @param allDepts the allDepts to set
     */
    public void setAllDepts(List<String> allDepts) {
        this.allDepts = allDepts;
    }

    /**
     * @return the allRoles
     */
    public List<String> getAllRoles() {
        return allRoles;
    }

    /**
     * @param allRoles the allRoles to set
     */
    public void setAllRoles(List<String> allRoles) {
        this.allRoles = allRoles;
    }

    /**
     * @return the employeeNewDeptName
     */
    public String getEmployeeNewDeptName() {
        return employeeNewDeptName;
    }

    /**
     * @param employeeNewDeptName the employeeNewDeptName to set
     */
    public void setEmployeeNewDeptName(String employeeNewDeptName) {
        this.employeeNewDeptName = employeeNewDeptName;
    }

    /**
     * @return the employeeNewRole
     */
    public String getEmployeeNewRole() {
        return employeeNewRole;
    }

    /**
     * @param employeeNewRole the employeeNewRole to set
     */
    public void setEmployeeNewRole(String employeeNewRole) {
        this.employeeNewRole = employeeNewRole;
    }

    /**
     * @return the allOUs
     */
    public List<OrganizationUnit> getAllOUs() {
        return allOUs;
    }

    /**
     * @param allOUs the allOUs to set
     */
    public void setAllOUs(List<OrganizationUnit> allOUs) {
        this.allOUs = allOUs;
    }

    /**
     * @return the allDeptLocations
     */
    public List<String> getAllDeptLocations() {
        for(OrganizationUnit ou: allOUs){
            if (!allDeptLocations.contains(ou.getLocation())){
            allDeptLocations.add(ou.getLocation());
            }  
        }
        
        return allDeptLocations;
    }

    /**
     * @param allDeptLocations the allDeptLocations to set
     */
    public void setAllDeptLocations(List<String> allDeptLocations) {
        this.allDeptLocations = allDeptLocations;
    }

    /**
     * @return the employeeNewDeptLocation
     */
    public String getEmployeeNewDeptLocation() {
        return employeeNewDeptLocation;
    }

    /**
     * @param employeeNewDeptLocation the employeeNewDeptLocation to set
     */
    public void setEmployeeNewDeptLocation(String employeeNewDeptLocation) {
        this.employeeNewDeptLocation = employeeNewDeptLocation;
    }

    /**
     * @return the anEmployee
     */
    public Employee getAnEmployee() {
        return anEmployee;
    }

    /**
     * @param anEmployee the anEmployee to set
     */
    public void setAnEmployee(Employee anEmployee) {
        this.anEmployee = anEmployee;
    }

    /**
     * @return the allActiveEmployees
     */
    public List<Employee> getAllActiveEmployees() {
        return allActiveEmployees;
    }

    /**
     * @param allActiveEmployees the allActiveEmployees to set
     */
    public void setAllActiveEmployees(List<Employee> allActiveEmployees) {
        this.allActiveEmployees = allActiveEmployees;
    }

}
