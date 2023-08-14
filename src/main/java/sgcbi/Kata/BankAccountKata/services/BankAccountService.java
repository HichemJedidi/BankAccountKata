package sgcbi.Kata.BankAccountKata.services;

import sgcbi.Kata.BankAccountKata.dtos.AccountDTO;
import sgcbi.Kata.BankAccountKata.dtos.OperationDTO;
import sgcbi.Kata.BankAccountKata.entities.Account;
import sgcbi.Kata.BankAccountKata.entities.Operation;
import sgcbi.Kata.BankAccountKata.enums.AccountStatus;
import sgcbi.Kata.BankAccountKata.exeptions.NoSuchAccountException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sgcbi.Kata.BankAccountKata.repositories.BankAccountRepository;
import sgcbi.Kata.BankAccountKata.util.ObjectMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;

    public AccountDTO createAccount(AccountDTO accountDTO){
        Account account = ObjectMapper.map(accountDTO, Account.class);
        account.setStatus(AccountStatus.CREATED);
        account.setId( UUID.randomUUID().toString());
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
