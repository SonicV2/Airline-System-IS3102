/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.PairingPolicy;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface PairingSessionBeanLocal {
   public ArrayList<ArrayList<String>> legMain();
     public void setPolicy();

    public PairingPolicy retrievePolicy();

    void changePolicy(int maxLeg, int maxFlight, int minStopTime);
}
