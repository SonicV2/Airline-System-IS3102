/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import java.util.List;
import Inventory.Entity.BookingClass;
import Inventory.Session.ClassSessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author YiQuan
 */
@Named(value = "classManageBean")
@ManagedBean
@SessionScoped
public class ClassManagedBean {
    
    @EJB
    private ClassSessionBeanLocal cm;
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
    private String season;
    private boolean travelagent;
    
    
     @PostConstruct
    public void init() {
        clear();
        setBookingClass(cm.retrieveBookingClasses());
        
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
    
    public void clear(){
        this.classcode = null;
        this.pricePercent = 0;
        this.advancedSales = 0;
        this.percentSold =0;
        this.serviceClass = null;
        this.rebook = false;
        this.cancel = false;
        this.travelagent = false;
        this.baggage = 0;
        this.millageAccru =0;
    }
    
    public void addClass(){
        if(classcode.isEmpty()){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please fill in Booking Class Code", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(pricePercent==0){
            FacesMessage msg = new FacesMessage("Please fill in Percentage of Base Fare","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(advancedSales==0){
            FacesMessage msg = new FacesMessage("Please fill in Days of Advanced Sales","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(percentSold==0){
            FacesMessage msg = new FacesMessage("Please fill in Percentage of Total Tickets Sold","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(serviceClass==null){
            FacesMessage msg = new FacesMessage("Please Select Service Class","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(baggage==0){
            FacesMessage msg = new FacesMessage("Please Select Baggage Class","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(millageAccru==0){
            FacesMessage msg = new FacesMessage("Amount of Mileage Accruable in Percentage","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(season==null){
            FacesMessage msg = new FacesMessage("Please Fill in Seasonality","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
            String message = cm.addClassCode(getClasscode(), getPricePercent(), getAdvancedSales(), 
            getPercentSold(), getServiceClass(), isRebook(), isCancel(), 
            getBaggage(), getMillageAccru(), getSeason(),travelagent);
            FacesMessage msg = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            setBookingClass(cm.retrieveBookingClasses());
            clear();

    
    }

    public boolean isTravelagent() {
        return travelagent;
    }

    public void setTravelagent(boolean travelagent) {
        this.travelagent = travelagent;
    }
    
    
    
    public void deleteClass(String classCode){
        cm.deleteClassCode(classCode);
        setBookingClass(cm.retrieveBookingClasses());
    }
     

    
    public void onRowEdit(RowEditEvent event) {
        BookingClass edited = (BookingClass)event.getObject();
        classcode = edited.getClasscode();
        BookingClass original = cm.findClass(classcode);
        
        
        if(!edited.equals(original))
        {
            cm.editClassCode(edited);
            FacesMessage msg = new FacesMessage("Fare Class Edited" );
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }else{
           FacesMessage msg = new FacesMessage("Edit Cancelled" ); 
           FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
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
