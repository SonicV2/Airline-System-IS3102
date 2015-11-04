/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.AccessRight;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author YiQuan
 */
@Remote
public interface AccessRightSessionBeanRemote {
    public String addAccessRight(String accessRightName);
    public List<AccessRight> retrieveAllAccessRight();
}
