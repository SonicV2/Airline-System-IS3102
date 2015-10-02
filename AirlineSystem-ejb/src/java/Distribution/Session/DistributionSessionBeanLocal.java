/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface DistributionSessionBeanLocal {
   public boolean isIATAAHub (String iata);
   public boolean existsDirectFlight (String originIata, String destinationIata);
   public List<Schedule> retrieveDirectFlightsForDate (String originIata, String destinationIata, Date startDate, String serviceType, int adults, int children);
   public Schedule getScheduleForFlightForDate (Flight flight, Date startDate);
   public List<Flight> retrieveFlightsForRoute (String originIata, String destinationIata);
   public List<Schedule> retrieveOneStopFlightSchedules (List <Schedule> legOne, List<Schedule> legTwo); 
   public boolean seatsAvailableForSchedule (Schedule schedule, String serviceType, int noOfSeats);
   public List<String> getHubIatasFromOrigin (String originIATA);
   public List<String> getTransitHubs (List<String> hubsFromOrigin, String destinationIata);
   public List<Route> getAllRoutes();
   public Date addOneDayToDate (Date date);
}
