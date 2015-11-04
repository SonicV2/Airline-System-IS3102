/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import DCS.Entity.BoardingPass;
import DCS.Entity.CheckInRecord;
import Inventory.Entity.Booking;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author HOULIANG
 */
@Stateless
public class BoardingPassSessionBean implements BoardingPassSessionBeanLocal {
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    public static int seq=1;
    
    @Override
    public void addBooking(Booking booking){
        BoardingPass boardingpass = new BoardingPass();
        boardingpass.create();
        
        boardingpass.setBookingID(booking.getId().toString());
        boardingpass.setFirstname(booking.getTravellerFristName());
        boardingpass.setLastname(booking.getTravellerLastName());
        boardingpass.setFlightNumber(booking.getFlightNo());
        boardingpass.setClasstype(booking.getServiceType());
        boardingpass.setArrivalCity(booking.getSeatAvail().getSchedule().getFlight().getRoute().getDestinationCity());
        boardingpass.setDepartCity(booking.getSeatAvail().getSchedule().getFlight().getRoute().getOriginCity());
        boardingpass.setDepartTime(booking.getFlightDate());
        
        Date depart=booking.getFlightDate();
        depart.setTime(depart.getTime() - 1800 * 1000); // half an hour before
        boardingpass.setBoardingTime(depart.toString());
        depart.setTime(depart.getTime() + 1800 * 1000);
        
        boardingpass.setSeqNumber(seq+"");
        seq++;
        
        em.persist(boardingpass);
    }
    
    @Override
    public void addSeat(Booking booking, String seatNo){
        BoardingPass boardingpass= findBoardingpass(booking);
        boardingpass.setSeat(seatNo);
        em.merge(boardingpass);
    }
    
    @Override 
    public BoardingPass findBoardingpass(Booking booking) {
        Query q = em.createQuery("SELECT c FROM BoardingPass c");
        List<BoardingPass> boardingpass = q.getResultList();

        for (BoardingPass b : boardingpass) {
            if ((b.getBookingID()).equals(booking.getId().toString())) {
                return b;
            } else {
                return null;
            }
        }
        return null;
    }
   
    
    
    public void persist(Object object) {
        em.persist(object);
    }

    
}
