/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Entity;

//import static FOS.Entity.Team_.id;
import Inventory.Entity.Booking;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author parthasarthygupta
 */
@Entity
public class PNR implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String pnrID;
    @OneToMany (cascade = {CascadeType.PERSIST}, mappedBy = "pnr")
    private List<Booking> bookings = new ArrayList();
    private int noOfTravellers;
    private String email;
    private String contactNo;
    private String pnrStatus;
    private double totalPrice;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBooking;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfConfirmation;
    
    private String bookingAvenue; 

    public void createPNR(String pnrID, int noOfTravellers, String email, String contactNo, String pnrStatus, double totalPrice, Date dateOfBooking, Date dateOfConfirmation, String bookingAvenue){
        this.pnrID = pnrID;
        this.noOfTravellers = noOfTravellers;
        this.email = email;
        this.contactNo = contactNo;
        this.totalPrice = totalPrice;  
        this.dateOfBooking = dateOfBooking;
        this.dateOfConfirmation = dateOfConfirmation;
        this.bookingAvenue = bookingAvenue;
        this.pnrStatus = pnrStatus;
    }
    
    public String getPnrID() {
        return pnrID;
    }

    public void setPnrID(String pnrID) {
        this.pnrID = pnrID;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getNoOfTravellers() {
        return noOfTravellers;
    }

    public void setNoOfTravellers(int noOfTravellers) {
        this.noOfTravellers = noOfTravellers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPnrStatus() {
        return pnrStatus;
    }

    public void setPnrStatus(String pnrStatus) {
        this.pnrStatus = pnrStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(Date dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public String getBookingAvenue() {
        return bookingAvenue;
    }

    public void setBookingAvenue(String bookingAvenue) {
        this.bookingAvenue = bookingAvenue;
    }

    public Date getDateOfConfirmation() {
        return dateOfConfirmation;
    }

    public void setDateOfConfirmation(Date dateOfConfirmation) {
        this.dateOfConfirmation = dateOfConfirmation;
    }
     
    
    

 


    @Override
    public String toString() {
        return "Distribution.Entity.PNR[ id=" + pnrID + " ]";
    }
    
}
