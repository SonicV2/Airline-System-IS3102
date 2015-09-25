/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.AccessRight;
import CI.Entity.Employee;
import CI.Entity.Role;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface RoleSessionBeanLocal {
//    public void addRole(String roleName,String accessRightName);
    public void addRole(String roleName, List<AccessRight> allAccessRights);
    public List<String> retrive();
    public void addNewRole(Employee employee,String new_Role);
    public String deleteEmployeeRole(Employee employee, List<String> roles);
    public String deleteRole(String roleName);
    public List<Role> retrieveAllRoles();
//    public String updateRoleAccessRight(String roleName, Boolean accessCreate, Boolean accessDelete, Boolean accessAssign, Boolean accessView );
    public String updateRoleName(Role oldRole, Role newRole);
    public List<AccessRight> getAccessRights(Long roleID);
    public Role getRoleUseID(Long roleID);
}
