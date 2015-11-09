package Administration.Session;

import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Entity.BookEntry;
import Administration.Entity.Posting;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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
        account = addAccount("Fuel Expenses", year, Type.EXPENSE);
        accounts.add(account);

        if (getAcBooks() == null) {
            acBook.setActive(true);
        }

        acBook.setAccounts(accounts);
        em.merge(acBook);
        
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        posting = new Posting();
        posting.createPosting(tmp.getTime(), "Initialize Cash Account");
        em.persist(posting);

        account = acBook.getAccountByName("Cash");
        debit(account.getAccountId(), posting.getId(), cashAmt);
        
        posting = new Posting();
        posting.createPosting(tmp.getTime(), "Initialize Equity Account");
        em.persist(posting);
        
        account = acBook.getAccountByName("Retained Earnings");
        debit(account.getAccountId(), posting.getId(), retainedAmt);

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
    public Posting getPosting(Long postingId) {
        posting = new Posting();

        try {

            Query q = em.createQuery("SELECT a FROM Posting" + " AS a WHERE a.postingId=:postingId");
            q.setParameter("postingId", postingId);

            List results = q.getResultList();
            if (!results.isEmpty()) {
                posting = (Posting) results.get(0);

            } else {
                posting = null;
            }

        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }
        return posting;
    }

    @Override
    public List<Posting> getPostings() {
        postings = new ArrayList<Posting>();

        try {
            Query q = em.createQuery("SELECT a from Posting a");

            List<Posting> results = q.getResultList();
            if (!results.isEmpty()) {

                postings = results;

            } else {
                postings = null;
                System.out.println("no postings!");
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error" + "enfe.getMessage()");
        }

//        Collections.sort(postings, comparator);
        return postings;
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
        em.persist(posting);

        account = new BookAccount();
        acBook = getCurrBook();

        if (name.equals("AcquireAircraft")) {
            account = acBook.getAccountByName("Accounts Payable");
            credit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Property and Equipment");
            debit(account.getAccountId(), posting.getId(), amount);
        } else if (name.equals("CustomerBooking")) {
            account = acBook.getAccountByName("Cash");
            debit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Unearned Revenue");
            credit(account.getAccountId(), posting.getId(), amount);
        } else if (name.equals("CustomerCheckIn")) {
            account = acBook.getAccountByName("Unearned Revenue");
            debit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Ticket Sales Revenue");
            credit(account.getAccountId(), posting.getId(), amount);
        } else if (name.equals("TravelAgencyBooking")) {
            account = acBook.getAccountByName("Accounts Receivable");
            debit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Travel Agency Revenue");
            credit(account.getAccountId(), posting.getId(), amount);
        } else if (name.equals("TravelAgencyCfm")) {
            account = acBook.getAccountByName("Cash");
            debit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Accounts Receivable");
            credit(account.getAccountId(), posting.getId(), amount);
        } else if (name.equals("FlightFlown")) {
            account = acBook.getAccountByName("Cash");
            credit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Fuel Expenses");
            debit(account.getAccountId(), posting.getId(), amount);
        } else if (name.equals("MaintainanceDone")) {//What?
            account = acBook.getAccountByName("Cash");
            credit(account.getAccountId(), posting.getId(), amount);
            account = acBook.getAccountByName("Fuel Expenses");
            debit(account.getAccountId(), posting.getId(), amount);
        }
//Any maintanance costs? Other costs?
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
    public List<BookAccount> getRevenueAccounts(int year) {
        acBook = getAcBook(year);
        accounts = acBook.getAccounts();
        List<BookAccount> result = new ArrayList<BookAccount>();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getType().equals(Type.REVENUE)) {
                result.add(accounts.get(i));
            }
        }

        return result;
    }

    @Override
    public List<BookAccount> getExpenseAccounts(int year) {
        acBook = getAcBook(year);
        accounts = acBook.getAccounts();
        List<BookAccount> result = new ArrayList<BookAccount>();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getType().equals(Type.EXPENSE)) {
                result.add(accounts.get(i));
            }
        }

        return result;
    }

    @Override
    public List<BookAccount> getEditableAccounts() {
        accounts = new ArrayList<BookAccount>();

        try {
            Query q = em.createQuery("SELECT a FROM BookAccount " + " AS a WHERE a.accountName=:name1 OR a.accountName=:name2");
            q.setParameter("name1", "Cash");
            q.setParameter("name2", "Retained Earnings");

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

    private BookAccount addAccount(String name, int year, Type type) {
        account = new BookAccount();
        account.createAccount(name, type);
        account.setEntries(null);
        acBook = em.find(AccountingBook.class, year);
        account.setAcBook(acBook);
        em.persist(account);

        return account;
    }
//
//    @Override
//    public void deleteAccount(Long accountId) {
//        account = getAccount(accountId);
//        em.remove(account);
//    }

    private void credit(Long accountId, Long postingId, Double amt) {
        entry = new BookEntry();
        account = getAccount(accountId);
        posting = getPosting(postingId);
        entry.createEntry(account.getType().getNormalBalanceSign() * (-amt));
        entry.setCredit(true);
        account.getEntries().add(entry);
        posting.getEntries().add(entry);
        em.persist(entry);
        em.merge(account);
        em.merge(posting);
    }

    private void debit(Long accountId, Long postingId, Double amt) {
        entry = new BookEntry();
        account = getAccount(accountId);
        posting = getPosting(postingId);
        entry.createEntry(account.getType().getNormalBalanceSign() * (amt));
        entry.setDebit(true);
        account.getEntries().add(entry);
        posting.getEntries().add(entry);
        em.persist(entry);
        em.merge(account);
        em.merge(posting);
    }
}
