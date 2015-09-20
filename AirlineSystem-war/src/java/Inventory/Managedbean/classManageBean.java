/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;
import Inventory.Session.ClassManagementLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;
import Inventory.Entity.BookingClass;
import javax.annotation.PostConstruct;

/**
 *
 * @author YiQuan
 */
@Named(value = "classManageBean")
@ManagedBean
@SessionScoped
public class classManageBean {
    
    @EJB
    private ClassManagementLocal cm;
    private String classcode; 
    private int pricePercent;
    private int advancedSales;
    private int percentSold;
    private String serviceClass;
    private boolean rebook;
    private boolean cancel;
    private int baggage;
    private int millageAccru;
    private List<BookingClass> bookingClass;
    
    
     @PostConstruct
    public void init() {
        setBookingClass(cm.retrieveBookingClasses());
        
    }
    
    public void addClass(){
    cm.addClassCode(getClasscode(), getPricePercent(), getAdvancedSales(), getPercentSold(), getServiceClass(), isRebook(), isCancel(), getBaggage(), getMillageAccru());
    }
    
    public void deleteClass(){
        cm.deleteClassCode(getClasscode());
    }
     
    public void editClass(){
        cm.editClassCode(getClasscode(), getPricePercent(), getAdvancedSales(), getPercentSold(), getServiceClass(), isRebook(), isCancel(), getBaggage(), getMillageAccru());
    }

    /**
     * @return the classcode
     */
    public String getClasscode() {
        return classcode;
    }

    /**
     * @param classcode the classcode to set
     */
    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }

    /**
     * @return the pricePercent
     */
    public int getPricePercent() {
        return pricePercent;
    }

    /**
     * @param pricePercent the pricePercent to set
     */
    public void setPricePercent(int pricePercent) {
        this.pricePercent = pricePercent;
    }

    /**
     * @return the advancedSales
     */
    public int getAdvancedSales() {
        return advancedSales;
    }

    /**
     * @param advancedSales the advancedSales to set
     */
    public void setAdvancedSales(int advancedSales) {
        this.advancedSales = advancedSales;
    }

    /**
     * @return the percentSold
     */
    public int getPercentSold() {
        return percentSold;
    }

    /**
     * @param percentSold the percentSold to set
     */
    public void setPercentSold(int percentSold) {
        this.percentSold = percentSold;
    }

    /**
     * @return the serviceClass
     */
    public String getServiceClass() {
        return serviceClass;
    }

    /**
     * @param serviceClass the serviceClass to set
     */
    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    /**
     * @return the rebook
     */
    public boolean isRebook() {
        return rebook;
    }

    /**
     * @param rebook the rebook to set
     */
    public void setRebook(boolean rebook) {
        this.rebook = rebook;
    }

    /**
     * @return the cancel
     */
    public boolean isCancel() {
        return cancel;
    }

    /**
     * @param cancel the cancel to set
     */
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * @return the baggage
     */
    public int getBaggage() {
        return baggage;
    }

    /**
     * @param baggage the baggage to set
     */
    public void setBaggage(int baggage) {
        this.baggage = baggage;
    }

    /**
     * @return the millageAccru
     */
    public int getMillageAccru() {
        return millageAccru;
    }

    /**
     * @param millageAccru the millageAccru to set
     */
    public void setMillageAccru(int millageAccru) {
        this.millageAccru = millageAccru;
    }

    /**
     * @return the bookingClass
     */
    public List<BookingClass> getBookingClass() {
        return bookingClass;
    }

    /**
     * @param bookingClass the bookingClass to set
     */
    public void setBookingClass(List<BookingClass> bookingClass) {
        this.bookingClass = bookingClass;
    }


 
   
    
}
