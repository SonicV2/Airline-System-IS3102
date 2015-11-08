/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.ManagedBean;

import CRM.Session.AnalyticsSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import CRM.Session.CustomerScore;
import Distribution.Entity.Customer;
import java.util.List;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import CI.Session.EmailSessionBeanLocal;
import CRM.Entity.DiscountType;
import CRM.Session.DiscountSessionBeanLocal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;

/**
 *
 * @author YiQuan
 */
@Named(value = "cRMAnalyticsManagedBean")
@ManagedBean
@SessionScoped
public class CRMAnalyticsManagedBean implements Serializable {

    @EJB
    private AnalyticsSessionBeanLocal am;
    
    @EJB
    private DiscountSessionBeanLocal dm;

    private String type;
    private int from;
    private int to;
    private String mean;
    private int median;
    private int mode;
    private List<CustomerScore> csList;
    private int hmSize;
    private HashMap<Integer, Integer> hm;
    private List<Customer> customerList;
    private String retentionRate;
    private String description = "HiHi";
    private Date expiry;
    private List<DiscountType> discountList;
    private DiscountType discountType;
    
    @EJB
    private EmailSessionBeanLocal emailSessionBean;
    private String emailBody;
    private String emailTitle;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy ");

    /**
     * Creates a new instance of CRMAnalyticsManagedBean
     */
  
    public CRMAnalyticsManagedBean() {

    }

    public String retrieveCustomerList() {
        if (to > 100) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "The To Percentile Has To Be Less Than 100", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "Index";
        }

