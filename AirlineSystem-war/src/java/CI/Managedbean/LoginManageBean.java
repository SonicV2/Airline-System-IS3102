package CI.Managedbean;

import CI.Entity.Employee;
import CI.Entity.Role;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import CI.Session.EmployeeSessionBeanLocal;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;
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
    
    String employeeUserName;
    String employeePassword;
    Employee employee;
    String doLogInMsg;
    boolean logInCheck;
    boolean firstTimer;
    HttpServletRequest req;
    String roles; // to get all roles in this string

    public LoginManageBean() {
    }

    public String check() {
       
        doLogin(employeeUserName, employeePassword);
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (logInCheck == true) {
             employeeSessionBean.logLogIn(employeeUserName);
            session.setAttribute("isLogin", employeeUserName);
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
    
    public String direct(){
        if(employee.getOrganizationUnit().getdepartmentName().equals("HR")){
            return "Department/HR" + "?faces-redirect=true";
        }else if(employee.getOrganizationUnit().getdepartmentName().equals("IT")){
            return "Department/IT" + "?faces-redirect=true";
        }
        
        return "CI/employeeDashBoard"+ "?faces-redirect=true";
    }
    
    
    public void doLogin(String employeeUserName, String employeePassword) {
        String temp_roles=""; // to get all roles in this string
        setEmployee(employeeSessionBean.getEmployee(employeeUserName));
        firstTimer = false;
        
        List<Role> role=employee.getRoles();
        for(Role r : role){
            temp_roles+=r.getRoleName()+" ";
        }
        
        roles=temp_roles;
        
        if (employeeUserName.equals("") && employeePassword.equals("")) {
            doLogInMsg = "Please Enter your User Name and Password!";
            logInCheck = false;
        } else {
            if (getEmployee() == null) {
                doLogInMsg = "Invaild Employee Name!";
                logInCheck = false;
            }else if(employee.isEmployeeLockOut()){
             doLogInMsg = "Accound has been locked!";
             logInCheck = false;
             System.out.println("look here!");
            
            }else if (employeeSessionBean.isSameHash(employeeUserName, employeePassword)) {
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

    
}
