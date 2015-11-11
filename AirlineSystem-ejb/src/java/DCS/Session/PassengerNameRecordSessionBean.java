/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import Distribution.Entity.Customer;

import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
import Inventory.Entity.BookingClass;
import Inventory.Entity.SeatAvailability;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class PassengerNameRecordSessionBean implements PassengerNameRecordSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    
    //get PNR use pnrNumber
    @Override
    public PNR getPNR(String pnrNumber) {
        Query q = em.createQuery("SELECT p FROM PNR p");
        List<PNR> pnrs = q.getResultList();

        for (PNR p : pnrs) {
            if (p.getPnrID().equals(pnrNumber)) {
                return p;
            }
        }
        return null;
    }

    // Get passenger booking
    @Override
    public List<Booking> getBooking(String pnrNumber, String passportNumber) {
        PNR pnr = getPNR(pnrNumber);
        List<Booking> bookings = new ArrayList<Booking>();

        List<Booking> results = new ArrayList<Booking>();
        if (pnr == null) {
            return results;
        } else {
            bookings = pnr.getBookings();
            for (Booking b : bookings) {
                if (b.getPassportNumber().equals(passportNumber)) {
                    results.add(b);
                }
            }
            return results;
        }
    }

    // Get if there is available schedule for checkin
    @Override
    public Schedule getCheckinSchedule(String pnrNumber, String passportNumber) {
        List<Booking> booking = getBooking(pnrNumber, passportNumber);
        Date date = new Date();

        if (booking == null) {
            return null;
        } else {
            SeatAvailability sa = booking.get(0).getSeatAvail();
            if (sa == null) {
                return null;
            } else {
                Schedule sch = sa.getSchedule();
                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sch.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                
//                if (checkTime(formattedDate1, formattedDate2) > 120) { // comment during testing
                    return sch;
//                } else {
//                    return null;
//                }
            }
        }
    }
    
    // Get checkin flight number
    @Override
    public Flight getCheckinFlight(String pnrNumber, String passportNumber){
        Schedule sch = getCheckinSchedule(pnrNumber, passportNumber);
        if(sch == null){
            return null;
        }else{
        
            Flight flight = sch.getFlight();
            return flight;
        }
        
    }
    
    @Override
    public String retrieveClass(String classcode){
        Query q = em.createQuery("SELECT b FROM BookingClass b");
        List<BookingClass> bc = q.getResultList();
        
        for(BookingClass b: bc){
            if(b.getClasscode().equals(classcode)){
                return b.getServiceClass();
            }
        }
        return "null";
    }
    
    @Override
    public void changeClass(Booking booking, String classcode){
        booking.setClassCode(classcode);
        em.merge(booking);
    }

    @Override
    public void changeServiceClass(Booking booking, String sClass){
        booking.setServiceType(sClass);
        em.merge(booking);
    }
    
    
    @Override
    public String retrieveUpgradeCosts(Booking booking){
        String field = booking.getField();
        
        if(field == null){
            return "0.0";
        }
        else if(field.contains("paid upgrade")){
            return (field.substring(12)); // get paid upgrade180 --> 180
        }
        return "0.0";
        
    }
    
    
    @Override
    public void changePNRStatus(Booking booking, String pnrID){
        Query q = em.createQuery("SELECT p FROM PNR p");
        List<PNR> pnrs = q.getResultList();
        for(PNR p: pnrs){
            if(p.getPnrID().equals(pnrID)){
                p.setPnrStatus("Flown");
                em.merge(p);
            }
        }
        booking.setBookingStatus("checkin");
        em.merge(booking);
    
    }
    
    
    
    @Override
    public void changeExistBooking(Booking booking, String scheduleID){
        Query q = em.createQuery("SELECT s FROM Schedule s");
        List<Schedule> schedules = q.getResultList();
        
        for(Schedule s : schedules){
            if(s.getScheduleId().toString().equals(scheduleID)){
                booking.setBookingStatus("Reschedule");
                booking.setSeatNumber("N.A");
                booking.setFlightNo(s.getFlight().getFlightNo());
                booking.setFlightDate(s.getStartDate());
                em.merge(booking);
              }
        }
    
    }
    
   
    @Override
    public void addMiles(Booking booking){
        System.out.println("-------------->Booking"+booking.getId());
        
        String custId = booking.getCustomerId() + "";
        if(!custId.equals("0")){
            int millage = getBookingMillage(booking.getClassCode());
            double distance = calculateDistance(booking);
            double result = distance*millage / 100.0;
            
            Customer c = retrieveCustomerByID(booking.getCustomerId());
            System.out.println("-------------->Customer"+c.getEmail());
            
            if(c==null){
                
                
            }else{
                int points = c.getMileagePoints();
                
                points += (int) result;
                
                c.setMileagePoints(points);
                em.merge(c);
            }
        }
    
    }
    
    public Customer retrieveCustomerByID(long customerId){
        Query q = em.createQuery("SELECT c FROM Customer c");
        List<Customer> customers = q.getResultList();
        
        for(Customer c: customers){
            if(c.getId().toString().equals((customerId+""))){
                return c;
            }
        }
        return null;
    }
    
    public int getBookingMillage(String classcode){
        Query q = em.createQuery("SELECT b FROM BookingClass b");
        List<BookingClass> bc = q.getResultList();
        
        for(BookingClass b: bc){
            if(b.getClasscode().equals(classcode)){
                return b.getMillageAccru();
            }
        }
        return 0;
    
    }
    
    public double calculateDistance(Booking booking){
        Schedule schedule = booking.getSeatAvail().getSchedule();
        return (schedule.getFlight().getRoute().getDistance());
    }
    
    
    public long checkTime(String time1, String time2) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date1;
        Date date2;
        try {
            date1 = formatter.parse(time1);
            date2 = formatter.parse(time2);
            long diff = (date2.getTime() - date1.getTime());
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long result = diffDays * 24 * 60 + diffHours * 60 + diffMinutes;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}


