/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface ScheduleSessionBeanLocal {

    public void addSchedule(Date startDate, String flightNo);
    public void deleteSchedule(Long id);
    public Schedule getSchedule(Long id);
    public List<Schedule> getSchedules();
    public Schedule getScheduleByDate(Date startDate);
<<<<<<< HEAD
    public void edit(Schedule schedule);
=======
>>>>>>> ae84d48dedf5d54ded0603d07af31bc4ff1aa4e1
    public void changeFlightDays(List<Flight> flights);
    public List<Schedule> getSchedules(String tailNo);
    public Date calcEndTime(Date startTime, Flight flight);
}
