/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Session;

import CRM.Entity.DiscountCode;
import CRM.Entity.DiscountType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author parthasarthygupta
 */
@Stateless
public class DiscountSessionBean implements DiscountSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<DiscountType> retrieveAllDiscountTypes() {
        List<DiscountType> discountTypes = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM DiscountType a");

            List<DiscountType> results = q.getResultList();
            if (!results.isEmpty()) {
                discountTypes = results;

            } else {
                discountTypes = null;
                System.out.println("No discountTypes Available!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return discountTypes;
    }

    @Override
    public List<DiscountCode> retrieveAllDiscountCodes() {
        List<DiscountCode> discountCodes = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM DiscountCode a");

            List<DiscountCode> results = q.getResultList();
            if (!results.isEmpty()) {
                discountCodes = results;

            } else {
                discountCodes = null;
                System.out.println("No discountTypes Available!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return discountCodes;
    }

    @Override
    public String generateDiscountCode() {
        String codeNumber = "";
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 5; i++) {
            codeNumber += possible.charAt((int) Math.floor(Math.random() * possible.length()));
        }
        return codeNumber;
    }
    
    @Override
    public boolean discountCodeValid (String code){
        List<DiscountCode> allDiscountCodes = retrieveAllDiscountCodes();
        if (allDiscountCodes == null || allDiscountCodes.isEmpty())
            return false;
        else{
            for (DiscountCode eachCode : allDiscountCodes){
                if (eachCode.getCodeNumber().equals(code) && eachCode.isClaimed()==false)
                    return true;
            }
            return false;
        }        
    }
    
    @Override
    public boolean discountTypeExists(double discount, String type){
        List<DiscountType> allDiscountTypes =  new ArrayList();
        if (allDiscountTypes == null || allDiscountTypes.isEmpty())
            return false;
        else{
            for (DiscountType eachDiscountType : allDiscountTypes){
                if (eachDiscountType.getDiscount()==discount && eachDiscountType.getType().equalsIgnoreCase(type))
                    return true;
            }
            return false;
        }
    }
    
    @Override
    public void addDiscountType (String description, double mileagePointsToRedeem, double discount, String type, Date expiryDate){
        DiscountType discountType = new DiscountType();
        discountType.setDescription(description);
        discountType.setDiscount(discount);
        discountType.setMileagePointsToRedeem(mileagePointsToRedeem);
        discountType.setNoOfCodesUnredeemed(0);
        discountType.setExpiryDate(expiryDate);
        discountType.setType(type);
        em.persist(discountType);
    }
    
    @Override
    public boolean discountTypeHasUnclaimedCodes (DiscountType discountType){
        List<DiscountCode> discountCodes = discountType.getDiscountCodes();
        if (discountCodes == null || discountCodes.isEmpty()){
            return false;
        }
        else{
            for (DiscountCode eachCode : discountCodes){
                if (eachCode.isClaimed()==false)
                    return true;
            }
        }
        return false;
    }
    
    @Override
    public void deleteDiscountType (DiscountType discountType){
        DiscountType mergedDiscountType = em.merge(discountType);
        em.remove(mergedDiscountType);
        em.flush();
    }
    
    @Override
     public int claimedCodesForDiscountType (DiscountType discountType){
         List<DiscountCode> discountCodes = discountType.getDiscountCodes();
        if (discountCodes == null || discountCodes.isEmpty()){
            return 0;
        }
        else{
            int count = 0;
            for (DiscountCode eachCode : discountCodes){
                if (eachCode.isClaimed()==true)
                    count++;
            }
        return count;
        }     
     }
     
      @Override
     public int unclaimedCodesForDiscountType (DiscountType discountType){
         List<DiscountCode> discountCodes = discountType.getDiscountCodes();
        if (discountCodes == null || discountCodes.isEmpty()){
            return 0;
        }
        else{
            int count = 0;
            for (DiscountCode eachCode : discountCodes){
                if (eachCode.isClaimed()==false)
                    count++;
            }
                 return count;
        }
   
     }
     
     @Override
      public String addDiscountCode(DiscountType discountType){
          DiscountCode discountCode = new DiscountCode();
          String codeGenerated = generateDiscountCode();
          discountCode.setCodeNumber(codeGenerated);
          discountCode.setClaimed(false);
          List<DiscountCode> currentCodes = discountType.getDiscountCodes();
          currentCodes.add(discountCode);
          discountType.setDiscountCodes(currentCodes);
          discountCode.setDiscountType(discountType);
          em.persist(discountCode);
          em.merge(discountType);
          em.flush();
          return codeGenerated;
      }
      
      @Override
      public DiscountCode getDiscountCodeFromCode (String code){
          List<DiscountCode> allDiscountCodes = retrieveAllDiscountCodes();
          if (allDiscountCodes!=null && !allDiscountCodes.isEmpty()){
              for (DiscountCode eachDiscountCode : allDiscountCodes){
                  if (eachDiscountCode.getCodeNumber().equals(code))
                      return eachDiscountCode;
              }
              return null;
          }
          else 
              return null;
      }
      
      @Override
      public void markCodeAsClaimed (DiscountCode discountCode){
          discountCode.setClaimed(true);
          em.merge(discountCode);
          em.flush();
      }
     
          @Override
    public List<DiscountType> retrieveAllMileageDiscountTypes() {
        List<DiscountType> discountTypes = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM DiscountType a");

            List<DiscountType> results = q.getResultList();
            if (!results.isEmpty()) {
                discountTypes = results;

            } else {
                discountTypes = null;
                System.out.println("No discountTypes Available!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        List<DiscountType> mileageDiscountTypes = new ArrayList();
        for (DiscountType eachDiscountType : discountTypes){
            if (eachDiscountType.getType().equalsIgnoreCase("Mileage"))
                mileageDiscountTypes.add(eachDiscountType);
        }
        return mileageDiscountTypes;
    }
    
      @Override
    public List<DiscountType> retrieveAllPromotionDiscountTypes() {
        List<DiscountType> discountTypes = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM DiscountType a");

            List<DiscountType> results = q.getResultList();
            if (!results.isEmpty()) {
                discountTypes = results;

            } else {
                discountTypes = null;
                System.out.println("No discountTypes Available!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        List<DiscountType> mileageDiscountTypes = new ArrayList();
        for (DiscountType eachDiscountType : discountTypes){
            if (eachDiscountType.getType().equalsIgnoreCase("Promotion"))
                mileageDiscountTypes.add(eachDiscountType);
        }
        return mileageDiscountTypes;
    }
     
}
