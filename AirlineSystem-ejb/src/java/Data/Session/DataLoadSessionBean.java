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

import CI.Entity.CabinCrew;
import CI.Entity.Pilot;

import CI.Entity.Employee;

import CI.Session.EmployeeSessionBeanLocal;
import CRM.Entity.DiscountType;
import CRM.Session.AnalyticsSessionBeanLocal;
import CRM.Session.DiscountSessionBeanLocal;
import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import Distribution.Session.CustomerSessionBeanLocal;
import Distribution.Session.PassengerBookingSessionBeanLocal;
import Distribution.Session.TravelAgencySessionBeanLocal;
import FOS.Entity.Checklist;
import FOS.Entity.Team;
import FOS.Session.ChecklistSessionBeanLocal;
import FOS.Session.CrewSignInSessionBeanLocal;
import Inventory.Entity.Booking;
import Inventory.Session.BookingSessionBeanLocal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author parthasarthygupta
 */
@Stateless
public class DataLoadSessionBean implements DataLoadSessionBeanLocal {

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
    private ChecklistSessionBeanLocal checklistSessionBean;

    @EJB
    private TravelAgencySessionBeanLocal travelAgencySessionBean;

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

    @EJB
    private PassengerBookingSessionBeanLocal passengerBookingSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private CrewSignInSessionBeanLocal crewSignInSessionBean;

    @EJB
    private BookingSessionBeanLocal bs;

    @EJB
    private AnalyticsSessionBeanLocal am;

    @Override
    public void init() {
        System.out.println("*****Loading data");
        addDiscountTypes();
        System.out.println("*****Discount Types Added");
        addFlights();
        System.out.println("*****Flights added");
        addCustomer();
        System.out.println("*****Customers added");
        addTravelAgencies();
        System.out.println("*****Travel agencies added");
        addCustomerBooking();
       System.out.println("*****Customer  bookings added");
        addTravelAgencyBookings();
        System.out.println("*****Travel Agency bookings added");
        addCabinCrew();
        System.out.println("*****Cabin Crew added");
        addPilot();
        System.out.println("*****Pilot added");
        addGroundCrew();
        System.out.println("*****Ground Crew added");
        addPostFlightChecklist();
        System.out.println("*****Checklists added");
        addManagers();
        System.out.println("*****Managers added");
        addExecutives();
        System.out.println("*****Executives added");
        bookSeats();
        System.out.println("***Data Load complete");
        

    }

