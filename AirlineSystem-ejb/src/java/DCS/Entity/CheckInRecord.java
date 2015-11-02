/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author HOULIANG
 */
@Entity
public class CheckInRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private long bookingID;
    
    private String creditCardNo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentDate;
    
    private String boardingPassNo;

    private String Status;
    
    private String changeFlight;
    private String hotelVoucher;
    private String seatNo;
    
    private String field1;
    
    public CheckInRecord(){}
    
    public void create(){
        this.Status="N.A";
        this.boardingPassNo="N.A";
        this.bookingID=0;
        this.changeFlight="N.A";
        this.creditCardNo="N.A";
        this.field1="N.A";
        this.hotelVoucher="N.A";
        this.paymentDate=null;   
        this.seatNo="N.A";
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBookingID() {
        return bookingID;
    }

    public void setBookingID(long bookingID) {
        this.bookingID = bookingID;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBoardingPassNo() {
        return boardingPassNo;
    }

    public void setBoardingPassNo(String boardingPassNo) {
        this.boardingPassNo = boardingPassNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getChangeFlight() {
        return changeFlight;
    }

    public void setChangeFlight(String changeFlight) {
        this.changeFlight = changeFlight;
    }

    public String getHotelVoucher() {
        return hotelVoucher;
    }

    public void setHotelVoucher(String hotelVoucher) {
        this.hotelVoucher = hotelVoucher;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
    

  
}
