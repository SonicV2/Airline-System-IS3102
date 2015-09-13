package APS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import static java.util.TimeZone.getTimeZone;
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
public class FlightSessionBean implements FlightSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    Flight flight;

    @Override
    public void addFlight(String flightNo, String flightDays, String timeslot, double flightDuration, double basicFare, Date planStartDate) {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        Schedule sc = new Schedule();
        flight.createFlight(flightNo, flightDays, timeslot, flightDuration, basicFare, planStartDate);
        //Forecast the last date of the flight in 6 months
        TimeZone tz = getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar cal = Calendar.getInstance(tz);
        cal.setTime(planStartDate);
        cal.add(Calendar.MONTH, 6);
        Date planEndDate = cal.getTime();

        //Create the array of schedule
        Calendar curr = Calendar.getInstance(tz);
        curr.setTime(planStartDate);
        Date counter = planStartDate;
        //Break up the hour and minutes
        int flightHr = (int) flightDuration;
        int flightMin = (int) ((flightDuration - (double) flightHr) * 60);
        
        //Add a list schedule until 6 months later
        while (curr.before(planEndDate)) {
            int day = curr.get(Calendar.DAY_OF_WEEK);
            if (flightDays.charAt(day - 1) == '1') {
                Date flightStart = curr.getTime();
                curr.add(Calendar.HOUR, flightHr);
                curr.add(Calendar.MINUTE, flightMin);
                Date flightEnd = curr.getTime();
                sc.createSchedule(flightStart, flightEnd);
                schedules.add(sc);
            }
            curr.setTime(counter);
            curr.add(Calendar.DATE, 1);
            counter = curr.getTime();
        }
        flight.setSchedule(schedules);
        em.persist(flight);
    }

    @Override
    public void deleteFlight(String flightNo) {
        flight = getflight(flightNo);
        em.remove(flight);
    }

    @Override
    public Flight getflight(String flightNo) {
        flight = new Flight();
        try {

            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.flightNo=:flightNo");
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
