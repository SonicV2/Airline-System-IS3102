package managedbean;

import entity.Employee;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import stateless.session.EmployeeSessionBeanLocal;

@ManagedBean
//@RequestScoped
@SessionScoped
@Named(value="loginManageBean")
public class LoginManageBean {
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    String employeeUserName;
    String employeePassword;
    Employee employee;
    String doLogInMsg;
    boolean logInCheck;
    boolean firstTimer;
    
    public LoginManageBean() {
    }
    
      public String check() {
      
      
       doLogin(employeeUserName,employeePassword);
        
            if(logInCheck==true) {
                if(firstTimer==true){
                    return "newUserChangePwd" + "?faces-redirect=true";
                }else{
                    return "employeeDashBoard" + "?faces-redirect=true";
                }
            } 
            RequestContext.getCurrentInstance().update("growl");
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"LoginFail",doLogInMsg));
            return ""; 
    }   
    
    
    public void doLogin(String employeeUserName, String employeePassword)
    {
        
        employee=employeeSessionBean.getEmployee(employeeUserName);
        firstTimer=false;
        if(employeeUserName.equals("") && employeePassword.equals("") )
        {
            doLogInMsg= "Please Enter your User Name and Password!";
            logInCheck=false;
        }else{if(employee==null){
                doLogInMsg= "Invaild Employee Name!";
                logInCheck=false;
            }else if(employeeSessionBean.isSameHash(employeeUserName, employeePassword)){
                doLogInMsg = employee.getEmployeeDisplayLastName();
                logInCheck=true;
                if(employee.isEmployeeAccountActivate()==false){
                    firstTimer=true;
                }
            }else {doLogInMsg= "Invaild Password!";
                    logInCheck=false;
            }
        }
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
    
    
    
    
    
}