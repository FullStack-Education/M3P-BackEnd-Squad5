package com.senai.projetofinal.datasource.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Entity
@Data
@Table(name = "turma")
public class TurmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    @ManyToOne
    private DocenteEntity docente;

    @ManyToOne
    private CursoEntity curso;

    @OneToMany(mappedBy = "turma", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<AlunoEntity> alunos;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataTermino;

    @Column(nullable = false)
    private String horario;
}