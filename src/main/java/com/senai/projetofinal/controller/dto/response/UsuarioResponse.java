package com.senai.projetofinal.controller.dto.response;

import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import org.springframework.http.ResponseEntity;

public record UsuarioResponse(
        Long id,
        String nome,
        String login,
        String email,
        String papel
) {
    public static ResponseEntity<UsuarioResponse> toResponseEntity(UsuarioEntity entity) {
        return ResponseEntity.ok(new UsuarioResponse(entity.getId(),entity.getNome(),entity.getLogin(),entity.getEmail(),entity.getPapel().getNome().toString()));
    }
}
