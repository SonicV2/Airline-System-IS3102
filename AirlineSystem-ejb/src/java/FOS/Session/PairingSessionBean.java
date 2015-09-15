/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.Leg;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author smu
 */
@Stateless
public class PairingSessionBean implements PairingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    final static int TIME_SCALE_MIN = 60;//minimum stop over time between two flight legs (2 hrs)
    final static int NUM_MAX_LEGS = 3; // Maximum flight legs in a pair
    final static int HOURS_MAX_FLIGHT = 1200; // Maximum flight hrs in a pair ( 12 hrs)

    @Override
    public void legMain() {
        System.out.println("-------------Welcome to crew pairing system---------------\n\n\n");
        //long programStart = ElapsedTime.systemTime();

        ArrayList<Leg> legs = new ArrayList<Leg>();
        ArrayList<Leg> route = new ArrayList<Leg>();
        Leg l;
        boolean first = true;
        String solution = "";
        int numSols = 0;
        int numLegs = 0;
        int numofFlightHours = 0;
        int totalFlightHours = 0;

        Leg l1 = new Leg(3028, " MXP", "BCN", 1850, 1950, "03/09/2012");

        Leg l2 = new Leg(2050, "BCN", "MAD", 2100, 2140, "03/09/2012");

        Leg l3 = new Leg(2240, "MAD", "BCN", 2240, 2310, "03/09/2012");

        Leg l4 = new Leg(1010, "BCN", "VLC", 1420, 1440, "03/09/2012");
        legs.add(l1);

        legs.add(l2);

        legs.add(l3);
        legs.add(l4);

        sortList(legs);

        String destination = legs.get(0).getDestination();
        int startHour = legs.get(0).getStartHour();
        int finishHour = legs.get(0).getFinishHour();
        String date = legs.get(0).getDate1();

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
                    solution = showSol(route, numSols, totalFlightHours);

                    saveSolution(solution, "/Users/smu/Desktop/output.txt");
                    first = true;
                    totalFlightHours = 0;
                }
            } while (l != null);
        } while (legs.size() > 0);

        System.out.println("\n**** END OF PROGRAM ****");
//		long programEnd = ElapsedTime.systemTime();
//		System.out.println("Total elapsed time = " + ElapsedTime.calcElapsedHMS(programStart, programEnd));

    }

    private static void sortList(ArrayList<Leg> legs) {
        Collections.sort(legs);
    }

    //it returns a leg the fulfills the condition
    public static Leg searchSol(ArrayList<Leg> legs, String destination, int startHour, int finishHour, String date,
            int numLegs, int numHourFlight) {
        Leg sol = null;
        boolean found = false;
        int hours = 0;
        int sum = 0;
        for (Leg i : legs) {
            if ((i.getDate1().equals(date)) && (i.getOrigin().equals(destination))
                    && ((calcDifMin(finishHour, i.getStartHour())) > TIME_SCALE_MIN) && (i.getStartHour() > startHour)
                    && (numLegs < NUM_MAX_LEGS) && (numHourFlight <= HOURS_MAX_FLIGHT)) {
                hours = calcFlightHours(i.getStartHour(), i.getFinishHour());
                sum = sum + hours;
                sum = addFlightHours(sum, numHourFlight);
                if ((found == false) && (sum <= HOURS_MAX_FLIGHT)) {
                    sol = i;
                    found = true;
                }
            }
        }
        return sol;
    }

    public static String showSol(ArrayList<Leg> leg, int numSol, int hFlight) {
        String sol = "";
        String date = " date =  ";
        String f = "";
        String hoursFlight = " hours of flight performed = ";
        String nLines = " numLines =   ";
        String sections = "";
        String hours = "";
        String startHour = "";
        String finishHour = "";
        sol += "*******************************************************************" + "********\n";
        if (((numSol / 10) > 0) && ((numSol / 10) < 10)) {
            sol += "*********************** S O L U T I O N " + numSol + " ***************************\n";
        } else if (((numSol / 10) > 9) && ((numSol / 10) < 100)) {
            sol += "*********************** S O L U T I O N  " + numSol + " **************************\n";
        } else if (((numSol / 10) > 99) && ((numSol / 10) < 1000)) {
            sol += "*********************** S O L U T I O N  " + numSol + " *************************\n";
        } else {
            sol += "*********************** S O L U T I O N  " + numSol + " ****************************\n";
        }

        sol += "***************************************************************************\n";
        for (int i = 0; i < leg.size(); i++) {
            if (f == "") {
                f = leg.get(i).getDate1();
                date += f;
            }
            nLines += leg.get(i).getLine() + "       ";
            if (i == 0) {
                sections += "         " + leg.get(i).getOrigin() + "------ " + leg.get(i).getDestination();
                startHour = String.format("%04d", leg.get(i).getStartHour());
                finishHour = String.format("%04d", leg.get(i).getFinishHour());
                hours += "            " + startHour + "-" + finishHour;
            } else {
                sections += " ------ " + leg.get(i).getDestination();
                startHour = String.format("%04d", leg.get(i).getStartHour());
                finishHour = String.format("%04d", leg.get(i).getFinishHour());
                hours += " " + startHour + "-" + finishHour;
            }
        }

        String resultMinDec;
        int minDecimal = ((hFlight % 100) * 100) / 60;
        if (minDecimal < 10) {
            resultMinDec = "0" + minDecimal;
        } else {
            resultMinDec = "" + minDecimal;
        }
        sol += "\n" + date + "\n" + hoursFlight + (hFlight / 100) + " hours " + (hFlight % 100) + " minutes" + " ("
                + (hFlight / 100) + "." + resultMinDec + " hrs )" + "\n\n" + nLines + "\n" + sections + "\n" + hours
                + "\n\n";
        System.out.println(sol);
        return sol;
    }

    public static void saveSolution(String solution, String archive) {
        try {
            FileWriter flS = new FileWriter(archive, true);
            BufferedWriter fS = new BufferedWriter(flS);
            fS.write(solution);
            fS.close();
        } catch (IOException e) {
            System.out.println("Error E/S en fichero escritura");
        }
    }

    public static int calcFlightHours(int startHour, int finishHour) {
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
    public static int calcDifMin(int earlyHour, int FinalHour) {
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

}
