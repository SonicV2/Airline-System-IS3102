/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import APS.Entity.Schedule;
import APS.Session.ScheduleSessionBeanLocal;
import CI.Managedbean.LoginManageBean;
import FOS.Entity.Checklist;
import FOS.Entity.ChecklistItem;
import FOS.Entity.Team;
import FOS.Session.ChecklistSessionBeanLocal;
import FOS.Session.CrewSignInSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
public class ChecklistManagedBean {
     @EJB
    private ChecklistSessionBeanLocal checklistSessionBean;
     
     @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
     
     @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;
     
     @EJB
    private CrewSignInSessionBeanLocal crewSignInSessionBean;

   
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
private String crewName;
private Team team;
private List<Schedule> schedulesForTeam;
private List<Schedule> pastSchedulesForTeam;
private List<Schedule> currentDaySchedulesForTeam;
FacesMessage message = null;

public ChecklistManagedBean(){
    
}

@PostConstruct
public void init(){
List<String> allChecklistTypes = new ArrayList();
allChecklistTypes.add("Pilot");
allChecklistTypes.add("Cabin Crew");
allChecklistTypes.add("Ground Staff");
setChecklistTypes(allChecklistTypes);

 
//setChecklistTypes(checklistSessionBean.retrieveAllChecklists());
}

public void clear(){
    setComments("");
    for (ChecklistItem eachItem: checklistItemsForChecklist){
        eachItem.setChecked(false);
    }
}

public String directToChooseChecklistToFill(String name){
    setChecklistName(name);
    setCrewName(loginManageBean.getEmployeeUserName());
    setTeam(getCrewSignInSessionBean().getCCTeam(crewName));
    setSchedulesForTeam(team.getSchedule());
    setCurrentDaySchedulesForTeam(scheduleSessionBean.filterForCurrentDaySchedules(schedulesForTeam));
    return "/FOS/ChooseChecklistToFill";
}


public String directToChooseChecklistToAddTo(){
    return"/FOS/ChooseChecklistToAddTo";
}

public String directToChooseChecklistToEdit(){
    return"/FOS/ChooseChecklistToEdit";
}

public String editChecklist(Long scheduleKey, String checklistToEdit ){
         setScheduleId(scheduleKey);
         setChecklistName (checklistToEdit);
         setSchedule(scheduleSessionBean.getSchedule(scheduleId));
         setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItemsByScheduleAndChecklistName(schedule, checklistName));
         return "/FOS/EditChecklist";
}

public String addChecklistItem(Long scheduleKey, String checklistToAdd ){
         setScheduleId(scheduleKey);
         setChecklistName (checklistToAdd);
         setSchedule(scheduleSessionBean.getSchedule(scheduleId));
         setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItemsByScheduleAndChecklistName(schedule, checklistName));
         return "/FOS/CreateChecklistItem";
}

public String homePage(){
    setComments("");
    return "/CI/employeeDashBoard";
}

public String addItem(){
    boolean itemAlreadyExists = false;
    for (ChecklistItem itemsOfChecklist: checklistItemsForChecklist){
        if (itemsOfChecklist.getName().equalsIgnoreCase(checklistItemName)){
            itemAlreadyExists = true;
        }
    }
    if (itemAlreadyExists == true){
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item already exists", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            checklistItemName = "";
            return null;
    }
    else{
    checklistSessionBean.addChecklistItemByScheduleAndChecklistName(schedule,checklistName, checklistItemName);
    return "/CI/employeeDashBoard";
    }
    
    
}

public String editParticularChecklist (String checklistCanAccess){
    setChecklistName(checklistCanAccess);
    setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItems(checklistName));
    return "/FOS/EditChecklist";
}

public String fillParticularChecklist (Long scheduleKey ){
    setScheduleId(scheduleKey);
    setSchedule(scheduleSessionBean.getSchedule(scheduleId));
    setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItemsByScheduleAndChecklistName(schedule, checklistName));
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
        setSchedule(scheduleSessionBean.getSchedule(scheduleId));
        checklistSessionBean.deleteChecklistItem(schedule, id, checklistName);
        setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItemsByScheduleAndChecklistName(schedule, checklistName));
        return "EditChecklist";
    }
    
    public String editChecklistDone(){
        return "/CI/employeeDashBoard";
    }
    
    public String submitChecklist(){
        setSelectedItems(checklistSessionBean.getItemsFromNames(schedule, checklistName, selectedItemNames));
        checklistSessionBean.updateFilledChecklist(schedule, checklistName, selectedItems, comments);
        //setSchedule(null);
        //setChecklistName("");
        //setSelectedItems(null);
        //setComments("");
        return "/CI/employeeDashBoard";
    }
    
    public String directToChooseChecklistToView(){
        return "/FOS/ChooseChecklistToView";
    }
    
     public String directToFilledChecklist(Long scheduleKey, String checklistToView){
         //Have schedule ID and checklist name
         setScheduleId(scheduleKey);
         setChecklistName (checklistToView);
         setSchedule(scheduleSessionBean.getSchedule(scheduleId));
         setChecklistItemsForChecklist(checklistSessionBean.retrieveChecklistItemsByScheduleAndChecklistName(schedule, checklistName));
         setComments(checklistSessionBean.retrieveChecklistCommentsByScheduleAndChecklistName(schedule, checklistName));
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

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public CrewSignInSessionBeanLocal getCrewSignInSessionBean() {
        return crewSignInSessionBean;
    }

    public void setCrewSignInSessionBean(CrewSignInSessionBeanLocal crewSignInSessionBean) {
        this.crewSignInSessionBean = crewSignInSessionBean;
    }

    public List<Schedule> getSchedulesForTeam() {
        return schedulesForTeam;
    }

    public void setSchedulesForTeam(List<Schedule> schedulesForTeam) {
        this.schedulesForTeam = schedulesForTeam;
    }

    public List<Schedule> getPastSchedulesForTeam() {
        return pastSchedulesForTeam;
    }

    public void setPastSchedulesForTeam(List<Schedule> pastSchedulesForTeam) {
        this.pastSchedulesForTeam = pastSchedulesForTeam;
    }

    public ChecklistSessionBeanLocal getChecklistSessionBean() {
        return checklistSessionBean;
    }

    public void setChecklistSessionBean(ChecklistSessionBeanLocal checklistSessionBean) {
        this.checklistSessionBean = checklistSessionBean;
    }

    public ScheduleSessionBeanLocal getScheduleSessionBean() {
        return scheduleSessionBean;
    }

    public void setScheduleSessionBean(ScheduleSessionBeanLocal scheduleSessionBean) {
        this.scheduleSessionBean = scheduleSessionBean;
    }

    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    public void setLoginManageBean(LoginManageBean loginManageBean) {
        this.loginManageBean = loginManageBean;
    }

    public List<Schedule> getCurrentDaySchedulesForTeam() {
        return currentDaySchedulesForTeam;
    }

    public void setCurrentDaySchedulesForTeam(List<Schedule> currentDaySchedulesForTeam) {
        this.currentDaySchedulesForTeam = currentDaySchedulesForTeam;
    }

    public FacesMessage getMessage() {
        return message;
    }

    public void setMessage(FacesMessage message) {
        this.message = message;
    }

    
    
    
    
            
    
}
