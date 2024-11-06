package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.materia.AtualizarMateriaRequest;
import com.senai.projetofinal.controller.dto.request.materia.InserirMateriaRequest;
import com.senai.projetofinal.controller.dto.response.MateriaResponse;
import com.senai.projetofinal.datasource.entity.MateriaEntity;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.service.MateriaService;
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

@Tag(name = "Materias", description = "CRUDs de Materia")
@RestController
@RequestMapping("/materias")
public class MateriaController {

    private final MateriaService service;

    public MateriaController(MateriaService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar todas as materias",
            description = "Lista todas as materias no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Materias listadas com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = MateriaResponse.class),
                            examples = @ExampleObject(
                                    value = "[{ \"id\": 2, " +
                                            "\"nome\": \"Matéria 1\" }, " +
                                            "{ \"id\": 3, " +
                                            "\"nome\": \"Matéria 2\" }] "
                            )
                    )
            ),

            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Não há materias cadastradas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Não há materias cadastradas"
                            )
                    ))
    })

    @GetMapping
    public ResponseEntity<List<MateriaEntity>> listarTodasMaterias(
            @RequestHeader("Authorization") String token) {
        List<MateriaEntity> listarMaterias = service.listarTodos(token.substring(7));
        return ResponseEntity.ok().body(listarMaterias);
    }



    @Operation(
            summary = "Buscar materia por id",
            description = "Busca uma materia pelo seu ID no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - materia encontrada com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = MateriaResponse.class),
                            examples = @ExampleObject(
                                    value = "[{ \"id\": 2, " +
                                            "\"nome\": \"nome da matéria\" }] "
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhuma materia encontrada com esse id",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Materia não encontrada"
                            )
                    ))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarMateriaPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            MateriaEntity materia = service.buscarPorId(id, token.substring(7));
            return ResponseEntity.ok(materia);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Buscar materia pelo id do curso",
            description = "Busca uma materia pelo ID do curso no qual ela está vinculada no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - materia encontrada com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = MateriaResponse.class),
                            examples = @ExampleObject(
                                    value = "[{ \"id\": 2, " +
                                            "\"nome\": \"nome da matéria\" }] "
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhuma materia encontrada vinculada ao curso com esse id",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Materia não encontrada"
                            )
                    ))
    })
    @GetMapping("/cursos/{curso_id}")
    public ResponseEntity<?> getMateriasByCurso(
            @PathVariable Long curso_id,
            @RequestHeader("Authorization") String token) {
        try {
            List<MateriaEntity> materias = service.buscarMateriasPorCursoId(curso_id, token.substring(7));
            return ResponseEntity.ok(materias);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Cadastrar uma nova materia",
            description = "Cadastra uma nova materia no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created - Materia cadastrada com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = MateriaResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 2, " +
                                            "\"nome\": \"nome da matéria\", " +
                                            "\"curso\": { " +
                                            "\"id\": 1, " +
                                            "\"nome\": \"nome do curso\", " +
                                            "\"materias\": [] } }"
                            )
                    )
            ),
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
    public ResponseEntity<?> criarMateria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{ \"nome\": \"Nome da matéria\", " +
                                            "\"curso\": 1 }"
                            )
                    )
            )
            @RequestBody InserirMateriaRequest inserirMateriaRequest,
            @RequestHeader("Authorization") String token) {
        try {
            MateriaResponse criarMateriaResponse = service.salvar(inserirMateriaRequest, token.substring(7));
            return new ResponseEntity<>(criarMateriaResponse, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Deletar uma matéria",
            description = "Deleta a matéria com ID passado do banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content - Matéria deletada com sucesso!",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhuma matéria encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhuma matéria encontrada com o id passado"
                            )
                    ))
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMateria(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            service.removerPorId(id, token.substring(7));
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Atualizar uma materia",
            description = "Atualiza a materia com ID passado no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Materia atualizada com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = MateriaResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 2, " +
                                            "\"nome\": \"nome da matéria\" }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nome não pode ser nulo ou vazio"    )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhuma matéria encontrada",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhuma matéria com o ID passado foi encontrada"
                            )
                    ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMateria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{ \"nome\": \"Nome da matéria 2\", " +
                                            "\"curso\": 1 }"
                            )
                    )
            )
            @RequestBody AtualizarMateriaRequest atualizarMateriaRequest,
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(service.atualizar(atualizarMateriaRequest, id, token.substring(7)));
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
