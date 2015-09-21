/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import FOS.Entity.Team;
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
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    private Flight flight;
    private Route route;
    private AircraftType aircraftType;
    private List<AircraftType> aircraftTypes;
    private Schedule schedule;
    private List<Schedule> schedules;
    private Team team;

    @Override
    public void scheduleFlights(String flightId) {
        flight = getFlight(flightId);
        aircraftType = new AircraftType();
        aircraftTypes = retrieveAircraftTypes();
        route = flight.getRoute();
        
        //Find the best Aircraft for the flight
        aircraftType = aircraftTypes.get(0);
        Double minFuel = aircraftTypes.get(0).getFuelCost();
        
        for (int i = 0; i<aircraftTypes.size();i++){
            if ((route.getDistance()<(double)aircraftTypes.get(i).getTravelRange()*1.1) && (aircraftTypes.get(i).getFuelCost() < minFuel)){
                aircraftType = aircraftTypes.get(i);
                minFuel = aircraftTypes.get(i).getFuelCost();
            }
        }
        
        flight.setAircraftType(aircraftType);
        aircraftType.getFlights().add(flight);
        flight.setFlightDuration(route.getDistance()/(aircraftType.getSpeed()*1062));
        
        //Create link with Schedules
        Date startDateTime = flight.getStartDateTime();
        String flightDays = flight.getFlightDays();
        schedules = new ArrayList<Schedule>();
        
        //Forecast the last date of the flight in 6 months
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar endTime = Calendar.getInstance(tz);
        endTime.setTime(startDateTime);
        endTime.set(Calendar.SECOND, 0);
        endTime.add(Calendar.MONTH, 6);

        Calendar curr = Calendar.getInstance(tz);
        curr.setTime(startDateTime);
        curr.set(Calendar.SECOND, 0);
        Date counter = startDateTime;
        //Break up the hour and minutes
        int flightHr = (int) flight.getFlightDuration();
        int flightMin = (int) ((flight.getFlightDuration() - (double) flightHr) * 60);

        //Add a list schedule until 6 months later
        while (curr.before(endTime)) {
            schedule = new Schedule();
            int day = curr.get(Calendar.DAY_OF_WEEK);
            if (flightDays.charAt(day - 1) == '1') {
                Date flightStart = curr.getTime();
                curr.add(Calendar.HOUR, flightHr);
                curr.add(Calendar.MINUTE, flightMin);
                Date flightEnd = curr.getTime();
                schedule.createSchedule(flightStart, flightEnd);
                schedule.setFlight(flight);
                schedule.setTeam(team);
                em.persist(schedule);
                schedules.add(schedule);
            }
            curr.setTime(counter);
            curr.add(Calendar.DATE, 1);
            counter = curr.getTime();
        }
        flight.setSchedule(schedules);
        em.persist(flight);
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
}
