/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ClassSessionBeanTest {
    
    private ClassSessionBeanRemote cm = lookup();
    
    public ClassSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addClassCode method, of class ClassSessionBean.
     */
    @Test
    public void testAddClassCode() throws Exception {
        System.out.println("addClassCode");
        String classcode = "Z11";
        int pricePercent = 50;
        int advancedSales = 50;
        int percentSold = 50;
        String serviceClass = "Business";
        boolean rebook = false;
        boolean cancel = false;
        int baggage = 1;
        int millageAccru = 50;
        String season = "High";
        String expResult = "Fare Class Added";
        
         
        String result = cm.addClassCode(classcode, pricePercent, advancedSales, percentSold, serviceClass, rebook, cancel, baggage, millageAccru, season);
        System.out.println("Expected Result:"+ expResult);
        System.out.println("Result:"+result);
        assertEquals(expResult, result);

    }

    /**
     * Test of findClass method, of class ClassSessionBean.
     */
    @Test
    public void testFindClass() throws Exception {
        
    }

    /**
     * Test of retrieveBookingClasses method, of class ClassSessionBean.
     */
    @Test
    public void testRetrieveBookingClasses() throws Exception {
       
    }

    /**
     * Test of deleteClassCode method, of class ClassSessionBean.
     */
    @Test
    public void testDeleteClassCode() throws Exception {
        
    }

    /**
     * Test of editClassCode method, of class ClassSessionBean.
     */
    @Test
    public void testEditClassCode() throws Exception {
     
    }
    
    private ClassSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (ClassSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/ClassSessionBean!Inventory.Session.ClassSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
