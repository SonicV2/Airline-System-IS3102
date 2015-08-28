package stateless.session;

import entity.Employee;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public void addEmployee(String employeeID,String employeeDisplayFirstName, String employeeDisplayLastName,
                            String employeeRole, String employeeDepartment, Date employeeDOB,
                            String employeeGender, String employeeHpNumber, String employeeMailingAddress,
                            String employeeOfficeNumber) {

        Employee employee = new Employee();
        employee.createEmployee(employeeID,employeeDisplayFirstName, employeeDisplayLastName, 
                                employeeRole, employeeDepartment,employeeDOB, employeeGender, 
                                employeeHpNumber, employeeMailingAddress,employeeOfficeNumber);
       
        String userName = generateUserName(employeeDisplayFirstName, employeeDisplayLastName);
        employee.setEmployeeUserName(userName);
        
        employee.setEmployeeEmailAddress(userName+"@merlion.com.sg");
        em.persist(employee);

    }
 
//get employee by using UserName
    @Override  
    public Employee getEmployee(String employeeUserName) {
        Employee employee=new Employee();
        try{
            
            Query q=em.createQuery("SELECT a FROM Employee " + "AS a WHERE a.employeeUserName=:userName");
            q.setParameter("userName", employeeUserName);
            
            List results=q.getResultList();
            if (!results.isEmpty()){
                employee = (Employee)results.get(0);         
            }else employee=null;

        }catch(EntityNotFoundException enfe){
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return employee;
    }

// Get employee by using ID
    @Override  
    public Employee getEmployeeUseID(String employeeID) {
        Employee employee=new Employee();
        try{
            
            Query q=em.createQuery("SELECT a FROM Employee " + "AS a WHERE a.employeeID=:id");
            q.setParameter("id", employeeID);
            
            List results=q.getResultList();
            if (!results.isEmpty()){
                employee = (Employee)results.get(0);
            
            }else employee=null;      
        }catch(EntityNotFoundException enfe){
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return employee;
    }
    
//    System generates user name for new employee
    @Override
    public String generateUserName(String employeeFirstName, String employeeLastName) {
        int firstNameLength = employeeFirstName.length();
        int lastNameLength = employeeLastName.length();
        String firstName;
        String lastName;
        String employeeUserName = "";

        if (firstNameLength >= 5) {
            firstName = employeeFirstName.substring(0, 5);
        } else {
            firstName = employeeFirstName;
        }

        if (lastNameLength >= 5) {
            lastName = employeeLastName.substring(0, 5);
        } else {
            lastName = employeeLastName;
        }

        String userName = firstName + lastName;

        Query query = em.createQuery("SELECT e.employeeUserName FROM Employee e");

        //query.setParameter("employeeUserName", userName);
        List<String> employees = query.getResultList();

        System.out.println(employees.size());
        int i = 1;
        int len = userName.length();
        //System.out.println(len);
        //System.out.println(userName);
        for (String e : employees) {

            if (e.equals(userName)) {
                userName = userName.substring(0, len) + i + "";
                System.out.println(userName);
                i++;
            }           
        }
        return userName;
    }
    
    
    
    
}
