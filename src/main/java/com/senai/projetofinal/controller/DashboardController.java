package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.response.DashboardResponse;
import com.senai.projetofinal.service.DashboardService;
import com.senai.projetofinal.service.TokenService;
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

@Tag(name = "Dashboard - Consulta", description = "Consulta os dados da dashboard")
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final TokenService tokenService;

    public DashboardController(DashboardService dashboardService, TokenService tokenService) {
        this.dashboardService = dashboardService;
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Busca dados da Dashboard",
            description = "Mostra o total de alunos, docentes e turmas cadastrados no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Dados buscados com sucesso!",
                    content = @Content(
                            schema = @Schema(implementation = DashboardResponse.class),
                            examples = @ExampleObject(
                                    value = "{ \"totalAlunos\": 1, \"totalDocentes\": 1, \"totalTurmas\": 1 }"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Credenciais inválidas",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Usuário sem permissão"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Bad Request - Dados ausentes ou inválidos",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "Erro ao buscar dados da Dashboard"
                            )
                    ))
    })
    @GetMapping
    public ResponseEntity<?> getDashboardData(@RequestHeader("Authorization") String token) {
        try {
            String role = tokenService.buscaCampo(token.substring(7), "scope");
            if (!"admin".equals(role)) {
                return new ResponseEntity<>("Usuário não autorizado", HttpStatus.UNAUTHORIZED);
            }

            DashboardResponse response = dashboardService.getDashboardData();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}