/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import FOS.Session.ChecklistSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

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
   
private String checklistItemName; 
private String checklistName;
private List <String> checklistTypes;

public checklistManagedBean(){
    
}

@PostConstruct
public void init(){
   setChecklistTypes(checklistSessionBean.retrieveAllChecklists());
}

public String addItem(){
    checklistSessionBean.addChecklistItem(checklistName, checklistItemName);
    return "CreateChecklistItem";
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
            
    
}
