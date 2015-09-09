/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Session;

import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface EmailSessionBeanLocal {
    public void sendEmail(String to, String subject, String body);
}
