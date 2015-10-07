/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import javax.ejb.Stateless;
import Inventory.Entity.BookingClass;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author YiQuan
 */
@Stateless
public class ClassManagement implements ClassManagementLocal {
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    // Add a booking class
    public String addClassCode(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru, String season){
        BookingClass bc = new BookingClass();
        BookingClass bc1 = em.find(BookingClass.class,classcode);
        if (bc1==null){
            bc.createClass(classcode, pricePercent, advancedSales, percentSold,
                    serviceClass, rebook, cancel, baggage, millageAccru, season);
            em.persist(bc);
            return "Fare Class Added";
        }
        else{
            return "Fare Class Already Exists";
        }
        
    }
    
    // Find a booking class based on its classcode
    public BookingClass findClass(String classCode){
        BookingClass bc = em.find(BookingClass.class, classCode);
        return bc;
    }
    
    // Retrieve the list of  available booking classes
    public List<BookingClass> retrieveBookingClasses(){
        List<BookingClass> allClasses = new ArrayList<BookingClass>();
        
        try{
            Query q = em.createQuery("SELECT a from BookingClass a");
            
            List<BookingClass> results = q.getResultList();
            if (!results.isEmpty()){
                
                allClasses = results;
                
            }else
            {
                allClasses = null;
                System.out.println("No Classes!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allClasses;
    }
    
    // Delete a booking class
    public void deleteClassCode(String classcode){
        try{
            BookingClass bc = em.find(BookingClass.class, classcode);
            em.remove(bc);
        }
        catch(EntityNotFoundException ex){
            System.out.println("\nEntity Not Found" );
            System.out.println(ex);
        }
    }
    
    // Edit a booking class
    public void editClassCode(BookingClass bc){
        em.merge(bc);
    }
    
}
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

