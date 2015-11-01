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
import CI.Session.EmailSessionBeanLocal;
import Distribution.Entity.FlightOptions;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import Distribution.Session.DistributionSessionBeanLocal;
import Distribution.Session.PassengerBookingSessionBeanLocal;
import Distribution.Session.TravelAgencySessionBeanLocal;
import Inventory.Entity.Booking;
import Inventory.Entity.SeatAvailability;
import Inventory.Session.PricingSessionBeanLocal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author parthasarthygupta
 */
@Named(value = "travelAgencyManagedBean")
@ManagedBean
@SessionScoped
public class TravelAgencyManagedBean {

    @EJB
    private TravelAgencySessionBeanLocal travelAgencySessionBean;

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;

    @EJB
    private PricingSessionBeanLocal pm;

    @EJB
    private PassengerBookingSessionBeanLocal passengerBookingSessionBean;

    private List<TravelAgency> travelAgencies;
    private TravelAgency selectedAgency;
    private TravelAgencyDisplay travelAgencyDisplay;

    private String name;
    private String address;
    private String primaryContact;
    private String contactNo;
    private double maxCredit;
    private double currentCredit;
    private double commission;
    private String email;
    private TravelAgency travelAgency;

    private String subject;
    private String body;
    private String hashedPassword;
    private String password;

    private String feedbackMessage;

    private boolean isAgencyLoggedOn;
    private Long id;
    private String newPassword;
    private String confirmedPassword;

    private List<PNR> pendingPNRs;
    private List<PNR> allPNRs;

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
    private List<Double> selectedDatePrices;

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

    private boolean isChild;
    private boolean boughtInsurance;
    private double insuranceFare;
    private String foodSelection;
    private boolean isDirect;

    private SeatAvailability seatAvail;

    private PNR pnr;

    private double totalWeightAllowed;
    private int totalNumBaggeAlloewd;

    private double totalSelectedPrice;
    private double totalPriceWinsurance;
    private double adultPrice;
    private double childPrice;
    private double creditUsed;

    private List<Passenger> passengerList;

    private String primaryEmail;
    private String primaryContactNo;

    private PNR searchedPNR;
    private Date systemDate;
    private PNR selectedPNR;
    private String selectedPNRId = "";

    private double currentCommission;
    private double currentSettlement;

    private String selectedMonth;
    
    private List<Schedule> uniqueSchedules;
    private List<String> uniqueTravellerNames;
    private String tempDate;
    private String refundStatus;
    private int weightAllowed;
            

    @PostConstruct
    public void retrieve() {

        setTravelAgencies(travelAgencySessionBean.getAllTravelAgencies());

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
        oneStopFlightSchedules = new ArrayList();
        transitHubs = new ArrayList();
        directFlightSchedules = new ArrayList();
        flightOptionsList = new ArrayList();
        selectedSchedules = new ArrayList();
        selectedDepartureSchedules = new ArrayList();
        selectedReturnSchedules = new ArrayList();
        totalSelectedPrice = 0;
        adultPrice = 0;
        childPrice = 0;
        passengerList = new ArrayList();
        totalPriceWinsurance = 0;
        systemDate = new Date();
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
    
    public void setClassRules(){
         if (serviceType.equals("Economy Saver") || serviceType.equals("Economy Basic")){
            refundStatus = "Not refundable";
            weightAllowed = 15;
        }
        else if (serviceType.equals("Economy Premium")){
            refundStatus = "Refundable";
            weightAllowed = 15;
        }
        else if (serviceType.equals("Business")){
            refundStatus = "Refundable";
            weightAllowed = 30;
        }
        else if (serviceType.equals("First Class")){
            refundStatus = "Refundable";
            weightAllowed = 45;
        }
    }

    public String displayDepartureFlights(Boolean oneWay) {
        

        oneWayFlight = oneWay;
        /*Convert the chosen origin and destination cities into IATAs*/
        List<Flight> allFlights = new ArrayList();
        allFlights = distributionSessionBean.getAllFlights();
        flightOptionsList = new ArrayList();
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

                    double priceForOne = pm.getPrice(pm.getClassCode(eachSchedule, serviceType, adults + children, true), eachSchedule);
                    selectedDatePrices.add(priceForOne);

                }
                setIsDirect(true);
                setPriceForDirect(selectedDatePrices.get(0));

                return "TravelAgencyDirectFlight";

            } else { //Retrieve one stop flights
                legOneSchedules.clear();
                legTwoSchedules.clear();
                oneStopFlightDuration = new ArrayList();
                oneStopFlightLayover = new ArrayList();
                oneStopFlightSchedules = new ArrayList();

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
                    priceForOne += pm.getPrice(pm.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults + children, true), oneStopFlightSchedules.get(i));
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

