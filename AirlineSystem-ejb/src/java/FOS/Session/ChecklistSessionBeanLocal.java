/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Schedule;
import FOS.Entity.Checklist;
import FOS.Entity.ChecklistItem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface ChecklistSessionBeanLocal {

    public void addChecklistItem(String checklistName, String itemName);

    public List<String> retrieveAllChecklists();

    public List<ChecklistItem> retrieveChecklistItems(String checklistName);

    public ChecklistItem findItem(Long key);

    public void editChecklistItem(ChecklistItem item);

    public void deleteChecklistItem(Schedule schedule, Long itemKey, String checklistName);

    public void updateFilledChecklist(Schedule schedule, String checklistName, List<ChecklistItem> checkedItems, String comments);

    public List<ChecklistItem> getItemsFromNames(Schedule schedule, String checklistName, List<String> selectedItemNames);

    public String retrieveChecklistComments(String checklistName);

    public List<Checklist> createChecklistAndItems();

    public List<ChecklistItem> retrieveChecklistItemsByScheduleAndChecklistName(Schedule schedule, String checklistName);

    public void addChecklistItemByScheduleAndChecklistName(Schedule schedule, String checklistName, String checklistItemName);

    public String retrieveChecklistCommentsByScheduleAndChecklistName(Schedule schedule, String checklistName);
}
