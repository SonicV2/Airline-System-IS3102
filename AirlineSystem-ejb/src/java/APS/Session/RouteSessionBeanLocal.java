/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Location;
import APS.Entity.Route;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface RouteSessionBeanLocal {
    public void addRoute(String origin, String destination);
    public Location findLocation(String location);

    public Route getRoute(Long id);

    public void deleteRoute(Long id);
}
