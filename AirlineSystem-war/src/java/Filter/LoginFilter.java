/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import java.util.logging.LogRecord;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author smu
 */
@WebFilter(urlPatterns = {"*.xhtml"})
public class LoginFilter implements Filter {

    private String userName;
    private String timeoutPage = "/AirlineSystem-war/Login.xhtml";
    //FacesContext context = event.getFacesContext();
    //ExternalContext ex = context.getExternalContext();

    public LoginFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession ses = req.getSession(false);
            String reqURI = req.getRequestURI();
            String[] url = reqURI.split("/");
            String redURl = url[0] + "/" + url[1] + "/" + "Login.xhtml";
            System.out.println("url0: " + url[0] + "url1: " + url[1] + "url: " + url[2]);

            if (url[2].equals("Login.xhtml") || reqURI.contains("javax.faces.resource") || url[2].equals("ForgetPassword.xhtml") || url[2].equals("APS")

                   || url[2].equals("CI")||url[2].equals("CRM") || url[2].equals("FOS" )|| url[2].equals("Inventory" ) || url[2].equals("Distribution")
                   || url[2].equals("DCS")/*Delete when necessary*/) {

                System.out.println("Process ");
                chain.doFilter(request, response);

            } else if (!url[2].equals("Login.xhtml")  && (ses != null && ses.getAttribute("isLogin") != null)) {

                if (ses.getAttribute("role").equals("SUPER ADMIN")) { /* Super Admin could access all web pages*/
                    chain.doFilter(request, response);
                } else if(url[3].equals("EmployeeDashBoard.xhtml") || url[3].equals("EmployeeProfile.xhtml") || url[3].equals("Message.xhtml") || url[3].equals("ChangeProfile.xhtml") || url[3].equals("NewUserChangePwd.xhtml") || url[3].equals("EmployeeChangePwd.xhtml")){
                    chain.doFilter(request, response);
                }
                else if((url[3].equals("CreateRole.xhtml") || url[3].equals("CreateDepartment.xhtml") || url[3].equals("DeleteRole.xhtml"))&&(ses.getAttribute("role").equals("SUPER ADMIN") || ses.getAttribute("department").equals("IT") )){
                    chain.doFilter(request, response);
                } else if((url[3].equals("AddRole.xhtml") || url[3].equals("EmployeeManagement.xhtml") || url[3].equals("EmployeeInformation.xhtml") || url[3].equals("DeleteEmployeeRole.xhtml") || url[3].equals("ChangeDepartment.xhtml") )&&(ses.getAttribute("role").equals("SUPER ADMIN") || ses.getAttribute("department").equals("HR"))){
                    chain.doFilter(request, response);
                }

//                } else if (url[3].equals("employeeProfile.xhtml") && ses.getAttribute("isLogin") != null) {
//                    chain.doFilter(request, response);
//                } else if (url[3].equals("message.xhtml") && ses.getAttribute("isLogin") != null) {
//                    chain.doFilter(request, response);
//                } else if(url[3].equals("changeProfile.xhtml")){
//                    chain.doFilter(request, response);
//                }
                else {
                    res.sendError(401);/*Unauthorized Page*/
                }

            } else {
                System.out.println("Redirct to");
                res.sendRedirect(redURl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public boolean isLoggable(LogRecord record) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getTimeoutPage() {
        return timeoutPage;
    }

    public void setTimeoutPage(String timeoutPage) {
        this.timeoutPage = timeoutPage;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
