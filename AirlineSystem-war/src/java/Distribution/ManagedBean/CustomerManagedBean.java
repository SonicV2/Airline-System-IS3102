/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Schedule;
import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import Distribution.Session.CustomerSessionBeanLocal;
import Distribution.Session.DistributionSessionBeanLocal;
import Inventory.Entity.Booking;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;

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
    private String passportNumber;
    private String nationality;
    private String title;
    private boolean isCustomerLoggedOn;
    private List<PNRDisplay> pnrDisplayList;

    @PostConstruct
    public void init() {
        customer = null;
        isCustomerLoggedOn = false;
        pnrDisplayList = new ArrayList();
    }

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
            setFeedbackMessage(customerSessionBean.addCustomer(customerFirstName, customerLastName, customerHpNumber, customerHpNumber, customerEmail, customerPassword, customerAddress, customerGender, customerDOB, title, nationality, passportNumber));
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            clearAll();
            return "signUpConfirmation";
        }
    }

    public String logOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        customer = null;
        isCustomerLoggedOn = false;
        return "/Distribution/merlionAirlines.xhtml?faces-redirect=true";
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
            isCustomerLoggedOn = true;
            return "merlionAirlines";

            //return "customerDashboard";
        } else {
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

    //redirect to customer's dashboard
    public String redirect() {

        return "customerDashboard";

    }

    public String displayCustomerPNRs() {
        pnrDisplayList.clear();
        List<Schedule> selectedSchedules = new ArrayList();

        List<Long> addedSchedules = new ArrayList();
        List<String> addedNames = new ArrayList();
        
        
        if (customer != null) {
            List<PNR> customerPNRs = customerSessionBean.retrieveCustomerPNRs(customer);
            if (customerPNRs != null) {//PNRs found for customer
                System.out.println("Number of pnrs retrieved : " +customerPNRs.size());
                for (PNR eachCustomerPNR : customerPNRs) {
                    System.out.println("FOR EACH PNR");
                    selectedSchedules.clear();
                    addedSchedules.clear();
                    addedNames.clear();
                    PNRDisplay eachPNRDisplay = new PNRDisplay();

                    eachPNRDisplay.setId(eachCustomerPNR.getPnrID());
                    int noOfTravellers = eachCustomerPNR.getNoOfTravellers();
                    System.out.println("No of travellers: " + noOfTravellers);
                    eachPNRDisplay.setNoOfTravellers(noOfTravellers);

                    for (Booking eachBooking : eachCustomerPNR.getBookings()) {
                        if (!addedNames.contains(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName())) {
                            addedNames.add(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName());
                        }

                        if (!addedSchedules.contains(eachBooking.getSeatAvail().getSchedule().getScheduleId())) {
                            addedSchedules.add(eachBooking.getSeatAvail().getSchedule().getScheduleId());
                            selectedSchedules.add(eachBooking.getSeatAvail().getSchedule());

                        }
                    }
                    System.out.println ("Added names size : " + addedNames.size());
                    for (String eachName : addedNames ){
                        System.out.println ("NAME : " + eachName);
                    }
                    
                    for (Schedule eachSelectedSchedule : selectedSchedules) {
                        System.out.println ("Start date of schedule is: " + eachSelectedSchedule.getStartDate());
                        System.out.println ("End date of schedule is: " + eachSelectedSchedule.getEndDate());
                        eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
                        eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
                    }

                    eachPNRDisplay.setTravellerNames(addedNames);
                    System.out.println("traveller names size:" + eachPNRDisplay.getTravellerNames().size());
                    
                    eachPNRDisplay.setUniqueSchedules(selectedSchedules);
                    System.out.println("unique schedules size:" + eachPNRDisplay.getUniqueSchedules().size());
                    
                    String serviceType = eachCustomerPNR.getBookings().get(0).getServiceType();
                    eachPNRDisplay.setServiceType(serviceType);
                    if (serviceType.charAt(0) == 'E') {
                        eachPNRDisplay.setNoOfBags(noOfTravellers);
                    } else if (serviceType.charAt(0) == 'B') {
                        eachPNRDisplay.setNoOfBags(noOfTravellers * 2);
                    } else if (serviceType.charAt(0) == 'F') {
                        eachPNRDisplay.setNoOfBags(noOfTravellers * 3);
                    }
                    pnrDisplayList.add(eachPNRDisplay);
                    
                }
            } else { //no PNRS for customer
                setFeedbackMessage("No bookings associated with your account");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
            System.out.println("PNR DISPLAY LIST SIZE: " + pnrDisplayList.size());
            for (int i=0; i<pnrDisplayList.size(); i++) {
                System.out.println("PNR DISPLAY LIST schedule: " + pnrDisplayList.get(i).getUniqueSchedules());
                System.out.println("PNR DISPLAY LIST traveller: " + pnrDisplayList.get(i).getTravellerNames());
                
            }
            return "DisplayCustomerPNRs";
        }
        
        else {//customer is null
            System.out.println("Customer is null");
            return null;
        }
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

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIsCustomerLoggedOn() {
        return isCustomerLoggedOn;
    }

    public void setIsCustomerLoggedOn(boolean isCustomerLoggedOn) {
        this.isCustomerLoggedOn = isCustomerLoggedOn;
    }

    public List<PNRDisplay> getPnrDisplayList() {
        return pnrDisplayList;
    }

    public void setPnrDisplayList(List<PNRDisplay> pnrDisplayList) {
        this.pnrDisplayList = pnrDisplayList;
    }

}
