/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

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
 * @author parthasarthygupta
 */
@Stateless
public class DistributionSessionBean implements DistributionSessionBeanLocal {
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    @Override
     public boolean isIATAAHub (String iata){
         if (iata.equalsIgnoreCase("SIN") || iata.equalsIgnoreCase ("FRA") || iata.equals("NRT"))
             return true;
         else
             return false;
     }
     
     @Override
     public boolean existsDirectFlight (String originIata, String destinationIata){
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
        for (Flight eachFlight: flights){
            Route eachRoute = eachFlight.getRoute();
            if (eachRoute.getOriginIATA().equals(originIata) && eachRoute.getDestinationIATA().equals(destinationIata)){
                return true;
            }                
        }
        return false;
     }
     
public List<Schedule> retrieveDirectFlightsForDate (String originIata, String destinationIata, Date startDate, String serviceType, int adults, int children){
    List <Flight> flightsForRoute = new ArrayList();
    flightsForRoute = retrieveFlightsForRoute(originIata, destinationIata);
    List<Schedule> schedulesForFlights = new ArrayList<Schedule>();
    List <Schedule> schedulesWithSeatsAvailable = new ArrayList<Schedule>();
        
        for (Flight eachFlight: flightsForRoute){
                schedulesForFlights.add(getScheduleForFlightForDate(eachFlight, startDate));
            }                
        for (Schedule eachScheduleForFlight: schedulesForFlights){
            if (seatsAvailableForSchedule(eachScheduleForFlight, serviceType, (adults+children) )){
                schedulesWithSeatsAvailable.add(eachScheduleForFlight);
            }                
        }
        return schedulesWithSeatsAvailable;   
    }

    @Override
    public Schedule getScheduleForFlightForDate (Flight flight, Date startDate){
        List<Schedule> schedulesForFlight = flight.getSchedule();
        for (Schedule eachScheduleForFlight: schedulesForFlight){
            if (eachScheduleForFlight.getStartDate().equals(startDate))
                return eachScheduleForFlight;
        }
        return null;            
    }
    
    @Override 
    public List<Flight> retrieveFlightsForRoute (String originIata, String destinationIata){
        List<Flight> flights = new ArrayList<Flight>();
        List<Flight> flightsForRoute = new ArrayList();
        
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
        for (Flight eachFlight: flights){
            Route eachRoute = eachFlight.getRoute();
            if (eachRoute.getOriginIATA().equals(originIata) && eachRoute.getDestinationIATA().equals(destinationIata)){
                flightsForRoute.add(eachFlight);
            }                
        }
        return flightsForRoute;
    }
    
    @Override
    public boolean seatsAvailableForSchedule (Schedule schedule, String serviceType, int noOfSeats){
        SeatAvailability seatAvailability = schedule.getSeatAvailability();
        int seatsAvailable=0;
        if (serviceType.equalsIgnoreCase("Economy Saver")){
            seatsAvailable = seatAvailability.getEconomySaverTotal()-seatAvailability.getEconomySaverBooked();
        }
        else if (serviceType.equalsIgnoreCase("Economy Basic")){
        seatsAvailable = seatAvailability.getEconomyBasicTotal()-seatAvailability.getEconomyBasicBooked();
    }
        else if (serviceType.equalsIgnoreCase("Economy Premium")){
        seatsAvailable = seatAvailability.getEconomyPremiumTotal()-seatAvailability.getEconomyPremiumBooked();
    }
    else if (serviceType.equalsIgnoreCase("Business")){
        seatsAvailable = seatAvailability.getBusinessTotal()-seatAvailability.getBusinessBooked();
    }
    else if (serviceType.equalsIgnoreCase("First Class")){
        seatsAvailable = seatAvailability.getFirstClassTotal()-seatAvailability.getFirstClassBooked();
    }
    if (seatsAvailable>=noOfSeats)
        return true;
    return false;
    
    }
    
    @Override
    public List<Route> getAllRoutes(){
       List<Route> routes = new ArrayList<Route>();
       try {

            Query q = em.createQuery("SELECT a FROM Route a");

            List<Route> results = q.getResultList();
            if (!results.isEmpty()) {
                routes = results;

            } else {
                routes = null;
                System.out.println("No Routes Available!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       return routes;
    }
    
    
    @Override
    public List<String> getHubIatasFromOrigin (String originIATA){
        List<Route> routes = new ArrayList<Route>();
        routes = getAllRoutes();
        List <String> hubs = new ArrayList<String>();
        
        
        for (Route eachRoute: routes){
            if (eachRoute.getOriginIATA().equals(originIATA) && isIATAAHub(eachRoute.getDestinationIATA()))
                hubs.add(eachRoute.getDestinationIATA());
        }
        return hubs;
    }
    
    @Override
    public List<String> getTransitHubs (List<String> hubsFromOrigin, String destinationIata){
        List<Route> routes = new ArrayList<Route>();
        routes = getAllRoutes();
        List <String> transitHubs = new ArrayList<String>();
        for (String eachHub: hubsFromOrigin){
            for (Route eachRoute: routes){
                if (eachRoute.getDestinationIATA().equals(destinationIata)&& eachRoute.getOriginIATA().equals(eachHub)){
                    transitHubs.add(eachHub);
                }
            }
        
        }
        return transitHubs;
    }     
    
    
}
