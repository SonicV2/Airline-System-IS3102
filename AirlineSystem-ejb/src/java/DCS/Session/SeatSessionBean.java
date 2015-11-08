/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Schedule;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author smu
 */
@Stateless
public class SeatSessionBean implements SeatSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<String> retrieveOccupiedSeats(Schedule schedule) {
        List<String> results = new ArrayList<String>();

        List<String> first = schedule.getSelectedSeatsF();
        List<String> business = schedule.getSelectedSeatsB();
        List<String> economy = schedule.getSelectedSeatsE();
        
       
//        List<String> temp=new ArrayList<String>();
//        temp.add("A11");
//        
//        schedule.setSelectedSeatsE(temp);
//        em.merge(schedule);
        
        
        if (first.isEmpty()) {
          
        } else {
            for (String s : first) {
                results.add(s);
            }
        }

        if (business.isEmpty()) {
            
        } else {
            for (String s :business) {
                results.add(s);
            }
        }

        if (economy.isEmpty()) {
            
        } else {
            for (String s : economy) {
                results.add(s);
            }
        }
        

        return results;
    }
    
    
    @Override
    public void inputChosenE(Schedule schedule,String seat){
        List<String> seats=schedule.getSelectedSeatsE();
        
        if(seats.contains(seat)){
            System.out.println("duplicated seat");
        }else{
            seats.add(seat);
            schedule.setSelectedSeatsE(seats);
            em.merge(schedule);
            
            
            
        }
    }
    @Override
    public void inputChosenB(Schedule schedule,String seat){
        List<String> seats=schedule.getSelectedSeatsB();
        
        if(seats.contains(seat)){
            System.out.println("duplicated seat");
        }else{
            seats.add(seat);
            
            schedule.setSelectedSeatsB(seats);
            em.merge(schedule);
        }
    }
    @Override
    public void inputChosenF(Schedule schedule,String seat){
        List<String> seats=schedule.getSelectedSeatsF();
        
        if(seats.contains(seat)){
            System.out.println("duplicated seat");
        }else{
            seats.add(seat);

            schedule.setSelectedSeatsF(seats);
            em.merge(schedule);
        }
    }

}
