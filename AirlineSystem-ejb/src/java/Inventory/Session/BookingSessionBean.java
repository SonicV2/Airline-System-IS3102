/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import Inventory.Entity.BookingClass;
import Inventory.Entity.SeatAvailability;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Inventory.Entity.Booking;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import java.util.Random;

/**
 *
 * @author YiQuan
 */
@Stateless
public class BookingSessionBean implements BookingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    private String serviceType;
    
    
    public void bookSeats(String flightNo){
        Query q = em.createQuery("SELECT o from SeatAvailability o WHERE o.schedule.flight.flightNo = :flightNo");
         q.setParameter("flightNo",flightNo);
         
         List<SeatAvailability> saList = q.getResultList();
         Calendar cal = Calendar.getInstance();
         int size = saList.size();
         for(int i=0; i<size; i++){
             Date fTime = saList.get(i).getSchedule().getStartDate();
             Calendar flightCal = Calendar.getInstance();
             flightCal.setTime(fTime);
             if(flightCal.compareTo(cal)<0)
                 pseudoBook(saList.get(i));             
         }
    }
    
    
    
    public void pseudoBook(SeatAvailability sa){
        Random random = new Random();
        int economySaverBooked = sa.getEconomySaverTotal()/2 + random.nextInt(sa.getEconomySaverTotal()/2 );
        sa.setEconomySaverBooked(economySaverBooked);
        int economyBasicBooked = sa.
        
    }
    
    
    // Create a booking
    public void addBooking(double price, String serviceType, 
            SeatAvailability seatAvail){
        Booking booking = new Booking();
        Long id = seatAvail.getId();
        seatAvail= em.find(SeatAvailability.class, id);
        booking.createBooking(price, serviceType, seatAvail);
        em.persist(booking);
        if(serviceType.equals("Economy Saver"))
            seatAvail.setEconomySaverBooked(seatAvail.getEconomySaverBooked()+1);
        if(serviceType.equals("Economy Basic")) 
            seatAvail.setEconomyBasicBooked(seatAvail.getEconomyBasicBooked()+1);
        if(serviceType.equals("Economy Premium"))
            seatAvail.setEconomyPremiumBooked(seatAvail.getEconomyPremiumBooked()+1);
        if(serviceType.equals("Business"))
            seatAvail.setBusinessBooked(seatAvail.getBusinessBooked()+1);
        if(serviceType.equals("First Class"))
            seatAvail.setFirstClassBooked(seatAvail.getFirstClassBooked()+1);  
       
       System.out.println(em.contains(seatAvail));
       em.flush();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
