/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Aircraft;
import APS.Entity.Schedule;
import FOS.Entity.GroundCrew;
import FOS.Entity.MaintainSchedule;
import FOS.Entity.MaintainanceTeam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
public class MaintainSessionBean implements MaintainSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    final long hoursInMillis = 60L * 60L * 1000L;

    Comparator<Schedule> comparator = new Comparator<Schedule>() {
        @Override
        public int compare(Schedule o1, Schedule o2) {
            int result = o1.getStartDate().compareTo(o2.getStartDate());
            if (result == 0) {
                return o1.getStartDate().before(o2.getStartDate()) ? -1 : 1;
            } else {
                return result;
            }
        }
    };

    @Override
    public void generateAllSchedules() {

        Query q = em.createQuery("SELECT a FROM Aircraft a");
        Query q1 = em.createQuery("SELECT m FROM MaintainSchedule m");

        List<Aircraft> aircrafts = q.getResultList();
        List<MaintainSchedule> mSchedules = q1.getResultList();

        boolean isSame = false;

        for (Aircraft a : aircrafts) {
            if (!a.getSchedules().isEmpty()) {
                List<Schedule> schedules = a.getSchedules();
                Collections.sort(schedules, comparator);
                long totalHours = 0;
                int cnt = 0; // 1230/65 = 2 --> 2 cnt
                int cntB = 0;
                for (Schedule s : schedules) {
                    totalHours += calFlightHour(s.getStartDate(), s.getEndDate());

                    if (totalHours / 65 > cnt && s.getFlight().getRoute().getDestinationIATA().equals(a.getHub())) {
                        System.out.println("DDDDDD: " + totalHours);

                        if (cntB == 3) {
                            MaintainSchedule ms = new MaintainSchedule();
                            Date mStartDate = new Date(s.getEndDate().getTime() + (1L * hoursInMillis)); // Adds 1 hours
                            Date mEndDate = new Date(mStartDate.getTime() + (11L * hoursInMillis));

                            for (MaintainSchedule m : mSchedules) {
                                if (m.getMainStartDate().equals(mStartDate) && m.getMainEndDate().equals(mEndDate)) {
                                    isSame = true;
                                }
                            }
                            if (!isSame) {
                                ms.create(mStartDate, mEndDate, a.getTailNo(), "N.A", "B");
                                ms.setmTeam(null);
                                em.persist(ms);
                                cnt++;
                                cntB = 0;
                            }
                        } else {
                            MaintainSchedule ms = new MaintainSchedule();
                            Date mStartDate = new Date(s.getEndDate().getTime() + (1L * hoursInMillis)); // Adds 1 hours
                            Date mEndDate = new Date(mStartDate.getTime() + (5L * hoursInMillis));

                            for (MaintainSchedule m : mSchedules) {
                                if (m.getMainStartDate().equals(mStartDate) && m.getMainEndDate().equals(mEndDate)) {
                                    isSame = true;
                                }
                            }

                            if (!isSame) {
                                ms.create(mStartDate, mEndDate, a.getTailNo(), "N.A", "A");
                                ms.setmTeam(null);
                                em.persist(ms);
                                cnt++;
                                cntB++;
                            }
                        }
                    }
                }

            }

        }

    }

    public long calFlightHour(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        long diffHours = diff / (60 * 60 * 1000) % 24;
        return diffHours;
    }

    @Override
    public List<MaintainSchedule> getAllMaintainSchedules() {
        Query q = em.createQuery("SELECT m FROM MaintainSchedule m");
        List<MaintainSchedule> mSchedules = q.getResultList();
        return mSchedules;
    }

    Comparator<MaintainSchedule> comparator1 = new Comparator<MaintainSchedule>() {

        @Override
        public int compare(MaintainSchedule o1, MaintainSchedule o2) {
            int result = o1.getMainStartDate().compareTo(o2.getMainStartDate());
            if (result == 0) {
                return o1.getMainStartDate().before(o2.getMainStartDate()) ? -1 : 1;
            } else {
                return result;
            }
        }
    };

    @Override
    public void assignTeam() {
        List<MaintainanceTeam> teams = getUnassignedMaintainTeams();
        List<MaintainSchedule> schedules = getAllMaintainSchedules();
        Collections.sort(schedules, comparator1);

        //int counter=1;
        int scheduleSize = schedules.size();
        int teamSize = teams.size();

        int counter = 0;
        for (int k = 0; k < scheduleSize; k++) {

            if (counter > teamSize - 1) {
                counter = 0;
            }

            List<MaintainSchedule> mSchedules = teams.get(counter).getmSchedules();
            mSchedules.add(schedules.get(k));
            teams.get(counter).setmSchedules(mSchedules);

            schedules.get(k).setmTeam(teams.get(counter));

            em.persist(teams.get(counter));

            counter++;
            System.out.println("COUNTER:  " + counter);
        }

    }

    @Override
    public void generateTeam() {
        List<GroundCrew> gCrews = getAvailGroundCrews();
        int counter = gCrews.size() / 6;

        for (int i = 0; i < counter; i++) {
            MaintainanceTeam team = new MaintainanceTeam();
            team.create(6);

            team.setmSchedules(null);
            em.persist(team);
        }

    }

    @Override
    public void assignMainCrews() {

        List<MaintainanceTeam> mTeams = getAvailMaintainTeams();
        int counter = 0;

        for (MaintainanceTeam t : mTeams) {

            List<GroundCrew> teamCrews = t.getGroundCrews();

            counter = 0;
            t.setMaintainCrewNo(6);
            List<String> currentPosition = new ArrayList<String>();
            List<GroundCrew> gCrews = getAvailGroundCrews();

            for (GroundCrew gc : gCrews) {

                if (currentPosition.contains(gc.getPosition())) {
                } else {

                    teamCrews.add(gc);
                    gc.setmTeam(t);

                    currentPosition.add(gc.getPosition());

                    em.persist(gc);
                    counter++;

                }

                if (counter == 6) {
                    
                    t.setGroundCrews(teamCrews);
                    t.setStatus("Assigned");
                    em.persist(t);

                    currentPosition.clear();

                    break;

                }

            }
        }

    }

    public List<GroundCrew> getAvailGroundCrews() {
        List<GroundCrew> results = new ArrayList<GroundCrew>();
        Query q = em.createQuery("SELECT g FROM GroundCrew g");
        List<GroundCrew> gCrews = q.getResultList();
        for (GroundCrew g : gCrews) {
            if (g.getmTeam() == null && !g.getPosition().equals("Check-in Crew")) {
                results.add(g);
            }
        }
        return results;
    }

    //assign ground crews to team
    public List<MaintainanceTeam> getAvailMaintainTeams() {
        List<MaintainanceTeam> results = new ArrayList<MaintainanceTeam>();
        Query q = em.createQuery("SELECT t FROM MaintainanceTeam t");
        List<MaintainanceTeam> teams = q.getResultList();
        for (MaintainanceTeam t : teams) {
            if (t.getStatus().equals("N.A")) {
                results.add(t);
            }
        }

        return results;
    }

    @Override
    //assign schedules to teams
    public List<MaintainanceTeam> getUnassignedMaintainTeams() {
        List<MaintainanceTeam> results = new ArrayList<MaintainanceTeam>();
        Query q = em.createQuery("SELECT t FROM MaintainanceTeam t");
        List<MaintainanceTeam> teams = q.getResultList();
        for (MaintainanceTeam t : teams) {
            if (!t.getStatus().equals("N.A")) {
                results.add(t);
            }
        }

        return results;
    }

    public String getHub(Long tailNo) {
        Query q = em.createQuery("SELECT a FROM Aircraft a");
        List<Aircraft> aircrafts = q.getResultList();

        for (Aircraft a : aircrafts) {
            if (a.getTailNo().equals(tailNo)) {
                return a.getHub();
            }
        }
        return "null";
    }

    @Override
    public Aircraft getAircraftByTailNo(String tailNo) {
        Query q = em.createQuery("SELECT a FROM Aircraft a");
        List<Aircraft> aircrafts = q.getResultList();
        for (Aircraft a : aircrafts) {
            if (a.getTailNo().toString().equals(tailNo)) {
                return a;
            }
        }
        return null;
    }

    @Override
     public MaintainanceTeam getCrewMaintainTeam(String username) {
        Query q = em.createQuery("SELECT m FROM MaintainanceTeam m");
        List<MaintainanceTeam> teams = q.getResultList();
        
        for (MaintainanceTeam t : teams) {
            List<GroundCrew> crews = t.getGroundCrews();
            for (GroundCrew g : crews) {
                if (g.getEmployeeUserName().equals(username)) {
                    return t;
                }
            }
        }
        return null;
    }
     
     
     @Override
     public GroundCrew getGroundCrew(String username){
     
         Query q = em.createQuery("SELECT g FROM GroundCrew g WHERE g.employeeUserName =:username");
         q.setParameter("username", username);
         List<GroundCrew> crews = q.getResultList();
         if(crews.isEmpty()){
             return null;
         }
         
         return crews.get(0);
     }
    

}
