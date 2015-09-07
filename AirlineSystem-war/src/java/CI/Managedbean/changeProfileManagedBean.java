/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Session.EmployeeSessionBeanLocal;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "changeProfileManagedBean")
@ManagedBean
@RequestScoped
public class changeProfileManagedBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    

     @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;
     
    private String userName;
    private Date employeeDOB;
    private String employeeGender;
    private String employeeHomeAddress;
    private String employeeOfficeNumber;
    private String employeeHpNumber;
    
 
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
        if(employeeHpNumber.equals("")){
            setEmployeeHpNumber(employeeSessionBean.getEmployee(loginManageBean.employeeUserName).getEmployeeHpNumber());
        }
        System.out.println("managebean: "+userName);
        System.out.println(employeeDOB+ " "+ employeeGender+ " "+ employeeHomeAddress+ " "+ employeeOfficeNumber+ " "+ employeeHpNumber);
        employeeSessionBean.updateInfo(userName, employeeDOB, employeeGender, employeeHomeAddress, employeeOfficeNumber, employeeHpNumber);
    }

    /**
     * Creates a new instance of changeProfileManagedBean
     */
    public changeProfileManagedBean() {
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
    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManageBean loginManageBean) {
        this.loginManageBean = loginManageBean;
    }

}
