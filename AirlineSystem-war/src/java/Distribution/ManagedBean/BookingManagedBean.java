/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import APS.Entity.Schedule;
import Distribution.Entity.Baggage;
import Distribution.Entity.PNR;
import Inventory.Entity.SeatAvailability;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Yunna
 */
@Named(value = "bookingManagedBean")
@Dependent
public class BookingManagedBean {

   private Date flightDate;
    private String bookingStatus;
    private String classCode;
    private String serviceType;
    
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
    
    private Long id;
    private double price;
    private SeatAvailability seatAvail;
    private String flightNo;
    
    private List<Baggage> baggages;
    
    private PNR pnr;
    
    private double totalWeightAllowed;
    private int totalNumBaggeAlloewd;
    
    private Schedule selectedDepartureSchedule;
    private Schedule selectedReturnSchedule;
    
    public BookingManagedBean() {
    }
    
    public String createBooking () {
        
        return "payment";
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
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
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
     * @return the flightNo
     */
    public String getFlightNo() {
        return flightNo;
    }

    /**
     * @param flightNo the flightNo to set
     */
    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    /**
     * @return the baggages
     */
    public List<Baggage> getBaggages() {
        return baggages;
    }

    /**
     * @param baggages the baggages to set
     */
    public void setBaggages(List<Baggage> baggages) {
        this.baggages = baggages;
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
    
}
