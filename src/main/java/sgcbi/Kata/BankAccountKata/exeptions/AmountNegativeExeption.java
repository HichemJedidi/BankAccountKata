package sgcbi.Kata.BankAccountKata.exeptions;

public class AmountNegativeExeption extends RuntimeException {
    public AmountNegativeExeption(String message) {
        super(message);
    }
}
