/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import CI.Session.EmailSessionBeanLocal;
import Distribution.Entity.TravelAgency;
import Distribution.Session.TravelAgencySessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author parthasarthygupta
 */
@Named(value = "travelAgencyManagedBean")
@ManagedBean
@SessionScoped
public class TravelAgencyManagedBean {
    
     @EJB
    private TravelAgencySessionBeanLocal travelAgencySessionBean;
     
     @EJB
    private EmailSessionBeanLocal emailSessionBean;
    
    private List<TravelAgency> travelAgencies;
    private TravelAgency selectedAgency;
     
     private String name;
     private String address;
     private String primaryContact;
     private String contactNo;
     private double maxCredit;
     private double currentCredit;
     private double commission;
     private String email;
     private TravelAgency travelAgency;
     
     private String subject;
     private String body;
     private String hashedPassword;
     private String password;
     
     private String feedbackMessage;
     
     private boolean isAgencyLoggedOn;
     
     
     @PostConstruct
     public void retrieve(){
        
         
     }
     
     public void addTravelAgency(){
         
         setEmail(email);
         
         setPassword(emailSessionBean.passGen());
         
         travelAgencySessionBean.addTravelAgency(name, maxCredit, maxCredit, 0.0, email, address, contactNo, password, primaryContact);
         
         sendEmail(getEmail());
         
         setFeedbackMessage("Travel Agency is registered successfully!");
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
         FacesContext.getCurrentInstance().addMessage(null, message);

         clear();
     }
     
     public void clear() {
         setEmail(null);
         setPassword(null);
         setName(null);
         setMaxCredit(0.0);
         setAddress(null);
         setContactNo(null);
         setPrimaryContact(null);
     }
             
     
     public void sendEmail(String email) {

        setSubject("*Confidential*--- Merlion Airelines Travel Agency Account ");
        setBody("Your password: " + password);
        emailSessionBean.sendEmail(email, getSubject(), getBody());
        
    }
     
     public String loginCheck() {

        if (doLogin(email, password)) {
            isAgencyLoggedOn = true;
            return "TravelAgencyDashboard";

            //return "CustomerDashboard";
        } else {
            return "SignUpConfirmation";
        }

    }
     
     public Boolean doLogin(String agencyEmail, String agencyPassword) {
        if (agencyEmail.equals("") || agencyPassword.equals("")) {
            setFeedbackMessage("Please enter your email or password!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return false;
        } else {
            setTravelAgency(travelAgencySessionBean.getAgencyUseEmail(agencyEmail));
            if (getTravelAgency() == null) {
                setFeedbackMessage("You have entered an invalid email!");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return false;
            } else if (travelAgencySessionBean.isSameHash(agencyEmail, agencyPassword)) {
                setFeedbackMessage("Login Successful!");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return true;
                //means the password and email match
//                  redirect();
            } else {
                setFeedbackMessage("Username and password do not match");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return false;
            }
        }
    }

    /**
     * @return the travelAgencies
     */
    public List<TravelAgency> getTravelAgencies() {
        return travelAgencies;
    }

    /**
     * @param travelAgencies the travelAgencies to set
     */
    public void setTravelAgencies(List<TravelAgency> travelAgencies) {
        this.travelAgencies = travelAgencies;
    }

    /**
     * @return the selectedAgency
     */
    public TravelAgency getSelectedAgency() {
        return selectedAgency;
    }

    /**
     * @param selectedAgency the selectedAgency to set
     */
    public void setSelectedAgency(TravelAgency selectedAgency) {
        this.selectedAgency = selectedAgency;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public double getMaxCredit() {
        return maxCredit;
    }

    public void setMaxCredit(double maxCredit) {
        this.maxCredit = maxCredit;
    }

    public double getCurrentCredit() {
        return currentCredit;
    }

    public void setCurrentCredit(double currentCredit) {
        this.currentCredit = currentCredit;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the travelAgency
     */
    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

    /**
     * @param travelAgency the travelAgency to set
     */
    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
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
     * @return the hashedPassword
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @param hashedPassword the hashedPassword to set
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
     
      /**
     * @return the feedbackMessage
     */
    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    /**
     * @param feedbackMessage the feedbackMessage to set
     */
    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }
    
}
