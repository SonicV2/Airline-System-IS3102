/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Entity.Pilot;
import FOS.Entity.Pairing;
import FOS.Entity.Team;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface CrewSignInSessionBeanLocal {

    public Team getCCTeam(String crewName);

    public String crewSignIn(String crewName, Team team, String firstScheduleID);

    public Schedule getFirstPairingSchedule(Pairing p);

    public CabinCrew getCabinCrew(String crewName);

    public Pilot getPilot(String crewName);

    public String submitLeave(String crewName, String reason, String scheduleId);

    public Schedule getScheduleByID(String scheduleId);

    public Team getTeamByID(String teamId);

    public Schedule getScheduleBy(String flightNumber, String flightDate);

    public String submitPilotLeave(String crewName, String reason, String scheduleId);

}
