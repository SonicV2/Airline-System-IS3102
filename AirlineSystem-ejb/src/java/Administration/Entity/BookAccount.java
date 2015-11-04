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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Yanlong
 */
@Entity
public class BookAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;
    private String accountName;

    public enum Type {

        ASSET(1),
        LIABILITY(-1),
        INCOME(-1),
        EXPENSE(1),
        EQUITY(-1);

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
    private List<BookEntry> entries = new ArrayList<BookEntry>();
    
    @ManyToOne
    private AccountingBook acBook = new AccountingBook();

    public void createAccount(String accountName, Type type) {

        this.accountName = accountName;
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

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

    public List<BookEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<BookEntry> entries) {
        this.entries = entries;
    }

    public AccountingBook getAcBook() {
        return acBook;
    }

    public void setAcBook(AccountingBook acBook) {
        this.acBook = acBook;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.accountId);
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
        final BookAccount other = (BookAccount) obj;
        if (!Objects.equals(this.accountId, other.accountId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Account{" + "accountId=" + accountId + '}';
    }
}
