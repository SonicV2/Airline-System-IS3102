/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.Session;

import APS.Entity.Schedule;
import APS.Session.FlightScheduleSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import CRM.Entity.DiscountType;
import CRM.Session.DiscountSessionBeanLocal;
import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import Distribution.Session.CustomerSessionBeanLocal;
import Distribution.Session.PassengerBookingSessionBeanLocal;
import Distribution.Session.TravelAgencySessionBeanLocal;
import Inventory.Entity.Booking;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author parthasarthygupta
 */
@Singleton
@Startup
@LocalBean
public class DataLoadSessionBean {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @EJB
    private DiscountSessionBeanLocal discountSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private TravelAgencySessionBeanLocal travelAgencySessionBean;

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

    @EJB
    private PassengerBookingSessionBeanLocal passengerBookingSessionBean;

    @PostConstruct
    public void init() {
        System.out.println("*****Loading data");
        //addDiscountTypes();
        //addFlights();
        //addCustomer();
        //addTravelAgencies();
        //addCustomerBooking();
        //addTravelAgencyBookings();
    }

    public void addDiscountTypes() {
        DiscountType discountType1 = new DiscountType();
        discountType1.setDescription("Get 10& off on your next booking");
        discountType1.setDiscount(10);
        discountType1.setMileagePointsToRedeem(200);
        discountType1.setNoOfCodesUnredeemed(0);
        em.persist(discountType1);

        DiscountType discountType2 = new DiscountType();
        discountType2.setDescription("Get 20& off on your next booking");
        discountType2.setDiscount(20);
        discountType2.setMileagePointsToRedeem(350);
        discountType2.setNoOfCodesUnredeemed(0);
        em.persist(discountType2);

        DiscountType discountType3 = new DiscountType();
        discountType3.setDescription("Get 30& off on your next booking");
        discountType3.setDiscount(30);
        discountType3.setMileagePointsToRedeem(500);
        discountType3.setNoOfCodesUnredeemed(0);
        em.persist(discountType3);

        DiscountType discountType4 = new DiscountType();
        discountType4.setDescription("Get 50& off on your next booking");
        discountType4.setDiscount(50);
        discountType4.setMileagePointsToRedeem(650);
        discountType4.setNoOfCodesUnredeemed(0);
        em.persist(discountType4);

        discountSessionBean.addDiscountCode(discountType4);
    }

