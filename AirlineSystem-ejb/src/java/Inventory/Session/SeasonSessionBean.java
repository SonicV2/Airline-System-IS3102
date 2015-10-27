/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import APS.Entity.Location;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Inventory.Entity.Season;
import java.util.Date;
import javax.persistence.Query;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author YiQuan
 */
@Stateless
public class SeasonSessionBean implements SeasonSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    public List<Season> getSeasons(){
        Query q = em.createQuery("SELECT o from Season o");
        List<Season> sList= q.getResultList();
        return sList;
    }

    public String addSeason(Date start, Date end, boolean origin, boolean destination,
    String demand, Location location, String remarks) {
        Query q = em.createQuery("SELECT o from Season o WHERE o.location = :location");
        q.setParameter("location", location);
        List<Season> sList= q.getResultList();
        if(!sList.isEmpty()){
            for(int i =0; i<sList.size(); i++){
                Season s = sList.get(i);
                if((origin==true&&s.isOrigin()) || (destination==true&&s.isDestination())){
                    if(start.before(s.getEnd()) || end.after(s.getStart())){
                        return "There is an overlap of dates for the specific location";
                    }
                }
            }
        }
        Season season = new Season();
        season.createSeason(start, end, origin, destination, demand, location, remarks);
        em.persist(season);
        return "Season Demand Added";
    }
    
    public String addCountrySeason(Date start, Date end, boolean origin, boolean destination,
    String demand, String remarks,String country){
        Query q = em.createQuery("SELECT o FROM Location o WHERE o.country = :country");
        q.setParameter("country", country);
        List<Location> lList= q.getResultList();
        int size = lList.size();
        if(size==0)
            return "Enter a Valid Country";
        for(int i=0; i<size; i++){
            Location location = lList.get(i);
            this.addSeason(start, end, origin, destination, demand, location, remarks);
        }
         return "Season Demand Added";
    }
    
    public void editSeason(Season season){
        em.merge(season);
    }
    
    public void deleteSeason(Season season){
        Season season1 = em.find(Season.class, season.getId());
        em.remove(season1);
    }
    
    public Season findSeason(Long id){
        return em.find(Season.class, id);
    }
    
    public Location findLocation(String IATA){
        Query q = em.createQuery("SELECT l from Location l WHERE l.IATA = :IATA");
        q.setParameter("IATA", IATA);
        List<Location> lList= q.getResultList();
        if(lList.isEmpty())
            return null;
        return lList.get(0);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
