/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.OrganizationUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author YiQuan
 */
public class DepartmentSessionBeanTest {
    
    private DepartmentSessionBeanRemote dm = lookup();
    
    @Rule
    public ExpectedException thrown = ExpectedException.none(); 
    
    public DepartmentSessionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addDepartment method, of class DepartmentSessionBean.
     */
    @Test
    public void testAddDepartment() throws Exception {
        System.out.println("addDepartment");
        String departName = "testDepartment";
        String departLocation = "Singapore";
        String result = dm.addDepartment(departName, departLocation);
    }
    
    @Test
    public void testGetDepartment() throws Exception {
        System.out.println("getDepartment");
        String deptName = "IT";
        assertNotNull(dm.getDepartment(deptName));        
    }


    /**
     * Test of updateOrgUnit method, of class DepartmentSessionBean.
     */
    @Test
    public void testUpdateOrgUnit() throws Exception {
        System.out.println("updateOrgUnit");
       String deptName ="testDepartment";
       OrganizationUnit ou1 = dm.getDepartment(deptName);
       OrganizationUnit ou2 = ou1;
       ou2.setLocation("Japan");
       String result = dm.updateOrgUnit(ou1, ou2);
       assertEquals("Updated successfully", result);
       
    }
    
    @Test
    public void testUpdateOrgUnitException() throws Exception {
        System.out.println("updateOrgUnit");
        OrganizationUnit ou1 = new OrganizationUnit();
        ou1.setDepartmentID(Long.valueOf("111"));
        try{
        dm.updateOrgUnit(ou1, ou1);
        }
        catch(EJBException ex){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unknown Organization id");
        throw ex.getCausedByException();
        }
    }
    
    

    /**
     * Test of deleteOrgUnit method, of class DepartmentSessionBean.
     */
    @Test
    public void testDeleteOrgUnit() throws Exception {
        System.out.println("deleteOrgUnit");
        String dept = "Finance";
        String result = dm.deleteOrgUnit(dept);
        assertEquals("Cannot Delete!",result);
        
        dept= "testDepartment";
        result = dm.deleteOrgUnit(dept);
        assertEquals("Delete successful!",result);
        
    }

    /**
     * Test of getDepartmentUseID method, of class DepartmentSessionBean.
     */
    @Test
    public void testGetDepartmentUseID() throws Exception {
        System.out.println("getDepartmentUseID");
        Long deptID = Long.valueOf("24815754651863");
        assertNotNull(dm.getDepartmentUseID(deptID));
    }

    /**
     * Test of retrive method, of class DepartmentSessionBean.
     */
    @Test
    public void testRetrive() throws Exception {
        System.out.println("retrive");
        assertNotNull(dm.retrive());
    }

    /**
     * Test of retrieveAllDepts method, of class DepartmentSessionBean.
     */
    @Test
    public void testRetrieveAllDepts() throws Exception {
        System.out.println("retrieveAllDepts");
        assertNotNull(dm.retrieveAllDepts());
    }

    

    /**
     * Test of searchEmployee method, of class DepartmentSessionBean.
     */
    @Test
    public void testSearchEmployee() throws Exception {
        System.out.println("searchEmployee");
        String staffID = "S0001";
        assertNotNull(dm.searchEmployee(staffID));
    }

    /**
     * Test of changeDepartment method, of class DepartmentSessionBean.
     */
    @Test
    public void testChangeDepartment() throws Exception {
        System.out.println("changeDepartment");
        String staffID = "S0001";
        String deptNameCom = "FINANCE(";
        String deptNameOldCom = "IT("; 
        String expResult = "Successful!";
        String result = dm.changeDepartment(staffID, deptNameCom, deptNameOldCom);
        assertEquals(expResult, result);
    }

    

    /**
     * Test of getDepartment method, of class DepartmentSessionBean.
     */
    
  
    
    private DepartmentSessionBeanRemote lookup() 
    {
        try 
        {
            Context c = new InitialContext();
            return (DepartmentSessionBeanRemote) c.lookup("java:global/AirlineSystem-ejb/DepartmentSessionBean!CI.Session.DepartmentSessionBeanRemote");
        }
        catch (NamingException ne)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
