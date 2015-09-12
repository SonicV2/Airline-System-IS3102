
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
       public void addFlight(String flightNo, Date flightDate);

    public void deleteFlight(String flightNo);

    public Flight getflight(String flightNo);
       
}
