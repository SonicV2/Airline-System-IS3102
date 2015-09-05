/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import javax.ejb.Stateless;
import APS.Entity.AircraftType;
import APS.Entity.Aircraft;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author Yunna
 */
@Stateless
public class APSFleetManagementSystem implements APSFleetManagementSystemLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    private List<AircraftType> aircraftTypeList = new ArrayList<AircraftType>();
    private Aircraft aircraft;
    private AircraftType aircraftType;
    
    public void acquireAircraft(String tailNo, Date datePurchased, Date lastMaintained, String aircraftTypeId){
        
        aircraft = new Aircraft();
        aircraft.createAircraft(tailNo, datePurchased, lastMaintained);
        aircraftType = getAircraftType(aircraftTypeId);
        aircraft.setAircraftType(aircraftType);
        
    }

    public void retireAircraft(String tailNo){
        
        aircraft = getAircraft(tailNo);
        em.remove(aircraft);
        
    }
    
    public List<AircraftType> getAircraftTypeList (String filter) {
        
        List<AircraftType> aircraftTypeList = new ArrayList();
        
        return aircraftTypeList;
    }
    
    // get aircraftType object when searching with aircraftTypeId
    public AircraftType getAircraftType(String aircraftTypeId){
        
        AircraftType type = new AircraftType();
        try {

            Query q = em.createQuery("SELECT a FROM AircraftType " + "AS a WHERE a.id=:id");
            q.setParameter("id", aircraftTypeId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                type = (AircraftType) results.get(0);

            } else {
                type = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return type;
    }
    
    // get aircraft object when searching with tail num
    public Aircraft getAircraft(String tailNum){
        
        Aircraft aircraft1 = new Aircraft();
        
        try {

            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.tailNo=:tailNo");
            q.setParameter("tailNo", tailNum);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                aircraft1 = (Aircraft) results.get(0);

            } else {
                aircraft1 = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return aircraft1;
    }
}