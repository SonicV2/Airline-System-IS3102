/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FOS.ManagedBean;

import APS.Entity.Schedule;
import CI.Entity.CabinCrew;
import CI.Managedbean.LoginManageBean;
import FOS.Session.CrewSignInSessionBeanLocal;
import FOS.Session.ReservedCrewScheduleSessionBeanLocal;
import FOS.Session.SearchCrewSessionBeanLocal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author smu
 */
@Named(value = "reservedCrewScheduleManagedBean")
@ManagedBean
@SessionScoped
public class ReservedCrewScheduleManagedBean {

    @EJB
    private CrewSignInSessionBeanLocal crewSignInSessionBean;

    @EJB
    private SearchCrewSessionBeanLocal searchCrewSessionBean;

    @EJB
    private ReservedCrewScheduleSessionBeanLocal reservedCrewScheduleSessionBean;

    @ManagedProperty(value = "#{loginManageBean}")
    private LoginManageBean loginManageBean;

    private List<CabinCrew> CCLists;
    private List<CabinCrew> leaveCCLists;
    private String crewUserName;
    private String msgAssign;
    private List<CabinCrew> approvedLists;
    private List<CabinCrew> rejectLists;
    private List<String> months;

    private String selectYear;
    private String selectMonth;
    private Date date;

    private List<String> reservedCrewScheduleResult;
    private List<String> reservedCrewScheduleResultDisabled;

    /**
     * Creates a new instance of ReservedCrewScheduleManagedBean
     */
    public ReservedCrewScheduleManagedBean() {
    }

    @PostConstruct
    public void init() {
        //getReservedCrew();
        months = new ArrayList<String>();
        getAllLeaveCrew();
        months.add("01");
        months.add("02");
        months.add("03");
        months.add("04");
        months.add("05");
        months.add("06");
        months.add("07");
        months.add("08");
        months.add("09");
        months.add("10");
        months.add("11");
        months.add("12");
        signInScheduleForReservedCrew();
    }

    public void getReservedCrew(ActionEvent event) {
        FacesMessage message = null;
        date = new Date();
        String month = new SimpleDateFormat("MM").format(date);
        String year = new SimpleDateFormat("yyyy").format(date);
        if (Integer.parseInt(selectMonth) < Integer.parseInt(month) || Integer.parseInt(selectYear) < Integer.parseInt(year)) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You cannot select previous month/year!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (reservedCrewScheduleSessionBean.getAllReservedCrew(selectYear, selectMonth) == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Reserved Crews!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            setCCLists(reservedCrewScheduleSessionBean.getAllReservedCrew(selectYear, selectMonth));
        }
    }

//Assign reserved crews schedules    
    public void assignSchedule(ActionEvent event) {
        for (CabinCrew c : CCLists) {
            reservedCrewScheduleSessionBean.assignSchedule(selectYear, selectMonth, c.getEmployeeUserName());
        }
        getReservedCrew(event);
        setSelectYear("");
        setSelectMonth("");
    }

    public void getAllLeaveCrew() {
        FacesMessage message = null;
        leaveCCLists = new ArrayList<CabinCrew>();
        approvedLists = new ArrayList<CabinCrew>();

        List<CabinCrew> tempLists = searchCrewSessionBean.getLeaveCabinCrew();
        for (CabinCrew c : tempLists) {
            if (c.getSchedule().equals("N.A")) {
                leaveCCLists.add(c);
            }
            if (c.getSchedule().contains("Team")) {
                approvedLists.add(c);
            }
        }

    }

