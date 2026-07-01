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
import banking.ProjectBanking.dto.BankingDtos.CustomerRequest;
import banking.ProjectBanking.dto.BankingDtos.CustomerResponse;
import banking.ProjectBanking.services.BankingService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final BankingService bankingService;

    public CustomerController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCustomers() {
        List<CustomerResponse> customers = bankingService.getAllCustomers();
        return ResponseEntity.ok(new ApiResponse("Customers retrieved successfully", customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCustomerById(@PathVariable UUID id) {
        CustomerResponse customer = bankingService.getCustomer(id);
        return ResponseEntity.ok(new ApiResponse("Customer retrieved successfully", customer));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody CustomerRequest request) {
        CustomerResponse createdCustomer = bankingService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Customer created successfully", createdCustomer));
    }
}
