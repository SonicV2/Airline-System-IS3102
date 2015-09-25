/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
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
     
     public String crewSignIn(String crewName, Team team);
     public Schedule getFirstPairingSchedule(Pairing p);
}
