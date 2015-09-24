/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.Checklist;
import FOS.Entity.ChecklistItem;
import java.util.ArrayList;
import java.util.Collection;
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
    
    @Override
    
    public List<ChecklistItem> retrieveChecklistItems (String checklistName){
        List <ChecklistItem> checklistItems = new ArrayList (); 
    
        try {
            
            Query q = em.createQuery("SELECT a FROM Checklist a where a.name = :nameChecklist ");
            q.setParameter("nameChecklist", checklistName);
            
            List<Checklist> results = q.getResultList();
            if (!results.isEmpty()) {
                for (Checklist checklist : results) {
                    checklistItems = checklist.getChecklistItems();
                }
                  
            } else {
                checklistItems = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
        }
        return checklistItems;
    
    }
    
    @Override
    public ChecklistItem findItem (Long key){
        return em.find(ChecklistItem.class, key);
    }
    
    @Override
    public void deleteChecklistItem (Long key, String checklistName){
        Checklist checklist = new Checklist();
        try {
            
            Query q = em.createQuery("SELECT a FROM Checklist a where a.name = :nameChecklist ");
            q.setParameter("nameChecklist", checklistName);
            
            List<Checklist> results = q.getResultList();
            if (!results.isEmpty()) {
                for (Checklist eachChecklist : results) {
                    checklist = eachChecklist;
                }
                  
            } else {
                checklist = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
        }
        ChecklistItem item = findItem(key);
        checklist.removeChecklistItem(item);
        em.remove(item);

    }
    
    @Override
    public void editChecklistItem (ChecklistItem item){
        em.merge(item);
    }
    
    @Override
    public void updateFilledChecklist (String checklistName, List<ChecklistItem> checkedItems, String comments){
        Checklist checklist = new Checklist();
        try {
            
            Query q = em.createQuery("SELECT a FROM Checklist a where a.name = :nameChecklist ");
            q.setParameter("nameChecklist", checklistName);
            
            List<Checklist> results = q.getResultList();
            if (!results.isEmpty()) {
                for (Checklist eachChecklist : results) {
                    checklist = eachChecklist;
                }
                  
            } else {
                checklist = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + enfe.getMessage());
        }
        for (ChecklistItem eachCheckedItem : checkedItems){
            checklist.checkItemAsCompleted (eachCheckedItem);
        }
        checklist.setComments(comments);      
      }
    
        @Override
        public List<ChecklistItem> getItemsFromNames(List<String> selectedItemNames){
        List<ChecklistItem> list = new ArrayList();
        
        for (String eachItemName: selectedItemNames){
            Query q = em.createQuery("SELECT a FROM ChecklistItem a where a.name = :nameItem ");
            q.setParameter("nameItem", eachItemName);
            
            List<ChecklistItem> results = q.getResultList();
            if (!results.isEmpty()) {
                for (ChecklistItem eachMatchedItem : results) {
                    list.add(eachMatchedItem);
                }
                  
            } else {
                list = null;
            } 
        }
        return list;
    }
    


}

