/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author YiQuan
 */
@Entity
public class BookingClass implements Serializable {
    private static long serialVersionUID = 1L;

 
    @Id
    private String classcode; //Unique identifier for user retrieve
    private int pricePercent;
    private int advancedSales;
    private int percentSold;
    private String serviceClass;
    private boolean rebook;
    private boolean cancel;
    private int baggage;
    private int millageAccru;

    
    
    public void createClass(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru){
        this.classcode= classcode;
        this.pricePercent = pricePercent;
        this.advancedSales = advancedSales;
        this.percentSold= percentSold;
        this.serviceClass= serviceClass;
        this.rebook = rebook;
        this.cancel = cancel;
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
            
    
 
    
    
}
