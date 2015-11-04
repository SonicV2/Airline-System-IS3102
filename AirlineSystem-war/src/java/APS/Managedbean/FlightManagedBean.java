package APS.Managedbean;

import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Forecast;
import APS.Entity.Route;
import APS.Entity.Schedule;
import APS.Session.DemandForecastSessionBeanLocal;
import APS.Session.FleetSessionBeanLocal;
import APS.Session.FlightScheduleSessionBeanLocal;
import APS.Session.FlightSessionBeanLocal;
import APS.Session.ScheduleSessionBeanLocal;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yanlong
 */
@Named(value = "flightManagedBean")
@ManagedBean
@SessionScoped
public class FlightManagedBean {

    @EJB
    private FlightSessionBeanLocal flightSessionBean;
    @EJB
    private FleetSessionBeanLocal fleetSessionBean;
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;
    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;
    @EJB
    private DemandForecastSessionBeanLocal demandForecastSessionBean;

    private String flightNo;
    private String tempNo;
    private String[] selectedFlightDays;
    private Double flightDuration;
    private Double basicFare;
    private Date entryDate;
    private Date entryTime;
    private boolean past = false;
    private boolean checked = false;
    String flightDays = "";

    private Long routeId;
    private Long forecastId;
    private Integer forecastYear;
    private Integer period;
    private boolean isUpdate = false;
    private double[] dForecast;

    FacesMessage message = null;

    private List<AircraftType> aircraftTypes;
    private List<Route> routes;
    private List<Long> routeIds;
    private List<Schedule> schedule; //Can be deleted in actual run
    private List<Flight> flights;
    private List<Forecast> forecasts;

    private Flight selectedFlight;

    private FileHandler fh;
    private String userID;

    public FlightManagedBean() {
    }

    @PostConstruct
    public void retrieve() {
        setForecasts(demandForecastSessionBean.getForecasts());
        setFlights(flightSessionBean.retrieveActiveFlights());
        setRoutes(flightSessionBean.retrieveFlightRoutes());
        setRouteIds(flightSessionBean.retrieveFlightRouteIds());
        setAircraftTypes(fleetSessionBean.retrieveAircraftTypes());
//        userID = (String) session.getAttribute("isLogin");

    }

    public void createFlight(ActionEvent event) {

        //Front end excception handling
        if (!checked) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check the validity of the flight number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

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

        flightNo = "MA" + tempNo; //Create flightNo from the valid no. user imputs

        if (selectedFlightDays.length == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select Flight Days!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (entryDate == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter the starting date of flight!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (entryTime == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter the time of flight!!", "");
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

        Flight tmpflight = flightSessionBean.getFlight(flightNo);
        if (tmpflight != null) {
            if (tmpflight.getArchiveData() != null) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight number is already in use!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }

        //Reading in the flight days
        flightDays = "";

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Sunday")) {
                setString("1");
            }
        }

        if (flightDays.isEmpty()) {
            setString("0");
        }

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Monday")) {
                setString("1");
            }
        }

        if (flightDays.length()
                == 1) {
            setString("0");
        }

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Tuesday")) {
                setString("1");
            }
        }

        if (flightDays.length()
                == 2) {
            setString("0");
        }

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Wednesday")) {
                setString("1");
            }
        }

        if (flightDays.length()
                == 3) {
            setString("0");
        }

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Thursday")) {
                setString("1");
            }
        }

        if (flightDays.length()
                == 4) {
            setString("0");
        }

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Friday")) {
                setString("1");
            }
        }

        if (flightDays.length()
                == 5) {
            setString("0");
        }

        for (int i = 0;
                i < selectedFlightDays.length;
                i++) {
            if (selectedFlightDays[i].equals("Saturday")) {
                setString("1");
            }
        }

        if (flightDays.length()
                == 6) {
            setString("0");
        }

        //Set up the startDateTime for flight
        Date startDateTime = combineDateTime(entryDate, entryTime);

        if (past) {
            flightSessionBean.addFlight(flightNo, flightDays, basicFare, startDateTime, tmpflight.getArchiveData(), true);
        } else {
            flightSessionBean.addFlight(flightNo, flightDays, basicFare, startDateTime, routeId, false);
        }

        flightScheduleSessionBean.scheduleFlights(flightNo); //Create schedule and link flight to best aircraftType
