/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightSessionBeanLocal;
import Administration.Session.AccountingSessionBeanLocal;
import CI.Session.EmailSessionBeanLocal;
import CRM.Entity.DiscountCode;
import CRM.Entity.DiscountType;
import CRM.Session.DiscountSessionBeanLocal;
import Distribution.Entity.Customer;
import Distribution.Entity.FlightOptions;
import Distribution.Entity.PNR;
import Distribution.Session.CustomerSessionBeanLocal;
import Distribution.Session.DistributionSessionBeanLocal;
import Distribution.Session.PassengerBookingSessionBeanLocal;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import Inventory.Session.PricingSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Yunna
 */
@Named(value = "mARSManagedBean")
@ManagedBean
@SessionScoped
public class MARSManagedBean {

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private PricingSessionBeanLocal pm;

    @EJB
    private PassengerBookingSessionBeanLocal passengerBookingSessionBean;

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @EJB
    private DiscountSessionBeanLocal discountSessionBean;
    
    @EJB
    private AccountingSessionBeanLocal accountingSessionBean;

    @ManagedProperty(value = "#{customerManagedBean}")
    private CustomerManagedBean customerManagedBean;

    private String flightNo;
    private Double flightDuration;
    private Double price;

    private Date departureDate;
    private Date returnDate;
    private Date tempDepartureDate;
    private Date tempReturnDate;

    //the variables for one way flights
    private Date oneWayDepartureDate;
    private int oneWayAdult;
    private int oneWayChildren;
    private String oneWayServiceType;
    private String oneWayOriginCity;
    private String oneWayDestinationCity;
    private Boolean oneWayFlight;

    private Long routeId;
    private String originCountry;
    private String originCity;
    private String originIATA;
    private String destinationCountry;
    private String destinationCity;
    private String destinationIATA;
    private Double distance;

    private int adults;
    private int children;
    private String serviceType;

    private List<Route> routes;
    private List<Schedule> schedule;
    private List<Flight> flights;
    private List<String> transitHubs;
    private List<Schedule> legOneSchedules;
    private List<Schedule> legTwoSchedules;
    private List<Schedule> directFlightSchedules;
    private List<Schedule> oneStopFlightSchedules;

    private String directFlightDuration; //oneDuration for all directFlights for same route

    private List<String> oneStopFlightDuration;
    private List<String> oneStopFlightLayover;

    private List<String> origins;
    private List<String> destinations;
    FacesMessage message = null;

    //Price variables
    private List<Double> minPricesForWeekDirectFlight;
    private List<Double> minPricesForWeekOneStopFlight;
    private List<Double> selectedDatePrices;
    private List<Date> weekDates;

    private List<Integer> adultList;
    private Integer adultTemp;

    private boolean isReturnDateSet;

    private List<FlightOptions> flightOptionsList;
    private Schedule legOne;
    private Schedule legTwo;
    private String layover;
    private String duration;
    private double priceForDirect;

    private Schedule selectedDepartureSchedule;
    private Schedule selectedReturnSchedule;
    private List<Schedule> selectedSchedules;
    private FlightOptions selectedDepartureFlightOption;
    private FlightOptions selectedReturnFlightOption;
    private List<Schedule> selectedDepartureSchedules;
    private List<Schedule> selectedReturnSchedules;

    private Date flightDate;
    private String bookingStatus;
    private String classCode;

    private String travellerTitle;
    private String travellerFristName;
    private String travellerLastName;
    private String passportNumber;
    private String nationality;
    private long customerId;

    private boolean isChild;
    private boolean boughtInsurance;
    private double insuranceFare;
    private String foodSelection;
    private boolean isDirect;

    private Long id;
    private SeatAvailability seatAvail;

    private PNR pnr;

    private double totalWeightAllowed;
    private int totalNumBaggeAlloewd;

    private double totalSelectedPrice;
    private double totalPriceWinsurance;
    private double adultPrice;
    private double childPrice;

    private List<Passenger> passengerList;

    private String primaryEmail;
    private String primaryContactNo;

    private String creditCard;
    private String csv;
    private String pnrId;

    private PNR searchedPNR;
    private Date systemDate;

    private String customerEmail;
    private String customerPassword;
    private boolean isCustomerLoggedOn;

    private String subject;
    private String body;

    private PNRDisplay pnrDisplayList;
    private DiscountCode discountCode;
    private boolean discountCodeApplied;
    private boolean discountTypeApplied;
    private String refundStatus;
    private int weightAllowed;
    private DiscountType selectedDiscountType;
    private int mileagePoints;
    private double oldFinalPrice;
    private double discountPercentage;

    @PostConstruct
    public void retrieve() {
        //Retrieve all the available flights into a list of flights
        setFlights(flightSessionBean.retrieveFlights());
        //For each flight, add the available routes to the list of routes

        List<Route> temp = new ArrayList<Route>();
        List<String> temp1 = new ArrayList<String>();
        List<String> temp2 = new ArrayList<String>();

        for (Flight eachFlight : flights) {
            temp.add(eachFlight.getRoute());
        }
        setRoutes(temp);

        /*Store origin and destination cities in lists for display purpose*/
        for (Route eachRoute : routes) {
            if (!temp1.contains(eachRoute.getOriginCity())) {
                temp1.add(eachRoute.getOriginCity());
            }
            if (!temp2.contains(eachRoute.getDestinationCity())) {
                temp2.add(eachRoute.getDestinationCity());
            }
        }
        setOrigins(temp1);
        setDestinations(temp2);

        legOneSchedules = new ArrayList();
        legTwoSchedules = new ArrayList();
        oneStopFlightDuration = new ArrayList();
        oneStopFlightLayover = new ArrayList();
        returnDate = null;
        setIsReturnDateSet(false);
        selectedDatePrices = new ArrayList();
        minPricesForWeekDirectFlight = new ArrayList();
        minPricesForWeekOneStopFlight = new ArrayList();
        oneStopFlightSchedules = new ArrayList();
        transitHubs = new ArrayList();
        directFlightSchedules = new ArrayList();
        flightOptionsList = new ArrayList();
        weekDates = new ArrayList();
        selectedSchedules = new ArrayList();
        selectedDepartureSchedules = new ArrayList();
        selectedReturnSchedules = new ArrayList();
        totalSelectedPrice = 0;
        adultPrice = 0;
        childPrice = 0;
        passengerList = new ArrayList();
        totalPriceWinsurance = 0;
        creditCard = null;
        csv = null;
        setPnrId(null);
        systemDate = new Date();
        discountCode = null;
    }

    public int convertToHours(double duration) {
        int hr = (int) duration;
        return hr;
    }

    public int convertToMins(double duration) {
        double d = duration - Math.floor(duration);
        int min = (int) (d * 60);
        return min;
    }

    public void setOneWayVariables() {
        setAdults(oneWayAdult);
        setChildren(oneWayChildren);
        setOriginCity(oneWayOriginCity);
        setDestinationCity(oneWayDestinationCity);
        setServiceType(oneWayServiceType);
        setDepartureDate(oneWayDepartureDate);
    }

