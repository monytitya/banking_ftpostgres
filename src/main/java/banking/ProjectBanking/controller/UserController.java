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
import banking.ProjectBanking.dto.BankingDtos.UserRequest;
import banking.ProjectBanking.dto.BankingDtos.UserResponse;
import banking.ProjectBanking.services.BankingService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final BankingService bankingService;

    public UserController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponse> users = bankingService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable UUID id) {
        UserResponse user = bankingService.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("User not found: " + id, null));
        }
        return ResponseEntity.ok(new ApiResponse("User retrieved successfully", user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserRequest request) {
        UserResponse createdUser = bankingService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("User created successfully", createdUser));
    }
}
