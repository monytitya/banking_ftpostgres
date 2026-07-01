package banking.ProjectBanking.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import banking.ProjectBanking.entity.QrPayment;

@Repository
public interface QrPaymentRepository extends JpaRepository<QrPayment, UUID> {
}