    //helps users to view other departure flights within the week
    public String viewOtherDepartureFlights(Date date) {
        if (oneWayFlight) {

            setOneWayDepartureDate(date);
            return displayDepartureFlights(true);
        } else {
            setDepartureDate(date);
            return displayDepartureFlights(false);
        }
    }

    public String viewOtherReturnFlights(Date date) {

        setReturnDate(date);
        String tempOrigin = originIATA;
        setOriginIATA(destinationIATA);
        setDestinationIATA(tempOrigin);
        return displayReturnFlights();

    }

    public void setClassRules() {
        if (serviceType.equals("Economy Saver") || serviceType.equals("Economy Basic")) {
            refundStatus = "Not refundable";
            weightAllowed = 15;
        } else if (serviceType.equals("Economy Premium")) {
            refundStatus = "Refundable";
            weightAllowed = 15;
        } else if (serviceType.equals("Business")) {
            refundStatus = "Refundable";
            weightAllowed = 30;
        } else if (serviceType.equals("First Class")) {
            refundStatus = "Refundable";
            weightAllowed = 45;
        }
    }

    public String displayDepartureFlights(Boolean oneWay) {

        setDiscountCodeApplied(false);
        discountTypeApplied = false;
        discountCode = null;
        oneWayFlight = oneWay;
        /*Convert the chosen origin and destination cities into IATAs*/
        List<Flight> allFlights = new ArrayList();
        allFlights = distributionSessionBean.getAllFlights();
        flightOptionsList = new ArrayList();
        weekDates.clear();
        selectedDepartureSchedules.clear();
        selectedSchedules.clear();

        //basic input check
        if (oneWay) {
            setOneWayVariables();
            if ((oneWayAdult == 0 && oneWayChildren == 0) || oneWayServiceType == null || oneWayOriginCity == null
                    || oneWayDestinationCity == null) {

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please fill in all required fields!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            } else if (oneWayOriginCity.equals(oneWayDestinationCity)) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Origin and destination cannot be the same!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        } else if (!oneWay) {
            if (departureDate == null || returnDate == null || (adults == 0 && children == 0)
                    || serviceType == null || originCity == null || destinationCity == null) {

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please fill in all required fields!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            } else if (originCity.equals(destinationCity)) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Origin and destination cannot be the same!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        }
        //go into database to get IATA of origin and destinations
        for (Flight eachFlight : allFlights) {
            if (eachFlight.getRoute().getOriginCity().equalsIgnoreCase(originCity)) {
                originIATA = eachFlight.getRoute().getOriginIATA();
            }
            if (eachFlight.getRoute().getDestinationCity().equalsIgnoreCase(destinationCity)) {
                destinationIATA = eachFlight.getRoute().getDestinationIATA();
            }
        }

        boolean inputValid = true;
        //One way jorney selected by user
        if (oneWay) {
            if (distributionSessionBean.existsSchedule(originIATA, destinationIATA, departureDate, serviceType, adults, children) == false) {
                inputValid = false;
            }
        } //return journey selected by user
        else {
            if (distributionSessionBean.existsSchedule(originIATA, destinationIATA, departureDate, serviceType, adults, children) == false || distributionSessionBean.existsSchedule(destinationIATA, originIATA, returnDate, serviceType, adults, children) == false) {
                inputValid = false;
            }
        }
        if (inputValid == false) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No flights found for the selected routes or dates", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            setDepartureDate(null);
            setReturnDate(null);
            setOriginIATA("");
            setDestinationIATA("");
            setAdults(0);
            setChildren(0);
            setServiceType("");
            return null;
        } else {
            selectedDatePrices.clear();
            directFlightSchedules = new ArrayList();
            setDirectFlightDuration("");
            setClassRules();

            //Check whether there is direct flight
            if (distributionSessionBean.existsDirectFlight(originIATA, destinationIATA)) {
                setDirectFlightSchedules(distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, departureDate, serviceType, adults, children));
                setDirectFlightDuration(distributionSessionBean.getTotalDurationForDirect(directFlightSchedules.get(0)));

                for (Schedule eachSchedule : directFlightSchedules) {
                    eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                    eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));

                    double priceForOne = pm.getPrice(pm.getClassCode(eachSchedule, serviceType, adults + children, false), eachSchedule);

                    selectedDatePrices.add(priceForOne);

                }
                setIsDirect(true);
                retrieveMinWeekPricesForDirect(originIATA, destinationIATA, departureDate, serviceType, adults, children);
                setPriceForDirect(selectedDatePrices.get(0));
                System.out.println("displayingggggg....");
                return "DisplayDepartureDirectFlightReturn?faces-redirect=true";

            } else { //Retrieve one stop flights
                System.out.println("managedbean::::one stop flights...");
                legOneSchedules.clear();
                legTwoSchedules.clear();
                oneStopFlightDuration = new ArrayList();
                oneStopFlightLayover = new ArrayList();
                oneStopFlightSchedules = new ArrayList();

                retrieveMinWeekPricesForOneStop(originIATA, destinationIATA, departureDate, serviceType, adults, children);
                legOneSchedules.clear();
                legTwoSchedules.clear();
                transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);
                for (int i = 0; i < transitHubs.size(); i++) {
                    addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(i), departureDate, serviceType, adults, children));
                    addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, distributionSessionBean.addDaysToDate(departureDate, 1), serviceType, adults, children));
                    addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, departureDate, serviceType, adults, children));
                }
                setOneStopFlightSchedules(distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules));
                int i;
                List<Schedule> flightOption = new ArrayList();
                flightOption.add(new Schedule());
                flightOption.add(new Schedule());

                double priceForOne = 0;

                for (i = 0; i < oneStopFlightSchedules.size(); i++) {
                    flightOption.set(i % 2, oneStopFlightSchedules.get(i));
                    priceForOne += pm.getPrice(pm.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults + children, false), oneStopFlightSchedules.get(i));
                    if (i % 2 == 1) {
                        oneStopFlightDuration.add(distributionSessionBean.getTotalDurationForOneStop(flightOption.get(0), flightOption.get(1)));
                        oneStopFlightLayover.add(distributionSessionBean.getLayoverTime(flightOption.get(0), flightOption.get(1)));
                        selectedDatePrices.add(priceForOne);
                        priceForOne = 0;

                    }
                }

                List<String> flightNosWithAdjustedEndDates = new ArrayList();

                for (Schedule eachSchedule : oneStopFlightSchedules) {
                    if (!flightNosWithAdjustedEndDates.contains(eachSchedule.getFlight().getFlightNo())) {
                        eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                        eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));
                        flightNosWithAdjustedEndDates.add(eachSchedule.getFlight().getFlightNo());
                    }
                }
                setIsDirect(false);
                int c = 0;

                System.out.println("The size of one stop schedules are!!!!!" + oneStopFlightSchedules.size());
                System.out.println("The size of one stop layover!!!!!" + oneStopFlightLayover.size());
                System.out.println("The size of one stop flight duration!!!!!" + oneStopFlightDuration.size());
                System.out.println("The size of selectedDates prices!!!!!" + selectedDatePrices.size());

                for (int a = 0; a < oneStopFlightSchedules.size(); a++) {
                    FlightOptions newFlightOptions = new FlightOptions();
                    int b = a;
                    a += 1;
                    newFlightOptions.createFlightOptions(oneStopFlightSchedules.get(b), oneStopFlightSchedules.get(a), oneStopFlightLayover.get(b - c), oneStopFlightDuration.get(b - c), selectedDatePrices.get(b - c));
                    distributionSessionBean.persistFlightOptions(newFlightOptions);
                    flightOptionsList.add(newFlightOptions);
                    c++;

                }

                return "DisplayDepartureOneStopFlightReturn";
