/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.EmailSessionBeanLocal;
import CI.Session.EmployeeSessionBeanLocal;
import Distribution.Session.CustomerSessionBeanLocal;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

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
    
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    

    private String receiver;
    private String subject;
    private String body;
    
    private String userName;
    private String NRIC;
    private String email;
    
    private String pass;
    
    private String customerEmail;
    private Date customerDOB;
    
    public EmailManagedBean() {
    }
    
    public void valideUser(ActionEvent event){
        setEmail(emailSessionBean.validateUser(userName, NRIC)); //get employee's email address 
        FacesMessage message = null;
        if(email.equals("nomatch")){
            
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Name and NRIC Does Not Match!","");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);    
        }else if(email.equals("nouser")){
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Such User Name!",""); 
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            setPass(emailSessionBean.passGen());
            employeeSessionBean.hashNewPwd(userName, pass);
            
            sendEmail(email);
        }
        
    }
    
    public void validateCustomer(ActionEvent event){
        setEmail(emailSessionBean.validateCustomer(customerEmail, customerDOB));
        FacesMessage message = null;
        if (email.equals("nomatch") || email.equals("nouser")){
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your email and birthday do not match!",""); 
//            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);
 
        }else
        {
            setPass(emailSessionBean.passGen());
            customerSessionBean.hashNewPwd(customerEmail, pass);
            
//            sendEmail(customerEmail);
            
        }
    }
    
    
    public void sendCustomerPasswordEmail(String customerEmail){
        
        setSubject("*Confidential* --- Merlion Airlines Flyer Program");
        setBody("You have recently made a request for change of password due to you forgetting your password."
                + "Your new password is: " + pass + "Please log in to change your password again.");
        emailSessionBean.sendEmail(customerEmail, subject, body);
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "An email has been sent ! Please check your personal email","");

//        RequestContext.getCurrentInstance().update("growll");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        
    }
    
    public void sendEmail(String email){
        
        setSubject("*Confidential*--- Password Reset ");
        setBody("Your New Password: " + pass);// need to think of the link
        System.out.println("userName "+receiver + "Subject: "+subject+"body: "+body);
        emailSessionBean.sendEmail(email, subject, body);
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Email has been sent ! Please check your personal email","");
        RequestContext.getCurrentInstance().update("growll");
        FacesContext.getCurrentInstance().addMessage(null, message);
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
