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
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "baggageManagedBean")
@RequestScoped
@ManagedBean
public class BaggageManagedBean implements Serializable {

    @EJB
    private BaggageSessionBeanLocal baggageSessionBean;

    @ManagedProperty(value = "#{searchBookingManagedBean}")
    private SearchBookingManagedBean searchBookingManagedBean;
    
    @ManagedProperty(value = "#{baggagePaymentManagedBean}")
    private BaggagePaymentManagedBean baggagePaymentManagedBean;
    
    
    private double totalWeightAllowed;
    private List<Baggage> allBaggage; //baggage weights for all added baggage
    private double addBagWeight; // add baggage

    private double totalweight;
    
    private double extraPayment;
    
    private String banddepart;
    private String bandarr;
    
    String departure;
    String destination;
    
    public BaggageManagedBean() {
    }

    @PostConstruct
    public void init() {
        retrieveAllBaggages();
        totalweight=baggageSessionBean.retrieveTotalBaggageWeights(searchBookingManagedBean.getReqBooking());
    }
    
    
    public void calculateExtra(ActionEvent event){
        retrieveAllBaggages();
        totalweight=baggageSessionBean.retrieveTotalBaggageWeights(searchBookingManagedBean.getReqBooking());
        
        
        
        departure=searchBookingManagedBean.getReqBooking().getSeatAvail().getSchedule().getFlight().getRoute().getOriginCountry();
        destination=searchBookingManagedBean.getReqBooking().getSeatAvail().getSchedule().getFlight().getRoute().getDestinationCountry();
        
        if(departure.equals("Singapore")){
        banddepart="Singapore";}else{
            banddepart=baggageSessionBean.bandSearch(departure);
        }
        
         if(destination.equals("Singapore")){
        bandarr="Singapore";}else{
            bandarr=baggageSessionBean.bandSearch(destination);
        }
            
        
        
        retrieveNumberofBaggageAllowed();
        
        double temp=(totalweight+addBagWeight);
        
        System.out.println("AAAAA total allow" + totalWeightAllowed );
        
        this.extraPayment=baggageSessionBean.calcualtePenalty(departure,destination,totalWeightAllowed,temp);
        
        baggagePaymentManagedBean.setExtra(extraPayment);
    
    
    }

    public void addBaggage(ActionEvent event) {
        FacesMessage message = null;
        retrieveNumberofBaggageAllowed();
        retrieveAllBaggages();
        totalweight=baggageSessionBean.retrieveTotalBaggageWeights(searchBookingManagedBean.getReqBooking());
    
       
        String msg = baggageSessionBean.addBaggage(searchBookingManagedBean.getReqBooking(), addBagWeight, totalWeightAllowed);
        if (msg.equals("excess")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Overweight, Please Pay Additional Charge!", "");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Baggage Added Successfully!", "");
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
    public double getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    /**
     * @param totalWeightAllowed the totalWeightAllowed to set
     */
    public void setTotalWeightAllowed(double totalWeightAllowed) {
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

    public double getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(double totalweight) {
        this.totalweight = totalweight;
    }

    public double getExtraPayment() {
        return extraPayment;
    }

    public void setExtraPayment(double extraPayment) {
        this.extraPayment = extraPayment;
    }

    public String getBanddepart() {
        return banddepart;
    }

    public String getBandarr() {
        return bandarr;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BaggagePaymentManagedBean getBaggagePaymentManagedBean() {
        return baggagePaymentManagedBean;
    }

    public void setBaggagePaymentManagedBean(BaggagePaymentManagedBean baggagePaymentManagedBean) {
        this.baggagePaymentManagedBean = baggagePaymentManagedBean;
    }
    
    
    

}
