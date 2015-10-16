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
import java.util.List;
import java.util.HashMap;




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
    private double mean;
    private int median;
    private int mode;
    private List<CustomerScore> csList;
    private int hmSize;
    private HashMap<Integer, Integer> hm;
    private String test;
    private List<Double> doubleList;


    /**
     * Creates a new instance of CRMAnalyticsManagedBean
     */
    public CRMAnalyticsManagedBean() {
        test= "haha";
    }

    public String retrieveCustomerList() {
        if (type.equals("Recency")) {
            csList = am.getRecency(from, to);
        } else if (type.equals("Frequency")) {
            csList = am.getFrequency(from, to);
        } else if (type.equals("Monetary")) {
            csList = am.getMonetary(from, to);
        }

        return "viewListResults";
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
    

    
    public void calculateAvg() {
        int size = csList.size();
        double totalScore = 0.0;
        for (int i = 0; i < size; i++) {
            CustomerScore cs = csList.get(i);
            totalScore += cs.getScore();
        }

        mean = totalScore / size;

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
        int max = 1, temp =0;
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
        mode= temp;

    }

    public String calMMM() {
        if (type.equals("Recency")) {
            csList = am.getRecency(0, 100);
        } else if (type.equals("Frequency")) {
            csList = am.getFrequency(0, 100);
        } else if (type.equals("Monetary")) {
            csList = am.getMonetary(0, 100);
        }
        calculateAvg();
        calculateMedian();
        calculateMode();
        
   
        
        return "analyticsResults";
    }
    
    public String getName(int index){
        
        return csList.get(index).getName();
    }
    public double getScore(int index){
        System.out.println("Get Score!!!");
        System.out.println(index);
        System.out.println(csList.get(index).getScore());
        return csList.get(index).getScore();
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

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
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
