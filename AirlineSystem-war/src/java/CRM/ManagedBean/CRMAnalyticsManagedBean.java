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
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import CRM.Session.CustomerScore;
import java.util.List;

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
    private double result;
    private List<CustomerScore> csList;

    /**
     * Creates a new instance of CRMAnalyticsManagedBean
     */
    public CRMAnalyticsManagedBean() {
    }

    public String retrieveCustomerList() {
        if (type.equals("Recency")) {
            csList = am.getRecency(from, to);
            System.out.println("Look Here !!!!");
            System.out.println(csList.get(0).getScore());
        }
        if (type.equals("Frequency")) {
            csList = am.getFrequency(from, to);
        }
        if (type.equals("Monetary")) {
            csList = am.getMonetary(from, to);
        }
        
        return "viewListResults";
    }
    

    public String calculateAvg() {
        if (type.equals("Recency")) {
            result = am.calAvgRecencyScore();
        }
        if (type.equals("Frequency")) {
            result = am.calAvgFrequencyScore();
        }
        if (type.equals("Monetary")) {
            result = am.calAvgMonetaryScore();
        }

        return "avgResults";
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

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

}
