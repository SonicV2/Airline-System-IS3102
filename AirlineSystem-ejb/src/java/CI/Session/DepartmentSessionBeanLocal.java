/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import CI.Entity.OrganizationUnit;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface DepartmentSessionBeanLocal {
    public void addDepartment(String departName, String departLocation);
    public List<String> retrive();
    public String searchEmployee(String staffID);
    public String changeDepartment(String staffID,String deptName,String deptNameOld);
    public String adminChangeDepartment(String staffID,String deptName,String deptNameOld);
    public List<OrganizationUnit> retrieveAllDepts();
    public OrganizationUnit getDepartment(String deptName);
    public OrganizationUnit getDepartmentUseID(Long deptID);
    public String deleteOrgUnit(String OUName);
    public String updateOrgUnit(OrganizationUnit oldOUnit, OrganizationUnit newOUnit);
    public void editGender(Employee edited);
    public Employee searchStaff(String id);
}
