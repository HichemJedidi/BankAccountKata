package dtos;


import entities.Account;
import enums.OperationType;
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
