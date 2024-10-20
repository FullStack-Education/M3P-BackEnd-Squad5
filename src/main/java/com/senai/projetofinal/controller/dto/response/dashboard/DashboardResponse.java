package com.senai.projetofinal.controller.dto.response.dashboard;

public record DashboardResponse(
        long totalAlunos,
        long totalDocentes,
        long totalTurmas
) {}