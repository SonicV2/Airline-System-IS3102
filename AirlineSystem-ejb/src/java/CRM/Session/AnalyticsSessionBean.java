/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Session;

import javax.ejb.Stateless;
import Distribution.Entity.Customer;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.mail.NoSuchProviderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import Inventory.Entity.Booking;
import java.util.Calendar;
import java.util.Random;
import Distribution.Entity.PNR;

/**
 *
 * @author YiQuan
 */
@Stateless
public class AnalyticsSessionBean implements AnalyticsSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    public void createPsuedoCustomers() {
        String firstName = "Customer";
        String hpNumber = "999";
        String email = "yuqing_w@u.nus.edu";
        String homenumber = "995";
        String password = "password";
        String address = "Com 1 NUS SOC";
        String gender = "male";
        Date date = new Date();
        for (int i = 0; i < 1000; i++) {
            Customer c = new Customer();
            c.createCustomer(firstName, Integer.toString(i), hpNumber, homenumber, email, password, address, gender, date, "MR", "Singaporean", "ABC123");
            em.persist(c);
        }
    }

    public void pseudoLink() {
        Query q = em.createQuery("SELECT b FROM Booking b");
        List<Booking> bList = q.getResultList();
        int bSize = bList.size();
        for (int i = 0; i < bSize; i++) {
            Booking b = bList.get(i);
            Random random = new Random();
            int roll = random.nextInt(1000);
            int roll2 = random.nextInt(24);
            String lastName = Integer.toString(roll);
            Query q1 = em.createQuery("SELECT o FROM Customer o WHERE o.lastName =:lastName");
            q1.setParameter("lastName", lastName);
            List<Customer> cList = q1.getResultList();
            Customer c = cList.get(0);
            b.setCustomerId(c.getId());
            b.setPrice(random.nextInt(500) + 100.0);
            Date flightDate= b.getFlightDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(flightDate);
            cal.setTimeInMillis(cal.getTimeInMillis()-(roll2*1000*60*60*24*30));
            b.setFlightDate(flightDate);
        }

    }

    public List<CustomerScore> getRecency(int from, int to) {
        List<CustomerScore> scoreList = new ArrayList();
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        for (int i = 0; i < size; i++) {
            Long score = Long.valueOf("99999");
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId");
            q1.setParameter("customerId", customerId);
            List<Booking> bList = q1.getResultList();
            int bSize = bList.size();
            Date today = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(today);
            for (int j = 0; j < bSize; j++) {
                Booking booking = bList.get(j);
                Calendar c2 = Calendar.getInstance();
                c2.setTime(booking.getFlightDate());

                Long time1 = c1.getTimeInMillis();
                Long time2 = c2.getTimeInMillis();
                Long score2 = Math.abs((time1 - time2) / 1000 / 60 / 60 / 24);
                if (score2 < score) {
                    score = score2;
                }
            }
            Customer c = em.find(Customer.class, customerId);
            CustomerScore cs = new CustomerScore(customerId, score * 1.0, c.getFirstName() + " " + c.getLastName(), c.getEmail());
            scoreList.add(cs);
        }
        int size2= scoreList.size();   
        for (int i=0; i<size2; i++){
            boolean change = false;
            CustomerScore cs = scoreList.get(i);
            double minScore =cs.getScore();
            int minIndex = i;
            for (int j=i+1; j<size2; j++){
                CustomerScore cs2 = scoreList.get(j);
                if(cs2.getScore()< minScore){
                    minScore = cs2.getScore();
                    minIndex=j;
                    change= true;
                }
            }
            if(change){
                CustomerScore temp= scoreList.get(minIndex);
                scoreList.remove(minIndex);
                scoreList.add(i, temp);                
                change=false;
            }
        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }

 

    public List<CustomerScore> getFrequency(int from, int to) {
        List<CustomerScore> scoreList = new ArrayList();
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        for (int i = 0; i < size; i++) {
            int score = 0;
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Calendar lastYear = Calendar.getInstance();
            lastYear.set(-1, Calendar.MONTH, Calendar.DATE);
            Date lastYearDate = lastYear.getTime();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId AND o.flightDate >:date");
            q1.setParameter("customerId", customerId);
            q1.setParameter("date", lastYearDate);
            List<Booking> bList = q1.getResultList();
            score = bList.size();
            Customer c = em.find(Customer.class, customerId);
            CustomerScore cs = new CustomerScore(customerId, score * 1.0, c.getFirstName() + " " + c.getLastName(), c.getEmail());
            scoreList.add(cs);

        }
        int size2= scoreList.size();   
        for (int i=0; i<size2; i++){
            boolean change = false;
            CustomerScore cs = scoreList.get(i);
            double minScore =cs.getScore();
            int minIndex = i;
            for (int j=i+1; j<size2; j++){
                CustomerScore cs2 = scoreList.get(j);
                if(cs2.getScore()< minScore){
                    minScore = cs2.getScore();
                    minIndex=j;
                    change= true;
                }
            }
            if(change){
                CustomerScore temp= scoreList.get(minIndex);
                scoreList.remove(minIndex);
                scoreList.add(i, temp);                
                change=false;
            }
        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }


    public List<CustomerScore> getMonetary(int from, int to) {
        List<CustomerScore> scoreList = new ArrayList();
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        for (int i = 0; i < size; i++) {
            double score = 0.0;
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Calendar lastYear = Calendar.getInstance();
            lastYear.set(-1, Calendar.MONTH, Calendar.DATE);
            Date lastYearDate = lastYear.getTime();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId AND o.flightDate >:date");
            q1.setParameter("customerId", customerId);
            q1.setParameter("date", lastYearDate);
            List<Booking> bList = q1.getResultList();
            int bSize = bList.size();
            for (int j = 0; j < bSize; j++) {
                Booking b = bList.get(j);
                score = score + b.getPrice();
            }
            Customer c = em.find(Customer.class, customerId);
            CustomerScore cs = new CustomerScore(customerId, score * 1.0, c.getFirstName() + " " + c.getLastName(), c.getEmail());
            scoreList.add(cs);

        }
        int size2= scoreList.size();   
        for (int i=0; i<size2; i++){
            boolean change = false;
            CustomerScore cs = scoreList.get(i);
            double minScore =cs.getScore();
            int minIndex = i;
            for (int j=i+1; j<size2; j++){
                CustomerScore cs2 = scoreList.get(j);
                if(cs2.getScore()< minScore){
                    minScore = cs2.getScore();
                    minIndex=j;
                    change= true;
                }
            }
            if(change){
                CustomerScore temp= scoreList.get(minIndex);
                scoreList.remove(minIndex);
                scoreList.add(i, temp);                
            }
        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }
    
    public List<Customer> getLostCustomers(){
        List<Customer> customerList = new ArrayList();
        Query q = em.createQuery("SELECT o FROM Customer o");
        boolean check = false;
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        for (int i = 0; i < size; i++) {
            Long score = Long.valueOf("99999");
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId");
            q1.setParameter("customerId", customerId);
            List<Booking> bList = q1.getResultList();
            int bSize = bList.size();
            Date today = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(today);
            for (int j = 0; j < bSize; j++) {
                Booking booking = bList.get(j);
                Calendar c2 = Calendar.getInstance();
                c2.setTime(booking.getFlightDate());

                Long time1 = c1.getTimeInMillis();
                Long time2 = c2.getTimeInMillis();
                Long score2 = Math.abs((time1 - time2) / 1000 / 60 / 60 / 24);
                if (score2 < score) {
                    score = score2;
                }
            }
            if (score>365 && score<730){
            Customer c = em.find(Customer.class, customerId);
            customerList.add(c);
            }     
        }
        
        return customerList;
    }
    
    public double getRetentionRate(){
        List<Customer> customerList = new ArrayList();
        List<Customer> customerList2 = new ArrayList();
        Query q = em.createQuery("SELECT o FROM Customer o");
        boolean check = false;
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        for (int i = 0; i < size; i++) {
            Long score = Long.valueOf("99999");
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId");
            q1.setParameter("customerId", customerId);
            List<Booking> bList = q1.getResultList();
            int bSize = bList.size();
            Date today = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(today);
            for (int j = 0; j < bSize; j++) {
                Booking booking = bList.get(j);
                Calendar c2 = Calendar.getInstance();
                c2.setTime(booking.getFlightDate());

                Long time1 = c1.getTimeInMillis();
                Long time2 = c2.getTimeInMillis();
                Long score2 = Math.abs((time1 - time2) / 1000 / 60 / 60 / 24);
                if (score2 < score) {
                    score = score2;
                }
                if(score2>365)
                    check = true;
            }
            if (score>365 && score<730){
            Customer c = em.find(Customer.class, customerId);
            customerList.add(c);
            }
            if (score<=365 && check){
            Customer c = em.find(Customer.class, customerId);
            customerList2.add(c);
            }
            
            check = false;
        }
        if(customerList2.size()==0)
            return 1.0;
        double result=customerList.size()/(customerList.size()+customerList2.size());
        return result;
    }
    
    public List<CustomerScore> getCLV(int from, int to, double retention) {
        List<CustomerScore> scoreList = new ArrayList();
        double discount = 0.1;
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        double totalScore = 0.0;
        double score = 0.0;
        for (int i = 0; i < size; i++) {
            Customer customer = cList.get(i);
            Long customerId = customer.getId();          
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId");
            q1.setParameter("customerId", customerId);
            List<Booking> bList = q1.getResultList();
            int bSize = bList.size();
            for (int j = 0; j < bSize; j++) {
                Booking b = bList.get(j);
                score = score + b.getPrice();
            }
            score = score*(retention*(1+discount-retention));
            Customer c = em.find(Customer.class, customerId);
            CustomerScore cs = new CustomerScore(customerId, score * 1.0, c.getFirstName() + " " + c.getLastName(), c.getEmail());
            scoreList.add(cs);

        }
        int size2= scoreList.size();   
        for (int i=0; i<size2; i++){
            boolean change = false;
            CustomerScore cs = scoreList.get(i);
            double minScore =cs.getScore();
            int minIndex = i;
            for (int j=i+1; j<size2; j++){
                CustomerScore cs2 = scoreList.get(j);
                if(cs2.getScore()< minScore){
                    minScore = cs2.getScore();
                    minIndex=j;
                    change= true;
                }
            }
            if(change){
                CustomerScore temp= scoreList.get(minIndex);
                scoreList.remove(minIndex);
                scoreList.add(i, temp);                
            }
        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }


}
