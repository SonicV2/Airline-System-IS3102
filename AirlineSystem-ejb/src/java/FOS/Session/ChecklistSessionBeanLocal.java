/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;


import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author parthasarthygupta
 */
@Local
public interface ChecklistSessionBeanLocal {

    public void addChecklistItem (String checklistName, String itemName);
    public List<String> retrieveAllChecklists();
    public List<String> retrieveChecklistItems (String checklistName);
}
