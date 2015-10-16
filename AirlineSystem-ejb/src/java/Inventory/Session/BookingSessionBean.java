/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import Inventory.Entity.SeatAvailability;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Inventory.Entity.Booking;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import java.util.Random;

/**
 *
 * @author YiQuan
 */
@Stateless
public class BookingSessionBean implements BookingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    private String serviceType;

    public void bookSeats(String flightNo) {
        Query q = em.createQuery("SELECT o from SeatAvailability o WHERE o.schedule.flight.flightNo = :flightNo");
        q.setParameter("flightNo", flightNo);

        List<SeatAvailability> saList = q.getResultList();
        Date current = new Date();
        int size = saList.size();
        for (int i = 0; i < size; i++) {
            Date fTime = saList.get(i).getSchedule().getStartDate();
            if (current.after(fTime)) {
                pseudoBook(saList.get(i));
            }
        }
    }

    public void pseudoBook(SeatAvailability sa) {
        Random random = new Random();
        int economySaverBooked = sa.getEconomySaverTotal() / 2 + random.nextInt(sa.getEconomySaverTotal() / 2);
        sa.setEconomySaverBooked(economySaverBooked);
        for (int i = 0; i < economySaverBooked; i++) {
            Booking book = new Booking();
            book.YQcreateBooking("Economy Saver", sa);
            pseuduTurnOut2(book);
            em.persist(book);
        }
        int economyBasicBooked = sa.getEconomyBasicTotal() / 2 + random.nextInt(sa.getEconomyBasicTotal() / 2);
        sa.setEconomyBasicBooked(economyBasicBooked);
        for (int i = 0; i < economyBasicBooked; i++) {
            Booking book = new Booking();
            book.YQcreateBooking("Economy Basic", sa);
            pseuduTurnOut2(book);
            em.persist(book);
        }
        int economyPremiumBooked = sa.getEconomyPremiumTotal() / 2 + random.nextInt(sa.getEconomyPremiumTotal() / 2);
        sa.setEconomyPremiumBooked(economyPremiumBooked);
        for (int i = 0; i < economyPremiumBooked; i++) {
            Booking book = new Booking();
            book.YQcreateBooking("Economy Premium", sa);
            pseuduTurnOut(book);
            em.persist(book);
        }
        int businessBooked = sa.getBusinessTotal() / 2 + random.nextInt(sa.getBusinessTotal() / 2);
        sa.setBusinessBooked(businessBooked);
        for (int i = 0; i < businessBooked; i++) {
            Booking book = new Booking();
            book.YQcreateBooking("Business", sa);
            pseuduTurnOut(book);
            em.persist(book);
        }
        int firstBooked = sa.getFirstClassTotal() / 2 + random.nextInt(sa.getFirstClassTotal() / 2);
        sa.setFirstClassBooked(firstBooked);
        for (int i = 0; i < firstBooked; i++) {
            Booking book = new Booking();
            book.YQcreateBooking("First Class", sa);
            pseuduTurnOut(book);
            em.persist(book);
        }

        em.merge(sa);
    }

    public void pseuduTurnOut(Booking booking) {
        Random random = new Random();
        int roll = random.nextInt(20) + 1;
        if (roll == 1) {
            booking.setBookingStatus("Missed");
        }
        if (roll == 2 || roll == 3 || roll == 4) {
            booking.setBookingStatus("Cancelled");
        }
        if (roll > 4) {
            booking.setBookingStatus("Checked In");
        }
    }

    public void pseuduTurnOut2(Booking booking) {
        Random random = new Random();
        int roll = random.nextInt(20) + 1;
        if (roll == 1) {
            booking.setBookingStatus("Missed");
        }
        if (roll == 2) {
            booking.setBookingStatus("Cancelled");
        }
        if (roll > 2) {
            booking.setBookingStatus("Checked In");
        }
    }

    // Create a booking
    public void addBooking(double price, String serviceType,
            SeatAvailability seatAvail) {
        Booking booking = new Booking();
        Long id = seatAvail.getId();
        seatAvail = em.find(SeatAvailability.class, id);
        booking.YQcreateBooking(serviceType, seatAvail);
        em.persist(booking);
        if (serviceType.equals("Economy Saver")) {
            seatAvail.setEconomySaverBooked(seatAvail.getEconomySaverBooked() + 1);
        }
        if (serviceType.equals("Economy Basic")) {
            seatAvail.setEconomyBasicBooked(seatAvail.getEconomyBasicBooked() + 1);
        }
        if (serviceType.equals("Economy Premium")) {
            seatAvail.setEconomyPremiumBooked(seatAvail.getEconomyPremiumBooked() + 1);
        }
        if (serviceType.equals("Business")) {
            seatAvail.setBusinessBooked(seatAvail.getBusinessBooked() + 1);
        }
        if (serviceType.equals("First Class")) {
            seatAvail.setFirstClassBooked(seatAvail.getFirstClassBooked() + 1);
        }

        System.out.println(em.contains(seatAvail));
        em.flush();
    }

    

}
