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
import banking.ProjectBanking.dto.BankingDtos.NotificationRequest;
import banking.ProjectBanking.dto.BankingDtos.NotificationResponse;
import banking.ProjectBanking.services.BankingService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final BankingService bankingService;

    public NotificationController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllNotifications() {
        List<NotificationResponse> notifications = bankingService.getAllNotifications();
        return ResponseEntity.ok(new ApiResponse("Notifications retrieved successfully", notifications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getNotificationById(@PathVariable UUID id) {
        NotificationResponse notification = bankingService.getNotification(id);
        return ResponseEntity.ok(new ApiResponse("Notification retrieved successfully", notification));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createNotification(@RequestBody NotificationRequest request) {
        NotificationResponse createdNotification = bankingService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Notification created successfully", createdNotification));
    }
}
