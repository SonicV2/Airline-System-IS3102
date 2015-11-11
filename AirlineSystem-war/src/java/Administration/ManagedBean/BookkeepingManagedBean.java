/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.ManagedBean;

import APS.Session.ScheduleSessionBeanLocal;
import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Entity.BookEntry;
import Administration.Entity.Posting;
import Administration.Session.AccountingSessionBeanLocal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    @EJB
    private ScheduleSessionBeanLocal scheduleSessionBean;

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
        acBook = accountingSessionBean.getAcBook(year);

        List<BookEntry> entries = new ArrayList<BookEntry>();
        account = new BookAccount();
        FacesContext context = FacesContext.getCurrentInstance();

        double revenue1 = 0.0;
        double revenue2 = 0.0;
        double revenue3 = 0.0;
        double expense1 = 0.0;
        double expense2 = 0.0;

        //Calculate the revenue and expense entries
        account = acBook.getAccountByName("Ticket Sales Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                revenue1 += account.getEntries().get(i).getAmount();
            }
        }
        account = acBook.getAccountByName("Travel Agency Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                revenue2 += account.getEntries().get(i).getAmount();
            }
        }
        account = acBook.getAccountByName("Misc Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                revenue3 += account.getEntries().get(i).getAmount();
            }
        }

        accountingSessionBean.recognizeFuelExpenses(year);
        account = acBook.getAccountByName("Fuel Expenses");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                expense1 += account.getEntries().get(i).getAmount();
            }
        }
        accountingSessionBean.recognizeMaintenanceExpenses(year);
        account = acBook.getAccountByName("Maintenance Expenses");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                expense2 += account.getEntries().get(i).getAmount();
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
            session.setAttribute("misc", revenue3);
            session.setAttribute("fuel", expense1);
            session.setAttribute("maintain", expense2);

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

        FacesContext.getCurrentInstance()
                .addMessage(null, message);

        return "PrintBookReport";
    }

    public String generateStockholderEquity(ActionEvent event) {
        if (year == 0) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Please input year of accounting!", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "PrintBookReport";
        }

        acBook = accountingSessionBean.getAcBook(year);
        account = new BookAccount();
        List<BookEntry> entries = new ArrayList<BookEntry>();

        //Calculate the Retained Earnings
        double currRet = 0.0;
        account = acBook.getAccountByName("Retained Earnings");
        System.out.println(account.getEntries());
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                currRet += account.getEntries().get(i).getAmount();
            }
        }

        //Calculate the income
        double rev = 0.0;
        double exp = 0.0;

        account = acBook.getAccountByName("Ticket Sales Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                rev += account.getEntries().get(i).getAmount();
            }
        }
        account = acBook.getAccountByName("Travel Agency Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                rev += account.getEntries().get(i).getAmount();
            }
        }
        account = acBook.getAccountByName("Misc Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                rev += account.getEntries().get(i).getAmount();
            }
        }

        accountingSessionBean.recognizeFuelExpenses(year);
        account = acBook.getAccountByName("Fuel Expenses");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                exp += account.getEntries().get(i).getAmount();
            }
        }
        accountingSessionBean.recognizeMaintenanceExpenses(year);
        account = acBook.getAccountByName("Maintenance Expenses");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                exp += account.getEntries().get(i).getAmount();
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
        
        acBook = accountingSessionBean.getCurrBook();
        List<BookAccount> asset = accountingSessionBean.getAccountsByType(Type.ASSET);
        List<BookAccount> liability = accountingSessionBean.getAccountsByType(Type.LIABILITY);
        List<BookEntry> entries = new ArrayList<BookEntry>();
        FacesContext context = FacesContext.getCurrentInstance();

        double asset1 = 0.0;
        double asset2 = 0.0;
        double asset3 = 0.0;
        double asset4 = 0.0;
        double liability1 = 0.0;
        double liability2 = 0.0;
        double equity1 = 0.0;
        double revenue1 = 0.0;
        double revenue2 = 0.0;
        double revenue3 = 0.0;
        double expense1 = 0.0;
        double expense2 = 0.0;

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
        account = acBook.getAccountByName("Retained Earnings");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                equity1 += account.getEntries().get(i).getAmount();
            }
        }

        //Calculate the revenue and expense entries
        account = acBook.getAccountByName("Ticket Sales Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                revenue1 += account.getEntries().get(i).getAmount();
            }
        }

        account = acBook.getAccountByName("Travel Agency Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                revenue2 += account.getEntries().get(i).getAmount();
            }
        }
        account = acBook.getAccountByName("Misc Revenue");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                revenue3 += account.getEntries().get(i).getAmount();
            }
        }

        accountingSessionBean.recognizeFuelExpenses(year);
        account = acBook.getAccountByName("Fuel Expenses");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                expense1 += account.getEntries().get(i).getAmount();
            }
        }
        accountingSessionBean.recognizeMaintenanceExpenses(year);
        account = acBook.getAccountByName("Maintenance Expenses");
        if (account.getEntries() != null) {
            for (int i = 0; i < account.getEntries().size(); i++) {
                expense2 += account.getEntries().get(i).getAmount();
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
            session.setAttribute("retained", equity1 + revenue1 + revenue2 + revenue3 - expense1 - expense2);
            session.setAttribute("revSum", revenue1 + revenue2 + revenue3);
            session.setAttribute("income", revenue1 + revenue2 + revenue3 - expense1 - expense2);
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
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        Comparator<Posting> comparator = new Comparator<Posting>() {
            @Override
            public int compare(Posting o1, Posting o2) {
                int result = o1.getPostingDate().compareTo(o2.getPostingDate());
                if (result == 0) {
                    return o1.getPostingDate().before(o2.getPostingDate()) ? -1 : 1;
                } else {
                    return result;
                }
            }
        };

        acBook = accountingSessionBean.getAcBook(year);
        System.out.println(acBook);
        System.out.println(acBook.getPostings());
        List<Posting> postings = new ArrayList<Posting>();
        postings = acBook.getPostings();
        int size = 0;
        for (int i = 0; i < postings.size(); i++) {
            for (int j = 0; j < postings.get(i).getEntries().size(); j++) {
                size++;
            }
        }
                
        Collections.sort(postings, comparator);
        String[][] result = new String[size][5];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        int counter = 0;
        for (int i = 0; i < postings.size(); i++) {
            for (int j = 0; j < postings.get(i).getEntries().size(); j++) {
                result[counter][0] = sdf.format(postings.get(i).getPostingDate());
                result[counter][1] = postings.get(i).getEntries().get(j).getAccount().getAccountName();
                result[counter][2] = df.format(Math.abs(postings.get(i).getEntries().get(j).getAmount()));
                if (postings.get(i).getEntries().get(j).isDebit()) {
                    result[counter][3] = "Debit";
                } else {
                    result[counter][3] = "Credit";
                }
                result[counter][4] = postings.get(i).getMemo();
                counter++;
            }
        }
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            HttpSession session = request.getSession();

            //Set the required attributes of the report
            session.setAttribute("year", year);
            session.setAttribute("result", result);

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
        setYear(null);
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
