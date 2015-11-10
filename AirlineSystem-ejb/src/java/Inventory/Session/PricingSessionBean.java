/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import javax.ejb.Stateless;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import Inventory.Entity.SeatAvailability;
import Inventory.Entity.BookingClass;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import APS.Entity.Flight;
import Inventory.Entity.Booking;
import APS.Entity.Schedule;
import Inventory.Entity.Season;
import javax.persistence.TemporalType;
import APS.Entity.Route;

/**
 *
 * @author YiQuan
 */
@Stateless
public class PricingSessionBean implements PricingSessionBeanLocal, PricingSessionBeanRemote {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    private Date rDate;

    // Generate the amount of seat available which includes overbooking based on demand forecast
    // Currently it is dummy generating availability
  
    public int[] generateAvailability(String flightNo, int economy, int business, int firstClass) {
        if ( calTurnOut(flightNo,"Business").equals("Not Enough Historic Data")
                || calTurnOut(flightNo,"First Class").equals("Not Enough Historic Data")){
        int[] seats = new int[5];
        seats[0] = (economy / 3) + 5;
        seats[1] = (economy / 3) + 5;
        seats[2] = (economy / 3) + 5;
        seats[3] = business + 5;
        seats[4] = firstClass + 5;
        return seats;
        }
        else{
          int[] seats = new int[5];
           double es = Double.valueOf(calTurnOut(flightNo,"Economy Saver"));
           double eb= Double.valueOf(calTurnOut(flightNo,"Economy Basic"));
           double ep = Double.valueOf(calTurnOut(flightNo,"Economy Premium"));
           double b = Double.valueOf(calTurnOut(flightNo,"Business"));
           double fc = Double.valueOf(calTurnOut(flightNo,"First Class"));
        seats[0] = (economy/3)*(100+ (int)es)/100 ;
        seats[1] = (economy/3)*(100+ (int)eb)/100 ;
        seats[2] = (economy/3)*(100+ (int)ep)/100 ;
        seats[3] = (business)*(100+ (int)b)/100 ;
        seats[4] = (firstClass)*(100+ (int)fc)/100 ;
        return seats; 
        }
    }
    
    public String calTurnOut(String flightNo, String serviceType) {
        Flight flight= em.find(Flight.class, flightNo);
        Long routeID = flight.getRoute().getRouteId();
        Query q = em.createQuery("SELECT o FROM Booking o WHERE o.seatAvail.schedule.flight.route.routeId =:routeID AND o.serviceType=:serviceType AND o.flightDate <:date");
        q.setParameter("routeID", routeID);
        q.setParameter("serviceType", serviceType);
        Date date= new Date();
        q.setParameter("date", date,TemporalType.TIMESTAMP);
        List<Booking> bList= q.getResultList();
        int size = bList.size();
        int turnOut=0;
        if(size<100){
            return "Not Enough Historic Data";
        }
        for(int i =0; i< size; i++){
            Booking booking = bList.get(i);
            if (booking.getBookingStatus().equals("Missed")|| booking.getBookingStatus().equals("Cancelled"))
                turnOut++;
        }
        Double result = turnOut*100.0/size;
        String result1 = String.format("%1$,.2f", result);
        return result1;

    }

    //Find the seat availability of a particular flight no and time
    public SeatAvailability findSA(Date fDate, String flightNo) {
        //System.out.println(flightNo);
        Query q = em.createQuery("SELECT o from SeatAvailability o WHERE o.schedule.flight.flightNo = :flightNo");
        q.setParameter("flightNo", flightNo);
        List<SeatAvailability> saList = q.getResultList();
        int size = saList.size();
        SeatAvailability sa = saList.get(0);
        for (int i = 0; i < size; i++) {
            if (saList.get(i).getSchedule().getStartDate().equals(fDate)) {
                sa = saList.get(i);
                //System.out.println("Date Check Successful!");
            }

        }
        return sa;
    }

    /**
     *
     * @param flightNo
     * @param fDate
     * @return
     */
    // Generate the amount of seat available which includes overbooking based on demand forecast
    @Override
    public int[] getAvail(String flightNo, Date fDate) {

        try {

            SeatAvailability sa = findSA(fDate, flightNo);
            int[] avail = new int[10];
            avail[0] = sa.getEconomySaverTotal() - sa.getEconomySaverBooked();
            avail[1] = sa.getEconomyBasicTotal() - sa.getEconomyBasicBooked();
            avail[2] = sa.getEconomyPremiumTotal() - sa.getEconomyPremiumBooked();
            avail[3] = sa.getBusinessTotal() - sa.getBusinessBooked();
            avail[4] = sa.getFirstClassTotal() - sa.getFirstClassBooked();
            avail[5] = (sa.getEconomySaverBooked() * 100) / sa.getEconomySaverTotal();
            avail[6] = (sa.getEconomyBasicBooked() * 100) / sa.getEconomyBasicTotal();
            avail[7] = (sa.getEconomyPremiumBooked() * 100) / sa.getEconomyPremiumTotal();
            avail[8] = (sa.getBusinessBooked() * 100) / sa.getBusinessTotal();
            avail[9] = (sa.getFirstClassBooked() * 100) / sa.getFirstClassTotal();
            return avail;
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error");
            return null;
        }
    }


