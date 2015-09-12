
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
    public void addFlight(String flightNo, String flightDay, String timeslot, double flightDuration, Date startDate, int weeks);

    public void deleteFlight(String flightNo);

    public Flight getflight(String flightNo);
       
}
