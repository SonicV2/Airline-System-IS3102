/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Session;

import APS.Entity.Forecast;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Yanlong
 */
@Local
public interface DemandForecastSessionBeanLocal {

    public void generateDemandForecast(Long routeId, int year, int period, boolean isUpdate);

    public Forecast getForecast(int year, long routeId);

    public boolean isValidYear();

}
