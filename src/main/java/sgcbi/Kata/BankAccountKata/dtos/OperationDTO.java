package sgcbi.Kata.BankAccountKata.dtos;


import sgcbi.Kata.BankAccountKata.entities.Account;
import sgcbi.Kata.BankAccountKata.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Data
@NoArgsConstructor@AllArgsConstructor
public class OperationDTO {
    private Long id;
    private Instant date;
    private double amount;

    private OperationType type;

    private Account account;
}
