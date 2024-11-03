package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.aluno.AtualizarAlunoRequest;
import com.senai.projetofinal.controller.dto.request.aluno.InserirAlunoRequest;
import com.senai.projetofinal.controller.dto.response.aluno.AlunoResponse;
import com.senai.projetofinal.datasource.entity.AlunoEntity;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Alunos")
@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService service;

    @Operation(summary = "Listar todos os alunos", description = "Recupera uma lista de todos os alunos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alunos listados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlunoEntity.class)))
    })
    @GetMapping
    public ResponseEntity<List<AlunoEntity>> listarTodosAlunos(
            @RequestHeader("Authorization") String token) {
        List<AlunoEntity> listarAlunos = service.listarTodos(token.substring(7));
        return ResponseEntity.ok().body(listarAlunos);
    }

    @Operation(summary = "Buscar aluno por ID", description = "Busca um aluno específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlunoEntity.class))),
            @ApiResponse(responseCode = "400", description = "Erro ao buscar aluno",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAlunoPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            AlunoEntity aluno = service.buscarPorId(id, token.substring(7));
            return new ResponseEntity<>(aluno, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Criar um novo aluno", description = "Realiza o cadastro de um novo aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlunoResponse.class),
                            examples = @ExampleObject(value = "{ \"id\": 1, \"nome\": \"Fulano\", \"dataNascimento\": \"30-04-1996\", \"usuario\": 1, \"turma\": 1 }"))),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar aluno",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> criarAluno(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do novo aluno para cadastro",
                    content = @Content(schema = @Schema(implementation = InserirAlunoRequest.class),
                            examples = @ExampleObject(value = "{ \"nome\": \"Fulano\", \"dataNascimento\": \"30-04-1996\", \"usuario\": 1, \"turma\": 1 }")))
            @RequestBody InserirAlunoRequest inserirAlunoRequest,
            @RequestHeader("Authorization") String token) {
        try {
            AlunoResponse criarAlunoResponse = service.salvar(inserirAlunoRequest, token.substring(7));
            return new ResponseEntity<>(criarAlunoResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Deletar um aluno", description = "Remove um aluno do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao deletar aluno",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAluno(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            service.removerPorId(id, token.substring(7));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Atualizar um aluno", description = "Atualiza os dados de um aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlunoEntity.class))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar aluno",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAluno(
            @Parameter(description = "ID do aluno para atualizar") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do aluno",
                    content = @Content(schema = @Schema(implementation = AtualizarAlunoRequest.class),
                            examples = @ExampleObject(value = "{ \"nome\": \"Fulano Update\", \"dataNascimento\": \"30-04-1996\", \"usuario\": 1, \"turma\": 1 }")))
            @RequestBody AtualizarAlunoRequest atualizarAlunoRequest,
            @RequestHeader("Authorization") String token) {
        try {
            AlunoEntity atualizarAlunoResponse = service.atualizar(atualizarAlunoRequest, id, token.substring(7));
            return new ResponseEntity<>(atualizarAlunoResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
