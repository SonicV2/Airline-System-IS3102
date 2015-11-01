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
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface CrewAssignSessionBeanLocal {
    public List<Pilot> getUnassignCaptains ();
     public List<Pilot> getUnassignFOs ();
     public List<CabinCrew> getUnassignLSs ();
     public List<CabinCrew> getUnassignFSs ();
     public List<CabinCrew> getUnassignSs ();
     
}
