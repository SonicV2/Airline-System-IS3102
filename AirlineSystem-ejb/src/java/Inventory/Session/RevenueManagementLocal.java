/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface RevenueManagementLocal {
    public void createAvailability(String flightNo, String flightDate, 
            String flightTime);
    
    public int[] generateAvailability(int economy, int business, int firstClass);
    
    
    
}
