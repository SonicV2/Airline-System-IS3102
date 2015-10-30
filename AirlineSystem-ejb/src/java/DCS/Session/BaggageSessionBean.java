/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCS.Session;

import DCS.Entity.BaggageTag;
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
public class BaggageSessionBean implements BaggageSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private static int tagNumber = 12345;

    public int calcualtePenalty(String departure, String destination, int allowed, int totalWeight) {
        int exceed = totalWeight - allowed;
        int penalty = 0;
        String dept = bandSearch(departure);
        String dest = bandSearch(destination);

        if (departure.equals("Singapore")) {
            if (dest.equals("band1")) {
                penalty += exceed * 8;
            } else if (dest.equals("band2")) {
                penalty += exceed * 12;
            } else if (dest.equals("band3")) {
                penalty += exceed * 30;
            } else if (dest.equals("band4")) {
                penalty += exceed * 55;
            }

        } else if (dept.equals("band1") && destination.equals("Singapore")) {
            penalty += exceed * 8;

        } else if (dept.equals("band1") && dest.equals("band1")) {
            penalty += exceed * 16;

        } else if (dept.equals("band1") && dest.equals("band2")) {
            penalty += exceed * 20;
        } else if (dept.equals("band1") && dest.equals("band3")) {
            penalty += exceed * 38;
        } else if (dept.equals("band1") && dest.equals("band3")) {
            penalty += exceed * 68;

        } else if (dept.equals("band2") && destination.equals("Singapore")) {
            penalty += exceed * 12;
        } else if (dept.equals("band2") && dest.equals("band1")) {
            penalty += exceed * 20;

        } else if (dept.equals("band2") && dest.equals("band2")) {
            penalty += exceed * 24;
        } else if (dept.equals("band2") && dest.equals("band3")) {
            penalty += exceed * 42;
        } else if (dept.equals("band2") && dest.equals("band3")) {
            penalty += exceed * 67;
        } else if (dept.equals("band3") && destination.equals("Singapore")) {
            penalty += exceed * 30;
        } else if (dept.equals("band3") && dest.equals("band1")) {
            penalty += exceed * 38;

        } else if (dept.equals("band3") && dest.equals("band2")) {
            penalty += exceed * 42;
        } else if (dept.equals("band3") && dest.equals("band3")) {
            penalty += exceed * 60;
        } else if (dept.equals("band3") && dest.equals("band3")) {
            penalty += exceed * 85;
        } else if (dept.equals("band4") && destination.equals("Singapore")) {
            penalty += exceed * 55;
        } else if (dept.equals("band4") && dest.equals("band1")) {
            penalty += exceed * 63;

        } else if (dept.equals("band4") && dest.equals("band2")) {
            penalty += exceed * 67;
        } else if (dept.equals("band4") && dest.equals("band3")) {
            penalty += exceed * 85;
        } else if (dept.equals("band4") && dest.equals("band3")) {
            penalty += exceed * 110;
        }

        return penalty;
    }

    public String bandSearch(String city) {
        String band = "";

        String[] band1 = {"Brunei", "Cambodia", "Indonesia", "Laos", "Malaysia", "Myanmar", "Philippines", "Thailand",
            "Vietnam"};

        String[] band2 = {"China", "Hong Kong", "Taiwan", "Macau", "Mongolia", "Afghanistan", "Bangladesh", "Bhutan",
            "India", "Maldives", "Nepal", "Pakistan", "Sri Lanka", "Kazakhstan", "Kyrgyzstan", "Turkmenistan",
            "Uzbekistan", "Tajikistan", "Russia"};

        String[] band3 = {"Japan", "Korea", "South West Pacific", "Guam", "Marshall Islands", "Micronesia", "Northern Mariana Islands", "Palau",
            "Timor Leste", "Middle East"};

        String[] band4 = {"Denmark", "Finland", "Iceland", "Ireland", "Lithuania", " Norway", "United Kingdom", "Greece",
            "Italy", "Spain", "Germany", "France", " Netherlands", "Switzerland", "United States", "South Africa"};

        for (int i = 0; i < band1.length; i++) {
            if (city.equals(band1[i])) {
                band = "band1";
            }
        }

        for (int i = 0; i < band2.length; i++) {
            if (city.equals(band2[i])) {
                band = "band2";
            }
        }

        for (int i = 0; i < band3.length; i++) {
            if (city.equals(band3[i])) {
                band = "band3";
            }
        }

        for (int i = 0; i < band4.length; i++) {
            if (city.equals(band4[i])) {
                band = "band4";
            }
        }

        return band;

    }

    public String generateTagNumber() {
        String leadDigits = "8518";
        tagNumber += 1;
        return (leadDigits + tagNumber + "");
    }

    

}
