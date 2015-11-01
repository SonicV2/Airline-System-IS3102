/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import DCS.Session.BaggageSessionBeanLocal;
import Distribution.Entity.Baggage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "baggageManagedBean")
@SessionScoped
@ManagedBean
public class BaggageManagedBean implements Serializable {

    @EJB
    private BaggageSessionBeanLocal baggageSessionBean;

    @ManagedProperty(value = "#{searchBookingManagedBean}")
    private SearchBookingManagedBean searchBookingManagedBean;

    private Double totalWeightAllowed;
    private List<Baggage> allBaggage; //baggage weights for all added baggage
    private double addBagWeight; // add baggage

    public BaggageManagedBean() {
    }

    @PostConstruct
    public void init() {

    }

    public void addBaggage(ActionEvent event) {
        FacesMessage message = null;
        String msg = baggageSessionBean.addBaggage(searchBookingManagedBean.getReqBooking(), addBagWeight, totalWeightAllowed);
        if (msg.equals("excess")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Overweight, Please Pay Additional Charge!", "");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Baggage Added Successfully!", "");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void retrieveAllBaggages() {
        setAllBaggage(baggageSessionBean.retrieveAllBaggages(searchBookingManagedBean.getReqBooking()));
    }

    public void retrieveNumberofBaggageAllowed() {
        int i = baggageSessionBean.retrieveNumberOfBaggageAllowed(getSearchBookingManagedBean().getReqBooking().getClassCode());
        setTotalWeightAllowed((i * 15.00));
    }

    /**
     * @return the totalWeightAllowed
     */
    public Double getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    /**
     * @param totalWeightAllowed the totalWeightAllowed to set
     */
    public void setTotalWeightAllowed(Double totalWeightAllowed) {
        this.totalWeightAllowed = totalWeightAllowed;
    }

    /**
     * @return the searchBookingManagedBean
     */
    public SearchBookingManagedBean getSearchBookingManagedBean() {
        return searchBookingManagedBean;
    }

    /**
     * @param searchBookingManagedBean the searchBookingManagedBean to set
     */
    public void setSearchBookingManagedBean(SearchBookingManagedBean searchBookingManagedBean) {
        this.searchBookingManagedBean = searchBookingManagedBean;
    }

    /**
     * @return the addBagWeight
     */
    public double getAddBagWeight() {
        return addBagWeight;
    }

    /**
     * @param addBagWeight the addBagWeight to set
     */
    public void setAddBagWeight(double addBagWeight) {
        this.addBagWeight = addBagWeight;
    }

    /**
     * @return the allBaggage
     */
    public List<Baggage> getAllBaggage() {
        return allBaggage;
    }

    /**
     * @param allBaggage the allBaggage to set
     */
    public void setAllBaggage(List<Baggage> allBaggage) {
        this.allBaggage = allBaggage;
    }

}
