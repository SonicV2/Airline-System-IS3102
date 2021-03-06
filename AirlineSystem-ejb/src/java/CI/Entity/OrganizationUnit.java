package CI.Entity;

import CI.Entity.Employee;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="OrganizationUnit")
public class OrganizationUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private Long departmentID;
    
    private String departmentName;
    
    private String location;
    
    
    @OneToMany(cascade = {CascadeType.PERSIST} , mappedBy="organizationUnit")
    private List<Employee> employee=new ArrayList<Employee>();
    
    
    
    /*
    1.HR (*)
    2.Ground ops 
    3.Flight ops
    4.cabin crew
    5.Marketing
    6.IT (our super admin)
    7.Customer service
    8.Finance
    */
    
    
    public OrganizationUnit (){
        setDepartmentID(System.nanoTime());
    }
    
    public void create(String departName, String departLocation){
        this.departmentName=departName;
        this.location=departLocation;
    }
    
    

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
   
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (departmentName!= null ? departmentName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganizationUnit)) {
            return false;
        }
        OrganizationUnit other = (OrganizationUnit) object;
        if ((this.departmentName == null && other.departmentName != null) || (this.departmentName != null && !this.departmentName.equals(other.departmentName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Organization.OrganizationUnit[ id=" + departmentName + " ]";
    }

    /**
     * @return the employee
     */
    public List<Employee> getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the departmentID
     */
    public Long getDepartmentID() {
        return departmentID;
    }

    /**
     * @param departmentID the departmentID to set
     */
    public void setDepartmentID(Long departmentID) {
        this.departmentID = departmentID;
    }
    
}
