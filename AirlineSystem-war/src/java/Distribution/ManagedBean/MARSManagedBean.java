/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Flight;
import APS.Entity.Location;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.RouteSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import Distribution.Session.DistributionSessionBeanLocal;
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

    private String directFlightDuration;
    
    private List<String> oneStopFlightDuration;
    private List<String> oneStopFlightLayover;

    private List<String> origins;
    private List<String> destinations;
    FacesMessage message = null;
    
    //Price variables
    private List<Double> minPricesForWeekDirectFlight;
    private List<Double> minPricesForWeekOneStopFlight;
    private List<Double> selectedDatePrices;

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
            if(!temp1.contains(eachRoute.getOriginCity()))
                temp1.add(eachRoute.getOriginCity());
            if(!temp2.contains(eachRoute.getDestinationCity()))
                temp2.add(eachRoute.getDestinationCity());
        }
        setOrigins(temp1);
        setDestinations(temp2);

        legOneSchedules = new ArrayList();
        legTwoSchedules = new ArrayList();
        oneStopFlightDuration = new ArrayList();
        oneStopFlightLayover = new ArrayList();
        returnDate = null;
        selectedDatePrices = new ArrayList();
    }

    public String displayDepartureFlights() {
        
        /*Convert the chosen origin and destination cities into IATAs*/
        List<Flight> allFlights = new ArrayList();
        allFlights = distributionSessionBean.getAllFlights();
        
        for (Flight eachFlight : allFlights) {
            if (eachFlight.getRoute().getOriginCity().equalsIgnoreCase(originCity))
                originIATA = eachFlight.getRoute().getOriginIATA();
            if (eachFlight.getRoute().getDestinationCity().equalsIgnoreCase(destinationCity))
                destinationIATA = eachFlight.getRoute().getDestinationIATA();
        }
        
        System.out.println("MANAGE BEAN - Origin IATA:::: " + originIATA);
        System.out.println("MANAGE BEAN - Destination IATA:::: " + destinationIATA);
        
        boolean inputValid = true;  
        //One way jorney selected by user
        if (returnDate == null) {
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
            setDepartureDate(distributionSessionBean.convertTimeZone(departureDate, distributionSessionBean.getTimeZoneFromIata(originIATA), distributionSessionBean.getSingaporeTimeZone()));
            if (returnDate != null) {
                setReturnDate(distributionSessionBean.convertTimeZone(returnDate, distributionSessionBean.getTimeZoneFromIata(destinationIATA), distributionSessionBean.getSingaporeTimeZone()));
            }
            selectedDatePrices.clear();

            //Check whether there is direct flight
            if (distributionSessionBean.existsDirectFlight(originIATA, destinationIATA)) {
                setDirectFlightSchedules(distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, departureDate, serviceType, adults, children));
                setDirectFlightDuration(distributionSessionBean.getTotalDurationForDirect(directFlightSchedules.get(0)));

                for (Schedule eachSchedule : directFlightSchedules) {
                    eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                    eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));
                    System.out.println("!!!!!!!!!!!!!!!Start Date:" + eachSchedule.getStartDate());
                    System.out.println("!!!!!!!!!!!!!!!End Date:" + eachSchedule.getEndDate());
                    
                    double priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults+children), eachSchedule);
                    selectedDatePrices.add((adults*priceForOne)+(0.75*priceForOne*children));
                    System.out.println("!!!!!!!!!!!!!Price:" + priceForOne);
                }
                retrieveMinWeekPricesForDirect(originIATA, destinationIATA, departureDate, serviceType, adults, children);
                if (returnDate == null) {
                    return "DisplayDirectFlight";
                } else {
                    return "DisplayDepartureDirectFlightReturn";
                }
            } else { //Retrieve one stop flights

                retrieveMinWeekPricesForOneStop(originIATA,destinationIATA,departureDate,serviceType,adults,children);
                legOneSchedules.clear();
                legTwoSchedules.clear();
                oneStopFlightDuration.clear();
                oneStopFlightLayover.clear();
                transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);
                for (int i = 0; i < transitHubs.size(); i++) {
                    addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(i), departureDate, serviceType, adults, children));
                    addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, distributionSessionBean.addDaysToDate(departureDate,1), serviceType, adults, children));
                    addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, departureDate, serviceType, adults, children));
                }
                setOneStopFlightSchedules(distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules));
                int i;
                List<Schedule> flightOption = new ArrayList();
                flightOption.add(new Schedule());
                flightOption.add(new Schedule());
                
                double flightOptionPrice=0;
                double priceForOne = 0;
         
                
                for (i = 0; i < oneStopFlightSchedules.size(); i++) {
                    flightOption.set(i % 2, oneStopFlightSchedules.get(i));
                    priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults+children), oneStopFlightSchedules.get(i));
                    flightOptionPrice += (adults*priceForOne) + (0.75*priceForOne*children);
                    if (i % 2 == 1) {
                        oneStopFlightDuration.add(distributionSessionBean.getTotalDurationForOneStop(flightOption.get(0), flightOption.get(1)));
                        oneStopFlightLayover.add(distributionSessionBean.getLayoverTime(flightOption.get(0), flightOption.get(1)));
                        selectedDatePrices.add(flightOptionPrice);
                        flightOptionPrice = 0;
                    }
                }

                for (Schedule eachSchedule : oneStopFlightSchedules) {
                    eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                    eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));

                }
                if (returnDate == null) {
                    return "DisplayOneStopFlight";
                } else {
                    return "DisplayDepartureOneStopFlightReturn";
                }
            }
        }
    }

    public String displayReturnFlights() {
        
        /*Convert the chosen origin and destination cities into IATAs*/
        List<Route> allRoutes = new ArrayList<Route>();
        allRoutes = routeSessionBean.retrieveRoutes();
        
        for (Route eachRoute : allRoutes) {
            if (eachRoute.getOriginCity().equals(originCity) && eachRoute.getDestinationCity().equals(destinationCity)) {
                originIATA = eachRoute.getOriginIATA();
                destinationIATA = eachRoute.getDestinationIATA();
            }
                
        }
        
        //switching origin and destination
        String tempOrigin = originIATA;
        setOriginIATA(destinationIATA);
        setDestinationIATA(tempOrigin);

        setTempDepartureDate(departureDate);
        setTempReturnDate(returnDate);
        setDepartureDate(returnDate);
        selectedDatePrices.clear();

        //Check whether there is direct flight
        if (distributionSessionBean.existsDirectFlight(originIATA, destinationIATA)) {
            setDirectFlightSchedules(distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, departureDate, serviceType, adults, children));
            setDirectFlightDuration(distributionSessionBean.getTotalDurationForDirect(directFlightSchedules.get(0)));

            for (Schedule eachSchedule : directFlightSchedules) {
                eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));
                double priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults+children), eachSchedule);
                selectedDatePrices.add((adults*priceForOne)+(0.75*priceForOne*children));
            }
            retrieveMinWeekPricesForDirect(originIATA, destinationIATA, departureDate, serviceType, adults, children);

            return "DisplayReturnDirectFlightReturn";
        } else { //Retrieve one stop flights
            
            retrieveMinWeekPricesForOneStop(originIATA,destinationIATA,departureDate,serviceType,adults,children);
            legOneSchedules.clear();
            legTwoSchedules.clear();
            oneStopFlightDuration.clear();
            oneStopFlightLayover.clear();
            transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);
            for (int i = 0; i < transitHubs.size(); i++) {
                addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(i), departureDate, serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, distributionSessionBean.addDaysToDate(departureDate,1), serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(i), destinationIATA, departureDate, serviceType, adults, children));
            }
            setOneStopFlightSchedules(distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules));
            int i;
            List<Schedule> flightOption = new ArrayList();
            flightOption.add(new Schedule());
            flightOption.add(new Schedule());
            
            double flightOptionPrice=0;
            double priceForOne = 0;
                
            for (i = 0; i < oneStopFlightSchedules.size(); i++) {
                 flightOption.set(i % 2, oneStopFlightSchedules.get(i));
                 priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(oneStopFlightSchedules.get(i), serviceType, adults+children), oneStopFlightSchedules.get(i));
                 flightOptionPrice += (adults*priceForOne) + (0.75*priceForOne*children);
                if (i % 2 == 1) {
                    oneStopFlightDuration.add(distributionSessionBean.getTotalDurationForOneStop(flightOption.get(0), flightOption.get(1)));
                    oneStopFlightLayover.add(distributionSessionBean.getLayoverTime(flightOption.get(0), flightOption.get(1)));
                    selectedDatePrices.add(flightOptionPrice);
                    flightOptionPrice = 0;
                }
            }

            for (Schedule eachSchedule : oneStopFlightSchedules) {
                eachSchedule.setStartDate(distributionSessionBean.convertTimeZone(eachSchedule.getStartDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getOriginIATA())));
                eachSchedule.setEndDate(distributionSessionBean.convertTimeZone(eachSchedule.getEndDate(), distributionSessionBean.getSingaporeTimeZone(), distributionSessionBean.getTimeZoneFromIata(eachSchedule.getFlight().getRoute().getDestinationIATA())));

            }
            return "DisplayReturnOneStopFlightReturn";
        }

    }
    
    public void retrieveMinWeekPricesForDirect(String originIATA, String destinationIATA, Date date, String serviceType, int adults, int children){
        List<Double> pricesForWeek = new ArrayList();
        List<Schedule> schedulesForEachDate = new ArrayList();
        double minPrice,priceForEachSchedule=0;
        int i;
        
        for (i=-3;i<=3;i++){
            Date eachDate = distributionSessionBean.addDaysToDate(date, i);
            schedulesForEachDate = distributionSessionBean.retrieveDirectFlightsForDate(originIATA, destinationIATA, eachDate, serviceType, adults, children);
            if (schedulesForEachDate.size()>0){
                minPrice = 99999999;
                for (Schedule eachSchedule: schedulesForEachDate){
                    //Store price for each schedule in priceForEachScheduleVariable
                    double priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults+children), eachSchedule);
                    priceForEachSchedule = (adults*priceForOne) + (0.75*priceForOne*children);
     
                    if (priceForEachSchedule<minPrice){
                        minPrice = priceForEachSchedule;
                    }
                }
                pricesForWeek.add(minPrice);
            }
            else
                pricesForWeek.add(0.0);
            
        }
        setMinPricesForWeekDirectFlight(pricesForWeek);
        
    }
    
       public void retrieveMinWeekPricesForOneStop(String originIATA, String destinationIATA, Date date, String serviceType, int adults, int children){
  
        List<Double> pricesForWeek = new ArrayList();
        List<Schedule> schedulesForEachDate = new ArrayList();
        double minPrice,priceForEachFlightOption=0.0,priceForOne;
        int i,j, k;
        List<String> transitHubs = distributionSessionBean.getTransitHubs(distributionSessionBean.getHubIatasFromOrigin(originIATA), destinationIATA);
        
        for (i=-3;i<=3;i++){
            Date eachDate = distributionSessionBean.addDaysToDate(date, i);
            legOneSchedules.clear();
            legTwoSchedules.clear();
              
            for (j = 0; j < transitHubs.size(); j++) {
                addSchedulesToLegOne(legOneSchedules, distributionSessionBean.retrieveDirectFlightsForDate(originIATA, transitHubs.get(j), eachDate, serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(j), destinationIATA, distributionSessionBean.addDaysToDate(eachDate,1), serviceType, adults, children));
                addSchedulesToLegTwo(legTwoSchedules, distributionSessionBean.retrieveDirectFlightsForDate(transitHubs.get(j), destinationIATA, eachDate, serviceType, adults, children));
            }
            schedulesForEachDate = distributionSessionBean.retrieveOneStopFlightSchedules(legOneSchedules, legTwoSchedules);
                      
            if (schedulesForEachDate.size()>0){
                minPrice = 99999999;
                
                
                for (k=0; k<schedulesForEachDate.size(); k++){
                   Schedule eachSchedule = schedulesForEachDate.get(k);
                   //Add price of each schdule to priceForEachFlightOption; price forEachFlightOption +=
                    priceForOne = pricingManagementBean.getPrice(pricingManagementBean.getClassCode(eachSchedule, serviceType, adults+children), eachSchedule);
                    priceForEachFlightOption += (adults*priceForOne) + (children+0.75*priceForOne);
                    System.out.println("EachFlightOptionPrice for schedule "+ k + " is " + priceForEachFlightOption );
                   if (k%2==1){
                       if (priceForEachFlightOption<minPrice){
                            minPrice = priceForEachFlightOption;
                        }
                    priceForEachFlightOption = 0.0;   
                   }      
                }
                pricesForWeek.add(minPrice);
            }
            else
                pricesForWeek.add(0.0);   
        }
        System.out.println("!!!!!Length:" + pricesForWeek.size());
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
    
    
    
}
