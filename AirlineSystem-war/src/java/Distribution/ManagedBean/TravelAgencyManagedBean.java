/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import CI.Session.EmailSessionBeanLocal;
import Distribution.Entity.TravelAgency;
import static Distribution.Entity.TravelAgency_.password;
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
     
     private String name;
     private String address;
     private String primaryContact;
     private String contactNo;
     private double maxCredit;
     private double currentCredit;
     private double commission;
     private String email;
     private TravelAgency travelAgency;
     private List<PNR> pendingPNRs;
     
     private String subject;
     private String body;
     private String hashedPassword;
     private String password;
     
     private String feedbackMessage;
     
     private boolean isAgencyLoggedOn;
     
     
     @PostConstruct
     public void retrieve(){
        
         
     }
     
     public String addTravelAgency(){
         travelAgency = new TravelAgency();
         
         //salt is already persisted, salt associated with customer
         travelAgency.createTravelAgent(name, maxCredit, maxCredit, 0.0, email, address, contactNo, password, primaryContact);
         
         setEmail(email);
         
         travelAgencySessionBean.persistTravelAgency(travelAgency);
         return null;
     }
     

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

    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }

    public List<PNR> getPendingPNRs() {
        return pendingPNRs;
    }

    public void setPendingPNRs(List<PNR> pendingPNRs) {
        this.pendingPNRs = pendingPNRs;
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
