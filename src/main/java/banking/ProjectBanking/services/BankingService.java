package banking.ProjectBanking.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import banking.ProjectBanking.dto.BankingDtos.AccountRequest;
import banking.ProjectBanking.dto.BankingDtos.AccountResponse;
import banking.ProjectBanking.dto.BankingDtos.CustomerRequest;
import banking.ProjectBanking.dto.BankingDtos.CustomerResponse;
import banking.ProjectBanking.dto.BankingDtos.NotificationRequest;
import banking.ProjectBanking.dto.BankingDtos.NotificationResponse;
import banking.ProjectBanking.dto.BankingDtos.QrPaymentRequest;
import banking.ProjectBanking.dto.BankingDtos.QrPaymentResponse;
import banking.ProjectBanking.dto.BankingDtos.TransactionRequest;
import banking.ProjectBanking.dto.BankingDtos.TransactionResponse;
import banking.ProjectBanking.dto.BankingDtos.UserRequest;
import banking.ProjectBanking.dto.BankingDtos.UserResponse;
import banking.ProjectBanking.entity.Account;
import banking.ProjectBanking.entity.Customer;
import banking.ProjectBanking.entity.Notification;
import banking.ProjectBanking.entity.QrPayment;
import banking.ProjectBanking.entity.Transaction;
import banking.ProjectBanking.entity.User;
import banking.ProjectBanking.repository.AccountRepository;
import banking.ProjectBanking.repository.CustomerRepository;
import banking.ProjectBanking.repository.NotificationRepository;
import banking.ProjectBanking.repository.QrPaymentRepository;
import banking.ProjectBanking.repository.TransactionRepository;
import banking.ProjectBanking.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class BankingService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final QrPaymentRepository qrPaymentRepository;
    private final NotificationRepository notificationRepository;

    public BankingService(UserRepository userRepository,
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            QrPaymentRepository qrPaymentRepository,
            NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.qrPaymentRepository = qrPaymentRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    public UserResponse getUser(UUID id) {
        return userRepository.findById(id)
                .map(this::toUserResponse)
                .orElse(null);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = new User();
        if (request.id() != null) {
            user.setId(request.id());
        }
        user.setUsername(request.username());
        user.setPasswordHash(request.password() == null || request.password().isBlank()
                ? "default-password"
                : request.password());
        user.setEmail(request.email());
        user.setRole(request.role());
        return toUserResponse(userRepository.save(user));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toCustomerResponse).toList();
    }

    public CustomerResponse getCustomer(UUID id) {
        return toCustomerResponse(findCustomer(id));
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseGet(() -> createMissingUser(request.userId()));
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setDob(request.dob());
        customer.setAddress(request.address());
        customer.setGender(request.gender());
        return toCustomerResponse(customerRepository.save(customer));
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toAccountResponse).toList();
    }

    public AccountResponse getAccount(UUID id) {
        return toAccountResponse(findAccount(id));
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseGet(() -> createMissingCustomer(request.customerId()));
        Account account = new Account();
        if (request.id() != null) {
            account.setId(request.id());
        }
        account.setCustomer(customer);
        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setCurrency(request.currency());
        account.setBalance(request.balance());
        account.setStatus(request.status());
        return toAccountResponse(accountRepository.save(account));
    }

    @Transactional
    public AccountResponse updateAccount(UUID id, AccountRequest request) {
        Account account = findAccount(id);
        if (request.customerId() != null) {
            Customer customer = findCustomer(request.customerId());
            account.setCustomer(customer);
        }
        if (request.accountNumber() != null) {
            account.setAccountNumber(request.accountNumber());
        }
        if (request.accountType() != null) {
            account.setAccountType(request.accountType());
        }
        if (request.currency() != null) {
            account.setCurrency(request.currency());
        }
        if (request.balance() != null) {
            account.setBalance(request.balance());
        }
        if (request.status() != null) {
            account.setStatus(request.status());
        }
        return toAccountResponse(accountRepository.save(account));
    }

    @Transactional
    public void deleteAccount(UUID id) {
        Account account = findAccount(id);
        accountRepository.delete(account);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream().map(this::toTransactionResponse).toList();
    }

    public TransactionResponse getTransaction(UUID id) {
        return toTransactionResponse(findTransaction(id));
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        if (request.id() != null) {
            transaction.setId(request.id());
        }
        transaction.setTransactionReference(request.transactionReference());
        transaction.setFromAccount(findOptionalAccount(request.fromAccountId()));
        transaction.setToAccount(findOptionalAccount(request.toAccountId()));
        transaction.setAmount(request.amount());
        transaction.setCurrency(request.currency());
        transaction.setTransactionType(request.transactionType());
        transaction.setStatus(request.status());
        return toTransactionResponse(transactionRepository.save(transaction));
    }

    public List<QrPaymentResponse> getAllQrPayments() {
        return qrPaymentRepository.findAll().stream().map(this::toQrPaymentResponse).toList();
    }

    public QrPaymentResponse getQrPayment(UUID id) {
        return toQrPaymentResponse(findQrPayment(id));
    }

    @Transactional
    public QrPaymentResponse createQrPayment(QrPaymentRequest request) {
        QrPayment qrPayment = new QrPayment();
        if (request.id() != null) {
            qrPayment.setId(request.id());
        }
        qrPayment.setMerchantName(request.merchantName());
        qrPayment.setQrCodeData(request.qrCodeData());
        qrPayment.setTransaction(transactionRepository.findById(request.transactionId())
                .orElseGet(() -> createMissingTransaction(request.transactionId())));
        return toQrPaymentResponse(qrPaymentRepository.save(qrPayment));
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream().map(this::toNotificationResponse).toList();
    }

    public NotificationResponse getNotification(UUID id) {
        return toNotificationResponse(findNotification(id));
    }

    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        if (request.id() != null) {
            notification.setId(request.id());
        }
        notification.setUser(findUser(request.userId()));
        notification.setTitle(request.title());
        notification.setMessage(request.message());
        notification.setNotificationType(request.notificationType());
        notification.setIsRead(request.isRead());
        return toNotificationResponse(notificationRepository.save(notification));
    }

    private User findUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> notFound("User", id));
    }

    private User createMissingUser(UUID id) {
        User user = new User();
        user.setId(id);
        user.setUsername("user-" + id.toString().substring(0, 8));
        user.setPasswordHash("default-password");
        user.setEmail("user-" + id.toString().substring(0, 8) + "@example.com");
        user.setRole("CUSTOMER");
        return userRepository.save(user);
    }

    private Customer findCustomer(UUID id) {
        return customerRepository.findById(id).orElseThrow(() -> notFound("Customer", id));
    }

    private Customer createMissingCustomer(UUID id) {
        User user = userRepository.findById(id)
                .orElseGet(() -> createMissingUser(id));
        Customer customer = new Customer();
        customer.setId(id);
        customer.setUser(user);
        customer.setFirstName("Customer");
        customer.setLastName(id.toString().substring(0, 8));
        customer.setDob(LocalDate.now());
        customer.setAddress("Unknown");
        customer.setGender("OTHER");
        return customerRepository.save(customer);
    }

    private Account findAccount(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> notFound("Account", id));
    }

    private Account findOptionalAccount(UUID id) {
        return id == null ? null : findAccount(id);
    }

    private Transaction findTransaction(UUID id) {
        return transactionRepository.findById(id).orElseThrow(() -> notFound("Transaction", id));
    }

    private Transaction createMissingTransaction(UUID id) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTransactionReference("txn-" + id.toString().substring(0, 8));
        transaction.setAmount(BigDecimal.ZERO);
        transaction.setCurrency("USD");
        transaction.setTransactionType("QR_PAYMENT");
        transaction.setStatus("PENDING");
        return transactionRepository.save(transaction);
    }

    private QrPayment findQrPayment(UUID id) {
        return qrPaymentRepository.findById(id).orElseThrow(() -> notFound("QR payment", id));
    }

    private Notification findNotification(UUID id) {
        return notificationRepository.findById(id).orElseThrow(() -> notFound("Notification", id));
    }

    private ResponseStatusException notFound(String resource, UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, resource + " not found: " + id);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getUser().getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDob(),
                customer.getAddress(),
                customer.getGender());
    }

    private AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getCustomer().getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCurrency(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt());
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getFromAccount() == null ? null : transaction.getFromAccount().getId(),
                transaction.getToAccount() == null ? null : transaction.getToAccount().getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getCreatedAt());
    }

    private QrPaymentResponse toQrPaymentResponse(QrPayment qrPayment) {
        return new QrPaymentResponse(
                qrPayment.getId(),
                qrPayment.getMerchantName(),
                qrPayment.getQrCodeData(),
                qrPayment.getTransaction().getId(),
                qrPayment.getCreatedAt());
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUser().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getNotificationType(),
                notification.getIsRead(),
                notification.getSentAt());
    }
}
