/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import java.util.Date;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import APS.Session.FleetSessionBeanRemote;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 *
 * @author YiQuan
 */
public class FleetSessionBeanTest {
    
    private FleetSessionBeanRemote fm = lookup();
    
    public FleetSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of acquireAircraft method, of class FleetSessionBean.
     */
    @Test
    public void testAcquireAircraft() throws Exception {
        System.out.println("acquireAircraft");
        String tailNo = "ABC999";
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 8, 1);
        Date datePurchased = cal.getTime();
        cal.set(2015, 2, 1);
        Date lastMaintained = cal.getTime();
        String aircraftTypeId = "Airbus A330-300";
        String hub = "Singapore";
        String status = "Stand-By";
        String result =fm.acquireAircraft(tailNo, datePurchased, lastMaintained, aircraftTypeId, hub, status);
        fm.retireAircraft("ABC999", "3470");
        assertEquals("Aircraft Acquired",result);
    }

    /**
     * Test of retireAircraft method, of class FleetSessionBean.
     */
    @Test
    public void testRetireAircraft() throws Exception {
        String tailNo = "ABC998";
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 8, 1);
        Date datePurchased = cal.getTime();
        cal.set(2015, 2, 1);
        Date lastMaintained = cal.getTime();
        String aircraftTypeId = "Airbus A330-300";
        String hub = "Singapore";
        String status = "Stand-By";
        fm.acquireAircraft(tailNo, datePurchased, lastMaintained, aircraftTypeId, hub, status);
        System.out.println("retireAircraft");
        String retireNo = "ABC998";
        String takeoverNo = "3470";
        String result = fm.retireAircraft(retireNo, takeoverNo);
        assertEquals("Aircraft Retired", result);
    }

    /**
     * Test of getAircraftType method, of class FleetSessionBean.
     */
   
    @Test
    public void testGetAircraft() throws Exception {
        System.out.println("getAircraft");
        String tailNo = "3470";    
        assertNotNull(fm.getAircraft(tailNo));
    }

    /**
     * Test of retrieveAircraftTypes method, of class FleetSessionBean.
     */
    @Test
    public void testRetrieveAircraftTypes() throws Exception {
        System.out.println("retrieveAircraftTypes");
        assertNotNull(fm.retrieveAircraftTypes());   
    }

    /**
     * Test of retrieveAircrafts method, of class FleetSessionBean.
     */
    @Test
    public void testRetrieveAircrafts() throws Exception {
        System.out.println("retrieveAircrafts");
        assertNotNull(fm.retrieveAircrafts());   
    }

    /**
     * Test of getReserveAircrafts method, of class FleetSessionBean.
     */
    @Test
    public void testGetReserveAircrafts() throws Exception {
        System.out.println("getReserveAircrafts");
        String status = "Stand-By";
        assertNotNull(fm.getReserveAircrafts(status));   
    }
    
    private FleetSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (FleetSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/FleetSessionBean!APS.Session.FleetSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}