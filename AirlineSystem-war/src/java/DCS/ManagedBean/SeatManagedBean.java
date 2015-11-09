package DCS.ManagedBean;

import APS.Entity.Schedule;
import DCS.Session.BaggageSessionBeanLocal;
import DCS.Session.BoardingPassSessionBeanLocal;
import DCS.Session.CheckInRecordSessionBeanLocal;
import DCS.Session.PassengerNameRecordSessionBeanLocal;
import DCS.Session.SeatSessionBeanLocal;
import Distribution.ManagedBean.MARSManagedBean;
import Distribution.Session.DistributionSessionBeanLocal;
import Inventory.Entity.Booking;
import Inventory.Session.PricingSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 *
 * @author HOULIANG
 */
@Named(value = "seatManagedBean")
@ManagedBean
@RequestScoped
public class SeatManagedBean {

    @EJB
    private DistributionSessionBeanLocal distributionSessionBean;

    @EJB
    private PricingSessionBeanLocal pricingSessionBean;

    @EJB
    private PassengerNameRecordSessionBeanLocal passengerNameRecordSessionBean;
    @EJB
    private BaggageSessionBeanLocal baggageSessionBean;

    @EJB
    private BoardingPassSessionBeanLocal boardingPassSessionBean;
    @EJB
    private CheckInRecordSessionBeanLocal checkInRecordSessionBean;

    @EJB
    private SeatSessionBeanLocal seatSessionBean;

    @ManagedProperty(value = "#{searchBookingManagedBean}")
    private SearchBookingManagedBean searchBookingManagedBean;

    @ManagedProperty(value = "#{baggageManagedBean}")
    private BaggageManagedBean baggageManagedBean;

    @ManagedProperty(value = "#{mARSManagedBean}")
    private MARSManagedBean mARSManagedBean;

    private Booking booking;

    private List<String> A330seatArrange = new ArrayList<String>();
    private List<String> B777_200seatArrange = new ArrayList<String>();
    private List<String> B777_200ERseatArrange = new ArrayList<String>();
    private List<String> B777_300seatArrange = new ArrayList<String>();
    private List<String> B777_300ERseatArrange = new ArrayList<String>();
    private String choose;

    private List<String> occupied;
    private String classtype;
    private String aircraftType;
    FacesMessage message = null;

    private Schedule schedule = new Schedule(); // get the current schedule

    private int Favail; // number of firstclass available for booking
    private int Bavail;
    private int Eavail;

    private int Fbooked;
    private int Bbooked;
    private int Ebooked;

    private int upgrade;  //default=0 upgrade=1 paid upgrade = 2

    private String selectedPaidUpgradeClass = "Economy Class";
    private List<String> bookingClassLists;

    private String totalWeightAllowed;

    private double upgradeCosts;

    private String upgradeCostsDB;

    private List<Schedule> nextAvailSchedules = new ArrayList<Schedule>();
    ;

    private List<Schedule> flightSchedules; // get available schedules from MARS

    private String selectedSchedule; // change flight schedule

    @PostConstruct
    public void init() {
        bookingClassLists = new ArrayList<String>();
        bookingClassLists.add("Economy Class");
        bookingClassLists.add("Business Class");
        bookingClassLists.add("First Class");

        upgrade = 0;
        booking = searchBookingManagedBean.getReqBooking();
        schedule = searchBookingManagedBean.getCheckinSchedule();
        setOccupied(seatSessionBean.retrieveOccupiedSeats(searchBookingManagedBean.getCheckinSchedule()));
        if (occupied.isEmpty()) {
            occupied.add(" ");
        }

        setClasstype(getSearchBookingManagedBean().getServiceClass());

        setAircraftType(getSearchBookingManagedBean().getAircraftType());

        if (getAircraftType().equals("Airbus A330-300")) {
            addSeatA330();
            Favail = 0;
            Bavail = 30 - schedule.getSelectedSeatsB().size();
            Eavail = 255 - schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-200")) {
            addSeatB777_200();
            Favail = 0;
            Bavail = 38 - schedule.getSelectedSeatsB().size();
            Eavail = 228 - schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-200ER")) {
            addSeatB777_200ER();
            Favail = 0;
            Bavail = 30 - schedule.getSelectedSeatsB().size();
            Eavail = 255 - schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-300")) {
            addSeatB777_300();
            Favail = 8 - schedule.getSelectedSeatsF().size();
            Bavail = 50 - schedule.getSelectedSeatsB().size();
            Eavail = 226 - schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-300ER")) {
            addSeatB777_300ER();
            Favail = 4 - schedule.getSelectedSeatsF().size();
            Bavail = 48 - schedule.getSelectedSeatsB().size();
            Eavail = 232 - schedule.getSelectedSeatsE().size();
        }

        retriveBookedF();
        changeFlight1();

    }

