package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String employeeID; //staff ID will be NRIC of employee
    private String employeeName;
    private String employeeDisplayName;
    private String employeePassword;
    
    private String  employeeRole;
    private String employeeDepartment;
    
    private String employeeDOB;
    private String employeeGender;
    private String employeeHpNumber;
    
    private boolean employeeLockOut;
    private boolean employeeAccountActivate;
    
     public void createEmployee(String employeeID,String employeeName,String employeeDisplayName,
                                String  employeeRole,String employeeDepartment,String employeeDOB,
                                String employeeGender,String employeeHpNumber){
        employeePassword="password";
        employeeAccountActivate=false;
        employeeLockOut=false;
        
    }
     
    public String getEmployeeID() {return employeeID; }
    public void setEmployeeID(String employeeID) {this.employeeID = employeeID;}

    public String getEmployeeName() {return employeeName;}
    public void setEmployeeName(String employeeName) {this.employeeName = employeeName;}

    public String getEmployeeDisplayName() {return employeeDisplayName;}
    public void setEmployeeDisplayName(String employeeDisplayName) {this.employeeDisplayName = employeeDisplayName;}

    public String getEmployeePassword() {return employeePassword;}
    public void setEmployeePassword(String employeePassword) {this.employeePassword = employeePassword;}

    public String getEmployeeRole() {return employeeRole;}
    public void setEmployeeRole(String employeeRole) {this.employeeRole = employeeRole;}

    public String getEmployeeDepartment() {return employeeDepartment;}
    public void setEmployeeDepartment(String employeeDepartment) {this.employeeDepartment = employeeDepartment;}

    public String getEmployeeDOB() {return employeeDOB;}
    public void setEmployeeDOB(String employeeDOB) {this.employeeDOB = employeeDOB;}

    public String getEmployeeGender() {return employeeGender;}
    public void setEmployeeGender(String employeeGender) {this.employeeGender = employeeGender;}

    public String getEmployeeHpNumber() {return employeeHpNumber;}
    public void setEmployeeHpNumber(String employeeHpNumber) {this.employeeHpNumber = employeeHpNumber;}

    public boolean isEmployeeLockOut() {return employeeLockOut;}
    public void setEmployeeLockOut(boolean employeeLockOut) {this.employeeLockOut = employeeLockOut;}

    public boolean isEmployeeAccountActivate() {return employeeAccountActivate;}
    public void setEmployeeAccountActivate(boolean employeeAccountActivate) {this.employeeAccountActivate = employeeAccountActivate;}
         
    
@Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeID != null ? employeeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeID == null && other.employeeID != null) || (this.employeeID != null && !this.employeeID.equals(other.employeeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeID + " ]";
    }
    
}
