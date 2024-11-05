package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.turma.AtualizarTurmaRequest;
import com.senai.projetofinal.controller.dto.request.turma.InserirTurmaRequest;
import com.senai.projetofinal.controller.dto.response.TurmaResponse;
import com.senai.projetofinal.datasource.entity.TurmaEntity;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Turmas")
@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private final TurmaService service;

    @Operation(
            summary = "Listar todas as turmas",
            description = "Retorna uma lista de todas as turmas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de turmas retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurmaEntity.class)
                    )),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhuma turma encontrada",
                    content = @Content)
    })

    @GetMapping
    public ResponseEntity<List<TurmaEntity>> listarTodasTurmas(
            @RequestHeader("Authorization") String token) {
        List<TurmaEntity> listarTurmas = service.listarTodos(token.substring(7));
        return ResponseEntity.ok().body(listarTurmas);
    }

    @Operation(
            summary = "Buscar turma por ID",
            description = "Retorna os detalhes de uma turma específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turma encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurmaEntity.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Turma não encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "\"Erro: Turma não encontrada\""
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTurmaPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            TurmaEntity turma = service.buscarPorId(id, token.substring(7));
            return ResponseEntity.ok(turma);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Criar nova turma",
            description = "Realiza o cadastro de uma nova turma"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turma criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurmaResponse.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Erro ao criar turma",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "\"Erro: Dados inválidos\""
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> criarTurma(
            @RequestBody InserirTurmaRequest inserirTurmaRequest,
            @RequestHeader("Authorization") String token) {
        try {
            TurmaResponse criarTurmaResponse = service.salvar(inserirTurmaRequest, token.substring(7));
            return new ResponseEntity<>(criarTurmaResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Deletar turma",
            description = "Remove uma turma existente pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Turma deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Turma não encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "\"Erro: Turma não encontrada\""
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTurma(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            service.removerPorid(id, token.substring(7));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Atualizar turma",
            description = "Atualiza os dados de uma turma existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turma atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TurmaResponse.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Turma não encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "\"Erro: Turma não encontrada\""
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar turma",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "\"Erro: Dados inválidos\""
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTurma(
            @PathVariable Long id,
            @RequestBody AtualizarTurmaRequest atualizarTurmaRequest,
            @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(service.atualizar(atualizarTurmaRequest, id, token.substring(7)));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
