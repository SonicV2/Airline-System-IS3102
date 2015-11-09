/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Session;

import CRM.Entity.DiscountCode;
import CRM.Entity.DiscountType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface DiscountSessionBeanLocal {
    
    public boolean discountTypeExists(double discount, String type);
    public String generateDiscountCode ();
    public List<DiscountType> retrieveAllDiscountTypes ();
    public List<DiscountCode> retrieveAllDiscountCodes();
    public boolean discountCodeValid (String code);
    public void addDiscountType (String description, double mileagePointsToRedeem, double discount, String type, Date expiryDate);
    public boolean discountTypeHasUnclaimedCodes (DiscountType discountType);
    public void deleteDiscountType (DiscountType discountType);
    public int claimedCodesForDiscountType (DiscountType discountType);
    public int unclaimedCodesForDiscountType (DiscountType discountType);
    public String addDiscountCode(DiscountType discountType);
    public String addExpiredDiscountCode (DiscountType discountType);
    public DiscountCode getDiscountCodeFromCode (String code);
    public void markCodeAsClaimed (DiscountCode discountCode);
    public List<DiscountType> retrieveAllMileageDiscountTypes();
    public List<DiscountType> retrieveAllPromotionDiscountTypes();
    public void deleteCodesOfType(DiscountType discountType);

    public List<String> sendDiscountCodes (DiscountType discountType, int no);
    public List<DiscountType> retrieveValidDiscounts();
    
}
