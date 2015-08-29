package managedbean;

import entity.Employee;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import stateless.session.EmployeeSessionBeanLocal;

@ManagedBean
@SessionScoped
//@RequestScoped
@Named(value = "employeeManageBean")
public class EmployeeManageBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    public EmployeeSessionBeanLocal getEmployeeSessionBean() {
        return employeeSessionBean;
    }

    String employeeID;
    String employeeUserName;
    String employeeDisplayFirstName;
    String employeeDisplayLastName;
    String employeeRole;
    String employeeDepartment;
    Date employeeDOB;
    String employeeGender;
    String employeeHpNumber;
    String employeeMailingAddress;
    String employeeOfficeNumber;
    String employeeEmailAddress;
    Employee employee;

    public EmployeeManageBean() {
    }

    public void addEmployee(ActionEvent event) {
        employeeSessionBean.addEmployee(employeeID, employeeDisplayFirstName, employeeDisplayLastName, employeeRole,
                employeeDepartment, employeeDOB, employeeGender, employeeHpNumber,
                employeeMailingAddress, employeeOfficeNumber);

        employee = getEmployee(employeeID); //in order to get the employeeUserName and email which is generated after the creation of employee
        
        employeeUserName = employee.getEmployeeUserName();
        employeeEmailAddress = employee.getEmployeeEmailAddress();
        employeeSessionBean.hashPwd(employeeID);

    }

    public Employee getEmployee(String employeeID) {
        Employee employee = employeeSessionBean.getEmployeeUseID(employeeID);
        return employee;
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

    public String getEmployeeUserName() {
        return employeeUserName;
    }

    public void setEmployeeUserName(String employeeUserName) {
        this.employeeUserName = employeeUserName;
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

    public String getEmployeeOfficeNumber() {
        return employeeOfficeNumber;
    }

    public void setEmployeeOfficeNumber(String employeeOfficeNumber) {
        this.employeeOfficeNumber = employeeOfficeNumber;
    }

    public String getEmployeeEmailAddress() {
        return employeeEmailAddress;
    }

    public void setEmployeeEmailAddress(String employeeEmailAddress) {
        this.employeeEmailAddress = employeeEmailAddress;
    }
}
