/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Yunna
 */
@Entity
public class ProfitAndLoss implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Temporal(TemporalType.DATE)
    private Date dateOfStatement;
    private double salesRevenue;
    private double airportRental;
    private double employeeSalaries;
    private double airportTaxes;
    private double commission;
    private double fuelCosts;
    private double aircrafts;
    
    private double totalPnL;
    private double totalRevenue;
    private double totalExpenses;
    
    public void createPnL (Date dateOfStatement, double salesRevenue, double commission, double aircrafts, double fuelCosts, double employeeSalaries, double airportRental, double airportTaxes, double totalRevenue, double totalExpenses, double totalPnL){
        this.dateOfStatement = dateOfStatement;
        this.salesRevenue = salesRevenue;
        this.aircrafts = aircrafts;
        this.commission = commission;
        this.fuelCosts = fuelCosts;
        this.employeeSalaries = employeeSalaries;
        this.airportRental = airportRental;
        this.airportTaxes = airportTaxes; 
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.totalPnL = totalPnL;
    }

    public Date getDateOfStatement() {
        return dateOfStatement;
    }

    public void setDateOfStatement(Date dateOfStatement) {
        this.dateOfStatement = dateOfStatement;
    }

    public double getSalesRevenue() {
        return salesRevenue;
    }

    public void setSalesRevenue(double salesRevenue) {
        this.salesRevenue = salesRevenue;
    }

    public double getAirportRental() {
        return airportRental;
    }

    public void setAirportRental(double airportRental) {
        this.airportRental = airportRental;
    }

    public double getEmployeeSalaries() {
        return employeeSalaries;
    }

    public void setEmployeeSalaries(double employeeSalaries) {
        this.employeeSalaries = employeeSalaries;
    }

    public double getAirportTaxes() {
        return airportTaxes;
    }

    public void setAirportTaxes(double airportTaxes) {
        this.airportTaxes = airportTaxes;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getFuelCosts() {
        return fuelCosts;
    }

    public void setFuelCosts(double fuelCosts) {
        this.fuelCosts = fuelCosts;
    }

    public double getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(double aircrafts) {
        this.aircrafts = aircrafts;
    }

    public double getTotalPnL() {
        return totalPnL;
    }

    public void setTotalPnL(double totalPnL) {
        this.totalPnL = totalPnL;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    
}
