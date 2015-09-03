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
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "messageManagedBean")
@ManagedBean
@SessionScoped
public class MessageManagedBean {
    @EJB
    private MessageSessionBeanLocal messageSessionBean;

    private String sender;
    private String receiver;
    private String msgText;
    private Date lastRead;
    private List<Message> unReadMsg;
    
    @ManagedProperty(value="#{loginManageBean}")
    private LoginManageBean loginManageBean;
    
    public MessageManagedBean() {
        lastRead = new Date();
    }
    
    public void sendMsg(ActionEvent event){
        setSender(loginManageBean.employeeUserName);
        System.out.println("Sender: " + sender + "Receiver: " + receiver + "message: " + msgText);
        messageSessionBean.sendMsg(sender, receiver, msgText);
    }

    public void unReadMessage(ActionEvent event){
        setUnReadMsg(messageSessionBean.unReadMsg(loginManageBean.employeeUserName));
//        for(Message m: unReadMsg){
//            m.setIsRead(true);
//        }
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
    
}
