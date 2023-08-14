package sgcbi.Kata.BankAccountKata;

import dtos.AccountDTO;
import dtos.OperationDTO;
import entities.Account;
import entities.Operation;
import enums.AccountStatus;
import enums.OperationType;
import exeptions.NoSuchAccountException;
import lombok.Builder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.junit.Before;
import repositories.BankAccountRepository;
import services.BankAccountService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class BankAccountServiceTest {
    @Spy
    private BankAccountService bankAccountService;
    @Mock
    private BankAccountRepository bankAccountRepository;

    private List<Operation> operations;
    private Account account ;
    @Before
    public void setUp(){
        account = Account.builder()
                .id("12343")
                .balance(1000)
                .currency("EUR")
                .status(AccountStatus.CREATED)
                .operations(anyList()).build();

        operations  = new ArrayList<>();
        operations.add(new Operation(12L,Instant.now(),10000,OperationType.DEPOSIT,account));
        account.setOperations(operations);
    }
    @Test
    public void TestAccountCreation(){
        when(bankAccountRepository.save(any())).thenReturn(account);
        Assert.assertNotNull(bankAccountService.createAccount(new AccountDTO()));
        assertEquals("12343", (bankAccountService.createAccount(new AccountDTO())).getId());

    }
    @Test
    public void printStatement_should_successfully_return_current_account_balance() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.of(account));
        AccountDTO accountDto = bankAccountService.printStatement("12343");
        Assertions.assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
    }

    @Test(expected = NoSuchAccountException.class)
    public void printStatement_should_throw_exception_for_no_such_account() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.empty());
        bankAccountService.printStatement("12343");
        Assert.fail("should have thrown NoSuchAccountException ");
    }
    @Test
    public void listAllOperations_should_successfully_return_all_account_operations() throws NoSuchAccountException {
        when(bankAccountRepository.findById("12343")).thenReturn(Optional.of(account));
        List<OperationDTO> operations = bankAccountService.listAllOperations("12343");
        Assertions.assertThat(operations).isNotEmpty();
        assertEquals(operations.size(), account.getOperations().size());
    }
    @Test(expected = NoSuchAccountException.class)
    public void listAllOperations_should_throw_exception_for_no_such_account() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.empty());
        bankAccountService.listAllOperations("12343");
        Assert.fail("should have thrown NoSuchAccountException ");
    }
}
