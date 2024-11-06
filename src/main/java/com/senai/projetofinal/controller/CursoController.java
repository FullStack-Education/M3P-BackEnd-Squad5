package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.curso.AtualizarCursoRequest;
import com.senai.projetofinal.controller.dto.request.curso.InserirCursoRequest;
import com.senai.projetofinal.controller.dto.response.CursoResponse;
import com.senai.projetofinal.datasource.entity.CursoEntity;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.infra.exception.error.SecurityException;
import com.senai.projetofinal.service.CursoService;
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

import java.util.List;

@Tag(name = "Cursos", description = "CRUDs de Curso")
@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar todos cursos",
            description = "Lista todos os cursos no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Cursos listados com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"Nome\": \"nome do curso\" }"))),

            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Não há cursos cadastrados",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Não há cursos cadastrados"
                            )
                    ))
    })
    @GetMapping
    public ResponseEntity<?> listarTodosCursos(
            @RequestHeader("Authorization") String token) {
        try {
            List<CursoEntity> listaCursos = service.listarTodos(token.substring(7));
            return ResponseEntity.ok().body(listaCursos);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Buscar curso por id",
            description = "Busca um curso pelo seu ID no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Curso encontrado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"Nome\": \"nome do curso\" }"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhum curso encontrado",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhum curso com o ID passado foi encontrado"
                            )
                    ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCursoPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            CursoEntity curso = service.buscarPorId(id, token.substring(7));
            return ResponseEntity.ok().body(curso);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Cadastrar um novo curso",
            description = "Cadastra um novo curso no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created - Curso cadastrado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"Nome\": \"nome do curso\" }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nome não pode ser nulo ou vazio"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    ))
    })
    @PostMapping
    public ResponseEntity<?> criarCurso(
            @RequestBody InserirCursoRequest inserirCursoRequest,
            @RequestHeader("Authorization") String token) {
        try {
            CursoResponse criarCursoResponse = service.salvar(inserirCursoRequest, token.substring(7));
            return new ResponseEntity<>(criarCursoResponse, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Deletar um curso",
            description = "Deleta o curso com ID passado do banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content - Curso deletado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"Nome\": \"nome do curso\" }"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhum curso encontrado",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhum curso com o ID passado foi encontrado"
                            )
                    ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCurso(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            service.removerPorId(id, token.substring(7));
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(
            summary = "Atualizar um curso",
            description = "Atualiza o curso com ID passado no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Curso atualizado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"Nome\": \"nome do curso\" }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nome não pode ser nulo ou vazio"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhum curso encontrado",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhum curso com o ID passado foi encontrado"
                            )
                    ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCurso(
            @RequestBody AtualizarCursoRequest atualizarCursoRequest,
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            CursoEntity atualizarCurso = service.atualizar(atualizarCursoRequest, id, token.substring(7));
            return new ResponseEntity<>(atualizarCurso, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Listar cursos de um aluno",
            description = "Lista todos os cursos de um aluno"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Lista de cursos retornada com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = CursoResponse.class),
                            examples = @ExampleObject(
                                    value = "[{ \"id\": 1, \"nome\": \"Curso A\" }, { \"id\": 2, \"nome\": \"Curso B\" }]"))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "ID do aluno não pode ser nulo ou vazio"
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Aluno não encontrado",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Aluno não encontrado"
                            )
                    ))
    })
    @GetMapping("/cursos/{idAluno}")
    public ResponseEntity<?> listarCursosPorAlunoTurma(
            @RequestParam("idAluno") Long idAluno,
            @RequestHeader("Authorization") String token) {
        try {
            List<CursoResponse> cursos = service.listarCursosPorAlunoId(idAluno, token.substring(7));
            return ResponseEntity.ok().body(cursos);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (java.lang.SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
