/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Session;

import APS.Entity.Aircraft;
import Distribution.Entity.PNR;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yunna
 */
@Local
public interface AircraftProductivitySessionBeanLocal {

    public String calculateYearsUsed(Aircraft aircraft);

    public double calculateTotalDistance(Aircraft aircraft);

    public double calculateTotalTravelTime(Aircraft aircraft);

    public List<PNR> getConfirmedPNRs(String status);

    public double calculateTotalProfit(Aircraft aircraft);
    
}
