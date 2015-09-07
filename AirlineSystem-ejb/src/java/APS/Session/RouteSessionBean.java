package APS.Session;

import APS.Entity.Route;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        route.create(origin, destination);
        em.persist(route);
    }
    
}
