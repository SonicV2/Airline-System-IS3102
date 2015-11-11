/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administration.Session;

import Administration.Entity.BookAccount;
import Administration.Entity.BookAccount.Type;
import Administration.Entity.AccountingBook;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Family
 */
@Local
public interface AccountingSessionBeanLocal {

    public void createBook(int year, double cashAmt, double retainedAmt);

    public BookAccount getAccount(Long accountId);

    public List<BookAccount> getAccounts();

    public List<BookAccount> getAccountsByType(Type type);

    public AccountingBook getAcBook(int year);

    public List<AccountingBook> getAcBooks();

    public void changeAcBookYear(int originalYear, int newYear);

    public void makeTransaction(String name, double amount);

    public AccountingBook getCurrBook();

    public List<Integer> getValidYears();

    public void recognizeFuelExpenses(int year);

    public void recognizeMaintenanceExpenses(int year);

}
