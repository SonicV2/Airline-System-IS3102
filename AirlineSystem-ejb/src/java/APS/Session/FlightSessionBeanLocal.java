package APS.Session;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface FlightSessionBeanLocal {

    public void addFlight(String flightNo, String flightDays, Double basicFare, Date startDateTime, Long routeId);
    public void deleteFlight(String flightNo);
    public void deleteSchedule(Long scheduleId);
    public Flight getFlight(String flightNo);
    public List<Flight> getFlights();
    public Route getRoute(Long id);
    public List<Flight> retrieveFlights();
    public Schedule getSchedule(Long scheduleId);
}
