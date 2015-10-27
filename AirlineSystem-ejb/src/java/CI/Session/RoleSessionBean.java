/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.AccessRight;
import CI.Entity.Employee;
import CI.Entity.OrganizationUnit;
import CI.Entity.Role;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HOULIANG
 */
@Stateless
public class RoleSessionBean implements RoleSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

   

    private List<Role> rolelist = new ArrayList<Role>();
    private Role role;
    List<Employee> employeelist_role = new ArrayList<Employee>();

    @Override
    public void addRole(String roleName, List<AccessRight> allAccessRights) {
        Role role_db = new Role();
        System.out.println("----------------------------"+role_db.getRoleID());
        role_db.create(roleName.toUpperCase());
        role_db.setAccessRights(allAccessRights);

        if (allAccessRights != null) {
            for (AccessRight aar : allAccessRights) {
                System.out.println("session bean: " + aar);
            }
        }
        
         System.out.println("----------------------------1"+role_db.getRoleID());
        em.flush();
        em.merge(role_db);
    }

    @Override
    public void addNewAccessRight(Long roleID, List<AccessRight> newAccessRights) {

        Role thisRole = getRoleUseID(roleID);
        List<AccessRight> currentARs = new ArrayList<AccessRight>();
        currentARs = thisRole.getAccessRights();
        currentARs.addAll(newAccessRights);
        thisRole.setAccessRights(currentARs);

        em.merge(thisRole);

    }

    @Override
    public List<AccessRight> getAccessRights(Long roleID) {
        Role oneRole = new Role();
        List<AccessRight> roleAccessRights = new ArrayList<AccessRight>();

        try {

            Query q = em.createQuery("SELECT a FROM Role " + "AS a WHERE a.roleID=:id");
            q.setParameter("id", roleID);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                oneRole = (Role) results.get(0);

            } else {
                oneRole = null;
            }

            if (oneRole.getAccessRights() != null) {
                roleAccessRights = oneRole.getAccessRights();

                System.out.println("sessionbean success!" + roleAccessRights.size());

            } else {
                roleAccessRights = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return roleAccessRights;

    }

    @Override
    public String updateRoleName(Role oldRole, Role newRole) {

        System.out.println("from session bean:" + oldRole.getRoleID() + oldRole.getRoleName());

        if (em.find(Role.class, oldRole.getRoleID()) == null) {
            throw new IllegalArgumentException("Unknown Role id");
        }

        newRole.setRoleName(newRole.getRoleName().toUpperCase());
        em.merge(newRole);
        return "Update Sucess";
    }

    @Override
    public Role getRoleUseID(Long roleID) {
        Query q = em.createQuery("SELECT a FROM Role a WHERE a.roleID =:roleID");
        q.setParameter("roleID", roleID);
        List<Role> results = q.getResultList();

        return results.get(0);
    }

    @Override
    public List<String> retrive() {
        ArrayList<String> list = new ArrayList();
        try {

            System.out.println("Look Look Here retrive");
            Query q = em.createQuery("SELECT a FROM Role a");

            List<Role> results = q.getResultList();
            if (!results.isEmpty()) {
                for (Role role : results) {
//                 if(!role.getRoleName().equals("SUPER ADMIN")){  //comment out if adding Super Admin
                    list.add(role.getRoleName());
//                   }
                }

            } else {
                list = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return list;
    }

    @Override
    public List<Role> retrieveAllRoles() {
        List<Role> allRoles = new ArrayList<Role>();

        try {
            Query q = em.createQuery("SELECT a FROM Role a");
            List<Role> results = q.getResultList();

            if (!results.isEmpty()) {
                allRoles = results;

            } else {
                allRoles = null;
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allRoles;
    }

    @Override
    public String deleteEmployeeRole(Employee employee, List<String> roles) {
        List<Role> roleList = employee.getRoles();
        for (String s : roles) {
            Role r = new Role();
            r = getRole(s); //get role entity
            List<Employee> employeeLists = r.getEmployees(); // get all employee from one role
            if (roleList.size() == 1 && roleList.contains(r)) {
                return "You cannot delete this role!";
            }
            roleList.remove(r); // employee remove this role --> employee is the owner entity
            employeeLists.remove(employee); // remove employee from this role
            r.setEmployees(employeeLists);
        }
        employee.setRoles(roleList);
        em.merge(employee);
        return "Delete role successful!";

    }

    @Override
    public String deleteRole(String roleName) {
        //String msg;
        Role role = new Role();
        role = getRole(roleName);
        List<Employee> employees = role.getEmployees();

        for (Employee e : employees) {
            List<Role> roles = e.getRoles();
            if (roles.size() == 1 && roles.contains(role)) {
                return "Cannot Delete!";
            }
            roles.remove(role);
            e.setRoles(roles);
            em.merge(e);
        }

        em.remove(role);
        em.flush();
        return "Successful!";

    }

    @Override
    public String deleteAccessRight(Long accessRightID, Long roleID) {

        try {
            Role thisRole = getRoleUseID(roleID);
            List<AccessRight> currentARs = new ArrayList<AccessRight>();
            currentARs = thisRole.getAccessRights();
            for (AccessRight ar : currentARs) {
                if (ar.getId().equals(accessRightID)) {
                    currentARs.remove(ar);
                }
            }
            thisRole.setAccessRights(currentARs);
            em.merge(thisRole);
            em.flush();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return "Removed Access Right Successfully!";

    }
    
    public void addNewRole(Employee employee, String new_Role) {
        rolelist = employee.getRoles();

        role = getRole(new_Role);
        rolelist.add(role);
        employee.setRoles(rolelist);

        employeelist_role = role.getEmployees();

        employeelist_role.add(employee);
        role.setEmployees(employeelist_role);

        em.merge(employee);
        em.merge(role);

    }

    public String changeRole(String employeeID, String newRole, String oldRole) {

        Employee employee = getEmployeeUserID(employeeID);
        Role roleOld = getRole(oldRole);
        Role roleNew = getRole(newRole);

        List<Employee> employees = roleOld.getEmployees();
        employees.remove(employee);
        roleOld.setEmployees(employees);

        List<Employee> employeesNew = roleNew.getEmployees();
        employeesNew.add(employee);
        roleNew.setEmployees(employeesNew);

//        employee.setOrganizationUnit(ouNew);
        employee.setRoles(rolelist);
        em.merge(employee);

        return "Changed role successful!";
    }

    public Employee getEmployeeUserID(String employeeID) {
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

    public Role getRole(String role) {

        Role role1 = new Role();
        try {

            Query q = em.createQuery("SELECT a FROM Role " + "AS a WHERE a.roleName=:roleName");
            q.setParameter("roleName", role);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                role1 = (Role) results.get(0);

            } else {
                role1 = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return role1;
    }
}