//                    if (oneWay) {
//                        return "DisplayOneStopFlight";
//                    } else {
//                        return "DisplayDepartureOneStopFlightReturn";
//                    }
            }
        }

    }

    public String bookDirectDepartureSchedule(Schedule directSchedule) {
        setSelectedDepartureSchedule(directSchedule);
        System.out.println(selectedDepartureSchedule.getScheduleId());
        if (oneWayFlight) {
            return summary();
        }
        if (!oneWayFlight) {
            return displayReturnFlights();
        }
        return null;
    }

    public String bookOneStopDeparture(FlightOptions flightOption) {
        setSelectedDepartureFlightOption(flightOption);

        if (oneWayFlight) {
            return summary();
        }
        if (!oneWayFlight) {
            return displayReturnFlights();
        }
        return null;
    }

    public String bookDirectReturnSchedule(Schedule directSchedule) {
        setSelectedReturnSchedule(directSchedule);
        return summary();
    }

    public String bookOneStopReturn(FlightOptions flightOption) {
        setSelectedReturnFlightOption(flightOption);
        return summary();
    }
    
    public String clear(){
        
        setOriginCity(null);
        setDestinationCity(null);
        setDepartureDate(null);
        setReturnDate(null);
        setAdults(0);
        setChildren(0);
        
        setOneWayOriginCity(null);
        setOneWayDestinationCity(null);
        setOneWayDepartureDate(null);
        setOneWayAdult(0);
        setOneWayChildren(0);
        
        return "/Distribution/MerlionAirlines?faces-redirect=true";
    }

    public String displayReturnFlights() {

        directFlightSchedules.clear();
        selectedDatePrices.clear();
        setDirectFlightDuration("");
        minPricesForWeekDirectFlight.clear();
        minPricesForWeekOneStopFlight.clear();
        oneStopFlightSchedules.clear();
        legOneSchedules.clear();
        legTwoSchedules.clear();
        transitHubs.clear();
        oneStopFlightDuration.clear();
        oneStopFlightLayover.clear();
        //switching origin and destination
        String tempOrigin = originIATA;
        setOriginIATA(destinationIATA);
        setDestinationIATA(tempOrigin);
        flightOptionsList.clear();
        weekDates.clear();
        selectedReturnSchedules.clear();

        setTempDepartureDate(departureDate);
        setTempReturnDate(returnDate);
        setDepartureDate(returnDate);
        //Check whether there is direct flight
        if (distributionSessionBean.existsDirectFlight(originIATA, destinationIATA)) {
            setDirectFlightSchedules(distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, departureDate, serviceType, adults, children));
            setDirectFlightDuration(distributionSessionBean.getTotalDurationForDirect(directFlightSchedules.get(0)));

            for (Schedule eachSchedule : directFlightSchedules) {
                eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));
                double priceForOne = pm.getPrice(pm.getClassCode(eachSchedule, serviceType, adults + children, false), eachSchedule);
                selectedDatePrices.add(priceForOne);
            }
            retrieveMinWeekPricesForDirect(originIATA, destinationIATA, departureDate, serviceType, adults, children);

            return "DisplayReturnDirectFlightReturn";
        } else { //Retrieve one stop flights

            retrieveMinWeekPricesForOneStop(originIATA, destinationIATA, departureDate, serviceType, adults, children);
            legOneSchedules.clear();
            legTwoSchedules.clear();
            oneStopFlightDuration.clear();
            oneStopFlightLayover.clear();
            transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);
            for (int i = 0; i < transitHubs.size(); i++) {
                addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(i), departureDate, serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, distributionSessionBean.addDaysToDate(departureDate, 1), serviceType, adults, children));
