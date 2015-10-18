/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import APS.Entity.Flight;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author parthasarthygupta
 */
@Stateless
public class TravelAgencySessionBean implements TravelAgencySessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public void persistTravelAgency(TravelAgency travelAgency) {
        em.persist(travelAgency);
    }

    @Override
    public void resetCreditsAndCommission(TravelAgency travelAgency) {
        travelAgency.setCommission(0.0);
        travelAgency.setCurrentCredit(travelAgency.getMaxCredit());
        em.merge(travelAgency);
        em.flush();
    }

    @Override
    public void changeCreditLimit(TravelAgency travelAgency, double newLimit) {
        travelAgency.setMaxCredit(newLimit);
        em.merge(travelAgency);
        em.flush();
    }

    @Override
    public List<PNR> retrievePendingPNRs(TravelAgency travelAgency) {
        if (travelAgency.getPnrs() == null || travelAgency.getPnrs().isEmpty()) {
            return null;
        } else {
            List<PNR> pendingPNRs = new ArrayList();
            for (PNR eachTravelAgencyPNR : travelAgency.getPnrs()) {
                if (eachTravelAgencyPNR.getPnrStatus().equalsIgnoreCase("Pending")) {
                    pendingPNRs.add(eachTravelAgencyPNR);
                }
            }
            return pendingPNRs;
        }
    }

    @Override
    public int noOfDaysSinceDate(Date date) {
        Date currentDate = new Date();
        long diff = currentDate.getTime() - date.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public void deletePNR(PNR pnr) {
        SeatAvailability seatAvailForBooking = new SeatAvailability();
        String serviceType;
        int noOfSeatsBooked;

        for (Booking eachPnrBooking : pnr.getBookings()) {
            seatAvailForBooking = eachPnrBooking.getSeatAvail();
            serviceType = eachPnrBooking.getServiceType();
            if (serviceType.equalsIgnoreCase("Economy Saver")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomySaverBooked();
                seatAvailForBooking.setEconomySaverBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("Economy Basic")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomyBasicBooked();
                seatAvailForBooking.setEconomyBasicBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("Economy Premium")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomyPremiumBooked();
                seatAvailForBooking.setEconomyPremiumBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("Business")) {
                noOfSeatsBooked = seatAvailForBooking.getBusinessBooked();
                seatAvailForBooking.setBusinessBooked(noOfSeatsBooked - 1);
            } else if (serviceType.equalsIgnoreCase("First Class")) {
                noOfSeatsBooked = seatAvailForBooking.getFirstClassBooked();
                seatAvailForBooking.setFirstClassBooked(noOfSeatsBooked - 1);
            }
            seatAvailForBooking.getBookings().remove(eachPnrBooking);
            em.merge(seatAvailForBooking);
            eachPnrBooking.setBookingStatus("Cancelled");
            eachPnrBooking.setPnr(null);
            em.merge(eachPnrBooking);
        }
        pnr.setPnrStatus("Cancelled");
        em.merge(pnr);
        em.flush();
    }

    @Override
    public void deletePendingPNRs() {
        List<PNR> pnrsForEachTravelAgency = new ArrayList();
        List<TravelAgency> allTravelAgencies = getAllTravelAgencies();
        if (allTravelAgencies != null) {
            for (TravelAgency eachTravelAgency : allTravelAgencies) {
                pnrsForEachTravelAgency = eachTravelAgency.getPnrs();
                if (pnrsForEachTravelAgency != null && !pnrsForEachTravelAgency.isEmpty()) {
                    for (PNR eachPNR : pnrsForEachTravelAgency) {
                        if (eachPNR.getPnrStatus().equalsIgnoreCase("Pending") && noOfDaysSinceDate(eachPNR.getDateOfBooking()) > 14) {
                            deletePNR(eachPNR);
                        }
                    }

                }
            }
        }

    }

    @Override
    public List<TravelAgency> getAllTravelAgencies() {
        List<TravelAgency> allTravelAgencies = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a from TravelAgency a");

            List<TravelAgency> results = q.getResultList();
            if (!results.isEmpty()) {

                allTravelAgencies = results;

            } else {
                allTravelAgencies = null;
                System.out.println("no travel agencies!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allTravelAgencies;
    }

}
