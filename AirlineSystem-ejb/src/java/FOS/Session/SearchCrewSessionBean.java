/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import FOS.Entity.Team;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class SearchCrewSessionBean implements SearchCrewSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Pilot> getAllPilots() {
        Query q = em.createQuery("SELECT p FROM Employee p WHERE p.employeeRole=:role");
        q.setParameter("role", "Pilot");
        List<Pilot> pilots = q.getResultList();
        List<Pilot> results = new ArrayList<Pilot>();
        for(Pilot pp: pilots){
            if(pp.getEmployeeRole().equals("Pilot") ){
                results.add(pp);
            }
        }
        return results;
    }
    
    @Override
    public List<CabinCrew> getAllCCs (){
        Query q = em.createQuery("SELECT c FROM Employee c WHERE c.employeeRole=:role");
          q.setParameter("role", "Cabin Crew");
        List<CabinCrew> ccs = q.getResultList();
        
        List<CabinCrew> results = new ArrayList<CabinCrew>();
        for(CabinCrew c: ccs){
            if(c.getEmployeeRole().equals("Cabin Crew")){
                results.add(c);
            }
        }
        return results;
    }

    @Override
    public List<Team> getAllTeam() {
        Query q = em.createQuery("SELECT t FROM Team t");
        List<Team> teams = q.getResultList();
        return teams;
    }
    
    @Override
    public List<CabinCrew> getLeaveCabinCrew(){
        Query q = em.createQuery("SELECT c FROM CabinCrew c");
        List<CabinCrew> results = new ArrayList<CabinCrew>();
        List<CabinCrew> crews = q.getResultList();
        for(CabinCrew c : crews){
            if(c.getStatus().contains("Leave")){
                results.add(c);
            }
        }
        return results;
    }
    
    @Override
    public List<Pilot> getLeavePilot(){
        Query q = em.createQuery("SELECT p FROM Pilot p");
        List<Pilot> results = new ArrayList<Pilot>();
        List<Pilot> crews = q.getResultList();
        for(Pilot c : crews){
            if(c.getStatus().contains("Leave")){
                results.add(c);
            }
        }
        return results;
    }
}
