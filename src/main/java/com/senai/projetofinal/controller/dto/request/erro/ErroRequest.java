package com.senai.projetofinal.controller.dto.request.erro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErroRequest {

    private String codigo;
    private String mensagem;

}