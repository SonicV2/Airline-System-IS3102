/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

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
     
     Role role=new Role();
    
    @Override
    public void addRole(String roleName) {

        role.create(roleName);
        em.persist(role);
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
                    list.add(role.getRoleName());
                }

            } else {
                list = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return list;
    }
    
}
