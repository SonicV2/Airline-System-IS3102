
package APS.Session;

import APS.Entity.Flight;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface FlightSessionBeanLocal {
    public void addFlight(String flightNo, String flightDays, String timeslot, double flightDuration, double basicFare, Date startDate);

    public void deleteFlight(String flightNo);

    public Flight getflight(String flightNo);
       
}
