package stateless.session;

import entity.Employee;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public void addEmployee(String employeeDisplayFirstName, String employeeDisplayLastName,
            String employeeRole, String employeeDepartment, Date employeeDOB,
            String employeeGender, String employeeHpNumber, String employeeMailingAddress) {

        Employee employee = new Employee();
        employee.createEmployee(employeeDisplayFirstName, employeeDisplayLastName, employeeRole, employeeDepartment,
                employeeDOB, employeeGender, employeeHpNumber, employeeMailingAddress);
        String userName = generateUserName(employeeDisplayFirstName, employeeDisplayLastName);
        employee.setEmployeeUserName(userName);
        em.persist(employee);

    }

    @Override
    public Employee getEmployee(String employeeID) {
        Query query = em.createNamedQuery("SELECT e FROM Employee e WHERE e.employeeID=:inEmployeeID");
        query.setParameter("inEmployeeID", employeeID);
        Employee employee = null;

        try {
            employee = (Employee) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
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
