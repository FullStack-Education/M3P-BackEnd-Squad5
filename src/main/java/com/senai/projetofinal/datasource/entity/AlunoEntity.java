package com.senai.projetofinal.datasource.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "aluno")
public class AlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    private String dataNascimento;

    private String genero;

    private String cpf;

    private String rg;

    private String estadoCivil;

    private String telefone;

    private String naturalidade;

    private String cep;

    private String cidade;

    private String estado;

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    private String pontoReferencia;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private TurmaEntity turma;
}