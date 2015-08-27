/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless.session;

import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface EmployeeSessionBeanLocal {
    
    public void addEmployee(String employeeID,String employeeName,String employeeDisplayName,
                                String  employeeRole,String employeeDepartment,String employeeDOB,
                                String employeeGender,String employeeHpNumber);
}
