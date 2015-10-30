package Administration.Session;

import Administration.Entity.Account;
import Administration.Entity.Entry;
import Administration.Entity.Posting;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yanlong
 */
@Stateless
public class AccountingSessionBean implements AccountingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    Account account;
    Entry entry;
    Posting posting;
    
    
    
}
