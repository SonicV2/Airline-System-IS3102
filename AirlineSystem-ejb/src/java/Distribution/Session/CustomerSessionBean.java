/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Session;

import CI.Entity.Salt;
import Distribution.Entity.Customer;
import Distribution.Entity.PNR;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.mail.NoSuchProviderException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yuqing
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    Customer customer;

    @Override
    public String addCustomer(String firstName, String lastName, String hpNumber, String homeNumber, String email, String password, String address, String gender, Date DOB, String title, String nationality, String passportNumber) {

        try {

            customer = new Customer();
            String saltCode = generateSalt();
            String hashedPwd = getSecurePassword(password, saltCode);
//            customer.setPassword(hashedPwd);
            Salt salt = new Salt();
            salt.create(saltCode);
            customer.setSalt(salt);
            em.persist(salt);

            customer.createCustomer(firstName, lastName, hpNumber, homeNumber, email, hashedPwd, address, gender, DOB, title, nationality, passportNumber);
            em.persist(customer);

        } catch (NoSuchAlgorithmException ex) {

        } catch (NoSuchProviderException ex) {

        } catch (java.security.NoSuchProviderException ex) {

        }

        return "Sign up successful!";
    }

    // Password encryption use MD 5 hashing
    public String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt.toString();
    }

    public String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public boolean isSameHash(String userEmail, String pwd) {

        Customer oneCustomer = getCustomerUseEmail(userEmail);
        String saltCode = oneCustomer.getSalt().getSaltCode();
        String rehash = getSecurePassword(pwd, saltCode);

        if (oneCustomer.getPassword().equals(rehash)) {
            return true;
        } else {
            return false;
        }
    }

    // Get customer using ID
    @Override
    public Customer getCustomerUseID(String customerID) {
        Customer oneCustomer = new Customer();
        try {

            Query q = em.createQuery("SELECT a FROM Customer " + "AS a WHERE a.id=:id");
            q.setParameter("id", customerID);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                oneCustomer = (Customer) results.get(0);

            } else {
                oneCustomer = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return oneCustomer;
    }

    //get the customer using his email
    @Override
    public Customer getCustomerUseEmail(String customerEmail) {
        Customer oneCustomer = new Customer();
        try {

            Query q = em.createQuery("SELECT a FROM Customer " + "AS a WHERE a.email=:email");
            q.setParameter("email", customerEmail);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                oneCustomer = (Customer) results.get(0);

            } else {
                oneCustomer = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return oneCustomer;
    }

    //check if the email entered already exists in the database
    @Override
    public Boolean emailExists(String customerEmail) {
        try {

            System.out.println("session bean - customer email is:" + customerEmail);
            Query q = em.createQuery("SELECT a FROM Customer " + "AS a WHERE a.email=:email");
            q.setParameter("email", customerEmail);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                System.out.println("session bean - customer email already exists");
                return true;

            } else {
                System.out.println("session bean - customer email does not exist");
                return false;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
            return false;
        }

    }

    @Override
    public void hashNewPwd(String customerEmail, String pwd) {
        Customer customer = getCustomerUseEmail(customerEmail);
        try {
            String saltCode = generateSalt();
            String hashedPwd = getSecurePassword(pwd, saltCode);
            customer.setPassword(hashedPwd);
            customer.getSalt().setSaltCode(saltCode);
            em.persist(customer);
        } catch (EntityNotFoundException enfe) {
            System.out.println(enfe.getMessage());

        } catch (NoSuchAlgorithmException ex) {

            System.out.println("\nNo Such Algorithm Exception" + ex.getMessage());

        } catch (NoSuchProviderException ex) {
            System.out.println("\nNo Such Provider Exception" + ex.getMessage());

        } catch (java.security.NoSuchProviderException ex) {
            System.out.println("\nJava security No Such Provider Exception" + ex.getMessage());

        }

    }

    @Override
    public List<PNR> retrieveCustomerPNRs(Customer customer) {
        Long customerId = customer.getId();
        
        List<Customer> allCustomers = new ArrayList<>();
        
        try{
            Query q = em.createQuery("SELECT a FROM Customer " + "AS a WHERE a.id=:id");
            q.setParameter("id", customerId);
            
            List<Customer> results = q.getResultList();
            if (!results.isEmpty()){
                
                allCustomers = results;
                
            }else
            {
                allCustomers = null;
                System.out.println("no reserve aircraft!");
            }
        }catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        customer = allCustomers.get(0);
        
        
        
        if (customer != null) {
            List<PNR> customerBookedPNRs = new ArrayList();
            for (PNR eachCustomerPNR : customer.getPnrs()) {
                if (eachCustomerPNR.getPnrStatus().equals("Booked")) {
                    customerBookedPNRs.add(eachCustomerPNR);
                }
            }
            return customerBookedPNRs;
        } else {
            return null;
        }
    }

}
