/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Yanlong
 */
@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String accountName;

    public enum Type {

        ASSET(-1),
        LIABILITY(1),
        INCOME(1),
        EXPENSE(-1);

        private int normalBalanceSign;

        private Type(int normalBalanceSign) {
            this.normalBalanceSign = normalBalanceSign;
        }

        /**
         * Returns the usual sign for this type of account. Normally debit
         * accounts (like Assets and Expenses) are negative (-1), normally
         * credit accounts are positive (1).
         *
         * @return -1 or 1
         */
        public int getNormalBalanceSign() {
            return normalBalanceSign;
        }
    }

    private Type type;
    
    @OneToMany(mappedBy = "account")
    private List<Entry> entries = new ArrayList<Entry>();

    public void createAccount(String accountName, Type type) {

        this.accountName = accountName;
        this.type = type;
    }

//    public List<Entry> getEntries() {
//        List<Entry> allEntries = new ArrayList<Entry>(entries);
//
//        for (Account a : subAccounts.values()) {
//            allEntries.addAll(a.getEntries());
//        }
//
//        return allEntries;
//    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
    
//    public int getTrialBalance() {
//        int balance = 0;
//
//        for (Entry e : getEntries()) {
//            balance += e.getAmount();
//        }
//
//        return balance;
//    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.accountName);
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
        final Account other = (Account) obj;
        if (!Objects.equals(this.accountName, other.accountName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Account{" + "accountName=" + accountName + '}';
    }
}
