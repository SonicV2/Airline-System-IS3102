/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.OrganizationUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author HOULIANG
 */
@Stateless
public class DepartmentSessionBean implements DepartmentSessionBeanLocal {

     @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    OrganizationUnit department=new OrganizationUnit();
     
    public void addDepartment(String departName, String departLocation){
        
        
        department.create(departName, departLocation);
        em.persist(department);
    }
}
