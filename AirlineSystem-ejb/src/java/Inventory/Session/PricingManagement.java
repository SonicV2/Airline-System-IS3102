/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import Inventory.Entity.SeatAvailability;
import Inventory.Entity.BookingClass;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import APS.Entity.Flight;
import Inventory.Entity.Booking;
import APS.Entity.Schedule;


/**
 *
 * @author YiQuan
 */
@Stateless
public class PricingManagement implements PricingManagementLocal{
    
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    private Date rDate;
    
    // Generate the amount of seat available which includes overbooking based on demand forecast
    // Currently it is dummy generating availability
    @Override
    public int[] generateAvailability(int economy, int business, int firstClass){
        int[] seats = new int[5];
        seats[0]= (economy/3)+5;
        seats[1]= (economy/3)+5;
        seats[2]= (economy/3)+5;
        seats[3]= business+5;
        seats[4]= business+5;
        return seats;
    }
    
    
    //Find the seat availability of a particular flight no and time
    public SeatAvailability findSA(Date fDate,String flightNo){
        System.out.println(flightNo);
        Query q = em.createQuery("SELECT o from SeatAvailability o WHERE o.schedule.flight.flightNo = :flightNo");
         q.setParameter("flightNo",flightNo);
         List<SeatAvailability> saList = q.getResultList();
         int size = saList.size();
         SeatAvailability sa = saList.get(0);
         for(int i =0; i<size; i++){
             if(saList.get(i).getSchedule().getStartDate().equals(fDate)){
                 sa= saList.get(i);
                 System.out.println("Date Check Successful!");
             }
                 
         }
         return sa;
    }
    
    /**
     *
     * @param flightNo
     * @param fDate
     * @return
     */
    
    // Generate the amount of seat available which includes overbooking based on demand forecast
    @Override
    public int[] getAvail(String flightNo,Date fDate){
        
        try{
         
         SeatAvailability sa = findSA(fDate, flightNo);
         int[] avail = new int[10];
         avail[0]= sa.getEconomySaverTotal()-sa.getEconomySaverBooked();
         avail[1]= sa.getEconomyBasicTotal() - sa.getEconomyBasicBooked();
         avail[2]= sa.getEconomyPremiumTotal() - sa.getEconomyPremiumBooked();
         avail[3] = sa.getBusinessTotal() - sa.getBusinessBooked();
         avail[4] = sa.getFirstClassTotal() - sa.getFirstClassBooked();
         avail[5] = (sa.getEconomySaverBooked() *100)/sa.getEconomySaverTotal();
         avail[6] = (sa.getEconomyBasicBooked() *100)/sa.getEconomyBasicTotal();
         avail[7] = (sa.getEconomyPremiumBooked() *100)/sa.getEconomyPremiumTotal();
         avail[8] = (sa.getBusinessBooked() *100)/sa.getBusinessTotal();
         avail[9] = (sa.getFirstClassBooked()*100)/sa.getFirstClassTotal();
         return avail;
        }
        catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error");
            return null;
        }
    }
    
    //Convert a string storing date and time to a date object
    public Date convertToDate(String flightDate, String flightTime){
        Date date= new Date();
        int[] seats = generateAvailability(40,20,10);
        int day = Integer.parseInt(flightDate.substring(0,2));
        int month = Integer.parseInt(flightDate.substring(2,4));
        month= month -1;
        int year = Integer.parseInt(flightDate.substring(4));
        int hour = Integer.parseInt(flightTime.substring(0,2));
        int min = Integer.parseInt(flightTime.substring(2));
        Calendar cflightDate = Calendar.getInstance();
        cflightDate.set(year,month,day,hour,min,0);
        date= cflightDate.getTime();
        return date;
    }
    
    // Get the base price of a particular flight number
    public double getBasePrice(String flightNo){
        Flight flight = em.find(Flight.class, flightNo);
        double baseprice= flight.getBasicFare();
        return baseprice;
    }
    
    // Get the price of a ticket of a specific flight and service type
    public String getPrice(String flightNo,Date fDate, String serviceClass, int realSold){
        int pricePercent=100;
        Date current = new Date();
        System.out.println("Percent Sold: "+ realSold);
        try{
            Query q = em.createQuery("SELECT o from BookingClass o WHERE o.serviceClass = :serviceClass");
            q.setParameter("serviceClass", serviceClass);
            List<BookingClass> classList = q.getResultList();
            int size = classList.size();
            long days = (fDate.getTime()-current.getTime())/ 1000 / 60 / 60 / 24;
            System.out.println("Service Type: "+ serviceClass + " " + days);
            List<BookingClass> updatedList = new ArrayList();
            
            for(int i=0;i<size;i++){
                BookingClass b = classList.get(i);
                int daysOut = b.getAdvancedSales();
                if (days<=daysOut){
                    updatedList.add(classList.get(i));
                    System.out.println("Fare Class qualified: " + b.getClasscode());
                }
            }

            size = updatedList.size();
            
            BookingClass c = updatedList.get(0);
            for(int i=0;i<size;i++){
                BookingClass b = updatedList.get(i);
                int daysOut = b.getAdvancedSales();
                int sold = b.getPercentSold();
                if (b.getPricePercent()<= c.getPricePercent()){
                    c=b;
                }
            }
            
            System.out.println("Lowest Fare Class: " + c.getClasscode());
            for(int i=0;i<size;i++){
                BookingClass b = updatedList.get(i);
                int daysOut = b.getAdvancedSales();
                int sold = b.getPercentSold();
                if (realSold >= sold){
                    c=b;
                    System.out.println("Higher Class Triggered ");
                }
            }
            
            double doubleprice = (c.getPricePercent() * getBasePrice(flightNo))/100;
            String price= Double.toString(doubleprice);
            System.out.println("Price: "+ price);
            System.out.println("Final Booking Class:" + c.getClasscode());
            return price;
        }
       catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" );
            return "100";
        }
        
    }

    
    
    
}
