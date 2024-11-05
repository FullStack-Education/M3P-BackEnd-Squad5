package com.senai.projetofinal.datasource.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "docente")
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 8, max = 64)
    private String nome;

    private LocalDate dataNascimento;

    private String genero;

    @Column(nullable = false)
    private String cpf;

    @Size(max = 20)
    private String rg;

    private String estadoCivil;

    private String telefone;

    @Email
    private String email;

    @Size(min = 8)
    private String senha;

    @Size(min = 8, max = 64)
    private String naturalidade;  // Local de nascimento

    // Endereço
    private String cep;
    private String cidade;
    private String estado;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String pontoReferencia;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "docente_materias", joinColumns = @JoinColumn(name = "docente_id"))
    @Column(name = "materia")
    private List<String> materias;

    private LocalDate dataEntrada = LocalDate.now();

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @PrePersist
    public void prePersist() {
        if (dataEntrada == null) {
            dataEntrada = LocalDate.now();
        }
    }
}
