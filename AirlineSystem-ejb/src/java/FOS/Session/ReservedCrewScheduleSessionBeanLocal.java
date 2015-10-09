/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import CI.Entity.CabinCrew;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface ReservedCrewScheduleSessionBeanLocal {

    public List<CabinCrew> getAllReservedCrew(String selectYear, String selectMonth);

    public void assignSchedule(String selectYear, String selectMonth, String crewName);

    public String reassign(String crewName);

    public String assignBack(String crewName);
}
