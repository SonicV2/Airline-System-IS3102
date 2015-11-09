/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Location;
import APS.Entity.Route;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface RouteSessionBeanLocal {
    
    public void addRoute(String origin, String destination);
    public void deleteRoute(Long id);
    public Route getRoute(Long id);
    public List<Route> retrieveRoutes();
    public Location findLocation(String location);
    public Location getLocation(String country1);
    public List<Location> retrieveLocations();
    public List<Location> searchLocationsByCity(String searchCity);
    public List<Location> searchLocationsByCountry(String searchCountry);
    public Double haversineDist(Double lat1, Double long1, Double lat2, Double long2);
    
}
