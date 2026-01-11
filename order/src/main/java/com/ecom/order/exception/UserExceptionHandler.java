package com.ecom.order.exception;

import com.ecom.order.dto.ExceptionResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> cartNotFoundExceptionHandling(UserNotFoundException ex, HttpServletRequest request){
        ExceptionResponseDTO exceptionResponseDTO = ExceptionResponseDTO.builder()
                .path(request.getServletPath())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now().toString())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDTO);
    }
}
