/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.ManagedBean;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import APS.Session.FleetSessionBeanLocal;
import Administration.Entity.ProfitAndLoss;
import Administration.Session.AircraftProductivitySessionBeanLocal;
import Administration.Session.ProfitAndLossSessionBeanLocal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Yunna
 */
@Named(value = "adminReportManagedBean")
@ManagedBean
@SessionScoped
public class AdminReportManagedBean {

    @EJB
    private ProfitAndLossSessionBeanLocal pnLSessionBean;

    @EJB
    private FleetSessionBeanLocal fleetSessionBean;

    @EJB
    private AircraftProductivitySessionBeanLocal aircraftProductivitySessionBean;

    private String selectedMonth;
    private ProfitAndLoss selectedPnL;
    private List<AircraftType> aircraftTypes = new ArrayList<AircraftType>();
    private List<Aircraft> aircrafts = new ArrayList<Aircraft>();
    private Aircraft selectedAircraft;
    private String yearsUsed;
    private String daysUsed;
    private String distanceCovered;
    private String totalTravelTime;
    private String totalProfit;
    private Date today;
    private String todayInString;

    private double airportRental;
    private double employeeSalaries;
    private double airportTaxes;
    private double commission;
    private double fuelCosts;
    private double aircraftAcquired;
    private double totalExpenses;

    public AdminReportManagedBean() {
    }

    @PostConstruct
    public void retrieve() {

        setAircraftTypes(fleetSessionBean.retrieveAircraftTypes());
        setAircrafts(fleetSessionBean.retrieveAircrafts());
    }

    public String getStatement() {

        setSelectedPnL(null);

        for (int i = 0; i < selectedMonth.length(); i++) {
            if (i != 2) {
                if (!(selectedMonth.charAt(i) >= '0' && selectedMonth.charAt(i) <= '9')) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid date entered!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }

            }
        }
        int month = Integer.parseInt(selectedMonth.substring(0, 2));