    public void retriveBookedF() {
        Fbooked = booking.getSeatAvail().getFirstClassBooked();
        Bbooked = booking.getSeatAvail().getBusinessBooked();
        Ebooked = booking.getSeatAvail().getEconomyBasicBooked() + booking.getSeatAvail().getEconomyPremiumBooked()
                + booking.getSeatAvail().getEconomySaverBooked();
    }

    public void upgradeClass(ActionEvent event) {   //free upgrade
        if (getAircraftType().equals("Airbus A330-300")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 30) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! ", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else if (getAircraftType().equals("Boeing 777-200")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 38) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! ", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else if (getAircraftType().equals("Boeing 777-200ER")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 30) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! ", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else if (getAircraftType().equals("Boeing 777-300")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 50) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0 && Fbooked < 8) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! ", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else if (getAircraftType().equals("Boeing 777-300ER")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 48) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0 && Fbooked < 4) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! ", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }

    }

    public void paidUpgradeClass(ActionEvent event) {
        if (getAircraftType().equals("Airbus A330-300")) {

            if (selectedPaidUpgradeClass.contains("Economy")) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, the selected class is not available for upgrade!", "");
                return;
            }
            if (Bavail != 0 && Bbooked < 30 && selectedPaidUpgradeClass.contains("Business")) {
                setClasstype("Business");
                retrieveNumberofBaggageAllowed("D11");
                passengerNameRecordSessionBean.changeClass(booking, "D11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "Business");

                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "Business", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice(); // need to replace dummy 900

                upgrade = 2;
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, the selected class is not available for upgrade!", "");

            }
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else if (getAircraftType().equals("Boeing 777-200")) {

            if (Bavail != 0 && Bbooked < 38 && selectedPaidUpgradeClass.contains("Business")) {
                setClasstype("Business");
                retrieveNumberofBaggageAllowed("D11");
                passengerNameRecordSessionBean.changeClass(booking, "D11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "Business");

                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "Business", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice();

                upgrade = 2;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, the selected class is available for upgrade!", "");

            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (getAircraftType().equals("Boeing 777-200ER")) {

            if (Bavail != 0 && Bbooked < 30 && selectedPaidUpgradeClass.contains("Business")) {
                setClasstype("Business");
                retrieveNumberofBaggageAllowed("D11");
                passengerNameRecordSessionBean.changeClass(booking, "D11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "Business");
                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "Business", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice();

                upgrade = 2;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, the selected class is available for upgrade!", "");

            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (getAircraftType().equals("Boeing 777-300")) {

            if (Bavail != 0 && Bbooked < 50 && selectedPaidUpgradeClass.contains("Business")) {
                setClasstype("Business");
                retrieveNumberofBaggageAllowed("D11");
                passengerNameRecordSessionBean.changeClass(booking, "D11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "Business");
                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "Business", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice();

                upgrade = 2;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else if (Favail != 0 && Fbooked < 8 && selectedPaidUpgradeClass.contains("First")) {
                setClasstype("First Class");
                retrieveNumberofBaggageAllowed("E11");
                passengerNameRecordSessionBean.changeClass(booking, "E11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "First Class");
                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "First Class", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice();

                upgrade = 2;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, the selected class is available for upgrade!", "");

            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (getAircraftType().equals("Boeing 777-300ER")) {

            if (Bavail != 0 && Bbooked < 48 && selectedPaidUpgradeClass.contains("Business")) {
                setClasstype("Business");
                retrieveNumberofBaggageAllowed("D11");
                passengerNameRecordSessionBean.changeClass(booking, "D11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "Business");
                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "Business", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice();

                upgrade = 2;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else if (Favail != 0 && Fbooked < 4 && selectedPaidUpgradeClass.contains("First")) {
                setClasstype("First Class");
                retrieveNumberofBaggageAllowed("E11");
                passengerNameRecordSessionBean.changeClass(booking, "E11");
                passengerNameRecordSessionBean.changeServiceClass(booking, "First Class");
                double price = pricingSessionBean.getPrice(pricingSessionBean.getClassCode(booking.getSeatAvail().getSchedule(), "First Class", 1, false), booking.getSeatAvail().getSchedule());
                upgradeCosts = price - booking.getPrice();

                upgrade = 2;

                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Additional Cost: " + upgradeCosts, "");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, the selected class is available for upgrade!", "");

            }
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void changeFlight(ActionEvent event) {
        Date date = new Date();
        Date tmr = new Date(date.getTime() + 1000 * 60 * 60 * 24);

        if (getAircraftType().equals("Airbus A330-300")) {
//            if (Eavail == 0 && Bbooked == 30) {
            flightSchedules = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), date, booking.getServiceType(), 1, 0);
            List<Schedule> temps = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), tmr, booking.getServiceType(), 1, 0);
            for (Schedule s : temps) {
                flightSchedules.add(s);
            }

            for (Schedule s : flightSchedules) {

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                if (checkTime(formattedDate1, formattedDate2) < 0) {
                    nextAvailSchedules.add(s);
                }
            }

//            } else {
//                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free change flight! Please select seat from economy class", "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } else if (getAircraftType().equals("Boeing 777-200")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 38) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! Please select seat from economy class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else if (getAircraftType().equals("Boeing 777-200ER")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 30) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! Please select seat from economy class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else if (getAircraftType().equals("Boeing 777-300")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 50) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0 && Fbooked < 8) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! Please select seat from economy class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else if (getAircraftType().equals("Boeing 777-300ER")) {
            if (Eavail == 0) {
                if (Bavail != 0 && Bbooked < 48) {
                    setClasstype("Business");
                    upgrade = 1;
                } else if (Favail != 0 && Fbooked < 4) {
                    setClasstype("First Class");
                    upgrade = 1;
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free upgrade! Please select seat from economy class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }

    }

    public void changeExistBooking(ActionEvent event) {

        passengerNameRecordSessionBean.changeExistBooking(booking, selectedSchedule);

    }

    public void retrieveNumberofBaggageAllowed(String classcode) {
        int i = baggageSessionBean.retrieveNumberOfBaggageAllowed(classcode);
        setTotalWeightAllowed((i * 15) + "");
    }

    public void addSeatA330() {
        for (int i = 1; i < 9; i++) {  // 30 business seats
            if (i == 1) {
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                getA330seatArrange().add("\uD83D\uDCBA" + "B" + i);
                getA330seatArrange().add("\uD83D\uDCBA" + "C" + i);
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                i++;
            }
            getA330seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getA330seatArrange().add(" ");
            getA330seatArrange().add(" ");
            getA330seatArrange().add(" ");
            getA330seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getA330seatArrange().add("\uD83D\uDCBA" + "C" + i);
            getA330seatArrange().add(" ");
            getA330seatArrange().add(" ");
            getA330seatArrange().add(" ");
            getA330seatArrange().add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getA330seatArrange().add(" ");
        }
        for (int i = 9; i < 41; i++) { //255 economic seats
            if (i == 40) {
                getA330seatArrange().add("\uD83D\uDCBA" + "A" + i);
                getA330seatArrange().add("\uD83D\uDCBA" + "B" + i);
                getA330seatArrange().add(" ");
                getA330seatArrange().add(" ");
                getA330seatArrange().add("\uD83D\uDCBA" + "D" + i);
                getA330seatArrange().add("\uD83D\uDCBA" + "E" + i);
                getA330seatArrange().add("\uD83D\uDCBA" + "F" + i);
                getA330seatArrange().add(" ");
                getA330seatArrange().add("\uD83D\uDCBA" + "G" + i);
                getA330seatArrange().add("\uD83D\uDCBA" + "H" + i);
                break;
            }
            getA330seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getA330seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getA330seatArrange().add(" ");
            getA330seatArrange().add("\uD83D\uDCBA" + "C" + i);
            getA330seatArrange().add("\uD83D\uDCBA" + "D" + i);
            getA330seatArrange().add("\uD83D\uDCBA" + "E" + i);
            getA330seatArrange().add("\uD83D\uDCBA" + "F" + i);
            getA330seatArrange().add(" ");
            getA330seatArrange().add("\uD83D\uDCBA" + "G" + i);
            getA330seatArrange().add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    getA330seatArrange().add(" ");
                }
            }

        }

        // get booked seat from database and display all the booked seats
        for (int j = 0; j < getA330seatArrange().size(); j++) {
            if (getA330seatArrange().get(j).length() < 2) {
            } else {
                if (getOccupied().contains(getA330seatArrange().get(j).substring(2))) {

                    getA330seatArrange().set(j, "\n" + "\u26D4" + "\n" + getA330seatArrange().get(j).substring(2));
                } else {

                }
            }
        }

    }

    public String chooseSeatA330() {
        System.out.println("seatmbean---" + getChoose() + choose);
         System.out.println("seatmbean--- occupied" + getOccupied().size());
        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;

        }  // make sure you cannot choose seat that is already booked

        if (getA330seatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 8 && Integer.parseInt(getChoose().substring(1)) < 41) {
                getA330seatArrange().set(getA330seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                if (!searchBookingManagedBean.isOnline()) {
                    return "AddBaggage.xhtml";
                } else {
                    return "ShowBoardingPass.xhtml";
                }

            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) < 9 && Integer.parseInt(getChoose().substring(1)) > 0) {
                getA330seatArrange().set(getA330seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                if (!searchBookingManagedBean.isOnline()) {

                    return "AddBaggage.xhtml";
                } else {
                    return "ShowBoardingPass.xhtml";
                }
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }

        } else {
            return null;
        }

    }

    //----------------------------------------------------------------------------------------------------   
    public void addSeatB777_200() {
        for (int i = 1; i < 11; i++) {  // 38 business seats
            if (i == 1) {
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add("\uD83D\uDCBA" + "B" + i);
                getB777_200seatArrange().add("\uD83D\uDCBA" + "C" + i);
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add(" ");
                i++;
            }
            getB777_200seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_200seatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getB777_200seatArrange().add(" ");
        }
        for (int i = 11; i < 40; i++) { //228 economic seats
            if (i == 39) {
                getB777_200seatArrange().add("\u26D4");
                getB777_200seatArrange().add("\u26D4");
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add("\uD83D\uDCBA" + "C" + i);
                getB777_200seatArrange().add("\uD83D\uDCBA" + "D" + i);
                getB777_200seatArrange().add("\uD83D\uDCBA" + "E" + i);
                getB777_200seatArrange().add("\uD83D\uDCBA" + "F" + i);
                getB777_200seatArrange().add(" ");
                getB777_200seatArrange().add("\u26D4");
                getB777_200seatArrange().add("\u26D4");
                break;
            }
            getB777_200seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_200seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_200seatArrange().add("\uD83D\uDCBA" + "D" + i);
            getB777_200seatArrange().add("\uD83D\uDCBA" + "E" + i);
            getB777_200seatArrange().add("\uD83D\uDCBA" + "F" + i);
            getB777_200seatArrange().add(" ");
            getB777_200seatArrange().add("\uD83D\uDCBA" + "G" + i);
            getB777_200seatArrange().add("\uD83D\uDCBA" + "H" + i);
            if (i == 27) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    getB777_200seatArrange().add(" ");
                }
            }

        }
        // get booked seat from database and display all the booked seats
        for (int j = 0; j < getB777_200seatArrange().size(); j++) {
            if (getB777_200seatArrange().get(j).length() < 2) {
            } else {
                if (getOccupied().contains(getB777_200seatArrange().get(j).substring(2))) {

                    getB777_200seatArrange().set(j, "\n" + "\u26D4" + "\n" + getB777_200seatArrange().get(j).substring(2));
                } else {

                }
            }
        }
    }

    public String chooseSeatB777_200() {

        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }  // make sure you cannot choose seat that is already booked

        if (getB777_200seatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 10) {
                getB777_200seatArrange().set(getB777_200seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";
            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) < 11) {
                getB777_200seatArrange().set(getB777_200seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }

        } else {
            return null;
        }

    }

//-------------------------------------------------------------------------------------------------------
    public void addSeatB777_200ER() {
        for (int i = 1; i < 9; i++) {  // 30 business seats
            if (i == 1) {
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "C" + i);
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                i++;
            }
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getB777_200ERseatArrange().add(" ");
        }
        for (int i = 9; i < 41; i++) { //255 economic seats
            if (i == 40) {
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "A" + i);
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "D" + i);
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "E" + i);
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "F" + i);
                getB777_200ERseatArrange().add(" ");
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "G" + i);
                getB777_200ERseatArrange().add("\uD83D\uDCBA" + "H" + i);
                break;
            }
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "D" + i);
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "E" + i);
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "F" + i);
            getB777_200ERseatArrange().add(" ");
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "G" + i);
            getB777_200ERseatArrange().add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    getB777_200ERseatArrange().add(" ");
                }
            }

        }
        // get booked seat from database and display all the booked seats
        for (int j = 0; j < getB777_200ERseatArrange().size(); j++) {
            if (getB777_200ERseatArrange().get(j).length() < 2) {
            } else {
                if (getOccupied().contains(getB777_200ERseatArrange().get(j).substring(2))) {

                    getB777_200ERseatArrange().set(j, "\n" + "\u26D4" + "\n" + getB777_200ERseatArrange().get(j).substring(2));
                } else {

                }
            }
        }
    }

    public String chooseSeatB777_200ER() {
        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }  // make sure you cannot choose seat that is already booked

        if (getB777_200ERseatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 8) {
                getB777_200ERseatArrange().set(getB777_200ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";

            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) < 9) {
                getB777_200ERseatArrange().set(getB777_200ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }

        } else {
            return null;
        }

    }
