/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import DCS.Entity.CheckInRecord;
import Inventory.Entity.Booking;
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
public class SummarySessionBean implements SummarySessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public Schedule retrieveSchedule(String flightNumber, Date date) {
        Flight flight = retrieveFlight(flightNumber);
        if (flight == null) {
            return null;
        }
        List<Schedule> schedules = flight.getSchedule();
        for (Schedule s : schedules) {
            String formattedYear = new SimpleDateFormat("MM/dd/yyyy").format(s.getStartDate());
            String formatted = new SimpleDateFormat("MM/dd/yyyy").format(date);
            
            System.out.println("FOrmatt: "+ formattedYear);
            
            if (formattedYear.equals(formatted)) {
                return s;
            }

        }
        return null;

    }

    @Override
    public List<CheckInRecord> retrieveRecords(List<Booking> bookings) {
        
        List<CheckInRecord> results = new ArrayList<CheckInRecord>();
        Query q = em.createQuery("SELECT c FROM CheckInRecord c");
        List<CheckInRecord> records = q.getResultList();
        
        for(CheckInRecord r : records){
            for(Booking b: bookings){
            
                if((r.getBookingID()+"").equals(b.getId().toString())){
                    results.add(r);
                }
            }
        
        }
        return results;
    }

    public Flight retrieveFlight(String flightNumber) {

        Query q = em.createQuery("SELECT f FROM Flight f");
        List<Flight> flights = q.getResultList();

        for (Flight f : flights) {
            if (f.getFlightNo().equals(flightNumber)) {
                return f;
            }
        }
        return null;
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
