/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import APS.Entity.Schedule;
import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface PassengerBookingSessionBeanLocal {
    
    public boolean isPassengerAFrequentFlyer(long customerId);
    public Customer getCustomerByCustomerId (long customerId);
    
    //initialize PNR, baggageList, reduce seatAvailablity --merge seatAvailablility        
    public Booking createBooking(double price, SeatAvailability seatAvail, String flightNo, Date flightDate, String bookingStatus, String classCode, String serviceType, String travellerTitle, String travellerFirstName, String travellerLastName, String passportNumber, String nationality, long customerId, boolean IsChild, boolean boughtInsurance, double insuranceFare, String foodSelection); 
    //initialize Bookings list
    public PNR createPNR (int noOfTravellers, String email, String contactNo, String pnrStatus, Double totalPrice, Date dateOfBooking, String bookingAvenue);
    public String generatePNRNumber ();
    //associate customer with PNR; pnr with bookings; booking with PNR; persist all
    public void persistBookingAndPNR (PNR pnr, List<Booking> bookings, Customer primaryCustomer);
    //increase SeatAvailablity --merge, changeBookingStatus to Cancelled for all bookings - merge booking, remove pnr from customer, Delete PNR, (Check deleteChecklistItem
    public void deletePNR (PNR pnr, Customer primaryCustomer);
    public List<Schedule> getDepartureSchedules (List<Schedule> selectedSchedules, boolean isReturnDateSet);
    //call only when return schedules is set
    public List<Schedule> getReturnSchedules (List<Schedule> selectedSchedules);
    
    
    
    public List<Customer> getAllCustomers();
    
}
