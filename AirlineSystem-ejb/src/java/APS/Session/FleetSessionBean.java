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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
    public void acquireAircraft(String tailNo, Date datePurchased, Date lastMaintained, String aircraftTypeId, String hub, String status) {
        aircraft = new Aircraft();
        aircraftType = new AircraftType();
        aircraftType = getAircraftType(aircraftTypeId);
        aircraft.setAircraftType(aircraftType);
        aircraft.createAircraft(tailNo, datePurchased, lastMaintained, hub, status);
        aircraftType.getAircrafts().add(aircraft);
        em.persist(aircraft);
    }

    @Override
    public void retireAircraft(String retireNo, String takeoverNo) {

        Aircraft retire = getAircraft(retireNo);
        Aircraft takeover = getAircraft(takeoverNo);
        
        //Move the schedules over to the takeover aircraft
        for (int i = 0; i < retire.getSchedules().size(); i++) {
            retire.getSchedules().get(i).setAircraft(takeover);
            em.merge(retire.getSchedules().get(i));
        }
        
        if(retire.getSchedules().isEmpty()){
            System.out.println("LOLOLOL");
        }
        
        takeover.setSchedules(retire.getSchedules());
        retire.setSchedules(null);
        em.merge(takeover);

        //Remove the aircraft from the list in aircraft type
        List<Aircraft> temp1 = retire.getAircraftType().getAircrafts();
        temp1.remove(retire);

        retire.getAircraftType().setAircrafts(temp1);
        retire.setAircraftType(null);
        em.remove(retire);
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
    public Aircraft getAircraft(String tailNo) {

        Aircraft aircraft1 = new Aircraft();

        try {

            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.tailNo=:tailNo");
            q.setParameter("tailNo", tailNo);

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

    //retireve all the existing aircraft types
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

    //retrieve all the aircrafts
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

    //get all the reserve aircrafts
    @Override
    public List<Aircraft> getReserveAircrafts(String status) {

        List<Aircraft> reserveAircrafts = new ArrayList<Aircraft>();

        try {
            Query q = em.createQuery("SELECT a FROM Aircraft " + "AS a WHERE a.status=:status");
            q.setParameter("status", status);

            List<Aircraft> results = q.getResultList();
            if (!results.isEmpty()) {

                reserveAircrafts = results;

            } else {
                reserveAircrafts = null;
                System.out.println("no reserve aircraft!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return reserveAircrafts;
    }
}
