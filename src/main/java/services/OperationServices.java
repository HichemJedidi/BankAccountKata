package services;

import dtos.AccountDTO;
import dtos.OperationDTO;
import entities.Account;
import entities.Operation;
import enums.OperationType;
import exeptions.AmountNegativeExeption;
import exeptions.BalanceNotSufficentExeption;
import exeptions.NoSuchAccountException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.BankAccountRepository;
import repositories.OperationRepository;
import util.ObjectMapper;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class OperationServices {
    private OperationRepository operationRepository;
    private BankAccountRepository bankAccountRepository;
    public AccountDTO deposit(String accountId, long amount) throws NoSuchAccountException{

        OperationDTO operationDTO = createAndPerformOperation(accountId,amount,OperationType.DEPOSIT);
        if(!bankAccountRepository.findById(accountId).isEmpty()){
            throw new NoSuchAccountException(": "+accountId);
        }
        Account bankAccount = bankAccountRepository.findById(accountId).get();

        bankAccount.getOperations().add(ObjectMapper.map(operationDTO,Operation.class));
        return ObjectMapper.map(bankAccount,AccountDTO.class);
    }
    public AccountDTO withdraw(String accountId, long amount) throws NoSuchAccountException{
        OperationDTO operationDTO = createAndPerformOperation(accountId,amount,OperationType.WITHDRAW);
        if(!bankAccountRepository.findById(accountId).isEmpty()){
            throw new NoSuchAccountException(": "+accountId);
        }
        Account bankAccount = bankAccountRepository.findById(accountId).get();
        bankAccount.getOperations().add(ObjectMapper.map(operationDTO,Operation.class));
        return ObjectMapper.map(bankAccount,AccountDTO.class);
    }
    public OperationDTO createAndPerformOperation(String accountId, long amount, OperationType operationType) throws NoSuchAccountException {
        Optional<Account> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isEmpty()){
            throw new NoSuchAccountException(": "+accountId);
        }
        Account account = optionalBankAccount.get();
        if (amount<0) throw new AmountNegativeExeption("le montant ne doit pas etre negative");
        if (operationType.equals(OperationType.DEPOSIT)&&account.getBalance()<amount) throw new BalanceNotSufficentExeption("le montant est inssufisant=>"+account.getBalance());



        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setDate(Instant.now());

        operation.setType(operationType);

        double newBalance = account.getBalance() + (operationType == OperationType.WITHDRAW ? -amount : amount);
        account.setBalance(newBalance);
        operation.setAccount(account);
        operationRepository.save(operation);

        return ObjectMapper.map(operation,OperationDTO.class);

    }
}
