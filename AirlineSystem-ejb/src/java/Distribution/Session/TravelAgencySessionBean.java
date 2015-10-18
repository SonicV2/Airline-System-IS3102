/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.TravelAgency;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author parthasarthygupta
 */
@Stateless
public class TravelAgencySessionBean implements TravelAgencySessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<TravelAgency> retrieveTravelAgencies() {
        List<TravelAgency> travelAgency = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a from TravelAgency a");

            List<TravelAgency> results = q.getResultList();
            if (!results.isEmpty()) {

                travelAgency = results;

            } else {
                travelAgency = null;
                System.out.println("no flight!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return travelAgency;
    }
    
    
}
