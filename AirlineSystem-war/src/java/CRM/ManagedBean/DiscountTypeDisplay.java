/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.ManagedBean;

import CRM.Entity.DiscountType;

/**
 *
 * @author parthasarthygupta
 */
public class DiscountTypeDisplay {
    
    private DiscountType discountType;
    private int noOfClaimedCodes;
    private int  noOfUnclaimedCodes;

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
    
    
    
}
