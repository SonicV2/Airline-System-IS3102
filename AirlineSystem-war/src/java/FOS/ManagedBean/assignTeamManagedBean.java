/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import FOS.Session.CrewAssignSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "assignTeamManagedBean")
@Dependent
public class assignTeamManagedBean {
    @EJB
    private CrewAssignSessionBeanLocal crewAssignSessionBean;
    
    private List<Pilot> unAssignCaptain;  
    private List<Pilot> unAssignFO;
    private List<CabinCrew> unAssignLeadStewardess;
    private List<CabinCrew> unAssignStewardess;
    private List<CabinCrew> unAssignSteward;
    
    private List<Employee> selectedCC;
    
    public assignTeamManagedBean() {
    }

    @PostConstruct
    public void retrieveUnassign(){
        setUnAssignCaptain(crewAssignSessionBean.getUnassignCaptains());
        setUnAssignFO(crewAssignSessionBean.getUnassignFOs());
        setUnAssignLeadStewardess(crewAssignSessionBean.getUnassignLSs());
        setUnAssignStewardess(crewAssignSessionBean.getUnassignFSs());
        setUnAssignSteward(crewAssignSessionBean.getUnassignSs());
        
    }

    /**
     * @return the crewAssignSessionBean
     */
    public CrewAssignSessionBeanLocal getCrewAssignSessionBean() {
        return crewAssignSessionBean;
    }

    /**
     * @param crewAssignSessionBean the crewAssignSessionBean to set
     */
    public void setCrewAssignSessionBean(CrewAssignSessionBeanLocal crewAssignSessionBean) {
        this.crewAssignSessionBean = crewAssignSessionBean;
    }

    /**
     * @return the unAssignCaptain
     */
    public List<Pilot> getUnAssignCaptain() {
        return unAssignCaptain;
    }

    /**
     * @param unAssignCaptain the unAssignCaptain to set
     */
    public void setUnAssignCaptain(List<Pilot> unAssignCaptain) {
        this.unAssignCaptain = unAssignCaptain;
    }

    /**
     * @return the unAssignFO
     */
    public List<Pilot> getUnAssignFO() {
        return unAssignFO;
    }

    /**
     * @param unAssignFO the unAssignFO to set
     */
    public void setUnAssignFO(List<Pilot> unAssignFO) {
        this.unAssignFO = unAssignFO;
    }

    /**
     * @return the unAssignLeadStewardess
     */
    public List<CabinCrew> getUnAssignLeadStewardess() {
        return unAssignLeadStewardess;
    }

    /**
     * @param unAssignLeadStewardess the unAssignLeadStewardess to set
     */
    public void setUnAssignLeadStewardess(List<CabinCrew> unAssignLeadStewardess) {
        this.unAssignLeadStewardess = unAssignLeadStewardess;
    }

    /**
     * @return the unAssignStewardess
     */
    public List<CabinCrew> getUnAssignStewardess() {
        return unAssignStewardess;
    }

    /**
     * @param unAssignStewardess the unAssignStewardess to set
     */
    public void setUnAssignStewardess(List<CabinCrew> unAssignStewardess) {
        this.unAssignStewardess = unAssignStewardess;
    }

    /**
     * @return the unAssignSteward
     */
    public List<CabinCrew> getUnAssignSteward() {
        return unAssignSteward;
    }

    /**
     * @param unAssignSteward the unAssignSteward to set
     */
    public void setUnAssignSteward(List<CabinCrew> unAssignSteward) {
        this.unAssignSteward = unAssignSteward;
    }

    /**
     * @return the selectedCC
     */
    public List<Employee> getSelectedCC() {
        return selectedCC;
    }

    /**
     * @param selectedCC the selectedCC to set
     */
    public void setSelectedCC(List<Employee> selectedCC) {
        this.selectedCC = selectedCC;
    }
    
    
}
