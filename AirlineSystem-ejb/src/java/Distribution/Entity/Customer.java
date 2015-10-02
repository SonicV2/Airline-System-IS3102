/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Entity;

import CI.Entity.Salt;
import FOS.Entity.Checklist;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author parthasarthygupta
 */
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String hpNumber;
    private String homeNumber;
    private Long mileagePoints;
     @OneToOne(cascade = {CascadeType.PERSIST})
    private Salt salt;
    private String username;
    private String email;
    private String password;
    private String address;
    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<PNR> pnrs = new ArrayList<PNR>();
    
    public void createCustomer (String firstName, String lastName, String hpNumber, String homeNumber, String username, String email, String password,String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.hpNumber = hpNumber;
        this.homeNumber = homeNumber;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;       
 }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHpNumber() {
        return hpNumber;
    }

    public void setHpNumber(String hpNumber) {
        this.hpNumber = hpNumber;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public Long getMileagePoints() {
        return mileagePoints;
    }

    public void setMileagePoints(Long mileagePoints) {
        this.mileagePoints = mileagePoints;
    }

    public Salt getSalt() {
        return salt;
    }

    public void setSalt(Salt salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PNR> getPnrs() {
        return pnrs;
    }

    public void setPnrs(List<PNR> pnrs) {
        this.pnrs = pnrs;
    }
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Distribution.Entity.Customer[ id=" + id + " ]";
    }
    
}
