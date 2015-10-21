/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APS.Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Yanlong
 */
@Entity
public class Forecast implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int yearOfForecast;
//    private double originalMth1;
//    private double originalMth2;
//    private double originalMth3;
//    private double originalMth4;
//    private double originalMth5;
//    private double originalMth6;
//    private double originalMth7;
//    private double originalMth8;
//    private double originalMth9;
//    private double originalMth10;
//    private double originalMth11;
//    private double originalMth12;
//    private double originalMth13;
//    private double originalMth14;
//    private double originalMth15;
//    private double originalMth16;
//    private double originalMth17;
//    private double originalMth18;
//    private double originalMth19;
//    private double originalMth20;
//    private double originalMth21;
//    private double originalMth22;
//    private double originalMth23;
//    private double originalMth24;
    private double mth1Forecast;
    private double mth2Forecast;
    private double mth3Forecast;
    private double mth4Forecast;
    private double mth5Forecast;
    private double mth6Forecast;
    private double mth7Forecast;
    private double mth8Forecast;
    private double mth9Forecast;
    private double mth10Forecast;
    private double mth11Forecast;
    private double mth12Forecast;
    
    @ManyToOne
    private Route route = new Route();

    public void createForecastReport(int yearOfForecast, double[] dForecast) {
        this.yearOfForecast = yearOfForecast;
//        this.setOriginalMth1(dForecast[0]);
//        this.setOriginalMth2(dForecast[1]);
//        this.setOriginalMth3(dForecast[2]);
//        this.setOriginalMth4(dForecast[3]);
//        this.setOriginalMth5(dForecast[4]);
//        this.setOriginalMth6(dForecast[5]);
//        this.setOriginalMth7(dForecast[6]);
//        this.setOriginalMth8(dForecast[7]);
//        this.setOriginalMth9(dForecast[8]);
//        this.setOriginalMth10(dForecast[9]);
//        this.setOriginalMth11(dForecast[10]);
//        this.setOriginalMth12(dForecast[11]);
//        this.setOriginalMth13(dForecast[12]);
//        this.setOriginalMth14(dForecast[13]);
//        this.setOriginalMth15(dForecast[14]);
//        this.setOriginalMth16(dForecast[15]);
//        this.setOriginalMth17(dForecast[16]);
//        this.setOriginalMth18(dForecast[17]);
//        this.setOriginalMth19(dForecast[18]);
//        this.setOriginalMth20(dForecast[19]);
//        this.setOriginalMth21(dForecast[20]);
//        this.setOriginalMth22(dForecast[21]);
//        this.setOriginalMth23(dForecast[22]);
//        this.setOriginalMth24(dForecast[23]);
        this.setMth1Forecast(dForecast[24]);
        this.setMth2Forecast(dForecast[25]);
        this.setMth3Forecast(dForecast[26]);
        this.setMth4Forecast(dForecast[27]);
        this.setMth5Forecast(dForecast[28]);
        this.setMth6Forecast(dForecast[29]);
        this.setMth7Forecast(dForecast[30]);
        this.setMth8Forecast(dForecast[31]);
        this.setMth9Forecast(dForecast[32]);
        this.setMth10Forecast(dForecast[33]);
        this.setMth11Forecast(dForecast[34]);
        this.setMth12Forecast(dForecast[35]);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return yearOfForecast;
    }

    public void setYear(int year) {
        this.yearOfForecast = year;
    }

