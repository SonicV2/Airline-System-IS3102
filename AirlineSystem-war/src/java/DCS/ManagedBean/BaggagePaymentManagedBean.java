/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import DCS.Session.BaggageSessionBeanLocal;
import DCS.Session.CheckInRecordSessionBeanLocal;
import Distribution.Entity.Baggage;
import Inventory.Entity.Booking;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author HOULIANG
 */
@Named(value = "baggagePaymentManagedBean")
@SessionScoped
@ManagedBean
public class BaggagePaymentManagedBean implements Serializable {
    @EJB
    private CheckInRecordSessionBeanLocal checkInRecordSessionBean;

    @EJB
    private BaggageSessionBeanLocal baggageSessionBean;
    

    private String creditCard;
    private String csv;
    private Date paymentDate;
    private double extra; // total amount for additional bags

    private double addBagWeight; // over weight
    private double temp; // totalweight + addbagweight

    private Booking booking;
    private List<Baggage> baggages;

    private double totalCosts; // baggage + upgrade if necessary

    public String makePayment() {
        paymentDate = new Date();
        baggageSessionBean.addExtraBaggage(booking, addBagWeight, extra);
        checkInRecordSessionBean.addcreditCardNo(booking, creditCard, paymentDate);
        
        return "GenerateBaggageTag.html";
    }
 

    public BaggagePaymentManagedBean() {
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    public BaggageSessionBeanLocal getBaggageSessionBean() {
        return baggageSessionBean;
    }

    public void setBaggageSessionBean(BaggageSessionBeanLocal baggageSessionBean) {
        this.baggageSessionBean = baggageSessionBean;
    }




    public double getAddBagWeight() {
        return addBagWeight;
    }

    public void setAddbagWeight(double addbagWeight) {
        this.addBagWeight = addbagWeight;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Baggage> getBaggages() {
        return baggages;
    }

    public void setBaggages(List<Baggage> baggages) {
        this.baggages = baggages;
    }

    /**
     * @return the temp
     */
    public double getTemp() {
        return temp;
    }

    /**
     * @param temp the temp to set
     */
    public void setTemp(double temp) {
        this.temp = temp;
    }

    /**
     * @return the totalCosts
     */
    public double getTotalCosts() {
        return totalCosts;
    }

    /**
     * @param totalCosts the totalCosts to set
     */
    public void setTotalCosts(double totalCosts) {
        this.totalCosts = totalCosts;
    }
    

}
