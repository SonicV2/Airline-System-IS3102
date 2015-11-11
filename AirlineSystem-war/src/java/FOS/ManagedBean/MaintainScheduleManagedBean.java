/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import APS.Entity.Aircraft;
import APS.Entity.Schedule;
import FOS.Entity.GroundCrew;
import FOS.Entity.MaintainSchedule;
import FOS.Entity.MaintainanceTeam;
import FOS.Session.MaintainSessionBeanLocal;
import FOS.Session.MaintenanceChecklistSessionBeanLocal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.extensions.model.timeline.TimelineGroup;

/**
 *
 * @author smu
 */
@Named(value = "maintainScheduleManagedBean")
@ViewScoped
@ManagedBean
public class MaintainScheduleManagedBean {

    @EJB
    private MaintenanceChecklistSessionBeanLocal maintenanceChecklistSessionBean;

    @EJB
    private MaintainSessionBeanLocal maintainSessionBean;

    private List<MaintainSchedule> maintainSchedules;

    private List<MaintainanceTeam> allAssignedMaintainTeams;

    private TimelineModel model;
    private Locale locale; // current locale as String, java.util.Locale is possible too.  
    private Date start;
    private Date end;
    private String fModel;
    private MaintainanceTeam selectedTeam; // from viewMaintenanceTeams
    private List<GroundCrew> selectedTeamCrews;
    private List<MaintainSchedule> selectedTeamMaintainSchedules;
    private GroundCrew gcLeader;

    private List<MaintainanceTeam> teams;

    /**
     * Creates a new instance of MaintainScheduleManagedBean
     */
    public MaintainScheduleManagedBean() {
    }

    @PostConstruct
    public void generateMaintainSchedules() {
        maintainSessionBean.generateAllSchedules();
        //  maintainSessionBean.assignTeam();
        getAllMaintainSchedules();
        timeLineDisplay();
        maintenanceChecklistSessionBean.assignChecklist();
    }

    public void getAllMaintainSchedules() {
        List<MaintainSchedule> temps = maintainSessionBean.getAllMaintainSchedules();
        setMaintainSchedules(temps);

    }

    public void timeLineDisplay() {
        model = new TimelineModel();

        for (MaintainSchedule m : maintainSchedules) {
            String tailNo = m.getTailNumber() + "";

            Aircraft air = maintainSessionBean.getAircraftByTailNo(tailNo);
            String aircraftType = air.getAircraftType().getId();
            tailNo += "(" + aircraftType + ")";
            TimelineEvent event = new TimelineEvent("TYPE " + m.getType(), m.getMainStartDate(), m.getMainEndDate(), true, tailNo, ("TYPE" + m.getType()).toLowerCase());
            model.add(event);
        }

    }

    public void assignTeam(ActionEvent event) {
        maintainSessionBean.assignTeam();
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfullly assigned schedules to teams!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void generateTeam(ActionEvent event) {
        maintainSessionBean.generateTeam();
        retrieveTeams();
    }

    public void retrieveTeams() {
        setTeams(maintainSessionBean.retrieveTeamId()); //retrieve maintainance team entity from db
    }

    public void assignCrew(ActionEvent event) {
        maintainSessionBean.assignMainCrews();
        FacesMessage message = null;
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfullly assigned maintenance crews to teams!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void getAllAssignedMaintainTeams(ActionEvent event) {
        setAllAssignedMaintainTeams(maintainSessionBean.getUnassignedMaintainTeams());
        
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

    /**
     * @return the model
     */
    public TimelineModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(TimelineModel model) {
        this.model = model;
    }

    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @return the fModel
     */
    public String getfModel() {
        return fModel;
    }

    /**
     * @param fModel the fModel to set
     */
    public void setfModel(String fModel) {
        this.fModel = fModel;
    }

    /**
     * @return the allAssignedMaintainTeams
     */
    public List<MaintainanceTeam> getAllAssignedMaintainTeams() {
        return allAssignedMaintainTeams;
    }

    /**
     * @param allAssignedMaintainTeams the allAssignedMaintainTeams to set
     */
    public void setAllAssignedMaintainTeams(List<MaintainanceTeam> allAssignedMaintainTeams) {
        this.allAssignedMaintainTeams = allAssignedMaintainTeams;
    }

    /**
     * @return the selectedTeam
     */
    public MaintainanceTeam getSelectedTeam() {
        return selectedTeam;
    }

    public void getLeader() {
        this.gcLeader = selectedTeamCrews.get(0);
        for (int i = 0; i < selectedTeamCrews.size(); i++) {
            for (int j = 1; j < selectedTeamCrews.size(); j++) {

                int a1 = Integer.parseInt(gcLeader.getExperience());
                int a2 = Integer.parseInt(selectedTeamCrews.get(j).getExperience());

                if (a2 > a1) {
                    this.gcLeader = selectedTeamCrews.get(j);
                }

            }
        }
    }

    /**
     * @param selectedTeam the selectedTeam to set
     */
    public void setSelectedTeam(MaintainanceTeam selectedTeam) {
        this.selectedTeam = selectedTeam;

        setSelectedTeamCrews(selectedTeam.getGroundCrews());

        setSelectedTeamMaintainSchedules(selectedTeam.getmSchedules());
        getLeader();
    }

    /**
     * @return the selectedTeamCrews
     */
    public List<GroundCrew> getSelectedTeamCrews() {
        return selectedTeamCrews;
    }

    /**
     * @param selectedTeamCrews the selectedTeamCrews to set
     */
    public void setSelectedTeamCrews(List<GroundCrew> selectedTeamCrews) {
        this.selectedTeamCrews = selectedTeamCrews;
    }

    /**
     * @return the selectedTeamMaintainSchedules
     */
    public List<MaintainSchedule> getSelectedTeamMaintainSchedules() {
        return selectedTeamMaintainSchedules;
    }

    /**
     * @param selectedTeamMaintainSchedules the selectedTeamMaintainSchedules to
     * set
     */
    public void setSelectedTeamMaintainSchedules(List<MaintainSchedule> selectedTeamMaintainSchedules) {
        this.selectedTeamMaintainSchedules = selectedTeamMaintainSchedules;
    }

    /**
     * @return the gcLeader
     */
    public GroundCrew getGcLeader() {
        return gcLeader;
    }

    /**
     * @param gcLeader the gcLeader to set
     */
    public void setGcLeader(GroundCrew gcLeader) {
        this.gcLeader = gcLeader;
    }

    /**
     * @return the teams
     */
    public List<MaintainanceTeam> getTeams() {
        return teams;
    }

    /**
     * @param teams the teams to set
     */
    public void setTeams(List<MaintainanceTeam> teams) {
        this.teams = teams;
    }

}
