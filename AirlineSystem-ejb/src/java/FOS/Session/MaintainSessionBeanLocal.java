/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.MaintainSchedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface MaintainSessionBeanLocal {

    public void generateAllSchedules();

    public List<MaintainSchedule> getAllMaintainSchedules();

    public void assignTeam();

}
