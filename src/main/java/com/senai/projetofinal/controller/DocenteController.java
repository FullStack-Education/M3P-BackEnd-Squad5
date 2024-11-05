package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.docente.AtualizarDocenteRequest;
import com.senai.projetofinal.controller.dto.request.docente.InserirDocenteRequest;
import com.senai.projetofinal.controller.dto.response.DocenteResponse;
import com.senai.projetofinal.datasource.entity.DocenteEntity;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import com.senai.projetofinal.service.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Docentes", description = "CRUDs de Docente")
@Slf4j
@RestController
@RequestMapping("/docentes")
public class DocenteController {

    private final DocenteService service;

    public DocenteController(DocenteService docenteService) {
        this.service = docenteService;
    }



    @Operation(
            summary = "Listar todos os docentes",
            description = "Lista todos os docentes no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Docentes listados com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = DocenteResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 3, " +
                                            "\"nome\": \"Charles Xavier\", " +
                                            "\"dataNascimento\": \"1952-05-12\", " +
                                            "\"genero\": \"MASCULINO\", " +
                                            "\"cpf\": \"123.456.789-10\", " +
                                            "\"rg\": \"987654321\", " +
                                            "\"estadoCivil\": \"Solteiro\", " +
                                            "\"telefone\": \"(11) 99999-9999\", " +
                                            "\"email\": \"charles.xavier@xmen.com\", " +
                                            "\"senha\": \"mindcontrol123\", " +
                                            "\"naturalidade\": \"Nova York\", " +
                                            "\"cep\": \"01000-000\", " +
                                            "\"cidade\": \"Nova York\", " +
                                            "\"estado\": \"NY\", " +
                                            "\"logradouro\": \"Xavier Institute\", " +
                                            "\"numero\": \"1000\", " +
                                            "\"complemento\": \"Mansão X\", " +
                                            "\"bairro\": \"Westchester\", " +
                                            "\"pontoReferencia\": \"Próximo ao Jardim Cerebro\", " +
                                            "\"materias\": [\"Telepatia Avançada\", \"Liderança e Estratégia\", \"História dos Mutantes\", \"Treinamento Mental\"], " +
                                            "\"dataEntrada\": \"2024-10-26\", " +
                                            "\"usuario\": { " +
                                            "\"id\": 2, " +
                                            "\"login\": \"pedagogico\", " +
                                            "\"senha\": \"$2a$10$9EX9No/q.np31WlkM1mRL.4oTiZ9VXWK8vwIRxg1DXkfB0qzxbWmy\", " +
                                            "\"papel\": { " +
                                            "\"id\": 2, " +
                                            "\"nome\": \"PEDAGOGICO\" } } }"
                            )
                    )
            ),

    @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Não há docentes cadastrados",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Não há docentes cadastrados"
                            )
                    ))
    })

    @GetMapping
    public ResponseEntity<List<DocenteEntity>> listarTodosDocentes(
            @RequestHeader("Authorization") String token) {
        List<DocenteEntity> listaDocentes = service.listarTodos(token.substring(7));
        return ResponseEntity.ok().body(listaDocentes);
    }



    @Operation(
            summary = "Buscar docente por id",
            description = "Busca um docente pelo seu ID no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - docente encontrado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = DocenteResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 3, " +
                                            "\"nome\": \"Charles Xavier\", " +
                                            "\"dataNascimento\": \"1952-05-12\", " +
                                            "\"genero\": \"MASCULINO\", " +
                                            "\"cpf\": \"123.456.789-10\", " +
                                            "\"rg\": \"987654321\", " +
                                            "\"estadoCivil\": \"Solteiro\", " +
                                            "\"telefone\": \"(11) 99999-9999\", " +
                                            "\"email\": \"charles.xavier@xmen.com\", " +
                                            "\"senha\": \"mindcontrol123\", " +
                                            "\"naturalidade\": \"Nova York\", " +
                                            "\"cep\": \"01000-000\", " +
                                            "\"cidade\": \"Nova York\", " +
                                            "\"estado\": \"NY\", " +
                                            "\"logradouro\": \"Xavier Institute\", " +
                                            "\"numero\": \"1000\", " +
                                            "\"complemento\": \"Mansão X\", " +
                                            "\"bairro\": \"Westchester\", " +
                                            "\"pontoReferencia\": \"Próximo ao Jardim Cerebro\", " +
                                            "\"materias\": [\"Telepatia Avançada\", \"Liderança e Estratégia\", \"História dos Mutantes\", \"Treinamento Mental\"], " +
                                            "\"dataEntrada\": \"2024-10-26\", " +
                                            "\"usuario\": { " +
                                            "\"id\": 2, " +
                                            "\"login\": \"pedagogico\", " +
                                            "\"senha\": \"$2a$10$9EX9No/q.np31WlkM1mRL.4oTiZ9VXWK8vwIRxg1DXkfB0qzxbWmy\", " +
                                            "\"papel\": { " +
                                            "\"id\": 2, " +
                                            "\"nome\": \"PEDAGOGICO\" } } }"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhum docente encontrado com esse id",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Docente não encontrado"
                            )
                    ))
    })


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDocentePorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            DocenteEntity docente = service.buscarPorId(id, token.substring(7));
            return ResponseEntity.ok().body(docente);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Cadastrar um novo docente",
            description = "Cadastra um novo docente no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created - Docente cadastrado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = DocenteResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 3, " +
                                            "\"nome\": \"Charles Xavier\", " +
                                            "\"dataNascimento\": \"1952-05-12\", " +
                                            "\"genero\": \"MASCULINO\", " +
                                            "\"cpf\": \"123.456.789-10\", " +
                                            "\"rg\": \"987654321\", " +
                                            "\"estadoCivil\": \"Solteiro\", " +
                                            "\"telefone\": \"(11) 99999-9999\", " +
                                            "\"email\": \"charles.xavier@xmen.com\", " +
                                            "\"senha\": \"mindcontrol123\", " +
                                            "\"naturalidade\": \"Nova York\", " +
                                            "\"cep\": \"01000-000\", " +
                                            "\"cidade\": \"Nova York\", " +
                                            "\"estado\": \"NY\", " +
                                            "\"logradouro\": \"Xavier Institute\", " +
                                            "\"numero\": \"1000\", " +
                                            "\"complemento\": \"Mansão X\", " +
                                            "\"bairro\": \"Westchester\", " +
                                            "\"pontoReferencia\": \"Próximo ao Jardim Cerebro\", " +
                                            "\"materias\": [\"Telepatia Avançada\", \"Liderança e Estratégia\", \"História dos Mutantes\", \"Treinamento Mental\"], " +
                                            "\"dataEntrada\": \"2024-10-26\", " +
                                            "\"usuario\": { " +
                                            "\"id\": 2, " +
                                            "\"login\": \"pedagogico\", " +
                                            "\"senha\": \"$2a$10$9EX9No/q.np31WlkM1mRL.4oTiZ9VXWK8vwIRxg1DXkfB0qzxbWmy\", " +
                                            "\"papel\": { " +
                                            "\"id\": 2, " +
                                            "\"nome\": \"PEDAGOGICO\" } } }"
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
    public ResponseEntity<?> criarDocente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{ \"nome\": \"Logan\", " +
                                            "\"dataNascimento\": \"12-05-1882\", " +
                                            "\"genero\": \"MASCULINO\", " +
                                            "\"cpf\": \"12345678910\", " +
                                            "\"rg\": \"987654321\", " +
                                            "\"estadoCivil\": \"Solteiro\", " +
                                            "\"telefone\": \"(11) 99999-9999\", " +
                                            "\"email\": \"logan.wolverine@xmen.com\", " +
                                            "\"senha\": \"adamantium123\", " +
                                            "\"naturalidade\": \"Alberta\", " +
                                            "\"cep\": \"01000-000\", " +
                                            "\"cidade\": \"Nova York\", " +
                                            "\"estado\": \"NY\", " +
                                            "\"logradouro\": \"Xavier Institute\", " +
                                            "\"numero\": \"1000\", " +
                                            "\"complemento\": \"Subsolo\", " +
                                            "\"bairro\": \"Westchester\", " +
                                            "\"pontoReferencia\": \"Próximo à Mansão X\", " +
                                            "\"materias\": [\"Combate\", \"Autodefesa\", \"História Mutante\"], " +
                                            "\"usuario\": 14 }"
                            )
                    )
            )
            @RequestBody InserirDocenteRequest inserirDocenteRequest,
            @RequestHeader("Authorization") String token) {
        try {
            DocenteResponse criarDocenteResponse = service.salvar(inserirDocenteRequest, token.substring(7));
            return new ResponseEntity<>(criarDocenteResponse, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Deletar um docente",
            description = "Deleta o docente com ID passado do banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content - Docente deletado com sucesso!",
                    content = @Content(
                                    examples = @ExampleObject(
                                    value = ""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhum docente encontrado",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhum docente encontrado com o id passado"
                            )
                    ))
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarDocente(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            service.removerPorId(id, token.substring(7));
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(
            summary = "Atualizar um docente",
            description = "Atualiza o docente com ID passado no banco de dados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Docente atualizado com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = DocenteResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 9, " +
                                            "\"nome\": \"Charles Xavier 2\", " +
                                            "\"dataNascimento\": \"1952-05-12\", " +
                                            "\"genero\": \"MASCULINO\", " +
                                            "\"cpf\": \"123.456.789-10\", " +
                                            "\"rg\": \"987654321\", " +
                                            "\"estadoCivil\": \"Solteiro\", " +
                                            "\"telefone\": \"(11) 99999-9999\", " +
                                            "\"email\": \"charles.xavier@xmen.com\", " +
                                            "\"senha\": \"mindcontrol123\", " +
                                            "\"naturalidade\": \"Nova York\", " +
                                            "\"cep\": \"01000-000\", " +
                                            "\"cidade\": \"Nova York\", " +
                                            "\"estado\": \"NY\", " +
                                            "\"logradouro\": \"Xavier Institute\", " +
                                            "\"numero\": \"1000\", " +
                                            "\"complemento\": \"Mansão X\", " +
                                            "\"bairro\": \"Westchester\", " +
                                            "\"pontoReferencia\": \"Próximo ao Jardim Cerebro\", " +
                                            "\"materias\": [\"Telepatia Avançada\", \"Liderança e Estratégia\", \"História dos Mutantes\", \"Treinamento Mental\"], " +
                                            "\"dataEntrada\": \"2024-10-26\", " +
                                            "\"usuario\": { " +
                                            "\"id\": 9, " +
                                            "\"login\": \"log.wolverine@xmen.com\", " +
                                            "\"senha\": \"$2a$10$kor5BlkTyOqzvLZOPyx5GOmdUMhDLXH4RcWNniXbxfRaLKEoumP4m\", " +
                                            "\"papel\": { " +
                                            "\"id\": 4, " +
                                            "\"nome\": \"PROFESSOR\" } } }"))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nome não pode ser nulo ou vazio"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário não autorizado"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Not Found - Nenhum docente encontrado",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Nenhum docente com o ID passado foi encontrado"
                            )
                    ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarDocente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{ \"nome\": \"Charles Xavier\", " +
                                            "\"dataNascimento\": \"12-05-1952\", " +
                                            "\"genero\": \"MASCULINO\", " +
                                            "\"cpf\": \"123.456.789-10\", " +
                                            "\"rg\": \"987654321\", " +
                                            "\"estadoCivil\": \"Solteiro\", " +
                                            "\"telefone\": \"(11) 99999-9999\", " +
                                            "\"email\": \"charles.xavier@xmen.com\", " +
                                            "\"senha\": \"mindcontrol123\", " +
                                            "\"naturalidade\": \"Nova York\", " +
                                            "\"cep\": \"01000-000\", " +
                                            "\"cidade\": \"Nova York\", " +
                                            "\"estado\": \"NY\", " +
                                            "\"logradouro\": \"Xavier Institute\", " +
                                            "\"numero\": \"1000\", " +
                                            "\"complemento\": \"Mansão X\", " +
                                            "\"bairro\": \"Westchester\", " +
                                            "\"pontoReferencia\": \"Próximo ao Jardim Cerebro\", " +
                                            "\"materias\": [\"Telepatia Avançada\", \"Liderança e Estratégia\", \"História dos Mutantes\", \"Treinamento Mental\"] " +
                                            "}"
                            )
                    )
            )
            @RequestBody AtualizarDocenteRequest atualizarDocenteRequest,
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            DocenteEntity atualizarDocente = service.atualizar(atualizarDocenteRequest, id, token.substring(7));
            return new ResponseEntity<>(atualizarDocente, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
