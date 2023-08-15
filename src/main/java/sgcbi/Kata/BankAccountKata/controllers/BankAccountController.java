package sgcbi.Kata.BankAccountKata.controllers;

import sgcbi.Kata.BankAccountKata.dtos.AccountDTO;
import sgcbi.Kata.BankAccountKata.dtos.OperationDTO;
import sgcbi.Kata.BankAccountKata.exeptions.NoSuchAccountException;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sgcbi.Kata.BankAccountKata.services.BankAccountService;
import sgcbi.Kata.BankAccountKata.services.OperationServices;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/bankAccount")
@AllArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final OperationServices operationService;
    @PostMapping("/createAccount")
    public ResponseEntity<AccountDTO> createAccount( @RequestBody AccountDTO accountDTO) throws NoSuchAccountException {
        AccountDTO result = bankAccountService.createAccount(accountDTO);
        bankAccountService.activateAccount(result.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/{accountId}")
    public AccountDTO printAccountState(@PathVariable Long accountId) throws NoSuchAccountException {
        return bankAccountService.printStatement(accountId);
    }
    @GetMapping("/AllAccounts")
    public List<AccountDTO> getAllAccounts()   {
        return bankAccountService.getAllAccounts();
    }
    @GetMapping("{accountId}/history")
    public List<OperationDTO> showOperationsList(@PathVariable Long accountId) throws NoSuchAccountException {
        return bankAccountService.listAllOperations(accountId);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exeptionHandler(Exception exeption){
        ResponseEntity<String> entity = new ResponseEntity<>(
                exeption.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return entity;
    }
    @PutMapping(path = "/deposit")
    public ResponseEntity<AccountDTO> depositMoney(@RequestParam Long accountId, @RequestParam double amount){

        try {
            AccountDTO updatedAccount = operationService.deposit(accountId, amount);
            return ResponseEntity.ok(updatedAccount);
        } catch (NoSuchAccountException e) {
            return ResponseEntity.notFound().build();
        }

    }
    @PutMapping(path = "/withdraw")
    public ResponseEntity<AccountDTO> withdrawMoney(@RequestParam Long accountId, @RequestParam double amount){

        try {
            AccountDTO updatedAccount = operationService.withdraw(accountId, amount);
            return ResponseEntity.ok(updatedAccount);
        } catch (NoSuchAccountException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
