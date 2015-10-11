/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Aircraft;
import APS.Entity.Schedule;
import FOS.Entity.MaintainSchedule;
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
    public List<MaintainSchedule> getAllMaintainSchedules(){
        Query q = em.createQuery("SELECT m FROM MaintainSchedule m");
        List<MaintainSchedule> mSchedules = q.getResultList();
        return mSchedules;
    }
    
    @Override
    public void assignTeam(){
        List<MaintainSchedule> schedules = getAllMaintainSchedules();
        int counter=1;
        for(MaintainSchedule s : schedules){
            if(counter==11){
                counter=1;
            }
            String hub = getHub(s.getTailNumber());
            s.setTeamId("Team "+counter+"("+ hub +")");
            counter++;
        }
    }
    
    public String getHub(Long tailNo){
        Query q = em.createQuery("SELECT a FROM Aircraft a");
        List<Aircraft> aircrafts = q.getResultList();
        
        for(Aircraft a : aircrafts){
            if(a.getTailNo().equals(tailNo)){
                return a.getHub();
            }
        }
        return "null";
    }
}
