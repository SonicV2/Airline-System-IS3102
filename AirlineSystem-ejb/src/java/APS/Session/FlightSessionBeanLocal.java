
package APS.Session;

import APS.Entity.Flight;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface FlightSessionBeanLocal {
    public void addFlight(String flightNo, String flightDays, double flightDuration, double basicFare, Date startDateTime);

    public void deleteFlight(String flightNo);

    public Flight getflight(String flightNo);

    public List<Flight> getflights();
       
}
