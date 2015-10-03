package APS.Managedbean;

import APS.Entity.Flight;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.FlightScheduleSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.RouteSessionBeanLocal;
import APS.Session.FleetSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yanlong
 */
@Named(value = "flightManageBean")
@ManagedBean
@SessionScoped

public class FlightManageBean {

    @EJB
    private FlightSessionBeanLocal flightSessionBean;
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
    @EJB
    private RouteSessionBeanLocal routeSessionBean;
    @EJB
    private FleetSessionBeanLocal fleetSessionBean;
    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

//    @ManagedProperty(value="#{scheduleManageBean}")
//    private ScheduleManageBean scheduleManageBean;
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private String flightNo;
    private String tempNo;
    private String[] selectedFlightDays;
    private Double flightDuration;
    private Double basicFare;
    private Date startDateTime;
    String flightDays = "";

    private Long routeId;
    private String originCountry;
    private String originCity;
    private String originIATA;
    private String destinationCountry;
    private String destinationCity;
    private String destinationIATA;
    private Double distance;

    FacesMessage message = null;

    private List<Route> routes;
    private List<Schedule> schedule;
    private List<Flight> flights;

    private Flight selectedFlight;

    private FileHandler fh;
    private String userID;

    public FlightManageBean() {
    }

    @PostConstruct
    public void retrieve() {

        setFlights(flightSessionBean.retrieveFlights());
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        userID = (String) session.getAttribute("isLogin");

    }

    public void createFlight(ActionEvent event) {
        
        //Front end excception handling
        if (routeId == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Route ID!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (tempNo.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter valid Flight Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        
        flightNo = "MA" + String.valueOf(tempNo); //Create flightNo from the valid no. user imputs

        if (selectedFlightDays.length == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select Flight Days!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (startDateTime == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter starting day and time!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (basicFare == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Basic Fare!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (flightSessionBean.getRoute(routeId) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such route!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (flights != null) {
            /*for (int i=0; i<flights.size(); i++) {
            
             Flight temp = flightSessionBean.retrieveFlights().get(i);
   
             if (temp.getRoute().equals(flightSessionBean.getRoute(routeId))) {
             message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight already exists!", "");
             FacesContext.getCurrentInstance().addMessage(null, message);
             return;
             }
            
             }*/

            if (flightSessionBean.getFlight(flightNo) != null) {
                if (flightSessionBean.getFlight(flightNo).getRoute() == flightSessionBean.getRoute(routeId)) {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight already exists!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return;
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight number is already in use!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return;
                }
            }
        }

        //Reading in the flight days
        flightDays = "";

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Sunday")) {
                setString("1");
            }
        }

        if (flightDays.isEmpty()) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Monday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 1) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Tuesday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 2) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Wednesday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 3) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Thursday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 4) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Friday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 5) {
            setString("0");
        }

        for (int i = 0; i < selectedFlightDays.length; i++) {
            if (selectedFlightDays[i].equals("Saturday")) {
                setString("1");
            }
        }

        if (flightDays.length() == 6) {
            setString("0");
        }

