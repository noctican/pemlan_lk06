package exceptions;

public class BorrowLimitExceededException extends Exception {
    public BorrowLimitExceededException(String message) { super(message); }
}