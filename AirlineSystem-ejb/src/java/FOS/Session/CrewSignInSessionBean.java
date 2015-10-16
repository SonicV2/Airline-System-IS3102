/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Flight;
import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import FOS.Entity.Pairing;
import FOS.Entity.Team;
import java.text.SimpleDateFormat;
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
public class CrewSignInSessionBean implements CrewSignInSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    // Given crewName to find the team the crew assigned
    @Override
    public Team getCCTeam(String crewName) {
        Query q = em.createQuery("SELECT t FROM Team t");
        List<Team> teams = q.getResultList();

        for (Team t : teams) {
            List<CabinCrew> ccList = t.getCabinCrews();
            List<Pilot> pilotList = t.getPilots();
            for (CabinCrew c : ccList) {
                if (c.getEmployeeUserName().equals(crewName)) {
                    return t;
                }
            }

            for (Pilot p : pilotList) {
                if (p.getEmployeeUserName().equals(crewName)) {
                    return t;
                }
            }
        }
        return null;
    }

    // Change the crew status to sign in 
    @Override
    public String crewSignIn(String crewName, Team team, String firstScheduleID) {

        String msg = "";

        Query q = em.createQuery("SELECT e FROM Employee e");
        List<Employee> employees = q.getResultList();

        for (Employee e : employees) {
            if (e.getEmployeeUserName().equals(crewName)) {
                if (e.getEmployeeRole().equals("Cabin Crew")) {
                    CabinCrew c = (CabinCrew) e;
                    if (c.getSignInStatus().contains("SignIn")) {
                        msg = "Signed";
                    } else {
                        c.setSignInStatus("SignIn" + firstScheduleID);
                        em.persist(c);
                        em.flush();
                    }

                }
                if (e.getEmployeeRole().equals("Pilot")) {
                    Pilot p = (Pilot) e;
                    if (p.getSignInStatus().contains("SignIn")) {
                        msg = "Signed";
                    } else {
                        p.setSignInStatus("SignIn" + firstScheduleID);
                        em.persist(p);
                        em.flush();
                    }

                }
            }
        }
        changeTeamStatus(team, firstScheduleID);
        return msg;

    }

    // If all crews sign in, then the team status will change to all sigin in
    public void changeTeamStatus(Team team, String firstScheduleID) {

        //em.merge(team);
        int cCount = 1;
        int pCount = 1;
        List<CabinCrew> ccs = team.getCabinCrews();
        List<Pilot> pilots = team.getPilots();

        for (CabinCrew c : ccs) {
            if (c.getSignInStatus().equals("SignIn" + firstScheduleID)) {
                cCount++;
            }
        }

        for (Pilot p : pilots) {
            if (p.getSignInStatus().equals("SignIn" + firstScheduleID)) {
                pCount++;
            }
        }
        if (cCount >= team.getcCrewNo() && pCount >= team.getPilotNo()) {  //not very logical but for the system's sake 
            team.setStatus("AllSignIn" + firstScheduleID);
            em.merge(team);
        }
        em.flush();
    }

    @Override
    public String submitLeave(String crewName, String reason, String scheduleId) {
        CabinCrew crew = getCabinCrew(crewName);
        if (crew.getStatus().split("-")[0].equals("Leave: " + scheduleId)) {
            return "done";
        } else {
            crew.setStatus("Leave: " + scheduleId+ "-"+reason);
            return "good";
        }
    }
    
    @Override
    public String submitPilotLeave(String crewName, String reason, String scheduleId) {
        Pilot crew = getPilot(crewName);
        if (crew.getStatus().split("-")[0].equals("Leave: " + scheduleId)) {
            return "done";
        } else {
            crew.setStatus("Leave: " + scheduleId+ "-"+reason);
            return "good";
        }
    }

    @Override
    public Schedule getFirstPairingSchedule(Pairing p) {

        String flightDate = p.getFDate();

        List<String> flightNumbers = p.getFlightNumbers();
        String firstFlightNo = flightNumbers.get(0);
        Flight f = getFlight(firstFlightNo);

        List<Schedule> schedules = f.getSchedule();

        for (Schedule s : schedules) {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(s.getStartDate());
            if (formattedDate.equals(flightDate)) {
                return s;
            }
        }
        return null;
    }

    public Flight getFlight(String flightNumber) {
        Query q = em.createQuery("SELECT f FROM Flight f");
        List<Flight> flights = q.getResultList();

        for (Flight f : flights) {

            if (f.getFlightNo().equals("MA" + flightNumber)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public CabinCrew getCabinCrew(String crewName) {
        Query q = em.createQuery("SELECT c FROM CabinCrew c WHERE c.employeeUserName =:crewName");
        q.setParameter("crewName", crewName);
        List<CabinCrew> results = q.getResultList();
        if(results.isEmpty()){
            return null;
        }
        return results.get(0);
    }
    
    @Override
    public Pilot getPilot(String crewName) {
        Query q = em.createQuery("SELECT p FROM Pilot p WHERE  p.employeeUserName =:crewName");
        q.setParameter("crewName", crewName);
        List<Pilot> results = q.getResultList();
        if(results.isEmpty()){
            return null;
        }
        return results.get(0);
    }

    @Override
    public Schedule getScheduleByID(String scheduleId){
        Query q = em.createQuery("SELECT s FROM Schedule s");
        List<Schedule> schedules = q.getResultList();
        for(Schedule s:schedules){
            if(s.getScheduleId().toString().equals(scheduleId)){
                return s;
            }
        }
        return null;
    }
    
    @Override
    public Schedule getScheduleBy(String flightNumber, String flightDate){
        String fNumber="";
        if(flightNumber.contains("MA")){
            fNumber=flightNumber.substring(2);
        }else{
            fNumber =flightNumber;
        }
        Flight flight = getFlight(fNumber);

        List<Schedule> schedules = flight.getSchedule();
        for(Schedule s : schedules){
            String sDate = new SimpleDateFormat("dd/MM/yyyy").format(s.getStartDate());
            if(sDate.equals(flightDate)){
                return s;
            }
        }
        return null;
    }
    
    @Override
    public Team getTeamByID(String teamId){
        Query q = em.createQuery("SELECT t FROM Team t");
        List<Team> teams = q.getResultList();
        for(Team t : teams){
            if(t.getId().equals(teamId)){
                return t;
            }
        }
        return null;
    }
}