    public void addManagers() {
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();

        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("1990-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("1978-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("1988-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-22");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        //employeeSessionBean.addEmployee("S0002", "S0002", "yi", "quan", "FINANCE MANAGER", "FINANCE(SINGAPORE)", date1, "Male", "98218057", "Serangoon North Ave 4, Singapore", "64810187", "yiqaunster@gmail.com", 3000.0);
        //employeeSessionBean.hashPwd("S0002");
        //employeeSessionBean.employeeActivateWithId("S0002");
        employeeSessionBean.addEmployee("A0001", "A0001", "Super", "Admin", "SUPER ADMIN", "IT(SINGAPORE)", date1, "Female", "98218057", "Serangoon North Ave 4, Singapore", "64810187", "yuqing2404@gmail.com", 4000.0);
        employeeSessionBean.hashPwd("A0001");
        employeeSessionBean.employeeActivateWithId("A0001");
        employeeSessionBean.addEmployee("A0002", "A0002", "Fin", "M", "FINANCE MANAGER", "FINANCE(SINGAPORE)", date1, "Male", "98218057", "Serangoon North Ave 4, Singapore", "64810187", "yuqing2404@gmail.com", 3000.0);
        employeeSessionBean.hashPwd("A0002");
        employeeSessionBean.employeeActivateWithId("A0002");
        employeeSessionBean.addEmployee("A0003", "A0003", "Ops", "M", "OPERATIONS MANAGER", "OPERATIONS(SINGAPORE)", date2, "Male", "98218058", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0003");
        employeeSessionBean.employeeActivateWithId("A0003");
        employeeSessionBean.addEmployee("A0004", "A0004", "Mark", "M", "MARKETING MANAGER", "MARKETING(SINGAPORE)", date2, "Male", "98218058", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0004");
        employeeSessionBean.employeeActivateWithId("A0004");
        employeeSessionBean.addEmployee("A0005", "A0005", "Sales", "M", "SALES MANAGER", "SALES(SINGAPORE)", date2, "Male", "98218058", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0005");
        employeeSessionBean.employeeActivateWithId("A0005");
        employeeSessionBean.addEmployee("A0006", "A0006", "HR", "M", "HR MANAGER", "HR(SINGAPORE)", date2, "Female", "98211258", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0006");
        employeeSessionBean.employeeActivateWithId("A0006");
        employeeSessionBean.addEmployee("A0007", "A0007", "IT", "M", "IT MANAGER", "IT(SINGAPORE)", date3, "Male", "98211358", "NUS", "64810787", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0007");
        employeeSessionBean.employeeActivateWithId("A0007");
    }

    public void addExecutives() {
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("1990-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("1978-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("1988-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-22");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }

        employeeSessionBean.addEmployee("A0009", "A0009", "Fin", "E", "FINANCE EXECUTIVE", "FINANCE(SINGAPORE)", date4, "Male", "98218057", "Serangoon North Ave 4, Singapore", "64810187", "yuqing2404@gmail.com", 3000.0);
        employeeSessionBean.hashPwd("A0009");
        employeeSessionBean.employeeActivateWithId("A0009");
        employeeSessionBean.addEmployee("A0010", "A0010", "Ops", "E", "OPERATIONS EXECUTIVE", "OPERATIONS(SINGAPORE)", date2, "Male", "98218058", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0010");
        employeeSessionBean.employeeActivateWithId("A0010");
        employeeSessionBean.addEmployee("A0011", "A0011", "Mark", "E", "MARKETING EXECUTIVE", "MARKETING(SINGAPORE)", date5, "Male", "98218058", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0011");
        employeeSessionBean.employeeActivateWithId("A0011");
        employeeSessionBean.addEmployee("A0012", "A0012", "Sales", "E", "SALES EXECUTIVE", "SALES(SINGAPORE)", date2, "Male", "98218058", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0012");
        employeeSessionBean.employeeActivateWithId("A0012");
        employeeSessionBean.addEmployee("A0013", "A0013", "HR", "E", "HR EXECUTIVE", "HR(SINGAPORE)", date2, "Female", "98211258", "NUS", "64810187", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0013");
        employeeSessionBean.employeeActivateWithId("A0013");
        employeeSessionBean.addEmployee("A0014", "A0014", "IT", "E", "IT EXECUTIVE", "IT(SINGAPORE)", date3, "Male", "98211358", "NUS", "64810787", "yuqing2404@gmail.com", 5000.0);
        employeeSessionBean.hashPwd("A0014");
        employeeSessionBean.employeeActivateWithId("A0014");
    }

    public void addDiscountTypes() {
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-22");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        DiscountType discountType1 = new DiscountType();
        discountType1.setDescription("Post new year 10% discount");
        discountType1.setDiscount(10);
        discountType1.setMileagePointsToRedeem(0.0);
        discountType1.setNoOfCodesUnredeemed(0);
        discountType1.setType("Promotion");
        discountType1.setExpiryDate(date1);
        em.persist(discountType1);

        DiscountType discountType2 = new DiscountType();
        discountType2.setDescription("New year special 20% discount");
        discountType2.setDiscount(20);
        discountType2.setMileagePointsToRedeem(0.0);
        discountType2.setNoOfCodesUnredeemed(0);
        discountType2.setType("Promotion");
        discountType2.setExpiryDate(date2);
        em.persist(discountType2);

        DiscountType discountType3 = new DiscountType();
        discountType3.setDescription("First Anniversary 30% discount");
        discountType3.setDiscount(30);
        discountType3.setMileagePointsToRedeem(0.0);
        discountType3.setNoOfCodesUnredeemed(0);
        discountType3.setType("Promotion");
        discountType3.setExpiryDate(date3);
        em.persist(discountType3);

        DiscountType discountType4 = new DiscountType();
        discountType4.setDescription("Summer holidays 50% discount");
        discountType4.setDiscount(50);
        discountType4.setMileagePointsToRedeem(0.0);
        discountType4.setNoOfCodesUnredeemed(0);
        discountType4.setType("Promotion");
        discountType4.setExpiryDate(date4);
        em.persist(discountType4);

        DiscountType discountType10 = new DiscountType();
        discountType10.setDescription("discount 50% for SG50");
        discountType10.setDiscount(37);
        discountType10.setMileagePointsToRedeem(0.0);
        discountType10.setNoOfCodesUnredeemed(0);
        discountType10.setType("Promotion");
        discountType10.setExpiryDate(date5);
        em.persist(discountType10);

        DiscountType discountType5 = new DiscountType();
        discountType5.setDescription("10% discount on next booking for 1000 miles");
        discountType5.setDiscount(10);
        discountType5.setMileagePointsToRedeem(1000.0);
        discountType5.setNoOfCodesUnredeemed(0);
        discountType5.setType("Mileage");
        discountType5.setExpiryDate(null);
        em.persist(discountType5);

        DiscountType discountType6 = new DiscountType();
        discountType6.setDescription("20% discount on next booking for 2000 miles");
        discountType6.setDiscount(20);
        discountType6.setMileagePointsToRedeem(2000.0);
        discountType6.setNoOfCodesUnredeemed(0);
        discountType6.setType("Mileage");
        discountType6.setExpiryDate(null);
        em.persist(discountType6);

        DiscountType discountType7 = new DiscountType();
        discountType7.setDescription("30% discount on next booking for 3000 miles");
        discountType7.setDiscount(30);
        discountType7.setMileagePointsToRedeem(3000.0);
        discountType7.setNoOfCodesUnredeemed(0);
        discountType7.setType("Mileage");
        discountType7.setExpiryDate(null);
        em.persist(discountType7);

        DiscountType discountType8 = new DiscountType();
        discountType8.setDescription("75% discount on next booking for 10000 miles");
        discountType8.setDiscount(75);
        discountType8.setMileagePointsToRedeem(10000);
        discountType8.setNoOfCodesUnredeemed(0);
        discountType8.setType("Mileage");
        discountType8.setExpiryDate(null);
        em.persist(discountType8);

        discountSessionBean.addDiscountCode(discountType4);
        discountSessionBean.addDiscountCode(discountType4);
        discountSessionBean.addDiscountCode(discountType4);
        discountSessionBean.addDiscountCode(discountType4);
        discountSessionBean.addDiscountCode(discountType1);
        discountSessionBean.addDiscountCode(discountType1);
        discountSessionBean.addDiscountCode(discountType1);
        discountSessionBean.addDiscountCode(discountType1);
        discountSessionBean.addDiscountCode(discountType1);
        discountSessionBean.addDiscountCode(discountType1);
        discountSessionBean.addExpiredDiscountCode(discountType10);


    }

    public void bookSeats() {
        bs.bookSeats("MA303");

    }
    
    public void prepareCRMData(){
        am.createPsuedoCustomers();
        am.pseudoLink();
        System.out.println("***Analytical CRM Data complete");
    }

    public void addFlights() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date(), date6 = new Date(), date7 = new Date(), date8 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 02:00:00");
            date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 15:00:00");
            date3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 01:00:00");
            date4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 13:00:00");
            date5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 17:00:00");
            date6 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-06 08:00:00");
            date7 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2014-11-01 16:00:00");
            date8 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-15 16:00:00");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");

        }

        //MA777 for Testing
        flightSessionBean.addFlight("MA777", "0000010", 200.0, date8, 852L, false);
        flightScheduleSessionBean.scheduleFlights("MA777", false);
        flightScheduleSessionBean.rotateAircrafts();

        //MA303 Past Flight
        flightSessionBean.addFlight("MA303", "0000100", 400.0, date7, 852L, false);
        flightScheduleSessionBean.scheduleFlights("MA303", false);
        flightScheduleSessionBean.rotateAircrafts();

        //MA202 New Flight SIN-HK
        flightSessionBean.addFlight("MA202", "0011100", 150.0, date2, 864L, false);
        flightScheduleSessionBean.scheduleFlights("MA202", false);
        flightScheduleSessionBean.rotateAircrafts();

        //SIN-ADL FLIGHTS (Tue,Wed,Thu)
        flightSessionBean.addFlight("MA110", "0011100", 500.0, date1, 851L, false);
        flightScheduleSessionBean.scheduleFlights("MA110", false);
        flightScheduleSessionBean.rotateAircrafts();
        flightSessionBean.addFlight("MA111", "0011100", 700.0, date2, 851L, false);
        flightScheduleSessionBean.scheduleFlights("MA111", false);
        flightScheduleSessionBean.rotateAircrafts();

        //SIN-NRT (Tue,Wed,Thu)
        flightSessionBean.addFlight("MA112", "0011100", 500.0, date3, 881L, false);
        flightScheduleSessionBean.scheduleFlights("MA112", false);
        flightScheduleSessionBean.rotateAircrafts();

        //NRT-IAH (tue,wed,thur)
        flightSessionBean.addFlight("MA113", "0011100", 600.0, date4, 1404L, false);
        flightScheduleSessionBean.scheduleFlights("MA113", false);
        flightScheduleSessionBean.rotateAircrafts();

        //IAH-NRT (mon,thur,fri)
        flightSessionBean.addFlight("MA114", "0100110", 600.0, date5, 1474L, false);
        flightScheduleSessionBean.scheduleFlights("MA114", false);
        flightScheduleSessionBean.rotateAircrafts();

        //NRT-SING
        flightSessionBean.addFlight("MA115", "1100011", 500.0, date6, 1440L, false);
        flightScheduleSessionBean.scheduleFlights("MA115", false);
        flightScheduleSessionBean.rotateAircrafts();

        //EXTRA FLIGHTS
        flightSessionBean.addFlight("MA116", "1100011", 500.0, date6, 861L, false);
        flightScheduleSessionBean.scheduleFlights("MA116", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA117", "1100011", 500.0, date6, 888L, false);
        flightScheduleSessionBean.scheduleFlights("MA117", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA118", "0011100", 500.0, date6, 1420L, false);
        flightScheduleSessionBean.scheduleFlights("MA118", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA119", "0011100", 500.0, date6, 1447L, false);
        flightScheduleSessionBean.scheduleFlights("MA119", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA120", "1100011", 500.0, date6, 874L, false);
        flightScheduleSessionBean.scheduleFlights("MA120", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA121", "1100011", 500.0, date6, 882L, false);
        flightScheduleSessionBean.scheduleFlights("MA121", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA122", "1100011", 500.0, date6, 868L, false);
        flightScheduleSessionBean.scheduleFlights("MA122", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA123", "0011100", 500.0, date6, 1418L, false);
        flightScheduleSessionBean.scheduleFlights("MA123", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA124", "1100011", 500.0, date6, 870L, false);
        flightScheduleSessionBean.scheduleFlights("MA124", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA125", "1100011", 500.0, date5, 1427L, false);
        flightScheduleSessionBean.scheduleFlights("MA125", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA126", "1100011", 500.0, date5, 1441L, false);
        flightScheduleSessionBean.scheduleFlights("MA126", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA127", "0011100", 500.0, date5, 1433L, false);
        flightScheduleSessionBean.scheduleFlights("MA127", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA128", "0011100", 500.0, date5, 1419L, false);
        flightScheduleSessionBean.scheduleFlights("MA128", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA129", "0011100", 500.0, date5, 1429L, false);
        flightScheduleSessionBean.scheduleFlights("MA129", false);
        flightScheduleSessionBean.rotateAircrafts();

        flightSessionBean.addFlight("MA130", "0011100", 500.0, date5, 1464L, false);
        flightScheduleSessionBean.scheduleFlights("MA130", false);
        flightScheduleSessionBean.rotateAircrafts();
    }

    public void addCustomer() {
        customerSessionBean.addCustomer("Daniel", "Lee", "9799 0869", "6762 3524", "singjjyn@gmail.com", "Sa123!", "12 Bukit Timah Road #19-02 S314253", "Male", new Date(), "Mr", "Singaporean", "SN123123");
        Customer createdCustomer1 = customerSessionBean.getCustomerUseEmail("singjjyn@gmail.com");
        createdCustomer1.setMileagePoints(3000);
        em.merge(createdCustomer1);

        customerSessionBean.addCustomer("Hou", "Liang", "9799 0869", "6762 3524", "hou.liang.90@gmail.com", "Sa123!", "12 Bukit Timah Road #19-02 S314253", "Male", new Date(), "Mr", "Chinese", "CN123123");
        Customer createdCustomer2 = customerSessionBean.getCustomerUseEmail("hou.liang.90@gmail.com");
        createdCustomer2.setMileagePoints(3000);
        em.merge(createdCustomer2);

    }

    public void addTravelAgencies() {
        travelAgencySessionBean.addTravelAgency("KentRidge Travel Agency", 100000.00, 100.00, 0, "singjjyn@gmail.com", "31A Boon Tat Street #03-21 S112233", "6562 3532", "Sa123!", "Dennis Wong");
        TravelAgency travelAgency = travelAgencySessionBean.getAgencyUseEmail("singjjyn@gmail.com");
        travelAgency.setCurrentCredit(100);
        em.merge(travelAgency);
        travelAgencySessionBean.addTravelAgency("Orchard Travel Agency", 500000.00, 400000.00, 0, "singjjyn@naver.com", "10 Bideford Road #22-05 S229922", "6112 4126", "Sa123!", "Tan Shi Hui");
        travelAgencySessionBean.addTravelAgency("Holland Travel Agency", 300000.00, 300000.00, 0, "rnjsdbssk@naver.com", "55 North Buona Vista Road #01-27 S543122", "6785 0102", "Sa123!", "Alice Lee");
        travelAgencySessionBean.addTravelAgency("HarbourFront Travel Agency", 400000.00, 300000.00, 0, "parthasarthy.gupta@gmail.com", "31A Boon Tat Street #03-21 S112233", "6562 3532", "Sa123!", "Dennis Wong");
        travelAgencySessionBean.addTravelAgency("NewTon Travel Agency", 350000.00, 350000.00, 0, "hou.liang.90@gmail.com", "48 Thomson Road #10-07 S77866", "6512 3565", "Sa123!", "Lim Jun Hao");

    }

    public void addCustomerBooking() {

        Schedule schedule1 = new Schedule(), schedule2 = new Schedule(), schedule3 = new Schedule(), schedule4 = new Schedule(), schedule5 = new Schedule(), schedule6 = new Schedule();
        try {
            schedule1 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-01 02:00:00"));
            schedule2 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-02 01:00:00"));
            schedule3 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-06 08:00:00"));
            schedule4 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-13 08:00:00"));
            schedule5 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-08 01:00:00"));
            schedule6 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-12-12 08:00:00"));

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }

       Customer customer1 = customerSessionBean.getCustomerUseEmail("singjjyn@gmail.com");
        Customer customer2 = customerSessionBean.getCustomerUseEmail("hou.liang.90@gmail.com");

       Booking booking1 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Booked", "A01", "Economy Saver", "Mr", "Daniel", "Lee", customer1.getPassportNumber(), customer1.getNationality(), customer1.getId(), false, true, 15.0, "Western");
        Booking booking2 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Booked", "C01", "Economy Premium", "Mr", "Daniel", "Lee", customer1.getPassportNumber(), customer1.getNationality(), customer1.getId(), false, true, 15.0, "Western");
        Booking booking3 = passengerBookingSessionBean.createBooking(500, schedule3.getSeatAvailability(), schedule3.getFlight().getFlightNo(), schedule3.getStartDate(), "Booked", "E01", "First Class", "Mr", "Daniel", "Lee", customer1.getPassportNumber(), customer1.getNationality(), customer1.getId(), false, true, 15.0, "Western");
        Booking booking4 = passengerBookingSessionBean.createBooking(400, schedule4.getSeatAvailability(), schedule4.getFlight().getFlightNo(), schedule4.getStartDate(), "Booked", "E01", "First Class", "Mr", "Daniel", "Lee", customer1.getPassportNumber(), customer1.getNationality(), customer1.getId(), false, true, 15.0, "Western");
        PNR pnr1 = passengerBookingSessionBean.createPNR(1, customer1.getEmail(), customer1.getHomeNumber(), "Booked", 600.0, new Date(), new Date(), "MARS");
        PNR pnr2 = passengerBookingSessionBean.createPNR(1, customer1.getEmail(), customer1.getHomeNumber(), "Booked", 500.0, new Date(), new Date(), "MARS");
        PNR pnr3 = passengerBookingSessionBean.createPNR(1, customer1.getEmail(), customer1.getHomeNumber(), "Booked", 500.0, new Date(), new Date(), "MARS");
        PNR pnr4 = passengerBookingSessionBean.createPNR(1, customer1.getEmail(), customer1.getHomeNumber(), "Booked", 400.0, new Date(), new Date(), "MARS");
        List<Booking> bookingList1 = new ArrayList();
        bookingList1.add(booking1);
        List<Booking> bookingList2 = new ArrayList();
        bookingList2.add(booking2);
        List<Booking> bookingList3 = new ArrayList();
        bookingList3.add(booking3);
        List<Booking> bookingList4 = new ArrayList();
        bookingList4.add(booking4);

        passengerBookingSessionBean.persistBookingAndPNR(pnr1, bookingList1, customer1);
        passengerBookingSessionBean.persistBookingAndPNR(pnr2, bookingList2, customer1);
        passengerBookingSessionBean.persistBookingAndPNR(pnr3, bookingList3, customer1);
        passengerBookingSessionBean.persistBookingAndPNR(pnr4, bookingList4, customer1);

        Booking booking5 = passengerBookingSessionBean.createBooking(600, schedule1.getSeatAvailability(), schedule1.getFlight().getFlightNo(), schedule1.getStartDate(), "Booked", "A01", "Economy Premium", "Mr", "Hou", "Liang", customer2.getPassportNumber(), customer2.getNationality(), customer2.getId(), false, true, 15.0, "Western");
        Booking booking6 = passengerBookingSessionBean.createBooking(500, schedule2.getSeatAvailability(), schedule2.getFlight().getFlightNo(), schedule2.getStartDate(), "Booked", "C01", "Economy Saver", "Mr", "Hou", "Liang", customer2.getPassportNumber(), customer2.getNationality(), customer2.getId(), false, true, 15.0, "Western");
        Booking booking7 = passengerBookingSessionBean.createBooking(500, schedule3.getSeatAvailability(), schedule3.getFlight().getFlightNo(), schedule3.getStartDate(), "Booked", "E01", "Economy Saver", "Mr", "Hou", "Liang", customer2.getPassportNumber(), customer2.getNationality(), customer2.getId(), false, true, 15.0, "Western");
        Booking booking8 = passengerBookingSessionBean.createBooking(400, schedule4.getSeatAvailability(), schedule4.getFlight().getFlightNo(), schedule4.getStartDate(), "Booked", "E01", "Economy Saver", "Mr", "Hou", "Liang", customer2.getPassportNumber(), customer2.getNationality(), customer2.getId(), false, true, 15.0, "Western");
        
        Booking booking9 = passengerBookingSessionBean.createBooking(800, schedule5.getSeatAvailability(), schedule5.getFlight().getFlightNo(), schedule5.getStartDate(), "Booked", "E01", "Business", "Mr", "Hou", "Liang", customer2.getPassportNumber(), customer2.getNationality(), customer2.getId(), false, true, 15.0, "Western");
        Booking booking10 = passengerBookingSessionBean.createBooking(800, schedule6.getSeatAvailability(), schedule6.getFlight().getFlightNo(), schedule6.getStartDate(), "Booked", "E01", "Business", "Mr", "Hou", "Liang", customer2.getPassportNumber(), customer2.getNationality(), customer2.getId(), false, true, 15.0, "Western");
        
        PNR pnr5 = passengerBookingSessionBean.createPNR(1, customer2.getEmail(), customer2.getHomeNumber(), "Booked", 600.0, new Date(), new Date(), "MARS");
        PNR pnr6 = passengerBookingSessionBean.createPNR(1, customer2.getEmail(), customer2.getHomeNumber(), "Booked", 500.0, new Date(), new Date(), "MARS");
        PNR pnr7 = passengerBookingSessionBean.createPNR(1, customer2.getEmail(), customer2.getHomeNumber(), "Booked", 500.0, new Date(), new Date(), "MARS");
        PNR pnr8 = passengerBookingSessionBean.createPNR(1, customer2.getEmail(), customer2.getHomeNumber(), "Booked", 400.0, new Date(), new Date(), "MARS");
        PNR pnr9 = passengerBookingSessionBean.createPNR(1, customer2.getEmail(), customer2.getHomeNumber(), "Booked", 400.0, new Date(), new Date(), "MARS");
        
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
        bookingList9.add(booking10);

        passengerBookingSessionBean.persistBookingAndPNR(pnr5, bookingList5, customer2);
        passengerBookingSessionBean.persistBookingAndPNR(pnr6, bookingList6, customer2);
        passengerBookingSessionBean.persistBookingAndPNR(pnr7, bookingList7, customer2);
        passengerBookingSessionBean.persistBookingAndPNR(pnr8, bookingList8, customer2);
        passengerBookingSessionBean.persistBookingAndPNR(pnr9, bookingList9, customer2);
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

    public void addGroundCrew() {
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("1990-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("1978-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("1988-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-22");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }

        employeeSessionBean.addGroundCrew("G12345A", "G", "1", "GROUND CREW(SINGAPORE)", date1, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Maintenance", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345A");
        employeeSessionBean.employeeActivateWithId("G12345A");
        employeeSessionBean.addGroundCrew("G12345B", "G", "2", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Avionics", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345B");
        employeeSessionBean.employeeActivateWithId("G12345B");
        employeeSessionBean.addGroundCrew("G12345C", "G", "3", "GROUND CREW(SINGAPORE)", date1, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Sheet Metal", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345C");
        employeeSessionBean.employeeActivateWithId("G12345C");
        employeeSessionBean.addGroundCrew("G12345D", "G", "4", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Trim and Fabrication", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345D");
        employeeSessionBean.employeeActivateWithId("G12345D");
        employeeSessionBean.addGroundCrew("G12345E", "G", "5", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Painting", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345E");
        employeeSessionBean.employeeActivateWithId("G12345E");
        employeeSessionBean.addGroundCrew("G12345F", "G", "6", "GROUND CREW(SINGAPORE)", date1, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Welding", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345F");
        employeeSessionBean.employeeActivateWithId("G12345F");
        employeeSessionBean.addGroundCrew("G12345G", "Counter", "1", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Check-in Crew", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345G");
        employeeSessionBean.employeeActivateWithId("G12345G");
        employeeSessionBean.addGroundCrew("G12345H", "G", "7", "GROUND CREW(SINGAPORE)", date1, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Maintenance", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345H");
        employeeSessionBean.employeeActivateWithId("G12345H");
        employeeSessionBean.addGroundCrew("G12345I", "G", "8", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Avionics", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345I");
        employeeSessionBean.employeeActivateWithId("G12345I");
        employeeSessionBean.addGroundCrew("G12345J", "G", "9", "GROUND CREW(SINGAPORE)", date1, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Sheet Metal", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345J");
        employeeSessionBean.employeeActivateWithId("G12345J");
        employeeSessionBean.addGroundCrew("G12345K", "G", "10", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Trim and Fabrication", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345K");
        employeeSessionBean.employeeActivateWithId("G12345K");
        employeeSessionBean.addGroundCrew("G12345L", "G", "11", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Painting", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345L");
        employeeSessionBean.employeeActivateWithId("G12345L");
        employeeSessionBean.addGroundCrew("G12345M", "G", "12", "GROUND CREW(SINGAPORE)", date1, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Welding", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345M");
        employeeSessionBean.employeeActivateWithId("G12345M");
        employeeSessionBean.addGroundCrew("G12345N", "Counter", "2", "GROUND CREW(SINGAPORE)", date2, "Male", "9876678", "NUS", "54333234", "a0083337@u.nus.edu", "Check-in Crew", "2", 3000.0);
        employeeSessionBean.hashPwd("G12345N");
        employeeSessionBean.employeeActivateWithId("G12345N");

    }

    public void addPilot() {
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("1990-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("1978-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("1988-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-22");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        List<String> skills1 = new ArrayList<String>();
        skills1.add("A380");
        skills1.add("A330");
        List<String> skills2 = new ArrayList<String>();
        skills2.add("A380");
        skills2.add("A340");
        List<String> skills3 = new ArrayList<String>();
        skills3.add("A380");
        skills3.add("B777");
        List<String> skills4 = new ArrayList<String>();
        skills4.add("A380");
        skills4.add("B747");

        employeeSessionBean.addPilot("S43210A", "PC", "1", "FLIGHT CREW(SINGAPORE)", date1, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills1, "Captain", 3300.0);
        employeeSessionBean.hashPwd("S43210A");
        employeeSessionBean.employeeActivateWithId("S43210A");
        employeeSessionBean.addPilot("S43210B", "PC", "2", "FLIGHT CREW(SINGAPORE)", date2, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills2, "Captain", 4060.0);
        employeeSessionBean.hashPwd("S43210B");
        employeeSessionBean.employeeActivateWithId("S43210B");
        employeeSessionBean.addPilot("S43210AR", "RPC", "1", "FLIGHT CREW(SINGAPORE)", date1, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills1, "Reserved Captain", 5050.0);
        employeeSessionBean.hashPwd("S43210AR");
        employeeSessionBean.employeeActivateWithId("S43210AR");
        employeeSessionBean.addPilot("S43210BR", "RPC", "2", "FLIGHT CREW(SINGAPORE)", date2, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills2, "Reserved Captain", 5006.0);
        employeeSessionBean.hashPwd("S43210BR");
        employeeSessionBean.employeeActivateWithId("S43210BR");

        employeeSessionBean.addPilot("S43210C", "PF", "1", "FLIGHT CREW(SINGAPORE)", date3, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills3, "First Officer", 4000.0);
        employeeSessionBean.hashPwd("S43210C");
        employeeSessionBean.employeeActivateWithId("S43210C");
        employeeSessionBean.addPilot("S43210D", "PF", "2", "FLIGHT CREW(SINGAPORE)", date4, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills4, "First Officer", 3500.0);
        employeeSessionBean.hashPwd("S43210D");
        employeeSessionBean.employeeActivateWithId("S43210D");
        employeeSessionBean.addPilot("S43210CR", "RPF", "1", "FLIGHT CREW(SINGAPORE)", date3, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills3, "Reserved First Officer", 3060.0);
        employeeSessionBean.hashPwd("S43210CR");
        employeeSessionBean.employeeActivateWithId("S43210CR");
        employeeSessionBean.addPilot("S43210DR", "RPF", "2", "FLIGHT CREW(SINGAPORE)", date4, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills4, "Reserved First Officer", 3500.0);
        employeeSessionBean.hashPwd("S43210DR");
        employeeSessionBean.employeeActivateWithId("S43210DR");
        employeeSessionBean.addPilot("S43210E", "PO", "1", "FLIGHT CREW(SINGAPORE)", date5, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills1, "Observer", 3000.0);
        employeeSessionBean.hashPwd("S43210E");
        employeeSessionBean.employeeActivateWithId("S43210E");
        employeeSessionBean.addPilot("S43210F", "PO", "2", "FLIGHT CREW(SINGAPORE)", date1, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills2, "Observer", 5000.0);
        employeeSessionBean.hashPwd("S43210F");
        employeeSessionBean.employeeActivateWithId("S43210F");
        employeeSessionBean.addPilot("S43210ER", "RPO", "1", "FLIGHT CREW(SINGAPORE)", date5, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills1, "Reserved Observer", 3000.0);
        employeeSessionBean.hashPwd("S43210ER");
        employeeSessionBean.employeeActivateWithId("S43210ER");
        employeeSessionBean.addPilot("S43210FR", "RPO", "2", "FLIGHT CREW(SINGAPORE)", date1, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills2, "Reserved Observer", 4000.0);
        employeeSessionBean.hashPwd("S43210FR");
        employeeSessionBean.employeeActivateWithId("S43210FR");

    }

    public void addCabinCrew() {   //Create Cabin Crew
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("1990-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("1978-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("1988-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-22");

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        List<String> langu1 = new ArrayList<String>();
        langu1.add("English");
        langu1.add("Chinese");
        List<String> langu2 = new ArrayList<String>();
        langu2.add("English");
        langu2.add("Japanese");
        List<String> langu3 = new ArrayList<String>();
        langu3.add("Korean");
        langu3.add("English");
        employeeSessionBean.addCabinCrew("S12345A", "E", "1", "FLIGHT CREW(SINGAPORE)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu1, "Lead Flight Stewardess", 2000.0);
        employeeSessionBean.hashPwd("S12345A");
        employeeSessionBean.employeeActivateWithId("S12345A");
        employeeSessionBean.addCabinCrew("S12345J", "E", "10", "FLIGHT CREW(SINGAPORE)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu2, "Lead Flight Stewardess", 4000.0);
        employeeSessionBean.hashPwd("S12345J");
        employeeSessionBean.employeeActivateWithId("S12345J");
        employeeSessionBean.addCabinCrew("S12345B", "E", "2", "FLIGHT CREW(SINGAPORE)", date3, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "3", langu2, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("S12345B");
        employeeSessionBean.employeeActivateWithId("S12345B");
        employeeSessionBean.addCabinCrew("S12345C", "E", "3", "FLIGHT CREW(SINGAPORE)", date4, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Flight Stewardess", 4000.0);
        employeeSessionBean.hashPwd("S12345C");
        employeeSessionBean.employeeActivateWithId("S12345C");
        employeeSessionBean.addCabinCrew("S12345D", "E", "4", "FLIGHT CREW(SINGAPORE)", date5, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("S12345D");
        employeeSessionBean.employeeActivateWithId("S12345D");
        employeeSessionBean.addCabinCrew("S12345E", "E", "5", "FLIGHT CREW(SINGAPORE)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu1, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("S12345E");
        employeeSessionBean.employeeActivateWithId("S12345E");
        employeeSessionBean.addCabinCrew("S12345F", "E", "6", "FLIGHT CREW(SINGAPORE)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "3", langu2, "Flight Stewardess", 4000.0);
        employeeSessionBean.hashPwd("S12345F");
        employeeSessionBean.employeeActivateWithId("S12345F");
        employeeSessionBean.addCabinCrew("S12345G", "E", "7", "FLIGHT CREW(SINGAPORE)", date3, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("S12345G");
        employeeSessionBean.employeeActivateWithId("S12345G");
        employeeSessionBean.addCabinCrew("S12345H", "E", "8", "FLIGHT CREW(SINGAPORE)", date4, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("S12345H");
        employeeSessionBean.employeeActivateWithId("S12345H");
        employeeSessionBean.addCabinCrew("S12345I", "E", "9", "FLIGHT CREW(SINGAPORE)", date5, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu1, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("S12345I");
        employeeSessionBean.employeeActivateWithId("S12345I");
        
        employeeSessionBean.addCabinCrew("J12345A", "JE", "1", "FLIGHT CREW(JAPAN)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu1, "Lead Flight Stewardess", 2000.0);
        employeeSessionBean.hashPwd("J12345A");
        employeeSessionBean.employeeActivateWithId("J12345A");
        employeeSessionBean.addCabinCrew("J12345J", "JE", "10", "FLIGHT CREW(JAPAN)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu2, "Lead Flight Stewardess", 4000.0);
        employeeSessionBean.hashPwd("J12345J");
        employeeSessionBean.employeeActivateWithId("J12345J");
        employeeSessionBean.addCabinCrew("J12345B", "JE", "2", "FLIGHT CREW(JAPAN)", date3, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "3", langu2, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("J12345B");
        employeeSessionBean.employeeActivateWithId("J12345B");
        employeeSessionBean.addCabinCrew("J12345C", "JE", "3", "FLIGHT CREW(JAPAN)", date4, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Flight Stewardess", 4000.0);
        employeeSessionBean.hashPwd("J12345C");
        employeeSessionBean.employeeActivateWithId("J12345C");
        employeeSessionBean.addCabinCrew("J12345D", "JE", "4", "FLIGHT CREW(JAPAN)", date5, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("J12345D");
        employeeSessionBean.employeeActivateWithId("J12345D");
        employeeSessionBean.addCabinCrew("J12345E", "JE", "5", "FLIGHT CREW(JAPAN)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu1, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("J12345E");
        employeeSessionBean.employeeActivateWithId("J12345E");
        employeeSessionBean.addCabinCrew("J12345F", "JE", "6", "FLIGHT CREW(JAPAN)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "3", langu2, "Flight Stewardess", 4000.0);
        employeeSessionBean.hashPwd("J12345F");
        employeeSessionBean.employeeActivateWithId("J12345F");
        employeeSessionBean.addCabinCrew("J12345G", "JE", "7", "FLIGHT CREW(JAPAN)", date3, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("J12345G");
        employeeSessionBean.employeeActivateWithId("J12345G");
        employeeSessionBean.addCabinCrew("J12345H", "JE", "8", "FLIGHT CREW(JAPAN)", date4, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("J12345H");
        employeeSessionBean.employeeActivateWithId("J12345H");
        employeeSessionBean.addCabinCrew("J12345I", "JE", "9", "FLIGHT CREW(JAPAN)", date5, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu1, "Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("J12345I");
        employeeSessionBean.employeeActivateWithId("J12345I");
        
        employeeSessionBean.addCabinCrew("RS12345A", "RE", "1", "FLIGHT CREW(SINGAPORE)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu1, "Reserved Flight Stewardess", 3400.0);
        employeeSessionBean.hashPwd("RS12345A");
        employeeSessionBean.employeeActivateWithId("RS12345A");
        employeeSessionBean.addCabinCrew("RS12345J", "RE", "10", "FLIGHT CREW(SINGAPORE)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu2, "Reserved Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("RS12345J");
        employeeSessionBean.employeeActivateWithId("RS12345J");
        employeeSessionBean.addCabinCrew("RS12345D", "RE", "4", "FLIGHT CREW(SINGAPORE)", date5, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Reserved Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("RS12345D");
        employeeSessionBean.employeeActivateWithId("RS12345D");
        employeeSessionBean.addCabinCrew("RS12345E", "RE", "5", "FLIGHT CREW(SINGAPORE)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu1, "Reserved Flight Stewardess", 3700.0);
        employeeSessionBean.hashPwd("RS12345E");
        employeeSessionBean.employeeActivateWithId("RS12345E");
        employeeSessionBean.addCabinCrew("RS12345F", "RE", "6", "FLIGHT CREW(SINGAPORE)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "3", langu2, "Reserved Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("RS12345F");
        employeeSessionBean.employeeActivateWithId("RS12345F");
        employeeSessionBean.addCabinCrew("RS12345G", "RE", "7", "FLIGHT CREW(SINGAPORE)", date3, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Reserved Flight Stewardess", 3500.0);
        employeeSessionBean.hashPwd("RS12345G");
        employeeSessionBean.employeeActivateWithId("RS12345G");
        employeeSessionBean.addCabinCrew("RS12345H", "RE", "8", "FLIGHT CREW(SINGAPORE)", date4, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Reserved Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("RS12345H");
        employeeSessionBean.employeeActivateWithId("RS12345H");
        employeeSessionBean.addCabinCrew("RS12345I", "RE", "9", "FLIGHT CREW(SINGAPORE)", date5, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu1, "Reserved Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("RS12345I");
        employeeSessionBean.employeeActivateWithId("RS12345I");
        
        employeeSessionBean.addCabinCrew("JRS12345A", "JRE", "1", "FLIGHT CREW(JAPAN)", date1, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu1, "Reserved Flight Stewardess", 3400.0);
        employeeSessionBean.hashPwd("JRS12345A");
        employeeSessionBean.employeeActivateWithId("JRS12345A");
        employeeSessionBean.addCabinCrew("JRS12345J", "JRE", "10", "FLIGHT CREW(JAPAN)", date2, "Female", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "5", langu2, "Reserved Flight Stewardess", 3000.0);
        employeeSessionBean.hashPwd("JRS12345J");
        employeeSessionBean.employeeActivateWithId("JRS12345J");
        
        employeeSessionBean.addCabinCrew("S12345K", "E", "11", "FLIGHT CREW(SINGAPORE)", date4, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Flight Steward", 3000.0);
        employeeSessionBean.hashPwd("S12345K");
        employeeSessionBean.employeeActivateWithId("S12345K");
        employeeSessionBean.addCabinCrew("S12345L", "E", "12", "FLIGHT CREW(SINGAPORE)", date5, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Flight Steward", 2000.0);
        employeeSessionBean.hashPwd("S12345L");
        employeeSessionBean.employeeActivateWithId("S12345L");
        employeeSessionBean.addCabinCrew("RS12345K", "RE", "11", "FLIGHT CREW(SINGAPORE)", date4, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Reserved Flight Steward", 3800.0);
        employeeSessionBean.hashPwd("RS12345K");
        employeeSessionBean.employeeActivateWithId("RS12345K");
        employeeSessionBean.addCabinCrew("RS12345L", "RE", "12", "FLIGHT CREW(SINGAPORE)", date5, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Reserved Flight Steward", 3000.0);
        employeeSessionBean.hashPwd("RS12345L");
        employeeSessionBean.employeeActivateWithId("RS12345L");

        employeeSessionBean.addCabinCrew("JRS12345K", "JRE", "11", "FLIGHT CREW(JAPAN)", date4, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "1", langu2, "Reserved Flight Steward", 3800.0);
        employeeSessionBean.hashPwd("JRS12345K");
        employeeSessionBean.employeeActivateWithId("JRS12345K");
        employeeSessionBean.addCabinCrew("JRS12345L", "JRE", "12", "FLIGHT CREW(JAPAN)", date5, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Reserved Flight Steward", 3000.0);
        employeeSessionBean.hashPwd("JRS12345L");
        employeeSessionBean.employeeActivateWithId("JRS12345L");

    }

    public void addPostFlightChecklist() {
        Date date1 = new Date(), date2 = new Date(), date3 = new Date(), date4 = new Date(), date5 = new Date();
        Schedule schedule1 = new Schedule();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse("1990-02-01");
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1989-01-01");
            date3 = new SimpleDateFormat("yyyy-MM-dd").parse("1978-03-20");
            date4 = new SimpleDateFormat("yyyy-MM-dd").parse("1988-07-10");
            date5 = new SimpleDateFormat("yyyy-MM-dd").parse("1987-08-22");
            schedule1 = scheduleSessionBean.getScheduleByDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2015-11-10 02:00:00"));

        } catch (ParseException ex) {
            System.out.println("Error initializing date");
        }
        List<String> langu1 = new ArrayList<String>();
        langu1.add("English");
        langu1.add("Chinese");
        List<String> langu2 = new ArrayList<String>();
        langu2.add("English");
        langu2.add("Japanese");
        List<String> skills1 = new ArrayList<String>();
        skills1.add("A380");
        skills1.add("A330");

        employeeSessionBean.addCabinCrew("X123", "CC", "PFCL", "FLIGHT CREW(SINGAPORE)", date5, "Male", "98765567", "NUS", "65778905", "a0083337@u.nus.edu", "2", langu1, "Flight Steward", 3000.0);
        employeeSessionBean.hashPwd("X123");
        employeeSessionBean.employeeActivateWithId("X123");

        employeeSessionBean.addPilot("Y123", "P", "PFCL", "FLIGHT CREW(SINGAPORE)", date1, "Male", "98722345", "NUS", "65345678", "a0083337@u.nus.edu", "3", skills1, "Captain", 3300.0);
        employeeSessionBean.hashPwd("Y123");
        employeeSessionBean.employeeActivateWithId("Y123");

        List<CabinCrew> cabinCrewList = new ArrayList();
        List<Pilot> pilotList = new ArrayList();
        cabinCrewList.add(crewSignInSessionBean.getCabinCrew("CCPFCL"));
        pilotList.add(crewSignInSessionBean.getPilot("PPFCL"));

        Team team1 = new Team();
        team1.setCabinCrews(cabinCrewList);
        team1.setPilots(pilotList);
        schedule1.setTeam(team1);

        CabinCrew crew1 = crewSignInSessionBean.getCabinCrew("CCPFCL");
        crew1.setTeam(team1);
        Pilot pilot1 = crewSignInSessionBean.getPilot("PPFCL");
        pilot1.setTeam(team1);

        List<Schedule> scheduleList = new ArrayList();
        scheduleList.add(schedule1);
        List<Checklist> checklistList = checklistSessionBean.createChecklistAndItems();
        schedule1.setChecklists(checklistList);
        team1.setSchedule(scheduleList);
        em.persist(team1);
        em.merge(crew1);
        em.merge(pilot1);
    }
    
 

}
