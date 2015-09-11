/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.OrganizationUnit;
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
public class DepartmentSessionBean implements DepartmentSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    OrganizationUnit department = new OrganizationUnit();

    public void addDepartment(String departName, String departLocation) {

        department.create(departName, departLocation);
        em.persist(department);
    }
    
    @Override
    public List<String> retrive() {
        ArrayList<String> list = new ArrayList();
        try {
            
            System.out.println("Look Look Here retrive");
            Query q = em.createQuery("SELECT a FROM OrganizationUnit a");

            List<OrganizationUnit> results = q.getResultList();
            if (!results.isEmpty()) {
                for (OrganizationUnit org : results) {
                    list.add(org.getDepartmentName() + "(" +org.getLocation() + ")");
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
    public List<OrganizationUnit> retrieveAllDepts(){
        
        List<OrganizationUnit> allDepts = new ArrayList<OrganizationUnit>();
        
        try{
            Query q = em.createQuery("SELECT a from OrganizationUnit a");
            
            List<OrganizationUnit> results = q.getResultList();
            if (!results.isEmpty()){
                
                allDepts = results;
                System.out.println("number of orgUNITS:" + allDepts.size()); 
                System.out.println(allDepts.get(1).getDepartmentName());
                System.out.println("retrieved!");
            }else
            {
                allDepts = null;
                System.out.println("no dept!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allDepts;
    }

}
