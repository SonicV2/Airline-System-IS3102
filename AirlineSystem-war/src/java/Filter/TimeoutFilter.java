/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filter;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author smu
 */
public class TimeoutFilter implements HttpSessionListener{
    public TimeoutFilter(){}
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("Current Session created : " + event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        System.out.println("Current Session destroyed :" + session.getId() + ";Logging out user......");
    }
    
    
    
}
