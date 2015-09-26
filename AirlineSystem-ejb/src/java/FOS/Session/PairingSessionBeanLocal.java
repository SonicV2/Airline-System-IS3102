/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.Pairing;
import FOS.Entity.PairingPolicy;
import FOS.Entity.Team;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface PairingSessionBeanLocal {

    public void legMain(String selectMonth);

    public void setPolicy();

    public PairingPolicy retrievePolicy();

    void changePolicy(int maxLeg, int maxFlight, int minStopTime);

    public List<Pairing> getPairings();

    public Pairing getPairingByID(String id);

    public Team generateTeam(Pairing pairing);

//    public List<List<Pairing>> addMonthlyPairing(List<Pairing> pairing);

}