    public void addFlights() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date(), date6 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 02:00:00");
            date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 15:00:00");
            date3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 01:00:00");
            date4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 13:00:00");
            date5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 17:00:00");
            date6 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 08:00:00");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");

        }
        //SIN-ADL FLIGHTS (Tue,Wed,Thu)
        flightSessionBean.addFlight("MA110", "0011100", 500.0, date1, 851L, false);
        flightScheduleSessionBean.scheduleFlights("MA110");
        flightSessionBean.addFlight("MA111", "0011100", 700.0, date2, 851L, false);
        flightScheduleSessionBean.scheduleFlights("MA111");

        //SIN-NRT (Tue,Wed,Thu)
        flightSessionBean.addFlight("MA112", "0011100", 500.0, date3, 881L, false);
        flightScheduleSessionBean.scheduleFlights("MA112");

        //NRT-IAH (tue,wed,thur)
        flightSessionBean.addFlight("MA113", "0011100", 600.0, date4, 1404L, false);
        flightScheduleSessionBean.scheduleFlights("MA113");

        //IAH-NRT (mon,thur,fri)
        flightSessionBean.addFlight("MA114", "0100110", 600.0, date5, 1474L, false);
        flightScheduleSessionBean.scheduleFlights("MA114");

        //NRT-SING
        flightSessionBean.addFlight("MA115", "1100011", 500.0, date6, 1440L, false);
        flightScheduleSessionBean.scheduleFlights("MA115");
    }

    public void addCustomer() {
        customerSessionBean.addCustomer("Daniel", "Lee", "9799 0869", "6762 3524", "singjjyn@gmail.com", "Sa123!", "12 Bukit Timah Road #19-02 S314253", "Male", new Date(), "Mr", "Singaporean", "SN123123");
        Customer createdCustomer = customerSessionBean.getCustomerUseEmail("singjjyn@gmail.com");
        createdCustomer.setMileagePoints(3000);
        em.merge(createdCustomer);

    }

    public void addTravelAgencies() {
        travelAgencySessionBean.addTravelAgency("KentRidge Travel Agency", 100000.00, 100.00, 0, "singjjyn@gmail.com", "31A Boon Tat Street #03-21 S112233", "6562 3532", "Sa123!", "Dennis Wong");
        TravelAgency travelAgency = travelAgencySessionBean.getAgencyUseEmail("singjjyn@gmail.com");
        travelAgency.setCurrentCredit(100);
        em.merge(travelAgency);
        travelAgencySessionBean.addTravelAgency("Orchard Travel Agency", 500000.00, 500000.00, 0, "singjjyn@naver.com", "10 Bideford Road #22-05 S229922", "6112 4126", "Sa123!", "Tan Shi Hui");
        travelAgencySessionBean.addTravelAgency("Holland Travel Agency", 300000.00, 300000.00, 0, "rnjsdbssk@naver.com", "55 North Buona Vista Road #01-27 S543122", "6785 0102", "Sa123!", "Alice Lee");
        travelAgencySessionBean.addTravelAgency("HarbourFront Travel Agency", 400000.00, 400000.00, 0, "parthasarthy.gupta@gmail.com", "31A Boon Tat Street #03-21 S112233", "6562 3532", "Sa123!", "Dennis Wong");
        travelAgencySessionBean.addTravelAgency("NewTon Travel Agency", 350000.00, 350000.00, 0, "hou.liang.90@gmail.com", "48 Thomson Road #10-07 S77866", "6512 3565", "Sa123!", "Lim Jun Hao");

    }

    public void addCustomerBooking() {

        Schedule schedule1 = new Schedule(), schedule2 = new Schedule(), schedule3 = new Schedule();
        try {
            schedule1 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-24 02:00:00"));
            schedule2 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-02 01:00:00"));
            schedule3 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-06 08:00:00"));

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        Customer customer = customerSessionBean.getCustomerUseEmail("singjjyn@gmail.com");
        Booking booking1 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Booked", "A01", "Economy Saver", "Mr", "Daniel", "Lee", customer.getPassportNumber(), customer.getNationality(), customer.getId(), false, true, 15.0, "Western");
        Booking booking2 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Booked", "C01", "Economy Premium", "Mr", "Daniel", "Lee", customer.getPassportNumber(), customer.getNationality(), customer.getId(), false, true, 15.0, "Western");
        Booking booking3 = passengerBookingSessionBean.createBooking(500, schedule3.getSeatAvailability(), schedule3.getFlight().getFlightNo(), schedule3.getStartDate(), "Booked", "E01", "First Class", "Mr", "Daniel", "Lee", customer.getPassportNumber(), customer.getNationality(), customer.getId(), false, true, 15.0, "Western");
        PNR pnr1 = passengerBookingSessionBean.createPNR(1, customer.getEmail(), customer.getHomeNumber(), "Booked", 600.0, new Date(), new Date(), "MARS");
        PNR pnr2 = passengerBookingSessionBean.createPNR(1, customer.getEmail(), customer.getHomeNumber(), "Booked", 500.0, new Date(), new Date(), "MARS");
        PNR pnr3 = passengerBookingSessionBean.createPNR(1, customer.getEmail(), customer.getHomeNumber(), "Booked", 500.0, new Date(), new Date(), "MARS");
        List<Booking> bookingList1 = new ArrayList();
        bookingList1.add(booking1);
        List<Booking> bookingList2 = new ArrayList();
        bookingList2.add(booking2);
        List<Booking> bookingList3 = new ArrayList();
        bookingList3.add(booking3);

        passengerBookingSessionBean.persistBookingAndPNR(pnr1, bookingList1, customer);
        passengerBookingSessionBean.persistBookingAndPNR(pnr2, bookingList2, customer);
        passengerBookingSessionBean.persistBookingAndPNR(pnr3, bookingList3, customer);
    }

    public void addTravelAgencyBookings() {
        TravelAgency travelAgency1 = travelAgencySessionBean.getAgencyUseEmail("singjjyn@naver.com");
        TravelAgency travelAgency2 = travelAgencySessionBean.getAgencyUseEmail("parthasarthy.gupta@gmail.com");

        Schedule schedule1 = new Schedule(), schedule2 = new Schedule(), schedule3 = new Schedule();
        //date 1 is past 14 days, date 2 is <14 days, date 3 is DOC
        Date date1 = new Date(), date2 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-10-01 02:00:00");
            date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-14 15:00:00");
            schedule1 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-24 02:00:00"));
            schedule2 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-02 01:00:00"));
            schedule3 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-06 08:00:00"));

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        Booking booking1 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Pending", "A01", "Economy Saver", "Mr", "Michael", "Yong", "Z1237", "Chinese", 0, false, false, 15.0, "Western");
        Booking booking2 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Pending", "C01", "Economy Premium", "Mr", "Chris", "Nolan", "AC245", "American", 0, false, true, 15.0, "Western");
        Booking booking3 = passengerBookingSessionBean.createBooking(500, schedule3.getSeatAvailability(), schedule3.getFlight().getFlightNo(), schedule3.getStartDate(), "Pending", "E01", "First Class", "Mr", "Daniel", "Ng", "HK25436", "Hong Kong", 0, false, true, 15.0, "Western");
        Booking booking4 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Pending", "A01", "Economy Saver", "Mr", "Janice", "Wong", "Z1239", "Chinese", 0, false, false, 15.0, "Western");
        Booking booking5 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Pending", "C01", "Economy Premium", "Mr", "Steve", "Anderson", "AC241", "French", 0, false, true, 15.0, "Western");
        Booking booking6 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Pending", "A01", "Economy Saver", "Mr", "Michael", "Yong", "Z1237", "Chinese", 0, false, false, 15.0, "Western");
        Booking booking7 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Pending", "C01", "Economy Premium", "Mr", "Chris", "Nolan", "AC245", "American", 0, false, true, 15.0, "Western");
        Booking booking8 = passengerBookingSessionBean.createBooking(500, schedule3.getSeatAvailability(), schedule3.getFlight().getFlightNo(), schedule3.getStartDate(), "Pending", "E01", "First Class", "Mr", "Daniel", "Ng", "HK25436", "Hong Kong", 0, false, true, 15.0, "Western");
        Booking booking9 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Pending", "A01", "Economy Saver", "Mr", "Janice", "Wong", "Z1239", "Chinese", 0, false, false, 15.0, "Western");
        Booking booking10 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Pending", "C01", "Economy Premium", "Mr", "Steve", "Anderson", "AC241", "French", 0, false, true, 15.0, "Western");

        PNR pnr1 = passengerBookingSessionBean.createPNR(1, "michael@gmail.com", "983145786", "Pending", 600.0, date1, null, "TravelAgency");
        PNR pnr2 = passengerBookingSessionBean.createPNR(1, "chris@gmail.com", "875437893", "Pending", 515.0, date1, null, "TravelAgency");
        PNR pnr3 = passengerBookingSessionBean.createPNR(1, "daniel@gmail.com", "42359321", "Pending", 515.0, date2, null, "TravelAgency");
        PNR pnr4 = passengerBookingSessionBean.createPNR(1, "janice@gmail.com", "875431893", "Pending", 600.0, date2, null, "TravelAgency");
        PNR pnr5 = passengerBookingSessionBean.createPNR(1, "steve@gmail.com", "42359321", "Pending", 515.0, date1, null, "TravelAgency");

        PNR pnr6 = passengerBookingSessionBean.createPNR(1, "michael@gmail.com", "983145786", "Pending", 600.0, date1, null, "TravelAgency");
        PNR pnr7 = passengerBookingSessionBean.createPNR(1, "chris@gmail.com", "875437893", "Pending", 515.0, date1, null, "TravelAgency");
        PNR pnr8 = passengerBookingSessionBean.createPNR(1, "daniel@gmail.com", "42359321", "Pending", 515.0, date2, null, "TravelAgency");
        PNR pnr9 = passengerBookingSessionBean.createPNR(1, "janice@gmail.com", "875431893", "Pending", 600.0, date2, null, "TravelAgency");
        PNR pnr10 = passengerBookingSessionBean.createPNR(1, "steve@gmail.com", "42359321", "Pending", 515.0, date1, null, "TravelAgency");

        List<Booking> bookingList1 = new ArrayList();
        bookingList1.add(booking1);
        List<Booking> bookingList2 = new ArrayList();
        bookingList2.add(booking2);
        List<Booking> bookingList3 = new ArrayList();
        bookingList3.add(booking3);
        List<Booking> bookingList4 = new ArrayList();
        bookingList4.add(booking4);
        List<Booking> bookingList5 = new ArrayList();
        bookingList5.add(booking5);
        List<Booking> bookingList6 = new ArrayList();
        bookingList6.add(booking6);
        List<Booking> bookingList7 = new ArrayList();
        bookingList7.add(booking7);
        List<Booking> bookingList8 = new ArrayList();
        bookingList8.add(booking8);
        List<Booking> bookingList9 = new ArrayList();
        bookingList9.add(booking9);
        List<Booking> bookingList10 = new ArrayList();
        bookingList10.add(booking10);

        passengerBookingSessionBean.persistBookingAndPNR(pnr1, bookingList1, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr2, bookingList2, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr3, bookingList3, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr4, bookingList4, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr5, bookingList5, null);
        
         passengerBookingSessionBean.persistBookingAndPNR(pnr6, bookingList6, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr7, bookingList7, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr8, bookingList8, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr9, bookingList9, null);
        passengerBookingSessionBean.persistBookingAndPNR(pnr10, bookingList10, null);

        travelAgencySessionBean.linkPNR(travelAgency1, pnr1);
        travelAgencySessionBean.linkPNR(travelAgency1, pnr2);
        travelAgencySessionBean.linkPNR(travelAgency1, pnr3);
        travelAgencySessionBean.linkPNR(travelAgency1, pnr5);
        travelAgencySessionBean.linkPNR(travelAgency1, pnr4);
        travelAgencySessionBean.linkPNR(travelAgency2, pnr6);
        travelAgencySessionBean.linkPNR(travelAgency2, pnr7);
        travelAgencySessionBean.linkPNR(travelAgency2, pnr8);
        travelAgencySessionBean.linkPNR(travelAgency2, pnr9);
        travelAgencySessionBean.linkPNR(travelAgency2, pnr10);

        travelAgencySessionBean.confirmPNR(travelAgency1, pnr5, pnr5.getTotalPrice());
        travelAgencySessionBean.confirmPNR(travelAgency2, pnr10, pnr10.getTotalPrice());

    }

}
