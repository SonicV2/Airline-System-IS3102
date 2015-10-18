/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import Distribution.Entity.TravelAgency;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface TravelAgencySessionBeanLocal {

    public List<TravelAgency> retrieveTravelAgencies();
    
}
