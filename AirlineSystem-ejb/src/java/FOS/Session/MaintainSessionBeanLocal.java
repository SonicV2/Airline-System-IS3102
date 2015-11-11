/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Aircraft;
import FOS.Entity.GroundCrew;
import FOS.Entity.MaintainSchedule;
import FOS.Entity.MaintainanceTeam;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface MaintainSessionBeanLocal {

    public void generateAllSchedules();

    public List<MaintainSchedule> getAllMaintainSchedules();

    public void assignTeam();

    public Aircraft getAircraftByTailNo(String tailNo);

    public void generateTeam();

    public void assignMainCrews();

    public List<MaintainanceTeam> getUnassignedMaintainTeams();

    public MaintainanceTeam getCrewMaintainTeam(String username);

    public GroundCrew getGroundCrew(String username);

    public List<MaintainanceTeam> retrieveTeamId();
}
