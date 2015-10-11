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
import APS.Session.RouteSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import Distribution.Entity.Customer;
import Distribution.Entity.FlightOptions;
import Distribution.Entity.PNR;
import Distribution.Session.DistributionSessionBeanLocal;
import Distribution.Session.PassengerBookingSessionBeanLocal;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import Inventory.Session.PricingManagementLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

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
    private RouteSessionBeanLocal routeSessionBean;

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

    @EJB
    private PricingManagementLocal pricingManagementBean;

    @EJB
    private PassengerBookingSessionBeanLocal passengerBookingSessionBean;

    private String flightNo;
    private Double flightDuration;
    private Double price;

    private Date departureDate;
    private Date returnDate;
    private Date tempDepartureDate;
    private Date tempReturnDate;

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

    private List<Passenger> passengerList;

    private String primaryEmail;
    private String primaryContactNo;

    private String creditCard;
    private String csv;

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
        passengerList = new ArrayList();
        totalPriceWinsurance = 0;
        creditCard = null;
        csv = null;
    }

    public String displayDepartureFlights() {

        /*Convert the chosen origin and destination cities into IATAs*/
        List<Flight> allFlights = new ArrayList();
        allFlights = distributionSessionBean.getAllFlights();
        flightOptionsList = new ArrayList();
        System.out.println("managedbean: display departure flight !!!!!!");
        System.out.println("departure date" + departureDate);
        System.out.println("reture date" + returnDate);
        System.out.println("adults" + adults);
        System.out.println("children" + children);
        System.out.println("service" + serviceType);
        System.out.println("origin city" + originCity);
        System.out.println("destination city" + destinationCity);
        weekDates.clear();
        selectedDepartureSchedules.clear();
        selectedSchedules.clear();

        //verify whether inputs are right
        if (originCity.equals(destinationCity)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Origin and destination cannot be the same!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            setDepartureDate(null);
//            setReturnDate(null);
//            setOriginCity("");
//            setDestinationCity("");
//            setAdults(0);
//            setChildren(0);
//            setServiceType("");
            return null;
        } else if (adults == 0 && children == 0) {
            System.out.println("managedbean: no adult or children chosen");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select at least one passenger!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
//            setDepartureDate(null);
//            setReturnDate(null);
//            setOriginIATA("");
//            setDestinationIATA("");
//            setAdults(0);
//            setChildren(0);
//            setServiceType("");
            return null;
        } else if (returnDate != null) {
            System.out.println("managedbean: return date is not null");
            isReturnDateSet = true;
        }

        {
            for (Flight eachFlight : allFlights) {
                System.out.println("managedbean: go into flight loop");
                if (eachFlight.getRoute().getOriginCity().equalsIgnoreCase(originCity)) {
                    originIATA = eachFlight.getRoute().getOriginIATA();
                }
                if (eachFlight.getRoute().getDestinationCity().equalsIgnoreCase(destinationCity)) {
                    destinationIATA = eachFlight.getRoute().getDestinationIATA();
                }
            }

            boolean inputValid = true;
            //One way jorney selected by user
            if (isReturnDateSet == false) {
                System.out.println("managedbean: user did not choose return date");
                if (distributionSessionBean.existsSchedule(originIATA, destinationIATA, departureDate, serviceType, adults, children) == false) {
                    inputValid = false;
                }
            } //return journey selected by user
            else {
                System.out.println("managedbean: user chose return date");
                if (distributionSessionBean.existsSchedule(originIATA, destinationIATA, departureDate, serviceType, adults, children) == false || distributionSessionBean.existsSchedule(destinationIATA, originIATA, returnDate, serviceType, adults, children) == false) {
                    inputValid = false;
                }
            }
            if (inputValid == false) {
                System.out.println("managedbean: input is not valid");
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

                //Check whether there is direct flight
                if (distributionSessionBean.existsDirectFlight(originIATA, destinationIATA)) {
                    setDirectFlightSchedules(distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, departureDate, serviceType, adults, children));
                    setDirectFlightDuration(distributionSessionBean.getTotalDurationForDirect(directFlightSchedules.get(0)));

                    for (Schedule eachSchedule : directFlightSchedules) {
                        eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                        eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));

                        double priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults + children), eachSchedule);
                        selectedDatePrices.add((adults * priceForOne) + (0.75 * priceForOne * children));

                    }
                    setIsDirect(true);
                    retrieveMinWeekPricesForDirect(originIATA, destinationIATA, departureDate, serviceType, adults, children);
                    setPriceForDirect(selectedDatePrices.get(0));
                    if (!isReturnDateSet) {
                        System.out.println("managedbean: everything is right...returning direct flight page");
                        return "DisplayDirectFlight";
                    } else {
                        System.out.println("managedbean: everything is right...returning departure direct flight page");
                        return "DisplayDepartureDirectFlightReturn";
                    }

                } else { //Retrieve one stop flights
                    legOneSchedules.clear();
                    legTwoSchedules.clear();
                    oneStopFlightDuration.clear();
                    oneStopFlightLayover.clear();

                    retrieveMinWeekPricesForOneStop(originIATA, destinationIATA, departureDate, serviceType, adults, children);
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

                    double flightOptionPrice = 0;
                    double priceForOne = 0;

                    for (i = 0; i < oneStopFlightSchedules.size(); i++) {
                        flightOption.set(i % 2, oneStopFlightSchedules.get(i));
                        priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults + children), oneStopFlightSchedules.get(i));
                        flightOptionPrice += (adults * priceForOne) + (0.75 * priceForOne * children);
                        if (i % 2 == 1) {
                            oneStopFlightDuration.add(distributionSessionBean.getTotalDurationForOneStop(flightOption.get(0), flightOption.get(1)));
                            oneStopFlightLayover.add(distributionSessionBean.getLayoverTime(flightOption.get(0), flightOption.get(1)));
                            selectedDatePrices.add(flightOptionPrice);
                            flightOptionPrice = 0;
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

                    for (int a = 0; a < oneStopFlightSchedules.size(); a++) {
                        FlightOptions newFlightOptions = new FlightOptions();
                        int b = a;
                        a += 1;
                        newFlightOptions.createFlightOptions(oneStopFlightSchedules.get(b), oneStopFlightSchedules.get(a), oneStopFlightLayover.get(b - c), oneStopFlightDuration.get(b - c), selectedDatePrices.get(b - c));
                        flightOptionsList.add(newFlightOptions);
                        c++;

                    }
                    ;

                    if (!isReturnDateSet) {
                        return "DisplayOneStopFlight";
                    } else {
                        return "DisplayDepartureOneStopFlightReturn";
                    }
                }
            }

        }
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
                double priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults + children), eachSchedule);
                selectedDatePrices.add((adults * priceForOne) + (0.75 * priceForOne * children));
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
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, departureDate, serviceType, adults, children));
            }
            setOneStopFlightSchedules(distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules));
            int i;
            List<Schedule> flightOption = new ArrayList();
            flightOption.add(new Schedule());
            flightOption.add(new Schedule());

            double flightOptionPrice = 0;
            double priceForOne = 0;

            for (i = 0; i < oneStopFlightSchedules.size(); i++) {
                flightOption.set(i % 2, oneStopFlightSchedules.get(i));
                priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults + children), oneStopFlightSchedules.get(i));
                flightOptionPrice += (adults * priceForOne) + (0.75 * priceForOne * children);
                if (i % 2 == 1) {
                    oneStopFlightDuration.add(distributionSessionBean.getTotalDurationForOneStop(flightOption.get(0), flightOption.get(1)));
                    oneStopFlightLayover.add(distributionSessionBean.getLayoverTime(flightOption.get(0), flightOption.get(1)));
                    selectedDatePrices.add(flightOptionPrice);
                    flightOptionPrice = 0;
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
                flightOptionsList.add(newFlightOptions);
                c++;
            }

            return "DisplayReturnOneStopFlightReturn";
        }

    }

    public void retrieveMinWeekPricesForDirect(String originIATA, String destinationIATA, Date date, String serviceType, int adults, int children) {
        List<Double> pricesForWeek = new ArrayList();
        List<Schedule> schedulesForEachDate = new ArrayList();
        double minPrice, priceForEachSchedule = 0;
        int i;

        for (i = -3; i <= 3; i++) {
            Date eachDate = distributionSessionBean.addDaysToDate(date, i);
            weekDates.add(eachDate);
            schedulesForEachDate = distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, eachDate, serviceType, adults, children);
            if (schedulesForEachDate.size() > 0) {
                minPrice = 99999999;
                for (Schedule eachSchedule : schedulesForEachDate) {
                    //Store price for each schedule in priceForEachScheduleVariable
                    double priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults + children), eachSchedule);
                    priceForEachSchedule = (adults * priceForOne) + (0.75 * priceForOne * children);

                    if (priceForEachSchedule < minPrice) {
                        minPrice = priceForEachSchedule;
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
                    priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, (adults + children)), eachSchedule);
                    priceForEachFlightOption += ((adults * priceForOne) + (children * 0.75 * priceForOne));

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

        passengerList.clear();

        if (isDirect) {
            selectedSchedules.add(selectedDepartureSchedule);
        } else {
            selectedSchedules.add(selectedDepartureFlightOption.getLegOne());
            selectedSchedules.add(selectedDepartureFlightOption.getLegTwo());
        }
        if (isReturnDateSet) {
            if (isDirect) {
                selectedSchedules.add(selectedReturnSchedule);
            } else {
                selectedSchedules.add(selectedReturnFlightOption.getLegOne());
                selectedSchedules.add(selectedReturnFlightOption.getLegTwo());
            }
        }

        setSelectedDepartureSchedules(passengerBookingSessionBean.getDepartureSchedules(selectedSchedules, isReturnDateSet));

        System.out.println("SELECTED DEPARTURE SCHEDULE LEG ONE: " + selectedDepartureSchedules.get(0).getFlight().getRoute());
