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
            description = "Frequency Score is  the many times  the customer flew with Merlion Airlines in the last year. The higher the better.";
            csList = am.getFrequency(from, to);
        } else if (type.equals("Monetary")) {
            description = "Monetary Score is  the amount the customer spent with Merlion Airlines in the last year. The higher the better";
            csList = am.getMonetary(from, to);
        } else if (type.equals("Customer Lifetime Value")) {
            description = "Customer Lifetime Value is a prediction of how valuable a customer is to Merlion Airlines";
            double retention = am.getRetentionRate();
            csList = am.getCLV(from, to, retention);
        }
        return "ViewListResults";
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

}
