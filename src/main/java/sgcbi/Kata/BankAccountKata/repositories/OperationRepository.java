package sgcbi.Kata.BankAccountKata.repositories;

import sgcbi.Kata.BankAccountKata.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Long> {
}
