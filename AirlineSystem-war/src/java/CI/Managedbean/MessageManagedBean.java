/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Entity.Employee;
import CI.Entity.Message;
import CI.Session.EmployeeSessionBeanLocal;
import CI.Session.MessageSessionBeanLocal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "messageManagedBean")
@ManagedBean
//@SessionScoped
@RequestScoped
public class MessageManagedBean {

    @EJB
    private MessageSessionBeanLocal messageSessionBean;
    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    private String sender;
//    private String receiver;
    private String msgText;
    private Date lastRead;
    private List<Message> unReadMsg;
    private List<Message> readMsg;
    private List<Message> sentMsg;
    private String errorMsg;
    private List<Employee> allActiveEmployees;
    private List<String> allEmployeeNames;
    private Employee oneEmployee;
    private String senderFullName;
    private String senderDepartment;
    
   
    @ManagedProperty(value = "#{loginManagedBean}")
    private LoginManagedBean loginManageBean;

    public MessageManagedBean() {
        lastRead = new Date();
    }

//    public void sendMsg(ActionEvent event) {
//        FacesMessage message = null;
//        if (messageSessionBean.getEmployee(receiver) == null || receiver.isEmpty()) {
//            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "No such receiver!", "");
//            receiveReadMessage(event); //to make sure the page refresh and show all the message
//        } else {
//            setSender(loginManageBean.employeeUserName);
//            System.out.println("Sender: " + sender + "Receiver: " + receiver + "message: " + msgText);
//            messageSessionBean.sendMsg(sender, receiver, msgText);
//            receiveReadMessage(event);
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message Sent", "");
//        }
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        clear();
//    }
    
    
    
    
    public String sendMsg(String receiver) {
        
        System.out.println("send message - receiver name:" + receiver);
        FacesMessage message = null;
        if (messageSessionBean.getEmployee(receiver) == null || receiver.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "No such receiver!", "");
            setReadMsg(messageSessionBean.readMsg(loginManageBean.employeeUserName));
//            receiveReadMessage(event); //to make sure the page refresh and show all the message
        } else {
            setSender(loginManageBean.employeeUserName);
            System.out.println("Sender: " + sender + "Receiver: " + receiver + "message: " + msgText);
            messageSessionBean.sendMsg(sender, receiver, msgText);
            setReadMsg(messageSessionBean.readMsg(loginManageBean.employeeUserName));
//            receiveReadMessage(event);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message Sent", "");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        clear();
        setSentMsg(messageSessionBean.sentMsg(loginManageBean.employeeUserName));
        return "Message";
    }
    
//    public void checkReceiver() {
//        if (messageSessionBean.getEmployee(receiver) == null) {
//            setErrorMsg("No user exists");
//        }
//    }

    public void clear(){
        setSender("");
//        setReceiver("");
        setMsgText("");
    }
    @PostConstruct
    public void unReadMessage() {
        setAllActiveEmployees(employeeSessionBean.retrieveAllActiveEmployees());
        setUnReadMsg(messageSessionBean.unReadMsg(loginManageBean.employeeUserName));
        messageSessionBean.setMsgRead(loginManageBean.employeeUserName);
        
    }

    public void receiveReadMessage(ActionEvent event) {
        setReadMsg(messageSessionBean.readMsg(loginManageBean.employeeUserName));
        setSentMsg(messageSessionBean.sentMsg(loginManageBean.employeeUserName));

    }

    /**
     * @return the msgText
     */
    public String getMsgText() {
        return msgText;
    }

    /**
     * @param msgText the msgText to set
     */
    public void setMsgText(String msgText) {
        this.msgText = msgText;
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
     * @return the receiver
     */
//    public String getReceiver() {
//        return receiver;
//    }

    /**
     * @param receiver the receiver to set
     */
//    public void setReceiver(String receiver) {
//        this.receiver = receiver;
//    }

    /**
     * @return the lastRead
     */
    public Date getLastRead() {
        return lastRead;
    }

    /**
     * @param lastRead the lastRead to set
     */
    public void setLastRead(Date lastRead) {
        this.lastRead = lastRead;
    }

    /**
     * @return the unReadMsg
     */
    public List<Message> getUnReadMsg() {
        return unReadMsg;
    }

    /**
     * @param unReadMsg the unReadMsg to set
     */
    public void setUnReadMsg(List<Message> unReadMsg) {
        this.unReadMsg = unReadMsg;
    }

    /**
     * @return the readMsg
     */
    public List<Message> getReadMsg() {
        return readMsg;
    }

    /**
     * @param readMsg the readMsg to set
     */
    public void setReadMsg(List<Message> readMsg) {
        this.readMsg = readMsg;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the allActiveEmployees
     */
    public List<Employee> getAllActiveEmployees() {
        return allActiveEmployees;
    }

    /**
     * @param allActiveEmployees the allActiveEmployees to set
     */
    public void setAllActiveEmployees(List<Employee> allActiveEmployees) {
        this.allActiveEmployees = allActiveEmployees;
    }

    /**
     * @return the oneEmployee
     */
    public Employee getOneEmployee() {
        return oneEmployee;
    }

    /**
     * @param oneEmployee the oneEmployee to set
     */
    public void setOneEmployee(Employee oneEmployee) {
        this.oneEmployee = oneEmployee;
    }

    /**
     * @return the allEmployeeNames
     */
    public List<String> getAllEmployeeNames() {
        return allEmployeeNames;
    }

    /**
     * @param allEmployeeNames the allEmployeeNames to set
     */
    public void setAllEmployeeNames(List<String> allEmployeeNames) {
        this.allEmployeeNames = allEmployeeNames;
    }

    /**
     * @return the senderFullName
     */
    public String getSenderFullName() {
        return senderFullName;
    }

    /**
     * @param senderFullName the senderFullName to set
     */
    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    /**
     * @return the senderDepartment
     */
    public String getSenderDepartment() {
        return senderDepartment;
    }

    /**
     * @param senderDepartment the senderDepartment to set
     */
    public void setSenderDepartment(String senderDepartment) {
        this.senderDepartment = senderDepartment;
    }

    /**
     * @return the sentMsg
     */
    public List<Message> getSentMsg() {
        return sentMsg;
    }

    /**
     * @param sentMsg the sentMsg to set
     */
    public void setSentMsg(List<Message> sentMsg) {
        this.sentMsg = sentMsg;
    }
    
}
