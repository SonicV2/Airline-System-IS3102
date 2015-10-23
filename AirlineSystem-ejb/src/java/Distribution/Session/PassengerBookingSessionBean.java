/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import APS.Entity.Schedule;
import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class PassengerBookingSessionBean implements PassengerBookingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public boolean isPassengerAFrequentFlyer(long customerId) {
        List<Customer> allCustomers = new ArrayList();
        allCustomers = getAllCustomers();
        if (allCustomers != null) {
            for (Customer eachCustomer : allCustomers) {
                if (eachCustomer.getId() == customerId) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a from Customer a");

            List<Customer> results = q.getResultList();
            if (!results.isEmpty()) {

                allCustomers = results;

            } else {
                allCustomers = null;
                System.out.println("no Customers!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return allCustomers;
    }

    @Override
    public Customer getCustomerByCustomerId(long customerId) {
        if (isPassengerAFrequentFlyer(customerId)) {
            List<Customer> allCustomers = getAllCustomers();
            for (Customer eachCustomer : allCustomers) {
                if (eachCustomer.getId() == customerId) {
                    return eachCustomer;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public Booking createBooking(double price, SeatAvailability seatAvail, String flightNo, Date flightDate, String bookingStatus, String classCode, String serviceType, String travellerTitle, String travellerFirstName, String travellerLastName, String passportNumber, String nationality, long customerId, boolean isChild, boolean boughtInsurance, double insuranceFare, String foodSelection) {

        Booking booking = new Booking();
        booking.setPrice(price);
        booking.setSeatAvail(seatAvail);
        booking.setFlightDate(flightDate);
        booking.setFlightNo(flightNo);
        booking.setBookingStatus(bookingStatus);
        booking.setClassCode(classCode);
        booking.setServiceType(serviceType);
        booking.setTravellerTitle(travellerTitle);
        booking.setTravellerFristName(travellerFirstName);
        booking.setTravellerLastName(travellerLastName);
        booking.setPassportNumber(passportNumber);
        booking.setNationality(nationality);
        if (customerId != 0) {
            booking.setCustomerId(customerId);
        }
        booking.setIsChild(isChild);
        booking.setBoughtInsurance(boughtInsurance);
        booking.setInsuranceFare(insuranceFare);
        booking.setFoodSelection(foodSelection);
        return booking;
    }

    @Override
    public PNR createPNR(int noOfTravellers, String email, String contactNo, String pnrStatus, Double totalPrice, Date dateOfBooking, Date dateOfConfirmation, String bookingAvenue) {
        PNR pnr = new PNR();
        String pnrNo = generatePNRNumber();
        pnr.createPNR(pnrNo, noOfTravellers, email, contactNo, pnrStatus, totalPrice, dateOfBooking, dateOfConfirmation, bookingAvenue);
        pnr.setBookings(new ArrayList<Booking>());
        return pnr;
    }

    @Override
    public String generatePNRNumber() {
        String pnr = "";
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 5; i++) {
            pnr += possible.charAt((int) Math.floor(Math.random() * possible.length()));
        }
        return pnr;
    }

    @Override
    public void persistBookingAndPNR(PNR pnr, List<Booking> bookings, Customer primaryCustomer) {

        int noOfSeatsBooked;
        String serviceType;

        for (Booking eachBooking : bookings) {
            eachBooking.setPnr(pnr);
            SeatAvailability seatAvailForBooking = new SeatAvailability();
            seatAvailForBooking = eachBooking.getSeatAvail();
            serviceType = eachBooking.getServiceType();

            if (serviceType.equalsIgnoreCase("Economy Saver")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomySaverBooked();
                seatAvailForBooking.setEconomySaverBooked(noOfSeatsBooked + 1);
            } else if (serviceType.equalsIgnoreCase("Economy Basic")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomyBasicBooked();
                seatAvailForBooking.setEconomyBasicBooked(noOfSeatsBooked + 1);
            } else if (serviceType.equalsIgnoreCase("Economy Premium")) {
                noOfSeatsBooked = seatAvailForBooking.getEconomyPremiumBooked();
                seatAvailForBooking.setEconomyPremiumBooked(noOfSeatsBooked + 1);
            } else if (serviceType.equalsIgnoreCase("Business")) {
                noOfSeatsBooked = seatAvailForBooking.getBusinessBooked();
                seatAvailForBooking.setBusinessBooked(noOfSeatsBooked + 1);
            } else if (serviceType.equalsIgnoreCase("First Class")) {
                noOfSeatsBooked = seatAvailForBooking.getFirstClassBooked();
                seatAvailForBooking.setFirstClassBooked(noOfSeatsBooked + 1);
            }
            em.merge(seatAvailForBooking);
        }
        pnr.setBookings(bookings);
        em.persist(pnr);

        if (primaryCustomer != null) {
            List<PNR> existingCustomerPNRs = primaryCustomer.getPnrs();
            existingCustomerPNRs.add(pnr);
            primaryCustomer.setPnrs(existingCustomerPNRs);
            em.merge(primaryCustomer);
        }

    }

    //increase SeatAvailablity --merge, changeBookingStatus to Cancelled for all bookings - merge booking, remove pnr from customer, Delete PNR
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
    public List<Schedule> getDepartureSchedules(List<Schedule> selectedSchedules, boolean isReturnDateSet
    ) {
        int noOfSelectedSchedules = selectedSchedules.size();
        List<Schedule> departureSchedules = new ArrayList();

        if (!isReturnDateSet) {
            return selectedSchedules;
        } //Return jouney selected
        else {
            if (noOfSelectedSchedules == 2) {
                departureSchedules.add(selectedSchedules.get(0));
                return departureSchedules;
            } else if (noOfSelectedSchedules == 4) {
                departureSchedules.add(selectedSchedules.get(0));
                departureSchedules.add(selectedSchedules.get(1));
                return departureSchedules;
            }
        }
        return departureSchedules;
    }

    @Override
    public List<Schedule> getReturnSchedules(List<Schedule> selectedSchedules) {
        int noOfSelectedSchedules = selectedSchedules.size();
        List<Schedule> returnSchedules = new ArrayList();
        if (noOfSelectedSchedules == 2) {
            returnSchedules.add(selectedSchedules.get(1));
            return returnSchedules;
        } else if (noOfSelectedSchedules == 4) {
            returnSchedules.add(selectedSchedules.get(2));
            returnSchedules.add(selectedSchedules.get(3));
            return returnSchedules;
        }
        return returnSchedules;
    }

    @Override
    public PNR getPNR(String pnrID) {
        PNR pnr = new PNR();
        try {

            Query q = em.createQuery("SELECT a FROM PNR " + "AS a WHERE a.pnrID=:pnrID");
            q.setParameter("pnrID", pnrID);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                pnr = (PNR) results.get(0);

            } else {
                pnr = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        if (pnr != null && pnr.getPnrStatus().equals("Cancelled")) {
            pnr = null;
        }

        return pnr;
    }

}
