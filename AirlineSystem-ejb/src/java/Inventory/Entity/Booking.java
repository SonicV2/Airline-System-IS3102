/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Entity;

import APS.Entity.Flight;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

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
    private String bookingDate;
    private double price;
    @OneToOne
    private BookingClass bookingClass;
    @OneToOne
    private SeatAvailability seatAvail = new SeatAvailability(); 

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
    public String getBookingDate() {
        return bookingDate;
    }

    /**
     * @param bookingDate the bookingDate to set
     */
    public void setBookingDate(String bookingDate) {
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
     * @return the bookingClass
     */
    public BookingClass getBookingClass() {
        return bookingClass;
    }

    /**
     * @param bookingClass the bookingClass to set
     */
    public void setBookingClass(BookingClass bookingClass) {
        this.bookingClass = bookingClass;
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

    
    
}
