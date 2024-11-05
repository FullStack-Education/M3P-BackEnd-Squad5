package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.curso.InserirCursoRequest;
import com.senai.projetofinal.controller.dto.response.CursoResponse;
import com.senai.projetofinal.datasource.entity.CursoEntity;
import com.senai.projetofinal.datasource.repository.CursoRepository;
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
class CursoServiceTest {

    @Mock
    CursoRepository repository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    CursoService service;

    static CursoEntity curso;

    @BeforeAll
    public static void setUp() {
        curso = new CursoEntity();
        curso.setId(1L);
        curso.setNome("Curso Teste");
    }

    @Test
    @Order(1)
    void salvarCurso() {
        // given
        String token = "mock-token";
        InserirCursoRequest request = new InserirCursoRequest("Curso Teste");
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.save(any())).thenReturn(curso);

        // when
        CursoResponse retorno = service.salvar(request, token);

        // then
        assertNotNull(retorno);
        assertEquals(curso.getNome(), retorno.nome());

        verify(repository, times(1)).save(any());
    }

    @Test
    void listarTodosCursos() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findAll()).thenReturn(List.of(curso));

        // when
        List<CursoEntity> retorno = service.listarTodos(token);

        // then
        assertNotNull(retorno);
        assertEquals(curso.getNome(), retorno.get(0).getNome());
    }

    @Test
    @Order(2)
    void removerCurso() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.existsById(curso.getId())).thenReturn(true);

        // when
        assertDoesNotThrow(() -> service.removerPorId(curso.getId(), token));

        // then
        verify(repository, times(1)).deleteById(curso.getId());
    }

    @Test
    @Order(4)
    void retornarCurso() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(curso));

        // when
        assertDoesNotThrow(() -> service.buscarPorId(1L, token));
    }

    @Test
    void retornarCursoError() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> service.buscarPorId(0L, token));
    }
}