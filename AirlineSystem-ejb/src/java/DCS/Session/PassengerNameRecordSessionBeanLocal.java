/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface PassengerNameRecordSessionBeanLocal {

    public PNR getPNR(String pnrNumber);

    public List<Booking> getBooking(String pnrNumber, String passportNumber);

    public Schedule getCheckinSchedule(String pnrNumber, String passportNumber);

    public Flight getCheckinFlight(String pnrNumber, String passportNumber);

    public String retrieveClass(String classcode);
}
