package banking.ProjectBanking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import banking.ProjectBanking.dto.ApiResponse;
import banking.ProjectBanking.dto.BankingDtos.TransactionRequest;
import banking.ProjectBanking.dto.BankingDtos.TransactionResponse;
import banking.ProjectBanking.services.BankingService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final BankingService bankingService;

    public TransactionController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTransactions() {
        List<TransactionResponse> transactions = bankingService.getAllTransactions();
        return ResponseEntity.ok(new ApiResponse("Transactions retrieved successfully", transactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTransactionById(@PathVariable UUID id) {
        TransactionResponse transaction = bankingService.getTransaction(id);
        return ResponseEntity.ok(new ApiResponse("Transaction retrieved successfully", transaction));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse createdTransaction = bankingService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Transaction created successfully", createdTransaction));
    }
}
