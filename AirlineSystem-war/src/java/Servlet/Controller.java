/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import APS.Session.DemandForecastSessionBeanLocal;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
            long id = (long) request.getSession().getAttribute("ID");
            String origin = (String) request.getSession().getAttribute("ORIGIN");
            String dest = (String) request.getSession().getAttribute("DEST");
            double[] data = (double[]) request.getSession().getAttribute("DATA");
            System.out.println(year + ", " + id + ", " + origin + ", " + dest);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + origin + "-" + dest + " " + year + " Demand Forcast.pdf\"");
            ServletOutputStream outputStream = response.getOutputStream();

            //Set up the parameters
            HashMap parameters = new HashMap();
            parameters.put("IMAGEPATH", "http://localhost:8080/AirlineSystem-war/JasperReports/flower1.png");
            parameters.put("YEAR", year);
            parameters.put("ID", id);

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
