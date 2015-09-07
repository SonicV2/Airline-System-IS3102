package CI.Entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String employeeID; //Unique identifier for user retrieve

    private String employeeUserName; // Unique User log-in name
    private String employeeDisplayFirstName; //employee's actual name
    private String employeeDisplayLastName;
    private String employeePassword;
    private String employeeEmailAddress; //company email @merlion.com.sg
    private String employeeMailingAddress;
    private String employeeRole;
    private String employeeDepartment;
    private String employeeGender;
    private String employeeHpNumber;
    private String employeeOfficeNumber;
    @Temporal(TemporalType.DATE)
    private Date employeeDOB;

    private boolean employeeLockOut;
    private boolean employeeAccountActivate;

    @OneToOne(cascade = {CascadeType.PERSIST})
    private Salt salt;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<Message> msgs = new ArrayList<Message>();
    
    
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private OrganizationUnit organizationUnit=new OrganizationUnit();
    
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Role> roles= new ArrayList<Role>(); 
    
    public void createEmployee(String employeeID, String employeeDisplayFirstName, String employeeDisplayLastName,
            /*String employeeRole, String employeeDepartment,*/ Date employeeDOB,
            String employeeGender, String employeeHpNumber, String employeeMailingAddress, String employeeOfficeNumber) {
        this.employeeID = employeeID;
        this.employeeDisplayFirstName = employeeDisplayFirstName;
        this.employeeDisplayLastName = employeeDisplayLastName;
        //this.employeeRole = employeeRole;
        //this.employeeDepartment = employeeDepartment;
        this.employeeDOB = employeeDOB;
        this.employeeGender = employeeGender;
        this.employeeHpNumber = employeeHpNumber;
        this.employeeMailingAddress = employeeMailingAddress;
        this.employeeOfficeNumber = employeeOfficeNumber;

        this.employeePassword = "password";
        this.employeeAccountActivate = false;
        this.employeeLockOut = false;

    }

    public String getEmployeeDisplayLastName() {
        return employeeDisplayLastName;
    }

    public void setEmployeeDisplayLastName(String employeeDisplayLastName) {
        this.employeeDisplayLastName = employeeDisplayLastName;
    }

    public String getEmployeeEmailAddress() {
        return employeeEmailAddress;
    }

    public void setEmployeeEmailAddress(String employeeEmailAddress) {
        this.employeeEmailAddress = employeeEmailAddress;
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

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
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

    public boolean isEmployeeLockOut() {
        return employeeLockOut;
    }

    public void setEmployeeLockOut(boolean employeeLockOut) {
        this.employeeLockOut = employeeLockOut;
    }

    public boolean isEmployeeAccountActivate() {
        return employeeAccountActivate;
    }

    public void setEmployeeAccountActivate(boolean employeeAccountActivate) {
        this.employeeAccountActivate = employeeAccountActivate;
    }

    public String getEmployeeOfficeNumber() {
        return employeeOfficeNumber;
    }

    public void setEmployeeOfficeNumber(String employeeOfficeNumber) {
        this.employeeOfficeNumber = employeeOfficeNumber;
    }

    /**
     * @return the salt
     */
    public Salt getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(Salt salt) {
        this.salt = salt;
    }

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

    /**
     * @return the msgs
     */
    public List<Message> getMsgs() {
        return msgs;
    }

   
    public void setMsgs(List<Message> msgs) {
        this.msgs = msgs;
    }

        public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    
    

}
