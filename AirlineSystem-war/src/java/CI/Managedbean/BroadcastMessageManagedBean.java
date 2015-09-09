/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.DepartmentSessionBeanLocal;
import CI.Session.MessageSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "broadcastMessageManagedBean")
@ManagedBean
@SessionScoped
//@RequestScoped
public class BroadcastMessageManagedBean {
    @EJB
    private MessageSessionBeanLocal messageSessionBean;
    @EJB
    private DepartmentSessionBeanLocal departmentSessionBean;
    
    @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;
    
    private String sender;
    private List<String> departments; //departments from database
    private String broadcastMsg;
    private List<String> selectDepart; //select Department

    /**
     * Creates a new instance of BroadcastMessageManagedBean
     */
    public BroadcastMessageManagedBean() {
    }
    
    @PostConstruct
    public void retrieveDepartment(){
        setDepartments(departmentSessionBean.retrive());
    }

    public void sendMsg(ActionEvent evnet){
       
        setSender(loginManageBean.employeeUserName);
        System.out.println("BC: "+loginManageBean.employeeUserName + "message: "+broadcastMsg);
        messageSessionBean.sendBroadcastMsg(sender, selectDepart, broadcastMsg);
        clear();
    }
    
    
    public void clear(){
        setSelectDepart(null);
        setBroadcastMsg("");

    }
    /**
     * @return the departments
     */
    public List<String> getDepartments() {
        return departments;
    }

    /**
     * @param departments the departments to set
     */
    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    /**
     * @return the broadcastMsg
     */
    public String getBroadcastMsg() {
        return broadcastMsg;
    }

    /**
     * @param broadcastMsg the broadcastMsg to set
     */
    public void setBroadcastMsg(String broadcastMsg) {
        this.broadcastMsg = broadcastMsg;
    }

    /**
     * @return the selectDepart
     */
    public List<String> getSelectDepart() {
        return selectDepart;
    }

    /**
     * @param selectDepart the selectDepart to set
     */
    public void setSelectDepart(List<String> selectDepart) {
        this.selectDepart = selectDepart;
    }

    /**
     * @return the departmentSessionBean
     */
    public DepartmentSessionBeanLocal getDepartmentSessionBean() {
        return departmentSessionBean;
    }

    /**
     * @param departmentSessionBean the departmentSessionBean to set
     */
    public void setDepartmentSessionBean(DepartmentSessionBeanLocal departmentSessionBean) {
        this.departmentSessionBean = departmentSessionBean;
    }

    /**
     * @return the messageSessionBean
     */
    public MessageSessionBeanLocal getMessageSessionBean() {
        return messageSessionBean;
    }

    /**
     * @param messageSessionBean the messageSessionBean to set
     */
    public void setMessageSessionBean(MessageSessionBeanLocal messageSessionBean) {
        this.messageSessionBean = messageSessionBean;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the loginManageBean
     */
    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManageBean loginManageBean) {
        this.loginManageBean = loginManageBean;
    }
    
}
