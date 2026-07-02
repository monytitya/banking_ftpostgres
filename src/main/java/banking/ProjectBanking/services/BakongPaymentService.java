package banking.ProjectBanking.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import banking.ProjectBanking.entity.QrPayment;
import banking.ProjectBanking.entity.Transaction;
import banking.ProjectBanking.repository.QrPaymentRepository;
import banking.ProjectBanking.repository.TransactionRepository;

@Service
public class BakongPaymentService {

    @Value("${bakong.api.url}")
    private String apiUrl;

    @Value("${bakong.api.token:}")
    private String apiToken;

    private final QrPaymentRepository qrPaymentRepository;
    private final TransactionRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public BakongPaymentService(QrPaymentRepository qrPaymentRepository,
            TransactionRepository transactionRepository) {
        this.qrPaymentRepository = qrPaymentRepository;
        this.transactionRepository = transactionRepository;
    }

    public boolean completePayment(UUID qrPaymentId) {
        Optional<QrPayment> opt = qrPaymentRepository.findById(qrPaymentId);
        if (opt.isEmpty()) {
            return false;
        }
        QrPayment qr = opt.get();
        Transaction tx = qr.getTransaction();

        Map<String, Object> payload = new HashMap<>();
        payload.put("reference", tx.getTransactionReference());
        payload.put("amount", tx.getAmount());
        payload.put("currency", tx.getCurrency());
        payload.put("merchantName", qr.getMerchantName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiToken != null && !apiToken.isBlank()) {
            headers.set("Authorization", "Bearer " + apiToken);
        }

        String endpoint = apiUrl != null && !apiUrl.isBlank() ? apiUrl : "";
        if (!endpoint.endsWith("/")) {
            endpoint = endpoint + "/";
        }
        endpoint = endpoint + "payments/complete";

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(endpoint, entity, String.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                tx.setStatus("COMPLETED");
                transactionRepository.save(tx);
                return true;
            } else {
                tx.setStatus("FAILED");
                transactionRepository.save(tx);
                return false;
            }
        } catch (Exception e) {
            tx.setStatus("FAILED");
            transactionRepository.save(tx);
            return false;
        }
    }
}
