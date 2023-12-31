package sgcbi.Kata.BankAccountKata;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import sgcbi.Kata.BankAccountKata.dtos.AccountDTO;
import sgcbi.Kata.BankAccountKata.dtos.OperationDTO;
import sgcbi.Kata.BankAccountKata.entities.Account;
import sgcbi.Kata.BankAccountKata.entities.Operation;
import sgcbi.Kata.BankAccountKata.enums.OperationType;
import sgcbi.Kata.BankAccountKata.exeptions.AmountNegativeExeption;
import sgcbi.Kata.BankAccountKata.exeptions.BalanceNotSufficentExeption;
import sgcbi.Kata.BankAccountKata.exeptions.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import sgcbi.Kata.BankAccountKata.repositories.BankAccountRepository;
import sgcbi.Kata.BankAccountKata.repositories.OperationRepository;
import sgcbi.Kata.BankAccountKata.services.OperationServices;

import java.time.Instant;
import java.util.Optional;


import org.assertj.core.api.Assertions;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private OperationRepository operationRepository;



    @Spy
    @InjectMocks
    private OperationServices operationService;

    private Account account ;
    private Operation operation;
    @Before
    public void setUp(){
        account = new Account();
        account.setBalance(5000);
        account.setId(123L);
        operation = new Operation(12L,Instant.now(),1000,OperationType.DEPOSIT,account);
    }

    @Test(expected = NoSuchAccountException.class)
    public void createAndPerformOperation_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.createAndPerformOperation(123L,0,OperationType.WITHDRAW);
        Assert.fail("should have thrown NoSuchAccountException ");

    }

    @Test
    public void createAndPerformOperation_should_perform_deposit() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        double currentAccountBalance = account.getBalance();
        OperationDTO operation = operationService.createAndPerformOperation(123L,1000,OperationType.DEPOSIT);
        Assertions.assertThat(operation.getAmount()).isEqualTo(1000);
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        Assertions.assertThat(operation.getAccount()).isNotNull();
        Assertions.assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance+1000);
    }

    @Test
    public void createAndPerformOperation_should_perform_withdrawal() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        double currentAccountBalance = account.getBalance();
        OperationDTO operation = operationService.createAndPerformOperation(123L,5000,OperationType.WITHDRAW);
        Assertions.assertThat(operation.getAmount()).isEqualTo(5000);
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAW);
        Assertions.assertThat(operation.getAccount()).isNotNull();
        Assertions.assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance-5000);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doDeposit_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.deposit(123L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void doDeposit_should_perform_deposit_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        double currentAccountBalance = account.getBalance();
        AccountDTO dto = operationService.deposit(123L,1200);
        assertTrue(dto.getOperations().contains(operation));
        Assertions.assertThat(dto.getBalance()).isEqualTo(currentAccountBalance+1200);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doWithdraw_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.withdraw(123L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void doWithdraw_should_perform_withdrawal_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        double currentAccountBalance = account.getBalance();
        AccountDTO dto = operationService.withdraw(123L,1200);
        assertTrue(dto.getOperations().contains(operation));
        Assertions.assertThat(dto.getBalance()).isEqualTo(currentAccountBalance-1200);
    }
    @Test
    public void testDoDepositWithInsufficientBalance() throws BalanceNotSufficentExeption {

        when(bankAccountRepository.findById(123L)).thenReturn(Optional.of(account));
        assertThrows(BalanceNotSufficentExeption.class, () -> operationService.deposit(123L, 500000));
    }
    @Test
    public void testNegativeAmountDoDeposit() throws AmountNegativeExeption {

        when(bankAccountRepository.findById(123L)).thenReturn(Optional.of(account));
        assertThrows(AmountNegativeExeption.class, () -> operationService.deposit(123L, -100));
    }
    @Test
    public void testNegativeAmountDoWithdraw() throws AmountNegativeExeption {

        when(bankAccountRepository.findById(123L)).thenReturn(Optional.of(account));
        assertThrows(AmountNegativeExeption.class, () -> operationService.withdraw(123L, -100));
    }
}
