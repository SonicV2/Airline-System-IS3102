/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import APS.Entity.Location;
import Inventory.Entity.Season;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author YiQuan
 */
@Local
public interface SeasonSessionBeanLocal {

    public String addSeason(Date start, Date end, boolean origin, boolean destination,
            String demand, Location location, String remarks);

    public void editSeason(Season season);

    public void deleteSeason(Season season);
    
    public Location findLocation(String IATA);
    
    public String addCountrySeason(Date start, Date end, boolean origin, boolean destination,
    String demand, String remarks,String country);
    
    public List<Season> getSeasons();
    
    public Season findSeason(Long id);
}
