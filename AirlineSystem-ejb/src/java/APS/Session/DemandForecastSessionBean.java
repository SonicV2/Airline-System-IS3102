/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Flight;
import APS.Entity.Forecast;
import APS.Entity.ForecastEntry;
import APS.Entity.Route;
import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yanlong
 */
@Stateless
public class DemandForecastSessionBean implements DemandForecastSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private List<Flight> flights;
    private Route route;
    private Schedule schedule;
    private SeatAvailability sa;
    private List<Schedule> schedules;
    private Forecast dForecast;
    private List<Forecast> forecasts;
    private ForecastEntry forecastEntry;
    private List<ForecastEntry> forecastEntries;
    private int[] demand;
    //Create comparator for sorting of Schedules according to starting time
    private Comparator<Schedule> comparator = new Comparator<Schedule>() {
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
    public void generateDemandForecast(Long routeId, int year, int period, boolean isUpdate) {
        route = getRoute(routeId);
        flights = route.getFlights();
        schedule = new Schedule();
        schedules = new ArrayList<Schedule>();
        sa = new SeatAvailability();
        forecastEntries = new ArrayList<ForecastEntry>();
        dForecast = new Forecast();
        if (isUpdate) {
            dForecast = hasForecast(year, routeId);
            forecastEntries = dForecast.getForecastEntry();
        }
        dForecast.createForecastReport(year);
        demand = new int[24]; //Historical demand for the past 24 full months
        double[] result = new double[36];
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);

        //Add all the schedules that the flights fly
        for (Flight flight : flights) {
            for (int j = 0; j < flight.getSchedule().size(); j++) {
                schedules.add(flight.getSchedule().get(j));
            }
        }

        //Sort Schedules according to startTime and remove schedules such that only 24 full months of schedules are left
        Collections.sort(schedules, comparator);
        //Set up calendar
        tmp.set(Calendar.DATE, 1);
        tmp.set(Calendar.HOUR, 0);
        tmp.set(Calendar.MINUTE, 0);
        tmp.set(Calendar.SECOND, 0);
        tmp.set(Calendar.MILLISECOND, 0);

        //Remove schedules for the current incomplete month
        schedules = removeScheduleAfter(schedules, tmp.getTime());
        //Remove schedules before 24 full months ago
        tmp.add(Calendar.MONTH, -25);
        tmp.add(Calendar.SECOND, -1);
        schedules = removeScheduleBefore(schedules, tmp.getTime());

        int counter = 0;
        //Add the daily demand to demand arraylist
        for (int i = 0; i < 24; i++) {
            int totalBooked = 0;
            schedule = schedules.get(0); //Save current Schedule
            counter = findLastScheduleOfMonth(schedules, schedule.getStartDate());
            System.out.println(counter);
            forecastEntry = new ForecastEntry();
            //Add the demand for all schedules of a month
            for (int j = 0; j <= counter; j++) {
                sa = schedules.get(j).getSeatAvailability();
                totalBooked += sa.getBusinessBooked() + sa.getEconomyBasicBooked() + sa.getEconomyPremiumBooked() + sa.getEconomySaverBooked() + sa.getFirstClassBooked();
            }
            demand[i] = totalBooked;
            if (isUpdate) {
                forecastEntries.get(i).createEntry(schedule.getStartDate(), 0.0, Double.valueOf(totalBooked));
            } else {
                forecastEntry.createEntry(schedule.getStartDate(), 0.0, Double.valueOf(totalBooked));
                forecastEntries.add(forecastEntry);
            }
            totalBooked = 0;
            schedules = removeScheduleBeforeIndex(schedules, counter);
        }

        result = forecast(demand, period, 12);
        tmp.set(Calendar.YEAR, year);
        tmp.set(Calendar.DAY_OF_MONTH, 1);

        //Create the forecast entries
        for (int i = 0; i < 36; i++) {
            forecastEntry = new ForecastEntry();

            if (i > 23) {
                tmp.set(Calendar.MONTH, i - 24);
                if (isUpdate) {
                    forecastEntry = forecastEntries.get(i);
                    forecastEntry.createEntry(tmp.getTime(), result[i], 0.0);
                } else {
                    forecastEntry.createEntry(tmp.getTime(), result[i], 0.0);
                    forecastEntries.add(forecastEntry);
                }
            } else {
                forecastEntry = forecastEntries.get(i);
                forecastEntry.setForecastValue(result[i]);
            }
            forecastEntry.setForecast(dForecast);
            if (isUpdate) {
                em.merge(forecastEntry);
            } else {
                em.persist(forecastEntry);
            }
        }

        dForecast.setForecastEntry(forecastEntries);

        if (isUpdate) {
            em.merge(dForecast);
            em.flush();
        } else {
            dForecast.setRoute(route);
            em.persist(dForecast);
        }
    }

