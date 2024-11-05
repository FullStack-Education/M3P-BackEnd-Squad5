package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.response.DashboardResponse;
import com.senai.projetofinal.datasource.repository.AlunoRepository;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    private final TurmaRepository turmaRepository;

    public DashboardService(AlunoRepository alunoRepository, DocenteRepository docenteRepository, TurmaRepository turmaRepository) {
        this.alunoRepository = alunoRepository;
        this.docenteRepository = docenteRepository;
        this.turmaRepository = turmaRepository;
    }

    public DashboardResponse getDashboardData() {
        return new DashboardResponse(
                alunoRepository.count(),
                docenteRepository.count(),
                turmaRepository.count()
        );
    }
}