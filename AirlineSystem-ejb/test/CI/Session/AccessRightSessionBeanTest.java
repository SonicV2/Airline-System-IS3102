/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import APS.Session.FleetSessionBeanRemote;
import CI.Entity.AccessRight;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author HOULIANG
 */
public class AccessRightSessionBeanTest {
    
    private AccessRightSessionBeanRemote am = lookup();
    
    public AccessRightSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addAccessRight method, of class AccessRightSessionBean.
     */
    @Test
    public void testAddAccessRight() throws Exception {
        System.out.println("addAccessRight");
        String name = "testAccessRight";
        String result = am.addAccessRight(name);
        assertEquals("Access Right Added", result);
    }


    /**
     * Test of retrieveAllAccessRight method, of class AccessRightSessionBean.
     */
    @Test
    public void testRetrieveAllAccessRight() throws Exception {
        System.out.println("retrieveAllAccessRight");
        assertNotNull(am.retrieveAllAccessRight());
    }
    
    private AccessRightSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (AccessRightSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/AccessRightSessionBean!CI.Session.AccessRightSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}