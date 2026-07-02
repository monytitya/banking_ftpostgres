package banking.ProjectBanking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import banking.ProjectBanking.dto.ApiResponse;
import banking.ProjectBanking.exception.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    @Test
    void handleResponseStatusExceptionReturnsStructuredData() {
        UUID userId = UUID.fromString("7d8c9a12-4b6f-45b2-8d5f-1c2e3f4a5b6c");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseStatusException exception = new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found: " + userId);

        ResponseEntity<ApiResponse> response = handler.handleResponseStatusException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found: " + userId, response.getBody().message());
        assertNotNull(response.getBody().data());
        assertTrue(response.getBody().data().toString().contains(userId.toString()));
    }
}
