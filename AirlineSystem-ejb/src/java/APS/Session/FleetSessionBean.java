/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import javax.ejb.Stateless;
import APS.Entity.AircraftType;
import APS.Entity.Aircraft;
import APS.Entity.Flight;
import APS.Entity.Route;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Yunna
 */
@Stateless
public class FleetSessionBean implements FleetSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;
    private FlightSessionBeanLocal flightSessionBean;

    private List<AircraftType> aircraftTypeList = new ArrayList<AircraftType>();
    private Flight flight;
    private Aircraft aircraft;
    private AircraftType aircraftType;

    @Override
    public void acquireAircraft(Date datePurchased, Date lastMaintained, String aircraftTypeId) {
        aircraft = new Aircraft();
        aircraftType = new AircraftType();
        aircraftType = getAircraftType(aircraftTypeId);
        aircraft.setAircraftType(aircraftType);
        aircraft.createAircraft(datePurchased, lastMaintained, "Stand-By");
        
        aircraftType.getAircraft().add(aircraft);
        aircraft.setFlight(flight);
        em.persist(aircraft);
    }

    @Override
    public void retireAircraft(Long tailNo) {

        aircraft = getAircraft(tailNo);
        em.remove(aircraft);

    }

    @Override
    public List<AircraftType> getAircraftTypeList(String filter) {

        List<AircraftType> aircraftTypeList = new ArrayList();

        return aircraftTypeList;
    }

    // get aircraftType object when searching with aircraftTypeId
    @Override
    public AircraftType getAircraftType(String aircraftTypeId) {

        AircraftType type1 = new AircraftType();
        try {

            Query q = em.createQuery("SELECT a FROM AircraftType " + "AS a WHERE a.id=:id");
            q.setParameter("id", aircraftTypeId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                type1 = (AircraftType) results.get(0);

            } else {
                type1 = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return type1;
    }

    // get aircraft object when searching with tail num
    @Override
    public Aircraft getAircraft(Long tailNum) {

        Aircraft aircraft1 = new Aircraft();

        try {

            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.tailNo=:tailNo");
            q.setParameter("tailNo", tailNum);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                aircraft1 = (Aircraft) results.get(0);

            } else {
                aircraft1 = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return aircraft1;
    }

    public List<AircraftType> retrieveAircraftTypes() {
        List<AircraftType> allTypes = new ArrayList<AircraftType>();

        try {
            Query q = em.createQuery("SELECT a from AircraftType a");

            List<AircraftType> results = q.getResultList();
            if (!results.isEmpty()) {

                allTypes = results;

            } else {
                allTypes = null;
                System.out.println("no aircraft type!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allTypes;
    }

    public List<Aircraft> retrieveAircrafts() {
        List<Aircraft> allAircrafts = new ArrayList<Aircraft>();

        try {
            Query q = em.createQuery("SELECT a from Aircraft a");

            List<Aircraft> results = q.getResultList();
            if (!results.isEmpty()) {

                allAircrafts = results;

            } else {
                allAircrafts = null;
                System.out.println("no aircraft!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allAircrafts;
    }

    @Override
    public void scheduleFlights() {
        List<Flight> flights = new ArrayList<Flight>();
        System.out.println("1");
        List<Aircraft> aircrafts = new ArrayList<Aircraft>();
        System.out.println("2");
        aircrafts = retrieveAircrafts();
        System.out.println("3");
        flights = getflights();
        System.out.println("4");
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

    public Flight getflight(String flightNo) {
        flight = new Flight();
        try {

            Query q = em.createQuery("SELECT a FROM Flight " + "AS a WHERE a.flightNo=:flightNo");
            q.setParameter("flightNo", flightNo);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                flight = (Flight) results.get(0);

            } else {
                flight = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return flight;
    }

    public List<Flight> getflights() {
        List<Flight> flights = new ArrayList<Flight>();
        try {

            Query q = em.createQuery("SELECT a FROM Flight a");

            List<Flight> results = q.getResultList();
            if (!results.isEmpty()) {
                flights = results;

            } else {
                flights = null;
                System.out.println("No Flights Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return flights;
    }

}
