/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;
import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface PricingSessionBeanLocal {
    
    // Generate the amount of seat available which includes overbooking based on demand forecast
   public int[] generateAvailability(String flightNo, int economy, int business, int firstClass) ;
    
    // Get the price of a ticket of a specific schedule and classCode
    public double getPrice(String classCode,Schedule s);
    
    // Get the booking class code for a particular schedule and service class for a set number of ppl
    public String getClassCode(Schedule s, String serviceClass, int noPpl, boolean travelagent);
    
    // Get the current seat availability of a particular flight at a particlular time and date
    // Returns a int array of 10. There are 5 types of tickets service type. First 5 integers
    // is the total amount while the last 5 is the amount booked
    public int[] getAvail(String flightNo, Date fDate);
    
    
    //Find the seat availability of a particular flight no and time
    public SeatAvailability findSA(Date fDate,String flightNo);
    
    public String calTurnOut(String flightNo, String serviceType);
    
    
    
}
