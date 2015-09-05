/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Aircraft;
import APS.Entity.AircraftType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yunna
 */
@Local
public interface APSFleetManagementSystemLocal {
    
    public void acquireAircraft(String tailNo, Date datePurchased, Date lastMaintained, String aircraftTypeId);
    public void retireAircraft(String tailNo);
    public AircraftType getAircraftType(String aircraftTypeId);
    public Aircraft getAircraft(String tailNum);
    public List<AircraftType> getAircraftTypeList(String filter);

}
