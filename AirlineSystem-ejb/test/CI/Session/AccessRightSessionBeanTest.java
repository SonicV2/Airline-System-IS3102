/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.AccessRight;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
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
        String accessRightName = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AccessRightSessionBeanLocal instance = (AccessRightSessionBeanLocal)container.getContext().lookup("java:global/classes/AccessRightSessionBean");
        instance.addAccessRight(accessRightName);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteAccessRight method, of class AccessRightSessionBean.
     */
    @Test
    public void testDeleteAccessRight() throws Exception {
        System.out.println("deleteAccessRight");
        Long accessRightID = null;
        AccessRightSessionBean instance = new AccessRightSessionBean();
        instance.deleteAccessRight(accessRightID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAccessRightID method, of class AccessRightSessionBean.
     */
    @Test
    public void testGetAccessRightID() throws Exception {
        System.out.println("getAccessRightID");
        Long accessRightID = null;
        AccessRightSessionBean instance = new AccessRightSessionBean();
        AccessRight expResult = null;
        AccessRight result = instance.getAccessRightID(accessRightID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveAllAccessRight method, of class AccessRightSessionBean.
     */
    @Test
    public void testRetrieveAllAccessRight() throws Exception {
        System.out.println("retrieveAllAccessRight");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        AccessRightSessionBeanLocal instance = (AccessRightSessionBeanLocal)container.getContext().lookup("java:global/classes/AccessRightSessionBean");
        List<AccessRight> expResult = null;
        List<AccessRight> result = instance.retrieveAllAccessRight();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
