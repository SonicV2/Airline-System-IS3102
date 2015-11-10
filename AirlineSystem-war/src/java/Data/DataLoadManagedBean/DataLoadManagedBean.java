/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.DataLoadManagedBean;

import Data.Session.DataLoadSessionBeanLocal;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author parthasarthygupta
 */
@Named(value = "dataLoadManagedBean")
@ManagedBean
@SessionScoped
public class DataLoadManagedBean {

    @EJB
    private DataLoadSessionBeanLocal dataLoadSessionBean;
    
    FacesMessage message = null;

   
    public DataLoadManagedBean() {
    }
    
    public String loadData(){
        dataLoadSessionBean.init();
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Loaded successfully!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null;
    }

}
