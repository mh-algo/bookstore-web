package com.bookshelf.bookproject.common.handler;

import com.bookshelf.bookproject.common.exception.AccessDeniedException;
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
    @ExceptionHandler(UnAuthenticationException.class)
    public ResponseEntity<String> handleUnauthenticatedException(UnAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(SaveFailedException.class)
    public ResponseEntity<String> handleSaveFailedException(SaveFailedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
