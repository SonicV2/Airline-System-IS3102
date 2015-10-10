/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import Inventory.Entity.BookingClass;
import Inventory.Entity.SeatAvailability;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface BookingSessionBeanLocal {
    // Create a booking
     public void addBooking(double price, String serviceType, 
            SeatAvailability seatAvail);
     public void bookSeats(String flightNo);
}
