/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Entity;

import Distribution.Entity.Baggage;
import Distribution.Entity.PNR;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author YiQuan
 */
@Entity
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;
    private double price;
    private String serviceType;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private SeatAvailability seatAvail = new SeatAvailability();
    private String flightNo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date flightDate;
    private String bookingStatus;
    
    private String travellerTitle;
    private String travellerFristName;
    private String travellerLastName;
    private String passportNumber;
    private String nationality;
    private String customerId;
    
    private boolean isChild;
    private boolean boughtInsurance;
    private double insuranceFare;
    private String foodSelection;
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "booking")
    private List<Baggage> baggages = new ArrayList<Baggage>();
    
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private PNR pnr = new PNR();
    
    private double totalWeightAllowed;
    private int totalNumBaggeAlloewd;
    
    
    public void createBooking(double price, String serviceType, 
            SeatAvailability seatAvail){
        this.setPrice(price);
        this.setServiceType(serviceType);
        this.setSeatAvail(seatAvail);
        setBookingDate(new Date());
        setBookingStatus("normal");
        this.setFlightNo(seatAvail.getSchedule().getFlight().getFlightNo());
        this.setFlightDate(seatAvail.getSchedule().getStartDate());
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
     * @return the bookingDate
     */
    public Date getBookingDate() {
        return bookingDate;
    }

    /**
     * @param bookingDate the bookingDate to set
     */
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getTravellerTitle() {
        return travellerTitle;
    }

    public String getTravellerFristName() {
        return travellerFristName;
    }

    public String getTravellerLastName() {
        return travellerLastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCustomerId() {
        return customerId;
    }

    public boolean isIsChild() {
        return isChild;
    }

    public boolean isBoughtInsurance() {
        return boughtInsurance;
    }

    public double getInsuranceFare() {
        return insuranceFare;
    }

    public String getFoodSelection() {
        return foodSelection;
    }

    public List<Baggage> getBaggages() {
        return baggages;
    }

    public double getTotalWeightAllowed() {
        return totalWeightAllowed;
    }

    public int getTotalNumBaggeAlloewd() {
        return totalNumBaggeAlloewd;
    }

    public void setTravellerTitle(String travellerTitle) {
        this.travellerTitle = travellerTitle;
    }

    public void setTravellerFristName(String travellerFristName) {
        this.travellerFristName = travellerFristName;
    }

    public void setTravellerLastName(String travellerLastName) {
        this.travellerLastName = travellerLastName;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setIsChild(boolean isChild) {
        this.isChild = isChild;
    }

    public void setBoughtInsurance(boolean boughtInsurance) {
        this.boughtInsurance = boughtInsurance;
    }

    public void setInsuranceFare(double insuranceFare) {
        this.insuranceFare = insuranceFare;
    }

    public void setFoodSelection(String foodSelection) {
        this.foodSelection = foodSelection;
    }

    public void setBaggages(List<Baggage> baggages) {
        this.baggages = baggages;
    }

    public void setTotalWeightAllowed(double totalWeightAllowed) {
        this.totalWeightAllowed = totalWeightAllowed;
    }

    public void setTotalNumBaggeAlloewd(int totalNumBaggeAlloewd) {
        this.totalNumBaggeAlloewd = totalNumBaggeAlloewd;
    }
   
    
    
}