//        flightScheduleSessionBean.rotateFlights(); //Rotate flights and assign aircraft to schedule
        flightDays = "";

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight Added Successfully!", "");

        FacesContext.getCurrentInstance()
                .addMessage(null, message);

        Logger logger = Logger.getLogger(FleetManagedBean.class.getName());

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

        logger.info(
                "User: " + userID
                + "has added Flight: " + flightNo);
        fh.close();

        setFlights(flightSessionBean.retrieveActiveFlights());
        setSchedule(scheduleSessionBean.getSchedules());
        clear();
    }

    public void createTestFlight(ActionEvent event) {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar cal = Calendar.getInstance(tz);
        cal.set(Calendar.SECOND, 0);

        //Flights for hub JAPAN
        //Round flights to Japan everyday
        cal.set(2015, 10, 2, 7, 2);
        flightSessionBean.addFlight("MA110", "1111111", 802.80, cal.getTime(), 881L, false);
        flightScheduleSessionBean.scheduleFlights("MA110");
        cal.set(2015, 10, 2, 16, 25);
        flightSessionBean.addFlight("MA111", "1111111", 802.80, cal.getTime(), 1440L, false);
        flightScheduleSessionBean.scheduleFlights("MA111");
        cal.set(2015, 10, 2, 9, 16);
        flightSessionBean.addFlight("MA122", "1111111", 802.80, cal.getTime(), 881L, false);
        flightScheduleSessionBean.scheduleFlights("MA122");
        cal.set(2015, 10, 2, 18, 45);
        flightSessionBean.addFlight("MA128", "1111111", 802.80, cal.getTime(), 1440L, false);
        flightScheduleSessionBean.scheduleFlights("MA128");

        //Round flights to San Francisco from Japan 3 times a week
        cal.set(2015, 10, 2, 17, 5);
        flightSessionBean.addFlight("MA212", "0101010", 1135.00, cal.getTime(), 1407L, false);
        flightScheduleSessionBean.scheduleFlights("MA212");
        cal.set(2015, 10, 3, 8, 11);
        flightSessionBean.addFlight("MA221", "0010101", 1024.00, cal.getTime(), 1477L, false);
        flightScheduleSessionBean.scheduleFlights("MA221");

        //Flights for hub FRANKFURT
        //Round flights to Frankfurt every day of the week
        cal.set(2015, 10, 2, 9, 22);
        flightSessionBean.addFlight("MA133", "1111111", 1253.00, cal.getTime(), 861L, false);
        flightScheduleSessionBean.scheduleFlights("MA133");
        cal.set(2015, 10, 3, 0, 55);
        flightSessionBean.addFlight("MA135", "1111111", 1352.45, cal.getTime(), 1467L, false);
        flightScheduleSessionBean.scheduleFlights("MA135");
        cal.set(2015, 10, 2, 13, 8);
        flightSessionBean.addFlight("MA146", "1111111", 1122.48, cal.getTime(), 861L, false);
        flightScheduleSessionBean.scheduleFlights("MA146");
        cal.set(2015, 10, 3, 7, 05);
        flightSessionBean.addFlight("MA149", "1111111", 1038.45, cal.getTime(), 1467L, false);
        flightScheduleSessionBean.scheduleFlights("MA149");

        //Round flights to New York from frankfurt 3 times every week
        cal.set(2015, 10, 2, 7, 0);
        flightSessionBean.addFlight("MA235", "0101010", 730.00, cal.getTime(), 1406L, false);
        flightScheduleSessionBean.scheduleFlights("MA235");
        cal.set(2015, 10, 2, 17, 45);
        flightSessionBean.addFlight("MA239", "0010101", 724.00, cal.getTime(), 1476L, false);
        flightScheduleSessionBean.scheduleFlights("MA239");

        //Round flights to Mumbai 2 times every week
        cal.set(2015, 10, 2, 11, 2);
        flightSessionBean.addFlight("MA155", "0010100", 403.00, cal.getTime(), 870L, false);
        flightScheduleSessionBean.scheduleFlights("MA155");
        cal.set(2015, 10, 2, 17, 9);
        flightSessionBean.addFlight("MA158", "0010100", 403.00, cal.getTime(), 1429L, false);
        flightScheduleSessionBean.scheduleFlights("MA158");

        //Round flights to Shanghai 4 times every week
        cal.set(2015, 10, 2, 10, 20);
        flightSessionBean.addFlight("MA162", "1101010", 679.00, cal.getTime(), 1449L, false);
        flightScheduleSessionBean.scheduleFlights("MA162");
        cal.set(2015, 10, 2, 19, 2);
        flightSessionBean.addFlight("MA166", "1101010", 679.00, cal.getTime(), 1450L, false);
        flightScheduleSessionBean.scheduleFlights("MA166");

        //Round flights to Korea 4 times every week
        cal.set(2015, 10, 2, 9, 33);
        flightSessionBean.addFlight("MA183", "1101010", 733.33, cal.getTime(), 882L, false);
        flightScheduleSessionBean.scheduleFlights("MA183");
        cal.set(2015, 10, 2, 20, 36);
        flightSessionBean.addFlight("MA186", "1101010", 733.33, cal.getTime(), 1441L, false);
        flightScheduleSessionBean.scheduleFlights("MA186");

        flightScheduleSessionBean.rotateAircrafts(); //Rotate flights and assign aircraft to schedule

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Test Flights Generated", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void testRotation(ActionEvent event) {
        flightScheduleSessionBean.rotateAircrafts();
    }

    //Archive the flight instead of actually deleting it from database
    public String deleteFlight(String flightNo) {

        selectedFlight = flightSessionBean.getFlight(flightNo);
        flights.remove(selectedFlight);

        // Find the latest schedule without bookings and delete all schedules after that
        Schedule currSc = new Schedule();
        int deletionIndex = selectedFlight.getSchedule().size();

        for (int i = 0; i < selectedFlight.getSchedule().size(); i++) {
            currSc = selectedFlight.getSchedule().get(i);
            if (currSc.getSeatAvailability().getBookings().isEmpty()) {
                deletionIndex = i;
                break;
            }
        }

        for (int i = deletionIndex; i < selectedFlight.getSchedule().size(); i++) {
            flightSessionBean.deleteSchedule(selectedFlight.getSchedule().get(i).getScheduleId());
        }

        if (deletionIndex == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight and relevant schedules have been completely removed!", "");
            flightSessionBean.deleteFlight(selectedFlight.getFlightNo(), false);
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight has been archieved and schedules starting from " + currSc.getStartDate() + " has been deleted!", "");
            flightSessionBean.deleteFlight(selectedFlight.getFlightNo(), true);
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        selectedFlight = null;
        setFlights(flightSessionBean.retrieveActiveFlights());
        setSchedule(scheduleSessionBean.getSchedules());

        return "DeleteFlight";

    }

    public void onRowEdit(RowEditEvent event) {

        Flight edited = (Flight) event.getObject();
        Flight original = flightSessionBean.getFlight(edited.getFlightNo());

        if (original.getAircraftType().getId().equals(edited.getAircraftType().getId())) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Cancelled", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        flightSessionBean.edit(edited, original);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aircraft Type will be used for future schedules! Please ensure that there is a relevant return flight for this Aircraft Type too!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void onRowCancel(RowEditEvent event) {
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Cancelled", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void generateForecast(ActionEvent event) {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz); //Get the current time

        //Front end excception handling
        if (!checked) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please check if the year is valid!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (forecastYear == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter forecast year!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } else if (!demandForecastSessionBean.isValidYear(routeId)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Not enough historical data to generate forecast!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (routeId == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter Route ID!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (period == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter the base duration to use forecast!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        //Set status to update if the dForecast for the year already exists
        if (demandForecastSessionBean.hasForecast(forecastYear, routeId) != null) {
            isUpdate = true;
        }
        demandForecastSessionBean.generateDemandForecast(routeId, forecastYear, period, isUpdate);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Demand forecast for " + forecastYear + " is generated!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);

        clear();
    }

    public String printForecast(Long selectedId) {
        System.out.println(selectedId);
        Forecast forecast = demandForecastSessionBean.getForecast(selectedId);
        
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("YEAR", forecast.getForecastYear());
            session.setAttribute("ID", forecast.getForecastId());
            session.setAttribute("ORIGIN", forecast.getRoute().getOriginIATA());
            session.setAttribute("DEST", forecast.getRoute().getDestinationIATA());

            request.setAttribute("type", "demand"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }
        return "PrintForecast";
    }

    /*clear input after submit*/
    public void clear() {
        setRouteId(null);
        setFlightNo("");
        setSelectedFlightDays(null);
        setEntryDate(null);
        setEntryTime(null);
        setBasicFare(null);
        setChecked(false);
        setPast(false);
    }

    //Returns true if the flight number belongs to a flight archived
    public String checkFlight(String tmpNo) {
        if (tmpNo.isEmpty()) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please enter valid Flight Number!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "AddFlight";
        }

        Flight flight = flightSessionBean.getFlight("MA" + tmpNo);

        if (flight != null) {
            if (flight.getArchiveData() == null) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight number is already in use!", "");
                setPast(false);
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Flight number belongs to an archived flight of a particular route! Adding will unarchive the flight with the updated fields!"
                        + " Please choose another flight number if you wish to create a new flight!", "");
                setChecked(true);
                setPast(true);
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valid flight number!!", "");
            setChecked(true);
            setPast(false);
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
        return "AddFlight";
    }

    //Check whether the year chosen is far from the current year
    public String checkForecastYear(int year) {
        System.out.println(year);
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz); //Get the current time

        if (year > tmp.get(Calendar.YEAR) + 1) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "There are no recent data for the year you have chosen. The generated forecast will be from the limited current dataset. Please consider choosing another year.", "");
            setChecked(true);
        } else if (year < tmp.get(Calendar.YEAR)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unable to generate forecast for past years! Please view forecast for past forecasts.", "");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Chosen year is valid!", "");
            setChecked(true);
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

        return "GenerateDemandForecast";
    }

    private Date combineDateTime(Date date1, Date time1) {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp1 = Calendar.getInstance(tz);
        Calendar tmp2 = Calendar.getInstance(tz);

        tmp1.setTime(date1);
        tmp2.setTime(time1);

        System.out.println(tmp2.getTime());
        tmp2.set(tmp1.get(Calendar.YEAR), tmp1.get(Calendar.MONTH), tmp1.get(Calendar.DATE));
        System.out.println(tmp2.getTime());
        tmp2.add(Calendar.MINUTE, -30);
        System.out.println(tmp2.getTime());

        return tmp2.getTime();
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

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public boolean isPast() {
        return past;
    }

    public void setPast(boolean past) {
        this.past = past;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Long> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<Long> routeIds) {
        this.routeIds = routeIds;
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

    public Long getForecastId() {
        return forecastId;
    }

    public void setForecastId(Long forecastId) {
        this.forecastId = forecastId;
    }

    public Integer getForecastYear() {
        return forecastYear;
    }

    public void setForecastYear(Integer forecastYear) {
        this.forecastYear = forecastYear;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public boolean isIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public double[] getdForecast() {
        return dForecast;
    }

    public void setdForecast(double[] dForecast) {
        this.dForecast = dForecast;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public List<AircraftType> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftType> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

}
