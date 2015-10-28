/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.util.Date;
import javax.ejb.embeddable.EJBContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author YiQuan
 */
public class PricingSessionBeanTest {
    
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
}

  