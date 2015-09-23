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
import Inventory.Session.RevenueManagementLocal;
import java.util.Date;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import Inventory.Session.BookingSessionBeanLocal;
import java.util.Calendar;

/**
 *
 * @author YiQuan
 */
@Named(value = "priceManageBean")
@ManagedBean
@SessionScoped
public class priceManageBean implements Serializable{
    
    @EJB
    private RevenueManagementLocal rm;
    @EJB
    private BookingSessionBeanLocal bm;
    
    private static final long serialVersionUID = 1L;
    private List<SeatPrice> spList = new ArrayList();
    private String flightNo;
    private String flightDate;
    private String flightTime;
    private SeatAvailability sa;
    
    /**
     *
     */
    @PostConstruct
    public void get1Flight(){
         setFlightNo("SQ001");
         setFlightDate("25092015");
         setFlightTime("0100");
         getPriceList();    
     }
    
    
    public String makeBooking(String price, String serviceType){
        double price1 = Double.parseDouble(price);
        Date date = rm.convertToDate(flightDate, flightTime);
        sa= rm.findSA(date, flightNo);
        bm.addBooking(price1, serviceType, sa);
        getPriceList();
        return "displayPrices";
    }
    
     
     public void getPriceList(){
        List<SeatPrice> spList1 = new ArrayList();
        Date date = rm.convertToDate(getFlightDate(), getFlightTime());
        int[] avail;
        String price;
        avail = rm.getAvail(getFlightNo(), date);
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
            es.setPrice( rm.getPrice(getFlightNo(), date, es.getServiceType(), avail[i+5])  );
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

