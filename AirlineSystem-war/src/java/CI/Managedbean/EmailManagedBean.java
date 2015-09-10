/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.EmailSessionBeanLocal;
import CI.Session.EmployeeSessionBeanLocal;
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
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    @EJB
    private EmailSessionBeanLocal emailSessionBean;
    

    private String receiver;
    private String subject;
    private String body;
    
    private String userName;
    private String NRIC;
    private String email;
    
    private String pass;
    
    public EmailManagedBean() {
    }
    
    public void valideUser(ActionEvent event){
        setEmail(emailSessionBean.validateUser(userName, NRIC)); //get employee's email address 
        if(!email.equals("")){
            setPass(emailSessionBean.passGen());
            employeeSessionBean.hashNewPwd(userName, pass);
            sendEmail(email);
        }
        
    }
    
    
    public void sendEmail(String email){
        
        setSubject("*Confidential*--- Password Reset ");
        setBody("Your New Password: " + pass);// need to think of the link
        System.out.println("userName "+receiver + "Subject: "+subject+"body: "+body);
        emailSessionBean.sendEmail(email, subject, body);
        clear();
    }

    public void clear(){
//        setReceiver("");
//        setSubject("");
//        setBody("");
        setUserName("");
        setNRIC("");
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

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the NRIC
     */
    public String getNRIC() {
        return NRIC;
    }

    /**
     * @param NRIC the NRIC to set
     */
    public void setNRIC(String NRIC) {
        this.NRIC = NRIC;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the employeeSessionBean
     */
    public EmployeeSessionBeanLocal getEmployeeSessionBean() {
        return employeeSessionBean;
    }

    /**
     * @param employeeSessionBean the employeeSessionBean to set
     */
    public void setEmployeeSessionBean(EmployeeSessionBeanLocal employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }

    
    
}
