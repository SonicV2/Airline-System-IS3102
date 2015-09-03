/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import CI.Entity.Message;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class MessageSessionBean implements MessageSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void sendMsg(String sender, String receiver, String msgText) {
        Message msg1 = new Message();
        Message msg2 = new Message();
        
        Employee msgSender= getEmployee(sender);
        Employee msgReceiver = getEmployee(receiver);
        
        List<Message> sMsgs = msgSender.getMsgs();
        List<Message> rMsgs = msgReceiver.getMsgs();
        
        msg1.createMsg(sender, receiver, msgText);
        msg2.createMsg(sender, receiver, msgText);
        System.out.println("SessionBean: Sender: " +sender + "Reciever: "+receiver + "Message: "+msgText);
      
        sMsgs.add(msg1);
        rMsgs.add(msg2);
        
        em.persist(msgSender);
        em.persist(msgReceiver);
        em.flush();
        
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

    @Override
    public List<Message> unReadMsg(String receiver) {
        Employee receUser = getEmployee(receiver);
        List<Message> msgs = receUser.getMsgs();
        System.out.println("Reciever: "+ receiver + " number: "+msgs.size() );
        List<Message> unReadMsgs = new ArrayList<Message>();
        for(Message m : msgs){
            if(m.getReceiver().equals(receiver) && m.isIsRead()==false){
                unReadMsgs.add(m);
            }
        }
        
        return unReadMsgs;
    }

    @Override
    public void setMsgRead(List<Message> msgs) {
       
    }
    
    
    
}
