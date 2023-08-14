package services;

import dtos.AccountDTO;
import dtos.OperationDTO;
import entities.Account;
import entities.Operation;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    public AccountDTO createAccount(AccountDTO accountDTO){
        return null;

    }
    public List<AccountDTO> getAllAccounts(){
        return null;

    }
    public List<OperationDTO> listAllAccountOperations(String accountId){
        return null;
    }
    public AccountDTO printStatement(String accountId){
        return null;
    }

    public List<OperationDTO> listAllOperations(String accountId) {
        return null;
    }
}
