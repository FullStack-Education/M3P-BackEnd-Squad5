package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.materia.AtualizarMateriaRequest;
import com.senai.projetofinal.controller.dto.request.materia.InserirMateriaRequest;
import com.senai.projetofinal.controller.dto.response.MateriaResponse;
import com.senai.projetofinal.datasource.entity.CursoEntity;
import com.senai.projetofinal.datasource.entity.MateriaEntity;
import com.senai.projetofinal.datasource.repository.CursoRepository;
import com.senai.projetofinal.datasource.repository.MateriaRepository;
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
class MateriaServiceTest {

    @Mock
    MateriaRepository materiaRepository;

    @Mock
    CursoRepository cursoRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    MateriaService materiaService;

    static MateriaEntity materia;

    @BeforeAll
    public static void setUp() {
        materia = new MateriaEntity();
        materia.setId(1L);
        materia.setNome("Materia Teste");
    }

    @Test
    @Order(1)
    void salvarMateria() {
        // given
        String token = "mock-token";
        InserirMateriaRequest request = new InserirMateriaRequest("Materia Teste", 1L);
        CursoEntity curso = new CursoEntity();
        curso.setId(1L);
        curso.setNome("Curso Teste");
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(materiaRepository.save(any())).thenReturn(materia);

        // when
        MateriaResponse retorno = materiaService.salvar(request, token);

        // then
        assertNotNull(retorno);
        assertEquals(materia.getNome(), retorno.nome());

        verify(materiaRepository, times(1)).save(any());
    }

    @Test
    void listarTodasMaterias() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(materiaRepository.findAll()).thenReturn(List.of(materia));

        // when
        List<MateriaEntity> retorno = materiaService.listarTodos(token);

        // then
        assertNotNull(retorno);
        assertEquals(materia.getNome(), retorno.get(0).getNome());
    }

    @Test
    @Order(2)
    void removerMateria() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(materiaRepository.existsById(materia.getId())).thenReturn(true);

        // when
        assertDoesNotThrow(() -> materiaService.removerPorId(materia.getId(), token));

        // then
        verify(materiaRepository, times(1)).deleteById(materia.getId());
    }

    @Test
    @Order(3)
    void atualizarMateria() {
        // given
        String token = "mock-token";
        Long materiaId = 1L;
        AtualizarMateriaRequest request = new AtualizarMateriaRequest("Materia Atualizada", 1L);
        CursoEntity curso = new CursoEntity();
        curso.setId(1L);
        curso.setNome("Curso Teste");
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(materiaRepository.findById(materiaId)).thenReturn(Optional.of(materia));
        when(materiaRepository.save(any())).thenReturn(materia);

        // when
        MateriaEntity retorno = materiaService.atualizar(request, materiaId, token);

        // then
        assertNotNull(retorno);
        assertEquals(request.nome(), retorno.getNome());
    }

    @Test
    @Order(4)
    void retornarMateria() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(materiaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(materia));

        // when
        assertDoesNotThrow(() -> materiaService.buscarPorId(1L, token));
    }

    @Test
    void retornarMateriaError() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(materiaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> materiaService.buscarPorId(0L, token));
    }
}