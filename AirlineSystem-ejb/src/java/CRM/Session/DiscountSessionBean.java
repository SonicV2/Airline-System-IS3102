/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Session;

import CRM.Entity.DiscountCode;
import CRM.Entity.DiscountType;
import java.util.ArrayList;
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
    public boolean discountTypeExists(double discount){
        List<DiscountType> allDiscountTypes =  new ArrayList();
        if (allDiscountTypes == null || allDiscountTypes.isEmpty())
            return false;
        else{
            for (DiscountType eachDiscountType : allDiscountTypes){
                if (eachDiscountType.getDiscount()==discount)
                    return true;
            }
            return false;
        }
    }
    
    @Override
    public void addDiscountType (String description, double mileagePointsToRedeem, double discount){
        DiscountType discountType = new DiscountType();
        discountType.setDescription(description);
        discountType.setDiscount(discount);
        discountType.setMileagePointsToRedeem(mileagePointsToRedeem);
        discountType.setNoOfCodesUnredeemed(0);
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
        em.remove(discountType);
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
          em.persist(discountCode);
          em.merge(discountType);
          em.flush();
          return codeGenerated;
      }
     
     
}
