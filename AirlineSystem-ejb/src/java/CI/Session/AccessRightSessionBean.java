/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.AccessRight;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author Yuqing
 */
@Stateless
public class AccessRightSessionBean implements AccessRightSessionBeanLocal, AccessRightSessionBeanRemote {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    AccessRight accessRight;
    
@Override    
public String addAccessRight(String accessRightName){
    accessRight = new AccessRight();
    accessRight.create(accessRightName);
    em.persist(accessRight);
    return "Access Right Added";
}

public void deleteAccessRight(Long accessRightID){
    em.remove(getAccessRightID(accessRightID));
    em.flush();
}

public AccessRight getAccessRightID(Long accessRightID) {
    AccessRight oneAccessRight = new AccessRight();
    try{
        Query q = em.createQuery("SELECT a FROM AccessRight " + "AS a WHERE a.accessRightID=:id");
        q.setParameter("id", accessRightID);
        List results = q.getResultList();
            if (!results.isEmpty()) {
                oneAccessRight = (AccessRight) results.get(0);

            } else {
                oneAccessRight = null;
            }
        

    }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
    }
    
    return oneAccessRight;
}


@Override
public List<AccessRight> retrieveAllAccessRight(){
    List<AccessRight> allAccessRight = new ArrayList<AccessRight> ();
    
    try{
    Query q = em.createQuery("SELECT a FROM AccessRight a");
    List<AccessRight> results = q.getResultList();
    
            if (!results.isEmpty()) {
 
            allAccessRight = results;

            } else {
                allAccessRight = null;
            }
    }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
        }
    
    return allAccessRight;

}
    
 
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