//                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, distributionSessionBean.addDaysToDate(departureDate, 2), serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, departureDate, serviceType, adults, children));
            }
            setOneStopFlightSchedules(distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules));
            int i;
            List<Schedule> flightOption = new ArrayList();
            flightOption.add(new Schedule());
            flightOption.add(new Schedule());

            double priceForOne = 0;

            for (i = 0; i < oneStopFlightSchedules.size(); i++) {
                flightOption.set(i % 2, oneStopFlightSchedules.get(i));
                priceForOne += pm.getPrice(pm.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults + children, false), oneStopFlightSchedules.get(i));
                if (i % 2 == 1) {
                    oneStopFlightDuration.add(distributionSessionBean.getTotalDurationForOneStop(flightOption.get(0), flightOption.get(1)));
                    oneStopFlightLayover.add(distributionSessionBean.getLayoverTime(flightOption.get(0), flightOption.get(1)));
                    selectedDatePrices.add(priceForOne);
                    priceForOne = 0;
                }
            }

            List<String> flightNosWithAdjustedEndDates = new ArrayList();

            for (Schedule eachSchedule : oneStopFlightSchedules) {
                if (!flightNosWithAdjustedEndDates.contains(eachSchedule.getFlight().getFlightNo())) {
                    eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                    eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));
                    flightNosWithAdjustedEndDates.add(eachSchedule.getFlight().getFlightNo());
                }
            }

            int c = 0;

            for (int a = 0; a < oneStopFlightSchedules.size(); a++) {
                FlightOptions newFlightOptions = new FlightOptions();
                int b = a;
                a += 1;
                newFlightOptions.createFlightOptions(oneStopFlightSchedules.get(b), oneStopFlightSchedules.get(a), oneStopFlightLayover.get(b - c), oneStopFlightDuration.get(b - c), selectedDatePrices.get(b - c));
                distributionSessionBean.persistFlightOptions(newFlightOptions);
                flightOptionsList.add(newFlightOptions);
                c++;
            }

            return "DisplayReturnOneStopFlightReturn";
        }

    }

    public void retrieveMinWeekPricesForDirect(String originIATA, String destinationIATA, Date date, String serviceType, int adults, int children) {
        List<Double> pricesForWeek = new ArrayList();
        List<Schedule> schedulesForEachDate = new ArrayList();
        double minPrice = 0;
        int i;

        for (i = -3; i <= 3; i++) {
            Date eachDate = distributionSessionBean.addDaysToDate(date, i);
            if (eachDate.compareTo(new Date()) < 0) {
                continue;
            }
            weekDates.add(eachDate);
            schedulesForEachDate = distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, eachDate, serviceType, adults, children);
            if (schedulesForEachDate.size() > 0) {
                minPrice = 99999999;
                for (Schedule eachSchedule : schedulesForEachDate) {
                    //Store price for each schedule in priceForEachScheduleVariable
                    double priceForOne = pm.getPrice(pm.getClassCode(eachSchedule, serviceType, adults + children, false), eachSchedule);

                    if (priceForOne < minPrice) {
                        minPrice = priceForOne;
                    }
                }
                pricesForWeek.add(minPrice);
            } else {
                pricesForWeek.add(0.0);
            }

        }
        setMinPricesForWeekDirectFlight(pricesForWeek);

    }

    public void retrieveMinWeekPricesForOneStop(String originIATA, String destinationIATA, Date date, String serviceType, int adults, int children) {

        List<Double> pricesForWeek = new ArrayList();
        List<Schedule> schedulesForEachDate = new ArrayList();
        double minPrice, priceForEachFlightOption = 0.0, priceForOne;
        int i, j, k;

        List<String> transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);

        for (i = -3; i <= 3; i++) {
            Date eachDate = distributionSessionBean.addDaysToDate(date, i);
            if (eachDate.compareTo(new Date()) < 0) {
                continue;
            }
            weekDates.add(eachDate);
            legOneSchedules.clear();
            legTwoSchedules.clear();
            for (j = 0; j < transitHubs.size(); j++) {
                addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(j), eachDate, serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(j), destinationIATA, distributionSessionBean.addDaysToDate(eachDate, 1), serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(j), destinationIATA, eachDate, serviceType, adults, children));
            }
            schedulesForEachDate = distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules);

            if (schedulesForEachDate.size() > 0) {
                minPrice = 99999999;

                for (k = 0; k < schedulesForEachDate.size(); k++) {
                    Schedule eachSchedule = schedulesForEachDate.get(k);
                    priceForOne = pm.getPrice(pm.getClassCode(eachSchedule, serviceType, (adults + children), false), eachSchedule);
                    priceForEachFlightOption += priceForOne;

                    if (k % 2 == 1) {
                        if (priceForEachFlightOption < minPrice) {
                            minPrice = priceForEachFlightOption;
                        }
                        priceForEachFlightOption = 0.0;
                    }
                }
                pricesForWeek.add(minPrice);
            } else {
                pricesForWeek.add(0.0);
            }
        }
        setMinPricesForWeekOneStopFlight(pricesForWeek);

    }

    public void addSchedulesToLegOne(List<Schedule> originalSchedules, List<Schedule> schedulesToAdd) {

        for (Schedule eachScheduleToAdd : schedulesToAdd) {
            originalSchedules.add(eachScheduleToAdd);
        }
        setLegOneSchedules(originalSchedules);
    }

    public void addSchedulesToLegTwo(List<Schedule> originalSchedules, List<Schedule> schedulesToAdd) {
        for (Schedule eachScheduleToAdd : schedulesToAdd) {
            originalSchedules.add(eachScheduleToAdd);
        }
        setLegTwoSchedules(originalSchedules);
    }

    public String summary() {

//        if (isDirect && oneWayFlight) {
//            if (selectedDepartureSchedule == null) {
//                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select a flight!", "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                return null;
//            }
//        } else if (isDirect && !oneWayFlight) {
//  
//            if (selectedReturnSchedule == null) {
//                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select a flight!", "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                return null;
//            }
//
//        }
//
//        if (!isDirect && oneWayFlight) {
//            if (selectedDepartureFlightOption == null) {
//                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select a flight!", "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                return null;
//            }
//
//        } else if (!isDirect && !oneWayFlight) {
//            
//            if (selectedReturnFlightOption == null) {
//                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select a flight!", "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//                return null;
//            }
//
//        }
        passengerList.clear();

        if (isDirect) {
            selectedSchedules.add(selectedDepartureSchedule);
        } else {
            selectedSchedules.add(selectedDepartureFlightOption.getLegOne());
            selectedSchedules.add(selectedDepartureFlightOption.getLegTwo());
        }
        if (!oneWayFlight) {
            if (isDirect) {
                selectedSchedules.add(selectedReturnSchedule);
            } else {
                selectedSchedules.add(selectedReturnFlightOption.getLegOne());
                selectedSchedules.add(selectedReturnFlightOption.getLegTwo());
            }
        }

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA()), distributionSessionBean.getSingaporeTimeZone()));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA()), distributionSessionBean.getSingaporeTimeZone()));
        }
        setSelectedDepartureSchedules(passengerBookingSessionBean.getDepartureSchedules(selectedSchedules, !oneWayFlight));
        if (!oneWayFlight) {
            setSelectedReturnSchedules(passengerBookingSessionBean.getReturnSchedules(selectedSchedules));
        }

        totalSelectedPrice = 0;

        double priceForEachSchedule = 0;
        for (Schedule eachSelectedSchedule : selectedSchedules) {
            priceForEachSchedule = pm.getPrice(pm.getClassCode(eachSelectedSchedule, serviceType, (adults + children), false), eachSelectedSchedule);
            totalSelectedPrice += (priceForEachSchedule * adults) + (priceForEachSchedule * 0.75 * children);
        }

        adultPrice = priceForEachSchedule;
        childPrice = priceForEachSchedule;
        Passenger passenger;

        passengerList.clear();
        for (int k = 0; k < adults + children; k++) {
            passenger = new Passenger();
            passenger.setId(k + 1);
            passengerList.add(passenger);
        }
        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
        }

        Customer loggedInCustomer = customerManagedBean.getCustomer();
        if (loggedInCustomer != null) {
            passengerList.get(0).setTitle(loggedInCustomer.getTitle());
            passengerList.get(0).setFirstName(loggedInCustomer.getFirstName());
            passengerList.get(0).setLastName(loggedInCustomer.getLastName());
            passengerList.get(0).setPassport(loggedInCustomer.getPassportNumber());
            passengerList.get(0).setNationality(loggedInCustomer.getNationality());
            passengerList.get(0).setCustomerId(loggedInCustomer.getId().toString());
            primaryContactNo = loggedInCustomer.getHpNumber();
            primaryEmail = loggedInCustomer.getEmail();
        }

        return "Summary";

    }

    public String loginCheckAtSummary() {

        if (customerManagedBean.doLogin(customerEmail, customerPassword)) {
            customerManagedBean.setCustomerPassword(customerPassword);
            customerManagedBean.setCustomerEmail(customerEmail);
            customerManagedBean.setIsCustomerLoggedOn(true);

            Customer loggedInCustomer = customerSessionBean.getCustomerUseEmail(customerEmail);

            passengerList.get(0).setTitle(loggedInCustomer.getTitle());
            passengerList.get(0).setFirstName(loggedInCustomer.getFirstName());
            passengerList.get(0).setLastName(loggedInCustomer.getLastName());
            passengerList.get(0).setPassport(loggedInCustomer.getPassportNumber());
            passengerList.get(0).setNationality(loggedInCustomer.getNationality());
            passengerList.get(0).setCustomerId(loggedInCustomer.getId().toString());
            primaryContactNo = loggedInCustomer.getHpNumber();
            primaryEmail = loggedInCustomer.getEmail();

            return "Summary";

            //return "CustomerDashboard";
        } else {
            return "";
        }

    }

    public void timeframeEndChanged(SelectEvent event) {
        setDepartureDate((Date) event.getObject());
        System.out.println(":::managedbean departure date:::" + departureDate);
    }

    public String payment() {

        for (int i = 0; i < passengerList.size(); i++) {
            if (passengerList.get(i).getCustomerId().isEmpty()) {
                continue;
            }
            if (!isInteger(passengerList.get(i).getCustomerId())) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Customer Id!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
            if (!passengerList.get(i).getCustomerId().equals("")) {
                if (!passengerBookingSessionBean.isPassengerAFrequentFlyer(Long.parseLong(passengerList.get(i).getCustomerId()))) {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Id does not exist!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }
                else{
                    Customer currentCustomer = customerSessionBean.getCustomerUseID(passengerList.get(i).getCustomerId());
                    if(!passengerList.get(i).getFirstName().equals(currentCustomer.getFirstName()) || !passengerList.get(i).getLastName().equals(currentCustomer.getLastName()) || !passengerList.get(i).getPassport().equals(currentCustomer.getPassportNumber())){
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Details do not match!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                    }
                }
                    
            }
            
        }

        totalPriceWinsurance = totalSelectedPrice;

        for (int k = 0; k < (adults + children); k++) {
            if (passengerList.get(k).isInsurance()) {
                totalPriceWinsurance += 15.0;
            }
        }
        return "Payment";
    }

    public String createBooking() {

        if (!checkCreditCard(creditCard)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid credit card number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        if (creditCard.length() != 16) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit card number should be 16 digits!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;

        }

        if (csv.length() != 3) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid csv!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        if (!isInteger(csv)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid csv!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA()), distributionSessionBean.getSingaporeTimeZone()));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA()), distributionSessionBean.getSingaporeTimeZone()));
        }

        double priceForEachBooking;
        bookingStatus = "Booked";
        List<Booking> bookingList = new ArrayList();
        Customer primaryCustomer = new Customer();

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            classCode = pm.getClassCode(eachSelectedSchedule, serviceType, (adults + children), false);
            priceForEachBooking = pm.getPrice(classCode, eachSelectedSchedule);
            setSeatAvail(eachSelectedSchedule.getSeatAvailability());
            setFlightNo(eachSelectedSchedule.getFlight().getFlightNo());
            setFlightDate(eachSelectedSchedule.getStartDate());
            for (int k = 0; k < (adults + children); k++) {
                setFlightDate(eachSelectedSchedule.getStartDate());
                if (passengerList.get(k).getCustomerId().equals("")) {
                    passengerList.get(k).setCustomerId("0");
                }
                Booking eachBooking = passengerBookingSessionBean.createBooking(priceForEachBooking, seatAvail, flightNo, flightDate, bookingStatus, classCode, serviceType, passengerList.get(k).getTitle(), passengerList.get(k).getFirstName(), passengerList.get(k).getLastName(), passengerList.get(k).getPassport(), passengerList.get(k).getNationality(), Long.parseLong(passengerList.get(k).getCustomerId()), false, passengerList.get(k).isInsurance(), 15.0, passengerList.get(k).getFoodSelection());
                bookingList.add(eachBooking);
            }
        }
        pnr = passengerBookingSessionBean.createPNR((adults + children), getPrimaryEmail(), getPrimaryContactNo(), "Booked", totalPriceWinsurance, new Date(), new Date(), "MerlionAirlines");
        if (passengerBookingSessionBean.isPassengerAFrequentFlyer(Long.parseLong(passengerList.get(0).getCustomerId()))) {
            primaryCustomer = passengerBookingSessionBean.getCustomerByCustomerId(Long.parseLong(passengerList.get(0).getCustomerId()));
        } else {
            primaryCustomer = null;
        }

        passengerBookingSessionBean.persistBookingAndPNR(pnr, bookingList, primaryCustomer);

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
        }

        setCsv(null);
        setCreditCard(null);

        sendEmail(primaryEmail);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A cofirmation email has been sent to the primary email. Please check.", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        if (discountCode != null) {
            discountSessionBean.markCodeAsClaimed(discountCode);
            discountCode = null;
        }
        if (discountTypeApplied){
            customerManagedBean.getCustomer().setMileagePoints(customerManagedBean.getCustomer().getMileagePoints()-(int)selectedDiscountType.getMileagePointsToRedeem());
            customerSessionBean.updateCustomerProfile(customerManagedBean.getCustomer());
            customerManagedBean.refreshDiscountTypesRedeemable();
        }
        
        
        accountingSessionBean.makeTransaction("Customer Booking", totalPriceWinsurance);
        
        return "Confirmation";
    }

    public void sendEmail(String email) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        setSubject("Merlion Airlines Booking Confirmation");

        String flight = "";
        String tempBody = "";

        for (int i = 0; i < selectedSchedules.size(); i++) {
            flight = "Flight Number: " + selectedSchedules.get(i).getFlight().getFlightNo()
                    + "\nOrigin Country: " + selectedSchedules.get(i).getFlight().getRoute().getOriginCity() + ", " + selectedSchedules.get(i).getFlight().getRoute().getOriginCountry()
                    + "\nDestination Country: " + selectedSchedules.get(i).getFlight().getRoute().getDestinationCity() + ", " + selectedSchedules.get(i).getFlight().getRoute().getDestinationCountry()
                    + "\nDeparture Date: " + formatter.format(selectedSchedules.get(i).getStartDate()) + ","
                    + "\nArrival Date: " + formatter.format(selectedSchedules.get(i).getEndDate()) + "\n\n";
            tempBody += flight;
        }

        setBody("Thank you for using Merlion Airlines. \n\nYour PNR Id: " + pnr.getPnrID() + "\nDate of Booking: " + formatter.format(pnr.getDateOfBooking())
                + "\nNumber of Travellers: " + pnr.getNoOfTravellers() + "\nTotal Baggage Allowance: " + (weightAllowed * pnr.getNoOfTravellers()) + "kgs\nBooking Refund Status: " + refundStatus + "\nTotal Price Paid: " + pnr.getTotalPrice()
                + "\n\n" + tempBody + "\n\nYou can always view the details of your booking at our website.");

        emailSessionBean.sendEmail(email, getSubject(), getBody());

    }

    public String searchPNR() {

        searchedPNR = passengerBookingSessionBean.getPNR(pnrId);
        if (searchedPNR == null || !primaryEmail.equals(searchedPNR.getEmail()) || searchedPNR.getPnrStatus().equalsIgnoreCase("Flown")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Booking record is not found!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        selectedSchedules.clear();

        PNRDisplay eachPNRDisplay = new PNRDisplay();

        List<Schedule> selectedSchedules = new ArrayList();

        List<Long> addedSchedules = new ArrayList();
        List<String> addedNames = new ArrayList();

        eachPNRDisplay.setId(searchedPNR.getPnrID());
        int noOfTravellers = searchedPNR.getNoOfTravellers();

        eachPNRDisplay.setNoOfTravellers(noOfTravellers);
        eachPNRDisplay.setBookingDate(searchedPNR.getDateOfBooking());
        eachPNRDisplay.setRefundable(distributionSessionBean.isPNRRefundable(searchedPNR));

        for (Booking eachBooking : searchedPNR.getBookings()) {
            if (!addedNames.contains(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName())) {
                addedNames.add(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName());
            }

            if (!addedSchedules.contains(eachBooking.getSeatAvail().getSchedule().getScheduleId())) {
                addedSchedules.add(eachBooking.getSeatAvail().getSchedule().getScheduleId());
                selectedSchedules.add(eachBooking.getSeatAvail().getSchedule());

            }
        }

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
        }

        eachPNRDisplay.setTravellerNames(addedNames);

        eachPNRDisplay.setUniqueSchedules(selectedSchedules);

        String serviceType = searchedPNR.getBookings().get(0).getServiceType();
        eachPNRDisplay.setServiceType(serviceType);
        if (serviceType.charAt(0) == 'E') {
            eachPNRDisplay.setNoOfBags(noOfTravellers);
        } else if (serviceType.charAt(0) == 'B') {
            eachPNRDisplay.setNoOfBags(noOfTravellers * 2);
        } else if (serviceType.charAt(0) == 'F') {
            eachPNRDisplay.setNoOfBags(noOfTravellers * 3);
        }

        setPnrDisplayList(eachPNRDisplay);
        setPrimaryEmail(null);
        setPnrId(null);

        return "DisplayPNR";
    }

    public String deletePNR() {

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA()), distributionSessionBean.getSingaporeTimeZone()));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA()), distributionSessionBean.getSingaporeTimeZone()));
        }
        passengerBookingSessionBean.deletePNR(searchedPNR);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Booking has been successfully cancelled!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "MerlionAirlines";
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public boolean checkCreditCard(String s) {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String redeemDiscountCode(String code) {
        setOldFinalPrice(totalSelectedPrice);
        //Accept the entered code as argument
        if (code.length() == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter a discount code!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (discountSessionBean.discountCodeValid(code) == false) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Code is Invalid!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (isDiscountCodeApplied() == true) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A Discount Code has already been applied!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            discountCode = discountSessionBean.getDiscountCodeFromCode(code);
            Date currentDate = new Date();
            if (discountCode.getDiscountType().getType().equals("Promotion") && discountCode.getDiscountType().getExpiryDate()!=null) {
                if (currentDate.compareTo(discountCode.getDiscountType().getExpiryDate()) > 0) {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount code has already expired!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }
            }
            setDiscountPercentage(discountCode.getDiscountType().getDiscount());
            totalSelectedPrice = totalSelectedPrice - (discountCode.getDiscountType().getDiscount() / 100 * totalSelectedPrice);
            setDiscountCodeApplied(true);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Code Applied!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return null;
    }
    
     public String redeemDiscountType() {
            setDiscountCodeApplied(true);
            discountTypeApplied = true;
            setOldFinalPrice(totalSelectedPrice);
            setDiscountPercentage(selectedDiscountType.getDiscount());
            
            totalSelectedPrice = totalSelectedPrice - (selectedDiscountType.getDiscount() / 100 * totalSelectedPrice);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Redeemed Successfully!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        
        return null;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public Double getFlightDuration() {
        return flightDuration;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getOriginCity() {
        return originCity;
    }

    public String getOriginIATA() {
        return originIATA;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getDestinationIATA() {
        return destinationIATA;
    }

    public Double getDistance() {
        return distance;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public DiscountType getSelectedDiscountType() {
        return selectedDiscountType;
    }

    public void setSelectedDiscountType(DiscountType selectedDiscountType) {
        this.selectedDiscountType = selectedDiscountType;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public void setOriginIATA(String originIATA) {
        this.originIATA = originIATA;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public void setDestinationIATA(String destinationIATA) {
        this.destinationIATA = destinationIATA;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<String> getTransitHubs() {
        return transitHubs;
    }

    public List<Schedule> getLegOneSchedules() {
        return legOneSchedules;
    }

    public List<Schedule> getLegTwoSchedules() {
        return legTwoSchedules;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setTransitHubs(List<String> transitHubs) {
        this.transitHubs = transitHubs;
    }

    public void setLegOneSchedules(List<Schedule> legOneSchedules) {
        this.legOneSchedules = legOneSchedules;
    }

    public void setLegTwoSchedules(List<Schedule> legTwoSchedules) {
        this.legTwoSchedules = legTwoSchedules;
    }

    public List<Schedule> getDirectFlightSchedules() {
        return directFlightSchedules;
    }

    public void setDirectFlightSchedules(List<Schedule> directFlightSchedules) {
        this.directFlightSchedules = directFlightSchedules;
    }

    public List<Schedule> getOneStopFlightSchedules() {
        return oneStopFlightSchedules;
    }

    public void setOneStopFlightSchedules(List<Schedule> oneStopFlightSchedules) {
        this.oneStopFlightSchedules = oneStopFlightSchedules;
    }

    public String getDirectFlightDuration() {
        return directFlightDuration;
    }

    public void setDirectFlightDuration(String directFlightDuration) {
        this.directFlightDuration = directFlightDuration;
    }

    public List<String> getOneStopFlightDuration() {
        return oneStopFlightDuration;
    }

    public void setOneStopFlightDuration(List<String> oneStopFlightDuration) {
        this.oneStopFlightDuration = oneStopFlightDuration;
    }

    public List<String> getOneStopFlightLayover() {
        return oneStopFlightLayover;
    }

    public void setOneStopFlightLayover(List<String> oneStopFlightLayover) {
        this.oneStopFlightLayover = oneStopFlightLayover;
    }

    public List<String> getOrigins() {
        return origins;
    }

    public void setOrigins(List<String> origins) {
        this.origins = origins;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public Date getTempDepartureDate() {
        return tempDepartureDate;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setTempDepartureDate(Date tempDepartureDate) {
        this.tempDepartureDate = tempDepartureDate;
    }

    public Date getTempReturnDate() {
        return tempReturnDate;
    }

    public void setTempReturnDate(Date tempReturnDate) {
        this.tempReturnDate = tempReturnDate;
    }

    public List<Double> getMinPricesForWeekDirectFlight() {
        return minPricesForWeekDirectFlight;
    }

    public void setMinPricesForWeekDirectFlight(List<Double> minPricesForWeekDirectFlight) {
        this.minPricesForWeekDirectFlight = minPricesForWeekDirectFlight;
    }

    public List<Double> getMinPricesForWeekOneStopFlight() {
        return minPricesForWeekOneStopFlight;
    }

    public void setMinPricesForWeekOneStopFlight(List<Double> minPricesForWeekOneStopFlight) {
        this.minPricesForWeekOneStopFlight = minPricesForWeekOneStopFlight;
    }
    

    public List<Double> getSelectedDatePrices() {
        return selectedDatePrices;
    }

    public void setSelectedDatePrices(List<Double> selectedDatePrices) {
        this.selectedDatePrices = selectedDatePrices;
    }

    public boolean isIsReturnDateSet() {
        return isReturnDateSet;
    }

    public void setIsReturnDateSet(boolean isReturnDateSet) {
        this.isReturnDateSet = isReturnDateSet;
    }

    public List<FlightOptions> getFlightOptionsList() {
        return flightOptionsList;
    }

    public void setFlightOptionsList(List<FlightOptions> flightOptionsList) {
        this.flightOptionsList = flightOptionsList;
    }

    public double getOldFinalPrice() {
        return oldFinalPrice;
    }

    public void setOldFinalPrice(double oldFinalPrice) {
        this.oldFinalPrice = oldFinalPrice;
    }

    public Schedule getLegOne() {
        return legOne;
    }

    public void setLegOne(Schedule legOne) {
        this.legOne = legOne;
    }

    public Schedule getLegTwo() {
        return legTwo;
    }

    public void setLegTwo(Schedule legTwo) {
        this.legTwo = legTwo;
    }

    public String getLayover() {
        return layover;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the adultList
     */
    public List<Integer> getAdultList() {
        return adultList;
    }

    /**
     * @param adultList the adultList to set
     */
    public void setAdultList(List<Integer> adultList) {
        this.adultList = adultList;
    }

    /**
     * @return the adultTemp
     */
    public Integer getAdultTemp() {
        return adultTemp;
    }

    /**
     * @param adultTemp the adultTemp to set
     */
    public void setAdultTemp(Integer adultTemp) {
        this.adultTemp = adultTemp;
    }

    public double getPriceForDirect() {
        return priceForDirect;
    }

    public void setPriceForDirect(double priceForDirect) {
        this.priceForDirect = priceForDirect;
    }

    /**
     * @return the selectedDepartureSchedule
     */
    public Schedule getSelectedDepartureSchedule() {
        return selectedDepartureSchedule;
    }

    /**
     * @param selectedDepartureSchedule the selectedDepartureSchedule to set
     */
    public void setSelectedDepartureSchedule(Schedule selectedDepartureSchedule) {
        this.selectedDepartureSchedule = selectedDepartureSchedule;
    }

    /**
     * @return the selectedReturnSchedule
     */
    public Schedule getSelectedReturnSchedule() {
        return selectedReturnSchedule;
    }

    public int getMileagePoints() {
        return mileagePoints;
    }

    public void setMileagePoints(int mileagePoints) {
        this.mileagePoints = mileagePoints;
    }

    /**
     * @param selectedReturnSchedule the selectedReturnSchedule to set
     */
    public void setSelectedReturnSchedule(Schedule selectedReturnSchedule) {
        this.selectedReturnSchedule = selectedReturnSchedule;
    }

    /**
     * @return the flightDate
     */
    public Date getFlightDate() {
        return flightDate;
    }

    /**
     * @param flightDate the flightDate to set
     */
    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    /**
     * @return the bookingStatus
     */
    public String getBookingStatus() {
        return bookingStatus;
    }

    /**
     * @param bookingStatus the bookingStatus to set
     */
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     * @return the classCode
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * @param classCode the classCode to set
     */
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    /**
     * @return the travellerTitle
     */
    public String getTravellerTitle() {
        return travellerTitle;
    }

    /**
     * @param travellerTitle the travellerTitle to set
     */
    public void setTravellerTitle(String travellerTitle) {
        this.travellerTitle = travellerTitle;
    }

    /**
     * @return the travellerFristName
     */
    public String getTravellerFristName() {
        return travellerFristName;
    }

    /**
     * @param travellerFristName the travellerFristName to set
     */
    public void setTravellerFristName(String travellerFristName) {
        this.travellerFristName = travellerFristName;
    }

    /**
     * @return the travellerLastName
     */
    public String getTravellerLastName() {
        return travellerLastName;
    }

    /**
     * @param travellerLastName the travellerLastName to set
     */
    public void setTravellerLastName(String travellerLastName) {
        this.travellerLastName = travellerLastName;
    }

    /**
     * @return the passportNumber
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * @param passportNumber the passportNumber to set
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public boolean isDiscountTypeApplied() {
        return discountTypeApplied;
    }

    public void setDiscountTypeApplied(boolean discountTypeApplied) {
        this.discountTypeApplied = discountTypeApplied;
    }

    /**
     * @return the customerId
     */
    public long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the isChild
     */
    public boolean isIsChild() {
        return isChild;
    }

    /**
     * @param isChild the isChild to set
     */
    public void setIsChild(boolean isChild) {
        this.isChild = isChild;
    }

    /**
     * @return the boughtInsurance
     */
    public boolean isBoughtInsurance() {
        return boughtInsurance;
    }

    /**
     * @param boughtInsurance the boughtInsurance to set
     */
    public void setBoughtInsurance(boolean boughtInsurance) {
        this.boughtInsurance = boughtInsurance;
    }

    /**
     * @return the insuranceFare
     */
    public double getInsuranceFare() {
        return insuranceFare;
    }

    /**
     * @param insuranceFare the insuranceFare to set
     */
    public void setInsuranceFare(double insuranceFare) {
        this.insuranceFare = insuranceFare;
    }

    /**
     * @return the foodSelection
     */
    public String getFoodSelection() {
        return foodSelection;
    }

    /**
     * @param foodSelection the foodSelection to set
     */
    public void setFoodSelection(String foodSelection) {
        this.foodSelection = foodSelection;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the seatAvail
     */
    public SeatAvailability getSeatAvail() {
        return seatAvail;
    }

    /**
     * @param seatAvail the seatAvail to set
     */
    public void setSeatAvail(SeatAvailability seatAvail) {
        this.seatAvail = seatAvail;
    }

    /**
     * @return the pnr
     */
    public PNR getPnr() {
        return pnr;
    }

    /**
     * @param pnr the pnr to set
     */
    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }

    /**
     * @return the totalWeightAllowed
     */
    public double getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    /**
     * @param totalWeightAllowed the totalWeightAllowed to set
     */
    public void setTotalWeightAllowed(double totalWeightAllowed) {
        this.totalWeightAllowed = totalWeightAllowed;
    }

    /**
     * @return the totalNumBaggeAlloewd
     */
    public int getTotalNumBaggeAlloewd() {
        return totalNumBaggeAlloewd;
    }

    /**
     * @param totalNumBaggeAlloewd the totalNumBaggeAlloewd to set
     */
    public void setTotalNumBaggeAlloewd(int totalNumBaggeAlloewd) {
        this.totalNumBaggeAlloewd = totalNumBaggeAlloewd;
    }

    public List<Schedule> getSelectedSchedules() {
        return selectedSchedules;
    }

    public void setSelectedSchedules(List<Schedule> selectedSchedules) {
        this.selectedSchedules = selectedSchedules;
    }

    public FlightOptions getSelectedDepartureFlightOption() {
        return selectedDepartureFlightOption;
    }

    public void setSelectedDepartureFlightOption(FlightOptions selectedDepartureFlightOption) {
        this.selectedDepartureFlightOption = selectedDepartureFlightOption;
    }

    public FlightOptions getSelectedReturnFlightOption() {
        return selectedReturnFlightOption;
    }

    public void setSelectedReturnFlightOption(FlightOptions selectedReturnFlightOption) {
        this.selectedReturnFlightOption = selectedReturnFlightOption;
    }

    public List<Date> getWeekDates() {
        return weekDates;
    }

    public void setWeekDates(List<Date> weekDates) {
        this.weekDates = weekDates;
    }

    public boolean isIsDirect() {
        return isDirect;
    }

    public void setIsDirect(boolean isDirect) {
        this.isDirect = isDirect;
    }

    public List<Schedule> getSelectedDepartureSchedules() {
        return selectedDepartureSchedules;
    }

    public void setSelectedDepartureSchedules(List<Schedule> selectedDepartureSchedules) {
        this.selectedDepartureSchedules = selectedDepartureSchedules;
    }

    public List<Schedule> getSelectedReturnSchedules() {
        return selectedReturnSchedules;
    }

    public void setSelectedReturnSchedules(List<Schedule> selectedReturnSchedules) {
        this.selectedReturnSchedules = selectedReturnSchedules;
    }

    /**
     * @return the oneWayDepartureDate
     */
    public Date getOneWayDepartureDate() {
        return oneWayDepartureDate;
    }

    /**
     * @param oneWayDepartureDate the oneWayDepartureDate to set
     */
    public void setOneWayDepartureDate(Date oneWayDepartureDate) {
        this.oneWayDepartureDate = oneWayDepartureDate;
    }

    /**
     * @return the oneWayAdult
     */
    public int getOneWayAdult() {
        return oneWayAdult;
    }

    /**
     * @param oneWayAdult the oneWayAdult to set
     */
    public void setOneWayAdult(int oneWayAdult) {
        this.oneWayAdult = oneWayAdult;
    }

    /**
     * @return the oneWayChildren
     */
    public int getOneWayChildren() {
        return oneWayChildren;
    }

    /**
     * @param oneWayChildren the oneWayChildren to set
     */
    public void setOneWayChildren(int oneWayChildren) {
        this.oneWayChildren = oneWayChildren;
    }

    /**
     * @return the oneWayServiceType
     */
    public String getOneWayServiceType() {
        return oneWayServiceType;
    }

    /**
     * @param oneWayServiceType the oneWayServiceType to set
     */
    public void setOneWayServiceType(String oneWayServiceType) {
        this.oneWayServiceType = oneWayServiceType;
    }

    /**
     * @return the oneWayOriginCity
     */
    public String getOneWayOriginCity() {
        return oneWayOriginCity;
    }

    /**
     * @param oneWayOriginCity the oneWayOriginCity to set
     */
    public void setOneWayOriginCity(String oneWayOriginCity) {
        this.oneWayOriginCity = oneWayOriginCity;
    }

    /**
     * @return the oneWayDestinationCity
     */
    public String getOneWayDestinationCity() {
        return oneWayDestinationCity;
    }

    /**
     * @param oneWayDestinationCity the oneWayDestinationCity to set
     */
    public void setOneWayDestinationCity(String oneWayDestinationCity) {
        this.oneWayDestinationCity = oneWayDestinationCity;
    }

    public double getTotalSelectedPrice() {
        return totalSelectedPrice;
    }

    public void setTotalSelectedPrice(double totalSelectedPrice) {
        this.totalSelectedPrice = totalSelectedPrice;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    /**
     * @return the primaryEmail
     */
    public String getPrimaryEmail() {
        return primaryEmail;
    }

    /**
     * @param primaryEmail the primaryEmail to set
     */
    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    /**
     * @return the primaryContactNo
     */
    public String getPrimaryContactNo() {
        return primaryContactNo;
    }

    /**
     * @param primaryContactNo the primaryContactNo to set
     */
    public void setPrimaryContactNo(String primaryContactNo) {
        this.primaryContactNo = primaryContactNo;
    }

    /**
     * @return the creditCard
     */
    public String getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard the creditCard to set
     */
    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * @return the totalPriceWinsurance
     */
    public double getTotalPriceWinsurance() {
        return totalPriceWinsurance;
    }

    /**
     * @param totalPriceWinsurance the totalPriceWinsurance to set
     */
    public void setTotalPriceWinsurance(double totalPriceWinsurance) {
        this.totalPriceWinsurance = totalPriceWinsurance;
    }

    /**
     * @return the csv
     */
    public String getCsv() {
        return csv;
    }

    /**
     * @param csv the csv to set
     */
    public void setCsv(String csv) {
        this.csv = csv;
    }

    /**
     * @return the pnrId
     */
    public String getPnrId() {
        return pnrId;
    }

    /**
     * @param pnrId the pnrId to set
     */
    public void setPnrId(String pnrId) {
        this.pnrId = pnrId;
    }

    /**
     * @return the searchedPNR
     */
    public PNR getSearchedPNR() {
        return searchedPNR;
    }

    /**
     * @param searchedPNR the searchedPNR to set
     */
    public void setSearchedPNR(PNR searchedPNR) {
        this.searchedPNR = searchedPNR;
    }

    public Boolean getOneWayFlight() {
        return oneWayFlight;
    }

    /**
     * @param oneWayFlight the oneWayFlight to set
     */
    public void setOneWayFlight(Boolean oneWayFlight) {
        this.oneWayFlight = oneWayFlight;
    }

    /**
     * @return the adultPrice
     */
    public double getAdultPrice() {
        return adultPrice;
    }

    /**
     * @param adultPrice the adultPrice to set
     */
    public void setAdultPrice(double adultPrice) {
        this.adultPrice = adultPrice;
    }

    /**
     * @return the childPrice
     */
    public double getChildPrice() {
        return childPrice;
    }

    /**
     * @param childPrice the childPrice to set
     */
    public void setChildPrice(double childPrice) {
        this.childPrice = childPrice;
    }

    public CustomerManagedBean getCustomerManagedBean() {
        return customerManagedBean;
    }

    public void setCustomerManagedBean(CustomerManagedBean customerManagedBean) {
        this.customerManagedBean = customerManagedBean;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    /**
     * @return the customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customerEmail the customerEmail to set
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * @return the customerPassword
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * @param customerPassword the customerPassword to set
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    /**
     * @return the isCustomerLoggedOn
     */
    public boolean isIsCustomerLoggedOn() {
        return isCustomerLoggedOn;
    }

    /**
     * @param isCustomerLoggedOn the isCustomerLoggedOn to set
     */
    public void setIsCustomerLoggedOn(boolean isCustomerLoggedOn) {
        this.isCustomerLoggedOn = isCustomerLoggedOn;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the pnrDisplayList
     */
    public PNRDisplay getPnrDisplayList() {
        return pnrDisplayList;
    }

    /**
     * @param pnrDisplayList the pnrDisplayList to set
     */
    public void setPnrDisplayList(PNRDisplay pnrDisplayList) {
        this.pnrDisplayList = pnrDisplayList;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public int getWeightAllowed() {
        return weightAllowed;
    }

    public void setWeightAllowed(int weightAllowed) {
        this.weightAllowed = weightAllowed;
    }

    /**
     * @return the discountCodeApplied
     */
    public boolean isDiscountCodeApplied() {
        return discountCodeApplied;
    }

    /**
     * @param discountCodeApplied the discountCodeApplied to set
     */
    public void setDiscountCodeApplied(boolean discountCodeApplied) {
        this.discountCodeApplied = discountCodeApplied;
    }

}
