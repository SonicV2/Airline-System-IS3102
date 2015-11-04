/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author YiQuan
 */
@Remote
public interface FlightSessionBeanRemote {
    public String addFlight(String flightNo, String flightDays, Double basicFare, Date startDateTime, Long routeId, boolean pastFlight);
    public String deleteFlight(String flightNo, boolean isArchive);
    public void edit(Flight edited, Flight original);
    public void deleteSchedule(Long scheduleId);
    public Flight getFlight(String flightNo);
    public Route getRoute(Long id);
    public List<Flight> retrieveFlights();
    public List<Flight> retrieveActiveFlights();
    public Schedule getSchedule(Long scheduleId);
    public List<Route> retrieveFlightRoutes();
    public List<Long> retrieveFlightRouteIds();
}
