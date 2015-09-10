/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.EmailSessionBeanLocal;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "emailManagedBean")
@RequestScoped
@ManagedBean
public class EmailManagedBean {
    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    private String receiver;
    private String subject;
    private String body;
    
    public EmailManagedBean() {
    }
    
    public void sendEmail(ActionEvent event){
        System.out.println("Receiver: "+receiver + "Subject: "+subject+"body: "+body);
        emailSessionBean.sendEmail(receiver, subject, body);
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
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the emailSessionBean
     */
    public EmailSessionBeanLocal getEmailSessionBean() {
        return emailSessionBean;
    }

    /**
     * @param emailSessionBean the emailSessionBean to set
     */
    public void setEmailSessionBean(EmailSessionBeanLocal emailSessionBean) {
        this.emailSessionBean = emailSessionBean;
    }
    
}
