package Administration.Session;

import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Entity.BookEntry;
import Administration.Entity.Posting;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    public void createBook(int year) {
        acBook = new AccountingBook();
        acBook.setYear(year);
        account = new BookAccount();
        accounts = new ArrayList<BookAccount>();

        account = addAccount("Cash", year, Type.ASSET);
        accounts.add(account);
        account = addAccount("Accounts Receivable", year, Type.ASSET);
        accounts.add(account);
        account = addAccount("Prepaid Expense", year, Type.ASSET);
        accounts.add(account);
        account = addAccount("Property and Equipment", year, Type.ASSET);
        accounts.add(account);
        //Salaries?
        account = addAccount("Accounts Payable", year, Type.LIABILITY);
        accounts.add(account);
        account = addAccount("Retained Earnings", year, Type.EQUITY);
        accounts.add(account);
        account = addAccount("Ticket Sales Revenue", year, Type.INCOME);
        accounts.add(account);
        account = addAccount("Expenses", year, Type.EXPENSE);
        accounts.add(account);

        acBook.setAccounts(accounts);
        em.persist(acBook);
    }

    @Override
    public BookAccount addAccount(String name, int year, Type type) {
        account = new BookAccount();
        account.createAccount(name, type);
        account.setEntries(null);
        acBook = em.find(AccountingBook.class, year);
        account.setAcBook(acBook);
        em.persist(account);

        return account;
    }

    @Override
    public void deleteAccount(Long accountId) {
        account = getAccount(accountId);
        em.remove(account);
    }

    @Override
    public BookAccount getAccount(Long accountId) {
        account = new BookAccount();
        try {

            Query q = em.createQuery("SELECT a FROM Account " + "AS a WHERE a.accountId=:accountId");
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
    public Posting getPosting(Long postingId) {
        posting = new Posting();

        try {

            Query q = em.createQuery("SELECT a FROM Posting" + "AS a WHERE a.postingId=:postingId");
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
    public List<Posting> getPostingsByDate() {
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

        Collections.sort(postings, comparator);
        return postings;
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
        AccountingBook tmp = em.find(AccountingBook.class, newYear);
        acBook.setActive(false);
        tmp.setActive(true);
        em.merge(acBook);
        em.merge(tmp);
        em.flush();
    }

    @Override
    public void makeTransaction(String name) {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00"); //Set Timezone to Singapore
        Calendar tmp = Calendar.getInstance(tz);
        posting = new Posting();
        posting.createPosting(tmp.getTime(), name);
        
        if (name.equals("AcquireAircraft")) {
            
        }
    }
    
    private AccountingBook getCurrBook(){
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
    
    private void credit(Long accountId, Long postingId, Double amt) {
        entry = new BookEntry();
        account = getAccount(accountId);
        posting = getPosting(postingId);
        entry.createEntry(account.getType().getNormalBalanceSign()*(-amt));
        account.getEntries().add(entry);
        
        
        em.persist(entry);
    }
    
}
