/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import CI.Entity.CabinCrew;
import CI.Entity.Pilot;
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

    public String reassignPilot(String crewName);

    public String assignBack(String crewName);

    public List<Pilot> getAllReservedPilot(String selectYear, String selectMonth);

    public void assignPilotSchedule(String selectYear, String selectMonth, String crewName);

    public void rejectCabinCrewLeave(String crewName);

    public void rejectPilotLeave(String crewName);

    public CabinCrew getCabinCrew(String crewName);

    public Pilot getPilot(String crewName);

    public void changeCabinCrewStatus(String crewName);

    public void changePilotStatus(String crewName);
}
