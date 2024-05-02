package com.accio.api.filter;

import com.accio.api.service.ConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.Principal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {
  private final ConfigService configService;

  @Override
  protected ResponseEntity<Object> handleErrorResponseException(@NotNull ErrorResponseException ex, @NotNull HttpHeaders headers,
                                                                @NotNull HttpStatusCode status, @NotNull WebRequest request) {
    if (status == HttpStatus.NOT_ACCEPTABLE && ex instanceof ResponseStatusException rse) {
      var reason = configService.isDevelopment() ? rse.getReason() : null;
      ex = new ResponseStatusException(
          rse.getStatusCode(),
          reason,
          rse.getCause()
      );
      ex.setStackTrace(rse.getStackTrace());
    }
    return super.handleErrorResponseException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    var body = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    body.setDetail(errors.values().toString());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException() {
    return new ResponseEntity<>(new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({SQLException.class})
  public void handleSqlException(Principal principal, HttpServletRequest request) {
    log.error("deadlock detected for user {} for request {}", principal != null ? principal.getName() : "unknown",
            request.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public Object handle(Exception e,
                       Principal principal,
                       HttpServletRequest request) {
    if (e instanceof ResponseStatusException
        || e instanceof ClientAbortException) return e;

    if (e instanceof HttpRequestMethodNotSupportedException
        || e instanceof HttpMediaTypeException
        || e instanceof MethodArgumentTypeMismatchException
        || e instanceof MissingServletRequestParameterException
        || e instanceof MissingRequestHeaderException) {
      return configService.isDevelopment() ? e : getResponse(FORBIDDEN);
    }

    log.error("Error for User {} and Request {} with Message {}",
            principal != null ? principal.getName() : "unknown",
            request.getRequestURI(),
            e.getMessage());


    return getResponse(INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = MultipartException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleFileUploadingError(MultipartException exception) {
    logger.warn("Failed to upload attachment", exception);
    return exception.getMessage();
  }

  private static ResponseEntity<?> getResponse(HttpStatusCode status) {
    return ResponseEntity
        .status(status)
        .build();
  }
}
