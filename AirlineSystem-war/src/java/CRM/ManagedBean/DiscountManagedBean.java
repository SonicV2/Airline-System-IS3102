/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.ManagedBean;

import CI.Session.EmailSessionBeanLocal;
import CRM.Entity.DiscountType;
import CRM.Session.AnalyticsSessionBeanLocal;
import CRM.Session.CustomerScore;
import CRM.Session.DiscountSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author parthasarthygupta
 */
@Named(value = "discountManagedBean")
@ManagedBean
@SessionScoped
public class DiscountManagedBean {

    @EJB
    private DiscountSessionBeanLocal discountSessionBean;

    @EJB
    private AnalyticsSessionBeanLocal analyticsSessionBean;

    @EJB
    private EmailSessionBeanLocal emailSessionBean;

    private List<DiscountType> discountTypes;
    private DiscountType selectedDiscountType;
    private List<DiscountTypeDisplay> discountTypeDisplays;
    private String description;
    private double discount;
    private double noOfMileagePointsToRedeem;
    FacesMessage message = null;
    private List<Integer> percentileList;
    private int selectedPercentile;
    private List<String> analysisTypeList;
    private String analysisType;
    private int from;
    private int to;

    @PostConstruct
    public void init() {
        discountTypes = new ArrayList();
        discountTypeDisplays = new ArrayList();
        discountTypes = discountSessionBean.retrieveAllDiscountTypes();
        analysisTypeList = new ArrayList();
        analysisTypeList.add("Recency");
        analysisTypeList.add("Frequency");
        analysisTypeList.add("Monetary Value");
        analysisTypeList.add("Customer Lifetime Value");

    }

    public String manageDiscountTypes() {
        discountTypeDisplays.clear();
        discount = 0.0;
        noOfMileagePointsToRedeem = 0.0;
        description = "";
        if (!discountTypes.isEmpty()) {
            DiscountTypeDisplay eachDiscountTypeDisplay;
            for (DiscountType eachDiscountType : discountTypes) {
                eachDiscountTypeDisplay = new DiscountTypeDisplay();
                eachDiscountTypeDisplay.setDiscountType(eachDiscountType);
                eachDiscountTypeDisplay.setNoOfClaimedCodes(discountSessionBean.claimedCodesForDiscountType(eachDiscountType));
                eachDiscountTypeDisplay.setNoOfUnclaimedCodes(discountSessionBean.unclaimedCodesForDiscountType(eachDiscountType));
                discountTypeDisplays.add(eachDiscountTypeDisplay);
            }
        }
        return "ManageDiscountTypes";
    }

    public String addDiscountType() {
        if (discountSessionBean.discountTypeExists(discount)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Similar Discount Type already exists!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        } else {
            discountSessionBean.addDiscountType(description, noOfMileagePointsToRedeem, discount);
            discountTypes = discountSessionBean.retrieveAllDiscountTypes();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Type added!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return manageDiscountTypes();
        }

    }

    public String deleteDiscountType() {
          //MAKE SURE selectedDiscountType IS SET BEFORE   

        if (discountSessionBean.discountTypeHasUnclaimedCodes(selectedDiscountType)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Type cannot be deleted as it has unclaimed codes!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        } else {
            discountSessionBean.deleteDiscountType(selectedDiscountType);
            discountTypes = discountSessionBean.retrieveAllDiscountTypes();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Type deleted!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return manageDiscountTypes();
        }

    }

    private String sendDiscountCodesToTopCustomers() {
        //Set analysisType,selectedpercentile & selectedDiscountType, from, to
        if (from > to || from < 0 || from > 100 || to < 0 || to > 100) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Entered range is incorrect", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            List<CustomerScore> customers = new ArrayList();
            if (analysisType.equals("Recency")) {
                customers = analyticsSessionBean.getRecency(from, to);
            } else if (analysisType.equals("Frequency")) {
                customers = analyticsSessionBean.getFrequency(from, to);
            } else if (analysisType.equals("Monetary Value")) {
                customers = analyticsSessionBean.getMonetary(from, to);
            } else if (analysisType.equals("Customer Lifetime Value")) {
                customers = analyticsSessionBean.getCLV(from, to, analyticsSessionBean.getRetentionRate());
            }

            if (customers == null || customers.isEmpty()) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Customers found!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                for (CustomerScore eachCustomerScore : customers) {
                    String code = discountSessionBean.addDiscountCode(selectedDiscountType);
                    sendEmailToTopCustomers(eachCustomerScore.getEmail(), eachCustomerScore.getName(), selectedDiscountType, code);
                }
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount codes sent!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
        return null;
    }

    public void sendEmailToTopCustomers(String email, String name, DiscountType selectedDiscountType, String code) {

        String body = "";
        body += "Dear " + name + ",\n\nCongratulations! As part of our ongoing promotional campaign at Merlion Airlines, we are pleased to announced that you have been awarded a " + selectedDiscountType.getDiscount() + "% discount voucher which can be redeemed on your next booking at Merlion Airlines websiste.";
        body += "\n\nYour discount voucher code is " + code + ".";
        body += "\n\nWe look forward to seeing you on board soon!";
        emailSessionBean.sendEmail(email, "Merlion Airlines Promotionanl Code", body);
    }

    public List<DiscountType> getDiscountTypes() {
        return discountTypes;
    }

    public void setDiscountTypes(List<DiscountType> discountTypes) {
        this.discountTypes = discountTypes;
    }

    public DiscountType getSelectedDiscountType() {
        return selectedDiscountType;
    }

    public void setSelectedDiscountType(DiscountType selectedDiscountType) {
        this.selectedDiscountType = selectedDiscountType;
    }

    public List<DiscountTypeDisplay> getDiscountTypeDisplays() {
        return discountTypeDisplays;
    }

    public void setDiscountTypeDisplays(List<DiscountTypeDisplay> discountTypeDisplays) {
        this.discountTypeDisplays = discountTypeDisplays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getNoOfMileagePointsToRedeem() {
        return noOfMileagePointsToRedeem;
    }

    public void setNoOfMileagePointsToRedeem(double noOfMileagePointsToRedeem) {
        this.noOfMileagePointsToRedeem = noOfMileagePointsToRedeem;
    }

    public List<Integer> getPercentileList() {
        return percentileList;
    }

    public void setPercentileList(List<Integer> percentileList) {
        this.percentileList = percentileList;
    }

    public int getSelectedPercentile() {
        return selectedPercentile;
    }

    public void setSelectedPercentile(int selectedPercentile) {
        this.selectedPercentile = selectedPercentile;
    }

    public List<String> getAnalysisTypeList() {
        return analysisTypeList;
    }

    public void setAnalysisTypeList(List<String> analysisTypeList) {
        this.analysisTypeList = analysisTypeList;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

}