//    public double getOriginalMth1() {
//        return originalMth1;
//    }
//
//    public void setOriginalMth1(double originalMth1) {
//        this.originalMth1 = originalMth1;
//    }
//
//    public double getOriginalMth2() {
//        return originalMth2;
//    }
//
//    public void setOriginalMth2(double originalMth2) {
//        this.originalMth2 = originalMth2;
//    }
//
//    public double getOriginalMth3() {
//        return originalMth3;
//    }
//
//    public void setOriginalMth3(double originalMth3) {
//        this.originalMth3 = originalMth3;
//    }
//
//    public double getOriginalMth4() {
//        return originalMth4;
//    }
//
//    public void setOriginalMth4(double originalMth4) {
//        this.originalMth4 = originalMth4;
//    }
//
//    public double getOriginalMth5() {
//        return originalMth5;
//    }
//
//    public void setOriginalMth5(double originalMth5) {
//        this.originalMth5 = originalMth5;
//    }
//
//    public double getOriginalMth6() {
//        return originalMth6;
//    }
//
//    public void setOriginalMth6(double originalMth6) {
//        this.originalMth6 = originalMth6;
//    }
//
//    public double getOriginalMth7() {
//        return originalMth7;
//    }
//
//    public void setOriginalMth7(double originalMth7) {
//        this.originalMth7 = originalMth7;
//    }
//
//    public double getOriginalMth8() {
//        return originalMth8;
//    }
//
//    public void setOriginalMth8(double originalMth8) {
//        this.originalMth8 = originalMth8;
//    }
//
//    public double getOriginalMth9() {
//        return originalMth9;
//    }
//
//    public void setOriginalMth9(double originalMth9) {
//        this.originalMth9 = originalMth9;
//    }
//
//    public double getOriginalMth10() {
//        return originalMth10;
//    }
//
//    public void setOriginalMth10(double originalMth10) {
//        this.originalMth10 = originalMth10;
//    }
//
//    public double getOriginalMth11() {
//        return originalMth11;
//    }
//
//    public void setOriginalMth11(double originalMth11) {
//        this.originalMth11 = originalMth11;
//    }
//
//    public double getOriginalMth12() {
//        return originalMth12;
//    }
//
//    public void setOriginalMth12(double originalMth12) {
//        this.originalMth12 = originalMth12;
//    }
//
//    public double getOriginalMth13() {
//        return originalMth13;
//    }
//
//    public void setOriginalMth13(double originalMth13) {
//        this.originalMth13 = originalMth13;
//    }
//
//    public double getOriginalMth14() {
//        return originalMth14;
//    }
//
//    public void setOriginalMth14(double originalMth14) {
//        this.originalMth14 = originalMth14;
//    }
//
//    public double getOriginalMth15() {
//        return originalMth15;
//    }
//
//    public void setOriginalMth15(double originalMth15) {
//        this.originalMth15 = originalMth15;
//    }
//
//    public double getOriginalMth16() {
//        return originalMth16;
//    }
//
//    public void setOriginalMth16(double originalMth16) {
//        this.originalMth16 = originalMth16;
//    }
//
//    public double getOriginalMth17() {
//        return originalMth17;
//    }
//
//    public void setOriginalMth17(double originalMth17) {
//        this.originalMth17 = originalMth17;
//    }
//
//    public double getOriginalMth18() {
//        return originalMth18;
//    }
//
//    public void setOriginalMth18(double originalMth18) {
//        this.originalMth18 = originalMth18;
//    }
//
//    public double getOriginalMth19() {
//        return originalMth19;
//    }
//
//    public void setOriginalMth19(double originalMth19) {
//        this.originalMth19 = originalMth19;
//    }
//
//    public double getOriginalMth20() {
//        return originalMth20;
//    }
//
//    public void setOriginalMth20(double originalMth20) {
//        this.originalMth20 = originalMth20;
//    }
//
//    public double getOriginalMth21() {
//        return originalMth21;
//    }
//
//    public void setOriginalMth21(double originalMth21) {
//        this.originalMth21 = originalMth21;
//    }
//
//    public double getOriginalMth22() {
//        return originalMth22;
//    }
//
//    public void setOriginalMth22(double originalMth22) {
//        this.originalMth22 = originalMth22;
//    }
//
//    public double getOriginalMth23() {
//        return originalMth23;
//    }
//
//    public void setOriginalMth23(double originalMth23) {
//        this.originalMth23 = originalMth23;
//    }
//
//    public double getOriginalMth24() {
//        return originalMth24;
//    }
//
//    public void setOriginalMth24(double originalMth24) {
//        this.originalMth24 = originalMth24;
//    }

    public double getMth1Forecast() {
        return mth1Forecast;
    }

    public void setMth1Forecast(double mth1Forecast) {
        this.mth1Forecast = mth1Forecast;
    }

    public double getMth2Forecast() {
        return mth2Forecast;
    }

    public void setMth2Forecast(double mth2Forecast) {
        this.mth2Forecast = mth2Forecast;
    }

    public double getMth3Forecast() {
        return mth3Forecast;
    }

    public void setMth3Forecast(double mth3Forecast) {
        this.mth3Forecast = mth3Forecast;
    }

    public double getMth4Forecast() {
        return mth4Forecast;
    }

    public void setMth4Forecast(double mth4Forecast) {
        this.mth4Forecast = mth4Forecast;
    }

    public double getMth5Forecast() {
        return mth5Forecast;
    }

    public void setMth5Forecast(double mth5Forecast) {
        this.mth5Forecast = mth5Forecast;
    }

    public double getMth6Forecast() {
        return mth6Forecast;
    }

    public void setMth6Forecast(double mth6Forecast) {
        this.mth6Forecast = mth6Forecast;
    }

    public double getMth7Forecast() {
        return mth7Forecast;
    }

    public void setMth7Forecast(double mth7Forecast) {
        this.mth7Forecast = mth7Forecast;
    }

    public double getMth8Forecast() {
        return mth8Forecast;
    }

    public void setMth8Forecast(double mth8Forecast) {
        this.mth8Forecast = mth8Forecast;
    }

    public double getMth9Forecast() {
        return mth9Forecast;
    }

    public void setMth9Forecast(double mth9Forecast) {
        this.mth9Forecast = mth9Forecast;
    }

    public double getMth10Forecast() {
        return mth10Forecast;
    }

    public void setMth10Forecast(double mth10Forecast) {
        this.mth10Forecast = mth10Forecast;
    }

    public double getMth11Forecast() {
        return mth11Forecast;
    }

    public void setMth11Forecast(double mth11Forecast) {
        this.mth11Forecast = mth11Forecast;
    }

    public double getMth12Forecast() {
        return mth12Forecast;
    }

    public void setMth12Forecast(double mth12Forecast) {
        this.mth12Forecast = mth12Forecast;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.yearOfForecast;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Forecast other = (Forecast) obj;
        if (this.yearOfForecast != other.yearOfForecast) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Forecast{" + "id=" + yearOfForecast + '}';
    }
}
