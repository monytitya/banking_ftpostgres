package banking.ProjectBanking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class BankingDtos {
        private BankingDtos() {
        }

        public record UserRequest(UUID id, String username, String password, String email, String role) {
        }

        public record UserResponse(UUID id, String username, String email, String role, LocalDateTime createdAt) {
        }

        public record CustomerRequest(
                        UUID userId,
                        String firstName,
                        String lastName,
                        LocalDate dob,
                        String address,
                        String gender) {
        }

        public record CustomerResponse(
                        UUID id,
                        UUID userId,
                        String firstName,
                        String lastName,
                        LocalDate dob,
                        String address,
                        String gender) {
        }

        public record AccountRequest(
                        UUID id,
                        UUID customerId,
                        String accountNumber,
                        String accountType,
                        String currency,
                        BigDecimal balance,
                        String status) {
        }

        public record AccountResponse(
                        UUID id,
                        UUID customerId,
                        String accountNumber,
                        String accountType,
                        String currency,
                        BigDecimal balance,
                        String status,
                        LocalDateTime createdAt) {
        }

        public record TransactionRequest(
                        UUID id,
                        String transactionReference,
                        UUID fromAccountId,
                        UUID toAccountId,
                        BigDecimal amount,
                        String currency,
                        String transactionType,
                        String status) {
        }

        public record TransactionResponse(
                        UUID id,
                        String transactionReference,
                        UUID fromAccountId,
                        UUID toAccountId,
                        BigDecimal amount,
                        String currency,
                        String transactionType,
                        String status,
                        LocalDateTime createdAt) {
        }

        public record QrPaymentRequest(UUID id, String merchantName, String qrCodeData, UUID transactionId) {
        }

        public record QrPaymentResponse(
                        UUID id,
                        String merchantName,
                        String qrCodeData,
                        UUID transactionId,
                        LocalDateTime createdAt) {
        }

        public record NotificationRequest(
                        UUID id,
                        UUID userId,
                        String title,
                        String message,
                        String notificationType,
                        Boolean isRead) {
        }

        public record NotificationResponse(
                        UUID id,
                        UUID userId,
                        String title,
                        String message,
                        String notificationType,
                        Boolean isRead,
                        LocalDateTime sentAt) {
        }
}
