package com.senai.projetofinal.controller.dto.response;

public record DashboardResponse(
        long totalAlunos,
        long totalDocentes,
        long totalTurmas
) {}