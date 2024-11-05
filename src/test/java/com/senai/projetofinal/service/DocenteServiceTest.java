package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.docente.AtualizarDocenteRequest;
import com.senai.projetofinal.controller.dto.request.docente.InserirDocenteRequest;
import com.senai.projetofinal.controller.dto.response.DocenteResponse;
import com.senai.projetofinal.datasource.entity.DocenteEntity;
import com.senai.projetofinal.datasource.entity.PapelEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DocenteServiceTest {

    @Mock
    DocenteRepository repository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    DocenteService service;

    static DocenteEntity docente;

    @BeforeAll
    public static void setUp() {
        docente = new DocenteEntity();
        docente.setId(1L);
        docente.setNome("Docente Teste");
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setLogin("docente");
        PapelEntity papel = new PapelEntity();
        papel.setNome(PapelEnum.PROFESSOR);
        usuario.setPapel(papel);
        docente.setUsuario(usuario);
    }

    @Test
    @Order(1)
    void salvarDocente() {
        String token = "mock-token";
        InserirDocenteRequest request = new InserirDocenteRequest(
                "Docente Teste",
                LocalDate.now(),
                "Genero Teste",
                "12345678900",
                "1234567",
                "Estado Civil Teste",
                "999995555",
                "Email Teste",
                "Senha Teste",
                "Naturalidade Teste",
                "12345-678",
                "Cidade Teste",
                "Estado Teste",
                "Logradouro Teste",
                "123456789",
                "Complemento Teste",
                "Bairo Teste",
                "Ponto de Referência Teste",
                List.of("1L"),
                1L
        );
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(docente.getUsuario()));
        when(repository.save(any())).thenReturn(docente);

        DocenteResponse retorno = service.salvar(request, token);

        assertNotNull(retorno);
        assertEquals(docente.getNome(), retorno.nome());

        verify(repository, times(1)).save(any());
    }

    @Test
    void listarTodosDocentes() {
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findAll()).thenReturn(List.of(docente));

        List<DocenteEntity> retorno = service.listarTodos(token);

        assertNotNull(retorno);
        assertEquals(docente.getNome(), retorno.get(0).getNome());
    }

    @Test
    @Order(2)
    void removerDocente() {
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.existsById(docente.getId())).thenReturn(true);

        assertDoesNotThrow(() -> service.removerPorId(docente.getId(), token));

        verify(repository, times(1)).deleteById(docente.getId());
    }

    @Test
    @Order(4)
    void retornarDocente() {
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(docente));

        assertDoesNotThrow(() -> service.buscarPorId(1L, token));
    }

    @Test
    void retornarDocenteError() {
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(0L, token));
    }

    @Test
    @Order(3)
    void atualizarDocente() {
        String token = "mock-token";
        Long docenteId = 1L;
        AtualizarDocenteRequest request = new AtualizarDocenteRequest(
                "Docente Atualizado",
                LocalDate.now(),
                "Genero Atualizado",
                "12345678900",
                "1234567",
                "Estado Civil Atualizado",
                "999995555",
                "Email Atualizado",
                "Senha Atualizada",
                "Naturalidade Atualizada",
                "12345-678",
                "Cidade Atualizada",
                "Estado Atualizado",
                "Logradouro Atualizado",
                "123456789",
                "Complemento Atualizado",
                "Bairro Atualizado",
                "Ponto de Referência Atualizado",
                List.of("1L")
        );

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(repository.findById(docenteId)).thenReturn(Optional.of(docente));
        when(repository.save(any())).thenReturn(docente);

        DocenteEntity retorno = service.atualizar(request, docenteId, token);

        assertNotNull(retorno);
        assertEquals(request.nome(), retorno.getNome());
        assertEquals(request.email(), retorno.getEmail());

        verify(repository, times(1)).save(any());
    }
}