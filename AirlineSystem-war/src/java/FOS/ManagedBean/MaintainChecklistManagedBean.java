/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import CI.Managedbean.LoginManagedBean;
import FOS.Entity.AMaintainChecklist;
import FOS.Entity.MaintainChecklistItem;
import FOS.Session.MaintenanceChecklistSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author smu
 */
@Named(value = "maintainChecklistManagedBean")
@SessionScoped
@ManagedBean
public class MaintainChecklistManagedBean {

    @EJB
    private MaintenanceChecklistSessionBeanLocal maintenanceChecklistSessionBean;

    @ManagedProperty(value = "#{loginManagedBean}")
    private LoginManagedBean loginManageBean;

    private List<AMaintainChecklist> availChecklists;
    private String selectedChecklistId;
    private List<MaintainChecklistItem> selectedChecklistItems;
    private List<String> checkedLists; // crew checked lists 
    private String comments;

    private List<MaintainChecklistItem> checkedItems;
    private List<AMaintainChecklist> submittedListsDB;

    private AMaintainChecklist selectedSubmittedChecklist;

    private TreeNode root;

    private List<String> checklist1;
    private List<String> checklist2;
    private List<String> checklist3;
    private List<String> checklist4;
    private List<String> checklist5;
    private List<String> checklist6;
    private List<String> checklist7;

    /**
     * Creates a new instance of MaintainChecklistManagedBean
     */
    public MaintainChecklistManagedBean() {
    }

    @PostConstruct
    public void init() {
        assignChecklist();
        getSubmittedLists();

//getCrewAvailChecklist();
    }

    public void assignChecklist() {
        maintenanceChecklistSessionBean.assignChecklist();
    }

    public void getCrewAvailChecklist(ActionEvent event) {
        getSubmittedLists();

        availChecklists = new ArrayList<AMaintainChecklist>();

        List<AMaintainChecklist> temps = maintenanceChecklistSessionBean.getCrewAvailChecklist(getLoginManageBean().getEmployeeUserName());

        System.out.println("----------------------CHECKLIST: " + temps.size());
        if (submittedListsDB == null) {
            for (AMaintainChecklist a : temps) {

                availChecklists.add(a);

            }
        } else {

            for (AMaintainChecklist a : temps) {
                if (!submittedListsDB.contains(a)) {
                    availChecklists.add(a);
                }
            }

        }
        //setAvailChecklists(maintenanceChecklistSessionBean.getCrewAvailChecklist(getLoginManageBean().getEmployeeUserName()));
    }

    public void getSubmittedLists() {
        submittedListsDB = new ArrayList<AMaintainChecklist>();
        setSubmittedListsDB(maintenanceChecklistSessionBean.getSubmittedChecklists());
    }

    public void submitChecklist(ActionEvent event) {
        checkedLists = new ArrayList<String>();

        if (checklist1 != null || !checklist1.isEmpty()) {
            for (String s : checklist1) {
                checkedLists.add(s);
            }
        }

        if (checklist2 != null || !checklist2.isEmpty()) {
            for (String s : checklist2) {
                checkedLists.add(s);
            }
        }

        if (checklist3 != null || !checklist3.isEmpty()) {
            for (String s : checklist3) {
                checkedLists.add(s);
            }
        }

        if (checklist4 != null || !checklist4.isEmpty()) {
            for (String s : checklist4) {
                checkedLists.add(s);
            }
        }

        if (checklist5 != null || !checklist5.isEmpty()) {
            for (String s : checklist5) {
                checkedLists.add(s);
            }
        }

        if (checklist6 != null || !checklist6.isEmpty()) {
            for (String s : checklist6) {
                checkedLists.add(s);
            }
        }
        
        if (checklist7 != null && !checklist7.isEmpty()) {
            for (String s : checklist7) {
                checkedLists.add(s);
            }
        }
        maintenanceChecklistSessionBean.submitChecklist(selectedChecklistId, checkedLists, comments, getLoginManageBean().getEmployeeUserName());
        setSelectedChecklistId("");
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Submitted!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getChecklistItems(String theChecklistID) {
        setSelectedChecklistId(theChecklistID);

       String sType = maintenanceChecklistSessionBean.retrieveTypeByScheduleID(selectedChecklistId);
       
        FacesMessage message = null;
        if (selectedChecklistId == null) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select one checklist!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "/FOS/DisplayCrewAvailChecklists?faces-redirect=true";

        } else {
            
            setSelectedChecklistItems(maintenanceChecklistSessionBean.getSelectedChecklistItems(selectedChecklistId));
            if(sType.equals("A")){
            return "/FOS/DisplayCrewSelectedChecklistItems?faces-redirect=true";
            }else {
                return "/FOS/DisplayTypeBChecklistItems?faces-redirect=true";
            }
        }

    }

