/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Flight;
import DCS.Entity.BaggageTag;
import Distribution.Entity.Baggage;
import Inventory.Entity.Booking;
import Inventory.Entity.BookingClass;
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
public class BaggageSessionBean implements BaggageSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private static int tagNumber = 12345;

    @Override
    public void addExtraBaggage(Booking booking, double baggageWeight, double extra) {
        List<Baggage> baggages = booking.getBaggages();

        if (baggages.isEmpty()) {
            Baggage baggage = new Baggage();
            baggage.createBaggage(baggageWeight);

            baggages.add(baggage);
            booking.setBaggages(baggages);

            baggage.setBooking(booking);
            baggage.setBaggageStatus(extra + "");

            //em.merge(booking);
            BaggageTag bagTag = new BaggageTag();
            String arrivalCity = findArrivalCityIATA(booking);

            bagTag.create(generateTagNumber(), booking.getFlightNo(), booking.getFlightDate(), arrivalCity, booking.getServiceType());
            baggage.setBaggageTag(bagTag);
            baggage.setBaggageStatus("Confrimed(Overweight)");
            bagTag.setBagStatus("Confrimed(Overweight)");
            em.merge(baggage);
        } else {
            Baggage baggage = new Baggage();
            baggage.createBaggage(baggageWeight);

            baggages.add(baggage);
            booking.setBaggages(baggages);

            baggage.setBooking(booking);
            baggage.setBaggageStatus(extra + "");

            BaggageTag bagTag = new BaggageTag();
            String arrivalCity = findArrivalCityIATA(booking);

            bagTag.create(generateTagNumber(), booking.getFlightNo(), booking.getFlightDate(), arrivalCity, booking.getServiceType());
            baggage.setBaggageTag(bagTag);
            baggage.setBaggageStatus("Confrimed(Overweight)");
            bagTag.setBagStatus("Confrimed(Overweight)");
            em.merge(baggage);
            em.flush();

        }

    }

    @Override
    public String addBaggage(Booking booking, Double baggageWeight, Double totalAllowedWeights) {

        double weights = retrieveTotalBaggageWeights(booking);

        if ((weights + baggageWeight) > totalAllowedWeights) {
            return "excess";
        }

        List<Baggage> baggages = booking.getBaggages();

        if (baggages.isEmpty()) {
            Baggage baggage = new Baggage();
            baggage.createBaggage(baggageWeight);

            baggages.add(baggage);
            booking.setBaggages(baggages);

            baggage.setBooking(booking);

            //em.merge(booking);
            BaggageTag bagTag = new BaggageTag();
            String arrivalCity = findArrivalCityIATA(booking);

            bagTag.create(generateTagNumber(), booking.getFlightNo(), booking.getFlightDate(), arrivalCity, booking.getServiceType());
            baggage.setBaggageTag(bagTag);
            baggage.setBaggageStatus("Confrimed");
            bagTag.setBagStatus("Confirmed");
            em.merge(baggage);
            return "good";
        } else {
            Baggage baggage = new Baggage();
            baggage.createBaggage(baggageWeight);

            baggages.add(baggage);
            booking.setBaggages(baggages);

            baggage.setBooking(booking);

            BaggageTag bagTag = new BaggageTag();
            String arrivalCity = findArrivalCityIATA(booking);

            bagTag.create(generateTagNumber(), booking.getFlightNo(), booking.getFlightDate(), arrivalCity, booking.getServiceType());
            baggage.setBaggageTag(bagTag);
            baggage.setBaggageStatus("Confrimed");
            bagTag.setBagStatus("Confirmed");
            em.merge(baggage);
            em.flush();
            return "good";

        }

    }

    @Override
    public List<Baggage> retrieveAllBaggages(Booking booking) {
        em.flush();
        List<Baggage> baggages = booking.getBaggages();
        return baggages;
    }

    @Override
    public double retrieveTotalBaggageWeights(Booking booking) {
        double totalWeights = 0.0;
        List<Baggage> baggages = booking.getBaggages();
        if (baggages.isEmpty()) {
            return totalWeights;
        }

        for (Baggage b : baggages) {
            totalWeights += b.getBaggageWeight();
        }
        return totalWeights;
    }

    public String findArrivalCityIATA(Booking booking) {

        String flightNo = booking.getFlightNo();
        Flight flight = getFlightByFlightNo(flightNo);

        return (flight.getRoute().getDestinationIATA());

    }

    public Flight getFlightByFlightNo(String flightNo) {

        Query q = em.createQuery("SELECT f FROM Flight f");

        List<Flight> flights = q.getResultList();

        for (Flight f : flights) {
            if (f.getFlightNo().equals(flightNo)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public double calcualtePenalty(String departure, String destination, double allowed, double totalWeight) {
        double exceed = (totalWeight - allowed);
        System.out.println("AAAAAA exceed" + exceed);
        double penalty = 0.0;
        String dept = bandSearch(departure);
        System.out.println("AAAAAA dept" + dept);

        String dest = bandSearch(destination);
        System.out.println("AAAAAA dest" + dest);

        if (departure.equals("Singapore")) {
            System.out.println("AAAAAA in");
            if (dest.equals("band1")) {
                penalty += exceed * 8.0;
            } else if (dest.equals("band2")) {
                penalty += exceed * 12.0;
            } else if (dest.equals("band3")) {
                penalty += exceed * 30.0;
            } else if (dest.equals("band4")) {
                penalty += exceed * 55.0;
            }

        } else if (dept.equals("band1") && destination.equals("Singapore")) {
            penalty += exceed * 8.0;

        } else if (dept.equals("band1") && dest.equals("band1")) {
            penalty += exceed * 16.0;

        } else if (dept.equals("band1") && dest.equals("band2")) {
            penalty += exceed * 20.0;
        } else if (dept.equals("band1") && dest.equals("band3")) {
            penalty += exceed * 38.0;
        } else if (dept.equals("band1") && dest.equals("band3")) {
            penalty += exceed * 68.0;

        } else if (dept.equals("band2") && destination.equals("Singapore")) {
            penalty += exceed * 12.0;
        } else if (dept.equals("band2") && dest.equals("band1")) {
            penalty += exceed * 20.0;

        } else if (dept.equals("band2") && dest.equals("band2")) {
            penalty += exceed * 24.0;
        } else if (dept.equals("band2") && dest.equals("band3")) {
            penalty += exceed * 42.0;
        } else if (dept.equals("band2") && dest.equals("band3")) {
            penalty += exceed * 67.0;
        } else if (dept.equals("band3") && destination.equals("Singapore")) {
            penalty += exceed * 30.0;
        } else if (dept.equals("band3") && dest.equals("band1")) {
            penalty += exceed * 38.0;

        } else if (dept.equals("band3") && dest.equals("band2")) {
            penalty += exceed * 42.0;
        } else if (dept.equals("band3") && dest.equals("band3")) {
            penalty += exceed * 60.0;
        } else if (dept.equals("band3") && dest.equals("band3")) {
            penalty += exceed * 85.0;
        } else if (dept.equals("band4") && destination.equals("Singapore")) {
            penalty += exceed * 55.0;
        } else if (dept.equals("band4") && dest.equals("band1")) {
            penalty += exceed * 63.0;

        } else if (dept.equals("band4") && dest.equals("band2")) {
            penalty += exceed * 67.0;
        } else if (dept.equals("band4") && dest.equals("band3")) {
            penalty += exceed * 85.0;
        } else if (dept.equals("band4") && dest.equals("band3")) {
            penalty += exceed * 110.0;
        }

        return penalty;
    }

    @Override
    public String bandSearch(String city) {
        String band = "";

        String[] band1 = {"Brunei", "Cambodia", "Indonesia", "Laos", "Malaysia", "Myanmar", "Philippines", "Thailand",
            "Vietnam"};

        String[] band2 = {"China", "Hong Kong", "Taiwan", "Macau", "Mongolia", "Afghanistan", "Bangladesh", "Bhutan",
            "India", "Maldives", "Nepal", "Pakistan", "Sri Lanka", "Kazakhstan", "Kyrgyzstan", "Turkmenistan",
            "Uzbekistan", "Tajikistan", "Russia"};

        String[] band3 = {"Japan", "Korea", "South West Pacific", "Guam", "Marshall Islands", "Micronesia", "Northern Mariana Islands", "Palau",
            "Timor Leste", "Middle East", "Australia", "New Zealand"};

        String[] band4 = {"Denmark", "Finland", "Iceland", "Ireland", "Lithuania", " Norway", "United Kingdom", "Greece",
            "Italy", "Spain", "Germany", "France", " Netherlands", "Switzerland", "United States", "South Africa"};

        for (int i = 0; i < band1.length; i++) {
            if (city.equals(band1[i])) {
                band = "band1";
            }
        }

        for (int i = 0; i < band2.length; i++) {
            if (city.equals(band2[i])) {
                band = "band2";
            }
        }

        for (int i = 0; i < band3.length; i++) {
            if (city.equals(band3[i])) {
                band = "band3";
            }
        }

        for (int i = 0; i < band4.length; i++) {
            if (city.equals(band4[i])) {
                band = "band4";
            }
        }

        return band;

    }

    public String generateTagNumber() {
        String leadDigits = "8518";
        //tagNumber += 1;
        String number = createRandomNumber(6);
        return (leadDigits + number);
    }

    public String createRandomNumber(long len) {

        long tLen = (long) Math.pow(10, len - 1) * 9;

        long number = (long) (Math.random() * tLen) + (long) Math.pow(10, len - 1) * 1;

        String tVal = number + "";

        return tVal;
    }

   

    @Override
    public int retrieveNumberOfBaggageAllowed(String classcode) {

        Query q = em.createQuery("SELECT b FROM BookingClass b");

        List<BookingClass> classes = q.getResultList();
        for (BookingClass b : classes) {
            if (b.getClasscode().equals(classcode)) {

                return b.getBaggage();

            }
        }
        return 0;
    }
}
