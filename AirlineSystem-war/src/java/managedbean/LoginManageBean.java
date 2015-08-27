/*package managedbean;

import entity.Employee;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import stateless.session.EmployeeSessionBeanLocal;

@ManagedBean
@RequestScoped
@Named(value="loginManageBean")
public class LoginManageBean {
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    
    String employeeID;
    
    public LoginManageBean() {
    }
    public String doLogin(String employeeID, String employeePassword)
    {
        Employee employee=employeeSessionBean.getEmployee(employeeID);
        if(employee==null){
            return "Employee not found!";
        }else if(employee.getEmployeePassword().equals(employeePassword)){
            return employee.getEmployeeDisplayFirstName();
        }else return "Invaild Password";
    }
    
    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }
    String employeePassword;
    
    
    
    
}
*/