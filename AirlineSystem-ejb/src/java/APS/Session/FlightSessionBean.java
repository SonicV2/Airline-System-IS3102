package APS.Session;

import APS.Entity.Flight;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yanlong
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    Flight flight;
    
    @Override
    public void addFlight(String flightNo, String flightDay, String timeslot, double flightDuration, Date startDate, int weeks){
        flight.createFlight(flightNo, flightDay, timeslot, flightDuration, startDate, weeks);
        em.persist(flight);
    }
    
    @Override
    public void deleteFlight(String flightNo){
        flight = getflight(flightNo);
        em.remove(flight);
    }
    
    @Override
    public Flight getflight(String flightNo){
         flight = new Flight();   
            try {

            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.flightNo=:flightNo");
            q.setParameter("flightNo", flightNo);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                flight = (Flight) results.get(0);

            } else {
                flight = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
            return flight;
    }
    
    
}
