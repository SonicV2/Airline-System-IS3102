/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventory.Session;

import javax.ejb.Stateless;
import APS.Entity.Location;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import APS.Entity.Schedule;
import Inventory.Entity.SeatAvailability;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 *
 * @author YiQuan
 */
@Stateless
public class RevenueManagement implements RevenueManagementLocal {
    
    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
