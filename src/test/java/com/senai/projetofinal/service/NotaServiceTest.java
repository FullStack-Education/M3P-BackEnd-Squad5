package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.nota.InserirNotaRequest;
import com.senai.projetofinal.controller.dto.request.nota.AtualizarNotaRequest;
import com.senai.projetofinal.controller.dto.response.NotaResponse;
import com.senai.projetofinal.datasource.entity.*;
import com.senai.projetofinal.datasource.repository.AlunoRepository;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.MateriaRepository;
import com.senai.projetofinal.datasource.repository.NotaRepository;
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
class NotaServiceTest {

    @Mock
    NotaRepository notaRepository;

    @Mock
    AlunoRepository alunoRepository;

    @Mock
    DocenteRepository docenteRepository;

    @Mock
    MateriaRepository materiaRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    NotaService notaService;

    static NotaEntity nota;

    @BeforeAll
    public static void setUp() {
        nota = new NotaEntity();
        nota.setId(1L);
        nota.setValor("8.5");
    }

    @Test
    @Order(1)
    void salvarNota() {
        // given
        String token = "mock-token";
        InserirNotaRequest request = new InserirNotaRequest(1L, 1L, 1L, "8.5");

        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome("Aluno Teste");
        TurmaEntity turma = new TurmaEntity();
        CursoEntity curso = new CursoEntity();
        MateriaEntity materia = new MateriaEntity();
        materia.setId(1L);
        curso.setId(1L);
        curso.setNome("Curso Teste");
        curso.setMaterias(List.of(materia));
        turma.setCurso(curso);
        aluno.setTurma(turma);

        DocenteEntity docente = new DocenteEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        PapelEntity papel = new PapelEntity();
        papel.setNome(PapelEnum.PROFESSOR);
        usuario.setPapel(papel);
        docente.setUsuario(usuario);

        NotaEntity nota = new NotaEntity();
        nota.setAluno(aluno);
        nota.setDocente(docente);
        nota.setMateria(materia);
        nota.setValor("8.5");

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(docente));
        when(materiaRepository.findById(1L)).thenReturn(Optional.of(materia));
        when(notaRepository.save(any())).thenReturn(nota);

        // when
        NotaResponse retorno = notaService.salvar(request, token);

        // then
        assertNotNull(retorno);
        assertEquals(nota.getValor(), retorno.valor());

        verify(notaRepository, times(1)).save(any());
    }

    @Test
    void listarTodasNotas() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(notaRepository.findAll()).thenReturn(List.of(nota));

        // when
        List<NotaEntity> retorno = notaService.listarTodos(token);

        // then
        assertNotNull(retorno);
        assertEquals(nota.getValor(), retorno.get(0).getValor());
    }

    @Test
    @Order(2)
    void removerNota() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(notaRepository.existsById(nota.getId())).thenReturn(true);

        // when
        assertDoesNotThrow(() -> notaService.removerPorId(nota.getId(), token));

        // then
        verify(notaRepository, times(1)).deleteById(nota.getId());
    }

    @Test
    @Order(3)
    void atualizarNota() {
        // given
        String token = "mock-token";
        Long notaId = 1L;
        AtualizarNotaRequest request = new AtualizarNotaRequest("9.0");
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(notaRepository.findById(notaId)).thenReturn(Optional.of(nota));
        when(notaRepository.save(any())).thenReturn(nota);

        // when
        NotaEntity retorno = notaService.atualizar(request, notaId, token);

        // then
        assertNotNull(retorno);
        assertEquals(request.valor(), retorno.getValor());
    }

    @Test
    @Order(4)
    void retornarNota() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(notaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(nota));

        // when
        assertDoesNotThrow(() -> notaService.buscarPorId(1L, token));
    }

    @Test
    void retornarNotaError() {
        // given
        String token = "mock-token";
        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(notaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> notaService.buscarPorId(0L, token));
    }
}