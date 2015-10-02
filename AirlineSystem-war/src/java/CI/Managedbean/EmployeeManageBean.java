package CI.Managedbean;

import CI.Entity.Employee;
import CI.Session.EmployeeSessionBean;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import CI.Session.EmployeeSessionBeanLocal;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
//@RequestScoped
@Named(value = "employeeManageBean")
public class EmployeeManageBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;

    public EmployeeSessionBeanLocal getEmployeeSessionBean() {
        return employeeSessionBean;
    }

    String employeeID;
    String employeeUserName;
    String employeeDisplayFirstName;
    String employeeDisplayLastName;
    String employeeRole;
    String employeeDepartment;

    String employeeGender;
    String employeeHpNumber;
    String employeeMailingAddress;
    String employeeOfficeNumber;
    String employeeEmailAddress;
    Employee employee;

    String employeePrivateEmail;

    private String employeeNewPwd;
    private String employeeNewPwdRe;

    //SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
    Date employeeDOB;

    //For Cabin Crew and pilot
    private String experience;
    private String position;
    private List<String> languages; //cabin crew

    private String firstLang;
    private String secondLang;
    private List<String> skills; //pilot eg. licence to fly a380 etc

    private boolean pwdChangeStatus;
    
    //For Logging purposes
    private FileHandler fh;
    private String userID;
   

    public EmployeeManageBean() {
    }

    public void addEmployee(ActionEvent event) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);


        if (session.getAttribute("isLogin") == null) {
            userID = "SUPER ADMIN ";
        } else {
            userID = session.getAttribute("isLogin").toString();

        }

        employeeSessionBean.addEmployee(userID, employeeID, employeeDisplayFirstName, employeeDisplayLastName, employeeRole,
                employeeDepartment, employeeDOB, employeeGender, employeeHpNumber,
                employeeMailingAddress, employeeOfficeNumber, employeePrivateEmail);

        employee = getEmployee(employeeID); //in order to get the employeeUserName and email which is generated after the creation of employee
        employeeUserName = employee.getEmployeeUserName();
        employeeEmailAddress = employee.getEmployeeEmailAddress();
        employeeSessionBean.hashPwd(employeeID);

        Logger logger = Logger.getLogger(EmployeeManageBean.class.getName());
        try {   
        fh = new FileHandler("%h/addEmployee.txt",99999,1,true); 
        logger.addHandler(fh);     
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  
        
        } catch (SecurityException e) {  
        e.printStackTrace();
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ userID 
                + " has added Employee: " + employeeID);
     
        fh.close();
        //clearAll();

    }

    //clear all input after keying in
    public String clearAll() {
        setEmployeeID("");
        setEmployeeDisplayFirstName("");
        setEmployeeDisplayLastName("");
        setEmployeeDOB(null);
        setEmployeeGender("");
        setEmployeeHpNumber("");
        setEmployeeMailingAddress("");
        setEmployeeDepartment("");
        setEmployeeRole("");
        setEmployeeOfficeNumber("");
        setEmployeePrivateEmail("");
        setExperience("");
        setLanguages(null);
        setPosition("");
        setSkills(null);
        setFirstLang("");
        setSecondLang("");

        return "employeeDashBoard";

    }

    public void addCabinCrew(ActionEvent event) {
        setEmployeeRole("Cabin Crew");
        languages = new ArrayList<String>();
        languages.add(firstLang);
        languages.add(secondLang);
        employeeSessionBean.addCabinCrew(employeeID, employeeDisplayFirstName, employeeDisplayLastName, employeeDepartment, employeeDOB, employeeGender, employeeHpNumber,
                employeeMailingAddress, employeeOfficeNumber, employeePrivateEmail, experience, languages, position);

        employee = getEmployee(employeeID);
        employeeUserName = employee.getEmployeeUserName();
        employeeEmailAddress = employee.getEmployeeEmailAddress();
        employeeSessionBean.hashPwd(employeeID);
        
        Logger logger = Logger.getLogger(EmployeeManageBean.class.getName());
        try {   
        fh = new FileHandler("%h/addCabinCrew.txt",99999,1,true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        } catch (SecurityException e) {  
        e.printStackTrace();  
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ userID 
                + " has added Cabin Crew: " + employeeID);
        fh.close();

    }

    public void addPilot(ActionEvent event) {
        setEmployeeRole("Pilot");
        employeeSessionBean.addPilot(employeeID, employeeDisplayFirstName, employeeDisplayLastName, employeeDepartment, employeeDOB, employeeGender, employeeHpNumber,
                employeeMailingAddress, employeeOfficeNumber, employeePrivateEmail, experience, skills, position);

        employee = getEmployee(employeeID);
        employeeUserName = employee.getEmployeeUserName();
        employeeEmailAddress = employee.getEmployeeEmailAddress();
        employeeSessionBean.hashPwd(employeeID);
        
         
        Logger logger = Logger.getLogger(EmployeeSessionBean.class.getName());
        try {   
        fh = new FileHandler("%h/addPilot.txt",99999,1,true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        } catch (SecurityException e) {  
        e.printStackTrace();  
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ userID 
                + " has added Pilot: " + employeeID);
        fh.close();
    }

    public String changePwd() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        //String userID= session.getAttribute("isLogin").toString();
        FacesMessage message = null;
        String employeeUserName = loginManageBean.employeeUserName;
        if (employeeNewPwd.equals(employeeNewPwdRe)) {
            employeeSessionBean.hashNewPwd(employeeUserName, employeeNewPwd);
            employeeSessionBean.employeeActivate(employeeUserName);
            pwdChangeStatus = true;            
            Logger logger = Logger.getLogger(EmployeeManageBean.class.getName());
            try {   
            fh = new FileHandler("%h/changePassword.txt",99999,1,true);  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  

            } catch (SecurityException e) {  
            e.printStackTrace();  
            } catch (IOException e) {  
            e.printStackTrace();  
            } 
            logger.info("User: "+ employeeUserName
                    + " has changed password: ");
            fh.close();

            return "/login.xhtml" + "?faces-redirect=true";

        } else {
            pwdChangeStatus = false;

            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Fail", "");

            FacesContext.getCurrentInstance().addMessage(null, message);

            return "newUserChangePwd";

        }
    }

    public Employee getEmployee(String employeeID) {
        Employee employee = employeeSessionBean.getEmployeeUseID(employeeID);
        return employee;
    }

    public void setEmployeeSessionBean(EmployeeSessionBeanLocal employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }

    public String getEmployeeDisplayLastName() {
        return employeeDisplayLastName;
    }

    public void setEmployeeDisplayLastName(String employeeDisplayLastName) {
        this.employeeDisplayLastName = employeeDisplayLastName;
    }

    public String getEmployeeMailingAddress() {
        return employeeMailingAddress;
    }

    public void setEmployeeMailingAddress(String employeeMailingAddress) {
        this.employeeMailingAddress = employeeMailingAddress;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeUserName() {
        return employeeUserName;
    }

    public void setEmployeeUserName(String employeeUserName) {
        this.employeeUserName = employeeUserName;
    }

    public String getEmployeeDisplayFirstName() {
        return employeeDisplayFirstName;
    }

    public void setEmployeeDisplayFirstName(String employeeDisplayFirstName) {
        this.employeeDisplayFirstName = employeeDisplayFirstName;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public Date getEmployeeDOB() {
        return employeeDOB;
    }

    public void setEmployeeDOB(Date employeeDOB) {
        this.employeeDOB = employeeDOB;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    public String getEmployeeHpNumber() {
        return employeeHpNumber;
    }

    public void setEmployeeHpNumber(String employeeHpNumber) {
        this.employeeHpNumber = employeeHpNumber;
    }

    public String getEmployeeOfficeNumber() {
        return employeeOfficeNumber;
    }

    public void setEmployeeOfficeNumber(String employeeOfficeNumber) {
        this.employeeOfficeNumber = employeeOfficeNumber;
    }

    public String getEmployeeEmailAddress() {
        return employeeEmailAddress;
    }

    public void setEmployeeEmailAddress(String employeeEmailAddress) {
        this.employeeEmailAddress = employeeEmailAddress;
    }

    public String getEmployeePrivateEmail() {
        return employeePrivateEmail;
    }

    public void setEmployeePrivateEmail(String employeePrivateEmail) {
        this.employeePrivateEmail = employeePrivateEmail;
    }

    /**
     * @return the employeeNewPwd
     */
    public String getEmployeeNewPwd() {
        return employeeNewPwd;
    }

    /**
     * @param employeeNewPwd the employeeNewPwd to set
     */
    public void setEmployeeNewPwd(String employeeNewPwd) {
        this.employeeNewPwd = employeeNewPwd;
    }

    /**
     * @return the loginManageBean
     */
    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManageBean loginManageBean) {
        this.loginManageBean = loginManageBean;
    }

    public String getEmployeeNewPwdRe() {
        return employeeNewPwdRe;
    }

    public void setEmployeeNewPwdRe(String employeeNewPwdRe) {
        this.employeeNewPwdRe = employeeNewPwdRe;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getFirstLang() {
        return firstLang;
    }

    public void setFirstLang(String firstLang) {
        this.firstLang = firstLang;
    }

    public String getSecondLang() {
        return secondLang;
    }

    public void setSecondLang(String secondLang) {
        this.secondLang = secondLang;
    }

}
