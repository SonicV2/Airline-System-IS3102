/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless.session;

import entity.Employee;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    public void addEmployee(String employeeDisplayFirstName,String employeeDisplayLastName,
                                String  employeeRole,String employeeDepartment,Date employeeDOB,
                                String employeeGender,String employeeHpNumber,String employeeMailingAddress);
    
    public Employee getEmployee(String employeeID);
}
