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
     
     Role role_db=new Role();
     
     
     private List<Role> rolelist=new ArrayList<Role>();
     private Role role;
     List<Employee> employeelist_role=new ArrayList<Employee>();
     
     
    @Override
    public void addRole(String roleName,List<String> accessRight) {

       AccessRight ar= new AccessRight();
        
        
        role_db.create(roleName.toUpperCase());
        
        if(accessRight.contains("accessAdd")){
            System.out.println("sessionBean: accessright");
            ar.setAccessAdd(true);         
        }if(accessRight.contains("accessDelete")){
            ar.setAccessDelete(true);
        }if(accessRight.contains("accessAssign")){
            ar.setAccessAssign(true);
        }if(accessRight.contains("accessCreate")){
            ar.setAccessCreate(true);
        }if(accessRight.contains("accessView")){
            ar.setAccessView(true);
        }
        role_db.setAccess(ar);
        em.persist(role_db);
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
                   if(!role.getRoleName().equals("SUPER ADMIN")){  //comment out if adding Super Admin
                    list.add(role.getRoleName());
                  }
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
    public String deleteEmployeeRole(Employee employee, List<String> roles){
        List<Role> roleList = employee.getRoles();
        for(String s : roles){
            Role r = new Role();
            r = getRole(s); //get role entity
            List<Employee> employeeLists = r.getEmployees(); // get all employee from one role
            if(roleList.size()==1 && roleList.contains(r)){
                return "Cannot Delete!";
            }
            roleList.remove(r); // employee remove this role --> employee is the owner entity
            employeeLists.remove(employee); // remove employee from this role
            r.setEmployees(employeeLists);
        }
        employee.setRoles(roleList);
        em.merge(employee);
        return "Successful!";
       
    }
    
    @Override
    public String deleteRole(String roleName){
        //String msg;
        Role role = new Role();
        role = getRole(roleName);
        List<Employee> employees = role.getEmployees();
        
        for(Employee e : employees){
            List<Role> roles = e.getRoles();
            if(roles.size()==1 && roles.contains(role)){
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
    
    public void addNewRole(Employee employee, String new_Role){
       rolelist=employee.getRoles();

       role=getRole(new_Role);
       rolelist.add(role);
       employee.setRoles(rolelist);
       
       employeelist_role=role.getEmployees();
       
       employeelist_role.add(employee);
       role.setEmployees(employeelist_role);
  
        em.merge(employee);
        em.merge(role);
     
    }
    
    public Role getRole(String role){
        
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
