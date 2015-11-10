package CI.Session;

import CI.Entity.AccessRight;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface AccessRightSessionBeanRemote {
    
    public String addAccessRight(String accessRightName);
    public List<AccessRight> retrieveAllAccessRight();
    
}