package CI.Managedbean;

import CI.Entity.AccessRight;
import CI.Entity.Employee;
import CI.Entity.Role;
import CI.Session.EmployeeSessionBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import CI.Session.EmployeeSessionBeanLocal;
import CI.Session.RoleSessionBeanLocal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
//@ViewScoped
//@RequestScoped
@SessionScoped
@Named(value = "loginManageBean")
public class LoginManageBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @EJB
    private RoleSessionBeanLocal roleSessionBean;

    String employeeUserName;
    String employeePassword;
    Employee employee;
    String doLogInMsg;
    boolean logInCheck;
    boolean firstTimer;
    HttpServletRequest req;
    String roles; // to get all roles in this string
    private List<Long> accessRightIDs; //get a list of access right IDs from employee
    private List<AccessRight> accessRightsForRole;
    
    private FileHandler fh;
    private String userID;
    

    public LoginManageBean() {
    }


    public String check() {

        doLogin(employeeUserName, employeePassword);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (logInCheck == true) {
            userID= employeeUserName;
            session.setAttribute("isLogin", employeeUserName);
            
        Logger logger = Logger.getLogger(LoginManageBean.class.getName());
        try {   
        fh = new FileHandler("%h/CI/LogIn.txt",99999,1,true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        } catch (SecurityException e) {  
        e.printStackTrace();  
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ userID 
                + "has logged in");
        fh.close();

            if (firstTimer == true) {

                return "CI/newUserChangePwd" + "?faces-redirect=true";
            } else {
                
                return direct();
            }

        }
        RequestContext.getCurrentInstance().update("growl");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "LoginFail", doLogInMsg));

        session.setAttribute("isLogin", null);
        return "";
    }

    public String direct() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (employee.getOrganizationUnit().getDepartmentName().equals("HR")) {
            session.setAttribute("department", "HR");
            //return "Department/HR" + "?faces-redirect=true";
        } else if (employee.getOrganizationUnit().getDepartmentName().equals("IT")) {
            session.setAttribute("department", "IT");
            // return "Department/IT" + "?faces-redirect=true";
        } else if (employee.getOrganizationUnit().getDepartmentName().equals("Flight Crew")) {
            session.setAttribute("department", "Flight Crew");
        }
                //get employee's access rights into a list of long
                accessRightsForRole = new ArrayList<AccessRight>();
                accessRightsForRole = getEmployee().getRoles().get(0).getAccessRights();

                accessRightIDs = new ArrayList<Long>();
                if (accessRightsForRole != null) {
                    for (AccessRight ar : accessRightsForRole) {
                        accessRightIDs.add(ar.getId());
                    }
                }
        return "CI/employeeDashBoard" + "?faces-redirect=true";
    }

    public void refresh() {
        setEmployee(employeeSessionBean.getEmployee(employeeUserName));
    }

    public void doLogin(String employeeUserName, String employeePassword) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String temp_roles = ""; // to get all roles in this string
        setEmployee(employeeSessionBean.getEmployee(employeeUserName));
        firstTimer = false;

        if (employeeUserName.equals("") && employeePassword.equals("")) {
            doLogInMsg = "Please Enter your User Name and Password!";
            logInCheck = false;
        } else {
            if (getEmployee() == null) {
                doLogInMsg = "Invaild Employee Name!";
                logInCheck = false;
            } else if (employee.isEmployeeLockOut()) {
                doLogInMsg = "Accound has been locked!";
                logInCheck = false;
                System.out.println("look here!");

            } else if (employeeSessionBean.isSameHash(employeeUserName, employeePassword)) {

                List<Role> role = employee.getRoles();
                for (Role r : role) {
                    temp_roles += r.getRoleName() + " ";
                    if (r.getRoleName().equals("SUPER ADMIN")) {
                        session.setAttribute("role", "SUPER ADMIN"); /*Add role --> SuperAdmin*/

                    } else {
                        session.setAttribute("role", "NSA");/*NSA--> Not Super Admin*/

                    }
                }

                roles = temp_roles;

                doLogInMsg = getEmployee().getEmployeeDisplayLastName();
                logInCheck = true;
                if (getEmployee().isEmployeeAccountActivate() == false) {
                    firstTimer = true;
                }
            } else {
                doLogInMsg = "Invaild Password!";
                logInCheck = false;
            }

        }
    }

//    public void isLogin() {
//        
//       if(logInCheck){
//           
//           session.setAttribute("isLogin", true);
//       }else{
//           session.setAttribute("isLogin", false);
//       }
//    }
    public String logout() {
        
        Logger logger = Logger.getLogger(LoginManageBean.class.getName());
        try {   
        fh = new FileHandler("%h/CI/LogOut.txt",99999,1,true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        } catch (SecurityException e) {  
        e.printStackTrace();  
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ userID 
                + "has logged out");
        fh.close();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public String getEmployeeUserName() {
        return employeeUserName;
    }

    public void setEmployeeUserName(String employeeUserName) {
        this.employeeUserName = employeeUserName;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * @return the accessRightIDs
     */
    public List<Long> getAccessRightIDs() {
        return accessRightIDs;
    }

    /**
     * @param accessRightIDs the accessRightIDs to set
     */
    public void setAccessRightIDs(List<Long> accessRightIDs) {
        this.accessRightIDs = accessRightIDs;
    }

    /**
     * @return the accessRightsForRole
     */
    public List<AccessRight> getAccessRightsForRole() {
        return accessRightsForRole;
    }

    /**
     * @param accessRightsForRole the accessRightsForRole to set
     */
    public void setAccessRightsForRole(List<AccessRight> accessRightsForRole) {
        this.accessRightsForRole = accessRightsForRole;
    }

}