    // Find reserve crew
    public void reassign(ActionEvent event) {

        setMsgAssign(reservedCrewScheduleSessionBean.reassign(crewUserName));
        FacesMessage message = null;
        if (msgAssign.equals("unsuccessful")) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No reserve crew available!", "");

        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "A reserved crew has been assigned!", "");
        }
        setCrewUserName("");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    // Reassign back (Crew finishes leave)
    public void assignBack(ActionEvent event) {
        FacesMessage message = null;
        String msg = reservedCrewScheduleSessionBean.assignBack(loginManageBean.getEmployeeUserName());
        if (msg.equals("successful")) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully checkout!", "");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Checkout unsuccessfully!", "");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void signInScheduleForReservedCrew() {
        Date signInDate = new Date();
        String tempString = "";
        String tempDisable = "";
        String crewName = loginManageBean.getEmployeeUserName();

        CabinCrew crew = crewSignInSessionBean.getCabinCrew(crewName);

        setReservedCrewScheduleResult(new ArrayList<String>());
        setReservedCrewScheduleResultDisabled(new ArrayList<String>());

        if (crew.getPosition().contains("Reserved") && !crew.getStatus().equals("N.S")) {
            String scheduleId = crew.getStatus();
            if (scheduleId.contains("/")) {
                String[] schLists = scheduleId.split("/");
                for (int i = 0; i < schLists.length; i++) {
                    Schedule sch = crewSignInSessionBean.getScheduleByID(schLists[i]);

                    tempString += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                            + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";

                    tempString += "\u26F3 Flight Number: [" + sch.getFlight().getFlightNo() + "] <br /> \uD83D\uDCC6 Schedule Start Date: " + sch.getStartDate() + "   "
                            + "<br /> \uD83D\uDD50 Schedule End Date: " + sch.getEndDate() + "<br />"
                            + "<br /> \uD83D\uDD50 Flight Origin City: " + sch.getFlight().getRoute().getOriginCity() + "<br />"
                            + "<br /> \uD83D\uDD50 Flight Destination City " + sch.getFlight().getRoute().getDestinationCity() + "<br />";

                }

                Schedule lastSch = crewSignInSessionBean.getScheduleByID(schLists[schLists.length - 1]);
                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(lastSch.getEndDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

                String date1 = new SimpleDateFormat("dd/MM/yyyy").format(lastSch.getEndDate());
                String date2 = new SimpleDateFormat("dd/MM/yyyy").format(signInDate);

                if (date1.equals(date2) && checkTime(formattedDate2, formattedDate1) > 60 || checkTime(formattedDate2, formattedDate1) < 0) {
                    reservedCrewScheduleResult.add(tempString);
                } else {
                    reservedCrewScheduleResultDisabled.add(tempString);
                }

            } else {
                Schedule sch = crewSignInSessionBean.getScheduleByID(scheduleId);

                String formattedDate1 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(sch.getEndDate());
                String formattedDate2 = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(signInDate);

                String date1 = new SimpleDateFormat("dd/MM/yyyy").format(sch.getEndDate());
                String date2 = new SimpleDateFormat("dd/MM/yyyy").format(signInDate);

                tempString += "<br /> <br /> \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1  \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 "
                        + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 "
                        + "\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1\uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1 \uD83C\uDFC1  <br /> <br />";

                tempString += "\u26F3 Flight Number: [" + sch.getFlight().getFlightNo() + "] <br /> \uD83D\uDCC6 Schedule Start Date: " + sch.getStartDate() + "   "
                        + "<br /> \uD83D\uDD50 Schedule End Date: " + sch.getEndDate() + "<br />"
                        + "<br /> \uD83D\uDD50 Flight Origin City: " + sch.getFlight().getRoute().getOriginCity() + "<br />"
                        + "<br /> \uD83D\uDD50 Flight Destination City " + sch.getFlight().getRoute().getDestinationCity() + "<br />";

                if (date1.equals(date2) && checkTime(formattedDate2, formattedDate1) > 60 || checkTime(formattedDate2, formattedDate1) < 0) {
                    reservedCrewScheduleResult.add(tempString);
                } else {
                    reservedCrewScheduleResultDisabled.add(tempString);
                }

            }
        }

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

    /**
     * @return the CCLists
     */
    public List<CabinCrew> getCCLists() {
        return CCLists;
    }

    /**
     * @param CCLists the CCLists to set
     */
    public void setCCLists(List<CabinCrew> CCLists) {
        this.CCLists = CCLists;
    }

    /**
     * @return the leaveCCLists
     */
    public List<CabinCrew> getLeaveCCLists() {
        return leaveCCLists;
    }

    /**
     * @param leaveCCLists the leaveCCLists to set
     */
    public void setLeaveCCLists(List<CabinCrew> leaveCCLists) {
        this.leaveCCLists = leaveCCLists;
    }

    /**
     * @return the crewUserName
     */
    public String getCrewUserName() {
        return crewUserName;
    }

    /**
     * @param crewUserName the crewUserName to set
     */
    public void setCrewUserName(String crewUserName) {
        this.crewUserName = crewUserName;
    }

    /**
     * @return the msgAssign
     */
    public String getMsgAssign() {
        return msgAssign;
    }

    /**
     * @param msgAssign the msgAssign to set
     */
    public void setMsgAssign(String msgAssign) {
        this.msgAssign = msgAssign;
    }

    /**
     * @return the approvedLists
     */
    public List<CabinCrew> getApprovedLists() {
        return approvedLists;
    }

    /**
     * @param approvedLists the approvedLists to set
     */
    public void setApprovedLists(List<CabinCrew> approvedLists) {
        this.approvedLists = approvedLists;
    }

    /**
     * @return the rejectLists
     */
    public List<CabinCrew> getRejectLists() {
        return rejectLists;
    }

    /**
     * @param rejectLists the rejectLists to set
     */
    public void setRejectLists(List<CabinCrew> rejectLists) {
        this.rejectLists = rejectLists;
    }

    /**
     * @return the selectYear
     */
    public String getSelectYear() {
        return selectYear;
    }

    /**
     * @param selectYear the selectYear to set
     */
    public void setSelectYear(String selectYear) {
        this.selectYear = selectYear;
    }

    /**
     * @return the selectMonth
     */
    public String getSelectMonth() {
        return selectMonth;
    }

    /**
     * @param selectMonth the selectMonth to set
     */
    public void setSelectMonth(String selectMonth) {
        this.selectMonth = selectMonth;
    }

    /**
     * @return the months
     */
    public List<String> getMonths() {
        return months;
    }

    /**
     * @param months the months to set
     */
    public void setMonths(List<String> months) {
        this.months = months;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the loginManageBean
     */
    public LoginManageBean getLoginManageBean() {
        return loginManageBean;
    }

    /**
     * @param loginManageBean the loginManageBean to set
     */
    public void setLoginManageBean(LoginManageBean loginManageBean) {
        this.loginManageBean = loginManageBean;
    }

    /**
     * @return the reservedCrewScheduleResult
     */
    public List<String> getReservedCrewScheduleResult() {
        return reservedCrewScheduleResult;
    }

    /**
     * @param reservedCrewScheduleResult the reservedCrewScheduleResult to set
     */
    public void setReservedCrewScheduleResult(List<String> reservedCrewScheduleResult) {
        this.reservedCrewScheduleResult = reservedCrewScheduleResult;
    }

    /**
     * @return the reservedCrewScheduleResultDisabled
     */
    public List<String> getReservedCrewScheduleResultDisabled() {
        return reservedCrewScheduleResultDisabled;
    }

    /**
     * @param reservedCrewScheduleResultDisabled the
     * reservedCrewScheduleResultDisabled to set
     */
    public void setReservedCrewScheduleResultDisabled(List<String> reservedCrewScheduleResultDisabled) {
        this.reservedCrewScheduleResultDisabled = reservedCrewScheduleResultDisabled;
    }

}
