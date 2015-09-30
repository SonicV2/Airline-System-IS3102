package APS.Session;

import APS.Entity.Location;
import APS.Entity.Route;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yunna
 */
@Stateless
public class RouteSessionBean implements RouteSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    Route route; 
    Location location;
    
    //Add new route entity
    @Override
    public void addRoute(String origin, String destination){
        route=new Route();
        Location start = findLocation(origin);
        Location end = findLocation(destination);
        System.out.println(start.getName());
        Double distance = haversineDist(start.getLatitude(),start.getLongitude(),end.getLatitude(),end.getLongitude());              
        route.create(start.getIATA(), end.getIATA(), start.getCountry(), end.getCountry(), start.getCity(), end.getCity(), distance);
        em.persist(route);
    }
    
    //Delete existing route entity
    @Override
    public void deleteRoute(Long routeId){
        route = getRoute(routeId);
        em.remove(route);
    }
    
    //Get a specific route with route id
    @Override
    public Route getRoute(Long id){
        route = new Route();   
            try {

            Query q = em.createQuery("SELECT a FROM Route " + "AS a WHERE a.routeId=:routeId");
            q.setParameter("routeId", id);

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
    
    //Get a specific location with the country name
    @Override
    public Location getLocation(String country1){
        location = new Location();   
            try {

            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.country=:country");
            q.setParameter("country", country1);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                location = (Location) results.get(0);

            } else {
                location = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
            return location;
    }
    
    //Get a specific location with IATA
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
    
    //Get all the existing locations
    public List<Location> retrieveLocations(){
        List<Location> allLocations = new ArrayList<Location>();
        
        try{
            Query q = em.createQuery("SELECT a from Location a");
            
            List<Location> results = q.getResultList();
            if (!results.isEmpty()){
                
                allLocations = results;
                
            }else
            {
                allLocations = null;
                System.out.println("no location!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allLocations;
    }
    
    //Get all the existing locations from same country
    public List<Location> searchLocationsByCountry(String searchCountry){
        
        List<Location> allLocations = new ArrayList<Location>();
        
        try{
            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.country=:country");
            q.setParameter("country", searchCountry);
            
            List<Location> results = q.getResultList();
            if (!results.isEmpty()){
                
                allLocations = results;
                
            }else
            {
                allLocations = null;
                System.out.println("no location!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allLocations;
    }
    
    //Get all the existing locations from same city
    public List<Location> searchLocationsByCity(String searchCity){
        
        List<Location> allLocations = new ArrayList<Location>();
        
        try{
            Query q = em.createQuery("SELECT a FROM Location " + "AS a WHERE a.city=:city");
            q.setParameter("city", searchCity);
            
            List<Location> results = q.getResultList();
            if (!results.isEmpty()){
                
                allLocations = results;
                
            }else
            {
                allLocations = null;
                System.out.println("no location!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allLocations;
    }
    
    //Retrieve all the existing routes
    public List<Route> retrieveRoutes(){
        List<Route> allRoutes = new ArrayList<Route>();
        
        try{
            Query q = em.createQuery("SELECT a from Route a");
            
            List<Route> results = q.getResultList();
            if (!results.isEmpty()){
                
                allRoutes = results;
                
            }else
            {
                allRoutes = null;
                System.out.println("no route!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return allRoutes;
    }
    
    //Calculate the distance between two airports, using latitudes and longtitudes
    public static Double haversineDist(Double lat1, Double long1, Double lat2, Double long2) {
    Double earthRadius = 6372.8; //kilometers
    Double dLat = Math.toRadians(lat2-lat1);
    Double dLong = Math.toRadians(long2-long1);
    Double a = Math.pow(Math.sin(dLat/2),2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.pow(Math.sin(dLong/2),2);
    Double c = 2 * Math.asin(Math.sqrt(a));
    return earthRadius * c;
    }    
}
