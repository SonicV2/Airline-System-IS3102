/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.ManagedBean;

import DCS.Session.BaggageSessionBeanLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "baggageManagedBean")
@SessionScoped
@ManagedBean
public class BaggageManagedBean implements Serializable {

    @EJB
    private BaggageSessionBeanLocal baggageSessionBean;

    private int counter;
    private List<String> baggageWeight;
    private List<String> counters;

    public BaggageManagedBean() {
    }

    @PostConstruct
    public void init() {
        baggageWeight = new ArrayList<String>();
        baggageWeight.add("");
        counters = new ArrayList<String>();
        counters.add("1");
        counters.add("2");
        counters.add("3");
        counters.add("4");
        counters.add("5");
        counters.add("6");
        counters.add("7");
        counters.add("8");
        counters.add("9");
        counters.add("10");
        counter = 0;
    }

    //add baggage
    public void extend() {
        baggageWeight.add("");
        counter ++;
    }
    
    
    public void submit(){
        System.out.println("ddddd"+ baggageWeight);
    }

    /**
     * @return the baggageWeight
     */
    public List<String> getBaggageWeight() {
        return baggageWeight;
    }

    /**
     * @param baggageWeight the baggageWeight to set
     */
    public void setBaggageWeight(List<String> baggageWeight) {
        this.baggageWeight = baggageWeight;
    }

    /**
     * @return the counters
     */
    public List<String> getCounters() {
        return counters;
    }

    /**
     * @param counters the counters to set
     */
    public void setCounters(List<String> counters) {
        this.counters = counters;
    }

    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

}
