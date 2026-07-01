package banking.ProjectBanking.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import banking.ProjectBanking.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
