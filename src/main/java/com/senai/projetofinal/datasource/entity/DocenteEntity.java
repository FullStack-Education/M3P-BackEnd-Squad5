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

    @Column(unique = true, nullable = false)
    @Size(min = 8, max = 64)
    private String nome;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @NotNull
    @Column(nullable = false)
    private String genero;

    @Column(nullable = false, unique = true)
    private String cpf;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false)
    private String rg;

    @NotBlank
    @Column(nullable = false)
    private String estadoCivil;

    @Column(nullable = false)
    private String telefone;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String senha;  // TODO: A senha deve ser criptografada ao salvar

    @NotBlank
    @Size(min = 8, max = 64)
    @Column(nullable = false)
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

    @ElementCollection
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
