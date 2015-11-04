/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Schedule;
import FOS.Entity.Checklist;
import FOS.Entity.Team;
import FOS.Session.ChecklistSessionBeanLocal;
import Inventory.Entity.SeatAvailability;
import Inventory.Session.PricingSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
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
    private Aircraft aircraft;
    private AircraftType aircraftType;
    private SeatAvailability seatAvail;
    private List<Checklist> checklists;

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

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
    @EJB
    private PricingSessionBeanLocal ps;
    @EJB
    private ChecklistSessionBeanLocal cs;

    //Update the changes made to schedules
    @Override
    public void edit(Schedule edited, Schedule original) {

        //Only if there was a change in the start time of schedule, calculate the new end time and persist it
        if (!edited.getStartDate().equals(original.getStartDate())) {
            edited.setEndDate(calcEndTime(edited.getStartDate(), edited.getFlight()));
            em.merge(edited);
        }

        //Only if there was a change in the aircraft tail number of schedule, assign the changed aircraft to this schedule
        if (!edited.getAircraft().getTailNo().equals(original.getAircraft().getTailNo())) {

            Aircraft ac = em.find(Aircraft.class, edited.getAircraft().getTailNo());
            List<Schedule> temp1 = ac.getSchedules();
            temp1.remove(original);
            temp1.add(edited);
            ac.setSchedules(temp1);

            Aircraft or = em.find(Aircraft.class, original.getAircraft().getTailNo());
            or.setStatus("Out-of-Order");

            Long id = edited.getScheduleId();
            Schedule change = em.find(Schedule.class, id);
            change.setAircraft(ac);
            em.flush();
        }
    }

    //Add new schedules to an existing flight according to the duration that the user specifies
    //@param isUpdate - boolean that specifies whether the call is upon creation of flight or updating of flight  
    //Precond: Duration in months
    @Override
    public void addSchedules(int duration, String flightNo, boolean isUpdate) {
        flight = getFlight(flightNo);
        String flightDays = flight.getFlightDays();
        aircraftType = flight.getAircraftType();
        schedules = new ArrayList<Schedule>();

        if (isUpdate) {
            schedules = flight.getSchedule();
            Collections.sort(schedules, comparator); //Sort the Schedules array
            schedule = schedules.get(schedules.size() - 1); //Assign the latest schedule
        }

        //Get the starting time and the counter
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar currTime = Calendar.getInstance(tz);
        if (isUpdate) {
            currTime.setTime(schedule.getStartDate());
            currTime.add(Calendar.DATE, 1); //Set the new Schedule to start on the next day
        } else {
            currTime.setTime(flight.getStartDateTime());
        }
        Date counter = currTime.getTime();

        //Find the the end time of the new set of schedules to be added
        Calendar endTime = Calendar.getInstance(tz);
        endTime.setTime(counter);
        endTime.add(Calendar.MONTH, duration);

        //Create attributes for the seatAvail
        int economy = aircraftType.getEconomySeats();
        int business = aircraftType.getBusinessSeats();
        int firstClass = aircraftType.getFirstSeats();
        int[] seats = ps.generateAvailability(flightNo, economy, business, firstClass);

        //Add a list schedule until the number of months specified
        while (currTime.before(endTime)) {
            schedule = new Schedule();
            seatAvail = new SeatAvailability();
            int day = currTime.get(Calendar.DAY_OF_WEEK);
            if (flightDays.charAt(day - 1) == '1') {
                Date flightEnd = calcEndTime(currTime.getTime(), flight);
                schedule.createSchedule(currTime.getTime(), flightEnd);
                schedule.setFlight(flight);
                schedule.setTeam(team);
                schedule.setAircraft(null);
                seatAvail.createSeatAvail(schedule, seats);
                schedule.setSeatAvailability(seatAvail);
                checklists = cs.createChecklistAndItems();
                schedule.setChecklists(checklists);
                em.persist(schedule);
                em.persist(seatAvail);

                schedules.add(schedule);
            }
            currTime.setTime(counter);
            currTime.add(Calendar.DATE, 1);
            counter = currTime.getTime();
        }
        flight.setSchedule(schedules);
        em.merge(flight);

        if (isUpdate) {
            flightScheduleSessionBean.rotateAircrafts(); //rotate flights to assign aircrafts if addSchedules is called as a update
        }
    }

    //Delete existing schedule entity
    @Override
    public void deleteSchedule(Long id) {
        schedule = getSchedule(id);

        seatAvail = schedule.getSeatAvailability();
        seatAvail.setSchedule(null);

        schedule.setSeatAvailability(null);

        em.remove(seatAvail);
        em.remove(schedule);
        em.flush();
    }

    //Get a specific schedule with schedule id
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

    //Get a specific schedule with start date of the schedule
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

    //Get all the existing schedules
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

    @Override
    public List<Schedule> getScheduleAfter(Date date) {
        schedules = getSchedules();
        List<Schedule> tmp = new ArrayList<Schedule>();

        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).getStartDate().after(date)) {
                tmp.add(schedules.get(i));
            }
        }
        return tmp;
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

    //Change the display of flight days from mixture of 1s and 0s to proper days in words
    @Override
    public void displayFlightDays(List<Flight> flights) {

        for (int i = 0; i < flights.size(); i++) {
            String temp = "";
            String flightDays = flights.get(i).getFlightDays();

            for (int j = 0; j < flightDays.length(); j++) {
                if (flightDays.charAt(j) == '1' && j == 0) {
                    temp += "Sunday";
                }

                if (flightDays.charAt(j) == '1' && j == 1) {
                    if (temp.isEmpty()) {
                        temp += "Monday";
                    } else {
                        temp += ", Monday";
                    }
                }
                if (flightDays.charAt(j) == '1' && j == 2) {
                    if (temp.isEmpty()) {
                        temp += "Tuesday";
                    } else {
                        temp += ", Tuesday";
                    }
                }

                if (flightDays.charAt(j) == '1' && j == 3) {
                    if (temp.isEmpty()) {
                        temp += "Wednesday";
                    } else {
                        temp += ", Wednesday";
                    }
                }

                if (flightDays.charAt(j) == '1' && j == 4) {
                    if (temp.isEmpty()) {
                        temp += "Thursday";
                    } else {
                        temp += ", Thursday";
                    }
                }

                if (flightDays.charAt(j) == '1' && j == 5) {
                    if (temp.isEmpty()) {
                        temp += "Friday";
                    } else {
                        temp += ", Friday";
                    }
                }

                if (flightDays.charAt(j) == '1' && j == 6) {
                    if (temp.isEmpty()) {
                        temp += "Saturday";
                    } else {
                        temp += ", Saturday";
                    }
                }

            }

            flights.get(i).setFlightDaysString(temp);
        }
    }

    //Get a list of schedules that use same aircraft tail number
    @Override
    public List<Schedule> getSchedules(Long tailNo) {
        aircraft = new Aircraft();
        try {
            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.tailNo=:tailNo");
            q.setParameter("tailNo", tailNo);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                aircraft = (Aircraft) results.get(0);

            } else {
                aircraft = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return aircraft.getSchedules();
    }

    //Calculate the new end time of edited schedule
    @Override
    public Date calcEndTime(Date startTime, Flight flight) {
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

    //Change the formats of dates for past schedules
    @Override
    public List<Schedule> filterForPastSchedules(List<Schedule> schedules) {
        Date todayDate = new Date();
        String todayDateFormatted = new SimpleDateFormat("yyyyMMdd").format(todayDate);
        int todayDateInt = Integer.parseInt(todayDateFormatted);

        List<Schedule> pastSchedules = new ArrayList();
        for (Schedule eachSchedule : schedules) {
            String eachScheduleDate = new SimpleDateFormat("yyyyMMdd").format(eachSchedule.getStartDate());
            int eachScheduleInt = Integer.parseInt(eachScheduleDate);

            if (eachScheduleInt <= todayDateInt) {
                pastSchedules.add(eachSchedule);
            }
        }
        return pastSchedules;
    }

    //Change the formats of dates for future schedules
    @Override
    public List<Schedule> filterForFutureSchedules(List<Schedule> schedules) {
        Date todayDate = new Date();
        String todayDateFormatted = new SimpleDateFormat("yyyyMMdd").format(todayDate);
        int todayDateInt = Integer.parseInt(todayDateFormatted);

        List<Schedule> futureSchedules = new ArrayList();
        for (Schedule eachSchedule : schedules) {
            String eachScheduleDate = new SimpleDateFormat("yyyyMMdd").format(eachSchedule.getStartDate());
            int eachScheduleInt = Integer.parseInt(eachScheduleDate);
            if (eachScheduleInt > todayDateInt) {
                futureSchedules.add(eachSchedule);
            }

        }
        return futureSchedules;
    }

    //Change the formats of dates for current schedules
    @Override
    public List<Schedule> filterForCurrentDaySchedules(List<Schedule> schedules) {
        Date todayDate = new Date();
        String todayDateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(todayDate);

        List<Schedule> currentDaySchedules = new ArrayList();
        for (Schedule eachSchedule : schedules) {
            String eachScheduleDate = new SimpleDateFormat("dd/MM/yyyy").format(eachSchedule.getEndDate());

            if (todayDateFormatted.equals(eachScheduleDate)) {
                currentDaySchedules.add(eachSchedule);
            }
        }
        return currentDaySchedules;
    }

}
