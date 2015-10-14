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
        for (int i = 0; i < 100; i++) {
            Customer c = new Customer();
            c.createCustomer(firstName, Integer.toString(i), hpNumber, homenumber, email, password, address, gender, date);
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
            int roll = random.nextInt(100);
            int roll2 = random.nextInt(36);
            String lastName = Integer.toString(roll);
            Query q1 = em.createQuery("SELECT o FROM Customer o WHERE o.lastName =:lastName");
            q1.setParameter("lastName", lastName);
            List<Customer> cList = q1.getResultList();
            Customer c = cList.get(0);
            b.setCustomerId(c.getId());
            b.setPrice(random.nextInt(500)+100.0);           
        }

    }

    public List<CustomerScore> getRecency(int from, int to) {
        System.out.println("Session Bean Reached!!!");
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
                Long score2 = Math.abs((time1-time2) / 1000 / 60 / 60 / 24);
                if (score2 < score) {
                    score = score2;
                }
            }
            
            CustomerScore cs = new CustomerScore(customerId, score * 1.0);
            System.out.println(cs.getScore());
            scoreList.add(cs);
        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }

    public double calAvgRecencyScore() {
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        Long totalScore = Long.valueOf("0");     
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
                System.out.println(c1.getTime());
                System.out.println(c2.getTime());
                Long time1 = c1.getTimeInMillis();
                Long time2 = c2.getTimeInMillis();
                Long score2 = Math.abs((time1-time2) / 1000 / 60 / 60 / 24);
                System.out.println(score2);
                if (score2 < score) {
                    score = score2;
                }
            }

            totalScore = totalScore + score;

        }
        double result = totalScore /(size*1.0);
        return result;
    }

    public List<CustomerScore> getFrequency(int from, int to) {
        List<CustomerScore> scoreList = new ArrayList();
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        int totalScore = 0;
        int score = 0;
        for (int i = 0; i < size; i++) {
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Calendar lastYear = Calendar.getInstance();
            lastYear.set(-2, Calendar.MONTH, Calendar.DATE);
            Date lastYearDate = lastYear.getTime();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerID AND o.flightDate >:date");
            q1.setParameter("customerId", customerId);
            q1.setParameter("date", lastYearDate);
            List<Booking> bList = q1.getResultList();
            score = bList.size();
            CustomerScore cs = new CustomerScore(customerId, score * 1.0);
            scoreList.add(cs);

        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }

    public double calAvgFrequencyScore() {
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        int totalScore = 0;
        int score = 0;
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            Calendar lastYear = Calendar.getInstance();
            lastYear.set(-2, Calendar.MONTH, Calendar.DATE);
            Date lastYearDate = lastYear.getTime();
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId AND o.flightDate >:date");
            q1.setParameter("customerId", customerId);
            q1.setParameter("date", lastYearDate);
            List<Booking> bList = q1.getResultList();
            score = bList.size();
            totalScore = totalScore + score;

        }
        double result = totalScore * 1.0 / size;
        return result;
    }

    public List<CustomerScore> getMonetary(int from, int to) {
        List<CustomerScore> scoreList = new ArrayList();
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

            CustomerScore cs = new CustomerScore(customerId, score * 1.0);
            scoreList.add(cs);

        }
        int scoreSize = scoreList.size();
        from = from * scoreSize / 100;
        to = to * scoreSize / 100;
        scoreList = scoreList.subList(from, to);
        return scoreList;
    }

    public double calAvgMonetaryScore() {
        Query q = em.createQuery("SELECT o FROM Customer o");
        List<Customer> cList = q.getResultList();
        int size = cList.size();
        double totalScore = 0.0;
        double score = 0.0;
        for (int i = 0; i < size; i++) {
            Customer customer = cList.get(i);
            Long customerId = customer.getId();
            System.out.println(customerId);
            Query q1 = em.createQuery("SELECT o FROM Booking o WHERE o.customerId =:customerId");
            q1.setParameter("customerId", customerId);
            List<Booking> bList = q1.getResultList();
            int bSize = bList.size();
            for (int j = 0; j < bSize; j++) {
                Booking b = bList.get(j);
                score = score + b.getPrice();
            }

            totalScore = totalScore + score;

        }
        double result = totalScore / size;
        return result;
    }

}
