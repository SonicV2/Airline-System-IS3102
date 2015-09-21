/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Entity.Flight;
import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Entity.OrganizationUnit;
import CI.Entity.Pilot;
import FOS.Entity.Leg;
import FOS.Entity.Pairing;
import FOS.Entity.PairingPolicy;
import FOS.Entity.Team;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class PairingSessionBean implements PairingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private int time_scale_min;
    private int num_max_legs;
    private int hours_max_flight;
    private Map<Integer, String> info; // get date,flight

    private ArrayList<String> sol1;

    SimpleDateFormat formatter;

    @Override
    public void legMain(String selectMonth) {
        ArrayList<Leg> legs = new ArrayList<Leg>();
        ArrayList<Leg> route = new ArrayList<Leg>();
        Leg l;
        boolean first = true;
        ArrayList<String> solution;
        int numSols = 0;
        int numLegs = 0;
        int numofFlightHours = 0;
        int totalFlightHours = 0;

        legs = addLeg(selectMonth);
        if (legs.isEmpty()) {
        } else {
            sortList(legs);

            String destination = legs.get(0).getDestination();
            int startHour = legs.get(0).getStartHour();
            int finishHour = legs.get(0).getFinishHour();
            String date = legs.get(0).getDate1();

            ArrayList<ArrayList<String>> slns = new ArrayList<ArrayList<String>>(); //--- sln list
            do {
                route.clear();
                numLegs = 1;
                if (first == true) {
                    destination = legs.get(0).getDestination();
                    startHour = legs.get(0).getStartHour();
                    finishHour = legs.get(0).getFinishHour();
                    date = legs.get(0).getDate1();
                    //add the first element of the section if used
                    route.add(legs.get(0));
                    numofFlightHours = calcFlightHours(startHour, finishHour);
                    totalFlightHours = addFlightHours(totalFlightHours, numofFlightHours);
                    legs.remove(0);
                    first = false;
                }

                do {
                    //we seek a sln leg
                    l = searchSol(legs, destination, startHour, finishHour, date, numLegs, totalFlightHours);
                    if (l != null) {
                        //add the leg to route
                        route.add(l);
                        numLegs++;
                        destination = l.getDestination();
                        startHour = l.getStartHour();
                        finishHour = l.getFinishHour();
                        date = l.getDate1();
                        numofFlightHours = calcFlightHours(startHour, finishHour);
                        totalFlightHours = addFlightHours(totalFlightHours, numofFlightHours);
                        //delete the leg used
                        int indice = legs.indexOf(l);
                        legs.remove(indice);
                    } else {
                        numSols++;
                        showSoln(route, numSols, totalFlightHours);
                        first = true;
                        totalFlightHours = 0;
                    }
                } while (l != null);
            } while (legs.size() > 0);
        }
    }

    private void sortList(ArrayList<Leg> legs) {
        Collections.sort(legs);
    }

    public ArrayList<Leg> addLeg(String selectMonth) {

        ArrayList<Leg> legs = new ArrayList<Leg>();

        Query q = em.createQuery("SELECT s FROM Schedule s");

        List<Schedule> scheds = q.getResultList();

        for (Schedule s : scheds) {
            String formattedMonth = new SimpleDateFormat("MM").format(s.getStartDate());

            if (formattedMonth.equals(selectMonth)) {

                String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(s.getStartDate());

                String formattedStartTime = new SimpleDateFormat("HHmm").format(s.getStartDate());
                String formattedEndTime = new SimpleDateFormat("HHmm").format(s.getEndDate());

                int formatStartTime = Integer.parseInt(formattedStartTime);// use
                int formatEndTime = Integer.parseInt(formattedEndTime);//use

                Flight flight = new Flight();

                flight = s.getFlight();

                int flightNumber = Integer.parseInt(flight.getFlightNo().substring(2));

                String deptCity = flight.getRoute().getOriginCity();
                String destCity = flight.getRoute().getDestinationCity();

                legs.add(new Leg(flightNumber, deptCity, destCity, formatStartTime, formatEndTime, formattedDate));

            }
        }
        return legs;
    }

    //it returns a leg the fulfills the condition
    public Leg searchSol(ArrayList<Leg> legs, String destination, int startHour, int finishHour, String date,
            int numLegs, int numHourFlight) {
        Leg sol = null;
        boolean found = false;
        int hours = 0;
        int sum = 0;
        setPolicy();
        for (Leg i : legs) {
            if ((i.getDate1().equals(date)) && (i.getOrigin().equals(destination))
                    && ((calcDifMin(finishHour, i.getStartHour())) > getTime_scale_min()) && (i.getStartHour() > startHour)
                    && (numLegs < getNum_max_legs()) && (numHourFlight <= getHours_max_flight())) {
                hours = calcFlightHours(i.getStartHour(), i.getFinishHour());
                sum = sum + hours;
                sum = addFlightHours(sum, numHourFlight);
                if ((found == false) && (sum <= getHours_max_flight())) {
                    sol = i;
                    found = true;
                }
            }
        }
        return sol;
    }

    public void showSoln(ArrayList<Leg> leg, int numSol, int hFlight) {  //unformated

        Pairing pr = new Pairing();
        String sol = "";
        String date = "";
        String f = "";
        List<String> flightNos = new ArrayList<String>();
        List<String> flightCities = new ArrayList<String>();
        List<String> flightTimes = new ArrayList<String>();
        sol1 = new ArrayList<String>();

        String sections = "";
        String hours = "";
        String startHour = "";
        String finishHour = "";

        for (int i = 0; i < leg.size(); i++) {
            if (f == "") {
                f = leg.get(i).getDate1();
                date += f;

            }
            flightNos.add(leg.get(i).getLine() + "");

            if (i == 0) {
                flightCities.add(leg.get(i).getOrigin());
                flightCities.add(leg.get(i).getDestination());
                startHour = String.format("%04d", leg.get(i).getStartHour());
                finishHour = String.format("%04d", leg.get(i).getFinishHour());
                hours = startHour + "-" + finishHour;
                flightTimes.add(hours);
            } else {
                flightCities.add(leg.get(i).getDestination());
                startHour = String.format("%04d", leg.get(i).getStartHour());
                finishHour = String.format("%04d", leg.get(i).getFinishHour());
                hours = startHour + "-" + finishHour;
                flightTimes.add(hours);
            }
        }

        String resultMinDec;
        int minDecimal = ((hFlight % 100) * 100) / 60;
        if (minDecimal < 10) {
            resultMinDec = "0" + minDecimal;
        } else {
            resultMinDec = "" + minDecimal;
        }

        String totalFlightHour = (hFlight / 100) + " hours " + (hFlight % 100) + " minutes";
        pr.create(date, totalFlightHour, flightNos, flightCities, flightTimes);
        List<Pairing> check = getPairings();
        boolean isContain = false;
        if (check.isEmpty()) {
            em.persist(pr);
        }
        for (Pairing p : check) {
            if (p.getFDate().equals(pr.getFDate()) && p.getFlightHour().equals(pr.getFlightHour()) && p.getFlightNumbers().equals(pr.getFlightNumbers())
                    && p.getFlightCities().equals(pr.getFlightCities()) && p.getFlightTimes().equals(pr.getFlightTimes()) && p.isPaired() == pr.isPaired()) {
                isContain = true;
            }
        }

        if (isContain == false) {
            em.persist(pr);
        }

    }

    @Override
    public List<Pairing> getPairings() {
        Query q = em.createQuery("SELECT p FROM Pairing p");
        return q.getResultList();
    }

    @Override
    public Pairing getPairingByID(String id) {
        Query q = em.createQuery("SELECT p FROM Pairing p");
        List<Pairing> pairs = q.getResultList();
        for (Pairing p : pairs) {
            if (p.getId() == Long.parseLong(id)) {
                return p;
            }
        }

        return null;
    }

    @Override
    public Team generateTeam(Pairing pairing) {
        String flightDate = pairing.getFDate();
//        String flightHour = pairing.getFlightHour();
        List<String> flightCities = pairing.getFlightCities();
        List<String> flightNumbers = pairing.getFlightNumbers();
//        List<String> flightTimes = pairing.getFlightTimes();

        Team team = new Team();
        Flight flight;
        List<Schedule> schedules;
        List<Schedule> teamSchedule;

        List<Pilot> captainList = new ArrayList<Pilot>();
        List<Pilot> FOList = new ArrayList<Pilot>();

        List<CabinCrew> leadCCList = new ArrayList<CabinCrew>(); //lead female
        List<CabinCrew> CCList = new ArrayList<CabinCrew>(); //female
        List<CabinCrew> FSList = new ArrayList<CabinCrew>(); // male

        String lastCity = flightCities.get(flightCities.size() - 1); //set all the team location attribure to lastCity
        team.setLocation(lastCity);
        team.setPilotNo(2);
        team.setcCrewNo(8);

        //Add crews to the team
        List<Pilot> pilots = new ArrayList<Pilot>();
        Query q = em.createQuery("SELECT p FROM Pilot p");

        List<Pilot> ps = q.getResultList();
        for (Pilot pi : ps) {
            if (pi.isAssigned() == false) {
                if (pi.getPosition().equals("Captain")) {
                    captainList.add(pi);
                }
                if (pi.getPosition().equals("First Officer")) {
                    FOList.add(pi);
                }
            }
        }

        pilots.add(captainList.get(0)); //select 2 captains from the table
        captainList.get(0).setAssigned(true);
        em.persist(captainList.get(0));

        pilots.add(FOList.get(0));  //select 2 first officer fromt he table
        FOList.get(0).setAssigned(true);
        em.persist(FOList.get(0));

        team.setPilots(pilots);

        List<CabinCrew> CCs = new ArrayList<CabinCrew>();
        Query q1 = em.createQuery("SELECT p FROM CabinCrew p");

        List<CabinCrew> ps1 = q1.getResultList();
        for (CabinCrew cc : ps1) {
            if (cc.isAssigned() == false) {
                if (cc.getPosition().equals("Lead Flight Stewardess")) {
                    leadCCList.add(cc);
                }
                if (cc.getPosition().equals("Flight Stewardess")) {
                    CCList.add(cc);
                }
                if (cc.getPosition().equals("Flight Steward")) {
                    FSList.add(cc);
                }
            }
        }

        CCs.add(leadCCList.get(0));
        leadCCList.get(0).setAssigned(true);
        em.persist(leadCCList.get(0));

        for (int i = 0; i < 6; i++) {
            CCs.add(CCList.get(i));
            CCList.get(i).setAssigned(true);
            em.persist(CCList.get(i));
        }

        CCs.add(FSList.get(0));
        FSList.get(0).setAssigned(true);
        em.persist(FSList.get(0));

        team.setCabinCrews(CCs);
        em.persist(team);

        for (String s : flightNumbers) {
            flight = new Flight();
            flight = getFlight(s);

            schedules = new ArrayList<Schedule>();
            schedules = flight.getSchedule();

            for (Schedule sh : schedules) {
                if (sh.getStartDate().toString().equals(flightDate)) {
                    teamSchedule = team.getSchedule();
                    teamSchedule.add(sh);
                    sh.setTeam(team);
                }
            }
            em.persist(team);

        }

        return team;
    }

    public Flight getFlight(String flightNumber) {

        Query q = em.createQuery("SELECT f FROM Flight f");
        List<Flight> flights = q.getResultList();

        for (Flight f : flights) {
            if (f.getFlightNo().equals(flightNumber)) {
                return f;
            }
        }
        return null;
    }

    public void saveSolution(String solution, String archive) {
        try {
            FileWriter flS = new FileWriter(archive, true);
            BufferedWriter fS = new BufferedWriter(flS);
            fS.write(solution);
            fS.close();
        } catch (IOException e) {
            System.out.println("Error E/S en fichero escritura");
        }
    }

    public int calcFlightHours(int startHour, int finishHour) {
        int minStart = 0;
        int minFin = 0;
        int startHours = 0;
        int finishHours = 0;
        int minTotal = 0;
        int totalHours = 0;
        int totalTime = 0;
        int minTotalStart = 0;
        int minTotalFin = 0;
        int TotalMinTime = 0;
        minStart = startHour % 100;
        minFin = finishHour % 100;
        startHours = startHour / 100;
        finishHours = finishHour / 100;
        minTotalStart = (startHours * 60) + minStart;
        minTotalFin = (finishHours * 60) + minFin;

        if (finishHour < startHour) {
            minTotalFin = minTotalFin + 1440;
        }
        TotalMinTime = minTotalFin - minTotalStart;
        totalHours = TotalMinTime / 60;
        minTotal = TotalMinTime % 60;
        totalTime = (totalHours * 100) + minTotal;
        return totalTime;
    }

    //calculate the time difference in minutes between two last hours
    public int calcDifMin(int earlyHour, int FinalHour) {
        int minStart = 0;
        int minFin = 0;
        int startHour = 0;
        int finishHour = 0;
        int minTotalStart = 0;
        int minTotalFin = 0;
        int difMinTime = 0;
        minStart = earlyHour % 100;
        minFin = FinalHour % 100;
        startHour = earlyHour / 100;
        finishHour = FinalHour / 100;
        minTotalStart = (startHour * 60) + minStart;
        minTotalFin = (finishHour * 60) + minFin;
        difMinTime = minTotalFin - minTotalStart;
        return difMinTime;
    }

    //accumulate the hours flown
    public static int addFlightHours(int totalFlightHours, int numHoursFlight) {
        int minTotal = 0;
        int hoursTotal = 0;
        int minHV = 0;
        int hoursHV = 0;
        int sumMin = 0;
        int sumHours = 0;
        int totalMin = 0;
        int totalH = 0;
        int totalM = 0;
        int totalTime = 0;
        minTotal = totalFlightHours % 100;
        hoursTotal = totalFlightHours / 100;
        minHV = numHoursFlight % 100;
        hoursHV = numHoursFlight / 100;
        sumMin = minTotal + minHV;
        sumHours = (hoursTotal * 60) + (hoursHV * 60);
        totalMin = sumMin + sumHours;
        totalH = totalMin / 60;
        totalM = totalMin % 60;
        totalTime = (totalH * 100) + totalM;
        return totalTime;
    }

    @Override
    public void setPolicy() {  //retrive from data base and set the global variables

        Query q = em.createQuery("SELECT p FROM PairingPolicy p");
        List<PairingPolicy> results = q.getResultList();
        PairingPolicy pp = (PairingPolicy) results.get(0);

        setTime_scale_min(pp.getTime_scale_min());
        setNum_max_legs(pp.getNum_max_legs());
        setHours_max_flight(pp.getHours_max_flight());
    }

    @Override
    public void changePolicy(int maxLeg, int maxFlight, int minStopTime) {
        Query q = em.createQuery("SELECT p FROM PairingPolicy p");
        List<PairingPolicy> results = q.getResultList();
        PairingPolicy pp = (PairingPolicy) results.get(0);

        pp.setNum_max_legs(maxLeg);
        pp.setHours_max_flight(maxFlight);
        pp.setTime_scale_min(minStopTime);
        em.persist(pp);

        setTime_scale_min(pp.getTime_scale_min());
        setNum_max_legs(pp.getNum_max_legs());
        setHours_max_flight(pp.getHours_max_flight());

    }

    @Override
    public PairingPolicy retrievePolicy() {
        Query q = em.createQuery("SELECT p FROM PairingPolicy p");
        List<PairingPolicy> results = q.getResultList();
        PairingPolicy pp = (PairingPolicy) results.get(0);

        return pp;
    }

