/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Managedbean;

import CI.Entity.AccessRight;
import CI.Session.AccessRightSessionBeanLocal;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Yuqing
 */

@Named(value = "accessRightManagedBean")
@ManagedBean
@ViewScoped
public class AccessRightManagedBean {

    @EJB
    private AccessRightSessionBeanLocal accessRightSession;
    private String accessRightName;
    private String accessRights;

    private List<AccessRight> allAccessRights;
    private List<AccessRight> selectedAccessRights;
    
    public AccessRightManagedBean(){
        
    }
    
    @PostConstruct
    public void init(){
        setAllAccessRights(accessRightSession.retrieveAllAccessRight());
    }
    
    public String addAccessRight(){
        
        
//        List<String> listOfAccessRights = Arrays.asList(accessRights.split("\\s*,\\s*"));
//        accessRightSession.addAccessRight(accessRightName, listOfAccessRights);
       accessRightSession.addAccessRight(accessRightName);
        setAccessRightName("");
//        setAccessRights("");
        setAllAccessRights(accessRightSession.retrieveAllAccessRight());
        return "manageAccessRight";
        
    }

    /**
     * @return the accessRightName
     */
    public String getAccessRightName() {
        return accessRightName;
    }

    /**
     * @param accessRightName the accessRightName to set
     */
    public void setAccessRightName(String accessRightName) {
        this.accessRightName = accessRightName;
    }

    /**
     * @return the allAccessRights
     */
    public List<AccessRight> getAllAccessRights() {
        return allAccessRights;
    }

    /**
     * @param allAccessRights the allAccessRights to set
     */
    public void setAllAccessRights(List<AccessRight> allAccessRights) {
        this.allAccessRights = allAccessRights;
    }

    /**
     * @return the accessRights
     */
    public String getAccessRights() {
        return accessRights;
    }

    /**
     * @param accessRights the accessRights to set
     */
    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }

    /**
     * @return the selectedAccessRights
     */
    public List<AccessRight> getSelectedAccessRights() {
        return selectedAccessRights;
    }

    /**
     * @param selectedAccessRights the selectedAccessRights to set
     */
    public void setSelectedAccessRights(List<AccessRight> selectedAccessRights) {
        this.selectedAccessRights = selectedAccessRights;
    }


    
    
}
