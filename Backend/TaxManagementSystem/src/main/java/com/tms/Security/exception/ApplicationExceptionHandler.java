// package com.tms.Security.exception;

// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;

// import jakarta.naming.ServiceUnavailableException;
// import jakarta.validation.ConstraintViolation;
// import jakarta.validation.ConstraintViolationException;

// import org.springframework.beans.ConversionNotSupportedException;
// import org.springframework.beans.TypeMismatchException;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.http.converter.HttpMessageNotWritableException;
// import org.springframework.jdbc.BadSqlGrammarException;
// import org.springframework.jdbc.CannotGetJdbcConnectionException;
// import org.springframework.jdbc.UncategorizedSQLException;
// import org.springframework.lang.Nullable;
// import org.springframework.validation.BindException;
// import org.springframework.validation.FieldError;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.HttpMediaTypeNotAcceptableException;
// import org.springframework.web.HttpMediaTypeNotSupportedException;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingPathVariableException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.ServletRequestBindingException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.context.request.WebRequest;
// import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
// import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
// import org.springframework.web.multipart.support.MissingServletRequestPartException;
// import org.springframework.web.servlet.NoHandlerFoundException;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// import com.tms.Security.exception.ErrorModel.ApiError;
// import com.microsoft.sqlserver.jdbc.SQLServerException;

// import io.jsonwebtoken.ExpiredJwtException;

// @RestControllerAdvice
// public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

//     // 400

//     @Override
//     protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final List<String> errors = new ArrayList<String>();
//         for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
//             errors.add(error.getField() + ": " + error.getDefaultMessage());
//         }
//         for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
//             errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
//         }
//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
//         return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
//     }

//     @Override
//     protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final List<String> errors = new ArrayList<String>();
//         for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
//             errors.add(error.getField() + ": " + error.getDefaultMessage());
//         }
//         for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
//             errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
//         }
//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
//         return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
//     }

//     @Override
//     protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     @Override
//     protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final String error = ex.getRequestPartName() + " part is missing";
//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     @Override
//     protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final String error = ex.getParameterName() + " parameter is missing";
//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     //

//     @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
//     public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
//         //
//         final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     @ExceptionHandler({ ConstraintViolationException.class })
//     public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
//         //
//         final List<String> errors = new ArrayList<String>();
//         for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//             errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
//         }

//         final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     // 404

//     @Override
//     protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

//         final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     // 405

//     @Override
//     protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final StringBuilder builder = new StringBuilder();
//         builder.append(ex.getMethod());
//         builder.append(" method is not supported for this request. Supported methods are ");
//         ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

//         final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     // 415

//     @Override
//     protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//         //
//         final StringBuilder builder = new StringBuilder();
//         builder.append(ex.getContentType());
//         builder.append(" media type is not supported. Supported media types are ");
//         ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

//         final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
//         return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     }

//     // 500

//     @ExceptionHandler({ Exception.class })
//     public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
//         logger.error("error", ex);
//         //  User not found
//         if (ex instanceof UserNotFoundException) {
//             final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//         // No Users Available
//         else if (ex instanceof NoUsersAvailableException){
//             final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }

//         // Staff not found
//         else if (ex instanceof StaffNotFoundException){
//             final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }

//         // Token Invalid
//         else if(ex instanceof InvalidTokenException){
//             final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//         else if(ex instanceof ServiceUnavailableException){
//             final ApiError apiError = new ApiError(HttpStatus.GATEWAY_TIMEOUT, "Upstream Service Not Responding, Try Again", "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }

//         // SQL Errors here . . . 

//         // Generic SQL error
//         else if (ex instanceof SQLException){
//             final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//         // BadSqlGrammarException
//         else if (ex instanceof BadSqlGrammarException){
//             final String errorMessage = "Error querying database.";
//             final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//         // Database Connection Error
//         else if (ex instanceof CannotGetJdbcConnectionException){
//             final String errorMessage = "Error connecting to the database.";
//             final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//         // Uncategorized Sql Exception
//         else if (ex instanceof UncategorizedSQLException){
//             final String errorMessage = "Unknown SQL Error.";
//             final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }

//         else if(ex instanceof NullPointerException){
//             final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//         else if (ex instanceof ExpiredJwtException ) {
//             final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,"JWT token is expired: " +  ex.getMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }
//             // logger.error("JWT token is expired: {}", e.getMessage());
//         else{
//             final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
//             return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//         }    
//     }

//     // @ResponseStatus(
//     //     value = HttpStatus.NOT_FOUND,
//     //     reason = "Requested User Not Found")
//     // @ExceptionHandler(UserNotFoundException.class)
//     // public ResponseEntity<Object> handleUserNotFoundExceptionException(UserNotFoundException ex) {
//     //     logger.error("error", ex);
//     //     //
//     //     final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "error occurred");
//     //     return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     // }

//     // // @ResponseStatus(
//     // //     value = HttpStatus.BAD_REQUEST,
//     // //     reason = "Token invalid or expired")
//     // @ExceptionHandler(InvalidTokenException.class)
//     // public ResponseEntity<Object> handleException(InvalidTokenException ex) {
//     //     logger.error("error", ex);
//     //     //
//     //     final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "error occurred");
//     //     return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     // }

//     // // @ResponseStatus(
//     // //     value = HttpStatus.GATEWAY_TIMEOUT,
//     // //     reason = "Upstream Service Not Responding, Try Again")
//     // @ExceptionHandler(ServiceUnavailableException.class)
//     // public ResponseEntity<Object> handleException(ServiceUnavailableException ex) {
//     //     logger.error("error", ex);
//     //     //
//     //     final ApiError apiError = new ApiError(HttpStatus.GATEWAY_TIMEOUT, ex.getLocalizedMessage(), "error occurred");
//     //     return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
//     // }






// }
