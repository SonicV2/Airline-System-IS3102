/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Session;

import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import Administration.Entity.BookEntry;
import Administration.Entity.Posting;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Family
 */
@Local
public interface AccountingSessionBeanLocal {

    public void makeTransaction(String name);

    public void changeAcBookYear(int originalYear, int newYear);

    public List<AccountingBook> getAcBooks();

    public List<Posting> getPostingsByDate();

    public Posting getPosting(Long postingId);

    public BookAccount getAccount(Long accountId);

    public void deleteAccount(Long accountId);

    public BookAccount addAccount(String name, int year, Type type);

    public void createBook(int year);

    public List<BookAccount> getAccounts();


}
