/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.ManagedBean;

import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Entity.BookEntry;
import Administration.Session.AccountingSessionBeanLocal;
import java.util.ArrayList;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    Integer year;
    Double cashAmt;
    Double retainedAmt;

    FacesMessage message = null;

    private BookAccount account;
    private List<BookAccount> accounts;
    private AccountingBook acBook;
    private List<AccountingBook> acBooks;
    private List<Integer> years;

    @PostConstruct
    public void retrieve() {
        setAccounts(accountingSessionBean.getAccounts());
        setAcBooks(accountingSessionBean.getAcBooks());
        setYears(accountingSessionBean.getValidYears());
    }

    public void createBookofAccounting(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (acBooks != null) {
            for (int i = 0; i < acBooks.size(); i++) {
                if (acBooks.get(i).getYear().equals(year)) {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Book of accounting for the chosen year already exists!", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return;
                }
            }
        }

        accountingSessionBean.createBook(year, cashAmt, retainedAmt);
        setAcBooks(accountingSessionBean.getAcBooks());
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Book of Accounting for " + year + " with standard accounts have been successfully created!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        clear();
    }

    public void checkBook(int year) {
        acBook = new AccountingBook();
        AccountingBook original = accountingSessionBean.getAcBook(year);

        //Search for the original active book
        acBook = accountingSessionBean.getCurrBook();

        accountingSessionBean.changeAcBookYear(acBook.getYear(), year);
        setAcBooks(accountingSessionBean.getAcBooks());
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "The Accounting Year has been changed and future transactions will be recorded on the new book!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        clear();
    }

    public String generateIncomeStatement(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PrintBookReport";
        }

        List<BookAccount> revenue = accountingSessionBean.getRevenueAccounts(year);
        List<BookAccount> expense = accountingSessionBean.getExpenseAccounts(year);
        List<BookEntry> entries = new ArrayList<BookEntry>();
        FacesContext context = FacesContext.getCurrentInstance();

        double revenue1 = 0.0;
        double revenue2 = 0.0;
        double expense1 = 0.0;

        //Calculate the revenue and expense entries
        for (int i = 0; i < revenue.size(); i++) {
            if (revenue.get(i).getAccountName().equals("Ticket Sales Revenue")) {
                entries = revenue.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    revenue1 += entries.get(j).getAmount();
                }
            }

            if (revenue.get(i).getAccountName().equals("Travel Agency Revenue")) {
                entries = revenue.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    revenue2 += entries.get(j).getAmount();
                }
            }
        }

        for (int i = 0; i < expense.size(); i++) {
            if (expense.get(i).getAccountName().equals("Fuel Expenses")) {
                entries = expense.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    expense1 += entries.get(j).getAmount();
                }
            }
        }

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("year", year);
            session.setAttribute("ticket", revenue1);
            session.setAttribute("travel", revenue2);
            session.setAttribute("fuel", expense1);

            request.setAttribute("type", "income"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }

        clear();
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Income statement for " + year + " has been successfully downloaded!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "PrintBookReport";
    }

    public String generateStockholderEquity(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PrintBookReport";
        }

        acBook = accountingSessionBean.getAcBook(year);
        List<BookAccount> revenue = accountingSessionBean.getRevenueAccounts(year);
        List<BookAccount> expense = accountingSessionBean.getExpenseAccounts(year);
        List<BookEntry> entries = new ArrayList<BookEntry>();

        //Calculate the Retained Earnings
        double currRet = 0.0;
        for (int i = 0; i < acBook.getAccounts().size(); i++) {
            if (acBook.getAccounts().get(i).getAccountName().equals("Retained Earnings")) {
                currRet = acBook.getAccounts().get(i).getEntries().get(0).getAmount();
                break;
            }
        }

        //Calculate the income
        double rev = 0.0;
        double exp = 0.0;

        for (int i = 0; i < revenue.size(); i++) {
            entries = revenue.get(i).getEntries();
            for (int j = 0; j < entries.size(); j++) {
                rev += entries.get(j).getAmount();
            }
        }

        for (int i = 0; i < expense.size(); i++) {
            entries = expense.get(i).getEntries();
            for (int j = 0; j < entries.size(); j++) {
                exp += entries.get(j).getAmount();
            }
        }

        double income = rev - exp;

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("currRet", currRet);
            session.setAttribute("result", currRet + income);
            session.setAttribute("income", income);
            session.setAttribute("year", year);

            request.setAttribute("type", "equity"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }

        clear();
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Stockholder's Equity statement for " + year + " has been successfully downloaded!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "PrintBookReport";
    }

    public String generateBalanceSheet(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PrintBookReport";
        }

        List<BookAccount> asset = accountingSessionBean.getAccountsByType(Type.ASSET);
        List<BookAccount> liability = accountingSessionBean.getAccountsByType(Type.LIABILITY);
        List<BookAccount> equity = accountingSessionBean.getAccountsByType(Type.EQUITY);
        List<BookAccount> revenue = accountingSessionBean.getRevenueAccounts(year);
        List<BookAccount> expense = accountingSessionBean.getExpenseAccounts(year);
        List<BookEntry> entries = new ArrayList<BookEntry>();
        FacesContext context = FacesContext.getCurrentInstance();

        //DecimalFormat
        double asset1 = 0.0;
        double asset2 = 0.0;
        double asset3 = 0.0;
        double asset4 = 0.0;
        double liability1 = 0.0;
        double liability2 = 0.0;
        double equity1 = 0.0;
        double revenue1 = 0.0;
        double revenue2 = 0.0;
        double expense1 = 0.0;

        //Calculate the asset values
        for (int i = 0; i < asset.size(); i++) {
            if (asset.get(i).getAccountName().equals("Cash")) {
                entries = asset.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    asset1 += entries.get(j).getAmount();
                }
            }

            if (asset.get(i).getAccountName().equals("Accounts Receivable")) {
                entries = asset.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    asset2 += entries.get(j).getAmount();
                }
            }

            if (asset.get(i).getAccountName().equals("Prepaid Expense")) {
                entries = asset.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    asset3 += entries.get(j).getAmount();
                }
            }

            if (asset.get(i).getAccountName().equals("Property and Equipment")) {
                entries = asset.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    asset4 += entries.get(j).getAmount();
                }
            }
        }

        //Calculate the liability values
        for (int i = 0; i < liability.size(); i++) {
            if (liability.get(i).getAccountName().equals("Accounts Payable")) {
                entries = liability.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    liability1 += entries.get(j).getAmount();
                }
            }

            if (liability.get(i).getAccountName().equals("Unearned Revenue")) {
                entries = liability.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    liability2 += entries.get(j).getAmount();
                }
            }
        }

        //Calculate the Equity values
        for (int i = 0; i < equity.size(); i++) {
            if (equity.get(i).getAccountName().equals("Retained Earnings")) {
                entries = equity.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    equity1 += entries.get(j).getAmount();
                }
            }
        }

        //Calculate the revenue and expense entries
        for (int i = 0; i < revenue.size(); i++) {
            if (revenue.get(i).getAccountName().equals("Ticket Sales Revenue")) {
                entries = revenue.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    revenue1 += entries.get(j).getAmount();
                }
            }

            if (revenue.get(i).getAccountName().equals("Travel Agency Revenue")) {
                entries = revenue.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    revenue2 += entries.get(j).getAmount();
                }
            }
        }

        for (int i = 0; i < expense.size(); i++) {
            if (expense.get(i).getAccountName().equals("Fuel Expenses")) {
                entries = expense.get(i).getEntries();
                for (int j = 0; j < entries.size(); j++) {
                    expense1 += entries.get(j).getAmount();
                }
            }
        }

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("cash", asset1);
            session.setAttribute("acctRecv", asset2);
            session.setAttribute("prepaid", asset3);
            session.setAttribute("pne", asset4);
            session.setAttribute("acctPay", liability1);
            session.setAttribute("unearned", liability2);
            session.setAttribute("retained", equity1+revenue1+revenue2-expense1);
            session.setAttribute("year", year);

            request.setAttribute("type", "balance"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }

        clear();
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Balance Sheet for " + year + " has been successfully downloaded!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "PrintBookReport";
    }

    public String generateAcctJournal(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PrintBookReport";
        }

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("year", year);

            request.setAttribute("type", "journal"); //Set to type in order to differentiate from other report generation
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Controller");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.responseComplete();
        }

        clear();
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Accounting Journal for " + year + " has been successfully downloaded!", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "PrintBookReport";
    }

    public void clear() {
        setYear(0);
        setCashAmt(null);
        setRetainedAmt(null);
        setAccount(null);
        setAcBook(null);
        setMessage(null);
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(Double cashAmt) {
        this.cashAmt = cashAmt;
    }

    public Double getRetainedAmt() {
        return retainedAmt;
    }

    public void setRetainedAmt(Double retainAmt) {
        this.retainedAmt = retainAmt;
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

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public FacesMessage getMessage() {
        return message;
    }

    public void setMessage(FacesMessage message) {
        this.message = message;
    }
}
