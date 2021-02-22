package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase db){
        this.database = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor accSet = database.rawQuery("SELECT account_no FROM accounts",null);
        List<String> accountNumbers = new ArrayList<String>();
        if(accSet.moveToFirst()) {
            do {
                accountNumbers.add(accSet.getString(accSet.getColumnIndex("account_no")));
            } while (accSet.moveToNext());
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor accSet = database.rawQuery("SELECT * FROM accounts",null);
        List<Account> accounts = new ArrayList<Account>();

        if(accSet.moveToFirst()) {
            do {
                Account account = new Account(accSet.getString(accSet.getColumnIndex("account_no")),
                        accSet.getString(accSet.getColumnIndex("bank_name")),
                        accSet.getString(accSet.getColumnIndex("holder_name")),
                        accSet.getDouble(accSet.getColumnIndex("balance")));
                accounts.add(account);
            } while (accSet.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor accSet = database.rawQuery("SELECT * FROM accounts WHERE account_no = " + accountNo,null);
        Account account = null;

        if(accSet.moveToFirst()) {
            do {
                account = new Account(accSet.getString(accSet.getColumnIndex("account_no")),
                        accSet.getString(accSet.getColumnIndex("bank_name")),
                        accSet.getString(accSet.getColumnIndex("holder_name")),
                        accSet.getDouble(accSet.getColumnIndex("balance")));
            } while (accSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        String prepStat = "INSERT INTO accounts (account_no,bank_name,holder_name,balance) VALUES (?,?,?,?)";
        SQLiteStatement sqlStatement = database.compileStatement(prepStat);

        sqlStatement.bindString(1, account.getAccountNo());
        sqlStatement.bindString(2, account.getBankName());
        sqlStatement.bindString(3, account.getAccountHolderName());
        sqlStatement.bindDouble(4, account.getBalance());

        sqlStatement.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String prepStat = "DELETE FROM accounts WHERE account_no = ?";
        SQLiteStatement sqlStatement = database.compileStatement(prepStat);

        sqlStatement.bindString(1,accountNo);

        sqlStatement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String prepStat = "UPDATE accounts SET balance = balance + ? WHERE account_no = "+accountNo;
        SQLiteStatement sqlStatement = database.compileStatement(prepStat);
        if(expenseType == ExpenseType.EXPENSE){
            sqlStatement.bindDouble(1,-amount);
        }else{
            sqlStatement.bindDouble(1,amount);
        }

        sqlStatement.executeUpdateDelete();
    }

    @Override
    public double getBalance(String accountNo){
        Cursor accSet = database.rawQuery("SELECT balance FROM accounts WHERE account_no="+accountNo,null);
        double balance = 0;
        if(accSet.moveToFirst()) {
            balance = accSet.getDouble(accSet.getColumnIndex("balance"));
        }
        return balance;
    }

}