package managedbean;

import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import stateless.session.EmployeeSessionBeanLocal;


@ManagedBean
@RequestScoped
@Named(value="employeeManageBean")
public class EmployeeManageBean {
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    public EmployeeSessionBeanLocal getEmployeeSessionBean() {
        return employeeSessionBean;
    }
    
    String employeeID;
    String employeeName;
    String employeeDisplayFirstName;
    String employeeDisplayLastName;
    String employeeRole;
    String employeeDepartment;
    Date employeeDOB;
    String employeeGender;
    String employeeHpNumber;
    String employeeMailingAddress;

   
    
    public EmployeeManageBean() {
    }
    
    
    public void addEmployee(ActionEvent event){
        employeeSessionBean.addEmployee(employeeDisplayFirstName,employeeDisplayLastName,employeeRole, 
                                        employeeDepartment, employeeDOB, employeeGender, employeeHpNumber,
                                        employeeMailingAddress);
    
    }
    
    
    
    public void setEmployeeSessionBean(EmployeeSessionBeanLocal employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }

    
     public String getEmployeeDisplayLastName() {
        return employeeDisplayLastName;
    }

    public void setEmployeeDisplayLastName(String employeeDisplayLastName) {
        this.employeeDisplayLastName = employeeDisplayLastName;
    }

    public String getEmployeeMailingAddress() {
        return employeeMailingAddress;
    }

    public void setEmployeeMailingAddress(String employeeMailingAddress) {
        this.employeeMailingAddress = employeeMailingAddress;
    }
    
    
    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeDisplayFirstName() {
        return employeeDisplayFirstName;
    }

    public void setEmployeeDisplayFirstName(String employeeDisplayFirstName) {
        this.employeeDisplayFirstName = employeeDisplayFirstName;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public Date getEmployeeDOB() {
        return employeeDOB;
    }

    public void setEmployeeDOB(Date employeeDOB) {
        this.employeeDOB = employeeDOB;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    public String getEmployeeHpNumber() {
        return employeeHpNumber;
    }

    public void setEmployeeHpNumber(String employeeHpNumber) {
        this.employeeHpNumber = employeeHpNumber;
    }
        
    
}
