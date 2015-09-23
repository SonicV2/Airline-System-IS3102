/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import FOS.Entity.Team;
import FOS.Session.SearchCrewSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
@Named(value = "searchCrewManagedBean")
@ManagedBean
@SessionScoped
public class SearchCrewManagedBean {

    @EJB
    private SearchCrewSessionBeanLocal searchCrewSessionBean;

    private List<Pilot> pilots;
    private List<CabinCrew> ccs;
    private List<Team> teams;
    
    private List<String> ccPosition;
    private List<String> languages;
    private List<String> pilotPosition;
    private List<String> pilotSkillsets;

    
    private Team selectTeam;
    private List<Pilot> teamPilots;
    private List<CabinCrew> teamCC;
    
    /**
     * Creates a new instance of SearchCrewManagedBean
     */
    public SearchCrewManagedBean() {
    }

    @PostConstruct
    public void getAllCrews() {
        ccPosition = new ArrayList<String>();
        languages = new ArrayList<String>();
        pilotPosition = new ArrayList<String>();
        pilotSkillsets = new ArrayList<String>();
        
         pilots= new ArrayList<Pilot>();
        setPilots(searchCrewSessionBean.getAllPilots());
        setCcs(searchCrewSessionBean.getAllCCs());
        setTeams(searchCrewSessionBean.getAllTeam());
        
        ccPosition.add("Lead Flight Stewardess");
        ccPosition.add("Flight Stewardess");
        ccPosition.add("Flight Steward");

        pilotPosition.add("Captain");
        pilotPosition.add("First Officer");
        pilotPosition.add("Observer");

        languages.add("Chinese");
        languages.add("Japanese");
        languages.add("Korean");
        languages.add("French");
        languages.add("Italian");

        pilotSkillsets.add("A380");
        pilotSkillsets.add("A330");
        pilotSkillsets.add("B777");
    }

    /**
     * @return the pilots
     */
    public List<Pilot> getPilots() {
        return pilots;
    }

    /**
     * @param pilots the pilots to set
     */
    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    /**
     * @return the ccs
     */
    public List<CabinCrew> getCcs() {
        return ccs;
    }

    /**
     * @param ccs the ccs to set
     */
    public void setCcs(List<CabinCrew> ccs) {
        this.ccs = ccs;
    }

    /**
     * @return the ccPosition
     */
    public List<String> getCcPosition() {
        return ccPosition;
    }

    /**
     * @param ccPosition the ccPosition to set
     */
    public void setCcPosition(List<String> ccPosition) {
        this.ccPosition = ccPosition;
    }

    /**
     * @return the languages
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * @param languages the languages to set
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     * @return the pilotPosition
     */
    public List<String> getPilotPosition() {
        return pilotPosition;
    }

    /**
     * @param pilotPosition the pilotPosition to set
     */
    public void setPilotPosition(List<String> pilotPosition) {
        this.pilotPosition = pilotPosition;
    }

    /**
     * @return the pilotSkillsets
     */
    public List<String> getPilotSkillsets() {
        return pilotSkillsets;
    }

    /**
     * @param pilotSkillsets the pilotSkillsets to set
     */
    public void setPilotSkillsets(List<String> pilotSkillsets) {
        this.pilotSkillsets = pilotSkillsets;
    }

    /**
     * @return the teams
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * @param teams the teams to set
     */
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * @return the selectTeam
     */
    public Team getSelectTeam() {
        return selectTeam;
    }

    /**
     * @param selectTeam the selectTeam to set
     */
    public void setSelectTeam(Team selectTeam) {
        this.selectTeam = selectTeam;
    }

}
