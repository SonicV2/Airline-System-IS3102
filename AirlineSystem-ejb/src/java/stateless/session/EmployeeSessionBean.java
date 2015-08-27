package stateless.session;

import entity.Employee;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

     public void addEmployee(String employeeDisplayFirstName,String employeeDisplayLastName,
                                String  employeeRole,String employeeDepartment,Date employeeDOB,
                                String employeeGender,String employeeHpNumber,String employeeMailingAddress){
        
         
         
        Employee employee=new Employee();
        employee.createEmployee(employeeDisplayFirstName,employeeDisplayLastName,employeeRole,employeeDepartment,
                employeeDOB,employeeGender,employeeHpNumber,employeeMailingAddress);
        em.persist(employee);
        
    }
    
    public Employee getEmployee(String employeeID){
        Query query=em.createNamedQuery("SELECT e FROM Employee e WHERE e.employeeID=:inEmployeeID");
        query.setParameter("inEmployeeID", employeeID);
        Employee employee=null;
        
        try{
            employee=(Employee)query.getSingleResult();
        }catch(NoResultException ex){
            ex.printStackTrace();
        }
        return employee;
    }
   
}
