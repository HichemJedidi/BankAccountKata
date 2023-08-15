package sgcbi.Kata.BankAccountKata.repositories;

import sgcbi.Kata.BankAccountKata.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<Account,Long> {
}
