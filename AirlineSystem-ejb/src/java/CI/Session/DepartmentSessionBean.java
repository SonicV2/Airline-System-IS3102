/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import CI.Entity.OrganizationUnit;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HOULIANG
 */
@Stateless
public class DepartmentSessionBean implements DepartmentSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    OrganizationUnit department = new OrganizationUnit();

    public void addDepartment(String departName, String departLocation) {

        department.create(departName, departLocation);
        em.persist(department);
    }
    
    @Override
    public List<String> retrive() {
        ArrayList<String> list = new ArrayList();
        try {
            
            System.out.println("Look Look Here retrive");
            Query q = em.createQuery("SELECT a FROM OrganizationUnit a");

            List<OrganizationUnit> results = q.getResultList();
            if (!results.isEmpty()) {
                for (OrganizationUnit org : results) {
                    list.add(org.getDepartmentName() + "(" +org.getLocation() + ")");
                }

            } else {
                list = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return list;
    }
    

    @Override
    public List<OrganizationUnit> retrieveAllDepts(){
        
        List<OrganizationUnit> allDepts = new ArrayList<OrganizationUnit>();
        
        try{
            Query q = em.createQuery("SELECT a from OrganizationUnit a");
            
            List<OrganizationUnit> results = q.getResultList();
            if (!results.isEmpty()){
                
                allDepts = results;
                
            }else
            {
                allDepts = null;
                System.out.println("no dept!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allDepts;
    }

    // Return employee department name
    @Override
    public String searchEmployee(String staffID){   
        Employee employee = getEmployeeUseID(staffID);
        if(employee == null){
            return "User Not Exist!";
        }else{
            String dept = employee.getOrganizationUnit().getDepartmentName() + "(" +employee.getOrganizationUnit().getLocation() + ")";
            System.out.println("Session Bean Dept: "+ dept);
            return dept;
        }
    }
        
    @Override
    public String changeDepartment(String staffID,String deptNameCom,String deptNameOldCom){
        String deptName = deptNameCom.substring(0, deptNameCom.indexOf("("));
        String deptNameOld = deptNameOldCom.substring(0, deptNameOldCom.indexOf("("));
        
        Employee employee = getEmployeeUseID(staffID);
        OrganizationUnit ouOld =getDepartment(deptNameOld);
        OrganizationUnit ouNew = getDepartment(deptName);
        
        List<Employee> employees = ouOld.getEmployee();
        employees.remove(employee);
        ouOld.setEmployee(employees);
        
        List<Employee> employeesNew = ouNew.getEmployee();
        employeesNew.add(employee);
        ouNew.setEmployee(employeesNew);
        
        employee.setOrganizationUnit(ouNew);
        em.merge(employee);
        
        return "Successful!";
        
    }
    
    public OrganizationUnit getDepartment(String deptName){
        Query q = em.createQuery("SELECT a FROM OrganizationUnit a WHERE a.departmentName =:deptName");
        q.setParameter("deptName", deptName);
        
         List<OrganizationUnit> results = q.getResultList();
         
         return results.get(0);
         
    }
    
    public Employee getEmployeeUseID(String employeeID) {
        Employee employee = new Employee();
        try {

            Query q = em.createQuery("SELECT a FROM Employee " + "AS a WHERE a.employeeID=:id");
            q.setParameter("id", employeeID);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                employee = (Employee) results.get(0);

            } else {
                employee = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return employee;
    }

}
