/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Entity;

import APS.Entity.Flight;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author YiQuan
 */
@Entity
public class SeatAvailability implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int economySaverTotal;
    private int economyBasicTotal;
    private int economyPremiumTotal;
    private int businessTotal;
    private int firstClassTotal;
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    @Temporal(TemporalType.DATE)
    private Date flightDate;
    private int economySaverBooked;
    private int economyBasicBooked;
    private int economyPremiumBooked;
    private int businessBooked;
    private int firstClassBooked;
    @OneToMany(mappedBy="seatAvail")
    private List<Booking> bookings = new ArrayList<Booking>();
    
    private String flightNo;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    public void createSeatAvail (String flightNo, int []seats, 
            Date releaseDate, Date flightDate){
        this.releaseDate= releaseDate;
        this.flightDate= flightDate;
        this.flightNo= flightNo;
        this.economySaverTotal = seats[0];
        this.economyBasicTotal = seats[1];
        this.economyPremiumTotal = seats[2];
        this.businessTotal = seats[3];
        this.firstClassTotal=seats[4];
        this.firstClassBooked=0;
        this.economySaverBooked =0;
        this.economyBasicBooked =0;
        this.businessBooked=0;
        this.economyPremiumBooked=0;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the economySaverTotal
     */
    public int getEconomySaverTotal() {
        return economySaverTotal;
    }

    /**
     * @param economySaverTotal the economySaverTotal to set
     */
    public void setEconomySaverTotal(int economySaverTotal) {
        this.economySaverTotal = economySaverTotal;
    }

    /**
     * @return the economyBasicTotal
     */
    public int getEconomyBasicTotal() {
        return economyBasicTotal;
    }

    /**
     * @param economyBasicTotal the economyBasicTotal to set
     */
    public void setEconomyBasicTotal(int economyBasicTotal) {
        this.economyBasicTotal = economyBasicTotal;
    }

    /**
     * @return the economyPremiumTotal
     */
    public int getEconomyPremiumTotal() {
        return economyPremiumTotal;
    }

    /**
     * @param economyPremiumTotal the economyPremiumTotal to set
     */
    public void setEconomyPremiumTotal(int economyPremiumTotal) {
        this.economyPremiumTotal = economyPremiumTotal;
    }

    /**
     * @return the businessTotal
     */
    public int getBusinessTotal() {
        return businessTotal;
    }

    /**
     * @param businessTotal the businessTotal to set
     */
    public void setBusinessTotal(int businessTotal) {
        this.businessTotal = businessTotal;
    }

    /**
     * @return the firstClassTotal
     */
    public int getFirstClassTotal() {
        return firstClassTotal;
    }

    /**
     * @param firstClassTotal the firstClassTotal to set
     */
    public void setFirstClassTotal(int firstClassTotal) {
        this.firstClassTotal = firstClassTotal;
    }

    /**
     * @return the releaseDate
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
     * @return the economySaverBooked
     */
    public int getEconomySaverBooked() {
        return economySaverBooked;
    }

    /**
     * @param economySaverBooked the economySaverBooked to set
     */
    public void setEconomySaverBooked(int economySaverBooked) {
        this.economySaverBooked = economySaverBooked;
    }

    /**
     * @return the economyBasicBooked
     */
    public int getEconomyBasicBooked() {
        return economyBasicBooked;
    }

    /**
     * @param economyBasicBooked the economyBasicBooked to set
     */
    public void setEconomyBasicBooked(int economyBasicBooked) {
        this.economyBasicBooked = economyBasicBooked;
    }

    /**
     * @return the economyPremiumBooked
     */
    public int getEconomyPremiumBooked() {
        return economyPremiumBooked;
    }

    /**
     * @param economyPremiumBooked the economyPremiumBooked to set
     */
    public void setEconomyPremiumBooked(int economyPremiumBooked) {
        this.economyPremiumBooked = economyPremiumBooked;
    }

    /**
     * @return the businessBooked
     */
    public int getBusinessBooked() {
        return businessBooked;
    }

    /**
     * @param businessBooked the businessBooked to set
     */
    public void setBusinessBooked(int businessBooked) {
        this.businessBooked = businessBooked;
    }

    /**
     * @return the firstClassBooked
     */
    public int getFirstClassBooked() {
        return firstClassBooked;
    }

    /**
     * @param firstClassBooked the firstClassBooked to set
     */
    public void setFirstClassBooked(int firstClassBooked) {
        this.firstClassBooked = firstClassBooked;
    }

    /**
     * @return the bookings
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * @param bookings the bookings to set
     */
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * @return the flight
     */
    public String getFlight() {
        return flightNo;
    }

    /**
     * @param flight the flight to set
     */
    public void setFlight(String flightNo) {
        this.flightNo = flightNo;
    }

   
    
}
