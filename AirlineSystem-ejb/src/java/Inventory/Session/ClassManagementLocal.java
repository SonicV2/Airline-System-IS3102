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
public interface ClassManagementLocal {
     public void addClassCode(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru);
     
     public void deleteClassCode(String classcode);
     
     public void editClassCode(String classcode, int pricePercent, int advancedSales
    , int percentSold, String serviceClass, boolean rebook, boolean cancel, 
    int baggage, int millageAccru);
}
