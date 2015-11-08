/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.ManagedBean;

import CRM.Entity.DiscountType;
import static CRM.Entity.DiscountType_.expiryDate;
import java.util.Date;

/**
 *
 * @author parthasarthygupta
 */
public class DiscountTypeDisplay {
    
    private DiscountType discountType;
    private int noOfClaimedCodes;
    private int  noOfUnclaimedCodes;
    private String expiryDate;


    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public int getNoOfClaimedCodes() {
        return noOfClaimedCodes;
    }

    public void setNoOfClaimedCodes(int noOfClaimedCodes) {
        this.noOfClaimedCodes = noOfClaimedCodes;
    }

    public int getNoOfUnclaimedCodes() {
        return noOfUnclaimedCodes;
    }

    public void setNoOfUnclaimedCodes(int noOfUnclaimedCodes) {
        this.noOfUnclaimedCodes = noOfUnclaimedCodes;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    
}
