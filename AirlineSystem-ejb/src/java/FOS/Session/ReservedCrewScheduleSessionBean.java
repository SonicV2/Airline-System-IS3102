/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import FOS.Entity.Team;
import java.text.SimpleDateFormat;
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
public class ReservedCrewScheduleSessionBean implements ReservedCrewScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    final static String pattern1 = "1111100001111000111110001111001";
    final static String pattern2 = "0000111110001111000111110011111";
    static int count = 1;

    // get all unassigned schedule reserved crew
    @Override
    public List<CabinCrew> getAllReservedCrew(String selectYear, String selectMonth) {
        List<CabinCrew> results = new ArrayList<CabinCrew>();

        Query q = em.createQuery("SELECT c FROM CabinCrew c");
        List<CabinCrew> ccs = q.getResultList();
        for (CabinCrew cc : ccs) {
            if (cc.getPosition().contains("Reserved") && cc.getSchedule().equals("N.A")) {
                results.add(cc);
            } else if (cc.getPosition().contains("Reserved") && !cc.getSchedule().equals("N.A")) {
                if (cc.getSchedule().contains("&")) {
                    String[] yearMonths = cc.getSchedule().split("&");
                    String yearMonth = yearMonths[yearMonths.length - 1].split("-")[0];

                    int year = Integer.parseInt(yearMonth.substring(0, 4));
                    int month = Integer.parseInt(yearMonth.substring(4));

                    if (year == Integer.parseInt(selectYear) && month + 1 == Integer.parseInt(selectMonth)) {
                        results.add(cc);
                    }
                    if ((year + 1) == Integer.parseInt(selectYear) && month == 12 && Integer.parseInt(selectMonth) == 1) {
                        results.add(cc);
                    }

                } else {
                    String yearMonth = cc.getSchedule().split("-")[0];
                    int year = Integer.parseInt(yearMonth.substring(0, 4));
                    int month = Integer.parseInt(yearMonth.substring(4));

                    if (year == Integer.parseInt(selectYear) && month + 1 == Integer.parseInt(selectMonth)) {
                        results.add(cc);
                    }
                    if ((year + 1) == Integer.parseInt(selectYear) && month == 12 && Integer.parseInt(selectMonth) == 1) {
                        results.add(cc);
                    }
                }
            }
        }
        return results;
    }

    @Override
    public void assignSchedule(String selectYear, String selectMonth, String crewName) {
        CabinCrew crew = getCabinCrew(crewName);
        if (count % 2 == 0) {
            String sch = crew.getSchedule();
            if (sch.equals("N.A")) {
                crew.setSchedule(selectYear + selectMonth + "-" + pattern1);
                count++;
            } else {
                crew.setSchedule(sch + "&" + selectYear + selectMonth + "-" + pattern1);
                count++;
            }

        } else {
            String sch = crew.getSchedule();
            if (sch.equals("N.A")) {
                crew.setSchedule(selectYear + selectMonth + "-" + pattern2);
                count++;
            } else {
                crew.setSchedule(sch + "&" + selectYear + selectMonth + "-" + pattern2);
                count++;
            }
        }
    }

    public CabinCrew getCabinCrew(String crewName) {

        Query q = em.createQuery("SELECT c FROM CabinCrew c WHERE c.employeeUserName = :userName");
        q.setParameter("userName", crewName);
        List<CabinCrew> crews = q.getResultList();
        return crews.get(0);
    }

    @Override
    public String reassign(String crewName) {

        CabinCrew crew = getCabinCrew(crewName);
        if (crew == null || crew.getTeam() == null) {
            return "unsuccessful";
        }

        List<String> langs = crew.getLanguages();

        List<CabinCrew> teamCrews = crew.getTeam().getCabinCrews();
        Team team = crew.getTeam();

        String scheduleId = crew.getStatus().split("-")[0].split("Leave: ")[1];
        int formattedDate = -1;
        String yearMonth = "";
        String chooseSchedule = "";

        List<CabinCrew> reservedCrews = getReservedCrew();

        if (scheduleId.contains("/")) {

            Schedule schedule = getSchedule(scheduleId.split("/")[0]);
            formattedDate = Integer.parseInt(new SimpleDateFormat("dd").format(schedule.getStartDate()));
            String year = new SimpleDateFormat("yyyy").format(schedule.getStartDate());
            String month = new SimpleDateFormat("MM").format(schedule.getStartDate());
            yearMonth = year + month;

        } else {
            Schedule schedule = getSchedule(scheduleId);
            formattedDate = Integer.parseInt(new SimpleDateFormat("dd").format(schedule.getStartDate()));
            String year = new SimpleDateFormat("yyyy").format(schedule.getStartDate());
            String month = new SimpleDateFormat("MM").format(schedule.getStartDate());
            yearMonth = year + month;
        }

        for (CabinCrew c : reservedCrews) {
            if (c.getOrganizationUnit().getLocation().equals(crew.getOrganizationUnit().getLocation()) && c.getPosition().substring(9).equals(crew.getPosition())
                    && c.getTeam() == null) {
                if (!c.getSchedule().equals("N.A")) {

                    if (c.getSchedule().contains("&")) {
                        if (c.getSchedule().contains(yearMonth)) {
                            String[] schs = c.getSchedule().split("&");
                            for (int i = 0; i < schs.length; i++) {
                                if (schs[i].contains(yearMonth)) {
                                    chooseSchedule = schs[i].split("-")[1];
                                }
                            }
                        }
                    } else {
                        chooseSchedule = c.getSchedule();
                    }

                    String index = chooseSchedule.substring(formattedDate - 1, formattedDate);
                    if (index.equals("1")) {

                        if (c.getLanguages().contains(langs.get(0)) || c.getLanguages().contains(langs.get(1))) {

                            Long teamId = crew.getTeam().getId();
                            crew.setSchedule("Team: " + teamId + "/" + c.getEmployeeUserName());
                            em.merge(crew);

                            team.setcCrewNo(team.getcCrewNo() + 1);
                            teamCrews.add(c);
                            team.setCabinCrews(teamCrews);
                            em.merge(team);
                            c.setAssigned(true);
                            c.setStatus(scheduleId);
                            c.setTeam(team);
                            em.merge(c);
                            return "successful";
                        }
                    }

                }
            }
        }

        if (reservedCrews.isEmpty()) {
            return "unsuccessful";
        } else {
            List<CabinCrew> temps = new ArrayList<CabinCrew>();
            for (CabinCrew c : reservedCrews) {
                if (c.getOrganizationUnit().getLocation().equals(crew.getOrganizationUnit().getLocation())) {
                    temps.add(c);
                }
            }

            if (temps.isEmpty()) {
                return "unsuccessful";
            } else {
                for (CabinCrew c : temps) {
                    if (!c.getSchedule().equals("N.A")) { // need to modify -----------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!
                        String index = c.getSchedule().substring(formattedDate - 1, formattedDate);
                        if (index.equals("1")) {
                            Long teamId = crew.getTeam().getId();
                            crew.setSchedule("Team: " + teamId + "/" + reservedCrews.get(0).getEmployeeUserName());
                            em.merge(crew);

                            team.setcCrewNo(team.getcCrewNo() + 1);
                            teamCrews.add(c);
                            team.setCabinCrews(teamCrews);
                            em.merge(team);

                            c.setAssigned(true);
                            c.setStatus(scheduleId);
                            c.setTeam(team);
                            em.merge(c);
                            return "successful";
                        }
                    }
                }
            }

        }
        return "unsuccessful";
    }

    @Override
    //Assign crew back to previously team
    public String assignBack(String crewName) {
        CabinCrew crew = getCabinCrew(crewName);
        System.out.println("!!!!!!!!! name: " + crew.getEmployeeUserName());

        if (crew.getPosition().contains("Reserved") && !crew.getStatus().equals("N.S")) {
            Team team = crew.getTeam();
            List<CabinCrew> crews = team.getCabinCrews();
            for (CabinCrew c : crews) {
                if (!c.getSchedule().equals("N.A")) {
                    if (c.getSchedule().split("/")[1].equals(crewName)) {
                        System.out.println("HEREDDDD");
                        CabinCrew reservedCrew = getCabinCrew(c.getSchedule().split("/")[1]);
//                        Team team = reservedCrew.getTeam();
                        reservedCrew.setTeam(null);
                        reservedCrew.setAssigned(false);
                        reservedCrew.setStatus("N.S");
                        em.merge(reservedCrew);

                        c.setSchedule("N.A");
                        c.setStatus("N.S");
                        em.merge(c);

                        List<CabinCrew> ccrews = team.getCabinCrews();
                        ccrews.remove(reservedCrew);
                        team.setCabinCrews(ccrews);
                        em.merge(team);
                        return "successful";
                    }
                }
            }
        }

        return "unsuccessful";
    }

    public List<CabinCrew> getReservedCrew() {
        List<CabinCrew> results = new ArrayList<CabinCrew>();

        Query q = em.createQuery("SELECT c FROM CabinCrew c");
        List<CabinCrew> ccs = q.getResultList();
        for (CabinCrew cc : ccs) {
            if (cc.getPosition().contains("Reserved")) {
                results.add(cc);
            }
        }
        return results;
    }

    public Schedule getSchedule(String scheduleId) {
        Query q = em.createQuery("SELECT s FROM Schedule s");

        List<Schedule> schedules = q.getResultList();
        for (Schedule s : schedules) {

            if (s.getScheduleId().toString().equals(scheduleId)) {
                return s;
            }
        }
        return null;
    }
}
