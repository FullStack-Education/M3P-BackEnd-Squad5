package com.senai.projetofinal.controller.dto.request.aluno;

public record AtualizarAlunoRequest(
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
        String estado,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String pontoReferencia,
        Long turma
) {
}
