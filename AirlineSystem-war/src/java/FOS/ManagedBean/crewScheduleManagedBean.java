/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import FOS.Session.PairingSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "crewScheduleManagedBean")
@ManagedBean
@SessionScoped
public class crewScheduleManagedBean {
    @EJB
    private PairingSessionBeanLocal pairingSessionBean;

    
    /**
     * Creates a new instance of crewScheduleManagedBean
     */
    public crewScheduleManagedBean() {
    }
    
    public void crewPairing(ActionEvent event){
        pairingSessionBean.legMain();
    }
    
}
