/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Session;

import APS.Entity.Aircraft;
import APS.Entity.Schedule;
import Administration.Entity.ProfitAndLoss;
import CI.Entity.CabinCrew;
import CI.Entity.Employee;
import CI.Entity.Pilot;
import Distribution.Entity.PNR;
import Distribution.Entity.TravelAgency;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ProfitAndLossSessionBean implements ProfitAndLossSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private ProfitAndLoss pnl;

    @Override
    public ProfitAndLoss createProfitAndLoss(Date dateChosen) {

        pnl = new ProfitAndLoss();

        /* Calculate salesRevenue of the month*/
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = formatter.format(dateChosen);
        double salesRevenue = 0;

        List<PNR> PNRs = new ArrayList();
        PNRs = getConfirmedPNRs("Confirmed");
        List<PNR> temp = new ArrayList();
        temp = getConfirmedPNRs("Booked");

        if (PNRs != null && !PNRs.isEmpty()) {
            if (temp != null && !temp.isEmpty()) {
                for (PNR eachBookedPNR : temp) {
                    PNRs.add(eachBookedPNR);
                }
            }
            for (PNR eachPNR : PNRs) {
                if (eachPNR.getDateOfConfirmation() != null) {
                    String formattedEachDate = formatter.format(eachPNR.getDateOfConfirmation());
                    if (formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        double purePrice = eachPNR.getTotalPrice();
                        for (int i = 0; i < eachPNR.getBookings().size(); i++) {
                            if (eachPNR.getBookings().get(i).isBoughtInsurance()) {
                                purePrice -= 15.0;
                            }
                        }

                        salesRevenue += purePrice;
                    }
                }
            }
        }

        /* Calculate commission */
        double commission = 0;
        List<TravelAgency> allAgencies = new ArrayList();
        allAgencies = getAllTravelAgencies();

        for (TravelAgency eachAgency : allAgencies) {

            commission += (getCurrentMonthSettlement(eachAgency, dateChosen)) * 0.1;

        }

        /* Calculate aircrafts purchased */
        double aircraftPurchased = 0;
        List<Aircraft> allAircrafts = new ArrayList();
        allAircrafts = retrieveAircrafts();

        if (allAircrafts != null && !allAircrafts.isEmpty()) {
            for (Aircraft eachAircraft : allAircrafts) {
                if (eachAircraft.getDatePurchased() != null) {
                    String formattedEachDate = formatter.format(eachAircraft.getDatePurchased());
                    if (formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        aircraftPurchased += (eachAircraft.getAircraftType().getCost()) * 1000000;
                    }
                }
            }
        }

        /* Calculate fuelCost */
        double fuelCost = 0;
        List<Schedule> allSchedules = new ArrayList();
        allSchedules = getSchedules();

        if (allSchedules != null && !allSchedules.isEmpty()) {
            for (Schedule eachSchedule : allSchedules) {
                if (eachSchedule.getStartDate() != null) {
                    String formattedEachDate = formatter.format(eachSchedule.getStartDate());
                    if (formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        System.out.println("Before calculation!!!!!!!!" + eachSchedule.getAircraft().getAircraftType().getFuelCost());
                        System.out.println("Before calculation!!!!!!!!" + eachSchedule.getFlight().getRoute().getDistance());

                        fuelCost += (eachSchedule.getAircraft().getAircraftType().getFuelCost()) * (eachSchedule.getFlight().getRoute().getDistance());

                     System.out.println("AFTER CALCULATION");

                    }
                }
            }
        }

        /* Calculate employee salaries */
        double employeeSalaries = 0;
        List<Employee> allEmployees = new ArrayList();
        List<CabinCrew> allCabins = new ArrayList();
        List<Pilot> allPilots = new ArrayList();
        List<Employee> executives = new ArrayList();
        List<Employee> managers = new ArrayList();

        allEmployees = getEmployees();
        allCabins = getCabinCrews();
        allPilots = getPilots();

        if (allEmployees == null) {
            employeeSalaries = 0;
        } else {
            for (Employee eachEmployee : allEmployees) {

                if (eachEmployee.getEmployeeRole().equalsIgnoreCase("Executive")) {
                    executives.add(eachEmployee);
                } else if (eachEmployee.getEmployeeRole().equalsIgnoreCase("Manager")) {
                    managers.add(eachEmployee);
                }

            }

            employeeSalaries = (executives.size() * 2000) + (managers.size() * 1000) + (allCabins.size() * 1000) + (allPilots.size() * 1500);

        }

        double totalExpenses = commission + aircraftPurchased + fuelCost + employeeSalaries + 10000 + (salesRevenue * 0.1);
        double totalPnL = salesRevenue - totalExpenses;

        pnl.createPnL(dateChosen, salesRevenue, commission, aircraftPurchased, fuelCost, employeeSalaries, 10000, (salesRevenue * 0.1), salesRevenue, totalExpenses, totalPnL);
        em.persist(pnl);

        return pnl;

    }

    @Override
    public ProfitAndLoss updateProfitAndLoss(ProfitAndLoss selectedPnl) {

        Date dateChosen = selectedPnl.getDateOfStatement();

        /* Calculate salesRevenue of the month*/
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = formatter.format(dateChosen);
        double salesRevenue = 0;

        List<PNR> PNRs = new ArrayList();
        PNRs = getConfirmedPNRs("Confirmed");
        List<PNR> temp = new ArrayList();
        temp = getConfirmedPNRs("Booked");

        if (PNRs != null && !PNRs.isEmpty()) {
            if (temp != null && !temp.isEmpty()) {
                for (PNR eachBookedPNR : temp) {
                    PNRs.add(eachBookedPNR);
                }
            }
            for (PNR eachPNR : PNRs) {
                if (eachPNR.getDateOfConfirmation() != null) {
                    String formattedEachDate = formatter.format(eachPNR.getDateOfConfirmation());
                    if (formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        double purePrice = eachPNR.getTotalPrice();
                        for (int i = 0; i < eachPNR.getBookings().size(); i++) {
                            if (eachPNR.getBookings().get(i).isBoughtInsurance()) {
                                purePrice -= 15.0;
                            }
                        }

                        salesRevenue += purePrice;
                    }
                }
            }
        }

        selectedPnl.setSalesRevenue(salesRevenue);

        /* Calculate commission */
        double commission = 0;
        List<TravelAgency> allAgencies = new ArrayList();
        allAgencies = getAllTravelAgencies();

        for (TravelAgency eachAgency : allAgencies) {

            commission += (getCurrentMonthSettlement(eachAgency, dateChosen)) * 0.1;

        }

        selectedPnl.setCommission(commission);

        /* Calculate aircrafts purchased */
        double aircraftPurchased = 0;
        List<Aircraft> allAircrafts = new ArrayList();
        allAircrafts = retrieveAircrafts();

        if (allAircrafts != null && !allAircrafts.isEmpty()) {
            for (Aircraft eachAircraft : allAircrafts) {
                if (eachAircraft.getDatePurchased() != null) {
                    String formattedEachDate = formatter.format(eachAircraft.getDatePurchased());
                    if (formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        aircraftPurchased += (eachAircraft.getAircraftType().getCost()) * 1000000;
                    }
                }
            }
        }

        selectedPnl.setAircrafts(aircraftPurchased);

        /* Calculate fuelCost */
        double fuelCost = 0;
        List<Schedule> allSchedules = new ArrayList();
        allSchedules = getSchedules();

        if (allSchedules != null && !allSchedules.isEmpty()) {
            for (Schedule eachSchedule : allSchedules) {
                if (eachSchedule.getStartDate() != null) {
                    String formattedEachDate = formatter.format(eachSchedule.getStartDate());
                    if (formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        fuelCost += (eachSchedule.getAircraft().getAircraftType().getFuelCost()) * (eachSchedule.getFlight().getRoute().getDistance());
                    }
                }
            }
        }

        selectedPnl.setFuelCosts(fuelCost);

        /* Calculate employee salaries */
        double employeeSalaries = 0;
        List<Employee> allEmployees = new ArrayList();
        List<CabinCrew> allCabins = new ArrayList();
        List<Pilot> allPilots = new ArrayList();
        List<Employee> executives = new ArrayList();
        List<Employee> managers = new ArrayList();

        allEmployees = getEmployees();
        allCabins = getCabinCrews();
        allPilots = getPilots();

        if (allEmployees == null) {
            employeeSalaries = 0;
        } else {
            for (Employee eachEmployee : allEmployees) {

                if (eachEmployee.getEmployeeRole().equalsIgnoreCase("Executive")) {
                    executives.add(eachEmployee);
                } else if (eachEmployee.getEmployeeRole().equalsIgnoreCase("Manager")) {
                    managers.add(eachEmployee);
                }

            }

            employeeSalaries = (executives.size() * 2000) + (managers.size() * 1000) + (allCabins.size() * 1000) + (allPilots.size() * 1500);

        }

        selectedPnl.setEmployeeSalaries(employeeSalaries);

        double totalExpenses = commission + aircraftPurchased + fuelCost + employeeSalaries + 10000 + (salesRevenue * 0.1);
        double totalPnL = salesRevenue - totalExpenses;

        selectedPnl.setTotalExpenses(totalExpenses);
        selectedPnl.setTotalRevenue(salesRevenue);
        selectedPnl.setTotalPnL(totalPnL);

        em.merge(selectedPnl);
        em.flush();

        return selectedPnl;
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
    public List<TravelAgency> getAllTravelAgencies() {
        List<TravelAgency> allTravelAgencies = new ArrayList();

        try {
            Query q = em.createQuery("SELECT a from TravelAgency a");

            List<TravelAgency> results = q.getResultList();
            if (!results.isEmpty()) {

                allTravelAgencies = results;

            } else {
                allTravelAgencies = null;
                System.out.println("no travel agencies!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return allTravelAgencies;
    }

    @Override
    public double getCurrentMonthSettlement(TravelAgency travelAgency, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = formatter.format(date);
        double settlementAmount = 0;

        List<PNR> travelAgencyPNRs = new ArrayList();
        travelAgencyPNRs = travelAgency.getPnrs();
        if (travelAgencyPNRs != null && !travelAgencyPNRs.isEmpty()) {
            for (PNR eachPNR : travelAgencyPNRs) {
                if (eachPNR.getDateOfConfirmation() != null) {
                    String formattedEachDate = formatter.format(eachPNR.getDateOfConfirmation());
                    if (eachPNR.getPnrStatus().equalsIgnoreCase("Confirmed") && formattedDate.substring(2, 8).equals(formattedEachDate.substring(2, 8))) {
                        double purePrice = eachPNR.getTotalPrice();
                        for (int i = 0; i < eachPNR.getBookings().size(); i++) {
                            if (eachPNR.getBookings().get(i).isBoughtInsurance()) {
                                purePrice -= 15.0;
                            }
                        }

                        settlementAmount += purePrice;
                    }
                }
            }
        }
        return settlementAmount;
    }

    @Override
    public List<Schedule> getSchedules() {
        List<Schedule> schedules = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule a");

            List<Schedule> results = q.getResultList();
            if (!results.isEmpty()) {
                schedules = results;

            } else {
                schedules = null;
                System.out.println("No Schedules Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return schedules;
    }

    @Override
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM Employee a");

            List<Employee> results = q.getResultList();
            if (!results.isEmpty()) {
                employees = results;

            } else {
                employees = null;
                System.out.println("No employee!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return employees;
    }

    @Override
    public List<CabinCrew> getCabinCrews() {
        List<CabinCrew> cabins = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM CabinCrew a");

            List<CabinCrew> results = q.getResultList();
            if (!results.isEmpty()) {
                cabins = results;

            } else {
                cabins = null;
                System.out.println("No cabin crews!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return cabins;
    }

    @Override
    public List<Pilot> getPilots() {
        List<Pilot> pilots = new ArrayList();
        try {

            Query q = em.createQuery("SELECT a FROM Pilot a");

            List<Pilot> results = q.getResultList();
            if (!results.isEmpty()) {
                pilots = results;

            } else {
                pilots = null;
                System.out.println("No pilotss!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return pilots;
    }

    @Override
    public ProfitAndLoss getPnLbyDate(Date chosenDate) {
        ProfitAndLoss pnlFound = new ProfitAndLoss();
        try {

            Query q = em.createQuery("SELECT a FROM ProfitAndLoss " + "AS a WHERE a.dateOfStatement=:dateOfStatement");
            q.setParameter("dateOfStatement", chosenDate);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                pnlFound = (ProfitAndLoss) results.get(0);

            } else {
                pnlFound = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return pnlFound;

    }

}
