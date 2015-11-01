/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.Session;

import FOS.Entity.AMaintainChecklist;
import FOS.Entity.GroundCrew;
import FOS.Entity.MaintainChecklistItem;
import FOS.Entity.MaintainSchedule;
import FOS.Entity.MaintainanceTeam;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author smu
 */
@Stateless
public class MaintenanceChecklistSessionBean implements MaintenanceChecklistSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    public AMaintainChecklist createAMaintananceChecklistAndItems() {
        Date date = new Date();
        AMaintainChecklist achecklist = new AMaintainChecklist();

        achecklist.setComments("");
        achecklist.setName("");
        achecklist.setLastEditDate(date);
        achecklist.setType("A");
        achecklist.setListStatus("N.A");

        List<MaintainChecklistItem> checklistItems = achecklist.getMaintainChecklistItems();

        // Structural/Zonal:
        MaintainChecklistItem item1 = new MaintainChecklistItem();
        item1.create("External structure of fuselage, mainplanes, empennage, cowlings, nacelles, control "
                + "surfaces, flaps and other high lift devices");
        checklistItems.add(item1);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item1);
        em.persist(achecklist);

        MaintainChecklistItem item2 = new MaintainChecklistItem();
        item2.create("Normal and emergency doors and windows, door hinges, door hinge attachment "
                + "points, required placards and operating instructions");
        checklistItems.add(item2);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item2);
        em.persist(achecklist);

        MaintainChecklistItem item3 = new MaintainChecklistItem();
        item3.create("Doors, hatches and windows latching and locking");
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item3);
        em.persist(achecklist);

        //Lanind Gear
        MaintainChecklistItem item4 = new MaintainChecklistItem();
        item4.create("Landing gear assemblies, shock-absorber struts/units for leaks and correct extension, brake system, brake linings, drums/discs, wheels, tyres");
        checklistItems.add(item4);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item4);
        em.persist(achecklist);

        MaintainChecklistItem item5 = new MaintainChecklistItem();
        item5.create("Tyre pressures, hydraulic brake system fluid level");
        checklistItems.add(item5);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item5);
        em.persist(achecklist);

        MaintainChecklistItem item6 = new MaintainChecklistItem();
        item6.create("Primary/secondary flight controls and trim systems for full and free movement in the correct sense. Position indicators agree with surface movement");
        checklistItems.add(item6);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item6);
        em.persist(achecklist);

        //Liquid, Air and Gas Systems:
        MaintainChecklistItem item7 = new MaintainChecklistItem();
        item7.create("Hydraulic, pneumatic, vacuum, other fluid systems");
        checklistItems.add(item7);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item7);
        em.persist(achecklist);

        MaintainChecklistItem item8 = new MaintainChecklistItem();
        item8.create("Fluid levels in reservoirs, accumulator pressures");
        checklistItems.add(item8);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item8);
        em.persist(achecklist);

        MaintainChecklistItem item9 = new MaintainChecklistItem();
        item9.create("Pitot/static system vents, pitot head, drains clear. Pitot head correctly aligned");
        checklistItems.add(item9);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item9);
        em.persist(achecklist);

        //Equipment and Environmental:
        MaintainChecklistItem item10 = new MaintainChecklistItem();
        item10.create("Correct stowage of equipment, validity of date on emergency equipment");
        checklistItems.add(item10);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item10);
        em.persist(achecklist);

        MaintainChecklistItem item11 = new MaintainChecklistItem();
        item11.create("Seats, belts/harnesses, attachment, locking and release");
        checklistItems.add(item11);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item11);
        em.persist(achecklist);

        MaintainChecklistItem item12 = new MaintainChecklistItem();
        item12.create("Fire extinguisher for leakage or discharge");
        checklistItems.add(item12);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item12);
        em.persist(achecklist);

        //Engine Lubrication:
        MaintainChecklistItem item13 = new MaintainChecklistItem();
        item13.create("Magnetic plugs");
        checklistItems.add(item13);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item13);
        em.persist(achecklist);

        MaintainChecklistItem item14 = new MaintainChecklistItem();
        item14.create("Engine oil change. Oil filter. Screens");
        checklistItems.add(item14);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item14);
        em.persist(achecklist);

        //Electrical System:
        MaintainChecklistItem item15 = new MaintainChecklistItem();
        item15.create("Battery, stowage/compartment, vents and drains. Electrolyte level");
        checklistItems.add(item15);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item15);
        em.persist(achecklist);

        MaintainChecklistItem item16 = new MaintainChecklistItem();
        item16.create("Alternator/generator drive belt tension and condition");
        checklistItems.add(item16);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item16);
        em.persist(achecklist);

        MaintainChecklistItem item17 = new MaintainChecklistItem();
        item17.create("Aerials, insulators, controllers, instruments, displays, microphones, headsets, jackplugs and sockets");
        checklistItems.add(item17);
        achecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item17);
        em.persist(achecklist);

        return achecklist;
    }

    public AMaintainChecklist createBMaintananceChecklistAndItems() {
        Date date = new Date();
        AMaintainChecklist bchecklist = new AMaintainChecklist();

        bchecklist.setComments("");
        bchecklist.setName("");
        bchecklist.setLastEditDate(date);
        bchecklist.setType("B");
        bchecklist.setListStatus("N.A");
        List<MaintainChecklistItem> checklistItems = bchecklist.getMaintainChecklistItems();

        // Structural/Zonal:
        MaintainChecklistItem item1 = new MaintainChecklistItem();
        item1.create("Internal structure of fuselage, floors, bulkheads, tail booms, mainplanes, nacelles, empennage. Control surfaces, flaps and other high lift devices, structural attachment "
                + "joint assemblies, struts, bracing wires and their attachments");
        checklistItems.add(item1);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item1);
        em.persist(bchecklist);

        MaintainChecklistItem item2 = new MaintainChecklistItem();
        item2.create("Internal corrosion protective treatments, drain holes and paths");
        checklistItems.add(item2);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item2);
        em.persist(bchecklist);

        MaintainChecklistItem item3 = new MaintainChecklistItem();
        item3.create("Vent holes, glued joints, bonded assemblies, protective treatments and finishes");
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item3);
        em.persist(bchecklist);

        //Lanind Gear
        MaintainChecklistItem item4 = new MaintainChecklistItem();
        item4.create("Structural members, attachment fittings, pivot points, shock absorbing devices, "
                + "bungee rubbers, torque links, shimmy dampers, main wheels, nose/tail wheels, "
                + "bearings, skids, hoses and lines, hydraulic and electric actuators, jacks, struts, wheel "
                + "fairing");
        checklistItems.add(item4);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item4);
        em.persist(bchecklist);

        MaintainChecklistItem item5 = new MaintainChecklistItem();
        item5.create("Main and parking brake systems, anti-skid devices");
        checklistItems.add(item5);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item5);
        em.persist(bchecklist);

        MaintainChecklistItem item6 = new MaintainChecklistItem();
        item6.create("Normal/emergency retraction and extension, locking devices, doors and operating "
                + "linkages, indicators, warning devices");
        checklistItems.add(item6);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item6);
        em.persist(bchecklist);

        MaintainChecklistItem item7 = new MaintainChecklistItem();
        item7.create("Hydraulic/pneumatic operating system.");
        checklistItems.add(item7);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item7);
        em.persist(bchecklist);

        //Flying Controls:
        MaintainChecklistItem item8 = new MaintainChecklistItem();
        item8.create("Hinges, brackets, push-pull rods, bellcranks, control horns, balance weights, cables,"
                + "pulleys, chains, tubes, guides, fairleads, rollers, tracks, rails, screw jacks/rams, "
                + "auxiliary gearboxes and other power-operated systems");
        checklistItems.add(item8);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item8);
        em.persist(bchecklist);

        MaintainChecklistItem item9 = new MaintainChecklistItem();
        item9.create("Turnbuckles, locking devices in safety");
        checklistItems.add(item9);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item9);
        em.persist(bchecklist);

        MaintainChecklistItem item10 = new MaintainChecklistItem();
        item10.create("Flap asymmetric protection mechanisms");
        checklistItems.add(item10);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item10);
        em.persist(bchecklist);

        //Equipment and Environmental:
        MaintainChecklistItem item11 = new MaintainChecklistItem();
        item11.create("Cabin air system, heater, blower");
        checklistItems.add(item11);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item11);
        em.persist(bchecklist);

        MaintainChecklistItem item12 = new MaintainChecklistItem();
        item12.create("Air conditioner, oil level");
        checklistItems.add(item12);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item12);
        em.persist(bchecklist);

        MaintainChecklistItem item13 = new MaintainChecklistItem();
        item13.create("Outflow valves, pressurisation controller, bleed system, shut-off valves");
        checklistItems.add(item13);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item13);
        em.persist(bchecklist);

        //Ignition:
        MaintainChecklistItem item14 = new MaintainChecklistItem();
        item14.create("Magnetos, harnesses, leads, switches, starting vibrators, contact breakers, cooling "
                + "system and ventilators");
        checklistItems.add(item14);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item14);
        em.persist(bchecklist);

        MaintainChecklistItem item15 = new MaintainChecklistItem();
        item15.create("Magneto internal timing and timing to engine");
        checklistItems.add(item15);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item15);
        em.persist(bchecklist);

        //Propeller:
        MaintainChecklistItem item16 = new MaintainChecklistItem();
        item16.create("Hub, constant speed unit, governor, accumulators, de-icing boots, slip rings and "
                + "brushes, fluid systems, control systems");
        checklistItems.add(item16);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item16);
        em.persist(bchecklist);

        MaintainChecklistItem item17 = new MaintainChecklistItem();
        item17.create("Pitch change mechanism for backlash");
        checklistItems.add(item17);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item17);
        em.persist(bchecklist);

        MaintainChecklistItem item18 = new MaintainChecklistItem();
        item18.create("Lubricate propeller in accordance with type design organisation recommendations");
        checklistItems.add(item18);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item18);
        em.persist(bchecklist);

        //Electrical Systems:
        MaintainChecklistItem item19 = new MaintainChecklistItem();
        item19.create("Components, wiring, terminals, connectors");
        checklistItems.add(item19);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item19);
        em.persist(bchecklist);

        MaintainChecklistItem item20 = new MaintainChecklistItem();
        item20.create("Correct type and rating of fuses and circuit breakers. Correct spare fuses carried.");
        checklistItems.add(item20);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item20);
        em.persist(bchecklist);
        
         MaintainChecklistItem item21 = new MaintainChecklistItem();
        item21.create("Lamps and lighting. Correct spare lamps carried");
        checklistItems.add(item21);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item21);
        em.persist(bchecklist);
        
        MaintainChecklistItem item22 = new MaintainChecklistItem();
        item22.create("Brushes in starters, alternators and generators");
        checklistItems.add(item22);
        bchecklist.setMaintainChecklistItems(checklistItems);
        em.persist(item22);
        em.persist(bchecklist);

        return bchecklist;
    }

    @Override
    //assign checklist to each maintenance schedule
    public void assignChecklist() {
        Query q = em.createQuery("SELECT m FROM MaintainSchedule m");
        List<MaintainSchedule> mSchedules = q.getResultList();

        for (MaintainSchedule m : mSchedules) {
            if (m.getType().equals("A") && m.getAmaintainChecklist() == null) {
                AMaintainChecklist achecklist = createAMaintananceChecklistAndItems();
                m.setAmaintainChecklist(achecklist);
                achecklist.setMaintainSchedule(m);
                em.persist(m);
            }
            if (m.getType().equals("B") && m.getAmaintainChecklist() == null) {
                AMaintainChecklist bchecklist = createBMaintananceChecklistAndItems();
                m.setAmaintainChecklist(bchecklist);
                bchecklist.setMaintainSchedule(m);
                em.persist(bchecklist);
            }
        }

    }

    @Override
    public List<AMaintainChecklist> getCrewAvailChecklist(String username) {
        Date date = new Date();
        MaintainanceTeam crewTeam = getCrewMaintainTeam(username);

        List<AMaintainChecklist> results = new ArrayList<AMaintainChecklist>();

        if (crewTeam == null) {
            return null;
        } else {

            List<MaintainSchedule> lists = crewTeam.getmSchedules();
            for (MaintainSchedule ms : lists) {
                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(ms.getMainStartDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date);
//                if (checkTime(formattedDate1, formattedDate2) < 60) { /// comment to test 
                if (!ms.getAmaintainChecklist().getListStatus().equals("Submitted")) {
                    results.add(ms.getAmaintainChecklist());
                }
                //}
            }
        }
        return results;

    }

    @Override
    public void saveChecklist(String checklistId, List<String> checklistItemNames, String comments) {
        Date date = new Date();

        AMaintainChecklist checklist = getMaintainChecklistByID(checklistId);
        String temp = checklist.getComments();
        temp += "\n" + comments;
        checklist.setComments(comments);
        checklist.setLastEditDate(date);
        checklist.setListStatus("Saved");

        em.persist(checklist);

        List<MaintainChecklistItem> items = checklist.getMaintainChecklistItems();

        for (MaintainChecklistItem i : items) {
            for (String s : checklistItemNames) {
                if (i.getContent().equals(s)) {
                    i.setChecked(true);
                    em.persist(i);
                }
            }
        }
    }

    @Override
    public void submitChecklist(String checklistId, List<String> checklistItemNames, String comments, String username) {
        Date date = new Date();

        AMaintainChecklist checklist = getMaintainChecklistByID(checklistId);
        String temp = checklist.getComments();
        temp += comments;
        checklist.setComments(comments);
        checklist.setLastEditDate(date);
        checklist.setListStatus("Submitted");
        checklist.setName(username);
        em.persist(checklist);
        em.flush();

        List<MaintainChecklistItem> items = checklist.getMaintainChecklistItems();

        for (MaintainChecklistItem i : items) {
            for (String s : checklistItemNames) {
                if (i.getContent().equals(s)) {
                    i.setChecked(true);
                    em.persist(i);
                }
            }
        }
    }

    @Override
    public List<AMaintainChecklist> getSubmittedChecklists() {
        List<AMaintainChecklist> results = new ArrayList<AMaintainChecklist>();
        Query q = em.createQuery("SELECT a FROM AMaintainChecklist a");
        List<AMaintainChecklist> lists = q.getResultList();

        for (AMaintainChecklist a : lists) {

            if (a.getListStatus().equals("Submitted")) {
                System.out.println("HERE");
                results.add(a);
            }
        }
        return results;
    }

    @Override
    public List<MaintainChecklistItem> getSelectedChecklistItems(String checklistId) {

        AMaintainChecklist am = getMaintainChecklistByID(checklistId);
        List<MaintainChecklistItem> items = am.getMaintainChecklistItems();
        return items;

    }

    public MaintainChecklistItem getChecklistItem(String checklistItemName) {

        Query q = em.createQuery("SELECT m FROM MaintainChecklistItem m WHERE m.content =:checklistItemName");
        q.setParameter("checklistItemName", checklistItemName);

        List<MaintainChecklistItem> checklists = q.getResultList();
        if (checklists.isEmpty()) {
            return null;
        } else {
            return checklists.get(0);
        }
    }

    @Override
    public AMaintainChecklist getMaintainChecklistByID(String checklistId) {

        Query q = em.createQuery("SELECT m FROM AMaintainChecklist m");

        List<AMaintainChecklist> checklists = q.getResultList();
        for (AMaintainChecklist m : checklists) {
            if (m.getId().toString().equals(checklistId)) {
                return m;
            }
        }
        return null;
    }

    public MaintainanceTeam getCrewMaintainTeam(String username) {
        Query q = em.createQuery("SELECT m FROM MaintainanceTeam m");
        List<MaintainanceTeam> teams = q.getResultList();

        for (MaintainanceTeam t : teams) {
            List<GroundCrew> crews = t.getGroundCrews();
            for (GroundCrew g : crews) {
                if (g.getEmployeeUserName().equals(username)) {
                    return t;
                }
            }
        }
        return null;
    }

    public long checkTime(String time1, String time2) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date1;
        Date date2;
        try {
            date1 = formatter.parse(time1);
            date2 = formatter.parse(time2);
            long diff = (date2.getTime() - date1.getTime());
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long result = diffDays * 24 * 60 + diffHours * 60 + diffMinutes;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }
}
