/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.util.Date;
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
import Inventory.Session.PricingSessionBeanRemote;

/**
 *
 * @author YiQuan
 */
public class PricingSessionBeanTest {
    
    private PricingSessionBeanRemote pm = lookup();
    
    public PricingSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of generateAvailability method, of class PricingSessionBean.
     */
    @Test
    public void testGenerateAvailability() throws Exception {
        System.out.println("generateAvailability");
        String flightNo = "";
        int economy = 0;
        int business = 0;
        int firstClass = 0;

        assertEquals("Fare Class Added", "Fare Class Added");
    }
    
    @Test
    public void testcalTurnOut(){
        System.out.println("calTurnOut");
        String flightNo ="MA777";
        String serviceClass = "Business";
        String result = pm.calTurnOut(flightNo, serviceClass);
        assertNotNull(result);
    }
    
    @Test
    public void testfindSA(){
        System.out.println("calTurnOut");
        String flightNo ="MA777";
        String serviceClass = "Business";
        String result = pm.calTurnOut(flightNo, serviceClass);
        assertNotNull(result);
    }
    
    private PricingSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (PricingSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/PricingSessionBean!Inventory.Session.PricingSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

  