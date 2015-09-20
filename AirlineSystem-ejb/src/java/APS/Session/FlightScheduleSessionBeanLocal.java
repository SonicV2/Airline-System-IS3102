/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface FlightScheduleSessionBeanLocal {
    public void scheduleFlights(String flightId);   
}
