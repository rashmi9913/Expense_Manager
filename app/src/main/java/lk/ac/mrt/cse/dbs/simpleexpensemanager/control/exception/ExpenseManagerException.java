package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

public class ExpenseManagerException extends Exception {
    public ExpenseManagerException(String detailMessage) {
        super(detailMessage);
    }

    public ExpenseManagerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
