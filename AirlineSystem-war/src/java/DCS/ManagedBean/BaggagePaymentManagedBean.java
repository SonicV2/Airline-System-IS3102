/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HOULIANG
 */
@Named(value = "baggagePaymentManagedBean")
@SessionScoped
public class BaggagePaymentManagedBean implements Serializable {
    
    private String creditCard;
    private String csv;
    private Date paymentDate;
    private double extra;
    
    
    public void makePayment(){
        paymentDate=new Date();
        
    }
    
    public BaggagePaymentManagedBean() {
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }
    
    
    
}
