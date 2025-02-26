package vector.lkawa.year_estimator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import vector.lkawa.year_estimator.util.Message;
import vector.lkawa.year_estimator.dto.ApiError;

@Slf4j
@ControllerAdvice
public class RestHandler {

    @ExceptionHandler(value = RestClientException.class)
    protected ResponseEntity<ApiError> handleRestClientException(Exception ex) {
        log.error("Blad podczas komunikacji z zewnetrznym serwisem: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ApiError(Message.API.Exception.externalServiceError()), HttpStatus.SERVICE_UNAVAILABLE);
    }

}
