/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import CI.Entity.Employee;
import CI.Entity.Message;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface MessageSessionBeanLocal {

    void sendMsg(String sender, String receiver, String msgText);

    List<Message> unReadMsg(String receiver);
    List<Message> readMsg(String receiver);
    void setMsgRead(String userName);
    public Employee getEmployee(String employeeUserName) ;
    
}
