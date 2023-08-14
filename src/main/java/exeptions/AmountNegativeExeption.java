package exeptions;

public class AmountNegativeExeption extends RuntimeException {
    public AmountNegativeExeption(String message) {
        super(message);
    }
}