//    @Override
//    public void deleteForecastEntries(Long forecastId) {
//        dForecast = getForecast(forecastId);
//        forecastEntries = dForecast.getForecastEntry();
//        System.out.println(forecastEntries.size());
//        dForecast.setForecastEntry(null);
//
//        for (int i = 0; i < forecastEntries.size(); i++) {
//            forecastEntries.get(i).setForecast(null);
//            System.out.println(forecastEntries.get(i));
//            em.remove(forecastEntries.get(i));
//        }
//        em.flush();
//    }
    @Override
    public Forecast getForecast(Long forecastId) {
        dForecast = new Forecast();
        try {

            Query q = em.createQuery("SELECT a FROM Forecast " + "AS a WHERE a.forecastId=:forecastId");
            q.setParameter("forecastId", forecastId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                dForecast = (Forecast) results.get(0);

            } else {
                dForecast = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return dForecast;
    }

    //Get all the existing schedules
    @Override
    public List<Forecast> getForecasts() {
        forecasts = new ArrayList<Forecast>();
        try {

            Query q = em.createQuery("SELECT a FROM Forecast a");

            List<Forecast> results = q.getResultList();
            if (!results.isEmpty()) {
                forecasts = results;

            } else {
                forecasts = null;
                System.out.println("No Forecasts Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return forecasts;
    }

    @Override
    public Forecast hasForecast(int forecastYear, Long routeId) {
        dForecast = new Forecast();
        try {

            Query q = em.createQuery("SELECT a FROM Forecast " + "AS a WHERE a.forecastYear=:forecastYear AND a.route.routeId=:routeId");
            q.setParameter("forecastYear", forecastYear);
            q.setParameter("routeId", routeId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                dForecast = (Forecast) results.get(0);

            } else {
                dForecast = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return dForecast;
    }

    //Get the whether the 
    @Override
    public boolean isValidYear(Long routeId) {
        route = getRoute(routeId);
        flights = route.getFlights();
        schedules = new ArrayList<Schedule>();
        for (int i = 0; i < flights.size(); i++) {
            schedules.addAll(flights.get(i).getSchedule());
        }

        Collections.sort(schedules, comparator);//Sort the schedules from database
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        Date currTime = tmp.getTime();
        tmp.setTime(schedules.get(0).getStartDate()); //Set time to the earliest schedule for the route
        tmp.add(Calendar.MONTH, 25);
        if (currTime.after(tmp.getTime())) {
            return true;
        }
        return false;
    }

    private Route getRoute(Long id) {
        route = new Route();
        try {

            Query q = em.createQuery("SELECT a FROM Route " + "AS a WHERE a.routeId=:routeId");
            q.setParameter("routeId", id);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                route = (Route) results.get(0);

            } else {
                route = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return route;
    }

    //Returns a list of Schedule objects with start date after the given date
    private List<Schedule> removeScheduleBefore(List<Schedule> sc, Date date) {
        schedules = new ArrayList<Schedule>();
        for (int i = 0; i < sc.size(); i++) {
            if (sc.get(i).getStartDate().after(date)) {
                schedules.add(sc.get(i));
            }
        }
        return schedules;
    }

    //Get all the existing schedules
    private List<Schedule> getSchedules() {
        schedules = new ArrayList<Schedule>();
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

    //Returns a list of Schedule objects after a index
    private List<Schedule> removeScheduleBeforeIndex(List<Schedule> sc, int index) {
        schedules = new ArrayList<Schedule>();
        if (index < sc.size() - 1) {  //Does not check if index is last index of the arraylist
            for (int i = index + 1; i < sc.size(); i++) {
                schedules.add(sc.get(i));
            }
        }
        return schedules;
    }

    //Returns a list of Schedule objects with start date before the given date
    private List<Schedule> removeScheduleAfter(List<Schedule> sc, Date date) {
        schedules = new ArrayList<Schedule>();
        for (int i = 0; i < sc.size(); i++) {
            if (sc.get(i).getStartDate().before(date)) {
                schedules.add(sc.get(i));
            }
        }
        return schedules;
    }

    //Returns last index of the schedule within the same month as input
    //Precond: Schedule array is sorted, more than 1 month schedule available
    private int findLastScheduleOfMonth(List<Schedule> sc, Date date) {
        int result = 0;
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar currTime = Calendar.getInstance(tz);
        Calendar tmp = Calendar.getInstance(tz);

        //Make Time to 00:00 of the first day of the next month for comparing
        currTime.setTime(date);
        currTime.add(Calendar.MONTH, 1);
        currTime.set(Calendar.DATE, 1);
        currTime.set(Calendar.HOUR, 0);
        currTime.set(Calendar.MINUTE, 0);
        currTime.set(Calendar.SECOND, 0);
        currTime.set(Calendar.MILLISECOND, 0);

        //Find the index of the last schedule of the month 
        for (int i = 0; i < sc.size(); i++) {
            tmp.setTime(sc.get(i).getStartDate());
            if (tmp.after(currTime) || tmp.equals(currTime)) {
                result = i - 1;
                break;  //Stop searching after result is found
            }
        }
        return result;
    }

    @Override
    public void testForecast() {
        int[] data = {42, 24, 32, 37, 46, 29, 37, 43, 49, 31, 38, 40, 51, 32, 41, 44, 56, 34, 42, 46, 60, 35, 44, 48};
        double[] result = forecast(data, 4, 12);
        //Print Array
        System.out.println("result:(");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + ", ");
        }
        System.out.println(")");
    }

    //Takes in the relevant inputs and forecasts the demand
    private double[] forecast(int[] histData, int period, int numForecasted) {

        double[] optimizedParams = new double[3];
        optimizedParams = optimizeParam(histData, period, 0.1d, 0.0d, 0.0d, 0.0d);
        //Call method again with different inputs to get 2 decimal point forecast parameters
        optimizedParams = optimizeParam(histData, period, 0.01d, optimizedParams[0], optimizedParams[1], optimizedParams[2]);
        DecimalFormat df = new DecimalFormat("0.##");
        double alpha = Double.valueOf(df.format(optimizedParams[0]));
        double beta = Double.valueOf(df.format(optimizedParams[1]));
        double gamma = Double.valueOf(df.format(optimizedParams[2]));

        System.out.println("Params:" + alpha + "," + beta + "," + gamma);
        return holtWintersForecast(histData, alpha, beta, gamma, period, numForecasted);
    }

    //Using the holt winters triple exponential smoothing, this method returns a double array of forecasted values
    private double[] holtWintersForecast(int[] histData, double alpha,
            double beta, double gamma, int period, int numForecasted) {

        //Set Decimal Format to 2dp
        DecimalFormat df = new DecimalFormat("0.##");

        // Initialize the function arrays
        double[] Lt = new double[histData.length];
        double[] Bt = new double[histData.length];
        double[] St = new double[histData.length];
        double[] Ft = new double[histData.length + numForecasted];

        //Initialize the base values
        int seasons = histData.length / period; //Number of seasons in the data
        Lt[period - 1] = histData[0];
        Bt[period - 1] = calculateInitialTrend(histData, period);
        double[] initialSeasonalIndices = calculateSeasonalIndices(histData, period, seasons);

        //Add the initial season indices to St function array
        for (int i = 0; i < period; i++) {
            St[i] = initialSeasonalIndices[i];
        }

        //Start calculations for predicted values
        for (int i = period; i < histData.length; i++) {

            //Calculate level smoothing, Lt
            Lt[i] = alpha * (histData[i] / St[i - period]) + (1.0 - alpha) * (Lt[i - 1] + Bt[i - 1]);

            //Calculate trend smoothing, Bt
            Bt[i] = beta * (Lt[i] - Lt[i - 1]) + (1.0 - beta) * Bt[i - 1];

            //Calculate seasonal smoothing, St
            St[i] = gamma * histData[i] / Lt[i] + (1.0 - gamma) * St[i - period];

            //Calculate forecast
            Ft[i] = Double.valueOf(df.format(Lt[i] + Bt[i] * St[i - period]));
        }

        //Forecast requested future values
        for (int i = 1; i <= numForecasted; i++) {
            int h = (int) Math.floor((i - 1) % period + 1);
            Ft[histData.length - 1 + i] = Double.valueOf(df.format((Lt[histData.length - 1] + i * Bt[histData.length - 1]) * St[histData.length - 1 - period + h]));
        }

        return Ft;
    }

    //Calculates the initial trend value for the holt winters forecast
    private double calculateInitialTrend(int[] y, int period) {

        double sum = 0;

        for (int i = 0; i < period; i++) {
            sum += (y[period + i] - y[i]);
        }

        return sum / (period * period);
    }

    //Calculates the seasonal indices for the holt winters forecast
    private double[] calculateSeasonalIndices(int[] y, int period, int seasons) {

        double[] seasonalAverage = new double[seasons];
        double[] seasonalIndices = new double[period];

        double[] averagedObservations = new double[y.length];

        for (int i = 0; i < seasons; i++) {
            for (int j = 0; j < period; j++) {
                seasonalAverage[i] += y[(i * period) + j];
            }
            seasonalAverage[i] /= period;
        }

        for (int i = 0; i < seasons; i++) {
            for (int j = 0; j < period; j++) {
                averagedObservations[(i * period) + j] = y[(i * period) + j] / seasonalAverage[i];
            }
        }

        for (int i = 0; i < period; i++) {
            for (int j = 0; j < seasons; j++) {
                seasonalIndices[i] += averagedObservations[(j * period) + i];
            }
            seasonalIndices[i] /= seasons;
        }

        return seasonalIndices;
    }

    private double[] optimizeParam(int[] histData, int period, double multiplyer, double baseA, double baseB, double baseC) {

        double currModel[] = new double[histData.length];
        double currSSE = 0.0;
        double[] result = {baseA, baseB, baseC}; //Set the result as the initial value first
        //Set the minSSE to the first SSE at alpha = baseNumber, beta = baseNumber, gamma = baseNumber
        double minSSE = calcSSE(histData, holtWintersForecast(histData, baseA, baseB, baseC, period, 0));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    currModel = holtWintersForecast(histData, baseA + i * multiplyer, baseB + j * multiplyer, baseC + k * multiplyer, period, 0);
                    currSSE = calcSSE(histData, currModel);
                    if (currSSE < minSSE) {
                        minSSE = currSSE;
                        result[0] = i * multiplyer;
                        result[1] = j * multiplyer;
                        result[2] = k * multiplyer;
                    }
                }
            }
        }

        return result;
    }

    //Calculates the Sum of Squared Errors for 2 datasets in double array form
    private double calcSSE(int[] original, double[] forecast) {
        double SSE = 0.0;
        for (int i = 0; i < original.length; i++) {
            SSE += Math.pow(original[i] - forecast[i], 2);
        }
        return SSE;
    }
}
