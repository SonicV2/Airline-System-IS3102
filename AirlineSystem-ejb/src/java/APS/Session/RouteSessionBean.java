package APS.Session;

import APS.Entity.Location;
import APS.Entity.Route;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HOULIANG
 */
@Stateless
public class RouteSessionBean implements RouteSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    Route route=new Route();
    
    @Override
    public void addRoute(String origin, String destination){
        Location start = findLocation(origin);
        Location end = findLocation(destination);
        System.out.println(start.getName());
        double distance = haversineDist(start.getLatitude(),start.getLongitude(),end.getLatitude(),end.getLongitude());              
        route.create(origin,destination,distance);
        em.persist(route);
    }
    @Override
    public void deleteRoute(Long id){
        route = getRoute(id);
        em.remove(route);
    }
    
    @Override
    public Route getRoute(Long id){
        route = new Route();   
            try {

            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.id=:id");
            q.setParameter("id", id);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                route = (Route) results.get(0);

            } else {
                route = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
            return route;
    }
    
    @Override
    public Location findLocation(String location){ 
         Location locationEntity = new Location();   
            try {

            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.IATA=:IATA");
            q.setParameter("IATA", location);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                locationEntity = (Location) results.get(0);

            } else {
                locationEntity = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
            return locationEntity;
    }
    
    public static double haversineDist(double lat1, double long1, double lat2, double long2) {
    double earthRadius = 6372.8; //kilometers
    double dLat = Math.toRadians(lat2-lat1);
    double dLong = Math.toRadians(long2-long1);
    double a = Math.pow(Math.sin(dLat/2),2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.pow(Math.sin(dLong/2),2);
    double c = 2 * Math.asin(Math.sqrt(a));
    return earthRadius * c;
    }    
}
