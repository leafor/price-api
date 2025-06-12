package com.example.priceapi.infrastructure.adapter.in.exception;

import com.example.priceapi.domain.exception.PriceNotFoundException;
import com.example.priceapi.infrastructure.adapter.support.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * <p>
 * Global exception handler for the Price Service REST API.
 * Captures and handles various exceptions thrown by controllers
 * and maps them to a uniform {@link ErrorMessage} response
 * with an appropriate HTTP status code.
 * </p>
 *
 * <ul>
 *   <li>Handles resource-not-found errors as 404 Not Found.</li>
 *   <li>Handles validation and type-mismatch errors as 400 Bad Request.</li>
 *   <li>Handles unsupported HTTP methods as 405 Method Not Allowed.</li>
 *   <li>Handles all other uncaught exceptions as 500 Internal Server Error.</li>
 * </ul>
 */
@Log4j2
@RestControllerAdvice
public class RestExceptionHandler {
    private final HttpServletRequest httpServletRequest;

    /**
     * Constructs the global exception handler, injecting the current
     * {@link HttpServletRequest} to capture request-specific information
     * (e.g. URI path) for error responses.
     *
     * @param httpServletRequest the current HTTP servlet request
     */
    public RestExceptionHandler(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }


    /**
     * Handles custom {@link NotFoundException} thrown when a requested
     * resource cannot be found.
     *
     * @param ex the {@link NotFoundException} instance
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 404 (Not Found)
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Handles type-mismatch errors when a request parameter cannot be converted
     * to the required type (e.g. invalid date format).
     *
     * @param ex      the {@link MethodArgumentTypeMismatchException}
     * @param request the current HTTP servlet request
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getName()+": "+ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getName()+": "+ex.getMessage(), ex);
    }

    /**
     * Handles missing required query parameters in a request.
     *
     * @param ex      the {@link MissingServletRequestParameterException}
     * @param request the current HTTP servlet request
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleMissingParam(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getParameterName()+": "+ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getParameterName()+": "+ex.getMessage(), ex);
    }

    /**
     * Catches any uncaught {@link Exception} not handled by more specific methods.
     *
     * @param ex the uncaught exception
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleAll(Exception ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
    }

    /**
     * Handles domain-specific {@link PriceNotFoundException} when no price
     * record exists for the given query parameters.
     *
     * @param ex  the {@link PriceNotFoundException}
     * @param req the current HTTP servlet request
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 404 (Not Found)
     */
    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handlePriceNotFound(PriceNotFoundException ex, HttpServletRequest req) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage() , ex);
    }

    /**
     * Handles Spring's {@link NoResourceFoundException} indicating a resource
     * could not be resolved, often mapping to 405 Method Not Allowed.
     *
     * @param ex the {@link NoResourceFoundException}
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 405 (Method Not Allowed)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorMessage> handleNoResourceFoundException(Exception ex) {
        log.error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), ex.getMessage());
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), ex);
    }

    /**
     * Handles Spring's {@link HttpRequestMethodNotSupportedException} when
     * an HTTP method is not supported by an endpoint.
     *
     * @param ex the {@link HttpRequestMethodNotSupportedException}
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage}
     *         with HTTP status 405 (Method Not Allowed)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleMethodNotAllowed(Exception ex) {
        log.error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), ex.getMessage());
        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), ex);
    }

    /**
     * Helper to build a standardized error response without a custom message.
     *
     * @param httpStatus the HTTP status to return
     * @param ex         the triggering exception
     * @return a {@link ResponseEntity} with the error details
     */
    private ResponseEntity<ErrorMessage> buildErrorResponse(HttpStatus httpStatus, Throwable ex) {
        return buildErrorResponse(httpStatus, null, ex);
    }

    /**
     * Helper to build a standardized error response with an optional custom
     * message. Populates status, code, message, resource URI, and timestamp.
     *
     * @param httpStatus   the HTTP status to return
     * @param otherMessage optional custom error message; if null, uses ex.getMessage()
     * @param ex           the triggering exception
     * @return a {@link ResponseEntity} with the error details
     */
    private ResponseEntity<ErrorMessage> buildErrorResponse(HttpStatus httpStatus, String otherMessage, Throwable ex) {

        ErrorMessage errorResponse = ErrorMessage
                .builder()
                .status(httpStatus.name())
                .code(httpStatus.value())
                .message(otherMessage!=null?otherMessage:ex.getMessage())
                .resource(httpServletRequest.getRequestURI())
                .timestamp(DateUtils.getCurrentTimestamp())
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
