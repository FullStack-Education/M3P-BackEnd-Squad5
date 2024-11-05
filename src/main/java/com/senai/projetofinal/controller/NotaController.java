package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.nota.AtualizarNotaRequest;
import com.senai.projetofinal.controller.dto.request.nota.InserirNotaRequest;
import com.senai.projetofinal.controller.dto.response.CursoResponse;
import com.senai.projetofinal.controller.dto.response.NotaResponse;
import com.senai.projetofinal.datasource.entity.NotaEntity;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.infra.exception.error.SecurityException;
import com.senai.projetofinal.service.NotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Notas", description = "CRUD de Notas")
@RestController
@RequestMapping("/notas")
public class NotaController {

    private final NotaService service;

    public NotaController(NotaService service) {
        this.service = service;
    }


    @Operation(
            summary = "Listar todas Notas",
            description = "Lista todas as Notas no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "OK - Notas listadas com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = CursoResponse.class),
                                examples = @ExampleObject(
                                        value = "[{\"aluno\": 1, \"docente\": 1, \"materia\": 1, \"valor\": 10}," +
                                                "{\"aluno\": 2, \"docente\": 1, \"materia\": 1, \"valor\": 8 }," +
                                                "{\"aluno\": 2, \"docente\": 2, \"materia\": 3, \"valor\": 9}]"))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                        description = "Not Found - Não há notas cadastradas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Não há notas cadastrados")))
    })
    @GetMapping
    public ResponseEntity<?> listarTodasNotas(
            @RequestHeader("Authorization") String token) {
        try {
            List<NotaEntity> listarNotas = service.listarTodos(token.substring(7));
            return ResponseEntity.ok().body(listarNotas);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Buscar nota por id",
            description = "Busca uma nota pelo seu ID no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "OK - Nota encontrada com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = CursoResponse.class),
                                examples = @ExampleObject(
                                        value = "{\"aluno\": 1, \"docente\": 1, \"materia\": 1, \"valor\": 10}"))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                        description = "Not Found - Nenhuma nota encontrada",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Nenhuma nota com o ID passado foi encontrada")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarNotaPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            NotaEntity nota = service.buscarPorId(id, token.substring(7));
            return ResponseEntity.ok(nota);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Buscar nota por docente",
            description = "Busca todas as notas dadas por um docente no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK - Nota encontrada com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"aluno\": 1, \"docente\": 1, \"materia\": 1, \"valor\": 10}," +
                                            "{\"aluno\": 1, \"docente\": 1, \"materia\": 1, \"valor\": 5}"))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                    description = "Not Found - Nenhuma nota encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhuma nota com o ID passado foi encontrada")))
    })
    @GetMapping("/docentes/{docente_id}")
    public ResponseEntity<?> getNotasByDocente(
            @PathVariable Long docente_id,
            @RequestHeader("Authorization") String token) {
        try {
            List<NotaEntity> notas = service.buscarNotasPorDocenteId(docente_id, token.substring(7));
            return ResponseEntity.ok(notas);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Listar notas de um aluno",
            description = "Lista todas as notas de um aluno"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "OK - Lista de notas retornada com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = CursoResponse.class),
                                examples = @ExampleObject(
                                        value = "[{\"aluno\": 1,\"docente\": 1, \"materia\": 1, \"valor\": 10}," +
                                                "{\"aluno\": 1, \"docente\": 1, \"materia\": 1, \"valor\": 7}]"))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                        description = "Not Found - Aluno não encontrado",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Aluno não encontrado")))
    })
    @GetMapping("/alunos/{aluno_id}")
    public ResponseEntity<?> getNotasByAluno(
            @PathVariable Long aluno_id,
            @RequestHeader("Authorization") String token) {
        try {
            List<NotaEntity> notas = service.buscarNotasPorAlunoId(aluno_id, token.substring(7));
            return ResponseEntity.ok(notas);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Calcula pontuação",
            description = "calcula a pontuação total do aluno"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "Ok - Pontuação calculada com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = NotaResponse.class),
                                examples = @ExampleObject(value = "9.0" ))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                        description = "Not Found - pontuação não encontrada",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Pontuação não encontrada")))
    }
    )
    @GetMapping("/alunos/{aluno_id}/pontuacao")
    public ResponseEntity<?> getPontuacaoByAluno(
            @PathVariable Long aluno_id,
            @RequestHeader("Authorization") String token) {
        try {
            BigDecimal pontuacao = service.calcularPontuacao(aluno_id, token.substring(7));
            return ResponseEntity.ok(pontuacao);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Cadastrar uma nova nota",
            description = "Cadastra uma nova nota no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                        description = "Created - Nota cadastrada com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = CursoResponse.class),
                                examples = @ExampleObject(
                                        value = "{ \"aluno\": 1, + \"docente\": 1, \"materia\": 1, \"valor\": 6 }"))),
            @ApiResponse(responseCode = "400",
                        description = "Bad Request - Dados ausentes ou inválidos",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Matéria não pode ser nulo ou vazio"))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado")))
    })
    @PostMapping
    public ResponseEntity<?> criarNota(
            @RequestBody InserirNotaRequest inserirNotaRequest,
            @RequestHeader("Authorization") String token) {
        try {
            NotaResponse criarNotaResponse = service.salvar(inserirNotaRequest, token.substring(7));
            return new ResponseEntity<>(criarNotaResponse, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(
            summary = "Deletar uma nota",
            description = "Deleta a nota com ID passado do banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                        description = "No Content - Nota deletada com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = CursoResponse.class),
                                examples = @ExampleObject(
                                        value = "{\"aluno\":, \"docente\":, \"materia\":, \"valor\":}"))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                        description = "Not Found - Nenhuma nota encontrada",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Nenhuma nota com o ID passado foi encontrado")
                        ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarNota(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            service.removerPorId(id, token.substring(7));
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Atualizar uma nota",
            description = "Atualiza a nota com ID passado no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "OK - Nota atualizado com sucesso!",
                        content = @Content(
                                schema = @Schema(implementation = CursoResponse.class),
                                examples = @ExampleObject(
                                        value = "{ \"valor\": 10 }"))),
            @ApiResponse(responseCode = "400",
                        description = "Bad Request - Dados ausentes ou inválidos",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Valor não pode ser nulo ou vazio"))),
            @ApiResponse(responseCode = "401",
                        description = "Unauthorized - Credenciais inválidas",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Usuário não autorizado"))),
            @ApiResponse(responseCode = "404",
                        description = "Not Found - Nenhuma nota encontrada",
                        content = @Content(
                                examples = @ExampleObject(
                                        value = "Nenhuma nota com o ID passado foi encontrada")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarNota(
            @PathVariable Long id,
            @RequestBody AtualizarNotaRequest atualizarNotaRequest,
            @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(service.atualizar(atualizarNotaRequest, id, token.substring(7)));
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
