/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import CI.Entity.CabinCrew;
import CI.Entity.Pilot;
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
public class CrewAssignSessionBean implements CrewAssignSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Pilot> getUnassignCaptains (){
        Query q = em.createQuery("SELECT p FROM Employee p");
        List<Pilot> pilots = q.getResultList();
        List<Pilot> results = new ArrayList<Pilot>();
        for(Pilot p: pilots){
            if(p.getEmployeeRole().equals("Pilot") && p.isAssigned()==false && p.getPosition().equals("Captain")){
                results.add(p);
            }
        }
        return results;
    }
    
     @Override
    public List<Pilot> getUnassignFOs (){
        Query q = em.createQuery("SELECT p FROM Employee p");
        List<Pilot> pilots = q.getResultList();
        List<Pilot> results = new ArrayList<Pilot>();
        for(Pilot p: pilots){
            if(p.getEmployeeRole().equals("Pilot") && p.isAssigned()==false && p.getPosition().equals("First Officer")){
                results.add(p);
            }
        }
        return results;
    }
    
    @Override
     public List<CabinCrew> getUnassignLSs (){
        Query q = em.createQuery("SELECT c FROM Employee c");
        List<CabinCrew> ccs = q.getResultList();
        List<CabinCrew> results = new ArrayList<CabinCrew>();
        for(CabinCrew cc : ccs){
            if(cc.getEmployeeRole().equals("Cabin Crew")&& cc.isAssigned()==false && cc.getPosition().equals("Lead Flight Stewardess")){
                results.add(cc);
            }
        }
//        System.out.println("---------SessionBean: " + results.size());
        return results;
    }
    
     @Override
     public List<CabinCrew> getUnassignFSs (){
        Query q = em.createQuery("SELECT c FROM Employee c");
        List<CabinCrew> ccs = q.getResultList();
        List<CabinCrew> results = new ArrayList<CabinCrew>();
        for(CabinCrew cc : ccs){
            if(cc.getEmployeeRole().equals("Cabin Crew")&& cc.isAssigned()==false && cc.getPosition().equals("Flight Stewardess")){
                results.add(cc);
            }
        }
//        System.out.println("---------SessionBean: " + results.size());
        return results;
    }
   
      @Override
     public List<CabinCrew> getUnassignSs (){
        Query q = em.createQuery("SELECT c FROM Employee c");
        List<CabinCrew> ccs = q.getResultList();
        List<CabinCrew> results = new ArrayList<CabinCrew>();
        for(CabinCrew cc : ccs){
            if(cc.getEmployeeRole().equals("Cabin Crew")&& cc.isAssigned()==false && cc.getPosition().equals("Flight Steward")){
                results.add(cc);
            }
        }
//        System.out.println("---------SessionBean: " + results.size());
        return results;
    }
}