        if (from >= to) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "The From Percentile Has To Be Less Than the To Percentile", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "Index";
        }

        if (type.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Select Type of Score", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "Index";
        }
        if (type.equals("Recency")) {
            description = "Recency Score is based on how many days since the customer last flown with Merlion Airlines compared to the current date. The lower the better,";
            csList = am.getRecency(from, to);
        } else if (type.equals("Frequency")) {
            description = "Frequency Score is the amount of times the customer flew with Merlion Airlines in the last year. The higher the better.";
            csList = am.getFrequency(from, to);
        } else if (type.equals("Monetary")) {
            description = "Monetary Score is  the amount the customer spent with Merlion Airlines in the last year. The higher the better";
            csList = am.getMonetary(from, to);
        } else if (type.equals("Customer Lifetime Value")) {
            description = "Customer Lifetime Value is a prediction of how valuable a customer is to Merlion Airlines";
            double retention = am.getRetentionRate();
            csList = am.getCLV(from, to, retention);
        }
        return "ViewListResults?faces-redirect=true";
    }

    public String getHistogramArray() {
        int size = csList.size();
        String results = " var results = [";
        results += "['Customer', 'Score'],";

        for (int i = 0; i < size - 1; i++) {
            results += "[";
            results += "'" + csList.get(i).getName() + "'" + "," + csList.get(i).getScore();
            results += "],";
        }
        results += "[";
        results += "'" + csList.get(size - 1).getName() + "'" + "," + csList.get(size - 1).getScore();
        results += "]";
        results += "]";
        return results;

    }

    public void calculateAvg() {
        int size = csList.size();
        double totalScore = 0.0;
        for (int i = 0; i < size; i++) {
            CustomerScore cs = csList.get(i);
            totalScore += cs.getScore();
        }

        mean = String.format("%1$,.2f", totalScore / size);

    }

    public void calculateMedian() {
        int index = csList.size() / 2;
        Double score = csList.get(index).getScore();
        median = score.intValue();
    }

    public void calculateMode() {

        int size = csList.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            Double score = csList.get(i).getScore();
            array[i] = score.intValue();
        }
        hm = new HashMap<Integer, Integer>();
        int max = 1, temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (hm.get(array[i]) != null) {
                int count = hm.get(array[i]);
                count = count + 1;
                hm.put(array[i], count);
                if (count > max) {
                    max = count;
                    temp = array[i];
                }
            } else {
                hm.put(array[i], 1);
            }
        }
        mode = temp;

    }

    public String calRecencyMMM() {
        description = "Recency Score is based on how many days since the customer last flown with Merlion Airlines compared to the current date. The lower the better,";

        csList = am.getRecency(0, 100);
        calculateAvg();
        calculateMedian();
        calculateMode();
        return "AnalyticsResults";
    }

    public String calFrequencyMMM() {
        description = "Frequency Score is  the many times  the customer flew with Merlion Airlines in the last year. The higher the better.";

        csList = am.getFrequency(0, 100);
        calculateAvg();
        calculateMedian();
        calculateMode();
        return "AnalyticsResults";
    }

    public String calMonetaryMMM() {
        description = "Monetary Score is  the amount the customer spent with Merlion Airlines in the last year. The higher the better";
        csList = am.getMonetary(0, 100);
        calculateAvg();
        calculateMedian();
        calculateMode();
        return "AnalyticsResults";
    }
    
    public String refresh(DiscountType discountType){
        this.discountType = discountType;
        return "SendDiscountCodes";
    }

    public String calCLVMMM() {
        description = "Customer Lifetime Value is a prediction of how valuable a customer is to Merlion Airlines";
        csList = am.getRecency(0, 100);
        double retention = am.getRetentionRate() * 100;
        System.out.println(retention);
        retentionRate = String.format("%1$,.2f", retention) + "%";
        calculateAvg();
        calculateMedian();
        calculateMode();
        return "CLVAnalyticsResults";
    }
    
    public void sendEmail(String email, String name) {
        
        String body = "";
        body += "Dear " + name + ",\n\n" + emailBody;
        body += "\n\nWe look forward to seeing you on board soon!";
        emailSessionBean.sendEmail(email, emailTitle, body);
    }
    
    public void sendPromotionEmail(String email, String name, String discountCode) {
        String body = "";
        String Date = df.format(expiry);
        String discount = String.valueOf(discountType.getDiscount());
        body += "Dear " + name + ",\n\n" + emailBody;
        body += "\n\n\n Your Discount Code is : " + discountCode + ". It will give u a "+ discount;
        body += "% discount. Its expiry date is " + Date + ".";
        body += "\n\nWe look forward to seeing you on board soon!";
        emailSessionBean.sendEmail(email, emailTitle, body);
    }
    
    public String sendMarketingEmail(){
        int size = csList.size();
        description= "You are sending a mass marketing email to customers from " + from+ " percentile" 
                +" to the " + to + " percentile of "+ type + " score. There exist " + size + " customers.";
        return "SendMarketingEmail";
    }
    
    public String sendPromotionalEmail(){
        int size = csList.size();
        discountList = dm.retrieveValidDiscounts();
        description= "You are sending a mass email with a discount code to customers from " + from+ " percentile" 
                +" to the " + to + " percentile of "+ type + " score. There exist " + size + " customers. Please select the discount type you want to send.";
        return "SendDiscountCodes";
    }
    
    public void sendDiscounttoCustomers(){
        expiry = discountType.getExpiryDate();
        int size = csList.size();
        List<String> codeList = dm.sendDiscountCodes(discountType, size);
        for(int i=0; i<size; i++){
            String email = csList.get(i).getEmail();
            String name = csList.get(i).getName();
            sendPromotionEmail(email,name, codeList.get(i));
        }
    }
    
    public void sendtoCustomerList1(){
        int size = csList.size();
        for(int i=0; i<size; i++){
            String email = csList.get(i).getEmail();
            String name = csList.get(i).getName();
            sendEmail(email,name);
        }
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }
    
    

    public String getRetentionRate() {
        return retentionRate;
    }

    public void setRetentionRate(String retentionRate) {
        this.retentionRate = retentionRate;
    }

    public String retrieveLostCustomers() {
        description="Lost customers are customers who previously flew with Merlion Airlines but had not flown for with Merlion Airlines for a year or more.";
        customerList = am.getLostCustomers();
        return "GetLostCustomers";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CustomerScore> getCsList() {
        return csList;
    }

    public void setCsList(List<CustomerScore> csList) {
        this.csList = csList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(int median) {
        this.median = median;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getHmSize() {
        return hmSize;
    }

    public void setHmSize(int hmSize) {
        this.hmSize = hmSize;
    }

    public HashMap<Integer, Integer> getHm() {
        return hm;
    }

    public void setHm(HashMap<Integer, Integer> hm) {
        this.hm = hm;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }  

    public List<DiscountType> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<DiscountType> discountList) {
        this.discountList = discountList;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
    
    

}
