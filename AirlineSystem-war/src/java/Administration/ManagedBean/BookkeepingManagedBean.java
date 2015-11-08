/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.ManagedBean;

import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Session.AccountingSessionBeanLocal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Yanlong
 */
@Named(value = "bookkeepingManagedBean")
@ManagedBean
@SessionScoped
public class BookkeepingManagedBean {

    @EJB
    private AccountingSessionBeanLocal accountingSessionBean;

    String accountName;
    Type type;
    String typeStr;
    Date entryDate;
    Double amount;
    Integer year;
    Boolean active;

    FacesMessage message = null;

    private String[] typeList = {"Asset", "Liability", "Income", "Expense", "Equity"};
    private BookAccount account;
    private List<BookAccount> accounts;
    private AccountingBook acBook;
    private List<AccountingBook> acBooks;

    @PostConstruct
    public void retrieve() {
        setAccounts(accountingSessionBean.getAccounts());
        setAcBooks(accountingSessionBean.getAcBooks());
    }

//    public void addAccount(ActionEvent event) {
//        if (accountName.isEmpty()) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input BookAccount Name!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return;
//        }
//
//        if (year == 0) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return;
//        }
//
//        if (typeStr.isEmpty()) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input type of account!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return;
//        }
//
//        account = accountingSessionBean.addAccount(accountName, year, Type.valueOf(typeStr));
//        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "BookAccount " + account.getAccountName() + "added successfully!", "");
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        clear();
//    }
//
//    public String deleteAccount(Long accountId) {
//        account = accountingSessionBean.getAccount(accountId);
//        if (!account.getEntries().isEmpty()) {
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "BookAccount has entries and cannot be deleted!", "");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//            return null;
//        }
//
//        accountingSessionBean.deleteAccount(accountId);
//        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "BookAccount has been deleted successfully!", "");
//        FacesContext.getCurrentInstance().addMessage(null, message);
//        clear();
//
//        return "DeleteAccount";
//    }

    public void createBookofAccounting(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        for (int i = 0; i < acBooks.size(); i++) {
            if (acBooks.get(i).getYear() == year) {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Book of accounting for the chosen year already exists!", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }

        accountingSessionBean.createBook(year);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Book of Accounting for " + year + "with standard accounts have been successfully created!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        clear();
    }
     public void onRowEdit(RowEditEvent event) {
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Cancelled", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     
     public void onRowCancel(RowEditEvent event) {
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edit Cancelled", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void checkBook(int year){
        for (int i = 0; i<acBooks.size(); i++){
            if(acBooks.get(i).isActive()){
                acBook = acBooks.get(i);
                break;
            }
        }
        accountingSessionBean.changeAcBookYear(acBook.getYear(), year);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "The Accounting Year has been changed and future transactions will be recorded on the new book!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        clear();
    }
    public void clear() {
        setAccountName(null);
        setTypeStr(null);
        setEntryDate(null);
        setAmount(0.0);
        setYear(0);
        setAccount(null);
        setAccounts(null);
        setAcBook(null);
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

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String[] getTypeList() {
        return typeList;
    }

    public void setTypeList(String[] typeList) {
        this.typeList = typeList;
    }

    public BookAccount getAccount() {
        return account;
    }

    public void setAccount(BookAccount account) {
        this.account = account;
    }

    public List<BookAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BookAccount> accounts) {
        this.accounts = accounts;
    }

    public AccountingBook getAcBook() {
        return acBook;
    }

    public void setAcBook(AccountingBook acBook) {
        this.acBook = acBook;
    }

    public List<AccountingBook> getAcBooks() {
        return acBooks;
    }

    public void setAcBooks(List<AccountingBook> acBooks) {
        this.acBooks = acBooks;
    }
}
