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
    
    private RouteSessionBeanRemote rm = lookup();
    
    public RouteSessionBeanTest() {
    }
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    public void testDeleteRoute() throws Exception {
        System.out.println("deleteRoute");
        Long routeId = Long.valueOf("2102");
        String result = rm.deleteRoute(routeId);
        assertEquals("Route Deleted",result);
    }


    @Test
    public void testAddRoute() throws Exception {
        System.out.println("addRoute");
        String origin = "SIN";
        String destination = "CPH";
        String result = rm.addRoute(origin, destination);
        assertEquals("Route Added",result);
    }



 


    @Test
    public void testFindLocation() throws Exception {
        System.out.println("getLocation");
        String country1 = "China";
        assertNotNull(rm.getLocation(country1));
    }


    @Test
    public void testRetrieveLocations() throws Exception {
        System.out.println("retrieveLocations");
        assertNotNull(rm.retrieveLocations());
    }

    /**
     * Test of searchLocationsByCountry method, of class RouteSessionBean.
     */
    @Test
    public void testSearchLocationsByCountry() throws Exception {
        System.out.println("searchLocationsByCountry");
        String searchCountry = "China";
        assertNotNull(rm.searchLocationsByCountry(searchCountry));
       
    }

  
    /**
     * Test of retrieveRoutes method, of class RouteSessionBean.
     */
    @Test
    public void testRetrieveRoutes() throws Exception {
        System.out.println("retrieveRoutes");
        assertNotNull(rm.retrieveRoutes());
    }

    /**
     * Test of haversineDist method, of class RouteSessionBean.
     */
    @Test
    public void testHaversineDist() throws Exception {
        System.out.println("haversineDist");
        Double lat1 = 100.0;
        Double long1 = 200.0;
        Double lat2 = 300.0;
        Double long2 = 400.0;
        String expResult = "15623.85";
        Double result = rm.haversineDist(lat1, long1, lat2, long2);
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
