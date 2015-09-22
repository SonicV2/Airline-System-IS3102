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
    public void edit(Schedule schedule){
        em.merge(schedule);
    }
    
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
        em.remove(schedule);
        em.flush();
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
    public Schedule getScheduleByDate(Date startDate) {
        schedule = new Schedule();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule " + "AS a WHERE a.startDate=:startDate");
            q.setParameter("startDate", startDate);

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
    public void changeFlightDays (List<Flight> flights) {
        
        for (int i=0; i<flights.size(); i++) {
            String temp = "";
            String flightDays = flights.get(i).getFlightDays();
            
            for (int j=0; j<flightDays.length(); j++) {
                if (flightDays.charAt(j) == '1' && j==0)
                    temp += "Sunday";
                
                if (flightDays.charAt(j) == '1' && j==1) {
                    if (temp.isEmpty())
                        temp += "Monday";
                    else
                        temp += ", Monday";
                }
                if (flightDays.charAt(j) == '1' && j==2) {
                    if (temp.isEmpty())
                        temp += "Tuesday";
                    else
                        temp += ", Tuesday";
                }
                
                if (flightDays.charAt(j) == '1' && j==3) {
                    if (temp.isEmpty())
                        temp += "Wednesday";
                    else
                        temp += ", Wednesday";
                }
                
                if (flightDays.charAt(j) == '1' && j==4) {
                    if (temp.isEmpty())
                        temp += "Thursday";
                    else
                        temp += ", Thursday";
                }
                
                if (flightDays.charAt(j) == '1' && j==5) {
                    if (temp.isEmpty())
                        temp += "Friday";
                    else
                        temp += ", Friday";
                }
                
                if (flightDays.charAt(j) == '1' && j==6) {
                    if (temp.isEmpty())
                        temp += "Saturday";
                    else
                        temp += ", Saturday";
                }
                
            }
            
            flights.get(i).setFlightDaysString(temp);
        }

    }
}
