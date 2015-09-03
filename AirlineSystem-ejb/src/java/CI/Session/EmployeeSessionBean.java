package CI.Session;

import CI.Entity.Employee;
import CI.Entity.Salt;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.ejb.Stateless;
import javax.mail.NoSuchProviderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    FileHandler fh;  
    
    
  
    @Override
    public void addEmployee(String userID,String employeeID, String employeeDisplayFirstName, String employeeDisplayLastName,
            String employeeRole, String employeeDepartment, Date employeeDOB,
            String employeeGender, String employeeHpNumber, String employeeMailingAddress, String employeeOfficeNumber) {

        Employee employee = new Employee();
        employee.createEmployee(employeeID, employeeDisplayFirstName, employeeDisplayLastName, employeeRole, employeeDepartment,
                employeeDOB, employeeGender, employeeHpNumber, employeeMailingAddress, employeeOfficeNumber);

        String userName = generateUserName(employeeDisplayFirstName, employeeDisplayLastName);
        employee.setEmployeeUserName(userName);

        employee.setEmployeeEmailAddress(userName + "@merlion.com.sg");

        em.persist(employee);
        Logger logger = Logger.getLogger(EmployeeSessionBean.class.getName());
        
        try {   
        fh = new FileHandler("%h/addEmployee.txt",99999,1,true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        } catch (SecurityException e) {  
        e.printStackTrace();  
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ userID 
                + "has added Employee: " + employeeID);
        fh.close();

    }

//get employee by using UserName
    @Override
    public Employee getEmployee(String employeeUserName) {
        Employee employee = new Employee();
        try {

            Query q = em.createQuery("SELECT a FROM Employee " + "AS a WHERE a.employeeUserName=:userName");
            q.setParameter("userName", employeeUserName);

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

// Get employee by using ID
    @Override
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

//    System generates user name for new employee Rule: Max letter of first and last name will be 5, repeated username will be append number behind
    @Override
    public String generateUserName(String employeeFirstName, String employeeLastName) {
        int firstNameLength = employeeFirstName.length();
        int lastNameLength = employeeLastName.length();
        String firstName;
        String lastName;

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

        //System.out.println(employees.size());
        int i = 1;
        int len = userName.length();
        //System.out.println(len);
        //System.out.println(userName);
        for (String e : employees) {

            if (e.equals(userName)) {
                userName = userName.substring(0, len) + i + "";
                //System.out.println(userName);
                i++;
            }
        }
        return userName;
    }

    // Password encryption use MD 5 hashing
    public String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt.toString();
    }

    public String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /*
     To hash user's defalut "password"  
     */
    @Override
    public void hashPwd(String userID) {
        Employee employee = getEmployeeUseID(userID);
        try {
            String saltCode = generateSalt();
            String hashedPwd = getSecurePassword(employee.getEmployeePassword(), saltCode);
            employee.setEmployeePassword(hashedPwd);
            Salt salt = new Salt();
            salt.create(saltCode);
            employee.setSalt(salt);
            em.persist(salt);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EmployeeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmployeeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.security.NoSuchProviderException ex) {
            Logger.getLogger(EmployeeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     To hash new password when user change their password
    */
    @Override
    public void hashNewPwd(String userName, String pwd) {
        Employee employee = getEmployee(userName);
        try {
            String saltCode = generateSalt();
            String hashedPwd = getSecurePassword(pwd, saltCode);
            employee.setEmployeePassword(hashedPwd);
            employee.getSalt().setSaltCode(saltCode);
            em.persist(employee);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EmployeeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmployeeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.security.NoSuchProviderException ex) {
            Logger.getLogger(EmployeeSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     For Login 
     Check if the two hash are same
     */

    @Override
    public boolean isSameHash(String userName, String pwd) {

        Employee employee = getEmployee(userName);
        String saltCode = employee.getSalt().getSaltCode();
        String rehash = getSecurePassword(pwd, saltCode);
        if (employee.getEmployeePassword().equals(rehash)) {
            return true;
        } else {
            return false;
        }
    }
    
    /* To set employee to be active after they changed their password*/
    
    public void employeeActivate(String userName){
        Employee employee = getEmployee(userName);
        employee.setEmployeeAccountActivate(true);
        em.persist(employee);
    }
   

}