        if (!(month >= 1 && month <= 12) || selectedMonth.length() != 7 || selectedMonth.charAt(2) != '/') {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Date should be in MM/YYYY format!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        int year = Integer.parseInt(selectedMonth.substring(3, 7));
        String dateFormat = "01" + month + year;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");

        try {
            Date date = formatter.parse(dateFormat);

            if (pnLSessionBean.getPnLbyDate(date) == null) {

                setSelectedPnL(pnLSessionBean.createProfitAndLoss(date));
            } else {
                setSelectedPnL(pnLSessionBean.getPnLbyDate(date));
                pnLSessionBean.updateProfitAndLoss(selectedPnL);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        if (selectedPnL.getSalesRevenue() == 0) {
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Statement is not ready for that month!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return null;
//        }

//        setSelectedMonth(null);
        return "GeneratePnLStatement";

    }

    public String getExpenses() {

        setSelectedPnL(null);

        for (int i = 0; i < selectedMonth.length(); i++) {
            if (i != 2) {
                if (!(selectedMonth.charAt(i) >= '0' && selectedMonth.charAt(i) <= '9')) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid date entered!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }

            }
        }
        int month = Integer.parseInt(selectedMonth.substring(0, 2));

        if (!(month >= 1 && month <= 12) || selectedMonth.length() != 7 || selectedMonth.charAt(2) != '/') {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Date should be in MM/YYYY format!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        int year = Integer.parseInt(selectedMonth.substring(3, 7));
        String dateFormat = "01" + month + year;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");

        try {
            Date date = formatter.parse(dateFormat);

            if (pnLSessionBean.getPnLbyDate(date) == null) {

                setSelectedPnL(pnLSessionBean.createProfitAndLoss(date));
            } else {
                setSelectedPnL(pnLSessionBean.getPnLbyDate(date));
                pnLSessionBean.updateProfitAndLoss(selectedPnL);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        setAirportRental(selectedPnL.getAirportRental());
        setEmployeeSalaries(selectedPnL.getEmployeeSalaries());
        setAirportTaxes(selectedPnL.getAirportTaxes());
        setCommission(selectedPnL.getCommission());
        setFuelCosts(selectedPnL.getFuelCosts());
        setAircraftAcquired(selectedPnL.getAircrafts());
        setTotalExpenses(selectedPnL.getTotalExpenses());

//        setSelectedMonth(null);
        return "GenerateExpenses";
    }

    public String getFleetAsset() {

        return "GenerateFleetAssetReport";
    }

    public String viewProductivity(String tailNo) {

        selectedAircraft = fleetSessionBean.getAircraft(tailNo);

        setToday(new Date());

        // Calculate years and days used
        setDaysUsed(aircraftProductivitySessionBean.calculateYearsUsed(selectedAircraft));
        double yearsInDouble = Double.parseDouble(daysUsed) / 365;
        DecimalFormat df = new DecimalFormat("#.00");
        String yearsFormatted = df.format(yearsInDouble);
        setYearsUsed(yearsFormatted);

        // Calculate total distance covered
        double totalDistance = aircraftProductivitySessionBean.calculateTotalDistance(selectedAircraft);
        String distanceFormatted = df.format(totalDistance);
        setDistanceCovered(distanceFormatted);

        // Calculate total travel time
        double totalTime = aircraftProductivitySessionBean.calculateTotalTravelTime(selectedAircraft);
        String timeFormatted = df.format(totalTime);
        setTotalTravelTime(timeFormatted);

        // Calculate total profit
        double profit = aircraftProductivitySessionBean.calculateTotalProfit(selectedAircraft);
        String profitFormatted = df.format(profit);
        setTotalProfit(profitFormatted);

        return "GenerateAircraftProductivityReport";
    }

    //Change the format of displayed dates
    public String getDateFormat(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
        return df.format(date);
    }

    public String printPnL() {

        FacesContext context = FacesContext.getCurrentInstance();
        DecimalFormat df = new DecimalFormat("$#,###.00");

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("DATE", selectedMonth);
            session.setAttribute("REVENUE", df.format(selectedPnL.getSalesRevenue()));
            session.setAttribute("SALARIES", df.format(selectedPnL.getEmployeeSalaries()));
            session.setAttribute("RENTALS", df.format(selectedPnL.getAirportRental()));
            session.setAttribute("TAXES", df.format(selectedPnL.getAirportTaxes()));
            session.setAttribute("COMMISSIONS", df.format(selectedPnL.getCommission()));
            session.setAttribute("FUELCOST", df.format(selectedPnL.getFuelCosts()));
            session.setAttribute("AIRCRAFT", df.format(selectedPnL.getAircrafts()));
            session.setAttribute("TOTALREVENUE", df.format(selectedPnL.getTotalRevenue()));
            session.setAttribute("TOTALEXPENSES", df.format(selectedPnL.getTotalExpenses()));
            session.setAttribute("TOTALPNL", df.format(selectedPnL.getTotalPnL()));
            
            request.setAttribute("type", "PnL"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }
        return "GeneratePnLStatement";
    }

    public String printExpenses() {

        FacesContext context = FacesContext.getCurrentInstance();
        DecimalFormat df = new DecimalFormat("#,###.00");
        
        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("DATE", selectedMonth);
            session.setAttribute("RENTAL", df.format(airportRental));
            session.setAttribute("TAX", df.format(airportTaxes));
            session.setAttribute("SALARY", df.format(employeeSalaries));
            session.setAttribute("COMMISSION", df.format(commission));
            session.setAttribute("FUEL", df.format(fuelCosts));
            session.setAttribute("AIRCRAFT", df.format(aircraftAcquired));
            session.setAttribute("TOTALEX", df.format(totalExpenses));

            request.setAttribute("type", "expenses"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }
        return "GenerateExpenses";
    }

    public String printProductivity(String tailNo) {

        FacesContext context = FacesContext.getCurrentInstance();

        selectedAircraft = fleetSessionBean.getAircraft(tailNo);

        // Calculate years and days used
        setDaysUsed(aircraftProductivitySessionBean.calculateYearsUsed(selectedAircraft));
        double yearsInDouble = Double.parseDouble(daysUsed) / 365;
        DecimalFormat df = new DecimalFormat("#.00");
        String yearsFormatted = df.format(yearsInDouble);
        setYearsUsed(yearsFormatted);

        // Calculate total distance covered
        double totalDistance = aircraftProductivitySessionBean.calculateTotalDistance(selectedAircraft);
        String distanceFormatted = df.format(totalDistance);
        setDistanceCovered(distanceFormatted);

        // Calculate total travel time
        double totalTime = aircraftProductivitySessionBean.calculateTotalTravelTime(selectedAircraft);
        String timeFormatted = df.format(totalTime);
        setTotalTravelTime(timeFormatted);

        // Calculate total profit
        double profit = aircraftProductivitySessionBean.calculateTotalProfit(selectedAircraft);
        String profitFormatted = df.format(profit);
        setTotalProfit(profitFormatted);

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("AIRCRAFT", selectedAircraft.getTailNo());
            session.setAttribute("AIRCRAFTTYPE", selectedAircraft.getAircraftType().getId());
            session.setAttribute("YEARS", yearsUsed);
            session.setAttribute("DAYS", daysUsed);
            session.setAttribute("DISTANCE", distanceCovered);
            session.setAttribute("TIME", totalTravelTime);
            session.setAttribute("PROFIT", totalProfit);
            session.setAttribute("SPEED", df.format(selectedAircraft.getAircraftType().getSpeed() * 1062));
            session.setAttribute("RNG", Double.toString(selectedAircraft.getAircraftType().getTravelRange()));
            session.setAttribute("FIRST", Integer.toString(selectedAircraft.getAircraftType().getFirstSeats()));
            session.setAttribute("BIZ", Integer.toString(selectedAircraft.getAircraftType().getBusinessSeats()));
            session.setAttribute("ECONS", Integer.toString(selectedAircraft.getAircraftType().getEconomySeats()));
            session.setAttribute("STAFF", Integer.toString(selectedAircraft.getAircraftType().getTotalStaff()));
            session.setAttribute("CABIN", Integer.toString(selectedAircraft.getAircraftType().getNumCabin()));
            session.setAttribute("COCKPIT", Integer.toString(selectedAircraft.getAircraftType().getNumCockpit()));
            session.setAttribute("FUEL", Double.toString(selectedAircraft.getAircraftType().getFuelCost()));
            session.setAttribute("COST", Double.toString(selectedAircraft.getAircraftType().getCost()));

            request.setAttribute("type", "productivity"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }
        return "RequestAircraftProductivityReport";
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public ProfitAndLoss getSelectedPnL() {
        return selectedPnL;
    }

    public void setSelectedPnL(ProfitAndLoss selectedPnL) {
        this.selectedPnL = selectedPnL;
    }

    public List<AircraftType> getAircraftTypes() {
        return aircraftTypes;
    }

    public void setAircraftTypes(List<AircraftType> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public Aircraft getSelectedAircraft() {
        return selectedAircraft;
    }

    public void setSelectedAircraft(Aircraft selectedAircraft) {
        this.selectedAircraft = selectedAircraft;
    }

    public String getYearsUsed() {
        return yearsUsed;
    }

    public void setYearsUsed(String yearsUsed) {
        this.yearsUsed = yearsUsed;
    }

    public String getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(String distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public String getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(String totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getDaysUsed() {
        return daysUsed;
    }

    public void setDaysUsed(String daysUsed) {
        this.daysUsed = daysUsed;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public double getAirportRental() {
        return airportRental;
    }

    public void setAirportRental(double airportRental) {
        this.airportRental = airportRental;
    }

    public double getEmployeeSalaries() {
        return employeeSalaries;
    }

    public void setEmployeeSalaries(double employeeSalaries) {
        this.employeeSalaries = employeeSalaries;
    }

    public double getAirportTaxes() {
        return airportTaxes;
    }

    public void setAirportTaxes(double airportTaxes) {
        this.airportTaxes = airportTaxes;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getFuelCosts() {
        return fuelCosts;
    }

    public void setFuelCosts(double fuelCosts) {
        this.fuelCosts = fuelCosts;
    }

    public double getAircraftAcquired() {
        return aircraftAcquired;
    }

    public void setAircraftAcquired(double aircraftAcquired) {
        this.aircraftAcquired = aircraftAcquired;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    /**
     * @return the todayInString
     */
    public String getTodayInString() {
        return todayInString;
    }

    /**
     * @param todayInString the todayInString to set
     */
    public void setTodayInString(String todayInString) {
        this.todayInString = todayInString;
    }

}
