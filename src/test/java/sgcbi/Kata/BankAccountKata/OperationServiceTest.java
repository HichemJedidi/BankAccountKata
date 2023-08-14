package sgcbi.Kata.BankAccountKata;

import dtos.AccountDTO;
import entities.Account;
import entities.Operation;
import enums.OperationType;
import exeptions.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import repositories.BankAccountRepository;
import repositories.OperationRepository;
import services.OperationServices;

import java.time.Instant;
import java.util.Optional;


import org.assertj.core.api.Assertions;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class OperationServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private OperationRepository operationRepository;



    @Spy
    private OperationServices operationService;

    private Account account ;
    private Operation operation;
    @Before
    public void setUp(){
        account = new Account();
        account.setBalance(5000);
        account.setId("123");
        operation = new Operation(12L,Instant.now(),1000,OperationType.DEPOSIT,account);
    }

    @Test(expected = NoSuchAccountException.class)
    public void createAndPerformOperation_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.empty());
        operationService.createAndPerformOperation("123",0,OperationType.WITHDRAW);
        Assert.fail("should have thrown NoSuchAccountException ");

    }

    @Test
    public void createAndPerformOperation_should_perform_deposit() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(account));
        double currentAccountBalance = account.getBalance();
        Operation operation = operationService.createAndPerformOperation("123",1000,OperationType.DEPOSIT);
        Assertions.assertThat(operation.getAmount()).isEqualTo(1000);
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        Assertions.assertThat(operation.getAccount()).isNotNull();
        Assertions.assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance+1000);
    }

    @Test
    public void createAndPerformOperation_should_perform_withdrawal() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(account));
        double currentAccountBalance = account.getBalance();
        Operation operation = operationService.createAndPerformOperation("123",5000,OperationType.WITHDRAW);
        Assertions.assertThat(operation.getAmount()).isEqualTo(-5000);
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAW);
        Assertions.assertThat(operation.getAccount()).isNotNull();
        Assertions.assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance-5000);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doDeposit_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.empty());
        operationService.deposit("123",1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void doDeposit_should_perform_deposit_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        double currentAccountBalance = account.getBalance();
        AccountDTO dto = operationService.deposit("123",1200);
        assertTrue(dto.getOperations().contains(operation));
        Assertions.assertThat(dto.getBalance()).isEqualTo(currentAccountBalance+1200);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doWithdraw_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.empty());
        operationService.withdraw("123",1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void doWithdraw_should_perform_withdrawal_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        double currentAccountBalance = account.getBalance();
        AccountDTO dto = operationService.withdraw("123",1200);
        assertTrue(dto.getOperations().contains(operation));
        Assertions.assertThat(dto.getBalance()).isEqualTo(currentAccountBalance-1200);
    }
}
