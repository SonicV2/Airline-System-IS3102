/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import APS.Entity.Schedule;
import APS.Session.ScheduleSessionBeanLocal;
import FOS.Entity.Checklist;
import FOS.Entity.ChecklistItem;
import FOS.Session.ChecklistSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author parthasarthygupta
 */

@Named(value = "checklistManagedBean")
@ManagedBean
@SessionScoped
public class checklistManagedBean {
     @EJB
    private ChecklistSessionBeanLocal checklistSessionBean;
     
     @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
   
private String checklistItemName; 
private String checklistName;
private List <String> checklistTypes;
private List <ChecklistItem> checklistItemsForChecklist;
private long itemKey;
private List <String> selectedItemNames;
private List <ChecklistItem> selectedItems;
private String comments;
private long scheduleId;
private Schedule schedule;
private Checklist checklistForSchedule;

public checklistManagedBean(){
    
}

@PostConstruct
public void init(){
   setChecklistTypes(checklistSessionBean.retrieveAllChecklists());
}

public void clear(){
    setComments("");
    for (ChecklistItem eachItem: checklistItemsForChecklist){
        eachItem.setChecked(false);
    }
}

public String directToCreateChecklistItem(){
    return"/FOS/CreateChecklistItem";
}

public String addItem(){
    checklistSessionBean.addChecklistItem(checklistName, checklistItemName);
    return "/CI/employeeDashBoard";
}

public String editParticularChecklist (String checklistCanAccess){
    setChecklistName(checklistCanAccess);
    setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItems(checklistName));
    return "/FOS/EditChecklist";
    //Page after editing checklistName should be set as "Select Checklist"
}

public String fillParticularChecklist (String checklistCanAccess){
    setChecklistName(checklistCanAccess);
    setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItems(checklistName));
    clear();
    return "/FOS/FillChecklist";
}



  public void onRowEdit(RowEditEvent event) {
        ChecklistItem edited = (ChecklistItem)event.getObject();
        itemKey = edited.getId();
        ChecklistItem original = checklistSessionBean.findItem(itemKey);
        
        
        if(!edited.equals(original))
        {
            checklistSessionBean.editChecklistItem(edited);
            FacesMessage msg = new FacesMessage("Checklist Item Edited" );
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
           FacesMessage msg = new FacesMessage("Edit Cancelled" ); 
           FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String deleteItem(Long id){
        checklistSessionBean.deleteChecklistItem(id, checklistName);
        setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItems(checklistName));
        return "EditChecklist";
    }
    
    public String editChecklistDone(){
        return "/CI/employeeDashBoard";
    }
    
    public String submitChecklist(){
        setSelectedItems(checklistSessionBean.getItemsFromNames(selectedItemNames));
        checklistSessionBean.updateFilledChecklist(checklistName, selectedItems, comments);
        return "/CI/employeeDashBoard";
    }
    
    public String directToChooseChecklistToView(){
        
        return "/FOS/ChooseChecklistToView";
    }
    
     public String directToFilledChecklist(){
         //Have schedule ID and checklist name
         setSchedule(scheduleSessionBean.getSchedule(scheduleId));
         setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItemsByScheduleAndChecklistName(schedule, checklistName));
         return "/FOS/displayFilledChecklist";
    }


    public String getChecklistItemName() {
        return checklistItemName;
    }

    public void setChecklistItemName(String checklistItemName) {
        this.checklistItemName = checklistItemName;
    }

    /**
     * @return the checklistTypes
     */
    public List <String> getChecklistTypes() {
        return checklistTypes;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    /**
     * @param checklistTypes the checklistTypes to set
     */
    public void setChecklistTypes(List <String> checklistTypes) {
        this.checklistTypes = checklistTypes;
    }

    public List<ChecklistItem> getChecklistItemsForChecklist() {
        return checklistItemsForChecklist;
    }

    public void setChecklistItemsForChecklist(List<ChecklistItem> checklistItemsForChecklist) {
        this.checklistItemsForChecklist = checklistItemsForChecklist;
    }

  
    public long getItemKey() {
        return itemKey;
    }

    public void setItemKey(long itemKey) {
        this.itemKey = itemKey;
    }

    public List<String> getSelectedItemNames() {
        return selectedItemNames;
    }

    public void setSelectedItemNames(List<String> selectedItemNames) {
        this.selectedItemNames = selectedItemNames;
    }

    public List<ChecklistItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<ChecklistItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

  

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Checklist getChecklistForSchedule() {
        return checklistForSchedule;
    }

    public void setChecklistForSchedule(Checklist checklistForSchedule) {
        this.checklistForSchedule = checklistForSchedule;
    }
    
    
    
    
   
    
            
    
}
