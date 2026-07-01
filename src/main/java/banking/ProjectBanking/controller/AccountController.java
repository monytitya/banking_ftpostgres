package banking.ProjectBanking.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import banking.ProjectBanking.dto.ApiResponse;
import banking.ProjectBanking.dto.BankingDtos.AccountRequest;
import banking.ProjectBanking.dto.BankingDtos.AccountResponse;
import banking.ProjectBanking.services.BankingService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final BankingService bankingService;

    public AccountController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAccounts() {
        List<AccountResponse> accounts = bankingService.getAllAccounts();
        return ResponseEntity.ok(new ApiResponse("Accounts retrieved successfully", accounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAccountById(@PathVariable UUID id) {
        AccountResponse account = bankingService.getAccount(id);
        return ResponseEntity.ok(new ApiResponse("Account retrieved successfully", account));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAccount(@RequestBody AccountRequest request) {
        AccountResponse createdAccount = bankingService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Account created successfully", createdAccount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable UUID id, @RequestBody AccountRequest request) {
        AccountResponse updatedAccount = bankingService.updateAccount(id, request);
        return ResponseEntity.ok(new ApiResponse("Account updated successfully", updatedAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable UUID id) {
        bankingService.deleteAccount(id);
        return ResponseEntity.ok(
                new ApiResponse("Account deleted successfully", null));
    }

}
