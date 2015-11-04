/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import DCS.Entity.BaggageTag;
import Distribution.Entity.Baggage;
import Inventory.Entity.Booking;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface BaggageSessionBeanLocal {

    public String addBaggage(Booking booking, Double baggageWeight, Double totalAllowedWeights);

    public int retrieveNumberOfBaggageAllowed(String classcode);

    public List<Baggage> retrieveAllBaggages(Booking booking);

    public double retrieveTotalBaggageWeights(Booking booking);

    public double calcualtePenalty(String departure, String destination, double allowed, double totalWeight);
    public String bandSearch(String city);
    
    public void addExtraBaggage(Booking booking, double baggageWeight, double extra);
    
   
}


