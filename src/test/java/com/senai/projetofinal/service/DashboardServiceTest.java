package com.senai.projetofinal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.senai.projetofinal.controller.dto.response.dashboard.DashboardResponse;
import com.senai.projetofinal.datasource.repository.AlunoRepository;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DashboardServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDashboardData() {
        when(alunoRepository.count()).thenReturn(10L);
        when(docenteRepository.count()).thenReturn(5L);
        when(turmaRepository.count()).thenReturn(3L);

        DashboardResponse response = dashboardService.getDashboardData();

        assertNotNull(response);
        assertEquals(10L, response.totalAlunos());
        assertEquals(5L, response.totalDocentes());
        assertEquals(3L, response.totalTurmas());
    }
}