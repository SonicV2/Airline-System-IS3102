/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Entity.Employee;
import CI.Entity.Role;
import CI.Session.EmployeeSessionBeanLocal;
import CI.Session.RoleSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author HOULIANG
 */
@Named(value = "roleManagedBean")
@ManagedBean
@ViewScoped
public class RoleManagedBean {
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    @EJB
    private RoleSessionBeanLocal roleSessionBean;

    
    String roleName; //fro admin create
    
    String role; //for receiving value from employeeManagement.xhtml
    
    private List<String> roles=new ArrayList();
     
    String userID; //NRIC
    Employee employee;
    String userName; //username
    String name; //full name
    List<String> newroles; // List<String> newroles
            
    String msg;
    
    String new_Role; // user input to set new rows
    
    
    public RoleManagedBean() {
    }
    
    public void search(ActionEvent event){
        newroles=new ArrayList<String>(); //to return all the current roles 
        employee=employeeSessionBean.getEmployeeUseID(userID);
        if(employee==null){
            newroles.add("no such user!");
            //msg="no such user!";
        }else{
            userName=employee.getEmployeeUserName();
            name=employee.getEmployeeDisplayFirstName()+" "+employee.getEmployeeDisplayLastName();
            List<Role> r =employee.getRoles();
            for(Role r1: r){
                newroles.add(r1.getRoleName());
            }
        }
        
        
    }
    
    

    //for admin to create new roles
    public void addRole(ActionEvent event){
        roleSessionBean.addRole(roleName);
    }
    
    
     @PostConstruct
    public void retrive(){
        setRoles(roleSessionBean.retrive());
    }
    
    
    //add new role to existing employee
    public void addNewRole(){
        roleSessionBean.addNewRole(employee,new_Role);
    
    }
    
    
    public RoleSessionBeanLocal getRoleSessionBean() {
        return roleSessionBean;
    }

    public void setRoleSessionBean(RoleSessionBeanLocal roleSessionBean) {
        this.roleSessionBean = roleSessionBean;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

   

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public EmployeeSessionBeanLocal getEmployeeSessionBean() {
        return employeeSessionBean;
    }

    public void setEmployeeSessionBean(EmployeeSessionBeanLocal employeeSessionBean) {
        this.employeeSessionBean = employeeSessionBean;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNewroles() {
        return newroles;
    }

    public void setNewroles(List<String> newroles) {
        this.newroles = newroles;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNew_Role() {
        return new_Role;
    }

    public void setNew_Role(String new_Role) {
        this.new_Role = new_Role;
    }

    
    
}
