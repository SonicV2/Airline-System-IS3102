/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Route;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yanlong
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanLocal {

    private FleetSessionBeanLocal fleetSessionBean;
    private FlightSessionBeanLocal flightSessionBean;
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public void scheduleFlights() {
        List<Flight> flights = new ArrayList<Flight>();
        System.out.println("1");
        List<Aircraft> aircrafts = new ArrayList<Aircraft>();
        System.out.println("1");
        aircrafts = fleetSessionBean.retrieveAircrafts();
        flights = flightSessionBean.getflights();
        System.out.println(aircrafts);
        System.out.println(flights);
        List<Aircraft> result = new ArrayList<Aircraft>();

        int aircraftSize = aircrafts.size();
        int flightSize = flights.size();
        double minFuel = aircrafts.get(0).getAircraftType().getFuelCost();
        String bestCraft = aircrafts.get(0).getAircraftType().getId();

        for (int i = 0; i < flightSize; i++) {
            Flight flight = flights.get(i);
            if (!flight.getFlightNo().equals("MAXXX")) {
                Route route = flight.getRoute();
                double currRouteDist = route.getDistance();
                for (int j = 0; j < aircraftSize; j++) {
                    AircraftType aircraftType = aircrafts.get(j).getAircraftType();
                    int aircraftRange = aircraftType.getTravelRange();
                    double fuel = aircraftType.getFuelCost();
                    if (aircraftRange > (int) (currRouteDist * 1.1) && fuel <= minFuel) {
                        bestCraft = aircraftType.getId();
                        minFuel = aircraftType.getFuelCost();
                    }
                }
                for (int k = 0; k < aircraftSize; k++) {
                    Aircraft aircraft = aircrafts.get(k);
                    if (aircraft.getAircraftType().getId().equals(bestCraft)) {
                        result.add(aircraft);
                        aircraft.setFlight(flight);
                    }
                }
                flight.setAircraft(result);
                em.persist(flight);
            }
        }
    }
}