    // Get the base price of a particular flight number
    public double getBasePrice(String flightNo) {
        Flight flight = em.find(Flight.class, flightNo);
        double baseprice = flight.getBasicFare();
        return baseprice;
    }

    // Get the price of a ticket of a specific flight and service type
    public String getClassCode(Schedule s, String serviceClass, int noPpl, boolean travelagent) {
        int pricePercent = 100;
        SeatAvailability sa = s.getSeatAvailability();
        Date current = new Date();
        int realSold = noPpl;
        if (serviceClass.equals("Economy Saver")) {
            realSold = realSold + sa.getEconomySaverBooked();
            realSold = (realSold * 100) / sa.getEconomySaverTotal();
        }else if (serviceClass.equals("Economy Basic")) {
            realSold = realSold + sa.getEconomyBasicBooked();
            realSold = (realSold * 100) / sa.getEconomyBasicTotal();
        } else if (serviceClass.equals("Economy Premium")) {
            realSold = realSold + sa.getEconomyPremiumBooked();
            realSold = (realSold * 100) / sa.getEconomyPremiumTotal();
        }else if (serviceClass.equals("Business")) {
            realSold = realSold + sa.getBusinessBooked();
            realSold = (realSold * 100) / sa.getBusinessTotal();
        }else if (serviceClass.equals("FirstClass")) {
            realSold = realSold + sa.getFirstClassBooked();
            realSold = (realSold * 100) / sa.getFirstClassTotal();
        }
        try {
            
            Date currentDate = new Date();
            Date fDate = s.getStartDate();
            long days = (fDate.getTime() - current.getTime()) / 1000 / 60 / 60 / 24;
            String currentSeason = "Neutral";

            String origin = s.getFlight().getRoute().getOriginIATA();
            Query q1 = em.createQuery("SELECT o FROM Season o WHERE o.locationIATA = :IATA");
            q1.setParameter("IATA", origin);
            List<Season> seasonList = q1.getResultList();
            if (!seasonList.isEmpty()) {
                for (int i = 0; i < seasonList.size(); i++) {
                    Season season = seasonList.get(i);
                    if (season.isOrigin() && fDate.after(season.getStart()) && fDate.before(season.getEnd()) )
                        currentSeason= season.getDemand();
                }
            }
            
            String destination = s.getFlight().getRoute().getDestinationIATA();
            Query q2 = em.createQuery("SELECT o FROM Season o WHERE o.locationIATA = :IATA");
            q2.setParameter("IATA", destination);
            List<Season> seasonList2 = q2.getResultList();
            if (!seasonList2.isEmpty()) {
                for (int i = 0; i < seasonList2.size(); i++) {
                    Season season2 = seasonList2.get(i);
                    if (season2.isDestination() && fDate.after(season2.getStart()) && fDate.before(season2.getEnd()) ){
                        currentSeason= season2.getDemand();
                    }
                }
            }
            
            
            Query q = em.createQuery("SELECT o FROM BookingClass o WHERE o.serviceClass = :serviceClass AND o.season =:season AND o.travelagent =:travelagent");
            q.setParameter("serviceClass", serviceClass);
            q.setParameter("season", currentSeason);
            q.setParameter("travelagent", travelagent);
            List<BookingClass> classList = q.getResultList();
            int size = classList.size();
            List<BookingClass> updatedList = new ArrayList();

            for (int i = 0; i < size; i++) {
                BookingClass b = classList.get(i);
                int daysOut = b.getAdvancedSales();
                if (days <= daysOut) {
                    updatedList.add(classList.get(i));
                    //System.out.println("Fare Class qualified: " + b.getClasscode());
                }
            }
            
            size = updatedList.size();
            
            BookingClass c = updatedList.get(0);
            for (int i = 0; i < size; i++) {
                BookingClass b = updatedList.get(i);
                int daysOut = b.getAdvancedSales();
                int sold = b.getPercentSold();
                if (b.getPricePercent() <= c.getPricePercent()) {
                    c = b;
                }
            }

            //System.out.println("Lowest Fare Class: " + c.getClasscode());
            for (int i = 0; i < size; i++) {
                BookingClass b = updatedList.get(i);
                int daysOut = b.getAdvancedSales();
                int sold = b.getPercentSold();
                if (realSold >= sold) {
                    c = b;
                    //System.out.println("Higher Class Triggered ");
                }
            }

            //System.out.println("Final Booking Class:" + c.getClasscode());
            return c.getClasscode();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error");
            return "No ClassCode Found";
        }

    }

    public double getPrice(String classCode, Schedule s) {
        BookingClass bc = em.find(BookingClass.class, classCode);
        double price = (bc.getPricePercent() * s.getFlight().getBasicFare()) / 100;
        return price;
    }

}
