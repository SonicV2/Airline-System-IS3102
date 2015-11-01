/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.AMaintainChecklist;
import FOS.Entity.MaintainChecklistItem;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author smu
 */
@Local
public interface MaintenanceChecklistSessionBeanLocal {

    public void assignChecklist();

    public List<AMaintainChecklist> getCrewAvailChecklist(String username);

    public void saveChecklist(String checklistId, List<String> checklistItemNames, String comments);

    public List<MaintainChecklistItem> getSelectedChecklistItems(String checklistId);

    public void submitChecklist(String checklistId, List<String> checklistItemNames, String comments, String username);

    public List<AMaintainChecklist> getSubmittedChecklists();

    public AMaintainChecklist getMaintainChecklistByID(String checklistId);
}