    public void saveChecklist(ActionEvent event) {

        checkedLists = new ArrayList<String>();

        if (checklist1 != null || !checklist1.isEmpty()) {
            for (String s : checklist1) {
                checkedLists.add(s);
            }
        }

        if (checklist2 != null || !checklist2.isEmpty()) {
            for (String s : checklist2) {
                checkedLists.add(s);
            }
        }

        if (checklist3 != null || !checklist3.isEmpty()) {
            for (String s : checklist3) {
                checkedLists.add(s);
            }
        }

        if (checklist4 != null || !checklist4.isEmpty()) {
            for (String s : checklist4) {
                checkedLists.add(s);
            }
        }

        if (checklist5 != null || !checklist5.isEmpty()) {
            for (String s : checklist5) {
                checkedLists.add(s);
            }
        }

        if (checklist6 != null || !checklist6.isEmpty()) {
            for (String s : checklist6) {
                checkedLists.add(s);
            }
        }
        
         
        if (checklist7 != null && !checklist7.isEmpty()) {
            for (String s : checklist7) {
                checkedLists.add(s);
            }
        }
        maintenanceChecklistSessionBean.saveChecklist(selectedChecklistId, checkedLists, comments);
        setSelectedChecklistId("");
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Saved!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * @return the availChecklists
     */
    public List<AMaintainChecklist> getAvailChecklists() {
        return availChecklists;
    }

    /**
     * @param availChecklists the availChecklists to set
     */
    public void setAvailChecklists(List<AMaintainChecklist> availChecklists) {
        this.availChecklists = availChecklists;
    }

    /**
     * @return the checkedLists
     */
    public List<String> getCheckedLists() {
        return checkedLists;
    }

    /**
     * @param checkedLists the checkedLists to set
     */
    public void setCheckedLists(List<String> checkedLists) {
        this.checkedLists = checkedLists;
    }

    /**
     * @return the selectedChecklistId
     */
    public String getSelectedChecklistId() {
        return selectedChecklistId;
    }

    /**
     * @param selectedChecklistId the selectedChecklistId to set
     */
    public void setSelectedChecklistId(String selectedChecklistId) {
        this.selectedChecklistId = selectedChecklistId;
    }

    /**
     * @return the loginManageBean
     */
    public LoginManagedBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManagedBean loginManageBean) {
        this.loginManageBean = loginManageBean;
    }

    /**
     * @return the selectedChecklistItems
     */
    public List<MaintainChecklistItem> getSelectedChecklistItems() {
        return selectedChecklistItems;
    }

    /**
     * @param selectedChecklistItems the selectedChecklistItems to set
     */
    public void setSelectedChecklistItems(List<MaintainChecklistItem> selectedChecklistItems) {
        this.selectedChecklistItems = selectedChecklistItems;
    }

    /**
     * @return the checkedItems
     */
    public List<MaintainChecklistItem> getCheckedItems() {
        return checkedItems;
    }

    /**
     * @param checkedItems the checkedItems to set
     */
    public void setCheckedItems(List<MaintainChecklistItem> checkedItems) {
        this.checkedItems = checkedItems;
        System.out.println("BOC: " + checkedItems);
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the submittedListsDB
     */
    public List<AMaintainChecklist> getSubmittedListsDB() {
        return submittedListsDB;
    }

    /**
     * @param submittedListsDB the submittedListsDB to set
     */
    public void setSubmittedListsDB(List<AMaintainChecklist> submittedListsDB) {
        this.submittedListsDB = submittedListsDB;
    }

    /**
     * @return the selectedSubmittedChecklist
     */
    public AMaintainChecklist getSelectedSubmittedChecklist() {
        return selectedSubmittedChecklist;
    }

    /**
     * @param selectedSubmittedChecklist the selectedSubmittedChecklist to set
     */
    public void setSelectedSubmittedChecklist(AMaintainChecklist selectedSubmittedChecklist) {
        this.selectedSubmittedChecklist = selectedSubmittedChecklist;
    }

    /**
     * @return the root
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * @return the checklist1
     */
    public List<String> getChecklist1() {
        return checklist1;
    }

    /**
     * @param checklist1 the checklist1 to set
     */
    public void setChecklist1(List<String> checklist1) {
        this.checklist1 = checklist1;
    }

    /**
     * @return the checklist2
     */
    public List<String> getChecklist2() {
        return checklist2;
    }

    /**
     * @param checklist2 the checklist2 to set
     */
    public void setChecklist2(List<String> checklist2) {
        this.checklist2 = checklist2;
    }

    /**
     * @return the checklist3
     */
    public List<String> getChecklist3() {
        return checklist3;
    }

    /**
     * @param checklist3 the checklist3 to set
     */
    public void setChecklist3(List<String> checklist3) {
        this.checklist3 = checklist3;
    }

    /**
     * @return the checklist4
     */
    public List<String> getChecklist4() {
        return checklist4;
    }

    /**
     * @param checklist4 the checklist4 to set
     */
    public void setChecklist4(List<String> checklist4) {
        this.checklist4 = checklist4;
    }

    /**
     * @return the checklist5
     */
    public List<String> getChecklist5() {
        return checklist5;
    }

    /**
     * @param checklist5 the checklist5 to set
     */
    public void setChecklist5(List<String> checklist5) {
        this.checklist5 = checklist5;
    }

    /**
     * @return the checklist6
     */
    public List<String> getChecklist6() {
        return checklist6;
    }

    /**
     * @param checklist6 the checklist6 to set
     */
    public void setChecklist6(List<String> checklist6) {
        this.checklist6 = checklist6;
    }

    /**
     * @return the checklist7
     */
    public List<String> getChecklist7() {
        return checklist7;
    }

    /**
     * @param checklist7 the checklist7 to set
     */
    public void setChecklist7(List<String> checklist7) {
        this.checklist7 = checklist7;
    }

}
