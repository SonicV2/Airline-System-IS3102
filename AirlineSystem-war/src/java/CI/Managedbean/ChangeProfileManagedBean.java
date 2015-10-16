/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.EmployeeSessionBeanLocal;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author smu
 */
@Named(value = "changeProfileManagedBean")
@ManagedBean
@RequestScoped
public class ChangeProfileManagedBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    

     @ManagedProperty(value = "#{loginManageBean}")
    private LoginManagedBean loginManageBean;
     
    private String userName;
    private Date employeeDOB;
    private String employeeGender;
    private String employeeHomeAddress;
    private String employeeOfficeNumber;
    private String employeeHpNumber;
    private String employeeEmail;
    private String employeePrivateEmail;
    
 
    private String employeeNewPwd;
    private String employeeNewPwdRe; 

    private FileHandler fh;
   
       
    public void updateInfo(ActionEvent event){
        
        setUserName(loginManageBean.employeeUserName);
        if(employeeDOB==null){
            setEmployeeDOB(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeeDOB());
        }
        if(employeeGender==null){
            setEmployeeGender(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeeGender());
        }
        if(employeeHomeAddress.equals("")){
            setEmployeeHomeAddress(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeeMailingAddress());
        }
        if(employeeOfficeNumber.equals("")){
            setEmployeeOfficeNumber(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeeOfficeNumber());
        }
        if(employeePrivateEmail.equals("")){
            setEmployeePrivateEmail(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeePrivateEmail());
        }
        if(employeeHpNumber.equals("")){
            setEmployeeHpNumber(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeeHpNumber());
        }
        System.out.println("managebean: "+userName);
        System.out.println(employeeDOB+ " "+ employeeGender+ " "+ employeeHomeAddress+ " "+ employeeOfficeNumber+ " "+ employeeHpNumber);
        employeeSessionBean.updateInfo(userName, employeeDOB, employeeGender, employeeHomeAddress, employeeOfficeNumber, employeeHpNumber
                                       ,employeePrivateEmail);
        clear();
        loginManageBean.refresh();
    }
    
    public void clear(){
        setEmployeeDOB(null);
        setEmployeeGender(null);
        setEmployeeHomeAddress("");
        setEmployeeOfficeNumber("");
        setEmployeeHpNumber("");
    }

    //to prepopulate data in change profile managedBean
    @PostConstruct
    public void init() {
        userName = loginManageBean.getEmployee().getEmployeeUserName();
        employeeDOB = loginManageBean.getEmployee().getEmployeeDOB();
        employeeGender = loginManageBean.getEmployee().getEmployeeGender();
        employeeHomeAddress = loginManageBean.getEmployee().getEmployeeMailingAddress();
        employeeOfficeNumber = loginManageBean.getEmployee().getEmployeeOfficeNumber();
        employeeHpNumber = loginManageBean.getEmployee().getEmployeeHpNumber();
        employeeEmail = loginManageBean.getEmployee().getEmployeeEmailAddress();
        employeePrivateEmail = loginManageBean.getEmployee().getEmployeePrivateEmail();
    // Or here, especially if you depend on injected dependencies.
}
    
    public void changePwd(){
       
         FacesMessage message = null;
         String employeeUserName = loginManageBean.employeeUserName;
         if(getEmployeeNewPwd().equals(getEmployeeNewPwdRe())){
            employeeSessionBean.hashNewPwd(employeeUserName, getEmployeeNewPwd());
            employeeSessionBean.employeeActivate(employeeUserName);
            Logger logger = Logger.getLogger(LoginManagedBean.class.getName());
        try {   
        fh = new FileHandler("%h/LogOut.txt",99999,1,true);  
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);  

        } catch (SecurityException e) {  
        e.printStackTrace();  
        } catch (IOException e) {  
        e.printStackTrace();  
        } 
        logger.info("User: "+ employeeUserName 
                + " has changed password");
        fh.close();
          
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Change Successfully!", "");
             FacesContext.getCurrentInstance().addMessage(null, message);
         }else{
           message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Change Failed", "");
             FacesContext.getCurrentInstance().addMessage(null, message);
         }
    }

    
    
    /**
     * Creates a new instance of changeProfileManagedBean
     */
    public ChangeProfileManagedBean() {
    
    }

    /**
     * @return the employeeDOB
     */
    public Date getEmployeeDOB() {
        return employeeDOB;
    }

    /**
     * @param employeeDOB the employeeDOB to set
     */
    public void setEmployeeDOB(Date employeeDOB) {
        this.employeeDOB = employeeDOB;
    }

    /**
     * @return the employeeGender
     */
    public String getEmployeeGender() {
        return employeeGender;
    }

    /**
     * @param employeeGender the employeeGender to set
     */
    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    /**
     * @return the employeeHomeAddress
     */
    public String getEmployeeHomeAddress() {
        return employeeHomeAddress;
    }

    /**
     * @param employeeHomeAddress the employeeHomeAddress to set
     */
    public void setEmployeeHomeAddress(String employeeHomeAddress) {
        this.employeeHomeAddress = employeeHomeAddress;
    }

    /**
     * @return the employeeOfficeNumber
     */
    public String getEmployeeOfficeNumber() {
        return employeeOfficeNumber;
    }

    /**
     * @param employeeOfficeNumber the employeeOfficeNumber to set
     */
    public void setEmployeeOfficeNumber(String employeeOfficeNumber) {
        this.employeeOfficeNumber = employeeOfficeNumber;
    }

    /**
     * @return the employeeHpNumber
     */
    public String getEmployeeHpNumber() {
        return employeeHpNumber;
    }

    /**
     * @param employeeHpNumber the employeeHpNumber to set
     */
    public void setEmployeeHpNumber(String employeeHpNumber) {
        this.employeeHpNumber = employeeHpNumber;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePrivateEmail() {
        return employeePrivateEmail;
    }

    public void setEmployeePrivateEmail(String employeePrivateEmail) {
        this.employeePrivateEmail = employeePrivateEmail;
    }
    
    

    /**
     * @return the employeeSessionBean
     */
    public EmployeeSessionBeanLocal getEmployeeSessionBean() {
        return employeeSessionBean;
    }

    /**
     * @param employeeSessionBean the employeeSessionBean to set
     */
    public void setEmployeeSessionBean(EmployeeSessionBeanLocal employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
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

    /**
     * @return the loginManageBean
     */
    public LoginManagedBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManagedBean loginManageBean) {
        this.loginManageBean = loginManageBean;
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
     * @return the employeeNewPwdRe
     */
    public String getEmployeeNewPwdRe() {
        return employeeNewPwdRe;
    }

    /**
     * @param employeeNewPwdRe the employeeNewPwdRe to set
     */
    public void setEmployeeNewPwdRe(String employeeNewPwdRe) {
        this.employeeNewPwdRe = employeeNewPwdRe;
    }

}
