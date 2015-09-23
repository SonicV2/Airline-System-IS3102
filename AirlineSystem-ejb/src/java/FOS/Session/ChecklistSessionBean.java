/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.Checklist;
import FOS.Entity.ChecklistItem;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author parthasarthygupta
 */
@Stateless
public class ChecklistSessionBean implements ChecklistSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<String> retrieveAllChecklists(){
        ArrayList<String> list = new ArrayList();
        try {
            

            Query q = em.createQuery("SELECT a FROM Checklist a");

            List<Checklist> results = q.getResultList();
            if (!results.isEmpty()) {
                for (Checklist checklist : results) {
                    list.add(checklist.getName());
                }
                  
            } else {
                list = null;
                  System.out.println("list is null");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
        }
        return list;
        
    }
    
    @Override
    public void addChecklistItem (String checklistName, String itemName){
        
        System.out.println ("Session Bean: the checklist selected is " + checklistName + " and the item name is " + itemName);
        
        ChecklistItem checklistItem = new ChecklistItem ();
        checklistItem.createChecklistItem(itemName);
        
        Checklist checklist = new Checklist ();
        
        try {         

            Query q = em.createQuery("SELECT a FROM Checklist a where a.name = :nameChecklist");
            q.setParameter("nameChecklist", checklistName);
            List<Checklist> results = q.getResultList();
                for (Checklist eachChecklist : results) {
                    checklist = eachChecklist;
                }
                  
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
        }
        checklist.addChecklistItem(checklistItem);
        em.persist (checklistItem);
        em.merge(checklist);
        
      
    }
}
