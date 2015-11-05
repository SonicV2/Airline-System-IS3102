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
    public String addBaggage(Booking booking, Double baggageWeight, Double totalAllowedWeights) {

        double weights = retrieveTotalBaggageWeights(booking);

        if ((weights + baggageWeight) > totalAllowedWeights) {
            return "excess";
        }

        List<Baggage> baggages = booking.getBaggages();

        if (baggages.isEmpty()) {
            Baggage baggage = new Baggage();
            baggage.createBaggage(baggageWeight, booking);
            baggages.add(baggage);
            baggage.setBooking(booking);
            em.persist(baggages);

            BaggageTag bagTag = new BaggageTag();
            String arrivalCity = findArrivalCityIATA(booking);
            generateTagNumber();
            bagTag.create(tagNumber + "", booking.getFlightNo(), booking.getFlightDate(), arrivalCity, booking.getServiceType());
            baggage.setBaggageTag(bagTag);
            em.persist(baggage);
            return "good";
        } else {
            Baggage baggage = new Baggage();
            baggage.createBaggage(baggageWeight, booking);
            baggages.add(baggage);
            baggage.setBooking(booking);
            em.persist(baggages);

            BaggageTag bagTag = new BaggageTag();
            String arrivalCity = findArrivalCityIATA(booking);
            generateTagNumber();
            bagTag.create(tagNumber + "", booking.getFlightNo(), booking.getFlightDate(), arrivalCity, booking.getServiceType());
            baggage.setBaggageTag(bagTag);
            em.persist(baggage);
            return "good";

        }

    }

    @Override
    public List<Baggage> retrieveAllBaggages(Booking booking) {
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

    public int calcualtePenalty(String departure, String destination, int allowed, int totalWeight) {
        int exceed = totalWeight - allowed;
        int penalty = 0;
        String dept = bandSearch(departure);
        String dest = bandSearch(destination);

        if (departure.equals("Singapore")) {
            if (dest.equals("band1")) {
                penalty += exceed * 8;
            } else if (dest.equals("band2")) {
                penalty += exceed * 12;
            } else if (dest.equals("band3")) {
                penalty += exceed * 30;
            } else if (dest.equals("band4")) {
                penalty += exceed * 55;
            }

        } else if (dept.equals("band1") && destination.equals("Singapore")) {
            penalty += exceed * 8;

        } else if (dept.equals("band1") && dest.equals("band1")) {
            penalty += exceed * 16;

        } else if (dept.equals("band1") && dest.equals("band2")) {
            penalty += exceed * 20;
        } else if (dept.equals("band1") && dest.equals("band3")) {
            penalty += exceed * 38;
        } else if (dept.equals("band1") && dest.equals("band3")) {
            penalty += exceed * 68;

        } else if (dept.equals("band2") && destination.equals("Singapore")) {
            penalty += exceed * 12;
        } else if (dept.equals("band2") && dest.equals("band1")) {
            penalty += exceed * 20;

        } else if (dept.equals("band2") && dest.equals("band2")) {
            penalty += exceed * 24;
        } else if (dept.equals("band2") && dest.equals("band3")) {
            penalty += exceed * 42;
        } else if (dept.equals("band2") && dest.equals("band3")) {
            penalty += exceed * 67;
        } else if (dept.equals("band3") && destination.equals("Singapore")) {
            penalty += exceed * 30;
        } else if (dept.equals("band3") && dest.equals("band1")) {
            penalty += exceed * 38;

        } else if (dept.equals("band3") && dest.equals("band2")) {
            penalty += exceed * 42;
        } else if (dept.equals("band3") && dest.equals("band3")) {
            penalty += exceed * 60;
        } else if (dept.equals("band3") && dest.equals("band3")) {
            penalty += exceed * 85;
        } else if (dept.equals("band4") && destination.equals("Singapore")) {
            penalty += exceed * 55;
        } else if (dept.equals("band4") && dest.equals("band1")) {
            penalty += exceed * 63;

        } else if (dept.equals("band4") && dest.equals("band2")) {
            penalty += exceed * 67;
        } else if (dept.equals("band4") && dest.equals("band3")) {
            penalty += exceed * 85;
        } else if (dept.equals("band4") && dest.equals("band3")) {
            penalty += exceed * 110;
        }

        return penalty;
    }

    public String bandSearch(String city) {
        String band = "";

        String[] band1 = {"Brunei", "Cambodia", "Indonesia", "Laos", "Malaysia", "Myanmar", "Philippines", "Thailand",
            "Vietnam"};

        String[] band2 = {"China", "Hong Kong", "Taiwan", "Macau", "Mongolia", "Afghanistan", "Bangladesh", "Bhutan",
            "India", "Maldives", "Nepal", "Pakistan", "Sri Lanka", "Kazakhstan", "Kyrgyzstan", "Turkmenistan",
            "Uzbekistan", "Tajikistan", "Russia"};

        String[] band3 = {"Japan", "Korea", "South West Pacific", "Guam", "Marshall Islands", "Micronesia", "Northern Mariana Islands", "Palau",
            "Timor Leste", "Middle East"};

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
        tagNumber += 1;
//        String sNumber = leadDigits + tagNumber + "";
//        if (seqNumberCheck(sNumber)) {
//            
//        }else{
//        
//            tagNumber+=1;
//            
//        }
        return (leadDigits + tagNumber + "");
    }

    public boolean seqNumberCheck(String sNumber) {
        Query q = em.createQuery("SELECT b FROM BaggageTag b");
        List<BaggageTag> tags = q.getResultList();

        for (BaggageTag b : tags) {
            if (b.getBaggageTagSeqNumber().equals(sNumber)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int retrieveNumberOfBaggageAllowed(String classcode) {

        System.out.println("DFFFFFFFF: " + classcode);

        Query q = em.createQuery("SELECT b FROM BookingClass b");

        List<BookingClass> classes = q.getResultList();
        for (BookingClass b : classes) {
            if (b.getClasscode().equals(classcode)) {
                System.out.println("DFFFFFFFF: " + b.getBaggage());
                return b.getBaggage();

            }
        }
        return 0;
    }
}


