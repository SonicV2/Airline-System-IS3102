/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import APS.Entity.Flight;
import APS.Entity.Location;
import APS.Entity.Route;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface DistributionSessionBeanLocal {
   //To be called by managedBean
   public boolean existsDirectFlight (String originIata, String destinationIata);
   public List<Schedule> retrieveDirectFlightsForDate (String originIata, String destinationIata, Date startDate, String serviceType, int adults, int children); 
   public List<Schedule> retrieveOneStopFlightSchedules (List <Schedule> legOne, List<Schedule> legTwo); 
   public List<String> getHubIatasFromOrigin (String originIATA);
   public List<String> getTransitHubs (List<String> hubsFromOrigin, String destinationIata);
   public Date addOneDayToDate (Date date);
   public Date convertTimeZone(Date date, TimeZone fromTZ , TimeZone toTZ);
   public String getLayoverTime (Schedule legOne, Schedule legTwo);
   public String getTotalDurationForOneStop (Schedule legOne, Schedule legTwo);
   public String getTotalDurationForDirect (Schedule schedule);
   public TimeZone getTimeZoneFromIata (String iata);
   public TimeZone getSingaporeTimeZone ();
   public boolean existsSchedule (String originIata, String destinationIata, Date date, String serviceType, int adults, int children);
   
   //only for internal processing
   public boolean isIATAAHub (String iata);
   public Schedule getScheduleForFlightForDate (Flight flight, Date startDate);
   public List<Flight> retrieveFlightsForRoute (String originIata, String destinationIata);
   public boolean seatsAvailableForSchedule (Schedule schedule, String serviceType, int noOfSeats); 
   public List<Route> getAllRoutes();
   public Location getLocationFromIata (String Iata);
}
