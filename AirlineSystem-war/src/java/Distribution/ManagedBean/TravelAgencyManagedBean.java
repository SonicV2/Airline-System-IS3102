/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import CI.Session.EmailSessionBeanLocal;
import Distribution.Entity.TravelAgency;
import Distribution.Session.TravelAgencySessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
    private TravelAgencyDisplay travelAgencyDisplay;
     
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
    private Long id;
    private String newPassword;
    private String confirmedPassword;

    @PostConstruct
    public void retrieve() {
        
        setTravelAgencies(travelAgencySessionBean.getAllTravelAgencies());

    }

    public void addTravelAgency() {
        
        if (travelAgencySessionBean.emailExists(email)) {
            setFeedbackMessage("Please use a different email, this email already exists!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        setEmail(email);

        setPassword(emailSessionBean.passGen());

        travelAgencySessionBean.addTravelAgency(name, maxCredit, maxCredit, 0.0, email, address, contactNo, password, primaryContact);

        setTravelAgency(travelAgencySessionBean.getAgencyUseEmail(email));
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
        setTravelAgency(null);
    }

    public void sendEmail(String email) {

        setSubject("*Confidential*--- Merlion Airelines Travel Agency Account ");
        setBody("Your password: " + password + "\nTravel Agency Id: " + travelAgency.getId()
                + "\n\nTravel Agency Id is a unique identification number provided to each agency."
                + "\nPlease log in to the system using your email, not the Travel Agency Id"
                + "\n\nThank you.");
        
        emailSessionBean.sendEmail(email, getSubject(), getBody());

    }

    public String loginCheck() {

        if (doLogin(email, password)) {
            setIsAgencyLoggedOn(true);
            return "TravelAgencyDashboard";

            //return "CustomerDashboard";
        } else {
            return "";
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
    

    public void validateUser(ActionEvent event) {
        setEmail(travelAgencySessionBean.validateUser(email, getId())); //get employee's email address 
        FacesMessage message = null;
        if (getEmail().equals("nomatch")) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Name and Travel Agency Id do not match!", "");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else if (getEmail().equals("nouser")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Such User Name!", "");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setPassword(emailSessionBean.passGen());
            travelAgencySessionBean.hashNewPwd(email, getPassword());

            sendNewPasswordEmail(getEmail());
            
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password has been sent to your email!", "");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void sendNewPasswordEmail(String email) {

        setSubject("*Confidential*--- Merlion Airelines Travel Agency New Password ");
        setBody("Your new password: " + password);
        emailSessionBean.sendEmail(email, getSubject(), getBody());

    }

    public String changePassword() {
        if (!travelAgencySessionBean.isSameHash(email, password)) {
            setFeedbackMessage("Incorrect old password!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "TravelAgencyChangePassword";

        } else if (!newPassword.equals(confirmedPassword)) {
            setFeedbackMessage("The passwords you entered do not match!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "TravelAgencyChangePassword";
        } else {

            travelAgency.setPassword(travelAgencySessionBean.newPassword(email, newPassword));
            travelAgencySessionBean.updateAgencyProfile(travelAgency);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been updated!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

        return "TravelAgencyDashboard";

    }
    
    public String updateProfile() {
        
        name = travelAgency.getName();
        email = travelAgency.getEmail();
        primaryContact = travelAgency.getPrimaryContact();
        contactNo = travelAgency.getContactNo();
        address = travelAgency.getAddress();
        maxCredit = travelAgency.getMaxCredit();
        currentCredit = travelAgency.getCurrentCredit();
        id = travelAgency.getId();
        
        return "TravelAgencyUpdateProfile";
    }
    
    public String persistUpdatedProfile() {

            travelAgency.setName(name);
            travelAgency.setEmail(email);
            travelAgency.setPrimaryContact(primaryContact);
            travelAgency.setContactNo(contactNo);
            travelAgency.setAddress(address);


            travelAgencySessionBean.updateAgencyProfile(travelAgency);

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your profile has been updated!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

            return "TravelAgencyDashboard";
    }

    public String viewTravelAgencies(){
        if (travelAgencies==null || travelAgencies.isEmpty()){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No record found!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return "ViewAllTravelAgencies";
    }
    
    public String viewTravelAgencyProfile(Long id){
        travelAgencyDisplay = new TravelAgencyDisplay();
         
        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));
        
        int noOfBookings = travelAgencySessionBean.noOfConfirmedBookings(travelAgency);
        travelAgencyDisplay.setTravelAgency(travelAgency);
        travelAgencyDisplay.setNoOfConfirmedBookings(noOfBookings);
   
        return "ViewTravelAgencyProfile";
    }
    
    public String manageTravelAgency(Long id){
        travelAgencyDisplay = new TravelAgencyDisplay();
        
        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));
        travelAgencyDisplay.setTravelAgency(travelAgency);
        
        return "ManageTravelAgency";
        
        
    }
    
    public void reset() {
        
        travelAgencySessionBean.resetCreditsAndCommission(travelAgency);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Credits and Commission have been reset!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
    }
    
    public void changeLimit() {
        
        travelAgencySessionBean.changeCreditLimit(travelAgency, maxCredit);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Limit has been changed!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void deleteTravelAgency(Long id) {
        
        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));
        System.out.println("IN MANAGED BEAN: " + travelAgency);
        travelAgencySessionBean.deleteTravelAgency(travelAgency);
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Travel Agency has been deleted!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    /**
     * @return the isAgencyLoggedOn
     */
    public boolean isIsAgencyLoggedOn() {
        return isAgencyLoggedOn;
    }

    /**
     * @param isAgencyLoggedOn the isAgencyLoggedOn to set
     */
    public void setIsAgencyLoggedOn(boolean isAgencyLoggedOn) {
        this.isAgencyLoggedOn = isAgencyLoggedOn;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the confirmedPassword
     */
    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    /**
     * @param confirmedPassword the confirmedPassword to set
     */
    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public TravelAgencyDisplay getTravelAgencyDisplay() {
        return travelAgencyDisplay;
    }

    public void setTravelAgencyDisplay(TravelAgencyDisplay travelAgencyDisplay) {
        this.travelAgencyDisplay = travelAgencyDisplay;
    }

    
}
