/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Session;
import java.io.Serializable;


/**
 *
 * @author YiQuan
 */
public class CustomerScore implements Serializable {
    private Long id;
    private double score;
    private String name;
    private String email;
    
    
    public CustomerScore(Long id, double score){
        this.id = id;
        this.score=score;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
}
