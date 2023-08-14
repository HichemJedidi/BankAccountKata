package services;

import dtos.AccountDTO;
import dtos.OperationDTO;
import entities.Account;
import entities.Operation;
import enums.AccountStatus;
import exeptions.NoSuchAccountException;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import repositories.BankAccountRepository;
import util.ObjectMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;

    public AccountDTO createAccount(AccountDTO accountDTO){
        Account account = ObjectMapper.map(accountDTO, Account.class);
        return ObjectMapper.map(bankAccountRepository.save(account), AccountDTO.class);

    }
    public List<AccountDTO> getAllAccounts(){
        List<Account> accounts = bankAccountRepository.findAll();
        List<AccountDTO> accountDTOs = new ArrayList<>();
        for (Account account : accounts) {
            AccountDTO accountDTO = ObjectMapper.map(account, AccountDTO.class);
            accountDTOs.add(accountDTO);
        }

        return accountDTOs;


    }
    public void activateAccount(String accountId) throws NoSuchAccountException {
        Optional<Account> optionalAccount = bankAccountRepository.findById(accountId);

        if (!optionalAccount.isEmpty()) {
            throw new NoSuchAccountException(": " + accountId);
        }

        Account account = optionalAccount.get();
        account.setStatus(AccountStatus.ACTIVATED);

        bankAccountRepository.save(account);
    }
    public AccountDTO printStatement(String accountId) throws NoSuchAccountException {
        Optional<Account> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isEmpty()){
            throw new NoSuchAccountException(": "+accountId);
        }
        return ObjectMapper.map(optionalBankAccount.get(),AccountDTO.class);
    }

    public List<OperationDTO> listAllOperations(String accountId) throws NoSuchAccountException {
        Optional<Account> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isEmpty()){
            throw new NoSuchAccountException(": "+accountId);
        }
        List<OperationDTO> operationDTOs = null;
        for (Operation operation : optionalBankAccount.get().getOperations()) {
            OperationDTO operationDTO = ObjectMapper.map(operation, OperationDTO.class);
            operationDTOs.add(operationDTO);
        }

        return operationDTOs;

    }
}