        flightSessionBean.addFlight(flightNo, flightDays, basicFare, startDateTime, routeId);
        flightScheduleSessionBean.scheduleFlights(flightNo); //Create schedule and link flight to best aircraftType
        flightScheduleSessionBean.dummyRotate(); //Rotate flights and assign aircraft to schedule
        flightDays = "";

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight Added Successfully!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        Logger logger = Logger.getLogger(FleetManageBean.class.getName());
        try {
            fh = new FileHandler("%h/addFlight.txt", 99999, 1, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("User: " + userID
                + "has added Flight: " + flightNo);
        fh.close();

        setFlights(flightSessionBean.retrieveFlights());
        setSchedule(scheduleSessionBean.getSchedules());
        clear();
    }

    public void createTestFlight() {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar cal = Calendar.getInstance(tz);

        //Flights for hub JAPAN
        //Round flights to Japan everyday
        cal.set(2015, 10, 2, 7, 2, 0);
        flightSessionBean.addFlight("MA110", "1111111", 802.80, cal.getTime(), 881L);
        flightScheduleSessionBean.scheduleFlights("MA110");
        cal.set(2015, 10, 2, 16, 25, 0);
        flightSessionBean.addFlight("MA111", "1111111", 802.80, cal.getTime(), 1440L);
        flightScheduleSessionBean.scheduleFlights("MA111");
        cal.set(2015, 10, 2, 9, 16, 0);
        flightSessionBean.addFlight("MA122", "1111111", 802.80, cal.getTime(), 881L);
        flightScheduleSessionBean.scheduleFlights("MA122");
        cal.set(2015, 10, 2, 18, 45, 0);
        flightSessionBean.addFlight("MA128", "1111111", 802.80, cal.getTime(), 1440L);
        flightScheduleSessionBean.scheduleFlights("MA128");

        //Round flights to San Francisco from Japan 3 times a week
        cal.set(2015, 10, 2, 17, 5, 0);
        flightSessionBean.addFlight("MA212", "0101010", 1135.00, cal.getTime(), 1407L);
        flightScheduleSessionBean.scheduleFlights("MA212");
        cal.set(2015, 10, 2, 8, 11, 0);
        flightSessionBean.addFlight("MA221", "0010101", 1024.00, cal.getTime(), 1477L);
        flightScheduleSessionBean.scheduleFlights("MA221");

        //Flights for hub FRANKFURT
        //Round flights to Frankfurt every day of the week
        cal.set(2015, 10, 2, 9, 22, 0);
        flightSessionBean.addFlight("MA133", "1111111", 1253.00, cal.getTime(), 861L);
        flightScheduleSessionBean.scheduleFlights("MA133");
        cal.set(2015, 10, 2, 0, 55, 0);
        flightSessionBean.addFlight("MA135", "1111111", 1352.45, cal.getTime(), 1467L);
        flightScheduleSessionBean.scheduleFlights("MA135");
        cal.set(2015, 10, 2, 13, 8, 0);
        flightSessionBean.addFlight("MA146", "1111111", 1122.48, cal.getTime(), 861L);
        flightScheduleSessionBean.scheduleFlights("MA136");
        cal.set(2015, 10, 2, 7, 05, 0);
        flightSessionBean.addFlight("MA149", "1111111", 1038.45, cal.getTime(), 1467L);
        flightScheduleSessionBean.scheduleFlights("MA139");

        //Round flights to New York from frankfurt 3 times every week
        cal.set(2015, 10, 2, 7, 7, 0);
        flightSessionBean.addFlight("MA235", "0101010", 730.00, cal.getTime(), 1406L);
        flightScheduleSessionBean.scheduleFlights("MA235");
        cal.set(2015, 10, 2, 17, 45, 0);
        flightSessionBean.addFlight("MA239", "0010101", 724.00, cal.getTime(), 1476L);
        flightScheduleSessionBean.scheduleFlights("MA239");

        //Round flights to Mumbai 2 times every week
        cal.set(2015, 10, 2, 11, 2, 0);
        flightSessionBean.addFlight("MA155", "0010100", 403.00, cal.getTime(), 870L);
        flightScheduleSessionBean.scheduleFlights("MA155");
        cal.set(2015, 10, 2, 17, 9, 0);
        flightSessionBean.addFlight("MA158", "0010100", 403.00, cal.getTime(), 1429L);
        flightScheduleSessionBean.scheduleFlights("MA158");
        
        //Round flights to Shanghai 4 times every week
        cal.set(2015, 10, 2, 10, 20, 0);
        flightSessionBean.addFlight("MA162", "1101010", 679.00, cal.getTime(), 1449L);
        flightScheduleSessionBean.scheduleFlights("MA162");
        cal.set(2015, 10, 2, 19, 2, 0);
        flightSessionBean.addFlight("MA166", "1101010", 679.00, cal.getTime(), 1450L);
        flightScheduleSessionBean.scheduleFlights("MA166");
        
        //Round flights to Korea 4 times every week
        cal.set(2015, 10, 2, 9, 33, 0);
        flightSessionBean.addFlight("MA183", "1101010", 733.33, cal.getTime(), 882L);
        flightScheduleSessionBean.scheduleFlights("MA183");
        cal.set(2015, 10, 2, 20, 36, 0);
        flightSessionBean.addFlight("MA186", "1101010", 733.33, cal.getTime(), 1441L);
        flightScheduleSessionBean.scheduleFlights("MA186");

        flightScheduleSessionBean.rotateFlights(); //Rotate flights and assign aircraft to schedule
    }

    public String deleteFlight(String flightNo) {

        selectedFlight = flightSessionBean.getFlight(flightNo);
        flights.remove(selectedFlight);

        //search for Flight Num in Flight lists linked to the Route and remove the Flight
        List<Flight> temp = selectedFlight.getRoute().getFlights();
        temp.remove(selectedFlight);
        selectedFlight.getRoute().setFlights(temp);
        selectedFlight.setRoute(null);

        //search for Flight Num in Flight lists linked to the Aircraft Type and remove the Flight
        List<Flight> temp1 = selectedFlight.getAircraftType().getFlights();
        temp1.remove(selectedFlight);
        selectedFlight.getAircraftType().setFlights(temp1);
        selectedFlight.setAircraftType(null);

        //search for Team in Schedule lists linked to the Schedule and remove
        for (int i = 0; i < selectedFlight.getSchedule().size(); i++) {
            selectedFlight.getSchedule().get(i).setTeam(null);
        }

        //delete schedule
        for (int j = 0; j < selectedFlight.getSchedule().size(); j++) {
            flightSessionBean.deleteSchedule(selectedFlight.getSchedule().get(j).getScheduleId());
        }

        flightSessionBean.deleteFlight(selectedFlight.getFlightNo());
        selectedFlight = null;

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight Removed", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        setFlights(flightSessionBean.retrieveFlights());
        setSchedule(scheduleSessionBean.getSchedules());

        return "DeleteFlight";

    }

    /*clear input after submit*/
    public void clear() {
        setRouteId(null);
        setFlightNo("");
        setSelectedFlightDays(null);
        setStartDateTime(null);
        setBasicFare(null);

    }

    public void setString(String value) {

        flightDays = flightDays + value;

    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getTempNo() {
        return tempNo;
    }

    public void setTempNo(String tempNo) {
        this.tempNo = tempNo;
    }


    public String getFlightDays() {
        return flightDays;
    }

    public void setFlightDays(String flightDays) {
        this.flightDays = flightDays;
    }

    public String[] getSelectedFlightDays() {
        return selectedFlightDays;
    }

    public void setSelectedFlightDays(String[] selectedFlightDays) {
        this.selectedFlightDays = selectedFlightDays;
    }

    public Double getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Double flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Double getBasicFare() {
        return basicFare;
    }

    public void setBasicFare(Double basicFare) {
        this.basicFare = basicFare;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
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

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public Flight getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(Flight selectedFlight) {
        this.selectedFlight = selectedFlight;
    }
//
//    public ScheduleManageBean getScheduleManageBean() {
//        return scheduleManageBean;
//    }
//
//    public void setScheduleManageBean(ScheduleManageBean scheduleManageBean) {
//        this.scheduleManageBean = scheduleManageBean;
//       

}
