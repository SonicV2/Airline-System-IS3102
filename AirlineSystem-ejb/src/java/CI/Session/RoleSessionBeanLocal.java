/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface RoleSessionBeanLocal {
    public void addRole(String roleName);
    public List<String> retrive();
    public void addNewRole(Employee employee,String new_Role);
}
