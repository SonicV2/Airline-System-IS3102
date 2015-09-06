/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Entity.Message;
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

    private String sender;
    private String receiver;
    private String msgText;
    private Date lastRead;
    private List<Message> unReadMsg;
    private List<Message> readMsg;
    private String errorMsg;

    @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;

    public MessageManagedBean() {
        lastRead = new Date();
    }

    public void sendMsg(ActionEvent event) {
        FacesMessage message = null;
        if (messageSessionBean.getEmployee(receiver) == null || receiver.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "No such receiver!", "");
        } else {
            setSender(loginManageBean.employeeUserName);
            System.out.println("Sender: " + sender + "Receiver: " + receiver + "message: " + msgText);
            messageSessionBean.sendMsg(sender, receiver, msgText);
            receiveReadMessage(event);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Message Sent", "");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void checkReceiver() {
        if (messageSessionBean.getEmployee(receiver) == null) {
            setErrorMsg("No user exists");
        }
    }

    @PostConstruct
    public void unReadMessage() {
        setUnReadMsg(messageSessionBean.unReadMsg(loginManageBean.employeeUserName));
        messageSessionBean.setMsgRead(loginManageBean.employeeUserName);
    }

    public void receiveReadMessage(ActionEvent event) {
        setReadMsg(messageSessionBean.readMsg(loginManageBean.employeeUserName));

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
    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManageBean loginManageBean) {
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
    public String getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

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

}
