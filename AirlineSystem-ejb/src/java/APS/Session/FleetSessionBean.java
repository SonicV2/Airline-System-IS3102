package APS.Session;

import javax.ejb.Stateless;
import APS.Entity.AircraftType;
import APS.Entity.Aircraft;
import APS.Entity.Flight;
import APS.Entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import javax.ejb.Schedules;

/**
 *
 * @author Yunna
 */
@Stateless
public class FleetSessionBean implements FleetSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private List<AircraftType> aircraftTypeList = new ArrayList<AircraftType>();
    private Aircraft aircraft;
    private AircraftType aircraftType;

    @Override
    public void acquireAircraft(Date datePurchased, Date lastMaintained, String aircraftTypeId, String hub, String status) {
        aircraft = new Aircraft();
        aircraftType = new AircraftType();
        aircraftType = getAircraftType(aircraftTypeId);
        aircraft.setAircraftType(aircraftType);
        aircraft.createAircraft(datePurchased, lastMaintained, hub, status);
        aircraftType.getAircrafts().add(aircraft);
        em.persist(aircraft);
    }

    @Override
    public void retireAircraft(Long tailNo) {

        aircraft = getAircraft(tailNo);
        em.remove(aircraft);

    }
    
    @Override
    public void persistSchedule(Schedule schedule){
        em.merge(schedule);
    }
    
    @Override
    public void persistAircraft(Aircraft aircraft){
        em.merge(aircraft);
    }

    // get aircraftType object when searching with aircraftTypeId
    @Override
    public AircraftType getAircraftType(String aircraftTypeId) {

        AircraftType type1 = new AircraftType();
        try {

            Query q = em.createQuery("SELECT a FROM AircraftType " + "AS a WHERE a.id=:id");
            q.setParameter("id", aircraftTypeId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                type1 = (AircraftType) results.get(0);

            } else {
                type1 = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return type1;
    }

    // get aircraft object when searching with tail num
    @Override
    public Aircraft getAircraft(Long tailNum) {

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
    
    @Override
    public List<AircraftType> retrieveAircraftTypes() {
        List<AircraftType> allTypes = new ArrayList<AircraftType>();

        try {
            Query q = em.createQuery("SELECT a from AircraftType a");

            List<AircraftType> results = q.getResultList();
            if (!results.isEmpty()) {

                allTypes = results;

            } else {
                allTypes = null;
                System.out.println("no aircraft type!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allTypes;
    }
    
    @Override
    public List<Aircraft> retrieveAircrafts() {
        List<Aircraft> allAircrafts = new ArrayList<Aircraft>();

        try {
            Query q = em.createQuery("SELECT a from Aircraft a");

            List<Aircraft> results = q.getResultList();
            if (!results.isEmpty()) {

                allAircrafts = results;

            } else {
                allAircrafts = null;
                System.out.println("no aircraft!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allAircrafts;
    }
    
    @Override
    public List<Aircraft> getReserveAircrafts(String status){
        
        List<Aircraft> reserveAircrafts = new ArrayList<Aircraft>();
        
        try{
            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.status=:status");
            q.setParameter("status", status);
            
            List<Aircraft> results = q.getResultList();
            if (!results.isEmpty()){
                
                reserveAircrafts = results;
                
            }else
            {
                reserveAircrafts = null;
                System.out.println("no reserve aircraft!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
       
        return reserveAircrafts;
    }
}
