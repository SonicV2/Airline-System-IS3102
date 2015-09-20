package APS.Session;

import APS.Entity.Flight;
import APS.Entity.Route;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface FlightSessionBeanLocal {

    public void addFlight(String flightNo, String flightDays, double basicFare, Date startDateTime, Long routeId);
    public void deleteFlight(String flightNo);
    public Flight getFlight(String flightNo);
    public List<Flight> getFlights();
}
