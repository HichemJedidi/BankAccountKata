package services;

import dtos.AccountDTO;
import entities.Operation;
import enums.OperationType;
import exeptions.NoSuchAccountException;
import org.springframework.stereotype.Service;

@Service
public class OperationServices {
    public AccountDTO deposit(String accountId, long amount){

        return null;
    }
    public AccountDTO withdraw(String accountId, long amount){
        return null;
    }
    public Operation createAndPerformOperation(String accountId, long amount, OperationType operationType) throws NoSuchAccountException {
        return null;

    }
}
