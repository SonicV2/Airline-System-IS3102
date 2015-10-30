/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Entity;

import java.io.Serializable;
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
 * @author Family
 */
@Entity
public class Posting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date postingDate;
    private String memo;
    
    @OneToMany(mappedBy = "posting")
    private List<Entry> entries;
    
    public void createPosting(Date postingDate, String memo){
        this.postingDate = postingDate;
        this.memo = memo;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
    
    
    /**
     * Returns true if the posting is balanced.  The sum of the credits equals
     * the sum of the debits.
     *
     * @return True or false
     */
//    public boolean isBalanced() {
//        int balance = 0;
//
//        for( Entry e : entries ) {
//            balance += e.getAmount();
//        }
//
//        return balance == 0;
//    }

//    protected Posting addEntry(String account, int amount) throws NonExistantAccountException {
//        Entry e = new Entry();
//        Account a = ledger.getAccount(account);
//        if (a != null) {
//            e.setAccount(a);
//            e.setAmount(amount);
//        } else {
//            throw new NonExistantAccountException(account);
//        }
//        entries.add(e);
//        
//        return this;
//    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posting)) {
            return false;
        }
        Posting other = (Posting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Admin.Entity.Posting[ id=" + id + " ]";
    }
    
}
