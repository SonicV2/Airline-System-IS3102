/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import APS.Session.DemandForecastSessionBeanLocal;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
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
import net.sf.jasperreports.engine.JREmptyDataSource;
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
            } else if (source.equals("PnL")) {
                printPnLPDF(request, response);
            } else if (source.equals("expenses")) {
                printExpensesPDF(request, response);
            } else if (source.equals("productivity")) {
                printProductivityPDF(request, response);
            } else if (source.equals("income")) {
                printIncomePDF(request, response);
            } else if (source.equals("equity")) {
                printEquityPDF(request, response);
            } else if (source.equals("balance")) {
                printBalancePDF(request, response);
            } else if (source.equals("journal")) {
                printJournalPDF(request, response);
            } else if (source.equals("baggageTag")) {
                printBaggageTagPDF(request, response);
            } else if (source.equals("boardingPass")) {
                printBoardingPassPDF(request, response);
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
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/web/resources/images/MaLogo.jpg");
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
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/AircraftProductivityReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            String tailNo = (String) request.getSession().getAttribute("AIRCRAFT");
            String yearsUsed = (String) request.getSession().getAttribute("YEARS");
            String daysUsed = (String) request.getSession().getAttribute("DAYS");
            String totalDistance = (String) request.getSession().getAttribute("DISTANCE");
            String totalTime = (String) request.getSession().getAttribute("TIME");
            String totalProfit = (String) request.getSession().getAttribute("PROFIT");
            String aircraftType = (String) request.getSession().getAttribute("AIRCRAFTTYPE");
            String speed = (String) request.getSession().getAttribute("SPEED");
            String rng = (String) request.getSession().getAttribute("RNG");
            String first = (String) request.getSession().getAttribute("FIRST");
            String biz = (String) request.getSession().getAttribute("BIZ");
            String econs = (String) request.getSession().getAttribute("ECONS");
            String staff = (String) request.getSession().getAttribute("STAFF");
            String cabin = (String) request.getSession().getAttribute("CABIN");
            String cockpit = (String) request.getSession().getAttribute("COCKPIT");
            String fuel = (String) request.getSession().getAttribute("FUEL");
            String cost = (String) request.getSession().getAttribute("COST");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "Aircraft-" + tailNo + " Productivity Report.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/MALogo.jpg");
            parameters.put("AIRCRAFTIMG", "http://localhost:8080/AirlineSystem-war/JasperReports/" + aircraftType.replaceAll("\\s","") + ".png");
            parameters.put("tailNo", tailNo);
            parameters.put("yearsUsed", yearsUsed);
            parameters.put("daysUsed", daysUsed);
            parameters.put("totalDistance", totalDistance);
            parameters.put("totalTime", totalTime);
            parameters.put("totalProfit", totalProfit);
            parameters.put("aircraftType", aircraftType);
            parameters.put("speed", speed);
            parameters.put("rng", rng);
            parameters.put("first", first);
            parameters.put("biz", biz);
            parameters.put("econs", econs);
            parameters.put("staff", staff);
            parameters.put("cabin", cabin);
            parameters.put("cockpit", cockpit);
            parameters.put("fuel", fuel);
            parameters.put("cost", cost);
            
            System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, new JREmptyDataSource());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printIncomePDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/DemandForecastReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            double ticketRev = (double) request.getSession().getAttribute("ticket");
            double travelRev = (double) request.getSession().getAttribute("travel");
            double exp = (double) request.getSession().getAttribute("fuel");
            int year = (int) request.getSession().getAttribute("year");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + year + " MAS Income Statement.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
            parameters.put("YEAR", year);
            parameters.put("TICKET", ticketRev);
            parameters.put("TRAVEL", travelRev);
            parameters.put("EXP", exp);
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, new JREmptyDataSource());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printEquityPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/DemandForecastReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            double currRet = (double) request.getSession().getAttribute("currRet");
            double prevRet = (double) request.getSession().getAttribute("prevRet");
            double income = (double) request.getSession().getAttribute("income");
            int year = (int) request.getSession().getAttribute("year");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + year + " MAS Equity Statement.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
            parameters.put("YEAR", year);
            parameters.put("CURR", currRet);
            parameters.put("PREV", prevRet);
            parameters.put("INCOME", income);
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, new JREmptyDataSource());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printBalancePDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/DemandForecastReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            double cash = (double) request.getSession().getAttribute("cash");
            double acctRecv = (double) request.getSession().getAttribute("acctRecv");
            double prepaid = (double) request.getSession().getAttribute("prepaid");
            double pne = (double) request.getSession().getAttribute("pne");
            double acctPay = (double) request.getSession().getAttribute("acctPay");
            double unearned = (double) request.getSession().getAttribute("unearned");
            double retained = (double) request.getSession().getAttribute("retained");
            int year = (int) request.getSession().getAttribute("year");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + year + " MAS Equity Statement.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
            parameters.put("YEAR", year);
            parameters.put("CASH", cash);
            parameters.put("ACCTRECV", acctRecv);
            parameters.put("PREPAID", prepaid);
            parameters.put("PNE", pne);
            parameters.put("ACCTPAY", acctPay);
            parameters.put("UNEARNED", unearned);
            parameters.put("RETAINED", retained);
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, new JREmptyDataSource());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printJournalPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/DemandForecastReport.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            int year = (int) request.getSession().getAttribute("year");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + year + " MAS Equity Statement.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
            parameters.put("YEAR", year);
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

    private void printBaggageTagPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/BaggageTag.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            String id = (String) request.getSession().getAttribute("ID");
            String city = (String) request.getSession().getAttribute("City");
            Date depart = (Date) request.getSession().getAttribute("DepartDate");
            String fNumber = (String) request.getSession().getAttribute("fNumber");
            String service = (String) request.getSession().getAttribute("Service");
            String seqNumber = (String) request.getSession().getAttribute("seqNumber");

            response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/MerlionAirlineLogo.jpg");
            parameters.put("Barcode", "http://localhost:8080/AirlineSystem-war/JasperReports/barcode.png");
            parameters.put("id", id);
            parameters.put("City", city);

            parameters.put("depart", depart);
            parameters.put("fNumber", fNumber);
            parameters.put("service", service);
            parameters.put("seqNumber", seqNumber);

            //System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, new JREmptyDataSource());

            outputStream.flush();
            outputStream.close();
        } catch (JRException jrex) {
            System.out.println("********** Jasperreport Exception");
            jrex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printBoardingPassPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream("/JasperReports/BoardingPass.jasper");
            response.setContentType("application/pdf");

            //Get the relevant information from the request sent
            String fName = (String) request.getSession().getAttribute("fName");
            String lName = (String) request.getSession().getAttribute("lName");
            String dCity = (String) request.getSession().getAttribute("dCity");
            String aCity = (String) request.getSession().getAttribute("aCity");
            Date DepartDate = (Date) request.getSession().getAttribute("DepartDate");
            String fNumber = (String) request.getSession().getAttribute("fNumber");

            String boardTime = (String) request.getSession().getAttribute("boardTime");
            String sClass = (String) request.getSession().getAttribute("sClass");
            String seat = (String) request.getSession().getAttribute("seat");

            String seqNumber = (String) request.getSession().getAttribute("seqNumber");
            String gate = (String) request.getSession().getAttribute("gate");
            String bID = (String) request.getSession().getAttribute("bID");

            response.setHeader("Content-Disposition", "attachment; filename=\"" + bID + ".pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/MerlionAirlineLogo.jpg");
            parameters.put("Barcode", "http://localhost:8080/AirlineSystem-war/JasperReports/barcode.png");
            parameters.put("fName", fName);
            parameters.put("lName", lName);

            parameters.put("dCity", dCity);
            parameters.put("aCity", aCity);
            parameters.put("DepartDate", DepartDate);
            parameters.put("fNumber", fNumber);

            parameters.put("boardTime", boardTime);
            parameters.put("sClass", sClass);
            parameters.put("seat", seat);
            parameters.put("seqNumber", seqNumber);

            parameters.put("gate", gate);

            //System.out.println(reportStream + ", " + outputStream + ", " + airlineSystemDataSource.getConnection());
            //Generate the report
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, new JREmptyDataSource());

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
