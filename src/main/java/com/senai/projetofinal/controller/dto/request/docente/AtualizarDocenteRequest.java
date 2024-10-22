package com.senai.projetofinal.controller.dto.request.docente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

//public record AtualizarDocenteRequest(
//        String nome
//) {
//}

public record AtualizarDocenteRequest(
        String nome,

        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate dataNascimento,

        String genero,
        String cpf,
        String rg,
        String estadoCivil,
        String telefone,
        String email,
        String senha,
        String naturalidade,
        String cep,
        String cidade,
        String estado,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String pontoReferencia,
        List<String> materias
) {
}