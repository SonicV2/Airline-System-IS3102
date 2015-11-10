package APS.Session;

import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
public class FlightSessionBean implements FlightSessionBeanLocal, FlightSessionBeanRemote {

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
    public String addFlight(String flightNo, String flightDays, Double basicFare, Date startDateTime, Long routeId, boolean pastFlight) {
        if (pastFlight) {
            flight = getFlight(flightNo);
        } else {
            flight = new Flight();
        }
        route = getRoute(routeId);
        
        //Set up the startDateTime for storing
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        tmp.setTime(startDateTime);
        tmp.set(Calendar.SECOND, 0);

        //Create link with route
        flight.createFlight(flightNo, flightDays, basicFare, tmp.getTime());
        flight.setRoute(route);
        route.getFlights().add(flight);
        String id = "Airbus A380-800";
        aircraftType = em.find(AircraftType.class, id);
        flight.setAircraftType(aircraftType);
        flight.setSchedule(schedules);
        
         if (pastFlight) {
            em.merge(flight);
            em.flush();
        } else {
            em.persist(flight);
        }
         return "Flight Added";
    }

    //Delete or Archieve an existing flight entity depending on the situation
    @Override
    public String deleteFlight(String flightNo, boolean isArchive) {
        flight = getFlight(flightNo);
        Long archieveData;

        //search for Flight Num in Flight lists linked to the Route and remove the Flight
        List<Flight> temp = flight.getRoute().getFlights();
        temp.remove(flight);
        Route tmpRoute = flight.getRoute();
        tmpRoute.setFlights(temp);
        flight.setRoute(null);
        archieveData = tmpRoute.getRouteId();

        //search for Flight Num in Flight lists linked to the Aircraft Type and remove the Flight
        List<Flight> temp1 = flight.getAircraftType().getFlights();
        temp1.remove(flight);
        AircraftType tmpAC = flight.getAircraftType();
        tmpAC.setFlights(temp1);
        flight.setAircraftType(null);

        if (isArchive) {
            flight.setArchiveData(archieveData);
            em.merge(flight);
            em.flush();
        } else {
            em.remove(flight);
        }
        return "Flight Deleted";
    }

    //Delete schedule implementation when archiving flight
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
    
    @Override
    public void edit(Flight edited, Flight original){
        AircraftType editedAC = em.find(AircraftType.class, edited.getAircraftType().getId());
        AircraftType originalAC = em.find(AircraftType.class, original.getAircraftType().getId());
        
        List<Flight> tmp = originalAC.getFlights();
        tmp.remove(original);
        originalAC.setFlights(tmp);
        em.merge(originalAC);
        
        tmp = editedAC.getFlights();
        tmp.add(edited);
        editedAC.setFlights(tmp);
        em.merge(editedAC);
        
        Flight update = em.find(Flight.class, original.getFlightNo());
        update.setAircraftType(editedAC);
        em.merge(update);
        em.flush();
        
        AircraftType tt = em.find(AircraftType.class, editedAC.getId());
        System.out.println(tt.getFlights());
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

    //Retrieve all the flights in the database
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
    public List<Flight> retrieveActiveFlights() {
         flights = new ArrayList<Flight>();

        try {
            Query q = em.createQuery("SELECT a from Flight " + "AS a WHERE a.archiveData = null");

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

    private AircraftType getAircraftType(String aircraftTypeId) {

        AircraftType type1 = new AircraftType();
        try {

            Query q = em.createQuery("SELECT a FROM AircraftType " + "AS a WHERE a.id=:id");
            q.setParameter("id", aircraftTypeId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                type1 = (AircraftType) results.get(0);

            } else {
                type1 = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return type1;
    }
}
