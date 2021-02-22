package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private Context ctx;
    public PersistentExpenseManager(Context ctx){
        this.ctx = ctx;
        setup();
    }
    @Override
    public void setup(){
        SQLiteDatabase expensedatabase = ctx.openOrCreateDatabase("expenseManger_DB", ctx.MODE_PRIVATE, null);

        expensedatabase.execSQL("CREATE TABLE IF NOT EXISTS accounts(" +
                "account_no VARCHAR PRIMARY KEY," +
                "bank_name VARCHAR," +
                "holder_name VARCHAR," +
                "balance REAL" +
                " );");

        expensedatabase.execSQL("CREATE TABLE IF NOT EXISTS transaction_details(" +
                "transaction_no INTEGER PRIMARY KEY," +
                "account_no VARCHAR," +
                "type INT," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (account_no) REFERENCES Account(account_no)" +
                ");");


        PersistentAccountDAO accDAO = new PersistentAccountDAO(expensedatabase);

        setAccountsDAO(accDAO);

        setTransactionsDAO(new PersistentTransactionDAO(expensedatabase));
    }
}

