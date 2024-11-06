package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.aluno.AtualizarAlunoRequest;
import com.senai.projetofinal.controller.dto.request.aluno.InserirAlunoRequest;
import com.senai.projetofinal.controller.dto.response.AlunoResponse;
import com.senai.projetofinal.datasource.entity.AlunoEntity;
import com.senai.projetofinal.datasource.entity.PapelEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.entity.TurmaEntity;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.AlunoRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlunoServiceTest {

    @Mock
    AlunoRepository alunoRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    TurmaRepository turmaRepository;

    @Mock
    TokenService tokenService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    AlunoService alunoService;

    static AlunoEntity aluno;

    @BeforeAll
    public static void setUp() {
        aluno = new AlunoEntity();
        aluno.setId(1L);
        aluno.setNome("Aluno Teste");
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setLogin("aluno");
        PapelEntity papel = new PapelEntity();
        papel.setNome(PapelEnum.ALUNO);
        usuario.setPapel(papel);
        aluno.setUsuario(usuario);
    }

    @Test
    @Order(1)
    void salvarAluno() {
        // given
        String token = "mock-token";
        InserirAlunoRequest request = new InserirAlunoRequest("Aluno Teste", "2000-01-01", "senha teste","","","","","","","","","","","","","","","",1L, 1L);
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(new TurmaEntity()));
        when(alunoRepository.save(any())).thenReturn(aluno);
        when(bCryptPasswordEncoder.encode(any(CharSequence.class))).thenReturn("encoded-password");
        when(usuarioService.cadastraNovoLogin(any(), any())).thenReturn(new UsuarioEntity());

        // when
        AlunoResponse retorno = alunoService.salvar(request, token);

        // then
        assertNotNull(retorno);
        assertEquals(aluno.getNome(), retorno.nome());

        verify(alunoRepository, times(1)).save(any());
    }

    @Test
    void listarTodosAlunos() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findAll()).thenReturn(List.of(aluno));

        // when
        List<AlunoEntity> retorno = alunoService.listarTodos(token);

        // then
        assertNotNull(retorno);
        assertEquals(aluno.getNome(), retorno.get(0).getNome());
    }

    @Test
    @Order(2)
    void removerAluno() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.existsById(aluno.getId())).thenReturn(true);
        when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));

        // when
        assertDoesNotThrow(() -> alunoService.removerPorId(aluno.getId(), token));

        // then
        verify(alunoRepository, times(1)).deleteById(aluno.getId());
    }

    @Test
    @Order(3)
    void atualizarAluno() {
        // given
        String token = "mock-token";
        Long alunoId = 1L;
        AtualizarAlunoRequest request = new AtualizarAlunoRequest("Aluno Atualizado", "2000-01-01", "","","","","","","","","","","","","","","","",1L);
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(aluno));
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(new TurmaEntity()));
        when(alunoRepository.save(any())).thenReturn(aluno);

        // when
        AlunoEntity retorno = alunoService.atualizar(request, alunoId, token);

        // then
        assertNotNull(retorno);
        assertEquals(request.nome(), retorno.getNome());
    }

    @Test
    @Order(4)
    void retornarAluno() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(aluno));

        // when
        assertDoesNotThrow(() -> alunoService.buscarPorId(1L, token));
    }

    @Test
    void retornarAlunoError() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> alunoService.buscarPorId(0L, token));
    }
}