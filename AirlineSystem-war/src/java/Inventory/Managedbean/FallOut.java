/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import java.io.Serializable;


/**
 *
 * @author YiQuan
 */

public class FallOut implements Serializable {
 
    private String serviceClass;
    private String rate;
    
    public FallOut(String serviceClass, String rate){
        this.serviceClass= serviceClass;
        this.rate = rate;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    
    
}
