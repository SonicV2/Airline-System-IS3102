/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.Pairing;
import FOS.Entity.PairingPolicy;
import FOS.Entity.Team;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface A380PairingSessionBeanLocal {

    public void legMain(String selectMonth, String selectYear);

    public List<Pairing> getPairings();

    public Pairing getPairingByID(String id);

    public Team generateA380Team(Pairing pairing);

    public void setPolicy();

    public void changePolicy(int maxLeg, int maxFlight, int minStopTime);

    public PairingPolicy retrievePolicy();

    public List<Pairing> filterPairings(String selectYear, String selectMonth);
}
