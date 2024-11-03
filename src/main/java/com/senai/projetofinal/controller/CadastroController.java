package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.InserirLoginRequest;
import com.senai.projetofinal.service.UsuarioService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Endpoint de Cadastro")
@RestController
@RequiredArgsConstructor
public class CadastroController {

    private final UsuarioService usuarioService;

    @Operation(
            summary = "Cadastro de novo usuário",
            description = "Realiza o cadastro de um novo usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(
                                    value = "\"Usuário Salvo!\""
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar usuário",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "\"Erro: Nome de Login já existe\""
                            )
                    )),
            @ApiResponse(responseCode = "401", description = "Erro ao cadastrar usuário sem token",
                    content = @Content)

    })

    @PostMapping("/cadastro")
    public ResponseEntity<String> novoLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário para cadastro",
                    content = @Content(
                            schema = @Schema(implementation = InserirLoginRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"nomeLogin\": \"fulano\", \"senha\": \"root\", \"nomePapel\": \"aluno\" }"
                            )
                    )
            )
            @RequestBody InserirLoginRequest inserirLoginRequest,
            @RequestHeader("Authorization") String token) {
        try {
            usuarioService.cadastraNovoLogin(inserirLoginRequest, token.substring(7));
            return ResponseEntity.ok("Usuário Salvo!");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
