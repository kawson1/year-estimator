package vector.lkawa.year_estimator.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@ControllerAdvice
public class RestHandler {

    @ExceptionHandler(value = RestClientException.class)
    protected ResponseEntity handleRestClientException() {
        return new ResponseEntity<>(Map.of("message", "Serwis niedostępny"), HttpStatus.SERVICE_UNAVAILABLE);
    }

}
