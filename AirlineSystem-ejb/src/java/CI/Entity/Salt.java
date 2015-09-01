/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CI.Entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author smu
 */
@Entity
public class Salt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long saltId;
    private String saltCode;

    public Salt(){
        setSaltId(System.nanoTime());
    }
    
    
    public void create(String saltCode){
        this.setSaltCode(saltCode);
    }
    /**
     * @return the saltId
     */
    public Long getSaltId() {
        return saltId;
    }

    /**
     * @param saltId the saltId to set
     */
    public void setSaltId(Long saltId) {
        this.saltId = saltId;
    }

    /**
     * @return the saltCode
     */
    public String getSaltCode() {
        return saltCode;
    }

    /**
     * @param saltCode the saltCode to set
     */
    public void setSaltCode(String saltCode) {
        this.saltCode = saltCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getSaltId() != null ? getSaltId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salt)) {
            return false;
        }
        Salt other = (Salt) object;
        if ((this.getSaltId() == null && other.getSaltId() != null) || (this.getSaltId() != null && !this.saltId.equals(other.saltId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Salt[ id=" + getSaltId() + " ]";
    }

}
