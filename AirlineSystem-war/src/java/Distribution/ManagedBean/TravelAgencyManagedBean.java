/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import Distribution.Entity.TravelAgency;
import Distribution.Session.TravelAgencySessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

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
     
     
     @PostConstruct
     public void retrieve(){
     }
     
     public String addTravelAgency(){
         travelAgency = new TravelAgency();
         
         //salt is already persisted, salt associated with customer
         travelAgency.createTravelAgent(name, maxCredit, maxCredit, 0.0, email, address, contactNo, password, primaryContact);
         
         
         travelAgencySessionBean.persistTravelAgency(travelAgency);
         return null;
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
     
     
    
}
