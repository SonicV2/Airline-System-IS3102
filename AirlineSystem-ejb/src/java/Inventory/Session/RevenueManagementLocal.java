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
    
    public int[] generateAvailability(int economy, int business, int firstClass);
    public String getPrice(String flightNo,Date fDate, String serviceClass, int realSold);
    public int[] getAvail(String flightNo, Date fDate);
    public Date convertToDate(String flightDate, String flightTime);
    public SeatAvailability findSA(Date fDate,String flightNo);
    
    
}
