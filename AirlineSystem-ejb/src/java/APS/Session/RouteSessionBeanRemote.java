/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Location;
import APS.Entity.Route;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author YiQuan
 */
@Remote
public interface RouteSessionBeanRemote {
    public void addRoute(String origin, String destination);
    public Location findLocation(String location);

    public Route getRoute(Long id);

    public void deleteRoute(Long id);
    public List<Location> retrieveLocations();
    public List<Route> retrieveRoutes();
    public Location getLocation(String country1);
    public List<Location> searchLocationsByCountry(String searchCountry);
    public List<Location> searchLocationsByCity(String searchCity);
}
