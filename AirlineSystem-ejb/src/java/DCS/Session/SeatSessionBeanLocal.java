/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Schedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface SeatSessionBeanLocal {

    public List<String> retrieveOccupiedSeats(Schedule schedule);
    public void inputChosenE(Schedule schedule,String seat);
    public void inputChosenB(Schedule schedule,String seat);
    public void inputChosenF(Schedule schedule,String seat);
    public void occupyAll(Schedule schedule, List<String> allSeats);
     public void unOccupyAll(Schedule schedule);
}
