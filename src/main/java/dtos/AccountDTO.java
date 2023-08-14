package dtos;

import entities.Operation;
import enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
@Data
@NoArgsConstructor@AllArgsConstructor
public class AccountDTO {
    private String id;
    private double balance;
    private AccountStatus status;
    private String currency;
    private Collection<Operation> operations;
}
