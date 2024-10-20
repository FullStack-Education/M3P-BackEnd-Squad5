package com.senai.projetofinal.controller;

import com.senai.projetofinal.controller.dto.response.dashboard.DashboardResponse;
import com.senai.projetofinal.service.DashboardService;
import com.senai.projetofinal.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final TokenService tokenService;

    public DashboardController(DashboardService dashboardService, TokenService tokenService) {
        this.dashboardService = dashboardService;
        this.tokenService = tokenService;
    }

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