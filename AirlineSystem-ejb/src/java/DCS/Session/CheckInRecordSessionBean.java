/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import DCS.Entity.CheckInRecord;
import Inventory.Entity.Booking;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HOULIANG
 */
@Stateless
public class CheckInRecordSessionBean implements CheckInRecordSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void addBooking(Booking booking) {
        CheckInRecord record = new CheckInRecord();
        record.create();
        record.setBookingID(booking.getId());
        em.persist(record);
    }
    
    @Override
    public void addSeat(Booking booking, String seat) {
        CheckInRecord record = findRecord(booking);
        record.setSeatNo(seat);
        booking.setSeatNumber(seat);
        em.merge(record);
        em.merge(booking);
        
    }
@Override
    public void addStatus(Booking booking, String status) {
        CheckInRecord record = findRecord(booking);
        record.setStatus(status);
        em.merge(record);
    }
@Override
    public void addboardingPassNo(Booking booking, String boardingPassNo) {
        CheckInRecord record = findRecord(booking);
        record.setBoardingPassNo(boardingPassNo);
        em.merge(record);
    }
@Override
    public void addchangeFlight(Booking booking, String changeFlight) {
        CheckInRecord record = findRecord(booking);
        record.setChangeFlight(changeFlight);
        em.merge(record);
    }
@Override
    public void addcreditCardNo(Booking booking, String creditCardNo, Date date) {
        CheckInRecord record = findRecord(booking);
        record.setCreditCardNo(creditCardNo);
        record.setPaymentDate(date);
        em.merge(record);
    }
@Override
    public void addhotelVoucher(Booking booking, String hotelVoucher) {
        CheckInRecord record = findRecord(booking);
        record.setHotelVoucher(hotelVoucher);
        em.merge(record);
    }
@Override
    public CheckInRecord findRecord(Booking booking) {
        Query q = em.createQuery("SELECT c FROM CheckInRecord c");
        List<CheckInRecord> records = q.getResultList();

        for (CheckInRecord b : records) {
            if ((b.getBookingID() + "").equals(booking.getId().toString())) {
                return b;
            } else {
                return null;
            }
        }
        return null;
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
