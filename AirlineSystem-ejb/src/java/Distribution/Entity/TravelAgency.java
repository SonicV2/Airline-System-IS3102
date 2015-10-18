/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Distribution.Entity;

import CI.Entity.Salt;
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
public class TravelAgency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double maxCredit;
    private double currentCredit;
    private double commission;
    private String email;
    private String address;
    private String contactNo;
    @OneToOne(cascade = {CascadeType.PERSIST})
    private Salt salt;
    private String password;
    private String  primaryContact;
    
            
    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<PNR> pnrs = new ArrayList<PNR>();
    
    public void createTravelAgent (String name, double maxCredit, double currentCredit, double commission, String email, String address, String contactNo, String password, String primaryContact){
        this.name = name;
        this.maxCredit = maxCredit;
        this.currentCredit = currentCredit;
        this.commission = commission;
        this.email = email;
        this.address = address;
        this.contactNo = contactNo;
        this.password = password; 
        this.primaryContact = primaryContact;
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
        if (!(object instanceof TravelAgency)) {
            return false;
        }
        TravelAgency other = (TravelAgency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Distribution.Entity.TravelAgent[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxCredit() {
        return maxCredit;
    }

    public void setMaxCredit(double maxCredit) {
        this.maxCredit = maxCredit;
    }

    public double getCurrentCredit() {
        return currentCredit;
    }

    public void setCurrentCredit(double currentCredit) {
        this.currentCredit = currentCredit;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PNR> getPnrs() {
        return pnrs;
    }

    public void setPnrs(List<PNR> pnrs) {
        this.pnrs = pnrs;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }
    
    
    
}
