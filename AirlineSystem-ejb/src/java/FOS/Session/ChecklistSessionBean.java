/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Schedule;
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
    public List<String> retrieveAllChecklists() {
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
    public void addChecklistItem(String checklistName, String itemName) {

        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.createChecklistItem(itemName);

        Checklist checklist = new Checklist();

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
        em.persist(checklistItem);
        em.merge(checklist);
    }

    @Override

    public List<ChecklistItem> retrieveChecklistItems(String checklistName) {
        List<ChecklistItem> checklistItems = new ArrayList();

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
    public ChecklistItem findItem(Long key) {
        return em.find(ChecklistItem.class, key);
    }

    @Override
    public void deleteChecklistItem(Schedule schedule, Long itemKey, String checklistName) {
        Checklist matchedChecklist = new Checklist();
        List<Checklist> checklistsForSchedule = schedule.getChecklists();

        for (Checklist eachChecklist : checklistsForSchedule) {
            if (eachChecklist.getName().equals(checklistName)) {
                matchedChecklist = eachChecklist;
            }
        }
        ChecklistItem item = new ChecklistItem();
        for (ChecklistItem eachItemOfChecklist : matchedChecklist.getChecklistItems()) {
            if (eachItemOfChecklist.getId() == itemKey) {
                item = eachItemOfChecklist;
            }
        }
        matchedChecklist.removeChecklistItem(item);
        em.merge(matchedChecklist);        
        ChecklistItem toBeRemoved = em.merge(item);
        em.remove(toBeRemoved);
    }

    @Override
    public void editChecklistItem(ChecklistItem item) {
        em.merge(item);
    }

    @Override
    public void updateFilledChecklist(Schedule schedule, String checklistName, List<ChecklistItem> checkedItems, String comments) {
        
        Checklist checklist = new Checklist();
        List<Checklist> checklistsForSchedule = schedule.getChecklists();

        for (Checklist eachChecklist : checklistsForSchedule) {
            if (eachChecklist.getName().equals(checklistName)) {
                checklist = eachChecklist;
            }
        }
        for (ChecklistItem eachItemInChecklist: checklist.getChecklistItems()){
            eachItemInChecklist.setChecked(false);
            em.merge(eachItemInChecklist);
        }

        for (ChecklistItem eachCheckedItem : checkedItems) {
            for (ChecklistItem eachItemInChecklist : checklist.getChecklistItems()){
                if (eachCheckedItem.getName().equals(eachItemInChecklist.getName())){
                    eachItemInChecklist.setChecked(true);
                    em.merge(eachItemInChecklist);
                }
            }
           
        }
        
        checklist.setComments(comments);
        em.merge(checklist);
        em.flush();
    }

    @Override
    public List<ChecklistItem> getItemsFromNames(Schedule schedule, String checklistName, List<String> selectedItemNames) {
        Checklist checklist = new Checklist();
        List<ChecklistItem> list = new ArrayList();
        List<Checklist> checklistsForSchedule = schedule.getChecklists();

        for (Checklist eachChecklist : checklistsForSchedule) {
            if (eachChecklist.getName().equals(checklistName)) {
                checklist = eachChecklist;
            }
        }
        for (String eachItemName : selectedItemNames) {
                for (ChecklistItem eachMatchedItem : checklist.getChecklistItems()) {
                    if (eachItemName.equals(eachMatchedItem.getName()))
                               list.add(eachMatchedItem);
            } 
        }
        return list;
    }

    @Override
    public String retrieveChecklistComments(String checklistName) {
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
        return checklist.getComments();

    }

    @Override
    public List<Checklist> createChecklistAndItems() {

        List<Checklist> checklists = new ArrayList();

        Checklist pilotChecklist = new Checklist();
        Checklist cabinCrewChecklist = new Checklist();
        Checklist groundStaffChecklist = new Checklist();
        pilotChecklist.setComments("");
        pilotChecklist.setName("Pilot");
        cabinCrewChecklist.setName("Cabin Crew");
        cabinCrewChecklist.setComments("");
        groundStaffChecklist.setName("Ground Staff");
        groundStaffChecklist.setComments("");

        //Creation of pilot checklist items
        ChecklistItem checklistItem1 = new ChecklistItem();
        checklistItem1.setName("Parking brake on");
        checklistItem1.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem1);
        em.persist(checklistItem1);

        ChecklistItem checklistItem2 = new ChecklistItem();
        checklistItem2.setName("Flaps up");
        checklistItem2.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem2);
        em.persist(checklistItem2);

        ChecklistItem checklistItem3 = new ChecklistItem();
        checklistItem3.setName("Throttle idle");
        checklistItem3.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem3);
        em.persist(checklistItem3);

        ChecklistItem checklistItem4 = new ChecklistItem();
        checklistItem4.setName("Fuel pump off");
        checklistItem4.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem4);
        em.persist(checklistItem4);

        ChecklistItem checklistItem5 = new ChecklistItem();
        checklistItem5.setName("Avionics Master off");
        checklistItem5.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem5);
        em.persist(checklistItem5);

        ChecklistItem checklistItem6 = new ChecklistItem();
        checklistItem6.setName("Electrical equipment off");
        checklistItem6.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem6);
        em.persist(checklistItem6);

        ChecklistItem checklistItem7 = new ChecklistItem();
        checklistItem7.setName("Mixture-idle cut off");
        checklistItem7.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem7);
        em.persist(checklistItem7);

        ChecklistItem checklistItem8 = new ChecklistItem();
        checklistItem8.setName("Magnetos off");
        checklistItem8.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem8);
        em.persist(checklistItem8);

        ChecklistItem checklistItem9 = new ChecklistItem();
        checklistItem9.setName("Communication transponders off");
        checklistItem9.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem9);
        em.persist(checklistItem9);

        ChecklistItem checklistItem10 = new ChecklistItem();
        checklistItem10.setName("Master switch off");
        checklistItem10.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem10);
        em.persist(checklistItem10);

        ChecklistItem checklistItem11 = new ChecklistItem();
        checklistItem11.setName("Landing gear lockdown levers");
        checklistItem11.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem11);
        em.persist(checklistItem11);

        ChecklistItem checklistItem12 = new ChecklistItem();
        checklistItem12.setName("Flight Log Complete");
        checklistItem12.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem12);
        em.persist(checklistItem12);

        ChecklistItem checklistItem13 = new ChecklistItem();
        checklistItem13.setName("Tie-down secure");
        checklistItem13.setChecked(false);
        pilotChecklist.addChecklistItem(checklistItem13);
        em.persist(checklistItem13);

        em.persist(pilotChecklist);

        //Create cabin crew checklist
        ChecklistItem checklistItem14 = new ChecklistItem();
        checklistItem14.setName("Cabin checked for left behind items");
        checklistItem14.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem14);
        em.persist(checklistItem14);

        ChecklistItem checklistItem15 = new ChecklistItem();
        checklistItem15.setName("Emergency exits checked");
        checklistItem15.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem15);
        em.persist(checklistItem15);

        ChecklistItem checklistItem16 = new ChecklistItem();
        checklistItem16.setName("Cabin checked for cleanliness");
        checklistItem16.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem16);
        em.persist(checklistItem16);

        ChecklistItem checklistItem17 = new ChecklistItem();
        checklistItem17.setName("Seat belts inspected");
        checklistItem17.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem17);
        em.persist(checklistItem17);

        ChecklistItem checklistItem18 = new ChecklistItem();
        checklistItem18.setName("Interiots checed for superficial damage");
        checklistItem18.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem18);
        em.persist(checklistItem18);

        ChecklistItem checklistItem19 = new ChecklistItem();
        checklistItem19.setName("Fire Extinguishers functioning properly");
        checklistItem19.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem19);
        em.persist(checklistItem19);

        ChecklistItem checklistItem20 = new ChecklistItem();
        checklistItem20.setName("Life Jackets checked");
        checklistItem20.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem20);
        em.persist(checklistItem20);

        ChecklistItem checklistItem21 = new ChecklistItem();
        checklistItem21.setName("Tray Tables checked");
        checklistItem21.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem21);
        em.persist(checklistItem21);

        ChecklistItem checklistItem22 = new ChecklistItem();
        checklistItem22.setName("Oxygen masks checked");
        checklistItem22.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem22);
        em.persist(checklistItem22);

        ChecklistItem checklistItem23 = new ChecklistItem();
        checklistItem23.setName("Window shades checked");
        checklistItem23.setChecked(false);
        cabinCrewChecklist.addChecklistItem(checklistItem23);
        em.persist(checklistItem23);

        em.persist(cabinCrewChecklist);

        //Ground staff checklist
        ChecklistItem checklistItem24 = new ChecklistItem();
        checklistItem24.setName("Cargo compartment checked for left behind items");
        checklistItem24.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem24);
        em.persist(checklistItem24);

        ChecklistItem checklistItem25 = new ChecklistItem();
        checklistItem25.setName("Battery disconnected");
        checklistItem25.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem25);
        em.persist(checklistItem25);

        ChecklistItem checklistItem26 = new ChecklistItem();
        checklistItem26.setName("Oil pressure normal");
        checklistItem26.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem26);
        em.persist(checklistItem26);

        ChecklistItem checklistItem27 = new ChecklistItem();
        checklistItem27.setName("Tyre pressure normal");
        checklistItem27.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem27);
        em.persist(checklistItem27);

        ChecklistItem checklistItem28 = new ChecklistItem();
        checklistItem28.setName("No oil leaks");
        checklistItem28.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem28);
        em.persist(checklistItem28);

        ChecklistItem checklistItem29 = new ChecklistItem();
        checklistItem29.setName("Fuel tanks filled with water");
        checklistItem29.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem29);
        em.persist(checklistItem29);

        ChecklistItem checklistItem30 = new ChecklistItem();
        checklistItem30.setName("Engine cleared of debris");
        checklistItem30.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem30);
        em.persist(checklistItem30);

        ChecklistItem checklistItem31 = new ChecklistItem();
        checklistItem31.setName("Exteriors checked for superficial damage");
        checklistItem31.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem31);
        em.persist(checklistItem31);

        ChecklistItem checklistItem32 = new ChecklistItem();
        checklistItem32.setName("Cargo compartment checked for damage");
        checklistItem32.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem32);
        em.persist(checklistItem32);

        ChecklistItem checklistItem33 = new ChecklistItem();
        checklistItem33.setName("Landing lights functioning properly");
        checklistItem33.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem33);
        em.persist(checklistItem33);

        ChecklistItem checklistItem34 = new ChecklistItem();
        checklistItem34.setName("Wiring checked");
        checklistItem34.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem34);
        em.persist(checklistItem34);

        ChecklistItem checklistItem35 = new ChecklistItem();
        checklistItem35.setName("Wings retraction checked");
        checklistItem35.setChecked(false);
        groundStaffChecklist.addChecklistItem(checklistItem35);
        em.persist(checklistItem35);

        em.persist(groundStaffChecklist);

        checklists.add(pilotChecklist);
        checklists.add(cabinCrewChecklist);
        checklists.add(groundStaffChecklist);

        return checklists;

    }

    public List<ChecklistItem> retrieveChecklistItemsByScheduleAndChecklistName(Schedule schedule, String checklistName) {
        Checklist matchedChecklist = new Checklist();
        List<Checklist> checklistsForSchedule = schedule.getChecklists();

        for (Checklist eachChecklist : checklistsForSchedule) {
            if (eachChecklist.getName().equals(checklistName)) {
                matchedChecklist = eachChecklist;
            }
        }
        return matchedChecklist.getChecklistItems();

    }
    
    @Override
    public String retrieveChecklistCommentsByScheduleAndChecklistName(Schedule schedule,String checklistName){
        Checklist matchedChecklist = new Checklist();
        List<Checklist> checklistsForSchedule = schedule.getChecklists();

        for (Checklist eachChecklist : checklistsForSchedule) {
            if (eachChecklist.getName().equals(checklistName)) {
                matchedChecklist = eachChecklist;
            }
        }
        return matchedChecklist.getComments();
    }

    @Override
    public void addChecklistItemByScheduleAndChecklistName(Schedule schedule, String checklistName, String checklistItemName) {
        Checklist matchedChecklist = new Checklist();
        List<Checklist> checklistsForSchedule = schedule.getChecklists();

        for (Checklist eachChecklist : checklistsForSchedule) {
            if (eachChecklist.getName().equals(checklistName)) {
                matchedChecklist = eachChecklist;
            }
        }
        ChecklistItem newItem = new ChecklistItem();
        newItem.createChecklistItem(checklistItemName);
        matchedChecklist.addChecklistItem(newItem);
        em.persist(newItem);
        em.merge(matchedChecklist);

    }

}
