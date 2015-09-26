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
import FOS.Entity.Checklist;
import FOS.Entity.ChecklistItem;
import FOS.Entity.Team;
import FOS.Session.ChecklistSessionBeanLocal;
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
import javax.ejb.EJB;

/**
 *
 * @author Yanlong
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @EJB
    private RevenueManagementLocal rm;
    @EJB
    private ChecklistSessionBeanLocal cs;

    private Flight flight;
    private List<Flight> flights;
    private Route route;
    private AircraftType aircraftType;
    private List<AircraftType> aircraftTypes;
    private Schedule schedule;
    private List<Schedule> schedules;
    private Team team;
    private List<Aircraft> aircrafts;
    private SeatAvailability sa;
    private List<Checklist> checklists;

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
        em.persist(aircraftType);
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

//        //Prepare checklists
//        checklists = new ArrayList<Checklist>();
//        checklistItems = new ArrayList<ChecklistItem>();
//        checklistItem = new ChecklistItem();
        //Create attributes for the seatAvail
        int economy = aircraftType.getEconomySeats();
        int business = aircraftType.getBusinessSeats();
        int firstClass = aircraftType.getFirstSeats();
        int[] seats = rm.generateAvailability(economy, business, firstClass);

        //Add a list schedule until 6 months later
        while (curr.before(endTime)) {
            schedule = new Schedule();
            sa = new SeatAvailability();
            int day = curr.get(Calendar.DAY_OF_WEEK);
            if (flightDays.charAt(day - 1) == '1') {
                Date flightEnd = calcEndTime(curr.getTime(), flight);
                schedule.createSchedule(curr.getTime(), flightEnd);
                schedule.setFlight(flight);
                schedule.setTeam(team);
                schedule.setAircraft(null);
                sa.createSeatAvail(schedule, seats);
                schedule.setSeatAvailability(sa);
                checklists = cs.createChecklistAndItems();
                schedule.setChecklists(checklists);
                em.persist(schedule);
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
        List<Schedule> curr = new ArrayList<Schedule>();
        List<Schedule> result;
        Schedule earliestSchedule = new Schedule();
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar currTime = Calendar.getInstance(tz);
        Calendar tmp = Calendar.getInstance(tz);
        System.out.println("Start");
        //NOTE: ADD FUNCTIONALITIES
        //Take into account available aircrafts
        //Remove the schedules that are after current time Note: May be replaced by new getSchedule algo
        for (int i = 0; i < schedules.size(); i++) {
            tmp.setTime(schedules.get(i).getStartDate());
            if (tmp.after(currTime)) {
                curr.add(schedules.get(i));
                System.out.println(schedules.get(i));
            }
        }
        schedules = curr;
        System.out.println("Stage 1");
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
        System.out.println("Stage 2");
        //Assign flights to schedules until all schedules are assigned
//        while (!isAllAssigned(schedules)) {
        for (Aircraft aircraft : aircrafts) {
            if (aircraft.getStatus().equals("Stand-By")) {
                Route incoming = new Route();
                Route currRoute = new Route();
                flights = new ArrayList<Flight>();
                curr = new ArrayList<Schedule>();
                result = new ArrayList<Schedule>();
                aircraftType = aircraft.getAircraftType();
                System.out.println("Stage 3");
                //Add flights according to hubs
                for (int i = 0; i < aircraftType.getFlights().size(); i++) {
                    currRoute = aircraftType.getFlights().get(i).getRoute();
                    if (currRoute.getOriginIATA().equals(aircraft.getHub()) || currRoute.getDestinationIATA().equals(aircraft.getHub())) {
                        flights.add(aircraftType.getFlights().get(i));
                    }
                }
                System.out.println("Stage 4");
                //Precond: There are enough aircraft to fly all the flights
                //Add all avaliable schedules to the curr List
                for (Flight flight1 : flights) {
                    for (int k = 0; k < flight1.getSchedule().size(); k++) {
                        if (!flight1.getSchedule().get(k).isAssigned()) {
                            curr.add(flight1.getSchedule().get(k));
//                                System.out.println(flight1.getSchedule().get(k));
                        }
                    }
                }

                //Sort the curr ArrayList according to startTime
                Collections.sort(curr, comparator);
                if (!curr.isEmpty()) {
                    while (!curr.isEmpty()) {
                        //Find the earliest schedule that starts from the hub
                        for (int j = 0; j < curr.size(); j++) {
                            System.out.println(curr.get(j).getStartDate());
                            if (curr.get(j).getFlight().getRoute().getOriginIATA().equals(aircraft.getHub())) {
                                earliestSchedule = curr.get(j);
                                break;
                            }
                        }
                        //Store the status for the earliest schedule
                        result.add(earliestSchedule);
                        earliestSchedule.setAssigned(true);
                        curr.remove(earliestSchedule);
                        earliestSchedule.setAircraft(aircraft);
                        em.merge(earliestSchedule);
                        currTime.setTime(earliestSchedule.getEndDate());
                        //Set the buffer time of 2 hours after the flight arrives
                        currTime.add(Calendar.HOUR, 2);
                        incoming = earliestSchedule.getFlight().getRoute();
                        System.out.println("Stage 5");
                        //Remove all the schedules before the endTime+2hrs of the earliest schedule 
                        curr = removeScheduleBefore(curr, currTime.getTime());

                        //Find the first occurance of the return flight
                        for (int k = 0; k < curr.size(); k++) {
                            currRoute = curr.get(k).getFlight().getRoute();
                            if (currRoute.getOriginIATA().equals(incoming.getDestinationIATA()) && incoming.getOriginIATA().equals(currRoute.getOriginIATA())) {
                                earliestSchedule = curr.get(k);
                                break;
                            }
                        }
                        //Store the status for the return schedule
                        result.add(earliestSchedule);
                        earliestSchedule.setAssigned(true);
                        curr.remove(earliestSchedule);
                        earliestSchedule.setAircraft(aircraft);
                        em.merge(earliestSchedule);
                        currTime.setTime(earliestSchedule.getEndDate());

                        //Set the buffer time of 2 hours after the flight arrives
                        currTime.add(Calendar.HOUR, 2);

                        //Remove all the schedules before the endTime+2hrs of the earliest schedule 
                        curr = removeScheduleBefore(curr, currTime.getTime());
                        System.out.println("Stage 6");
                    }
                    System.out.println("Aircraft set!");
                    aircraft.setSchedules(result);
                    em.merge(aircraft);
                }
            }
        }
//        }
        clearAssignment(getSchedules());
    }

    @Override
    public void dummyRotate() {
        aircrafts = retrieveAircrafts();
        schedules = getSchedules();
        List<Schedule> curr = new ArrayList<Schedule>();
        int size = schedules.size() / aircrafts.size();
        int remaining = schedules.size() % aircrafts.size();
        int k = 0;
        int j = 0;

        for (int i = 0; i < aircrafts.size(); i++) {
            for (j = k; j < size + k; j++) {
                curr.add(schedules.get(j));
                schedules.get(j).setAircraft(aircrafts.get(i));
                em.persist(schedules.get(j));
            }
            k = j;
            aircrafts.get(i).setSchedules(curr);
            em.persist(aircrafts.get(i));
        }

        for (int i = 0; i < remaining; i++) {
            aircrafts.get(i).getSchedules().add(schedules.get(j));
            schedules.get(j).setAircraft(aircrafts.get(i));
            em.persist(schedules.get(j));
            em.persist(aircrafts.get(i));
            j++;
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

    //Alternate getSchedules algo
    private List<Schedule> getSchedules(Date currTime) {
        schedules = new ArrayList<Schedule>();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule a" + "AS a WHERE a.startDate>:startDate");
            q.setParameter("startDate", currTime);

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

    private List<Schedule> removeScheduleBefore(List<Schedule> sc, Date date) {
        schedules = new ArrayList<Schedule>();
        for (int i = 0; i < sc.size(); i++) {
            if (sc.get(i).getStartDate().after(date)) {
                schedules.add(sc.get(i));
            }
        }
        return schedules;
    }
    
    private void clearAssignment(List<Schedule> sc) {
        for (int i = 0; i < sc.size(); i++) {
                sc.get(i).setAssigned(false);
                em.merge(sc.get(i));
        }
    }
}
