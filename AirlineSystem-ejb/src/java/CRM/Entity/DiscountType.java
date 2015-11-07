/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CRM.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author parthasarthygupta
 */
@Entity
public class DiscountType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private double mileagePointsToRedeem;
    private double discount;
    private int noOfCodesUnredeemed;
    private String type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @OneToMany (cascade = {CascadeType.REMOVE}, mappedBy = "discountType")
    private List<DiscountCode> discountCodes = new ArrayList();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMileagePointsToRedeem() {
        return mileagePointsToRedeem;
    }

    public void setMileagePointsToRedeem(double mileagePointsToRedeem) {
        this.mileagePointsToRedeem = mileagePointsToRedeem;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getNoOfCodesUnredeemed() {
        return noOfCodesUnredeemed;
    }

    public void setNoOfCodesUnredeemed(int noOfCodesUnredeemed) {
        this.noOfCodesUnredeemed = noOfCodesUnredeemed;
    }

    public List<DiscountCode> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(List<DiscountCode> discountCodes) {
        this.discountCodes = discountCodes;
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
        if (!(object instanceof DiscountType)) {
            return false;
        }
        DiscountType other = (DiscountType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CRM.Entity.DiscountType[ id=" + id + " ]";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
}
