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

    @Override
    public String crewSignIn(String crewName, Team team) {

        String msg = "";
        System.out.println("++++++UserName session bean: " + crewName);

        Query q = em.createQuery("SELECT e FROM Employee e");
        List<Employee> employees = q.getResultList();

        for (Employee e : employees) {
            if (e.getEmployeeUserName().equals(crewName)) {
                if (e.getEmployeeRole().equals("Cabin Crew")) {
                    CabinCrew c = (CabinCrew) e;
                    if (c.getStatus().equals("SignIn")) {
                        msg = "Signed";
                    } else {
                        c.setStatus("SignIn");
                        System.out.println("++++++UserName c: " + c.getEmployeeUserName());
                        em.persist(c);
                        em.flush();
                    }

                }
                if (e.getEmployeeRole().equals("Pilot")) {
                    Pilot p = (Pilot) e;
                    if (p.getStatus().equals("SignIn")) {
                        msg = "Signed";
                    } else {
                        p.setStatus("SignIn");
                        em.persist(p);
                        em.flush();
                    }

                }
            }
        }
        System.out.println("Team ID: " + team.getId());
        changeTeamStatus(team);
        return msg;

    }

    public void changeTeamStatus(Team team) {

        //em.merge(team);
        int cCount = 1;
        int pCount = 1;
        List<CabinCrew> ccs = team.getCabinCrews();
        List<Pilot> pilots = team.getPilots();

        for (CabinCrew c : ccs) {
            if (c.getStatus().equals("SignIn")) {
                cCount++;

            }
        }

        for (Pilot p : pilots) {

            if (p.getStatus().equals("SignIn")) {
                pCount++;

            }
        }
        if (cCount >= team.getcCrewNo() && pCount >= team.getPilotNo()) {  //not very logical but for the system's sake 
            System.out.println("TEAM ID: " + team.getId());
            team.setStatus("AllSignIn");
            em.merge(team);
        }
        em.flush();
    }
    
    
    @Override
    public Schedule getFirstPairingSchedule(Pairing p){
         System.out.println(">>>>>>>>>" + p.getId());
        String flightDate = p.getFDate();
         System.out.println(">>>>>>>>>" + p.getFDate());
         
        List<String> flightNumbers = p.getFlightNumbers();
        String firstFlightNo = flightNumbers.get(0);
        Flight f = getFlight(firstFlightNo);
        System.out.println(">>>>>>>>>" + firstFlightNo);
       
        List<Schedule> schedules =f.getSchedule();
        
        for(Schedule s : schedules){
             String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(s.getStartDate());
             System.out.println(">>>>>>>>>date" + formattedDate);
             if(formattedDate.equals(flightDate)){
                 return s;
             }
        }
        return null;
    }
    
    public Flight getFlight(String flightNumber){
        Query q = em.createQuery("SELECT f FROM Flight f");
        List<Flight> flights = q.getResultList();
        System.out.println("GetFlight: "+ flightNumber);
        for(Flight f: flights){
            System.out.println("FLIGHT: "+ f.getFlightNo());
            if(f.getFlightNo().equals("MA"+flightNumber)){
                return f;
            }
        }
        return null;
    }

}