                for (int a = 0; a < oneStopFlightSchedules.size(); a++) {
                    FlightOptions newFlightOptions = new FlightOptions();
                    int b = a;
                    a += 1;
                    newFlightOptions.createFlightOptions(oneStopFlightSchedules.get(b), oneStopFlightSchedules.get(a), oneStopFlightLayover.get(b - c), oneStopFlightDuration.get(b - c), selectedDatePrices.get(b - c));
                    distributionSessionBean.persistFlightOptions(newFlightOptions);
                    flightOptionsList.add(newFlightOptions);
                    c++;

                }

                return "TravelAgencyOneStopFlight";
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

    public String displayReturnFlights() {

        directFlightSchedules.clear();
        selectedDatePrices.clear();
        setDirectFlightDuration("");

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
                double priceForOne = pm.getPrice(pm.getClassCode(eachSchedule, serviceType, adults + children, true), eachSchedule);
                selectedDatePrices.add(priceForOne);
            }

            return "TravelAgencyDirectFlightReturn";
        } else { //Retrieve one stop flights

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
                priceForOne += pm.getPrice(pm.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults + children, true), oneStopFlightSchedules.get(i));
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

            return "TravelAgencyOneStopFlightReturn";
        }

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
            priceForEachSchedule = pm.getPrice(pm.getClassCode(eachSelectedSchedule, serviceType, (adults + children), true), eachSelectedSchedule);
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

        return "SummaryForTravelAgency";

    }

    public String checkBalance() {

        if (totalSelectedPrice > travelAgency.getCurrentCredit()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Not enough credit!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;

        }

        return "TravelAgencyBooking";
    }

    public String createBooking() {

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
            }
        }

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA()), distributionSessionBean.getSingaporeTimeZone()));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA()), distributionSessionBean.getSingaporeTimeZone()));
        }

        double priceForEachBooking;
        bookingStatus = "Pending";
        List<Booking> bookingList = new ArrayList();

        totalPriceWinsurance = totalSelectedPrice;

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            classCode = pm.getClassCode(eachSelectedSchedule, serviceType, (adults + children), true);
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
                if (passengerList.get(k).isInsurance()) {
                    totalPriceWinsurance += 15.0;
                }
            }
        }
        pnr = passengerBookingSessionBean.createPNR((adults + children), getPrimaryEmail(), getPrimaryContactNo(), "Pending", totalPriceWinsurance, new Date(), null, "TravelAgency");

        passengerBookingSessionBean.persistBookingAndPNR(pnr, bookingList, null);

        for (Schedule eachSelectedSchedule : selectedSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
        }

        travelAgencySessionBean.deductCredit(travelAgency, totalSelectedPrice);
        travelAgencySessionBean.linkPNR(travelAgency, pnr);
        
        
    setOneWayDepartureDate(null);
    setOneWayAdult(0);
    setOneWayChildren(0);
    setOneWayServiceType(null);
    setOneWayOriginCity(null);
    setOneWayDestinationCity(null);
    
    setDestinationCity(null);
    setOriginCity(null);
    setDepartureDate(null);
    setReturnDate(null);
    setAdults (0);
    setChildren(0);
    

        return "TravelAgencyConfirmation";
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

    public void addTravelAgency() {

        if (travelAgencySessionBean.emailExists(email)) {
            setFeedbackMessage("Please use a different email, this email already exists!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setEmail(email);

            setPassword(emailSessionBean.passGen());

            travelAgencySessionBean.addTravelAgency(name, maxCredit, maxCredit, 0.0, email, address, contactNo, password, primaryContact);

            setTravelAgency(travelAgencySessionBean.getAgencyUseEmail(email));
            sendEmail(getEmail());

            setFeedbackMessage("Travel Agency is registered successfully!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);

            setTravelAgencies(travelAgencySessionBean.getAllTravelAgencies());
            clear();
        }

    }

    public void clear() {
        setEmail(null);
        setPassword(null);
        setName(null);
        setMaxCredit(0.0);
        setAddress(null);
        setContactNo(null);
        setPrimaryContact(null);
        setTravelAgency(null);
    }

    public void sendEmail(String email) {

        setSubject("*Confidential*--- Merlion Airelines Travel Agency Account ");
        setBody("Welcome " + travelAgency.getName() + "!\n You can now log into our system using your email and password: " + password + "\nTravel Agency Id: " + travelAgency.getId()
                + "\n\nTravel Agency Id is a unique identification number provided to each agency."
                + "\nPlease log in to the system using your email, not the Travel Agency Id"
                + "\n\nThank you.");

        emailSessionBean.sendEmail(email, getSubject(), getBody());

    }

    public String loginCheck() {

        if (doLogin(email, password)) {
            setIsAgencyLoggedOn(true);
            setTravelAgency(travelAgencySessionBean.getAgencyUseEmail(email));
            return "TravelAgencySearchFlights";

            //return "CustomerDashboard";
        } else {
            return "";
        }

    }

    public Boolean doLogin(String agencyEmail, String agencyPassword) {
        if (agencyEmail.equals("") || agencyPassword.equals("")) {
            setFeedbackMessage("Please enter your email or password!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return false;
        } else {
            setTravelAgency(travelAgencySessionBean.getAgencyUseEmail(agencyEmail));
            if (getTravelAgency() == null) {
                setFeedbackMessage("You have entered an invalid email!");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return false;
            } else if (travelAgencySessionBean.isSameHash(agencyEmail, agencyPassword)) {
                return true;
                //means the password and email match
//                  redirect();
            } else {
                setFeedbackMessage("Username and password do not match");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return false;
            }
        }
    }

    public void validateUser(ActionEvent event) {
        setEmail(travelAgencySessionBean.validateUser(email, getId())); //get employee's email address 
        FacesMessage message = null;
        if (getEmail().equals("nomatch")) {

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Name and Travel Agency Id do not match!", "");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else if (getEmail().equals("nouser")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Such User Name!", "");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setPassword(emailSessionBean.passGen());
            travelAgencySessionBean.hashNewPwd(email, getPassword());

            sendNewPasswordEmail(getEmail());

            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password has been sent to your email!", "");
            RequestContext.getCurrentInstance().update("growll");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void sendNewPasswordEmail(String email) {

        setSubject("*Confidential*--- Merlion Airelines Travel Agency New Password ");
        setBody("Your new password: " + password);
        emailSessionBean.sendEmail(email, getSubject(), getBody());

    }

    public String changePassword() {
        if (!travelAgencySessionBean.isSameHash(email, password)) {
            setFeedbackMessage("Incorrect old password!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "TravelAgencyChangePassword";

        } else if (!newPassword.equals(confirmedPassword)) {
            setFeedbackMessage("The passwords you entered do not match!");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, feedbackMessage, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "TravelAgencyChangePassword";
        } else {

            travelAgency.setPassword(travelAgencySessionBean.newPassword(email, newPassword));
            travelAgencySessionBean.updateAgencyProfile(travelAgency);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password has been updated!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

        return "TravelAgencySearchFlights";

    }

    public String logout() {

        setTravelAgency(null);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged Out Successfully!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "TravelAgencyLogin";
    }

    public String viewAccount() {
        setMaxCredit(travelAgency.getMaxCredit());
        setCurrentCredit(travelAgency.getCurrentCredit());
        setCreditUsed(maxCredit - currentCredit);

        return "TravelAgencyViewAccount?faces-redirect=true";
    }

    public String updateProfile() {

        name = travelAgency.getName();
        email = travelAgency.getEmail();
        primaryContact = travelAgency.getPrimaryContact();
        contactNo = travelAgency.getContactNo();
        address = travelAgency.getAddress();
        maxCredit = travelAgency.getMaxCredit();
        currentCredit = travelAgency.getCurrentCredit();
        id = travelAgency.getId();

        return "TravelAgencyUpdateProfile";
    }

    public String persistUpdatedProfile() {

        travelAgency.setName(name);
        travelAgency.setEmail(email);
        travelAgency.setPrimaryContact(primaryContact);
        travelAgency.setContactNo(contactNo);
        travelAgency.setAddress(address);

        travelAgencySessionBean.updateAgencyProfile(travelAgency);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your profile has been updated!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "TravelAgencyDashboard?faces-redirect=true";
    }

    public String viewTravelAgencies() {
        if (travelAgencies == null || travelAgencies.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No record found!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return "/Distribution/ViewAllTravelAgencies?faces-redirect=true";
    }

    public String viewTravelAgencyProfile(Long id) {
        travelAgencyDisplay = new TravelAgencyDisplay();

        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));

        int noOfBookings = travelAgencySessionBean.noOfConfirmedBookings(travelAgency);
        travelAgencyDisplay.setTravelAgency(travelAgency);
        travelAgencyDisplay.setNoOfConfirmedBookings(noOfBookings);

        return "ViewTravelAgencyProfile?faces-redirect=true";
    }

    public String manageTravelAgency(Long id) {
        travelAgencyDisplay = new TravelAgencyDisplay();

        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));
        travelAgencyDisplay.setTravelAgency(travelAgency);

        setCurrentSettlement(travelAgencySessionBean.getCurrentMonthSettlement(travelAgency, new Date()));
        setCurrentCommission(0.1 * currentSettlement);

        return "ManageTravelAgency?faces-redirect=true";

    }

    public String deletePNRs() {

        if (travelAgencySessionBean.deletePendingPNRs() <= 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No expired pending PNRs!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "All pending PNRs have been deleted!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return "SalesDepartmentDashboard?faces-redirect=true";

    }

    public void reset() {

        travelAgencySessionBean.resetCreditsAndCommission(travelAgency, currentSettlement);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Credits and Commission have been reset!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void changeLimit() {

        travelAgencySessionBean.changeCreditLimit(travelAgency, maxCredit);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Limit has been changed!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        maxCredit = 0;
    }

    public String deleteTravelAgency(Long id) {

        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));

        travelAgencySessionBean.deleteTravelAgency(travelAgency);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Travel Agency has been deleted!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        setTravelAgencies(travelAgencySessionBean.getAllTravelAgencies());

        return "ViewAllTravelAgencies?faces-redirect=true";
    }

    public String viewPNRs(Long id) {

        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));
        setPendingPNRs(travelAgencySessionBean.retrievePendingPNRs(travelAgency));

        return "ViewTravelAgencyPNRs?faces-redirect=true";
    }

    public String confirmPNR(PNR pnr) {
        
        setSelectedPNR(pnr);
        
        if (selectedPNR.getPnrStatus().equals("Cancelled")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelled PNR cannot be confirmed!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;

        }

        if (selectedPNR.getPnrStatus().equals("Confirmed")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "PNR is already confirmed!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;

        }

        double purePrice = selectedPNR.getTotalPrice();

        for (int i = 0; i < selectedPNR.getBookings().size(); i++) {
            if (selectedPNR.getBookings().get(i).isBoughtInsurance()) {
                purePrice -= 15.0;
            }
        }

        travelAgencySessionBean.confirmPNR(travelAgency, selectedPNR, purePrice);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "PNR has been confirmed!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "TravelAgencyViewPNRs";
    }

    public String cancelPNR(PNR pnr) {

       
        setSelectedPNR(pnr);
        
        if (selectedPNR.getPnrStatus().equals("Cancelled")) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "PNR is already cancelled!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;

        }

        double purePrice = selectedPNR.getTotalPrice();

        for (int i = 0; i < selectedPNR.getBookings().size(); i++) {
            if (selectedPNR.getBookings().get(i).isBoughtInsurance()) {
                purePrice -= 15.0;
            }
        }

        travelAgencySessionBean.cancelPNR(travelAgency, selectedPNR, purePrice);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "PNR has been cancelled!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "TravelAgencyViewPNRs?faces-redirect=true";
    }

    public String viewSettlementAndCommission(Long id) {
        
        setSelectedMonth(null);
        setTravelAgency(travelAgencySessionBean.getTravelAgencyById(id));

        return "SelectMonth?faces-redirect=true";

    }

    public String calculateSettlementAndCommission() {

        setCurrentSettlement(0);
        setCurrentCommission(0);

        for (int i = 0; i < selectedMonth.length(); i++) {
            if (i != 2) {
                if (!(selectedMonth.charAt(i) >= '0' && selectedMonth.charAt(i) <= '9')) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid date entered!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }

            }
        }
        int month = Integer.parseInt(selectedMonth.substring(0, 2));
        if (!(month >= 1 && month <= 12) || selectedMonth.length() != 7 || selectedMonth.charAt(2) != '/') {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Date should be in MM/YYYY format!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        int year = Integer.parseInt(selectedMonth.substring(3, 7));
        String dateFormat = "01" + month + year;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        try {
            Date date = formatter.parse(dateFormat);
            setCurrentSettlement(travelAgencySessionBean.getCurrentMonthSettlement(travelAgency, date));
            setCurrentCommission(0.1 * currentSettlement);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (currentSettlement == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No records found!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        
        setTempDate(selectedMonth);
        setSelectedMonth(null);
        
        return "ViewSettlementAndCommission";
    }

    public String agencyCalculateSettlementAndCommission() {

        setCurrentSettlement(0);
        setCurrentCommission(0);

        for (int i = 0; i < selectedMonth.length(); i++) {
            if (i != 2) {
                if (!(selectedMonth.charAt(i) >= '0' && selectedMonth.charAt(i) <= '9')) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid date entered!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }

            }
        }
        int month = Integer.parseInt(selectedMonth.substring(0, 2));
        if (!(month >= 1 && month <= 12) || selectedMonth.length() != 7 || selectedMonth.charAt(2) != '/') {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Date should be in MM/YYYY format!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        int year = Integer.parseInt(selectedMonth.substring(3, 7));
        String dateFormat = "01" + month + year;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        try {
            Date date = formatter.parse(dateFormat);
            setCurrentSettlement(travelAgencySessionBean.getCurrentMonthSettlement(travelAgency, date));
            setCurrentCommission(0.1 * currentSettlement);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (currentSettlement == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No records found!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        
        setTempDate(selectedMonth);
        setSelectedMonth(null);

        return "TravelAgencyViewSettlementAndCommission?faces-redirect=true";
    }
    
    public String viewPNRDetails(PNR pnr) {

        setSelectedPNR(pnr);
        
        List<Long> addedSchedules = new ArrayList();
        uniqueSchedules = new ArrayList();
        uniqueTravellerNames = new ArrayList();

        
        for (Booking eachBooking : selectedPNR.getBookings()) {

            if (!uniqueTravellerNames.contains(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName())) {
                uniqueTravellerNames.add(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName());
            }

            if (!addedSchedules.contains(eachBooking.getSeatAvail().getSchedule().getScheduleId())) {
                addedSchedules.add(eachBooking.getSeatAvail().getSchedule().getScheduleId());
                uniqueSchedules.add(eachBooking.getSeatAvail().getSchedule());

            }
        }

        for (Schedule eachSelectedSchedule : uniqueSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
        }

        return "TravelAgencyViewPNRDetails?faces-redirect=true";
    }
    
    public String clearForm() {
        setSelectedMonth(null);
        
        return "TravelAgencySelectsMonth?faces-redirect=true";
    }

        public String travelAgencyPNRDetails(PNR pnr) {

        setSelectedPNR(pnr);
        
        List<Long> addedSchedules = new ArrayList();
        uniqueSchedules = new ArrayList();
        uniqueTravellerNames = new ArrayList();

        
        for (Booking eachBooking : selectedPNR.getBookings()) {

            if (!uniqueTravellerNames.contains(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName())) {
                uniqueTravellerNames.add(eachBooking.getTravellerFristName() + " " + eachBooking.getTravellerLastName());
            }

            if (!addedSchedules.contains(eachBooking.getSeatAvail().getSchedule().getScheduleId())) {
                addedSchedules.add(eachBooking.getSeatAvail().getSchedule().getScheduleId());
                uniqueSchedules.add(eachBooking.getSeatAvail().getSchedule());

            }
        }

        for (Schedule eachSelectedSchedule : uniqueSchedules) {
            eachSelectedSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getOriginIATA())));
            eachSelectedSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSelectedSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSelectedSchedule.getFlight().getRoute().getDestinationIATA())));
        }

        return "SalesDepartmentViewPNRDetails?faces-redirect=true";
    }
    /**
     * @return the travelAgencies
     */
    public List<TravelAgency> getTravelAgencies() {
        return travelAgencies;
    }

    /**
     * @param travelAgencies the travelAgencies to set
     */
    public void setTravelAgencies(List<TravelAgency> travelAgencies) {
        this.travelAgencies = travelAgencies;
    }

    /**
     * @return the selectedAgency
     */
    public TravelAgency getSelectedAgency() {
        return selectedAgency;
    }

    /**
     * @param selectedAgency the selectedAgency to set
     */
    public void setSelectedAgency(TravelAgency selectedAgency) {
        this.selectedAgency = selectedAgency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public double getMaxCredit() {
        return maxCredit;
    }

    public void setMaxCredit(double maxCredit) {
        this.maxCredit = maxCredit;
    }

    public double getCurrentCredit() {
        return currentCredit;
    }

    public void setCurrentCredit(double currentCredit) {
        this.currentCredit = currentCredit;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the travelAgency
     */
    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

    /**
     * @param travelAgency the travelAgency to set
     */
    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
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
     * @return the hashedPassword
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @param hashedPassword the hashedPassword to set
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the feedbackMessage
     */
    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    /**
     * @param feedbackMessage the feedbackMessage to set
     */
    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    /**
     * @return the isAgencyLoggedOn
     */
    public boolean isIsAgencyLoggedOn() {
        return isAgencyLoggedOn;
    }

    /**
     * @param isAgencyLoggedOn the isAgencyLoggedOn to set
     */
    public void setIsAgencyLoggedOn(boolean isAgencyLoggedOn) {
        this.isAgencyLoggedOn = isAgencyLoggedOn;
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
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the confirmedPassword
     */
    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    /**
     * @param confirmedPassword the confirmedPassword to set
     */
    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public TravelAgencyDisplay getTravelAgencyDisplay() {
        return travelAgencyDisplay;
    }

    public void setTravelAgencyDisplay(TravelAgencyDisplay travelAgencyDisplay) {
        this.travelAgencyDisplay = travelAgencyDisplay;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
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

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
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

    public Date getOneWayDepartureDate() {
        return oneWayDepartureDate;
    }

    public void setOneWayDepartureDate(Date oneWayDepartureDate) {
        this.oneWayDepartureDate = oneWayDepartureDate;
    }

    public int getOneWayAdult() {
        return oneWayAdult;
    }

    public void setOneWayAdult(int oneWayAdult) {
        this.oneWayAdult = oneWayAdult;
    }

    public int getOneWayChildren() {
        return oneWayChildren;
    }

    public void setOneWayChildren(int oneWayChildren) {
        this.oneWayChildren = oneWayChildren;
    }

    public String getOneWayServiceType() {
        return oneWayServiceType;
    }

    public void setOneWayServiceType(String oneWayServiceType) {
        this.oneWayServiceType = oneWayServiceType;
    }

    public String getOneWayOriginCity() {
        return oneWayOriginCity;
    }

    public void setOneWayOriginCity(String oneWayOriginCity) {
        this.oneWayOriginCity = oneWayOriginCity;
    }

    public String getOneWayDestinationCity() {
        return oneWayDestinationCity;
    }

    public void setOneWayDestinationCity(String oneWayDestinationCity) {
        this.oneWayDestinationCity = oneWayDestinationCity;
    }

    public Boolean getOneWayFlight() {
        return oneWayFlight;
    }

    public void setOneWayFlight(Boolean oneWayFlight) {
        this.oneWayFlight = oneWayFlight;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginIATA() {
        return originIATA;
    }

    public void setOriginIATA(String originIATA) {
        this.originIATA = originIATA;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationIATA() {
        return destinationIATA;
    }

    public void setDestinationIATA(String destinationIATA) {
        this.destinationIATA = destinationIATA;
    }

    public Double getDistance() {
        return distance;
    }

    public double getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(double creditUsed) {
        this.creditUsed = creditUsed;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public List<String> getTransitHubs() {
        return transitHubs;
    }

    public void setTransitHubs(List<String> transitHubs) {
        this.transitHubs = transitHubs;
    }

    public List<Schedule> getLegOneSchedules() {
        return legOneSchedules;
    }

    public void setLegOneSchedules(List<Schedule> legOneSchedules) {
        this.legOneSchedules = legOneSchedules;
    }

    public List<Schedule> getLegTwoSchedules() {
        return legTwoSchedules;
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

    public FacesMessage getMessage() {
        return message;
    }

    public void setMessage(FacesMessage message) {
        this.message = message;
    }

    public List<Double> getSelectedDatePrices() {
        return selectedDatePrices;
    }

    public void setSelectedDatePrices(List<Double> selectedDatePrices) {
        this.selectedDatePrices = selectedDatePrices;
    }

    public List<Integer> getAdultList() {
        return adultList;
    }

    public void setAdultList(List<Integer> adultList) {
        this.adultList = adultList;
    }

    public Integer getAdultTemp() {
        return adultTemp;
    }

    public void setAdultTemp(Integer adultTemp) {
        this.adultTemp = adultTemp;
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

    public double getPriceForDirect() {
        return priceForDirect;
    }

    public void setPriceForDirect(double priceForDirect) {
        this.priceForDirect = priceForDirect;
    }

    public Schedule getSelectedDepartureSchedule() {
        return selectedDepartureSchedule;
    }

    public void setSelectedDepartureSchedule(Schedule selectedDepartureSchedule) {
        this.selectedDepartureSchedule = selectedDepartureSchedule;
    }

    public Schedule getSelectedReturnSchedule() {
        return selectedReturnSchedule;
    }

    public void setSelectedReturnSchedule(Schedule selectedReturnSchedule) {
        this.selectedReturnSchedule = selectedReturnSchedule;
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

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getTravellerTitle() {
        return travellerTitle;
    }

    public void setTravellerTitle(String travellerTitle) {
        this.travellerTitle = travellerTitle;
    }

    public String getTravellerFristName() {
        return travellerFristName;
    }

    public void setTravellerFristName(String travellerFristName) {
        this.travellerFristName = travellerFristName;
    }

    public String getTravellerLastName() {
        return travellerLastName;
    }

    public void setTravellerLastName(String travellerLastName) {
        this.travellerLastName = travellerLastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public boolean isIsChild() {
        return isChild;
    }

    public void setIsChild(boolean isChild) {
        this.isChild = isChild;
    }

    public boolean isBoughtInsurance() {
        return boughtInsurance;
    }

    public void setBoughtInsurance(boolean boughtInsurance) {
        this.boughtInsurance = boughtInsurance;
    }

    public double getInsuranceFare() {
        return insuranceFare;
    }

    public void setInsuranceFare(double insuranceFare) {
        this.insuranceFare = insuranceFare;
    }

    public String getFoodSelection() {
        return foodSelection;
    }

    public void setFoodSelection(String foodSelection) {
        this.foodSelection = foodSelection;
    }

    public boolean isIsDirect() {
        return isDirect;
    }

    public void setIsDirect(boolean isDirect) {
        this.isDirect = isDirect;
    }

    public SeatAvailability getSeatAvail() {
        return seatAvail;
    }

    public void setSeatAvail(SeatAvailability seatAvail) {
        this.seatAvail = seatAvail;
    }

    public PNR getPnr() {
        return pnr;
    }

    public void setPnr(PNR pnr) {
        this.pnr = pnr;
    }

    public double getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    public void setTotalWeightAllowed(double totalWeightAllowed) {
        this.totalWeightAllowed = totalWeightAllowed;
    }

    public int getTotalNumBaggeAlloewd() {
        return totalNumBaggeAlloewd;
    }

    public void setTotalNumBaggeAlloewd(int totalNumBaggeAlloewd) {
        this.totalNumBaggeAlloewd = totalNumBaggeAlloewd;
    }

    public double getTotalSelectedPrice() {
        return totalSelectedPrice;
    }

    public void setTotalSelectedPrice(double totalSelectedPrice) {
        this.totalSelectedPrice = totalSelectedPrice;
    }

    public double getTotalPriceWinsurance() {
        return totalPriceWinsurance;
    }

    public void setTotalPriceWinsurance(double totalPriceWinsurance) {
        this.totalPriceWinsurance = totalPriceWinsurance;
    }

    public double getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(double adultPrice) {
        this.adultPrice = adultPrice;
    }

    public double getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(double childPrice) {
        this.childPrice = childPrice;
    }

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryContactNo() {
        return primaryContactNo;
    }

    public void setPrimaryContactNo(String primaryContactNo) {
        this.primaryContactNo = primaryContactNo;
    }

    public PNR getSearchedPNR() {
        return searchedPNR;
    }

    public void setSearchedPNR(PNR searchedPNR) {
        this.searchedPNR = searchedPNR;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    /**
     * @return the pendingPNRs
     */
    public List<PNR> getPendingPNRs() {
        return pendingPNRs;
    }

    /**
     * @param pendingPNRs the pendingPNRs to set
     */
    public void setPendingPNRs(List<PNR> pendingPNRs) {
        this.pendingPNRs = pendingPNRs;
    }

    /**
     * @return the selectedPNR
     */
    public PNR getSelectedPNR() {
        return selectedPNR;
    }

    /**
     * @param selectedPNR the selectedPNR to set
     */
    public void setSelectedPNR(PNR selectedPNR) {
        this.selectedPNR = selectedPNR;
    }

    /**
     * @return the selectedPNRId
     */
    public String getSelectedPNRId() {
        return selectedPNRId;
    }

    /**
     * @param selectedPNRId the selectedPNRId to set
     */
    public void setSelectedPNRId(String selectedPNRId) {
        this.selectedPNRId = selectedPNRId;
    }

    /**
     * @return the allPNRs
     */
    public List<PNR> getAllPNRs() {
        return allPNRs;
    }

    /**
     * @param allPNRs the allPNRs to set
     */
    public void setAllPNRs(List<PNR> allPNRs) {
        this.allPNRs = allPNRs;
    }

    /**
     * @return the currentSettlement
     */
    public double getCurrentSettlement() {
        return currentSettlement;
    }

    /**
     * @param currentSettlement the currentSettlement to set
     */
    public void setCurrentSettlement(double currentSettlement) {
        this.currentSettlement = currentSettlement;
    }

    /**
     * @return the currentCommission
     */
    public double getCurrentCommission() {
        return currentCommission;
    }

    /**
     * @param currentCommission the currentCommission to set
     */
    public void setCurrentCommission(double currentCommission) {
        this.currentCommission = currentCommission;
    }

    /**
     * @return the selectedMonth
     */
    public String getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * @param selectedMonth the selectedMonth to set
     */
    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public List<Schedule> getUniqueSchedules() {
        return uniqueSchedules;
    }

    public void setUniqueSchedules(List<Schedule> uniqueSchedules) {
        this.uniqueSchedules = uniqueSchedules;
    }

    public List<String> getUniqueTravellerNames() {
        return uniqueTravellerNames;
    }

    public void setUniqueTravellerNames(List<String> uniqueTravellerNames) {
        this.uniqueTravellerNames = uniqueTravellerNames;
    }

    /**
     * @return the tempDate
     */
    public String getTempDate() {
        return tempDate;
    }

    /**
     * @param tempDate the tempDate to set
     */
    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

}
