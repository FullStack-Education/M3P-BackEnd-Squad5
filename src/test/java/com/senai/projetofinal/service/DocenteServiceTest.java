package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.docente.AtualizarDocenteRequest;
import com.senai.projetofinal.datasource.entity.DocenteEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private DocenteService docenteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_Admin() {
        String token = "mock-token";
        List<DocenteEntity> docentes = List.of(new DocenteEntity(), new DocenteEntity());

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(docenteRepository.findAll()).thenReturn(docentes);

        List<DocenteEntity> result = docenteService.listarTodos(token);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(docenteRepository, times(1)).findAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"pedagogico", "recruiter"})
    void listarTodos_PedagogicoOuRecruiter(String role) {
        String token = "mock-token";
        List<DocenteEntity> docentes = List.of(new DocenteEntity(), new DocenteEntity());

        when(tokenService.buscaCampo(token, "scope")).thenReturn(role);
        when(docenteRepository.findByUsuario_Papel_Nome(PapelEnum.PROFESSOR)).thenReturn(docentes);

        List<DocenteEntity> result = docenteService.listarTodos(token);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(docenteRepository, times(1)).findByUsuario_Papel_Nome(PapelEnum.PROFESSOR);
    }

    @Test
    void listarTodos_Unauthorized() {
        String token = "mock-token";

        when(tokenService.buscaCampo(token, "scope")).thenReturn("unauthorized");

        assertThrows(SecurityException.class, () -> docenteService.listarTodos(token));
        verify(docenteRepository, never()).findAll();
    }

    @Test
    void buscarDocentePorId() {
        Long docenteId = 1L;
        String token = "mock-token";
        DocenteEntity docente = new DocenteEntity();
        docente.setId(docenteId);
        docente.setNome("testdocente");

        when(docenteRepository.findById(docenteId)).thenReturn(Optional.of(docente));
        when(tokenService.buscaCampo(anyString(), eq("scope"))).thenReturn("admin");

        DocenteEntity found = docenteService.buscarPorId(docenteId, token);

        assertNotNull(found);
        assertEquals("testdocente", found.getNome());
    }

    @Test
    void buscarDocentePorId_NotFound() {
        Long docenteId = 1L;
        String token = "mock-token";

        when(docenteRepository.findById(docenteId)).thenReturn(Optional.empty());
        when(tokenService.buscaCampo(anyString(), eq("scope"))).thenReturn("admin");

        assertThrows(RuntimeException.class, () -> docenteService.buscarPorId(docenteId, token));
    }

    @Test
    void atualizarDocente() {
        Long docenteId = 1L;
        String token = "mock-token";
        DocenteEntity docente = new DocenteEntity();
        docente.setId(docenteId);
        docente.setNome("nomeAntigo");

        AtualizarDocenteRequest atualizarDocenteRequest = new AtualizarDocenteRequest(
                "nomeNovo", LocalDate.of(1990, 1, 1), "novogenero", "novocpf", "novorg",
                "novoestadoCivil", "novotelefone", "novoemail@exemplo.com", "novasenha",
                "novanaturalidade", "novocep", "novacidade", "novoestado", "novologradouro",
                "novonumero", "novocomplemento", "novobairro", "novopontoReferencia", null
        );

        when(docenteRepository.findById(docenteId)).thenReturn(Optional.of(docente));
        when(tokenService.buscaCampo(anyString(), eq("scope"))).thenReturn("admin");
        when(docenteRepository.save(any(DocenteEntity.class))).thenReturn(docente);

        DocenteEntity atualizado = docenteService.atualizar(atualizarDocenteRequest, docenteId, token);

        assertNotNull(atualizado);
        assertEquals("nomeNovo", atualizado.getNome());
        assertEquals("novoemail@exemplo.com", atualizado.getEmail());
        assertEquals("novasenha", atualizado.getSenha());
    }
}