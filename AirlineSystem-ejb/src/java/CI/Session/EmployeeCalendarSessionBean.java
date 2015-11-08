/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import CI.Entity.EmployeeCalendar;
import static java.util.Collections.list;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class EmployeeCalendarSessionBean implements EmployeeCalendarSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<EmployeeCalendar> retrieveCalendar(String username) {
        Employee e = retrieveEmployeeByUsername(username);
        if (e == null) {
            return null;
        }

        List<EmployeeCalendar> calendars = e.getCalendars();
        return calendars;

    }

    public Employee retrieveEmployeeByUsername(String username) {
        Query q = em.createQuery("SELECT e FROM Employee e");
        List<Employee> employees = q.getResultList();

        for (Employee e : employees) {
            if (e.getEmployeeUserName().equals(username) && (!e.getOrganizationUnit().getDepartmentName().equals("Flight Crew")
                    || !e.getOrganizationUnit().getDepartmentName().equals("Ground Crew"))) {
                return e;
            }
        }

        return null;

    }

    @Override
    public void addCalendar(String eventId, String username, Date startDate, Date endDate, String title) {

        Employee e = retrieveEmployeeByUsername(username);

        EmployeeCalendar calendar = new EmployeeCalendar();
        calendar.create(eventId, startDate, endDate, title);

        List<EmployeeCalendar> calendars = e.getCalendars();

        calendars.add(calendar);

        e.setCalendars(calendars);

        em.persist(e);

    }

    @Override
    public void removeCalendar(String username, Date startDate, Date endDate, String title) {
        Employee e = retrieveEmployeeByUsername(username);
        List<EmployeeCalendar> calendars = e.getCalendars();

        Iterator i = calendars.iterator();
        while (i.hasNext()) {
            EmployeeCalendar c = (EmployeeCalendar) i.next();
            if (c.getStartDate().equals(startDate) && c.getEndDate().equals(endDate) && c.getTitle().equals(title)) {
                i.remove();
            }
        }
       
        e.setCalendars(calendars);
        em.merge(e);

    }

    @Override
    public EmployeeCalendar retrieveCalendarBy(Date startDate, Date endDate, String title) {
        Query q = em.createQuery("SELECT e FROM EmployeeCalendar e");
        List<EmployeeCalendar> calendars = q.getResultList();

        for (EmployeeCalendar e : calendars) {
            if (e.getStartDate().equals(startDate) && e.getEndDate().equals(endDate) && e.getTitle().equals(title)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void updateCalendar(String username, String eventId, Date startDate, Date endDate, String title) {
        Employee employee = retrieveEmployeeByUsername(username);
        // EmployeeCalendar e = retrieveCalendarBy(eventId);
       

        List<EmployeeCalendar> calendars = employee.getCalendars();

        for (EmployeeCalendar c : calendars) {
            if (c.getEventId().equals(eventId)) {
                c.setStartDate(startDate);
                c.setEndDate(endDate);
                c.setTitle(title);
                em.merge(c);
            }
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
