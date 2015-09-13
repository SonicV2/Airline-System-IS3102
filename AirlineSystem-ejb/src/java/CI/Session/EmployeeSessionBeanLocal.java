/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    public void addEmployee(String userID,String employeeID,String employeeDisplayFirstName,String employeeDisplayLastName,
                            String  employeeRole,String employeeDepartment,Date employeeDOB,
                            String employeeGender,String employeeHpNumber,String employeeMailingAddress,
                            String employeeOfficeNumber,String employeePrivateEmail);
    
    public Employee getEmployee(String employeeUserName);
    public void logLogIn(String userID);
    public void logPasswordChange(String userID);
    public Employee getEmployeeUseID(String employeeID); //get employee by using ID
    
    public String generateUserName(String employeeFirstName, String employeeLastName);
    public void hashPwd(String userName);
    public boolean isSameHash(String userName, String pwd);
    public void hashNewPwd(String userName, String pwd);
    public void employeeActivate(String userName);
    
    public void updateInfo(String employeeUserName, Date employeeDOB,String employeeGender, String employeeHomeAddress, String employeeOfficeNumber,
    String employeeHpNumber, String employeePrivateEmail);
    
    public void addCabinCrew(String employeeID, String employeeDisplayFirstName, String employeeDisplayLastName,
            String employeeDepartment,Date employeeDOB,String employeeGender, String employeeHpNumber, 
            String employeeMailingAddress, String employeeOfficeNumber, String employeePrivateEmail,
            String experience, List<String>language, String position);
    
}
