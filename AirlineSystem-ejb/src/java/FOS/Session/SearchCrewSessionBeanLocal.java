/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import FOS.Entity.Team;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface SearchCrewSessionBeanLocal {

    public List<Pilot> getAllPilots();

    public List<CabinCrew> getAllCCs();

    public List<Team> getAllTeam();

    public List<CabinCrew> getLeaveCabinCrew();

    public List<Pilot> getLeavePilot();
}
