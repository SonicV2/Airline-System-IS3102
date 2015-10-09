/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import APS.Entity.Flight;
import APS.Entity.Location;
import APS.Entity.Route;
import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.text.SimpleDateFormat;
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
 * @author parthasarthygupta
 */
@Stateless
public class DistributionSessionBean implements DistributionSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public boolean isIATAAHub(String iata) {
        if (iata.equalsIgnoreCase("SIN") || iata.equalsIgnoreCase("FRA") || iata.equals("NRT")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean existsDirectFlight(String originIata, String destinationIata) {
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
        for (Flight eachFlight : flights) {
            Route eachRoute = eachFlight.getRoute();
            if (eachRoute.getOriginIATA().equals(originIata) && eachRoute.getDestinationIATA().equals(destinationIata)) {
                return true;
            }
        }
        return false;
    }

    public List<Schedule> retrieveDirectFlightsForDate(String originIata, String destinationIata, Date startDate, String serviceType, int adults, int children) {
        System.out.println("StartDate " + startDate);
        System.out.println("ORIGIN: " + originIata);
        System.out.println("DESTINATION: " + destinationIata);
        System.out.println("service type: " + serviceType);
        System.out.println("adults: " + adults);
        System.out.println("dhildren: " + children);
        
        
        List<Flight> flightsForRoute = new ArrayList();
        flightsForRoute = retrieveFlightsForRoute(originIata, destinationIata);
        List<Schedule> schedulesForFlights = new ArrayList<Schedule>();
        List<Schedule> schedulesWithSeatsAvailable = new ArrayList<Schedule>();

        for (Flight eachFlight : flightsForRoute) {
            System.out.println("Flight for route: " + eachFlight.getFlightNo());
            if (getScheduleForFlightForDate(eachFlight, startDate) != null) {
                System.out.println("Get schedule for flight for date " + getScheduleForFlightForDate(eachFlight, startDate));
                schedulesForFlights.add(getScheduleForFlightForDate(eachFlight, startDate));
            }
        }
        System.out.println("SchedulesForFlight " + schedulesForFlights.size());
        if (schedulesForFlights.size() > 0) {
            for (Schedule eachScheduleForFlight : schedulesForFlights) {

                if (seatsAvailableForSchedule(eachScheduleForFlight, serviceType, (adults + children))) {
                    schedulesWithSeatsAvailable.add(eachScheduleForFlight);
                }
            }
        }
        System.out.println("#### Schedules with seatsAvailable size" + schedulesWithSeatsAvailable.size());
        return schedulesWithSeatsAvailable;
    }

    @Override
    public Schedule getScheduleForFlightForDate(Flight flight, Date startDate) {
        List<Schedule> schedulesForFlight = flight.getSchedule();
        String startDateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(startDate);

        for (Schedule eachScheduleForFlight : schedulesForFlight) {
            Date eachScheduleStartDateInLocalTime = convertTimeZone(eachScheduleForFlight.getStartDate(), getSingaporeTimeZone(), getTimeZoneFromIata(eachScheduleForFlight.getFlight().getRoute().getOriginIATA()));
            String eachScheduleStartDate = new SimpleDateFormat("dd/MM/yyyy").format(eachScheduleStartDateInLocalTime);

            if (eachScheduleStartDate.equals(startDateFormatted)) {
                return eachScheduleForFlight;
            }
        }
        return null;
    }

    @Override
    public List<Flight> retrieveFlightsForRoute(String originIata, String destinationIata) {
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
        for (Flight eachFlight : flights) {
            Route eachRoute = eachFlight.getRoute();
            if (eachRoute.getOriginIATA().equals(originIata) && eachRoute.getDestinationIATA().equals(destinationIata)) {
                flightsForRoute.add(eachFlight);
            }
        }
        return flightsForRoute;
    }

    @Override
    public boolean seatsAvailableForSchedule(Schedule schedule, String serviceType, int noOfSeats) {

        SeatAvailability seatAvailability = schedule.getSeatAvailability();
        int seatsAvailable = 0;
        if (serviceType.equalsIgnoreCase("Economy Saver")) {
            seatsAvailable = seatAvailability.getEconomySaverTotal() - seatAvailability.getEconomySaverBooked();
        } else if (serviceType.equalsIgnoreCase("Economy Basic")) {
            seatsAvailable = seatAvailability.getEconomyBasicTotal() - seatAvailability.getEconomyBasicBooked();
        } else if (serviceType.equalsIgnoreCase("Economy Premium")) {
            seatsAvailable = seatAvailability.getEconomyPremiumTotal() - seatAvailability.getEconomyPremiumBooked();
        } else if (serviceType.equalsIgnoreCase("Business")) {
            seatsAvailable = seatAvailability.getBusinessTotal() - seatAvailability.getBusinessBooked();
        } else if (serviceType.equalsIgnoreCase("First Class")) {
            seatsAvailable = seatAvailability.getFirstClassTotal() - seatAvailability.getFirstClassBooked();
        }
        System.out.println("Distribution session bean: Seats available: " + seatsAvailable);
        System.out.println("Distribution session bean: noOfSeats: " + noOfSeats);
        if (seatsAvailable >= noOfSeats) {
            return true;
        }
        return false;

    }

    @Override
    public List<Route> getAllRoutes() {
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
    public Location getLocationFromIata(String Iata) {
        List<Location> allLocations = new ArrayList<Location>();
        try {

            Query q = em.createQuery("SELECT a FROM Location a");

            List<Location> results = q.getResultList();
            if (!results.isEmpty()) {
                allLocations = results;

            } else {
                allLocations = null;
                System.out.println("No locations Available!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        for (Location eachLocation : allLocations) {
            if (eachLocation.getIATA().equals(Iata)) {
                return eachLocation;
            }
        }
        return allLocations.get(0);
    }

    @Override
    public List<String> getHubIatasFromOrigin(String originIATA) {
        List<Flight> flights = new ArrayList<Flight>();
        flights = getAllFlights();
        List<String> hubs = new ArrayList<String>();

        for (Flight eachFlight : flights) {
            if (eachFlight.getRoute().getOriginIATA().equals(originIATA) && isIATAAHub(eachFlight.getRoute().getDestinationIATA())) {
                hubs.add(eachFlight.getRoute().getDestinationIATA());
            }
        }
        return hubs;
    }

    @Override
    public List<String> getTransitHubs(List<String> hubsFromOrigin, String destinationIata) {

        List<Flight> flights = new ArrayList<Flight>();
        flights = getAllFlights();

        List<String> transitHubs = new ArrayList<String>();

        for (String eachHub : hubsFromOrigin) {
            for (Flight eachFlight : flights) {
                if (!transitHubs.contains(eachHub) && eachFlight.getRoute().getDestinationIATA().equalsIgnoreCase(destinationIata) && eachFlight.getRoute().getOriginIATA().equalsIgnoreCase(eachHub)) {
                    transitHubs.add(eachHub);
                }
            }

        }
        return transitHubs;
    }

    @Override
    public List<Schedule> retrieveOneStopFlightSchedules(List<Schedule> legOne, List<Schedule> legTwo) {
        List<Schedule> oneStopSchedules = new ArrayList<Schedule>();
        final long MIN_TRANSIT_TIME = 3600000; //1 hour in milliseconds
        final long MAX_TRANSIT_TIME = 36000000; //10 hours in milliseconds 
        for (Schedule eachLegOneSchedule : legOne) {
            for (Schedule eachLegTwoSchedule : legTwo) {
                if (((eachLegTwoSchedule.getStartDate().getTime() - eachLegOneSchedule.getEndDate().getTime() >= MIN_TRANSIT_TIME) && (eachLegTwoSchedule.getStartDate().getTime() - eachLegOneSchedule.getEndDate().getTime() <= MAX_TRANSIT_TIME)) && eachLegOneSchedule.getFlight().getRoute().getDestinationIATA().equals(eachLegTwoSchedule.getFlight().getRoute().getOriginIATA())) {
                    oneStopSchedules.add(eachLegOneSchedule);
                    oneStopSchedules.add(eachLegTwoSchedule);
                }
            }
        }
        return oneStopSchedules;

    }

    @Override
    public Date addDaysToDate(Date date, int no) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, no);
        return c.getTime();
    }

    @Override
    public Date convertTimeZone(Date date, TimeZone fromTZ, TimeZone toTZ) {
        long fromTZDst = 0;
        if (fromTZ.inDaylightTime(date)) {
            fromTZDst = fromTZ.getDSTSavings();
        }

        long fromTZOffset = fromTZ.getRawOffset() + fromTZDst;

        long toTZDst = 0;
        if (toTZ.inDaylightTime(date)) {
            toTZDst = toTZ.getDSTSavings();
        }
        long toTZOffset = toTZ.getRawOffset() + toTZDst;

        return new java.util.Date(date.getTime() + (toTZOffset - fromTZOffset));
    }

    @Override
    public String getLayoverTime(Schedule legOne, Schedule legTwo) {
        int hours, mins;
        long layoverInMilliseconds = legTwo.getStartDate().getTime() - legOne.getEndDate().getTime();

        hours = (int) (layoverInMilliseconds / (60 * 60 * 1000));
        layoverInMilliseconds %= 60 * 60 * 1000;
        mins = (int) (layoverInMilliseconds / (60 * 1000));
        return (hours + " hours " + mins + " mins ");
    }

    @Override
    public String getTotalDurationForOneStop(Schedule legOne, Schedule legTwo) {
        int hours, mins;
        long totalDurationInMilliseconds = legTwo.getEndDate().getTime() - legOne.getStartDate().getTime();

        hours = (int) (totalDurationInMilliseconds / (60 * 60 * 1000));
        totalDurationInMilliseconds %= 60 * 60 * 1000;
        mins = (int) (totalDurationInMilliseconds / (60 * 1000));
        return (hours + " hours " + mins + " mins ");

    }

    @Override
    public String getTotalDurationForDirect(Schedule schedule) {
        int hours, mins;
        long totalDurationInMilliseconds = schedule.getEndDate().getTime() - schedule.getStartDate().getTime();

        hours = (int) (totalDurationInMilliseconds / (60 * 60 * 1000));
        totalDurationInMilliseconds %= 60 * 60 * 1000;
        mins = (int) (totalDurationInMilliseconds / (60 * 1000));
        return (hours + " hours " + mins + " mins ");
    }

    public TimeZone getTimeZoneFromIata(String iata) {
        Location location = getLocationFromIata(iata);
        String zone = location.getTz();
        TimeZone timeZone = TimeZone.getTimeZone(zone);
        return timeZone;
    }

    public TimeZone getSingaporeTimeZone() {
        return (TimeZone.getTimeZone("Asia/Singapore"));
    }

    public boolean existsSchedule(String originIata, String destinationIata, Date date, String serviceType, int adults, int children) {

        if (existsOneStopFlight(originIata, destinationIata, date, serviceType, adults, children) == false && retrieveDirectFlightsForDate(originIata, destinationIata, date, serviceType, adults, children).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existsOneStopFlight(String originIATA, String destinationIATA, Date date, String serviceType, int adults, int children) {

        List<String> transitHubs = new ArrayList();
        transitHubs = getTransitHubs(getHubIatasFromOrigin(originIATA), destinationIATA);

        List<Schedule> legOneSchedules = new ArrayList();
        List<Schedule> legTwoSchedules = new ArrayList();
        List<Schedule> oneStopFlightSchedules = new ArrayList();

        for (int i = 0; i < transitHubs.size(); i++) {
            legOneSchedules = addSchedulesToList(legOneSchedules, retrieveDirectFlightsForDate(originIATA, transitHubs.get(i), date, serviceType, adults, children));
            legTwoSchedules = addSchedulesToList(legTwoSchedules, retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, addDaysToDate(date, 1), serviceType, adults, children));
            legTwoSchedules = addSchedulesToList(legTwoSchedules, retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, date, serviceType, adults, children));
        }

        oneStopFlightSchedules = retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules);
        if (oneStopFlightSchedules.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<Schedule> addSchedulesToList(List<Schedule> originalSchedules, List<Schedule> schedulesToAdd) {
        for (Schedule eachScheduleToAdd : schedulesToAdd) {
            originalSchedules.add(eachScheduleToAdd);
        }
        return originalSchedules;
    }

    public List<Flight> getAllFlights() {
        List<Flight> allFlights = new ArrayList<Flight>();

        try {
            Query q = em.createQuery("SELECT a from Flight a");

            List<Flight> results = q.getResultList();
            if (!results.isEmpty()) {

                allFlights = results;

            } else {
                allFlights = null;
                System.out.println("no flight!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allFlights;
    }

}
