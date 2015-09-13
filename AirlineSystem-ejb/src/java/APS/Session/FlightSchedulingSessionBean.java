package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.Flight;
import APS.Entity.Route;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yanlong
 */
@Stateless
public class FlightSchedulingSessionBean implements FlightSchedulingSessionBeanLocal{
   @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
   Flight flight = new Flight();
   
   @Override
   public ArrayList<Flight> optimizeSchedule (ArrayList<Flight> flights, ArrayList<Aircraft> aircrafts){
       return null;
   }
}
