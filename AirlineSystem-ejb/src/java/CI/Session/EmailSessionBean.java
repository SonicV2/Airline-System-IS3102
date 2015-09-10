/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class EmailSessionBean implements EmailSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private int port = 465;
    private String host = "smtp.gmail.com";
    private String from = "merlionairpassreset@gmail.com";
    private boolean auth = true;
    private String username = "merlionairpassreset@gmail.com";
    private String password = "merlionair";
    private Protocol protocol = Protocol.SMTPS;
    private boolean debug = true;

    @Override
    public void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        switch (protocol) {
            case SMTPS:
                props.put("mail.smtp.ssl.enable", true);
                break;
            case TLS:
                props.put("mail.smtp.starttls.enable", true);
                break;
        }

        Authenticator authenticator = null;
        if (auth) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);

                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public String validateUser(String userName, String NRIC) {
        Employee employee = getEmployee(userName);
        String email;
        if (employee != null) {
            if (employee.getEmployeeID().equals(NRIC)) {
                email = employee.getEmployeePrivateEmail(); //need to change 
                System.out.println("Email Add: "+email);
            } else {
                email = "";
            }
        } else {
            email = "";
        }
        return email;
    }

    @Override
    public String passGen() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rdm = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(AB.charAt(rdm.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public Employee getEmployee(String employeeUserName) {
        Employee employee = new Employee();
        try {

            Query q = em.createQuery("SELECT a FROM Employee " + "AS a WHERE a.employeeUserName=:userName");
            q.setParameter("userName", employeeUserName);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                employee = (Employee) results.get(0);

            } else {
                employee = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return employee;
    }

}
