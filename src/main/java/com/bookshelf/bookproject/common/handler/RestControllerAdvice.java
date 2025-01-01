package com.bookshelf.bookproject.common.handler;

import com.bookshelf.bookproject.common.ApiResponse;
import com.bookshelf.bookproject.common.exception.AccessDeniedException;
import com.bookshelf.bookproject.common.exception.InvalidApiResponseException;
import com.bookshelf.bookproject.common.exception.SaveFailedException;
import com.bookshelf.bookproject.common.exception.UnAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class, ResponseBody.class})
public class RestControllerAdvice {
    // 401
    @ExceptionHandler(UnAuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthenticatedException(UnAuthenticationException ex) {
        return getResponse(HttpStatus.UNAUTHORIZED, ex);
    }

    // 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return getResponse(HttpStatus.FORBIDDEN, ex);
    }

    // 404
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return getResponse(HttpStatus.NOT_FOUND, ex);
    }

    // 500
    @ExceptionHandler({InvalidApiResponseException.class, SaveFailedException.class})
    public ResponseEntity<ApiResponse<Void>> handleSaveFailedException(Exception ex) {
        return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private static ResponseEntity<ApiResponse<Void>> getResponse(HttpStatus status, Exception ex) {
        return ResponseEntity.status(status).body(
                ApiResponse.error(status, ex.getMessage()));
    }
}