//    // Get Flight Entity use flight number
//    public Flight getFlight(String flightNo) {
//        //Flight flight = new Flight();
//        Query q = em.createQuery("SELECT f FROM Flight f WHERE f.flightNo =:flightNo");
//        q.setParameter("flightNo", flightNo);
//        List<Flight> results = q.getResultList();
//        if (results != null) {
//            return results.get(0);
//        } else {
//            return null;
//        }
//    }
//    public Schedule getSchedule(String flightNo, String date) {
//        Flight flight = getFlight(flightNo);
//        List<Schedule> schedules = flight.getSchedule();
//        for (Schedule s : schedules) {
//            if (s.getStartDate().toString().equals(date)) {
//                return s;
//            }
//        }
//        return null;
//    }
    /**
     * @return the time_scale_min
     */
    public int getTime_scale_min() {
        return time_scale_min;
    }

    /**
     * @param time_scale_min the time_scale_min to set
     */
    public void setTime_scale_min(int time_scale_min) {
        this.time_scale_min = time_scale_min;
    }

    /**
     * @return the num_max_legs
     */
    public int getNum_max_legs() {
        return num_max_legs;
    }

    /**
     * @param num_max_legs the num_max_legs to set
     */
    public void setNum_max_legs(int num_max_legs) {
        this.num_max_legs = num_max_legs;
    }

    /**
     * @return the hours_max_flight
     */
    public int getHours_max_flight() {
        return hours_max_flight;
    }

    /**
     * @param hours_max_flight the hours_max_flight to set
     */
    public void setHours_max_flight(int hours_max_flight) {
        this.hours_max_flight = hours_max_flight;
    }

}
