/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Flight;
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
public class ScheduleSessionBean implements ScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private Flight flight;
    private Schedule schedule;
    private List<Schedule> schedules;
    private Team team;

    @Override
    public void addSchedule(Date startDate, String flightNo) {
        schedule = new Schedule();
        flight = getFlight(flightNo);
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar endDate = Calendar.getInstance(tz);
        endDate.setTime(startDate);
        endDate.set(Calendar.SECOND, 0);
        
        //Break up the hour and minutes
        int flightHr = (int) flight.getFlightDuration();
        int flightMin = (int) ((flight.getFlightDuration() - (double) flightHr) * 60);
        endDate.add(Calendar.HOUR, flightHr);
        endDate.add(Calendar.MINUTE, flightMin);
        
        schedule.createSchedule(startDate, endDate.getTime());
        flight.getSchedule().add(schedule);
        schedule.setFlight(flight);
        schedule.setTeam(team);
        em.persist(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        //NEED TO REMOVE RELATION WITH AIRCRAFT TOO
        schedule = getSchedule(id);
        schedule.setFlight(flight);
        schedule.setTeam(team);
        em.remove(schedule);
    }

    @Override
    public Schedule getSchedule(Long id) {
        schedule = new Schedule();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule " + "AS a WHERE a.scheduleId=:scheduleId");
            q.setParameter("scheduleId", id);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                schedule = (Schedule) results.get(0);

            } else {
                schedule = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return schedule;
    }

    @Override
    public List<Schedule> getSchedules() {
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
}
