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

/**
 *
 * @author YiQuan
 */
@Stateless
public class BookingSessionBean implements BookingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    private String serviceType;
    
    
    
    public void addBooking(double price, String serviceType, 
            SeatAvailability seatAvail){
        Booking booking = new Booking();
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
        
        em.merge(seatAvail);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
