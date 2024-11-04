package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.turma.InserirTurmaRequest;
import com.senai.projetofinal.controller.dto.response.turma.TurmaResponse;
import com.senai.projetofinal.datasource.entity.*;
import com.senai.projetofinal.datasource.repository.CursoRepository;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TurmaServiceTest {

    @Mock
    TurmaRepository turmaRepository;

    @Mock
    CursoRepository cursoRepository;

    @Mock
    DocenteRepository docenteRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    TurmaService turmaService;

    static TurmaEntity turma;

    @BeforeAll
    public static void setUp() {
        turma = new TurmaEntity();
        turma.setId(1L);
        turma.setNome("Turma Teste");
    }

    @Test
    @Order(1)
    void salvarTurma() {
        // given
        String token = "mock-token";
        InserirTurmaRequest request = new InserirTurmaRequest("Turma Teste", 1L, 1L);

        CursoEntity curso = new CursoEntity();
        curso.setId(1L);
        curso.setNome("Curso Teste");

        DocenteEntity docente = new DocenteEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        PapelEntity papel = new PapelEntity();
        papel.setNome(PapelEnum.PROFESSOR);
        usuario.setPapel(papel);
        docente.setUsuario(usuario);
        docente.setId(1L);

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(turmaRepository.save(any())).thenReturn(turma);

        // when
        TurmaResponse retorno = turmaService.salvar(request, token);

        // then
        assertNotNull(retorno);
        assertEquals(turma.getNome(), retorno.nome());

        verify(turmaRepository, times(1)).save(any());
    }

    @Test
    void listarTodasTurmas() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(turmaRepository.findAll()).thenReturn(List.of(turma));

        // when
        List<TurmaEntity> retorno = turmaService.listarTodos(token);

        // then
        assertNotNull(retorno);
        assertEquals(turma.getNome(), retorno.get(0).getNome());
    }

    @Test
    @Order(2)
    void removerTurma() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(turmaRepository.existsById(turma.getId())).thenReturn(true);

        // when
        assertDoesNotThrow(() -> turmaService.removerPorid(turma.getId(), token));

        // then
        verify(turmaRepository, times(1)).deleteById(turma.getId());
    }


    @Test
    @Order(4)
    void retornarTurma() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(turma));

        // when
        assertDoesNotThrow(() -> turmaService.buscarPorId(1L, token));
    }

    @Test
    void retornarTurmaError() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(turmaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> turmaService.buscarPorId(0L, token));
    }
}