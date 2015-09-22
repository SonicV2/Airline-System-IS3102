/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import FOS.Entity.Team;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import Inventory.Entity.SeatAvailability;
import Inventory.Session.RevenueManagementLocal;

/**
 *
 * @author Yanlong
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private RevenueManagementLocal rm;

    private Flight flight;
    private List<Flight> flights;
    private Route route;
    private AircraftType aircraftType;
    private List<AircraftType> aircraftTypes;
    private Schedule schedule;
    private List<Schedule> schedules;
    private Team team;
    private List<Aircraft> aircrafts;

    @Override
    public void scheduleFlights(String flightId) {
        flight = getFlight(flightId);
        aircraftType = new AircraftType();
        aircraftTypes = retrieveAircraftTypes();
        route = flight.getRoute();

        //Find the best Aircraft for the flight
        aircraftType = aircraftTypes.get(0);
        Double minFuel = aircraftTypes.get(0).getFuelCost();

        for (AircraftType aircraftType1 : aircraftTypes) {
            if ((route.getDistance() < (double) aircraftType1.getTravelRange() * 1.1) && (aircraftType1.getFuelCost() < minFuel)) {
                aircraftType = aircraftType1;
                minFuel = aircraftType1.getFuelCost();
            }
        }

        flight.setAircraftType(aircraftType);
        aircraftType.getFlights().add(flight);
        DecimalFormat df = new DecimalFormat("0.##");
        flight.setFlightDuration(Double.valueOf(df.format(route.getDistance() / (aircraftType.getSpeed() * 1062))));

        //Create link with Schedules
        Date startDateTime = flight.getStartDateTime();
        String flightDays = flight.getFlightDays();
        schedules = new ArrayList<Schedule>();

        //Forecast the last date of the flight in 6 months
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar endTime = Calendar.getInstance(tz);
        endTime.setTime(startDateTime);
        endTime.set(Calendar.SECOND, 0);
        endTime.add(Calendar.YEAR, 1);

        Calendar curr = Calendar.getInstance(tz);
        curr.setTime(startDateTime);
        curr.set(Calendar.SECOND, 0);
        Date counter = startDateTime;

        //Add a list schedule until 6 months later
        while (curr.before(endTime)) {
            schedule = new Schedule();
            int day = curr.get(Calendar.DAY_OF_WEEK);
            if (flightDays.charAt(day - 1) == '1') {
                Date flightEnd = calcEndTime(curr.getTime(), flight);
                schedule.createSchedule(curr.getTime(), flightEnd);
                schedule.setFlight(flight);
                schedule.setTeam(team);
                SeatAvailability sa = new SeatAvailability();
                schedule.setSeatAvailability(sa);
                int economy = flight.getAircraftType().getEconomySeats();
                int business = flight.getAircraftType().getBusinessSeats();
                int firstClass = flight.getAircraftType().getFirstSeats();
                int[] seats = rm.generateAvailability(economy, business, firstClass);
                em.persist(schedule);
                sa.createSeatAvail(schedule, seats);
                em.persist(sa);
                schedules.add(schedule);
            }
            curr.setTime(counter);
            curr.add(Calendar.DATE, 1);
            counter = curr.getTime();
        }
        flight.setSchedule(schedules);
        em.persist(flight);
    }

    @Override
    public void rotateFlights() {
        aircrafts = retrieveAircrafts();
        schedules = getSchedules();
        aircraftType = new AircraftType();
        List<Schedule> curr;
        List<Schedule> result;
        Schedule earliestSchedule = new Schedule();
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar currTime = Calendar.getInstance(tz);
        Calendar tmp = Calendar.getInstance(tz);

        //Create comparator for sorting of Schedules according to starting time
        Comparator<Schedule> comparator = new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                int result = o1.getStartDate().compareTo(o2.getStartDate());
                if (result == 0) {
                    return o1.getStartDate().before(o2.getStartDate()) ? -1 : 1;
                } else {
                    return result;
                }
            }
        };

        //Assign flights to schedules until all schedules are assigned
        while (isAllAssigned(schedules)) {
            for (Aircraft aircraft : aircrafts) {
                Route incoming = new Route();
                Route currRoute = new Route();
                flights = new ArrayList<Flight>();
                curr = new ArrayList<Schedule>();
                result = new ArrayList<Schedule>();
                aircraftType = aircraft.getAircraftType();

                //Add flights according to hubs
                for (int i = 0; i < aircraftType.getFlights().size(); i++) {
                    currRoute = aircraftType.getFlights().get(i).getRoute();
                    if (currRoute.getOriginIATA().equals(aircraft.getHub()) || currRoute.getDestinationIATA().equals(aircraft.getHub())) {
                        flights.add(aircraftType.getFlights().get(i));
                    }
                }

                //Precond: All the flights start from the hubs i.e. flight with early start time are from hubs
                //Add all avaliable schedules to the curr List
                for (Flight flight1 : flights) {
                    for (int k = 0; k < flight1.getSchedule().size(); k++) {
                        if (!flight1.getSchedule().get(k).isAssigned()) {
                            curr.add(flight1.getSchedule().get(k));
                        }
                    }
                }
                if (!curr.isEmpty()) {
                    //Sort the curr ArrayList according to startTime
                    Collections.sort(curr, comparator);
                    earliestSchedule = curr.get(0);
                    for (int j = 0; j < curr.size(); j++) {
                        //Store the status for the earliest schedule
                        result.add(earliestSchedule);
                        earliestSchedule.setAssigned(true);
                        curr.remove(earliestSchedule);
                        em.persist(earliestSchedule);
                        currTime.setTime(earliestSchedule.getEndDate());
                        incoming = earliestSchedule.getFlight().getRoute();

                        //Remove all the schedules before the endTime of the earliest schedule
                        for (int k = 0; k < curr.size(); k++) {
                            tmp.setTime(curr.get(k).getStartDate());
                            if (tmp.before(currTime)) {
                                curr.remove(curr.get(k));
                                k--;
                            } else {
                                break;
                            }
                        }

                        //Set the buffer time of 1 hours after the flight arrives
                        currTime.add(Calendar.HOUR, 1);

                        //Find the first occurance of the return flight
                        for (int k = 0; k < curr.size(); k++) {
                            currRoute = curr.get(k).getFlight().getRoute();
                            if (currRoute.getOriginIATA().equals(incoming.getDestinationIATA()) && incoming.getOriginIATA().equals(currRoute.getOriginIATA())) {
                                currTime.setTime(curr.get(j).getStartDate());
                                earliestSchedule = curr.get(k);
                                break;
                            }
                        }
                    }
                }
                aircraft.setSchedules(result);
            }
        }
    }

    private Flight getFlight(String flightNo) {
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

    private List<AircraftType> retrieveAircraftTypes() {
        List<AircraftType> allTypes = new ArrayList<AircraftType>();

        try {
            Query q = em.createQuery("SELECT a from AircraftType a");

            List<AircraftType> results = q.getResultList();
            if (!results.isEmpty()) {

                allTypes = results;

            } else {
                allTypes = null;
                System.out.println("no aircraft type!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allTypes;
    }

    private List<Aircraft> retrieveAircrafts() {
        List<Aircraft> allAircrafts = new ArrayList<Aircraft>();

        try {
            Query q = em.createQuery("SELECT a from Aircraft a");

            List<Aircraft> results = q.getResultList();
            if (!results.isEmpty()) {

                allAircrafts = results;

            } else {
                allAircrafts = null;
                System.out.println("no aircraft!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allAircrafts;
    }

    private List<Schedule> getSchedules() {
        schedules = new ArrayList<Schedule>();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule a");

            List<Schedule> results = q.getResultList();
            if (!results.isEmpty()) {
                schedules = results;

            } else {
                schedules = null;
                System.out.println("No Schedules Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return schedules;
    }

    private boolean isAllAssigned(List<Schedule> schedules) {
        for (Schedule schedule1 : schedules) {
            if (!schedule1.isAssigned()) {
                return false;
            }
        }
        return true;
    }

    private Date calcEndTime(Date startTime, Flight flight) {
        //Break up the hour and minutes
        int flightHr = (int) flight.getFlightDuration();
        int flightMin = (int) ((flight.getFlightDuration() - (double) flightHr) * 60);

        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar endTime = Calendar.getInstance(tz);
        endTime.setTime(startTime);
        endTime.add(Calendar.HOUR, flightHr);
        endTime.add(Calendar.MINUTE, flightMin);

        return endTime.getTime();
    }
}
