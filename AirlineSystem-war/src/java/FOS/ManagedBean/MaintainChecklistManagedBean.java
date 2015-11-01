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

    /**
     * Creates a new instance of MaintainChecklistManagedBean
     */
    public MaintainChecklistManagedBean() {
    }

    @PostConstruct
    public void init() {
        assignChecklist();
        getSubmittedLists();

//        getCrewAvailChecklist();
    }

    public void assignChecklist() {
        maintenanceChecklistSessionBean.assignChecklist();
    }

    public void getCrewAvailChecklist(ActionEvent event) {
        getSubmittedLists();

        availChecklists = new ArrayList<AMaintainChecklist>();

        List<AMaintainChecklist> temps = maintenanceChecklistSessionBean.getCrewAvailChecklist(getLoginManageBean().getEmployeeUserName());

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
        maintenanceChecklistSessionBean.submitChecklist(selectedChecklistId, checkedLists, comments, getLoginManageBean().getEmployeeUserName());
        setSelectedChecklistId("");
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully Submitted!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getChecklistItems(ActionEvent event) {
        root = new DefaultTreeNode("Root", null);

        FacesMessage message = null;
        if (selectedChecklistId == null) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select one checklist!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "/FOS/DisplayCrewAvailChecklists";

        } else {
           
            setSelectedChecklistItems(maintenanceChecklistSessionBean.getSelectedChecklistItems(selectedChecklistId));
            return "/FOS/DisplayCrewSelectedChecklistItems";
        }

    }

    public void saveChecklist(ActionEvent event) {

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

}
