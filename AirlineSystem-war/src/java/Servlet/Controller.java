/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import APS.Session.DemandForecastSessionBeanLocal;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Yanlong
 */
@WebServlet(name = "Controller", urlPatterns = {"/Controller", "/Controller?*"})
public class Controller extends HttpServlet {

    @Resource(name = "airlineSystemDataSource")
    private DataSource airlineSystemDataSource;

    @EJB
    DemandForecastSessionBeanLocal demandForecastSessionBean;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String source = (String) request.getAttribute("type");
            System.out.println(source);
            if (source.equals("demand")) {
                System.out.println(source);
                printForecastPDF(request, response);
            }
            if (source.equals("PnL")) {
                printPnLPDF(request, response);
            }
            if (source.equals("expenses")) {
                printExpensesPDF(request, response);
            }
            if (source.equals("productivity")) {
                printProductivityPDF(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printForecastPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/DemandForecastReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            int year = (int) request.getSession().getAttribute("YEAR");
            Long id = (Long) request.getSession().getAttribute("ID");
            String origin = (String) request.getSession().getAttribute("ORIGIN");
            String dest = (String) request.getSession().getAttribute("DEST");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + origin + "-" + dest + " " + year + " Demand Forcast.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

//            //Create a temporary table for the printing of the graph
//            String tempTableName = "TEMPTTT";
//            String query = "CREATE TEMPORARY TABLE " + tempTableName + " (MONTH int NOT NULL, "
//                    + "DEMAND double, PRIMARY KEY (MONTH))";
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            ResultSet rs = null;
//            Statement stmt = null;
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/AirlinesDB", "root", "password");
//            stmt = connection.createStatement();
//            stmt.executeUpdate(query);
//            for (int i = 1; i <= 36; i++) {
//                query = "INSERT INTO " + tempTableName + " VALUES (" + i + ", " + data[i - 1] + ")";
//                stmt.executeUpdate(query);
//            }
            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
//            parameters.put("YEAR", year);
            parameters.put("FORECASTID", 592337L);
            System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, airlineSystemDataSource.getConnection());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printPnLPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/ProfitAndLossStatement.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            String date = (String) request.getSession().getAttribute("MONTH");
            double salesRevenue = (double) request.getSession().getAttribute("REVENUE");
            double employeeSalaries = (double) request.getSession().getAttribute("SALARIES");
            double airportRental = (double) request.getSession().getAttribute("RENTALS");
            double airportTax = (double) request.getSession().getAttribute("TAXES");
            double commission = (double) request.getSession().getAttribute("COMMISSIONS");
            double fuelCost = (double) request.getSession().getAttribute("FUELCOST");
            double totalRevenue = (double) request.getSession().getAttribute("TOTALREVENUE");
            double totalExpenses = (double) request.getSession().getAttribute("TOTALEXPENSES");
            double totalPnL = (double) request.getSession().getAttribute("TOTALPNL");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + date + " Profit And Loss Statement.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
//            parameters.put("YEAR", year);
            parameters.put("FORECASTID", 592337L);
            System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, airlineSystemDataSource.getConnection());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printExpensesPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/OperatingExpensesReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            String date = (String) request.getSession().getAttribute("DATE");
            double employeeSalaries = (double) request.getSession().getAttribute("SALARY");
            double airportRental = (double) request.getSession().getAttribute("RENTAL");
            double airportTax = (double) request.getSession().getAttribute("TAX");
            double commission = (double) request.getSession().getAttribute("COMMISSION");
            double fuelCost = (double) request.getSession().getAttribute("FUEL");
            double totalExpenses = (double) request.getSession().getAttribute("TOTALEX");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + date + " Operating Expenses Report.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
//            parameters.put("YEAR", year);
            parameters.put("FORECASTID", 592337L);
            System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, airlineSystemDataSource.getConnection());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printProductivityPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/AircrafProductivityReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            Date date = (Date) request.getSession().getAttribute("DATE");
            String tailNo = (String) request.getSession().getAttribute("AIRCRAFT");
            String yearsUsed = (String) request.getSession().getAttribute("YEARS");
            String daysUsed = (String) request.getSession().getAttribute("DAYS");
            String totalDistance = (String) request.getSession().getAttribute("DISTANCE");
            String totalTime = (String) request.getSession().getAttribute("TIME");
            String totalProfit = (String) request.getSession().getAttribute("PROFIT");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "Aircraft: " + tailNo + " Productivity Report.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
//            parameters.put("YEAR", year);
            parameters.put("FORECASTID", 592337L);
            System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, airlineSystemDataSource.getConnection());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
