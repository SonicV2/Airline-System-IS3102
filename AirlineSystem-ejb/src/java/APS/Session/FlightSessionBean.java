package APS.Session;

import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yanlong
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private Flight flight;
    private Schedule schedule;
    private Route route;
    private AircraftType aircraftType;
    private List<Schedule> schedules;

    @Override
    public void addFlight(String flightNo, String flightDays, Double basicFare, Date startDateTime, Long routeId) {
        flight = new Flight();
        route = getRoute(routeId);

        //Create link with route
        flight.createFlight(flightNo, flightDays, basicFare, startDateTime);
        flight.setRoute(route);
        route.getFlights().add(flight);
        
        flight.setAircraftType(aircraftType);
        flight.setSchedule(schedules);
        em.persist(flight);
    }

    @Override
    public void deleteFlight(String flightNo) {
        flight = getFlight(flightNo);
        em.remove(flight);
        em.flush();
    }
    
    public void deleteSchedule(Long scheduleId) {
        schedule = getSchedule(scheduleId);
        em.remove(schedule);
        em.flush();
    }
    
    @Override
    public Schedule getSchedule(Long scheduleId) {
        schedule = new Schedule();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule " + "AS a WHERE a.scheduleId=:scheduleId");
            q.setParameter("scheduleId", scheduleId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                schedule = (Schedule) results.get(0);

            } else {
                flight = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return schedule;
    }

    @Override
    public Flight getFlight(String flightNo) {
        flight = new Flight();
        try {

            Query q = em.createQuery("SELECT a FROM Flight " + "AS a WHERE a.flightNo=:flightNo");
            q.setParameter("flightNo", flightNo);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                flight = (Flight) results.get(0);

            } else {
                flight = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return flight;
    }

    @Override
    public List<Flight> getFlights() {
        List<Flight> flights = new ArrayList<Flight>();
        try {

            Query q = em.createQuery("SELECT a FROM Flight a");

            List<Flight> results = q.getResultList();
            if (!results.isEmpty()) {
                flights = results;

            } else {
                flights = null;
                System.out.println("No Flights Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return flights;
    }
    
    @Override
    public Route getRoute(Long id) {
        route = new Route();
        try {

            Query q = em.createQuery("SELECT a FROM Route " + "AS a WHERE a.routeId=:routeId");
            q.setParameter("routeId", id);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                route = (Route) results.get(0);

            } else {
                route = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return route;
    }
    
    public List<Flight> retrieveFlights(){
        List<Flight> allFlights = new ArrayList<Flight>();
        
        try{
            Query q = em.createQuery("SELECT a from Flight a");
            
            List<Flight> results = q.getResultList();
            if (!results.isEmpty()){
                
                allFlights = results;
                
            }else
            {
                allFlights = null;
                System.out.println("no flight!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allFlights;
    }

}
