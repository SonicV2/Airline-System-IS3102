/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import DCS.Entity.CheckInRecord;
import Inventory.Entity.Booking;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author HOULIANG
 */
@Local
public interface CheckInRecordSessionBeanLocal {

    public void addBooking(Booking booking);

    public void addSeat(Booking booking, String seat,int upgrade, double upgradeCosts);

    public void addStatus(Booking booking, String status);

    public void addboardingPassNo(Booking booking, String boardingPassNo);

    public void addchangeFlight(Booking booking, String changeFlight);

    public void addcreditCardNo(Booking booking, String creditCardNo, Date date);

    public void addhotelVoucher(Booking booking, String hotelVoucher);

    public CheckInRecord findRecord(Booking booking);

}
