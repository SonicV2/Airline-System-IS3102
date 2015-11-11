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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author YiQuan
 */
public class FlightSessionBeanTest {
    
    private FlightSessionBeanRemote fm = lookup();
    
    public FlightSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addFlight method, of class FlightSessionBean.
     */
    @Test
    public void testAddFlight() throws Exception {
        System.out.println("addFlight");
        String flightNo = "MA998";
        String flightDays = "1000000";
        Double basicFare = 500.0;
        Date startDateTime = new Date();
        Long routeId = Long.valueOf("852");
        boolean pastFlight = false;
        String result = fm.addFlight(flightNo, flightDays, basicFare, startDateTime, routeId, pastFlight);
        fm.deleteFlight("MA998", false);
        assertEquals("Flight Added",result);
    }

    /**
     * Test of deleteFlight method, of class FlightSessionBean.
     */
    @Test
    public void testDeleteFlight() throws Exception {
        System.out.println("deleteFlight");
        String flightNo = "MA999";
        String flightDays = "1000000";
        Double basicFare = 500.0;
        Date startDateTime = new Date();
        Long routeId = Long.valueOf("852");
        fm.addFlight(flightNo, flightDays, basicFare, startDateTime, routeId, false);
        String result = fm.deleteFlight("MA999", false);
      
    }


    /**
     * Test of getSchedule method, of class FlightSessionBean.
     */
    @Test
    public void testGetSchedule() throws Exception {
        System.out.println("getSchedule");
        Long id = Long.valueOf("39");
        assertNotNull(fm.getSchedule(id));
    }
    
    

    /**
     * Test of getFlight method, of class FlightSessionBean.
     */
    @Test
    public void testGetFlight() throws Exception {
        System.out.println("getFlight");
        String flightNo = "MA777";
        assertNotNull(fm.getFlight(flightNo));
    }

    /**
     * Test of getRoute method, of class FlightSessionBean.
     */
    @Test
    public void testGetRoute() throws Exception {
        System.out.println("getRoute");
        Long id = Long.valueOf("852");
        assertNotNull(fm.getRoute(id));
    }

    /**
     * Test of retrieveFlights method, of class FlightSessionBean.
     */
    @Test
    public void testRetrieveFlights() throws Exception {
        System.out.println("retrieveFlights");
        assertNotNull(fm.retrieveFlights());
    }

    /**
     * Test of retrieveFlightRoutes method, of class FlightSessionBean.
     */
    @Test
    public void testRetrieveFlightRoutes() throws Exception {
        System.out.println("retrieveFlightRoutes");
        assertNotNull(fm.retrieveFlightRoutes());
    }

    /**
     * Test of retrieveFlightRouteIds method, of class FlightSessionBean.
     */
    @Test
    public void testRetrieveFlightRouteIds() throws Exception {
        System.out.println("retrieveFlightRouteIds");
        assertNotNull(fm.retrieveFlightRouteIds());
    }
    
    private FlightSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (FlightSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/FlightSessionBean!APS.Session.FlightSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}