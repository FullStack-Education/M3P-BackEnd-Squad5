package com.senai.projetofinal.controller.dto.request.aluno;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

public record InserirAlunoRequest(
        String nome,
        String email,
        String senha,
        String dataNascimento,
        String genero,
        String cpf,
        String rg,
        String estadoCivil,
        String telefone,
        String naturalidade,
        String cep,
        String cidade,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String pontoReferencia,
        Long usuario,
        Long turma
) {
}


