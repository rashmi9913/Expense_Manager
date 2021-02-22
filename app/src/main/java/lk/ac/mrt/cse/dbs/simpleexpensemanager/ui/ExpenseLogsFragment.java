package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_MANAGER;

public class ExpenseLogsFragment extends Fragment {
    private ExpenseManager currentExpenseManager;

    public static ExpenseLogsFragment newInstance(ExpenseManager expenseManager) {
        ExpenseLogsFragment expenseLogsFragment = new ExpenseLogsFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXPENSE_MANAGER, expenseManager);
        expenseLogsFragment.setArguments(args);
        return expenseLogsFragment;
    }

    public ExpenseLogsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense_logs, container, false);
        TableLayout logsTableLayout = (TableLayout) rootView.findViewById(R.id.logs_table);
        TableRow tableRowHeader = (TableRow) rootView.findViewById(R.id.logs_table_header);
        TextView balance = (TextView) rootView.findViewById(R.id.balance);
        currentExpenseManager = (ExpenseManager) getArguments().get(EXPENSE_MANAGER);
        List<Transaction> transactionList = new ArrayList<>();
        List<String> accountNumbersList = new ArrayList<>();
        if (currentExpenseManager != null) {
            transactionList = currentExpenseManager.getTransactionLogs();
            accountNumbersList = currentExpenseManager.getAccountNumbersList();
        }
        String balanceList = "";
        for (String accountNo : accountNumbersList) {
            balanceList+="Account No: "+accountNo+" balance is Rs. "+(String.valueOf(currentExpenseManager.getBalance(accountNo)))+"\n";
        }
        balance.setText((CharSequence) balanceList);
        generateTransactionsTable(rootView, logsTableLayout, transactionList);
        return rootView;
    }

    private void generateTransactionsTable(View rootView, TableLayout logsTableLayout,
                                           List<Transaction> transactionList) {
        currentExpenseManager = (ExpenseManager) getArguments().get(EXPENSE_MANAGER);
        for (Transaction transaction : transactionList) {
            TableRow tr = new TableRow(rootView.getContext());
            TextView lDateVal = new TextView(rootView.getContext());

            SimpleDateFormat sdf = new SimpleDateFormat(getActivity().getString(R.string.config_date_log_pattern));
            String formattedDate = sdf.format(transaction.getDate());
            lDateVal.setText(formattedDate);
            lDateVal.setTextColor(0xFFFFFFFF);
            lDateVal.setTextSize(16);
            lDateVal.setTextAlignment(4);
            tr.addView(lDateVal);

            TextView lAccountNoVal = new TextView(rootView.getContext());
            lAccountNoVal.setText(transaction.getAccountNo());
            lAccountNoVal.setTextColor(0xFFFFFFFF);
            lAccountNoVal.setTextAlignment(4);
            tr.addView(lAccountNoVal);

            TextView lExpenseTypeVal = new TextView(rootView.getContext());
            lExpenseTypeVal.setText(transaction.getExpenseType().toString());
            lExpenseTypeVal.setTextColor(0xFFFFFFFF);
            lExpenseTypeVal.setTextAlignment(4);
            tr.addView(lExpenseTypeVal);

            TextView lAmountVal = new TextView(rootView.getContext());
            lAmountVal.setText(String.valueOf(transaction.getAmount()));
            lAmountVal.setTextColor(0xFFFFFFFF);
            lAmountVal.setTextAlignment(4);
            tr.addView(lAmountVal);

            logsTableLayout.addView(tr);
        }
    }
}
