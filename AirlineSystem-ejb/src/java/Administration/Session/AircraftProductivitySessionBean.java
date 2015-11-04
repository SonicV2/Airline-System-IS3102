/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Session;

import APS.Entity.Aircraft;
import APS.Entity.Schedule;
import Distribution.Entity.PNR;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yunna
 */
@Stateless
public class AircraftProductivitySessionBean implements AircraftProductivitySessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public String calculateYearsUsed(Aircraft aircraft) {

        Date datePurchased = aircraft.getDatePurchased();
        Date current = new Date();

        long difference = current.getTime() - datePurchased.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(difference);
        String daysUsed = String.valueOf(diffInDays);

        return daysUsed;
    }

    @Override
    public double calculateTotalDistance(Aircraft aircraft) {

        List<Schedule> schedules = new ArrayList();
        schedules = aircraft.getSchedules();
        double totalDistance = 0;

        for (Schedule eachSchedule : schedules) {
            if (eachSchedule.getStartDate().before(new Date())) {
                totalDistance += eachSchedule.getFlight().getRoute().getDistance();
            }
        }

        return totalDistance;
    }

    @Override
    public double calculateTotalTravelTime(Aircraft aircraft) {

        List<Schedule> schedules = new ArrayList();
        schedules = aircraft.getSchedules();
        double totalTime = 0;

        for (Schedule eachSchedule : schedules) {
            if (eachSchedule.getStartDate().before(new Date())) {
                totalTime += eachSchedule.getFlight().getFlightDuration();
            }
        }

        return totalTime;
    }

    @Override
    public double calculateTotalProfit(Aircraft aircraft) {
        
        double profit = 0;

        List<PNR> PNRs = new ArrayList();
        PNRs = getConfirmedPNRs("Confirmed");
        List<PNR> temp = new ArrayList();
        temp = getConfirmedPNRs("Booked");

        for (PNR eachBookedPNR : temp) {
            PNRs.add(eachBookedPNR);
        }

        if (PNRs != null && !PNRs.isEmpty()) {
            for (PNR eachPNR : PNRs) {
                if (eachPNR.getDateOfConfirmation() != null) {
                    if (eachPNR.getBookings().get(0).getSeatAvail().getSchedule().getStartDate().before(new Date())) {
                        double purePrice = eachPNR.getTotalPrice();
                        for (int i = 0; i < eachPNR.getBookings().size(); i++) {
                            if (eachPNR.getBookings().get(i).isBoughtInsurance()) {
                                purePrice -= 15.0;
                            }
                        }

                        profit += purePrice;
                    }
                }
            }
        }

        return profit;
    }

    @Override
    public List<PNR> getConfirmedPNRs(String status) {

        List<PNR> confirmedPNRs = new ArrayList<PNR>();

        try {
            Query q = em.createQuery("SELECT a FROM PNR " + "AS a WHERE a.pnrStatus=:pnrStatus");
            q.setParameter("pnrStatus", status);

            List<PNR> results = q.getResultList();
            if (!results.isEmpty()) {

                confirmedPNRs = results;

            } else {
                confirmedPNRs = null;
                System.out.println("no confirmed pnr!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return confirmedPNRs;
    }
}
