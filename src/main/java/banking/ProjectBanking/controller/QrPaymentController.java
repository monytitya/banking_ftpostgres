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
import banking.ProjectBanking.dto.BankingDtos.QrPaymentRequest;
import banking.ProjectBanking.dto.BankingDtos.QrPaymentResponse;
import banking.ProjectBanking.services.BankingService;

@RestController
@RequestMapping("/api/qr-payments")
public class QrPaymentController {

    private final BankingService bankingService;

    public QrPaymentController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllQrPayments() {
        List<QrPaymentResponse> qrPayments = bankingService.getAllQrPayments();
        return ResponseEntity.ok(new ApiResponse("QR Payments retrieved successfully", qrPayments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getQrPaymentById(@PathVariable UUID id) {
        QrPaymentResponse qrPayment = bankingService.getQrPayment(id);
        return ResponseEntity.ok(new ApiResponse("QR Payment retrieved successfully", qrPayment));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createQrPayment(@RequestBody QrPaymentRequest request) {
        QrPaymentResponse createdQrPayment = bankingService.createQrPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("QR Payment created successfully", createdQrPayment));
    }
}