//-------------------------------------------------------------------------------------------------------

    public void addSeatB777_300() {
        for (int i = 1; i < 3; i++) {  // 8 first class seats

            getB777_300seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getB777_300seatArrange().add(" ");
        }

        for (int i = 3; i < 12; i++) {  // 50 business seats
            if (i == 3) {
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add("\uD83D\uDCBA" + "E" + i);
                getB777_300seatArrange().add("\uD83D\uDCBA" + "F" + i);
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add(" ");
                i++;
            }
            getB777_300seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add("\uD83D\uDCBA" + "E" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "F" + i);
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add("\uD83D\uDCBA" + "J" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "K" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getB777_300seatArrange().add(" ");
        }

        for (int i = 12; i < 41; i++) { //226 economic seats
            if (i == 40) {
                getB777_300seatArrange().add("\uD83D\uDCBA" + "C" + i);
                getB777_300seatArrange().add("\uD83D\uDCBA" + "D" + i);

                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add("\u26D4");
                getB777_300seatArrange().add("\u26D4");
                getB777_300seatArrange().add("\u26D4");
                getB777_300seatArrange().add("\u26D4");
                getB777_300seatArrange().add(" ");
                getB777_300seatArrange().add("\u26D4");
                getB777_300seatArrange().add("\u26D4");
                break;
            }
            getB777_300seatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "D" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "E" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "F" + i);
            getB777_300seatArrange().add(" ");
            getB777_300seatArrange().add("\uD83D\uDCBA" + "G" + i);
            getB777_300seatArrange().add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    getB777_300seatArrange().add(" ");
                }
            }

        }

        // get booked seat from database and display all the booked seats
        for (int j = 0; j < getB777_300seatArrange().size(); j++) {
            if (getB777_300seatArrange().get(j).length() < 2) {
            } else {
                if (getOccupied().contains(getB777_300seatArrange().get(j).substring(2))) {

                    getB777_300seatArrange().set(j, "\n" + "\u26D4" + "\n" + getB777_300seatArrange().get(j).substring(2));
                } else {

                }
            }
        }
    }

    public String chooseSeatB777_300() {

        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }  // make sure you cannot choose seat that is already booked

        if (getB777_300seatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 11) {
                getB777_300seatArrange().set(getB777_300seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";

            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) > 2 && Integer.parseInt(getChoose().substring(1)) < 12) {
                getB777_300seatArrange().set(getB777_300seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";

            } else if (getClasstype().equals("First Class") && Integer.parseInt(getChoose().substring(1)) < 3) {
                getB777_300seatArrange().set(getB777_300seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenF(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";

            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return null;
            }

        } else {
            return null;
        }

    }

//-------------------------------------------------------------------------------------------------------    
    public void addSeatB777_300ER() {
        for (int i = 1; i < 2; i++) {  // 4 first class seats

            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "D" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getB777_300ERseatArrange().add(" ");
        }

        for (int i = 2; i < 10; i++) {  // 48 business seats

            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "E" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "F" + i);
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "J" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "K" + i);
        }
        for (int i = 1; i < 11; i++) { // seperation row eg toliet   
            getB777_300ERseatArrange().add(" ");
        }

        for (int i = 10; i < 39; i++) { //232 economic seats

            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "A" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "B" + i);
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "C" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "D" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "E" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "F" + i);
            getB777_300ERseatArrange().add(" ");
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "G" + i);
            getB777_300ERseatArrange().add("\uD83D\uDCBA" + "H" + i);
            if (i == 25) {   // seperation row eg toliet
                for (int j = 1; j < 11; j++) {
                    getB777_300ERseatArrange().add(" ");
                }
            }

        }
        // get booked seat from database and display all the booked seats
        for (int j = 0; j < getB777_300ERseatArrange().size(); j++) {
            if (getB777_300ERseatArrange().get(j).length() < 2) {
            } else {
                if (getOccupied().contains(getB777_300ERseatArrange().get(j).substring(2))) {

                    getB777_300ERseatArrange().set(j, "\n" + "\u26D4" + "\n" + getB777_300ERseatArrange().get(j).substring(2));
                } else {

                }
            }
        }
    }

    public String chooseSeatB777_300ER() {
        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }  // make sure you cannot choose seat that is already booked

        if (getB777_300ERseatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 9) {
                getB777_300ERseatArrange().set(getB777_300ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";
            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) > 1 && Integer.parseInt(getChoose().substring(1)) < 10) {
                getB777_300ERseatArrange().set(getB777_300ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";
            } else if (getClasstype().equals("First Class") && Integer.parseInt(getChoose().substring(1)) < 2) {
                getB777_300ERseatArrange().set(getB777_300ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenF(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase(), upgrade, upgradeCosts);
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

                return "AddBaggage.xhtml";
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return "AddBaggage.xhtml";
            }

        } else {
            return null;
        }

    }

