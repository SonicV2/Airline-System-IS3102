/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Managedbean;

import APS.Entity.Location;
import Inventory.Entity.BookingClass;
import Inventory.Session.SeasonSessionBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import Inventory.Entity.Season;
import java.util.Calendar;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author YiQuan
 */
@Named(value = "seasonManageBean")
@ManagedBean
@SessionScoped
public class SeasonManageBean {

    /**
     * Creates a new instance of seasonManageBean
     */
    @EJB
    private SeasonSessionBeanLocal sm;
    private Date start;
    private Date end;
    private boolean origin;
    private boolean destination;
    private String demand;
    private Location location;
    private String remarks;
    private String IATA;
    private String country;
    private List<Season> sList;
    private HashMap hm;
    private int month;
    private int year;
    
    
    @PostConstruct
    public void init() {
        setsList(sm.getSeasons());
        Date now= new  Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
    }
    
    public void onRowEdit(RowEditEvent event) {
        Season edited = (Season)event.getObject();
              
            sm.editSeason(edited);
            FacesMessage msg = new FacesMessage("Season Edited" );
            FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


    
    public void deleteSeason(Season season){
        sm.deleteSeason(season);
        setsList(sm.getSeasons());
    }
    
    public String refresh(){
        return "DisplaySeasons";
    }
    
    public String getGeoArray()
    {   
        hm = new HashMap<String, Integer>();
        int size = sList.size();
        String results=" var results = [";
        results += "['Country', 'Popularity']";
        List <String> countries = new ArrayList();
       
        for (int i = 0; i < size; i++) {
            Season season = sList.get(i);
            Date startDate = season.getStart();
            Date endDate = season.getEnd();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int from = cal.get(Calendar.MONTH);
            cal.setTime(endDate);
            int end = cal.get(Calendar.MONTH);
            int seasonYear = cal.get(Calendar.YEAR);           
            if(from<=month && month<=end && year==seasonYear){
            String country =season.getLocation().getCountry();
            if (hm.get(country) != null) {
                int count=0; 
                if(season.getDemand().equals("High"))
                    count=1;
                if(season.getDemand().equals("Low"))
                    count= -1;
                
                int score = (int)hm.get(country);
                score += count;          
                hm.put(country, score);

            } else {
                hm.put(country, 0);
                countries.add(country);
            }
            }
        }
         
        if (countries.isEmpty()){
            results +="]";
            return results;
        }
        
        else{
        results+=",";
        for(int i= 0; i< countries.size()-1; i++){
            results += "[";
            results += "'"+countries.get(i)+"'"+"," + hm.get(countries.get(i)).toString();
            results += "],";
        }
        results += "[";
        results += "'"+countries.get(countries.size()-1)+"'"+"," + hm.get(countries.get(countries.size()-1)).toString();
        results += "]";
        results += "]";
        return results;
        }
    }

    

    public void addSeason() {
        if (start == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter Start Date", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (end == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter End Date", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (origin == false && destination == false) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Indicate Origin or Destination", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (demand==null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter Season Demand", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (remarks.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter Remarks", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            location = sm.findLocation(IATA);
            if (location == null && country == null) {
                FacesMessage msg = new FacesMessage("Please Enter a Valid IATA code");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
            if (country == null) {
            String message = sm.addSeason(start, end, origin, destination, demand, location, remarks);
            FacesMessage msg = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            
            if (location == null){
            String message = sm.addCountrySeason(start, end, origin, destination, demand, remarks, country);
            FacesMessage msg = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            
        }
    }

    public List<Season> getsList() {
        return sList;
    }

    public void setsList(List<Season> sList) {
        this.sList = sList;
    }

    
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public HashMap getHm() {
        return hm;
    }

    public void setHm(HashMap hm) {
        this.hm = hm;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    

    public boolean isOrigin() {
        return origin;
    }

    public void setOrigin(boolean origin) {
        this.origin = origin;
    }

    public boolean isDestination() {
        return destination;
    }

    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    


}
