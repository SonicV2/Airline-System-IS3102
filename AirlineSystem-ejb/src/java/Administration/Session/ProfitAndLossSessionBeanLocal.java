/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Session;

import APS.Entity.Aircraft;
import APS.Entity.Schedule;
import Administration.Entity.ProfitAndLoss;
import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yunna
 */
@Local
public interface ProfitAndLossSessionBeanLocal {

    public List<PNR> getConfirmedPNRs(String status);

    public List<Aircraft> retrieveAircrafts();

    public double getCurrentMonthSettlement(TravelAgency travelAgency, Date date);

    public List<TravelAgency> getAllTravelAgencies();

    public List<Schedule> getSchedules();

    public List<Employee> getEmployees();

    public List<CabinCrew> getCabinCrews();

    public List<Pilot> getPilots();

    public ProfitAndLoss createProfitAndLoss(Date dateChosen);

    public ProfitAndLoss getPnLbyDate(Date chosenDate);
    
}
