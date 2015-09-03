/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author smu
 */
@Entity
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long MessageId;
    private String sender;
    private String receiver;
    //@Temporal(TemporalType.DATE)
    private String sendTime;
    private boolean isRead;
    private String msgText;
    
    public Message(){
        this.setMessageId(System.nanoTime());
    }

    public void createMsg(String sender, String receiver, String msg){
        this.setSender(sender);
        this.setReceiver(receiver);
        this.setMsgText(msg);
        this.setSendTime(new Date().toString());
        this.setIsRead(false);
        System.out.println("Message ENtity: Sender: "+sender +"Receiver: "+receiver +"Message: "+msg+ "date: "+sendTime);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getMessageId() != null ? getMessageId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.getMessageId()== null && other.getMessageId() != null) || (this.getMessageId() != null && !this.MessageId.equals(other.MessageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CI.Entity.Message[ id=" + MessageId + " ]";
    }

    /**
     * @return the MessageId
     */
    public Long getMessageId() {
        return MessageId;
    }

    /**
     * @param MessageId the MessageId to set
     */
    public void setMessageId(Long MessageId) {
        this.MessageId = MessageId;
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
     * @return the sendTime
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime the sendTime to set
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return the isRead
     */
    public boolean isIsRead() {
        return isRead;
    }

    /**
     * @param isRead the isRead to set
     */
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
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

    
    
}
