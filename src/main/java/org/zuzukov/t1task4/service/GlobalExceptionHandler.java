package org.zuzukov.t1task4.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().contains("Invalid or expired refresh token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Ошибка: refresh token недействителен или истёк");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка сервера: " + ex.getMessage());
    }
}
