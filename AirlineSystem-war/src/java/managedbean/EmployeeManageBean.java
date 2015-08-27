package managedbean;

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
    String employeeDisplayName;
    String  employeeRole;
    String employeeDepartment;
    String employeeDOB;
    String employeeGender;
    String employeeHpNumber;
    
    
    public EmployeeManageBean() {
    }
    
    
    public void addEmployee(ActionEvent event){
        employeeSessionBean.addEmployee(employeeID, employeeName, employeeDisplayName, employeeRole, employeeDepartment, employeeDOB, employeeGender, employeeHpNumber);
    
    }
    
    
    
    public void setEmployeeSessionBean(EmployeeSessionBeanLocal employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
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

    public String getEmployeeDisplayName() {
        return employeeDisplayName;
    }

    public void setEmployeeDisplayName(String employeeDisplayName) {
        this.employeeDisplayName = employeeDisplayName;
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

    public String getEmployeeDOB() {
        return employeeDOB;
    }

    public void setEmployeeDOB(String employeeDOB) {
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
