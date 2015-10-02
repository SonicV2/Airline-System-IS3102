/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Entity;

import static FOS.Entity.Team_.id;
import Inventory.Entity.Booking;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author parthasarthygupta
 */
@Entity
public class PNR implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String pnrID;
    @OneToMany (cascade = {CascadeType.PERSIST}, mappedBy = "PNR")
    private List<Booking> bookings = new ArrayList<Booking> ();
    private int noOfTravellers;
    private String email;
    private String contactNo;
    private String pnrStatus;
    private double totalPrice;

    public void createPNR(String pnrID, int noOfTravellers, String email, String contactNo, double totalPrice){
        this.pnrID = pnrID;
        this.noOfTravellers = noOfTravellers;
        this.email = email;
        this.contactNo = contactNo;
        this.totalPrice = totalPrice;                      
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
    
    
    

 


    @Override
    public String toString() {
        return "Distribution.Entity.PNR[ id=" + id + " ]";
    }
    
}