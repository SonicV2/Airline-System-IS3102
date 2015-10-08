/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import Distribution.Entity.Customer;
import Distribution.Session.CustomerSessionBean;
import Distribution.Session.CustomerSessionBeanLocal;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Yuqing
 */
@ManagedBean
@Named(value = "customerManagedBean")
@SessionScoped
public class CustomerManagedBean {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    private Long customerID;
    private String customerFirstName;
    private String customerLastName;
    private String customerHpNumber;
    private String customerHomeNumber;
    private int customerMileagePoints;

    private String customerEmail;
    private String customerPassword;
    private String customerConfirmedPassword;
    private String customerAddress;
    private Date customerDOB;
    private String customerGender;
    private String feedbackMessage; //the faces message for users.

    private Customer customer;

    /**
     * Creates a new instance of CustomerManagedBean
     */
    public CustomerManagedBean() {
    }

    public String addCustomer() {
        if (customerSessionBean.emailExists(customerEmail)) {
            setFeedbackMessage("Please use a different email, this email already exists!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "merlionAirlinesSignUp";

        } else if (!customerPassword.equals(customerConfirmedPassword)) {
            setFeedbackMessage("The passwords you entered do not match!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "merlionAirlinesSignUp";
        } else {
            setFeedbackMessage(customerSessionBean.addCustomer(customerFirstName, customerLastName, customerHpNumber, customerHpNumber, customerEmail, customerPassword, customerAddress, customerGender, customerDOB));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            clearAll();
            return "signUpConfirmation";
        }
    }

    public void clearAll() {
        setCustomerFirstName("");
        setCustomerLastName("");
        setCustomerHpNumber("");
        setCustomerHomeNumber("");
        setCustomerEmail("");
        setCustomerPassword("");
        setCustomerAddress("");
        setCustomerDOB(null);
        setCustomerGender("");

    }

    public String loginCheck() {

        if (doLogin(customerEmail, customerPassword)) {

            return "customerDashboard";
        } else {

            System.out.println("managed bean --- log in credentials are not right");
            return "signUpConfirmation";
        }

    }

    public Boolean doLogin(String customerEmail, String customerPassword) {
        if (customerEmail.equals("") || customerPassword.equals("")) {
            setFeedbackMessage("Please enter your email or password!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return false;
        } else {
            setCustomer(customerSessionBean.getCustomerUseEmail(customerEmail));
            if (getCustomer() == null) {
                setFeedbackMessage("You have entered an invalid email!");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return false;
            } else if (customerSessionBean.isSameHash(customerEmail, customerPassword)) {
                setFeedbackMessage("Your password does not match!");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return true;
                //means the password and email match
//                  redirect();
            } else {
                return false;
            }
        }
    }

    //redirect to customer's dashboard
    public String redirect() {

        return "customerDashboard";

    }

    /**
     * @return the customerSessionBean
     */
    public CustomerSessionBeanLocal getCustomerSessionBean() {
        return customerSessionBean;
    }

    /**
     * @param customerSessionBean the customerSessionBean to set
     */
    public void setCustomerSessionBean(CustomerSessionBeanLocal customerSessionBean) {
        this.customerSessionBean = customerSessionBean;
    }

    /**
     * @return the customerID
     */
    public Long getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customerID to set
     */
    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    /**
     * @return the customerFirstName
     */
    public String getCustomerFirstName() {
        return customerFirstName;
    }

    /**
     * @param customerFirstName the customerFirstName to set
     */
    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    /**
     * @return the customerLastName
     */
    public String getCustomerLastName() {
        return customerLastName;
    }

    /**
     * @param customerLastName the customerLastName to set
     */
    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    /**
     * @return the customerHpNumber
     */
    public String getCustomerHpNumber() {
        return customerHpNumber;
    }

    /**
     * @param customerHpNumber the customerHpNumber to set
     */
    public void setCustomerHpNumber(String customerHpNumber) {
        this.customerHpNumber = customerHpNumber;
    }

    /**
     * @return the customerHomeNumber
     */
    public String getCustomerHomeNumber() {
        return customerHomeNumber;
    }

    /**
     * @param customerHomeNumber the customerHomeNumber to set
     */
    public void setCustomerHomeNumber(String customerHomeNumber) {
        this.customerHomeNumber = customerHomeNumber;
    }

    /**
     * @return the customerMileagePoints
     */
    public int getCustomerMileagePoints() {
        return customerMileagePoints;
    }

    /**
     * @param customerMileagePoints the customerMileagePoints to set
     */
    public void setCustomerMileagePoints(int customerMileagePoints) {
        this.customerMileagePoints = customerMileagePoints;
    }

    /**
     * @return the customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customerEmail the customerEmail to set
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * @return the customerPassword
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * @param customerPassword the customerPassword to set
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    /**
     * @return the customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress the customerAddress to set
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return the customerDOB
     */
    public Date getCustomerDOB() {
        return customerDOB;
    }

    /**
     * @param customerDOB the customerDOB to set
     */
    public void setCustomerDOB(Date customerDOB) {
        this.customerDOB = customerDOB;
    }

    /**
     * @return the customerGender
     */
    public String getCustomerGender() {
        return customerGender;
    }

    /**
     * @param customerGender the customerGender to set
     */
    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
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
     * @return the customerConfirmedPassword
     */
    public String getCustomerConfirmedPassword() {
        return customerConfirmedPassword;
    }

    /**
     * @param customerConfirmedPassword the customerConfirmedPassword to set
     */
    public void setCustomerConfirmedPassword(String customerConfirmedPassword) {
        this.customerConfirmedPassword = customerConfirmedPassword;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
