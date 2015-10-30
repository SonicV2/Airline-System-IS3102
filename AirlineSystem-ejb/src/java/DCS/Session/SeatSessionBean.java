/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import APS.Entity.Schedule;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author smu
 */
@Stateless
public class SeatSessionBean implements SeatSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<String> retrieveOccupiedSeats(Schedule schedule) {
        List<String> results = new ArrayList<String>();

        List<String> first = schedule.getSelectedSeatsF();
        List<String> business = schedule.getSelectedSeatsB();
        List<String> economy = schedule.getSelectedSeatsE();

        if (first == null) {
        } else {
            for (String s : first) {
                results.add(s);
            }
        }

        if (business == null) {
        } else {
            for (String s : first) {
                results.add(s);
            }
        }

        if (economy == null) {
        } else {
            for (String s : first) {
                results.add(s);
            }
        }

        return results;
    }

}
