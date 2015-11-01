package APS.Session;

import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
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
    private List<Flight> flights;
    private Schedule schedule;
    private Route route;
    private List<Route> routes;
    private AircraftType aircraftType;
    private List<Schedule> schedules;
    private SeatAvailability seatAvail;
    //Add new flight entity
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

    //Delete an existing flight entity
    @Override
    public void deleteFlight(String flightNo) {
        flight = getFlight(flightNo);
        em.remove(flight);
        em.flush();
    }

    //Delete an existing schedule entity
    @Override
    public void deleteSchedule(Long scheduleId) {
        schedule = getSchedule(scheduleId);

        seatAvail = schedule.getSeatAvailability();
        seatAvail.setSchedule(null);

        schedule.setSeatAvailability(null);
        
        em.remove(seatAvail);
        em.remove(schedule);
        em.flush();
    }

    //Get a specific schedule with schedule id
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

    //Get a specific flgith with flight number
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

    //Get a specific route with route id
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

    //Retrieve all the existing flights
    @Override
    public List<Flight> retrieveFlights() {
        flights = new ArrayList<Flight>();

        try {
            Query q = em.createQuery("SELECT a from Flight a");

            List<Flight> results = q.getResultList();
            if (!results.isEmpty()) {

                flights = results;

            } else {
                flights = null;
                System.out.println("no flight!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return flights;
    }

    //Retrieve all the existing routes that the flights fly
    @Override
    public List<Route> retrieveFlightRoutes() {
        routes = new ArrayList<Route>();
        List<Route> routesWithFlights = new ArrayList<Route>();

        try {
            Query q = em.createQuery("SELECT a from Route a");

            List<Route> results = q.getResultList();
            if (!results.isEmpty()) {

                routes = results;

            } else {
                routes = null;
                System.out.println("no Route!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        for (int i = 0; i < routes.size(); i++) {
            if (!routes.get(i).getFlights().isEmpty()) {
                routesWithFlights.add(routes.get(i));
            }
        }

        return routesWithFlights;
    }
    
    //Retrieve all the existing routes that the flights fly
    @Override
    public List<Long> retrieveFlightRouteIds() {
        routes = new ArrayList<Route>();
        List<Long> routeIds = new ArrayList<Long>();

        try {
            Query q = em.createQuery("SELECT a from Route a");

            List<Route> results = q.getResultList();
            if (!results.isEmpty()) {

                routes = results;

            } else {
                routes = null;
                System.out.println("no Route!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        for (int i = 0; i < routes.size(); i++) {
            if (!routes.get(i).getFlights().isEmpty()) {
                routeIds.add(routes.get(i).getRouteId());
            }
        }

        return routeIds;
    }
}
