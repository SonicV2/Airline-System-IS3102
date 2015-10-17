/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Managedbean;

import APS.Session.DemandForecastSessionBeanLocal;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yanlong
 */
@Named(value = "demandForecastManageBean")
@ManagedBean
@SessionScoped
public class DemandForecastManagedBean {
    @EJB
    private DemandForecastSessionBeanLocal demandForecastSessionBean;
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    
    
}
