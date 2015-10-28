/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;

import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
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
