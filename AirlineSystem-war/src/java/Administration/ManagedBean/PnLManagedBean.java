/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.ManagedBean;

import Administration.Entity.ProfitAndLoss;
import Administration.Session.ProfitAndLossSessionBeanLocal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Yunna
 */
@Named(value = "pnLManagedBean")
@ManagedBean
@SessionScoped
public class PnLManagedBean {

    @EJB
    private ProfitAndLossSessionBeanLocal pnLSessionBean;

    private String selectedMonth;
    private ProfitAndLoss selectedPnL;
    
    public PnLManagedBean() {
    }
    
    public String getStatement() {
        
        setSelectedPnL(null);
        
        System.out.println("In ManagedBean.......... month: " + selectedMonth);

        for (int i = 0; i < selectedMonth.length(); i++) {
            if (i != 2) {
                if (!(selectedMonth.charAt(i) >= '0' && selectedMonth.charAt(i) <= '9')) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid date entered!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return null;
                }

            }
        }
        int month = Integer.parseInt(selectedMonth.substring(0, 2));
        
        if (!(month >= 1 && month <= 12) || selectedMonth.length() != 7 || selectedMonth.charAt(2) != '/') {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Date should be in MM/YYYY format!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }
        
        int year = Integer.parseInt(selectedMonth.substring(3, 7));
        String dateFormat = "01" + month + year;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        
        try {
            Date date = formatter.parse(dateFormat);
            
            if (pnLSessionBean.getPnLbyDate(date) == null) {
                
                setSelectedPnL(pnLSessionBean.createProfitAndLoss(date));
                
            }
            
            else {
                setSelectedPnL(pnLSessionBean.getPnLbyDate(date));
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (selectedPnL.getSalesRevenue() == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Statement is not ready for that month!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        setSelectedMonth(null);
        
        return "GeneratePnLStatement";
        
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public ProfitAndLoss getSelectedPnL() {
        return selectedPnL;
    }

    public void setSelectedPnL(ProfitAndLoss selectedPnL) {
        this.selectedPnL = selectedPnL;
    }

    
}
