package DCS.ManagedBean;

import APS.Entity.Schedule;
import CI.Managedbean.LoginManagedBean;
import DCS.Session.BoardingPassSessionBeanLocal;
import DCS.Session.CheckInRecordSessionBeanLocal;
import DCS.Session.SeatSessionBeanLocal;
import Inventory.Entity.Booking;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    private BoardingPassSessionBeanLocal boardingPassSessionBean;
    @EJB
    private CheckInRecordSessionBeanLocal checkInRecordSessionBean;

    @EJB
    private SeatSessionBeanLocal seatSessionBean;
    
    

    @ManagedProperty(value = "#{searchBookingManagedBean}")
    private SearchBookingManagedBean searchBookingManagedBean;
    
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
    
    @PostConstruct
    public void init() {

        booking=searchBookingManagedBean.getReqBooking();
        schedule = searchBookingManagedBean.getCheckinSchedule();
        setOccupied(seatSessionBean.retrieveOccupiedSeats(searchBookingManagedBean.getCheckinSchedule()));
        if (occupied.isEmpty()) {
            occupied.add(" ");
        }

        setClasstype(getSearchBookingManagedBean().getServiceClass());

        setAircraftType(getSearchBookingManagedBean().getAircraftType());

        if (getAircraftType().equals("Airbus A330-300")) {
            addSeatA330();
            Favail=0;
            Bavail=30-schedule.getSelectedSeatsB().size();
            Eavail=255-schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-200")) {
            addSeatB777_200();
             Favail=0;
            Bavail=38-schedule.getSelectedSeatsB().size();
            Eavail=228-schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-200ER")) {
            addSeatB777_200ER();
            Favail=0;
            Bavail=30-schedule.getSelectedSeatsB().size();
            Eavail=255-schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-300")) {
            addSeatB777_300();
            Favail=8-schedule.getSelectedSeatsF().size();
            Bavail=50-schedule.getSelectedSeatsB().size();
            Eavail=226-schedule.getSelectedSeatsE().size();
        } else if (getAircraftType().equals("Boeing 777-300ER")) {
            addSeatB777_300ER();
             Favail=4-schedule.getSelectedSeatsF().size();
            Bavail=48-schedule.getSelectedSeatsB().size();
            Eavail=232-schedule.getSelectedSeatsE().size();
        }

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

    public void chooseSeatA330() {

        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }  // make sure you cannot choose seat that is already booked

        if (getA330seatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 8) {
                getA330seatArrange().set(getA330seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());

            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) < 9) {
                getA330seatArrange().set(getA330seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {

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

    public void chooseSeatB777_200() {

        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }  // make sure you cannot choose seat that is already booked

        if (getB777_200seatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 10) {
                getB777_200seatArrange().set(getB777_200seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) < 11) {
                getB777_200seatArrange().set(getB777_200seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

        } else {

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

    public void chooseSeatB777_200ER() {
        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }  // make sure you cannot choose seat that is already booked

        if (getB777_200ERseatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 8) {
                getB777_200ERseatArrange().set(getB777_200ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) < 9) {
                getB777_200ERseatArrange().set(getB777_200ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } else {

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

    public void chooseSeatB777_300() {

        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }  // make sure you cannot choose seat that is already booked

        if (getB777_300seatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 11) {
                getB777_300seatArrange().set(getB777_300seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) > 2 && Integer.parseInt(getChoose().substring(1)) < 12) {
                getB777_300seatArrange().set(getB777_300seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else if (getClasstype().equals("First Class") && Integer.parseInt(getChoose().substring(1)) < 3) {
                getB777_300seatArrange().set(getB777_300seatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenF(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

        } else {

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

    public void chooseSeatB777_300ER() {
        if (getOccupied().contains(getChoose().toUpperCase())) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "seat has already been choosen!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }  // make sure you cannot choose seat that is already booked

        if (getB777_300ERseatArrange().contains("\uD83D\uDCBA" + getChoose().toUpperCase())) {
            if (getClasstype().contains("Economy") && Integer.parseInt(getChoose().substring(1)) > 9) {
                getB777_300ERseatArrange().set(getB777_300ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenE(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else if (getClasstype().equals("Business") && Integer.parseInt(getChoose().substring(1)) > 1 && Integer.parseInt(getChoose().substring(1)) < 10) {
                getB777_300ERseatArrange().set(getB777_300ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());

                seatSessionBean.inputChosenB(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else if (getClasstype().equals("First Class") && Integer.parseInt(getChoose().substring(1)) < 2) {
                getB777_300ERseatArrange().set(getB777_300ERseatArrange().indexOf("\uD83D\uDCBA" + getChoose().toUpperCase()), "\n" + "\u26D4" + "\n" + getChoose().toUpperCase());

                getOccupied().add(getChoose().toUpperCase());
                seatSessionBean.inputChosenF(schedule, choose.toUpperCase());
                checkInRecordSessionBean.addSeat(booking, choose.toUpperCase());
                boardingPassSessionBean.addSeat(booking, choose.toUpperCase());
                
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "please choose the correct class", "");
                FacesContext.getCurrentInstance().addMessage(null, message);

            }

        } else {

        }

    }

//-------------------------------------------------------------------------------------------------------    
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
    
    
    

}
