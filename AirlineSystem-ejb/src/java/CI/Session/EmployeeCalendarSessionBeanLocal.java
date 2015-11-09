/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.EmployeeCalendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface EmployeeCalendarSessionBeanLocal {

    public List<EmployeeCalendar> retrieveCalendar(String username);

    public void addCalendar(String eventId, String username, Date startDate, Date endDate, String title);

    public EmployeeCalendar retrieveCalendarBy(Date startDate, Date endDate, String title);

    public void updateCalendar(String username, String eventId, Date startDate, Date endDate, String title);

    public void removeCalendar(String username, Date startDate, Date endDate, String title);
}
