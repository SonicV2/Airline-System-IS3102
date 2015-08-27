package stateless.session;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

     public void addEmployee(String employeeID,String employeeName,String employeeDisplayName,
                                String  employeeRole,String employeeDepartment,String employeeDOB,
                                String employeeGender,String employeeHpNumber){
        
         
         
        Employee employee=new Employee();
        employee.createEmployee(employeeID,employeeName,employeeDisplayName,employeeRole,employeeDepartment,
                employeeDOB,employeeGender,employeeHpNumber);
        em.persist(employee);
        
    }
    
   
}
