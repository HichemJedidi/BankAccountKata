package sgcbi.Kata.BankAccountKata.exeptions;

public class NoSuchAccountException extends Exception {

    String message;

    public NoSuchAccountException(String message) {
        super(message);
        this.message = message;
    }
}