//-------------------------------------------------------------------------------------------------------    
    public long checkTime(String time1, String time2) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date1;
        Date date2;
        try {
            date1 = formatter.parse(time1);
            date2 = formatter.parse(time2);
            long diff = (date2.getTime() - date1.getTime());
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long result = diffDays * 24 * 60 + diffHours * 60 + diffMinutes;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public SeatManagedBean() {
    }

    /**
     * @return the searchBookingManagedBean
     */
    public SearchBookingManagedBean getSearchBookingManagedBean() {
        return searchBookingManagedBean;
    }

    /**
     * @param searchBookingManagedBean the searchBookingManagedBean to set
     */
    public void setSearchBookingManagedBean(SearchBookingManagedBean searchBookingManagedBean) {
        this.searchBookingManagedBean = searchBookingManagedBean;
    }

    /**
     * @return the A330seatArrange
     */
    public List<String> getA330seatArrange() {
        return A330seatArrange;
    }

    /**
     * @param A330seatArrange the A330seatArrange to set
     */
    public void setA330seatArrange(List<String> A330seatArrange) {
        this.A330seatArrange = A330seatArrange;
    }

    /**
     * @return the B777_200seatArrange
     */
    public List<String> getB777_200seatArrange() {
        return B777_200seatArrange;
    }

    /**
     * @param B777_200seatArrange the B777_200seatArrange to set
     */
    public void setB777_200seatArrange(List<String> B777_200seatArrange) {
        this.B777_200seatArrange = B777_200seatArrange;
    }

    /**
     * @return the B777_200ERseatArrange
     */
    public List<String> getB777_200ERseatArrange() {
        return B777_200ERseatArrange;
    }

    /**
     * @param B777_200ERseatArrange the B777_200ERseatArrange to set
     */
    public void setB777_200ERseatArrange(List<String> B777_200ERseatArrange) {
        this.B777_200ERseatArrange = B777_200ERseatArrange;
    }

    /**
     * @return the B777_300seatArrange
     */
    public List<String> getB777_300seatArrange() {
        return B777_300seatArrange;
    }

    /**
     * @param B777_300seatArrange the B777_300seatArrange to set
     */
    public void setB777_300seatArrange(List<String> B777_300seatArrange) {
        this.B777_300seatArrange = B777_300seatArrange;
    }

    /**
     * @return the B777_300ERseatArrange
     */
    public List<String> getB777_300ERseatArrange() {
        return B777_300ERseatArrange;
    }

    /**
     * @param B777_300ERseatArrange the B777_300ERseatArrange to set
     */
    public void setB777_300ERseatArrange(List<String> B777_300ERseatArrange) {
        this.B777_300ERseatArrange = B777_300ERseatArrange;
    }

    /**
     * @return the choose
     */
    public String getChoose() {
        return choose;
    }

    /**
     * @param choose the choose to set
     */
    public void setChoose(String choose) {
        this.choose = choose;
    }

    /**
     * @return the occupied
     */
    public List<String> getOccupied() {
        return occupied;
    }

    /**
     * @param occupied the occupied to set
     */
    public void setOccupied(List<String> occupied) {
        this.occupied = occupied;
    }

    /**
     * @return the classtype
     */
    public String getClasstype() {
        return classtype;
    }

    /**
     * @param classtype the classtype to set
     */
    public void setClasstype(String classtype) {
        this.classtype = classtype;
    }

    /**
     * @return the aircraftType
     */
    public String getAircraftType() {
        return aircraftType;
    }

    /**
     * @param aircraftType the aircraftType to set
     */
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public int getFavail() {
        return Favail;
    }

    public void setFavail(int Favail) {
        this.Favail = Favail;
    }

    public int getBavail() {
        return Bavail;
    }

    public void setBavail(int Bavail) {
        this.Bavail = Bavail;
    }

    public int getEavail() {
        return Eavail;
    }

    public void setEavail(int Eavail) {
        this.Eavail = Eavail;
    }

    public int getFbooked() {
        return Fbooked;
    }

    public void setFbooked(int Fbooked) {
        this.Fbooked = Fbooked;
    }

    public int getBbooked() {
        return Bbooked;
    }

    public void setBbooked(int Bbooked) {
        this.Bbooked = Bbooked;
    }

    public int getEbooked() {
        return Ebooked;
    }

    public void setEbooked(int Ebooked) {
        this.Ebooked = Ebooked;
    }

    /**
     * @return the selectedPaidUpgradeClass
     */
    public String getSelectedPaidUpgradeClass() {
        return selectedPaidUpgradeClass;
    }

    /**
     * @param selectedPaidUpgradeClass the selectedPaidUpgradeClass to set
     */
    public void setSelectedPaidUpgradeClass(String selectedPaidUpgradeClass) {
        this.selectedPaidUpgradeClass = selectedPaidUpgradeClass;
    }

    /**
     * @return the bookingClassLists
     */
    public List<String> getBookingClassLists() {
        return bookingClassLists;
    }

    /**
     * @param bookingClassLists the bookingClassLists to set
     */
    public void setBookingClassLists(List<String> bookingClassLists) {
        this.bookingClassLists = bookingClassLists;
    }

    /**
     * @return the totalWeightAllowed
     */
    public String getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    /**
     * @param totalWeightAllowed the totalWeightAllowed to set
     */
    public void setTotalWeightAllowed(String totalWeightAllowed) {
        this.totalWeightAllowed = totalWeightAllowed;
    }

    /**
     * @return the upgradeCosts
     */
    public double getUpgradeCosts() {
        return upgradeCosts;
    }

    /**
     * @param upgradeCosts the upgradeCosts to set
     */
    public void setUpgradeCosts(double upgradeCosts) {
        this.upgradeCosts = upgradeCosts;
    }

    /**
     * @return the baggageManagedBean
     */
    public BaggageManagedBean getBaggageManagedBean() {
        return baggageManagedBean;
    }

    /**
     * @param baggageManagedBean the baggageManagedBean to set
     */
    public void setBaggageManagedBean(BaggageManagedBean baggageManagedBean) {
        this.baggageManagedBean = baggageManagedBean;
    }

    /**
     * @return the upgradeCostsDB
     */
    public String getUpgradeCostsDB() {
        return upgradeCostsDB;
    }

    /**
     * @param upgradeCostsDB the upgradeCostsDB to set
     */
    public void setUpgradeCostsDB(String upgradeCostsDB) {
        this.upgradeCostsDB = upgradeCostsDB;
    }

    /**
     * @return the mARSManagedBean
     */
    public MARSManagedBean getmARSManagedBean() {
        return mARSManagedBean;
    }

    /**
     * @param mARSManagedBean the mARSManagedBean to set
     */
    public void setmARSManagedBean(MARSManagedBean mARSManagedBean) {
        this.mARSManagedBean = mARSManagedBean;
    }

    /**
     * @return the flightSchedules
     */
    public List<Schedule> getFlightSchedules() {
        return flightSchedules;
    }

    /**
     * @param flightSchedules the flightSchedules to set
     */
    public void setFlightSchedules(List<Schedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    /**
     * @return the selectedSchedule
     */
    public String getSelectedSchedule() {
        return selectedSchedule;
    }

    /**
     * @param selectedSchedule the selectedSchedule to set
     */
    public void setSelectedSchedule(String selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    /**
     * @return the nextAvailSchedules
     */
    public List<Schedule> getNextAvailSchedules() {
        return nextAvailSchedules;
    }

    /**
     * @param nextAvailSchedules the nextAvailSchedules to set
     */
    public void setNextAvailSchedules(List<Schedule> nextAvailSchedules) {
        this.nextAvailSchedules = nextAvailSchedules;
    }

    public void changeFlight1() {
        Date date = new Date();
        Date tmr = new Date(date.getTime() + 1000 * 60 * 60 * 24);

        if (getAircraftType().equals("Airbus A330-300")) {
//            if (Eavail == 0 && Bbooked == 30) {
            flightSchedules = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), date, booking.getServiceType(), 1, 0);
           
           
            List<Schedule> temps = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), tmr, booking.getServiceType(), 1, 0);
            
            for (Schedule s : temps) {
                flightSchedules.add(s);
            }

            for (Schedule s : flightSchedules) {

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                if (checkTime(formattedDate1, formattedDate2) < 0) {
                    nextAvailSchedules.add(s);
                }
            }

//            } else {
//                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not eligible for free change flight! Please select seat from economy class", "");
//                FacesContext.getCurrentInstance().addMessage(null, message);
//            }
        } else if (getAircraftType().equals("Boeing 777-200")) {
            flightSchedules = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), date, booking.getServiceType(), 1, 0);
            List<Schedule> temps = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), tmr, booking.getServiceType(), 1, 0);
            for (Schedule s : temps) {
                flightSchedules.add(s);
            }

            for (Schedule s : flightSchedules) {

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                if (checkTime(formattedDate1, formattedDate2) < 0) {
                    nextAvailSchedules.add(s);
                }
            }
        } else if (getAircraftType().equals("Boeing 777-200ER")) {
            flightSchedules = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), date, booking.getServiceType(), 1, 0);
            List<Schedule> temps = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), tmr, booking.getServiceType(), 1, 0);
            for (Schedule s : temps) {
                flightSchedules.add(s);
            }

            for (Schedule s : flightSchedules) {

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                if (checkTime(formattedDate1, formattedDate2) < 0) {
                    nextAvailSchedules.add(s);
                }
            }
        } else if (getAircraftType().equals("Boeing 777-300")) {
            flightSchedules = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), date, booking.getServiceType(), 1, 0);
            List<Schedule> temps = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), tmr, booking.getServiceType(), 1, 0);
            for (Schedule s : temps) {
                flightSchedules.add(s);
            }

            for (Schedule s : flightSchedules) {

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                if (checkTime(formattedDate1, formattedDate2) < 0) {
                    nextAvailSchedules.add(s);
                }
            }
        } else if (getAircraftType().equals("Boeing 777-300ER")) {
            flightSchedules = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), date, booking.getServiceType(), 1, 0);
            List<Schedule> temps = distributionSessionBean.retrieveDirectFlightsForDate(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginIATA(), booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationIATA(), tmr, booking.getServiceType(), 1, 0);
            for (Schedule s : temps) {
                flightSchedules.add(s);
            }

            for (Schedule s : flightSchedules) {

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(s.getStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);

                if (checkTime(formattedDate1, formattedDate2) < 0) {
                    nextAvailSchedules.add(s);
                }
            }
        }

    }

}
