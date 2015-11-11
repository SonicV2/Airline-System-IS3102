/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import APS.Entity.Flight;
import APS.Entity.Route;
import Inventory.Session.PricingSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

/**
 *
 * @author YiQuan
 */
@Named(value = "turnOutManageBean")
@ManagedBean
@SessionScoped
public class TurnOutManagedBean {

    /**
     * Creates a new instance of seasonManageBean
     */
    @EJB
    private PricingSessionBeanLocal pm;
    private List<FallOut> fList;
    private Flight flight;
    private Route route;

    public List<FallOut> getfList() {
        return fList;
    }

    public void setfList(List<FallOut> fList) {
        this.fList = fList;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    
    public String next(Flight f){
    this.flight = f;
    this.route = f.getRoute();
    displayRates(f.getFlightNo());
    return "DisplayTurnOut";
    }
    
    public void displayRates(String flightNo){
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
        
       
    }   

}
