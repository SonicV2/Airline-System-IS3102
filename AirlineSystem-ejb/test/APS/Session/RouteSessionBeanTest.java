/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Location;
import APS.Entity.Route;
import Inventory.Session.ClassSessionBeanRemote;
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
public class RouteSessionBeanTest {
    
    public RouteSessionBeanTest() {
    }
    
    private RouteSessionBeanRemote rm = lookup();
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addRoute method, of class RouteSessionBean.
     */
//    @Test
//    public void testAddRoute() throws Exception {
//        System.out.println("addRoute");
//        String origin = "";
//        String destination = "";
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        instance.addRoute(origin, destination);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteRoute method, of class RouteSessionBean.
//     */
//    @Test
//    public void testDeleteRoute() throws Exception {
//        System.out.println("deleteRoute");
//        Long routeId = null;
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        instance.deleteRoute(routeId);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getRoute method, of class RouteSessionBean.
//     */
//    @Test
//    public void testGetRoute() throws Exception {
//        System.out.println("getRoute");
//        Long id = null;
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        Route expResult = null;
//        Route result = instance.getRoute(id);
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getLocation method, of class RouteSessionBean.
//     */
//    @Test
//    public void testGetLocation() throws Exception {
//        System.out.println("getLocation");
//        String country1 = "";
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        Location expResult = null;
//        Location result = instance.getLocation(country1);
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findLocation method, of class RouteSessionBean.
//     */
//    @Test
//    public void testFindLocation() throws Exception {
//        System.out.println("findLocation");
//        String location = "";
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        Location expResult = null;
//        Location result = instance.findLocation(location);
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveLocations method, of class RouteSessionBean.
//     */
//    @Test
//    public void testRetrieveLocations() throws Exception {
//        System.out.println("retrieveLocations");
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        List<Location> expResult = null;
//        List<Location> result = instance.retrieveLocations();
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchLocationsByCountry method, of class RouteSessionBean.
//     */
//    @Test
//    public void testSearchLocationsByCountry() throws Exception {
//        System.out.println("searchLocationsByCountry");
//        String searchCountry = "";
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        List<Location> expResult = null;
//        List<Location> result = instance.searchLocationsByCountry(searchCountry);
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of searchLocationsByCity method, of class RouteSessionBean.
//     */
//    @Test
//    public void testSearchLocationsByCity() throws Exception {
//        System.out.println("searchLocationsByCity");
//        String searchCity = "";
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        List<Location> expResult = null;
//        List<Location> result = instance.searchLocationsByCity(searchCity);
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of retrieveRoutes method, of class RouteSessionBean.
//     */
//    @Test
//    public void testRetrieveRoutes() throws Exception {
//        System.out.println("retrieveRoutes");
//        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
//        RouteSessionBeanLocal instance = (RouteSessionBeanLocal)container.getContext().lookup("java:global/classes/RouteSessionBean");
//        List<Route> expResult = null;
//        List<Route> result = instance.retrieveRoutes();
//        assertEquals(expResult, result);
//        container.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of haversineDist method, of class RouteSessionBean.
//     */
    @Test
    public void testHaversineDist() throws Exception {
        System.out.println("haversineDist");
        Double lat1 = 100.0;
        Double long1 = 200.0;
        Double lat2 = 300.0;
        Double long2 = 400.0;
        String expResult = "15623.85";
        Double result = RouteSessionBean.haversineDist(lat1, long1, lat2, long2);
        String resultString = result.toString();
        int index = resultString.indexOf(".") +3;
        resultString = resultString.substring(0,index);
        System.out.println(resultString);
        assertEquals(expResult, resultString);       
        // TODO review the generated test code and remove the default call to fail.
    }
    
    private RouteSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (RouteSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/RouteSessionBean!APS.Session.RouteSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
