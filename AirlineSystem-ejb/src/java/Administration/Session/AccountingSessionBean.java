package Administration.Session;

import APS.Entity.Schedule;
import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Entity.BookEntry;
import Administration.Entity.Posting;
import FOS.Entity.MaintainSchedule;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Yanlong
 */
@Stateless
public class AccountingSessionBean implements AccountingSessionBeanLocal {

    @PersistenceContext(unitName = "AirlineSystem-ejbPU")
    private EntityManager em;

    private BookAccount account;
    private List<BookAccount> accounts;
    private BookEntry entry;
    private Posting posting;
    private List<Posting> postings;
    private AccountingBook acBook;
    private List<AccountingBook> acBooks;

    //Create comparator for sorting of Entries according to starting time
    private Comparator<Posting> comparator = new Comparator<Posting>() {
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

    @Override
    public void createBook(int year, double cashAmt, double retainedAmt) {
        acBook = new AccountingBook();
        acBook.setYear(year);
        if (getAcBooks() == null) {
            acBook.setActive(true);
        }
        em.persist(acBook);

        //Set up the accounts
        accounts = new ArrayList<BookAccount>();
        account = new BookAccount();
        account = addAccount("Cash", year, Type.ASSET);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Accounts Receivable", year, Type.ASSET);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Prepaid Expense", year, Type.ASSET);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Property and Equipment", year, Type.ASSET);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Accounts Payable", year, Type.LIABILITY);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Unearned Revenue", year, Type.LIABILITY);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Retained Earnings", year, Type.EQUITY);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Ticket Sales Revenue", year, Type.REVENUE);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Travel Agency Revenue", year, Type.REVENUE);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Misc Revenue", year, Type.REVENUE);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Fuel Expenses", year, Type.EXPENSE);
        accounts.add(account);
        account = new BookAccount();
        account = addAccount("Maintenance Expenses", year, Type.EXPENSE);
        accounts.add(account);

        acBook.setAccounts(accounts);
        em.merge(acBook);

        acBook = getAcBook(year);

        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);

        posting = new Posting();
        posting.createPosting(tmp.getTime(), "Initialize Cash Account");
        posting.setEntries(null);
        posting.setAcBook(acBook);

        account = acBook.getAccountByName("Cash");
        debit(account, posting, cashAmt);

        posting = new Posting();
        posting.createPosting(tmp.getTime(), "Initialize Equity Account");
        posting.setEntries(null);
        posting.setAcBook(acBook);

        account = acBook.getAccountByName("Retained Earnings");
        credit(account, posting, retainedAmt);

    }

    @Override
    public BookAccount getAccount(Long accountId) {
        account = new BookAccount();
        try {

            Query q = em.createQuery("SELECT a FROM BookAccount " + " AS a WHERE a.accountId=:accountId");
            q.setParameter("accountId", accountId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                account = (BookAccount) results.get(0);

            } else {
                account = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return account;
    }

    @Override
    public List<BookAccount> getAccounts() {
        accounts = new ArrayList<BookAccount>();

        try {
            Query q = em.createQuery("SELECT a from BookAccount a");

            List<BookAccount> results = q.getResultList();
            if (!results.isEmpty()) {

                accounts = results;

            } else {
                accounts = null;
                System.out.println("no accounts!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return accounts;
    }

    @Override
    public List<BookAccount> getAccountsByType(Type type) {
        accounts = new ArrayList<BookAccount>();

        try {
            Query q = em.createQuery("SELECT a FROM BookAccount " + " AS a WHERE a.type=:type");
            q.setParameter("type", type);

            List<BookAccount> results = q.getResultList();
            if (!results.isEmpty()) {

                accounts = results;

            } else {
                accounts = null;
                System.out.println("no accounts!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return accounts;
    }

    @Override
    public AccountingBook getAcBook(int year) {
        acBook = new AccountingBook();

        try {

            Query q = em.createQuery("SELECT a FROM AccountingBook" + " AS a WHERE a.year=:year");
            q.setParameter("year", year);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                acBook = (AccountingBook) results.get(0);

            } else {
                acBook = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return acBook;
    }

    @Override
    public List<AccountingBook> getAcBooks() {
        acBooks = new ArrayList<AccountingBook>();

        try {
            Query q = em.createQuery("SELECT a from AccountingBook a");

            List<AccountingBook> results = q.getResultList();
            if (!results.isEmpty()) {

                acBooks = results;

            } else {
                acBooks = null;
                System.out.println("no accounting books!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

        return acBooks;
    }

    @Override
    public void changeAcBookYear(int originalYear, int newYear) {
        acBook = em.find(AccountingBook.class, originalYear);
        AccountingBook curr = em.find(AccountingBook.class, newYear);
        acBook.setActive(false);
        curr.setActive(true);
        em.merge(acBook);
        em.merge(curr);
        em.flush();
    }

    @Override
    public void makeTransaction(String name, double amount) {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        posting = new Posting();
        posting.createPosting(tmp.getTime(), name);
        acBook = getCurrBook();
        posting.setAcBook(acBook);
        posting.setEntries(null);

        account = new BookAccount();

        if (name.equals("Acquire Aircraft")) {
            account = acBook.getAccountByName("Accounts Payable");
            System.out.println(account);
            credit(account, posting, amount);
            account = acBook.getAccountByName("Property and Equipment");
            debit(account, posting, amount);
        } else if (name.equals("Customer Booking")) {
            account = acBook.getAccountByName("Cash");
            debit(account, posting, amount);
            account = acBook.getAccountByName("Unearned Revenue");
            credit(account, posting, amount);
        } else if (name.equals("Customer CheckIn")) {
            account = acBook.getAccountByName("Unearned Revenue");
            debit(account, posting, amount);
            account = acBook.getAccountByName("Ticket Sales Revenue");
            credit(account, posting, amount);
        } else if (name.equals("Counter Payments")) {
            account = acBook.getAccountByName("Cash");
            debit(account, posting, amount);
            account = acBook.getAccountByName("Misc Revenue");
            credit(account, posting, amount);
        } else if (name.equals("Travel Agency Booking")) {
            account = acBook.getAccountByName("Accounts Receivable");
            debit(account, posting, amount);
            account = acBook.getAccountByName("Travel Agency Revenue");
            credit(account, posting, amount);
        } else if (name.equals("Travel Agency Confirm Payment")) {
            account = acBook.getAccountByName("Cash");
            debit(account, posting, amount);
            account = acBook.getAccountByName("Accounts Receivable");
            credit(account, posting, amount);
        }
    }

    @Override
    public AccountingBook getCurrBook() {
        acBook = new AccountingBook();
        try {

            Query q = em.createQuery("SELECT a FROM AccountingBook " + "AS a WHERE a.active=:active");
            q.setParameter("active", true);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                acBook = (AccountingBook) results.get(0);

            } else {
                acBook = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return acBook;
    }

    @Override
    public List<Integer> getValidYears() {
        List<Integer> result = new ArrayList<Integer>();
        acBooks = new ArrayList<AccountingBook>();
        acBooks = getAcBooks();
        if (acBooks != null) {
            for (int i = 0; i < acBooks.size(); i++) {
                result.add(acBooks.get(i).getYear());
            }
        }
        return result;
    }

    @Override
    public void recognizeFuelExpenses(int year) {
        List<Schedule> schedules = new ArrayList();
        List<Schedule> result = new ArrayList();
        schedules = getSchedules();
        if (schedules != null) {
            Comparator<Schedule> comparator = new Comparator<Schedule>() {
                @Override
                public int compare(Schedule o1, Schedule o2) {
                    int result = o1.getStartDate().compareTo(o2.getStartDate());
                    if (result == 0) {
                        return o1.getStartDate().before(o2.getStartDate()) ? -1 : 1;
                    } else {
                        return result;
                    }
                }
            };
            TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
            Calendar tmp = Calendar.getInstance(tz);
            Calendar curr = Calendar.getInstance(tz);

            //Find the latest posting regarding fuel
            postings = new ArrayList<Posting>();
            postings = acBook.getPostings();

            tmp.set(Calendar.YEAR, year - 1);
            tmp.set(Calendar.MONTH, 11);
            tmp.set(Calendar.DATE, 31);
            Date latestDate = tmp.getTime();
            for (int i = 0; i < postings.size(); i++) {
                if (postings.get(i).getPostingDate().after(latestDate) && postings.get(i).getMemo().equals("Flight Flown")) {
                    latestDate = postings.get(i).getPostingDate();
                }
            }

            //Find out the relevant schedules
            Collections.sort(schedules, comparator);
            for (int i = 0; i < schedules.size(); i++) {
                tmp.setTime(schedules.get(i).getEndDate());
                if (tmp.get(Calendar.YEAR) == year && tmp.getTime().after(latestDate) && tmp.before(curr) && schedules.get(i).getFlight().getRoute() != null) {
                    result.add(schedules.get(i));
                }
            }
            
            for (int i = 0; i < result.size(); i++) {
                makeExpense("Flight Flown", result.get(i).getEndDate(), result.get(i).getFlight().getRoute().getDistance() * result.get(i).getFlight().getAircraftType().getFuelCost(), year);
            }
        }
    }

    @Override
    public void recognizeMaintenanceExpenses(int year) {
        List<MaintainSchedule> schedules = new ArrayList();
        List<MaintainSchedule> result = new ArrayList();
        schedules = getMaintainSchedules();
        if (schedules != null) {
            Comparator<MaintainSchedule> comparator = new Comparator<MaintainSchedule>() {
                @Override
                public int compare(MaintainSchedule o1, MaintainSchedule o2) {
                    int result = o1.getMainStartDate().compareTo(o2.getMainStartDate());
                    if (result == 0) {
                        return o1.getMainStartDate().before(o2.getMainStartDate()) ? -1 : 1;
                    } else {
                        return result;
                    }
                }
            };
            TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
            Calendar tmp = Calendar.getInstance(tz);
            Calendar curr = Calendar.getInstance(tz);

            //Find the latest posting regarding fuel
            postings = new ArrayList<Posting>();
            postings = acBook.getPostings();

            tmp.set(Calendar.YEAR, year - 1);
            tmp.set(Calendar.MONTH, 11);
            tmp.set(Calendar.DATE, 31);
            Date latestDate = tmp.getTime();
            for (int i = 0; i < postings.size(); i++) {
                if (postings.get(i).getPostingDate().after(latestDate) && postings.get(i).getMemo().equals("Maintenance Done")) {
                    latestDate = postings.get(i).getPostingDate();
                }
            }

            //Find out the relevant schedules
            Collections.sort(schedules, comparator);
            for (int i = 0; i < schedules.size(); i++) {
                tmp.setTime(schedules.get(i).getMainEndDate());
                if (tmp.get(Calendar.YEAR) == year && tmp.getTime().after(latestDate) && tmp.before(curr)) {
                    result.add(schedules.get(i));
                }
            }

            //Rate for maintenance: $710/Hr
            for (int i = 0; i < result.size(); i++) {
                makeExpense("Maintenance Done", result.get(i).getMainEndDate(), 710 * ((result.get(i).getMainEndDate().getTime() - result.get(i).getMainStartDate().getTime()) / (60 * 60 * 1000)), year);
            }
        }
    }

    private BookAccount addAccount(String name, int year, Type type) {
        account = new BookAccount();
        account.createAccount(name, type);
        account.setEntries(null);
        acBook = em.find(AccountingBook.class, year);
        account.setAcBook(acBook);
        em.persist(account);

        return account;
    }

    private void makeExpense(String name, Date date, double amount, int year) {
        posting = new Posting();
        posting.createPosting(date, name);
        acBook = new AccountingBook();
        acBook = getAcBook(year);
        posting.setAcBook(acBook);
        posting.setEntries(null);

        account = new BookAccount();

        if (name.equals("Flight Flown")) {
            account = acBook.getAccountByName("Cash");
            credit(account, posting, amount);
            account = acBook.getAccountByName("Fuel Expenses");
            debit(account, posting, amount);
        } else if (name.equals("Maintenance Done")) {
            account = acBook.getAccountByName("Cash");
            credit(account, posting, amount);
            account = acBook.getAccountByName("Maintenance Expenses");
            debit(account, posting, amount);
        }
    }

    private void credit(BookAccount account, Posting posting, Double amt) {
        entry = new BookEntry();
        List<BookEntry> entries = new ArrayList<BookEntry>();
        entry.createEntry(account.getType().getNormalBalanceSign() * (-amt));
        entry.setCredit(true);
        entry.setAccount(account);
        entry.setPosting(posting);
        if (account.getEntries() != null) {
            account.getEntries().add(entry);
        } else {
            entries.add(entry);
            account.setEntries(entries);
        }

        if (posting.getEntries() != null) {
            posting.getEntries().add(entry);
        } else {
            entries.add(entry);
            posting.setEntries(entries);
        }
        em.persist(entry);
        em.merge(posting);
        em.merge(account);
        em.flush();
    }

    private void debit(BookAccount account, Posting posting, Double amt) {
        entry = new BookEntry();
        List<BookEntry> entries = new ArrayList<BookEntry>();
        entry.createEntry(account.getType().getNormalBalanceSign() * (amt));
        entry.setDebit(true);
        entry.setAccount(account);
        entry.setPosting(posting);
        if (account.getEntries() != null) {
            account.getEntries().add(entry);
        } else {
            entries.add(entry);
            account.setEntries(entries);
        }

        if (posting.getEntries() != null) {
            posting.getEntries().add(entry);
        } else {
            entries.add(entry);
            posting.setEntries(entries);
        }
        em.persist(entry);
        em.merge(posting);
        em.merge(account);
        em.flush();
    }

    private List<Schedule> getSchedules() {
        List<Schedule> schedules = new ArrayList<Schedule>();
        try {

            Query q = em.createQuery("SELECT a FROM Schedule a");

            List<Schedule> results = q.getResultList();
            if (!results.isEmpty()) {
                schedules = results;

            } else {
                schedules = null;
                System.out.println("No Schedules Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return schedules;
    }

    private List<MaintainSchedule> getMaintainSchedules() {
        List<MaintainSchedule> schedules = new ArrayList<MaintainSchedule>();
        try {

            Query q = em.createQuery("SELECT a FROM MaintainSchedule a");

            List<MaintainSchedule> results = q.getResultList();
            if (!results.isEmpty()) {
                schedules = results;

            } else {
                schedules = null;
                System.out.println("No MaintainSchedules Added!");
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return schedules;
    }
}
