package banking.ProjectBanking.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import banking.ProjectBanking.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ApiResponse(ex.getReason(), buildErrorData(ex.getStatusCode().value(), ex.getReason())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("An unexpected error occurred: " + ex.getMessage(),
                        buildErrorData(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage())));
    }

    private Map<String, Object> buildErrorData(int status, String message) {
        Map<String, Object> errorData = new LinkedHashMap<>();
        errorData.put("status", status);
        errorData.put("message", message);
        return errorData;
    }
}
