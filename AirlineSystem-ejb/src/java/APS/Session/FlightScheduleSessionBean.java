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
import FOS.Entity.MaintainSchedule;
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
    private ScheduleSessionBeanLocal scheduleSessionBean;

    private Flight flight;
    private List<Flight> flights;
    private Route route;
    private AircraftType aircraftType;
    private List<AircraftType> aircraftTypes;
    private List<Schedule> schedules;
    private List<Aircraft> aircrafts;

    //Create comparator for sorting of Schedules according to starting time
    private Comparator<Schedule> comparator = new Comparator<Schedule>() {
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

    @Override
    public void scheduleFlights(String flightNo, boolean fortnight) {
        flight = getFlight(flightNo);
        aircraftType = new AircraftType();
        aircraftTypes = retrieveAircraftTypes();
        route = flight.getRoute();

        //Find the best Aircraft for the flight
        aircraftType = aircraftTypes.get(0);
        Double minCost = aircraftType.getFuelCost();
        Double maxRev = 0.0;
        Double currCost = 0.0;
        Double currRev = 0.0;
        for (AircraftType aircraftType1 : aircraftTypes) {
            currCost = aircraftType1.getFuelCost();
            currRev = flight.getBasicFare() * (aircraftType1.getBusinessSeats() + aircraftType1.getEconomySeats() + aircraftType1.getFirstSeats());
            if ((route.getDistance() < (double) aircraftType1.getTravelRange() * 1.1) && (currCost <= minCost) && (currRev >= maxRev)) {
                aircraftType = aircraftType1;
                minCost = currCost;
                maxRev = currRev;
            }
        }

        flight.setAircraftType(aircraftType);
        aircraftType.getFlights().add(flight);
        em.merge(aircraftType);
        DecimalFormat df = new DecimalFormat("0.##");
        flight.setFlightDuration(Double.valueOf(df.format(route.getDistance() / (aircraftType.getSpeed() * 1062))));
        em.merge(flight);

        //Create link with Schedules
        //Add a list schedule until 12 months/1 year later
        scheduleSessionBean.addSchedules(12, flightNo, false, fortnight);
    }

    @Override
    public void rotateAircrafts() {
        aircrafts = retrieveAircrafts();
        schedules = getSchedules();
        aircraftType = new AircraftType();
        List<Schedule> curr = new ArrayList<Schedule>();
        List<Schedule> result;
        Schedule earliestSchedule = new Schedule();
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar currTime = Calendar.getInstance(tz);
        Calendar tmp = Calendar.getInstance(tz);

        //Remove the schedules that are after current time Note: May be replaced by new getSchedule algo
        for (int i = 0; i < schedules.size(); i++) {
            tmp.setTime(schedules.get(i).getStartDate());
            if (tmp.after(currTime)) {
                curr.add(schedules.get(i));
            }
        }
        schedules = curr;

        //Assign flights to schedules until all schedules are assigned
        for (Aircraft aircraft : aircrafts) {
            if (aircraft.getStatus().equals("Ready")) {
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

                if (!curr.isEmpty()) {
                    //Sort the curr ArrayList according to startTime
                    Collections.sort(curr, comparator);
                    tmp.setTime(curr.get(0).getStartDate());
                    tmp.add(Calendar.MINUTE, -1); //Set a temporary time before the earliest time schedule
                    curr = removeHubScheduleBefore(curr, tmp.getTime(), aircraft.getHub()); //Remove schedules that start earlier but not from the hub

                    while (!curr.isEmpty()) {
                        //Assign the first schedule to be the earliest
                        earliestSchedule = curr.get(0);
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
                        //Remove all the schedules before the endTime+2hrs of the earliest schedule 
                        curr = removeScheduleBefore(curr, currTime.getTime());

                        //Find the first occurance of the return flight
                        for (int k = 0; k < curr.size(); k++) {
                            currRoute = curr.get(k).getFlight().getRoute();
                            if (currRoute.getOriginIATA().equals(incoming.getDestinationIATA()) && incoming.getOriginIATA().equals(currRoute.getDestinationIATA())) {
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
                        curr = removeHubScheduleBefore(curr, currTime.getTime(), aircraft.getHub());
//                        }
                    }
                    System.out.println("Aircraft set!");
                    aircraft.setSchedules(result);
                    em.merge(aircraft);
                }
            }
        }
//        }
        //Clear the isAssigned attribute of Aircrafts for the next assignment
        System.out.println("Aircraft Rotation DONE!");
        clearAssignment(getSchedules());
    }

    @Override //Deprecated 
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

    @Override
    public void rotateMaintainenceSchedule(String tailNo) {
        Aircraft aircraft = em.find(Aircraft.class, tailNo);
        schedules = aircraft.getSchedules();
//        List<MaintainSchedule> mSchedules = aircraft.getmSchedules();
        List<Aircraft> reserves = getReserveAircrafts("Stand-By");
        Comparator<MaintainSchedule> comparator2 = new Comparator<MaintainSchedule>() {
            @Override
            public int compare(MaintainSchedule o1, MaintainSchedule o2) {
                int result = o1.getMainStartDate().compareTo(o2.getMainStartDate());
                if (result == 0) {
                    return o1.getMainStartDate().before(o2.getMainStartDate()) ? -1 : 1;
                } else {
                    return result;
                }
            }
        };
        Schedule selectedSchedule1 = new Schedule();
        Schedule selectedSchedule2 = new Schedule();
        Aircraft selectedAircraft = new Aircraft();
        int counter = 0;
        List<Schedule> selectedSchedules = new ArrayList<Schedule>();
        Collections.sort(schedules, comparator);
//        Collections.sort(mSchedules, comparator2);
//        for (int i = 0; i < mSchedules.size(); i++) {
//            //Search for the pair of schedules that conflicts
//            for (int j = 0; j < schedules.size(); j++) {
//                if (mSchedules.get(i).getMainStartDate().before(schedules.get(i).getStartDate())) {
//                    selectedSchedule1 = schedules.get(i);
//                    selectedSchedule2 = schedules.get(i + 1);
//                }
//            }
//
//            for (int j = 0; j < reserves.size(); j++) {
//                selectedAircraft = reserves.get(j);
//                selectedSchedules = selectedAircraft.getSchedules();
//                Collections.sort(selectedSchedules, comparator);
//                for (counter = 0; counter < selectedSchedules.size(); counter++) {
//                    if (selectedSchedules.get(counter).getStartDate().before(selectedSchedule1.getStartDate()) 
//                            && selectedSchedules.get(counter).getEndDate().after(selectedSchedule1.getEndDate())) {
//                        break;
//                    }
//                    
//                    if (selectedSchedules.get(counter).getStartDate().before(selectedSchedule2.getStartDate()) 
//                            && selectedSchedules.get(counter).getEndDate().after(selectedSchedule2.getEndDate())) {
//                        break;
//                    }
//                }
//
//                if (counter == selectedSchedules.size() - 1) {
//                    break;
//                }
//            }
//            
//            aircraft.getSchedules().remove(selectedSchedule1);
//            aircraft.getSchedules().remove(selectedSchedule2);
//            selectedAircraft.getSchedules().add(selectedSchedule1);
//            selectedAircraft.getSchedules().add(selectedSchedule2);
//            selectedSchedule1.setAircraft(selectedAircraft);
//            selectedSchedule2.setAircraft(selectedAircraft);
//            em.merge(selectedSchedule1);
//            em.merge(selectedSchedule2);
//        }

    }

    //Retrieve flight object by searching with flight number
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

    //Retireve the whole database of Aircraft Types
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

    //Retrieve the whole database of Aircrafts
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

    //Retrieve the whole database of schedules
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

//    //Alternate getSchedules algo
//    private List<Schedule> getSchedules(Date currTime) {
//        schedules = new ArrayList<Schedule>();
//        try {
//
//            Query q = em.createQuery("SELECT a FROM Schedule a" + "AS a WHERE a.startDate>:startDate");
//            q.setParameter("startDate", currTime);
//
//            List<Schedule> results = q.getResultList();
//            if (!results.isEmpty()) {
//                schedules = results;
//
//            } else {
//                schedules = null;
//                System.out.println("No Schedules Added!");
//            }
//
//        } catch (EntityNotFoundException enfe) {
//            System.out.println("\nEntity not found error" + "enfe.getMessage()");
//        }
//        return schedules;
//    }
    //Returns a list of Schedule objects with start date after the given date
    private List<Schedule> removeScheduleBefore(List<Schedule> sc, Date date) {
        schedules = new ArrayList<Schedule>();
        for (int i = 0; i < sc.size(); i++) {
            if (sc.get(i).getStartDate().after(date)) {
                schedules.add(sc.get(i));
            }
        }
        return schedules;
    }

    //Returns a list of Schedule objects with originIATA at the hub and start date after the given date
    private List<Schedule> removeHubScheduleBefore(List<Schedule> sc, Date date, String hub) {
        schedules = new ArrayList<Schedule>();
        int counter = -1; //Initialize counter for first occurance of schedule with hub as origin
        for (int i = 0; i < sc.size(); i++) {
            if (sc.get(i).getStartDate().after(date) && sc.get(i).getFlight().getRoute().getOriginIATA().equals(hub)) {
                counter = i;
                break;
            }
        }

        if (counter != -1) {
            for (int i = counter; i < sc.size(); i++) {
                schedules.add(sc.get(i));
            }
        }
        return schedules;
    }

    private List<Aircraft> getReserveAircrafts(String status) {

        List<Aircraft> reserveAircrafts = new ArrayList<Aircraft>();

        try {
            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.status=:status");
            q.setParameter("status", status);

            List<Aircraft> results = q.getResultList();
            if (!results.isEmpty()) {

                reserveAircrafts = results;

            } else {
                reserveAircrafts = null;
                System.out.println("no reserve aircraft!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return reserveAircrafts;
    }

    //Resets all the isAssigned attribute of the aircrafts
    private void clearAssignment(List<Schedule> sc) {
        for (int i = 0; i < sc.size(); i++) {
            sc.get(i).setAssigned(false);
            em.merge(sc.get(i));
        }
    }
}
