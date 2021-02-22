package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public abstract class ExpenseManager implements Serializable {
    private AccountDAO accountsHolder;
    private TransactionDAO transactionsHolder;

    public List<String> getAccountNumbersList() {
        return accountsHolder.getAccountNumbersList();
    }
    public void updateAccountBalance(String accountNo, int day, int month, int year, ExpenseType expenseType,
                                     String amount) throws InvalidAccountException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date transactionDate = calendar.getTime();

        if (!amount.isEmpty()) {
            double amountVal = Double.parseDouble(amount);
            accountsHolder.updateBalance(accountNo, expenseType, amountVal);
            transactionsHolder.logTransaction(transactionDate, accountNo, expenseType, amountVal);
        }
    }

    public List<Transaction> getTransactionLogs() {
        return transactionsHolder.getPaginatedTransactionLogs(100);
    }

    public void addAccount(String accountNo, String bankName, String accountHolderName, double initialBalance) {
        Account account = new Account(accountNo, bankName, accountHolderName, initialBalance);
        accountsHolder.addAccount(account);
    }

    public AccountDAO getAccountsDAO() {
        return accountsHolder;
    }

    public void setAccountsDAO(AccountDAO accountDAO) {
        this.accountsHolder = accountDAO;
    }

    public TransactionDAO getTransactionsDAO() {
        return transactionsHolder;
    }

    public void setTransactionsDAO(TransactionDAO transactionDAO) {
        this.transactionsHolder = transactionDAO;
    }

    public abstract void setup() throws ExpenseManagerException;

    public double getBalance(String accountNo) {
        return accountsHolder.getBalance(accountNo);
    }
}
