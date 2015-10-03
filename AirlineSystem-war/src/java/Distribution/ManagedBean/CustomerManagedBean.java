/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import Distribution.Session.CustomerSessionBean;
import Distribution.Session.CustomerSessionBeanLocal;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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
    private String customerAddress;
    private Date customerDOB;
    private String customerGender;
    private String feedbackMessage; //the faces message for users.

    /**
     * Creates a new instance of CustomerManagedBean
     */
    public CustomerManagedBean() {
    }

    
    public void addCustomer (ActionEvent event){
        setFeedbackMessage(customerSessionBean.addCustomer(customerFirstName, customerLastName, customerHpNumber, customerHpNumber, customerEmail, customerPassword, customerAddress, customerGender, customerDOB));
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        clearAll();
    }
    
    public void clearAll(){
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
    
}
