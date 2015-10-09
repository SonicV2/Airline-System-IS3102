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
import APS.Entity.Schedule;
import javax.persistence.CascadeType;
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
    private int economySaverBooked;
    private int economyBasicBooked;
    private int economyPremiumBooked;
    private int businessBooked;
    private int firstClassBooked;
    @OneToMany
    private List<Booking> bookings = new ArrayList<Booking>();
    
    @OneToOne(cascade={CascadeType.ALL})
    private Schedule schedule;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    public void createSeatAvail (Schedule schedule, int []seats){
        this.setSchedule(schedule);
        this.setEconomySaverTotal(seats[0]);
        this.setEconomyBasicTotal(seats[1]);
        this.setEconomyPremiumTotal(seats[2]);
        this.setBusinessTotal(seats[3]);
        this.setFirstClassTotal(seats[4]);
        this.setFirstClassBooked(0);
        this.setEconomySaverBooked(0);
        this.setEconomyBasicBooked(0);
        this.setBusinessBooked(0);
        this.setEconomyPremiumBooked(0);
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
     * @return the schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * @param schedule the schedule to set
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    
   
    
}
