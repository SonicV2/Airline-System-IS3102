/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;
import Inventory.Entity.BookingClass;
import Inventory.Entity.SeatAvailability;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import Inventory.Session.PricingManagementLocal;
import java.util.Date;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import Inventory.Session.BookingSessionBeanLocal;
import java.util.Calendar;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author YiQuan
 */
@Named(value = "priceManageBean")
@ManagedBean
@SessionScoped
public class priceManageBean implements Serializable{
    
    @EJB
    private PricingManagementLocal rm;
    @EJB
    private BookingSessionBeanLocal bm;
    
    private static final long serialVersionUID = 1L;
    private List<SeatPrice> spList = new ArrayList();
    private String flightNo;
    private String flightDate;
    private String flightTime;
    private SeatAvailability sa;
    private Date fDate;
    /**
     *
     */
    
    public String get1Flight(String flightNo, Date fDate){
         this.flightNo = flightNo;
         this.fDate = fDate;
         getPriceList();
         return "displayPrices";
     }
    
    
    public String makeBooking(String price, String serviceType, int avail){
        if(avail<=0){
           FacesMessage msg = new FacesMessage(serviceType + " Tickets Sold Out");
           FacesContext.getCurrentInstance().addMessage(null, msg);
           return "displayPrices";
        }
        double price1 = Double.parseDouble(price);
        sa= rm.findSA(fDate, flightNo);
        bm.addBooking(price1, serviceType, sa);
        getPriceList();
        FacesMessage msg = new FacesMessage(serviceType + "  ticket is booked");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "displayPrices";
    }
    
     
     public void getPriceList(){
        List<SeatPrice> spList1 = new ArrayList();
        int[] avail;
        String price;
        avail = rm.getAvail(getFlightNo(), fDate);
        System.out.println("Get Price List()");
        for(int i=0;i<5; i++){
            SeatPrice es = new SeatPrice();
            es.setAvail(avail[i]);
            if(i==0)
                es.setServiceType("Economy Saver");
            if(i==1)
                es.setServiceType("Economy Basic");
            if(i==2)
                es.setServiceType("Economy Premium");
            if(i==3)
                es.setServiceType("Business");
            if(i==4)
                es.setServiceType("First Class");
            es.setPrice( rm.getPrice(getFlightNo(), fDate, es.getServiceType(), avail[i+5])  );
            System.out.println(es.getServiceType());
            System.out.println(es.getPrice());
            System.out.println(es.getAvail());
            spList1.add(es);
        }
        
        setSpList(spList1);
    }    

    /**
     * @return the spList
     */
    public List<SeatPrice> getSpList() {
        return spList;
    }

    /**
     * @param spList the spList to set
     */
    public void setSpList(List<SeatPrice> spList) {
        this.spList = spList;
    }

    /**
     * @return the flightNo
     */
    public String getFlightNo() {
        return flightNo;
    }

    /**
     * @param flightNo the flightNo to set
     */
    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    /**
     * @return the flightDate
     */
    public String getFlightDate() {
        return flightDate;
    }

    /**
     * @param flightDate the flightDate to set
     */
    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    /**
     * @return the flightTime
     */
    public String getFlightTime() {
        return flightTime;
    }

    /**
     * @param flightTime the flightTime to set
     */
    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

 
   
    
}

