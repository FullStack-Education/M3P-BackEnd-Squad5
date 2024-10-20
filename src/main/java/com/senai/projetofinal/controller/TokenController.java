package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.request.LoginRequest;
import com.senai.projetofinal.controller.dto.response.LoginResponse;
import com.senai.projetofinal.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Endpoint de Login")
public class TokenController {

    private final TokenService tokenService;

    private static long TEMPO_EXPIRACAO = 360000L;

    @Operation(
            summary = "Gera um token JWT",
            description = "Dadas as credenciais do usuário gera um token de autenticação."
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Login bem sucedido!",
                    content = @Content(
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"valorJWT\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"tempoExpiracao\": 360000 }"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = " \"Usuário incorreto\" \n \"Senha incorreta\""
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = " \"Login não pode ser nulo ou vazio\" \n \"Senha não pode ser nula ou vazia\""
                            )
                    ))
    })


    @PostMapping("/login")
    public ResponseEntity<?> gerarToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{ \"login\": \"admin\", \"senha\": \"1234\" }"
                            )
                    )
            )
            @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = tokenService.gerarToken(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
