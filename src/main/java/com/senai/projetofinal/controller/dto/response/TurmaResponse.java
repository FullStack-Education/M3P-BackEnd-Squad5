package com.senai.projetofinal.controller.dto.response;

import com.senai.projetofinal.datasource.entity.CursoEntity;
import com.senai.projetofinal.datasource.entity.DocenteEntity;

public record TurmaResponse(
        Long id,
        String nome,
        DocenteEntity docente,
        CursoEntity curso
) {
}
