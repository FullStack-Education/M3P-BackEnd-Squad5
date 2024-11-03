package com.senai.projetofinal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.senai.projetofinal.controller.dto.request.aluno.AtualizarAlunoRequest;
import com.senai.projetofinal.controller.dto.request.aluno.InserirAlunoRequest;
import com.senai.projetofinal.controller.dto.response.aluno.AlunoResponse;
import com.senai.projetofinal.datasource.entity.*;
import com.senai.projetofinal.datasource.repository.AlunoRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AlunoService alunoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos() {
        String token = "mock-token";
        AlunoEntity aluno1 = new AlunoEntity();
        aluno1.setId(1L);
        aluno1.setNome("Aluno 1");
        AlunoEntity aluno2 = new AlunoEntity();
        aluno2.setId(2L);
        aluno2.setNome("Aluno 2");

        List<AlunoEntity> alunos = List.of(aluno1, aluno2);

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findAll()).thenReturn(alunos);

        List<AlunoEntity> response = alunoService.listarTodos(token);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Aluno 1", response.get(0).getNome());
        assertEquals("Aluno 2", response.get(1).getNome());
    }

    @Test
    void salvar() {
        String token = "mock-token";
        InserirAlunoRequest request = new InserirAlunoRequest("Aluno Teste", "2000-01-01", 1L, 1L);
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        PapelEntity papel = new PapelEntity();
        papel.setNome(PapelEnum.ALUNO);
        usuario.setPapel(papel);
        TurmaEntity turma = new TurmaEntity();
        turma.setId(1L);

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
        when(alunoRepository.existsByNome("Aluno Teste")).thenReturn(false);
        when(alunoRepository.save(any(AlunoEntity.class))).thenAnswer(invocation -> {
            AlunoEntity aluno = invocation.getArgument(0);
            aluno.setId(1L);
            return aluno;
        });

        AlunoResponse response = alunoService.salvar(request, token);

        assertNotNull(response);
        assertEquals("Aluno Teste", response.nome());
    }

    @Test
    void removerPorId() {
        String token = "mock-token";
        Long alunoId = 1L;

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.existsById(alunoId)).thenReturn(true);

        alunoService.removerPorId(alunoId, token);

        verify(alunoRepository, times(1)).deleteById(alunoId);
    }

    @Test
    void atualizar() {
        String token = "mock-token";
        Long alunoId = 1L;
        AtualizarAlunoRequest request = new AtualizarAlunoRequest("Aluno Atualizado", "2000-01-01", 1L);
        AlunoEntity aluno = new AlunoEntity();
        aluno.setId(alunoId);
        aluno.setNome("Aluno Antigo");
        TurmaEntity turma = new TurmaEntity();
        turma.setId(1L);

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
        when(alunoRepository.existsByNome("Aluno Atualizado")).thenReturn(false);
        when(alunoRepository.save(any(AlunoEntity.class))).thenReturn(aluno);

        AlunoEntity response = alunoService.atualizar(request, alunoId, token);

        assertNotNull(response);
        assertEquals("Aluno Atualizado", response.getNome());
    }
}