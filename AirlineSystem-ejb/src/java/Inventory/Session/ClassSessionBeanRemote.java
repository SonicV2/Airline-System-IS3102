/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;
import Inventory.Entity.BookingClass;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author YiQuan
 */
@Remote
public interface ClassSessionBeanRemote {
    
     // Add a booking class
     public String addClassCode(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru, String season);
     
     // Delete a booking class
     public void deleteClassCode(String classcode);
     
     // Edit a booking class
     public void editClassCode(BookingClass bc);
     
     // Retrieve a list of booking classes
     public List<BookingClass> retrieveBookingClasses();
     
     // Find a specific booking class using its classcode
     public BookingClass findClass(String classCode);
    
}
