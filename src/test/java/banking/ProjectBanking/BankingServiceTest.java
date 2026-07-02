package banking.ProjectBanking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import banking.ProjectBanking.dto.BankingDtos.CustomerRequest;
import banking.ProjectBanking.dto.BankingDtos.CustomerResponse;
import banking.ProjectBanking.entity.Customer;
import banking.ProjectBanking.entity.User;
import banking.ProjectBanking.repository.AccountRepository;
import banking.ProjectBanking.repository.CustomerRepository;
import banking.ProjectBanking.repository.NotificationRepository;
import banking.ProjectBanking.repository.QrPaymentRepository;
import banking.ProjectBanking.repository.TransactionRepository;
import banking.ProjectBanking.repository.UserRepository;
import banking.ProjectBanking.services.BankingService;

@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private QrPaymentRepository qrPaymentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private BankingService bankingService;

    @Test
    void createCustomerShouldCreateMissingUserWhenUserIdIsProvided() {
        UUID userId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        CustomerRequest request = new CustomerRequest(
                userId,
                "Mao",
                "Tityamony",
                LocalDate.of(2003, 8, 15),
                "Phnom Penh, Cambodia",
                "MALE");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse response = bankingService.createCustomer(request);

        assertNotNull(response);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User createdUser = userCaptor.getValue();
        assertEquals(userId, createdUser.getId());
        assertEquals("CUSTOMER", createdUser.getRole());
        assertNotNull(createdUser.getUsername());
    }
}
