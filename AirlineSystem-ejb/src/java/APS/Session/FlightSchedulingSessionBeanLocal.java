/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.Flight;
import java.util.ArrayList;
import javax.ejb.Local;

/**
 *
 * @author Family
 */
@Local
public interface FlightSchedulingSessionBeanLocal {

    public ArrayList<Flight> optimizeSchedule(ArrayList<Flight> flights, ArrayList<Aircraft> aircrafts);
    
}