//        System.out.println("SELECTED DEPARTURE SCHEDULE LEG TWO: " + selectedDepartureSchedules.get(1).getFlight().getRoute());

        if (isReturnDateSet) {
            setSelectedReturnSchedules(passengerBookingSessionBean.getReturnSchedules(selectedSchedules));
        }

        System.out.println("SELECTED RETURN SCHEDULE LEG ONE: " + selectedReturnSchedules.get(0).getFlight().getRoute());
//        System.out.println("SELECTED RETURN SCHEDULE LEG TWO: " + selectedReturnSchedules.get(1).getFlight().getRoute());

        double priceForEachSchedule;
        for (Schedule eachSelectedSchedule : selectedSchedules) {
            priceForEachSchedule = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSelectedSchedule, serviceType, (adults + children)), eachSelectedSchedule);
            totalSelectedPrice += (priceForEachSchedule * adults) + (priceForEachSchedule * 0.75 * children);
        }
        Passenger passenger;
        for (int k = 0; k < adults + children; k++) {
            passenger = new Passenger();
            passenger.setId(k + 1);
            passengerList.add(passenger);
        }

        return "summary";

    }

    public String payment() {

        for (int i = 0; i < passengerList.size(); i++) {
            if (passengerList.get(i).getCustomerId().isEmpty())
                continue;
            if (!isInteger(passengerList.get(i).getCustomerId())) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Customer Id!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }
        }

        return "payment";
    }

    public String createBooking() {

        System.out.println("INSIDE CREATE BOOKING!!!!!!!!");
        System.out.println("selected Schedule: " + selectedSchedules);
        System.out.println("Passenger: " + passengerList.get(0).getFirstName());

//        if (!isInteger(creditCard)) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid credit card number!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return null;
//        }

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

        double priceForEachBooking;
        bookingStatus = "Booked";
        List<Booking> bookingList = new ArrayList();
        Customer primaryCustomer = new Customer();
        
        totalPriceWinsurance = totalSelectedPrice;

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            classCode = pricingManagementBean.getClassCode(eachSelectedSchedule, serviceType, (adults + children));
            priceForEachBooking = pricingManagementBean.getPrice(classCode, eachSelectedSchedule);
            setSeatAvail(eachSelectedSchedule.getSeatAvailability());
            setFlightNo(eachSelectedSchedule.getFlight().getFlightNo());
            setFlightDate(eachSelectedSchedule.getStartDate());
            for (int k = 0; k < passengerList.size(); k++) {
                if (passengerList.get(k).getCustomerId() == null)
                    passengerList.get(k).setCustomerId("0");
                Booking eachBooking = passengerBookingSessionBean.createBooking(priceForEachBooking, seatAvail, flightNo, flightDate, bookingStatus, classCode, serviceType, passengerList.get(k).getTitle(), passengerList.get(k).getFirstName(), passengerList.get(k).getLastName(), passengerList.get(k).getPassport(), passengerList.get(k).getNationality(), Long.parseLong(passengerList.get(k).getCustomerId()), false, passengerList.get(k).isInsurance(), 15.0, passengerList.get(k).getFoodSelection());
                bookingList.add(eachBooking);
                if (passengerList.get(k).isInsurance()) {
                    totalPriceWinsurance += 15.0;
                }
            }
            pnr = passengerBookingSessionBean.createPNR((adults + children), getPrimaryEmail(), getPrimaryContactNo(), "Booked", totalSelectedPrice, new Date(), "MerlionAirlines");
            if (passengerBookingSessionBean.isPassengerAFrequentFlyer(Long.parseLong(passengerList.get(0).getCustomerId()))) {
                primaryCustomer = passengerBookingSessionBean.getCustomerByCustomerId(Long.parseLong(passengerList.get(0).getCustomerId()));
            } else {
                primaryCustomer = null;
            }
        }
        passengerBookingSessionBean.persistBookingAndPNR(pnr, bookingList, primaryCustomer);

        return "confirmation";
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

}
