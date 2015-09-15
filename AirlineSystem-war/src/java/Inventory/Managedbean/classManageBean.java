/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;
import Inventory.Session.ClassManagementLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
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
    String classcode; 
    int pricePercent;
    int advancedSales;
    int percentSold;
    String serviceClass;
    boolean rebook;
    boolean cancel;
    int baggage;
    int millageAccru;
    
    public void addClass(){
    cm.addClassCode(classcode, pricePercent, advancedSales, percentSold, serviceClass, rebook, cancel, baggage, millageAccru);
    }
    
    public void deleteClass(){
        cm.deleteClassCode(classcode);
    }
     
    public void editClass(){
        cm.editClassCode(classcode, pricePercent, advancedSales, percentSold, serviceClass, rebook, cancel, baggage, millageAccru);
    }

    public ClassManagementLocal getCm() {
        return cm;
    }

    public void setCm(ClassManagementLocal cm) {
        this.cm = cm;
    }

    public String getClasscode() {
        return classcode;
    }

    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }

    public int getPricePercent() {
        return pricePercent;
    }

    public void setPricePercent(int pricePercent) {
        this.pricePercent = pricePercent;
    }

    public int getAdvancedSales() {
        return advancedSales;
    }

    public void setAdvancedSales(int advancedSales) {
        this.advancedSales = advancedSales;
    }

    public int getPercentSold() {
        return percentSold;
    }

    public void setPercentSold(int percentSold) {
        this.percentSold = percentSold;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public boolean isRebook() {
        return rebook;
    }

    public void setRebook(boolean rebook) {
        this.rebook = rebook;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public int getBaggage() {
        return baggage;
    }

    public void setBaggage(int baggage) {
        this.baggage = baggage;
    }

    public int getMillageAccru() {
        return millageAccru;
    }

    public void setMillageAccru(int millageAccru) {
        this.millageAccru = millageAccru;
    }
    
    
    
}
