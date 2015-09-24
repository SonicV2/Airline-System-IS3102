/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import CI.Entity.Message;
import CI.Entity.OrganizationUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MessageSessionBean implements MessageSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public void sendMsg(String sender, String receiver, String msgText) {
        Message msg1 = new Message();
        Message msg2 = new Message();

        Employee msgSender = getEmployee(sender);
        Employee msgReceiver = getEmployee(receiver);

        List<Message> sMsgs = msgSender.getMsgs();
        List<Message> rMsgs = msgReceiver.getMsgs();

        msg1.createMsg(sender, receiver, msgText);
        msg2.createMsg(sender, receiver, msgText);
        System.out.println("SessionBean: Sender: " + sender + "Reciever: " + receiver + "Message: " + msgText);

        sMsgs.add(msg1);
        rMsgs.add(msg2);

        em.persist(msgSender);
        em.persist(msgReceiver);
        em.flush();

    }

    @Override
    public void sendBroadcastMsg(String sender, List<String> departments, String msgText) {
       List<OrganizationUnit> ous = new ArrayList<OrganizationUnit>();
        for(String s: departments){
            System.out.println("Department: "+s);
            String dep = s.substring(0, s.indexOf("("));
            ous.add(getDepartment(dep));
        }
        
        for(OrganizationUnit ou : ous){
            for(Employee e: ou.getEmployee()){
                sendMsg(sender,e.getEmployeeUserName(),msgText);
            }
        }
    }
    
    public OrganizationUnit getDepartment(String department){
        OrganizationUnit depart= new OrganizationUnit();
        try {
            Query q = em.createQuery("SELECT a FROM OrganizationUnit a WHERE a.departmentName=:departmentName");
            q.setParameter("departmentName", department);
            List<OrganizationUnit> results = q.getResultList();
            if (!results.isEmpty()) {
                depart = (OrganizationUnit) results.get(0);
            } else {
                depart = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return depart;
    
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

    //get a list of unread messages from the database
    @Override
    public List<Message> unReadMsg(String receiver) {
        Employee receUser = getEmployee(receiver);
        List<Message> msgs = receUser.getMsgs();
        System.out.println("Reciever: " + receiver + " number: " + msgs.size());
        List<Message> unReadMsgs = new ArrayList<Message>();
        for (Message m : msgs) {
            if (m.getReceiver().equals(receiver) && m.isIsRead() == false) {
                unReadMsgs.add(m);
            }
        }

        return unReadMsgs;
    }

    //get at list of read messages from database
    @Override
    public List<Message> readMsg(String receiver) {
        Employee receUser = getEmployee(receiver);
        List<Message> msgs = receUser.getMsgs();
        System.out.println("Reciever: " + receiver + " number: " + msgs.size());
        List<Message> readMsgs = new ArrayList<Message>();
        for (Message m : msgs) {
            if (m.getReceiver().equals(receiver) && m.isIsRead() == true) {
                readMsgs.add(m);
            }
        }

        return readMsgs;
    }
    
    
    //get a list of messages sent by the user
    @Override
    public List<Message> sentMsg(String sender){
        Employee sendUser = getEmployee(sender);
        List<Message> msgs = sendUser.getMsgs();
        System.out.println("Sender: " + sender + "number: " + msgs.size());
        List<Message> sentMsgs = new ArrayList<Message>();
        for (Message m: msgs)
            if (m.getSender().equals(sender)){
                sentMsgs.add(m);
            }
        return sentMsgs;
    }
    
    

    //to set a list of messages as read
    @Override
    public void setMsgRead(String userName) {
        Employee user = getEmployee(userName);
        List<Message> msgList = user.getMsgs();
        for (Message m : msgList) {
            if (m.isIsRead() == false) {
                m.setIsRead(true);
            }
        }
    }

    private static class Department {

        public Department() {
        }
    }

}
