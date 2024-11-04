package com.senai.projetofinal.controller.dto.request.aluno;

public record AtualizarAlunoRequest(
        String nome,
        String email,
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
        String materia,
        Long turma
) {
}
