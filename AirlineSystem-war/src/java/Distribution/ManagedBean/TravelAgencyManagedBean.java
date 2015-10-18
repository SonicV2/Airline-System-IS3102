/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.ManagedBean;

import Distribution.Entity.TravelAgency;
import Distribution.Session.TravelAgencySessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author parthasarthygupta
 */
@Named(value = "travelAgencyManagedBean")
@ManagedBean
@SessionScoped
public class TravelAgencyManagedBean {
    
     @EJB
    private TravelAgencySessionBeanLocal travelAgencySessionBeanLocal;
    
    private List<TravelAgency> travelAgencies;
    private TravelAgency selectedAgency;
     
     @PostConstruct
     public void retrieve(){
         
     }

    /**
     * @return the travelAgencies
     */
    public List<TravelAgency> getTravelAgencies() {
        return travelAgencies;
    }

    /**
     * @param travelAgencies the travelAgencies to set
     */
    public void setTravelAgencies(List<TravelAgency> travelAgencies) {
        this.travelAgencies = travelAgencies;
    }

    /**
     * @return the selectedAgency
     */
    public TravelAgency getSelectedAgency() {
        return selectedAgency;
    }

    /**
     * @param selectedAgency the selectedAgency to set
     */
    public void setSelectedAgency(TravelAgency selectedAgency) {
        this.selectedAgency = selectedAgency;
    }
    
}
