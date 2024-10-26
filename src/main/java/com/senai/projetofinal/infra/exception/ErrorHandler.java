package com.senai.projetofinal.infra.exception;

import com.senai.projetofinal.controller.dto.request.erro.ErroRequest;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.infra.exception.error.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handler(Exception e) {
        ErroRequest erro = ErroRequest.builder()
                .codigo("500")
                .mensagem(e.getMessage())
                .build();
        return ResponseEntity.status(500).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handler(NotFoundException e) {
        ErroRequest erro = ErroRequest.builder()
                .codigo("404")
                .mensagem("Requisição inválida, por exemplo, dados ausentes ou incorretos")
                .build();
        return ResponseEntity.status(404).body(erro);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handler(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
