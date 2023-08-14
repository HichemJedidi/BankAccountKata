package sgcbi.Kata.BankAccountKata.exeptions;

public class BalanceNotSufficentExeption extends RuntimeException {
    public BalanceNotSufficentExeption(String message) {
        super(message);
    }
}
