/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import APS.Entity.Location;
import Inventory.Session.PricingManagementLocal;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 * @author YiQuan
 */
@Named(value = "turnOutManageBean")
@ManagedBean
@SessionScoped
public class turnOutManageBean {

    /**
     * Creates a new instance of seasonManageBean
     */
    @EJB
    private PricingManagementLocal pm;
    private List<FallOut> fList;

    public List<FallOut> getfList() {
        return fList;
    }

    public void setfList(List<FallOut> fList) {
        this.fList = fList;
    }

    
    
    public String displayRates(String flightNo){
        fList= new ArrayList();
       FallOut fallout = new FallOut("Economy Saver", pm.calTurnOut(flightNo,"Economy Saver"));
       fList.add(fallout);
       FallOut fallout1 = new FallOut("Economy Basic", pm.calTurnOut(flightNo,"Economy Basic"));
       fList.add(fallout1);
       FallOut fallout2 = new FallOut("Economy Premium", pm.calTurnOut(flightNo,"Economy Premium"));
       fList.add(fallout2);
       FallOut fallout3 = new FallOut("Business", pm.calTurnOut(flightNo,"Business"));
       fList.add(fallout3);
       FallOut fallout4 = new FallOut("First Class", pm.calTurnOut(flightNo,"First Class"));
       fList.add(fallout4);
        
       return "displayTurnOut";
    }   

}
