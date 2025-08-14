package com.hotel.x.hotel_x.domain.Hospede.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
public class HospedeNotFoundException extends RuntimeException {

    public HospedeNotFoundException(String dado) {
        super("Dado do hóspede não encontrado: " + dado);
    }

    // Método auxiliar para gerar o ResponseEntity
    public ResponseEntity<String> toResponseEntity() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(getMessage());
    }
}