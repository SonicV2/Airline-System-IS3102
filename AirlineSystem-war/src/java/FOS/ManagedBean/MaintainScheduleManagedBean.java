/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import FOS.Entity.MaintainSchedule;
import FOS.Session.MaintainSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "maintainScheduleManagedBean")
@RequestScoped
@ManagedBean
public class MaintainScheduleManagedBean {

    @EJB
    private MaintainSessionBeanLocal maintainSessionBean;

    private List<MaintainSchedule> maintainSchedules;

    /**
     * Creates a new instance of MaintainScheduleManagedBean
     */
    public MaintainScheduleManagedBean() {
    }

    public void generateMaintainSchedules(ActionEvent event) {
        maintainSessionBean.generateAllSchedules();
        maintainSessionBean.assignTeam();
        getAllMaintainSchedules();
    }

    public void getAllMaintainSchedules() {
        List<MaintainSchedule> temps = maintainSessionBean.getAllMaintainSchedules();
        System.out.println("TEMPS: "+ temps.size());
        setMaintainSchedules(temps);    

    }

    /**
     * @return the maintainSessionBean
     */
    public MaintainSessionBeanLocal getMaintainSessionBean() {
        return maintainSessionBean;
    }

    /**
     * @param maintainSessionBean the maintainSessionBean to set
     */
    public void setMaintainSessionBean(MaintainSessionBeanLocal maintainSessionBean) {
        this.maintainSessionBean = maintainSessionBean;
    }

    /**
     * @return the maintainSchedules
     */
    public List<MaintainSchedule> getMaintainSchedules() {
        return maintainSchedules;
    }

    /**
     * @param maintainSchedules the maintainSchedules to set
     */
    public void setMaintainSchedules(List<MaintainSchedule> maintainSchedules) {
        this.maintainSchedules = maintainSchedules;
    }

 

   
}
