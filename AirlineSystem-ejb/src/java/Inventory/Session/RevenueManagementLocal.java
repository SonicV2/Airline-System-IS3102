/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;
import Inventory.Entity.SeatAvailability;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface RevenueManagementLocal {
    
    // Generate the amount of seat available which includes overbooking based on demand forecast
    public int[] generateAvailability(int economy, int business, int firstClass);
    
    // Get the price of a ticket of a specific flight and service type
    public String getPrice(String flightNo,Date fDate, String serviceClass, int realSold);
    
    // Get the current seat availability of a particular flight at a particlular time and date
    // Returns a int array of 10. There are 5 types of tickets service type. First 5 integers
    // is the total amount while the last 5 is the amount booked
    public int[] getAvail(String flightNo, Date fDate);
    
    //Convert a string storing date and time to a date object
    public Date convertToDate(String flightDate, String flightTime);
    
    //Find the seat availability of a particular flight no and time
    public SeatAvailability findSA(Date fDate,String flightNo);
    
    
}
