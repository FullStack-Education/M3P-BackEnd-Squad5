package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.turma.AtualizarTurmaRequest;
import com.senai.projetofinal.controller.dto.request.turma.InserirTurmaRequest;
import com.senai.projetofinal.controller.dto.response.turma.TurmaResponse;
import com.senai.projetofinal.datasource.entity.CursoEntity;
import com.senai.projetofinal.datasource.entity.DocenteEntity;
import com.senai.projetofinal.datasource.entity.TurmaEntity;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.CursoRepository;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j


public class TurmaService {

    private final TurmaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final CursoRepository cursoRepository;
    private final TokenService tokenService;

    public TurmaService(TurmaRepository repository, UsuarioRepository usuarioRepository, DocenteRepository docenteRepository, CursoRepository cursoRepository, TokenService tokenService) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.docenteRepository = docenteRepository;
        this.cursoRepository = cursoRepository;
        this.tokenService = tokenService;
    }

    public List<TurmaEntity> listarTodos(String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        List<TurmaEntity> turmas = repository.findAll();

        if (turmas.isEmpty()) {
            log.info("Nenhuma turma encontrada");
            throw new NotFoundException("Nenhuma turma encontrada");
        }

        log.info("Todas as turmas listadas");
        return turmas;
    }

    public TurmaEntity buscarPorId(Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        return repository.findById(id).orElseThrow(() -> {
            log.error("Turma não encontrada");
            return new NotFoundException("Turma não encontrada");
        });
    }

    public TurmaResponse salvar(InserirTurmaRequest inserirTurmaRequest, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role) && !"professor".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        validateRequestFields(inserirTurmaRequest);

        CursoEntity curso = cursoRepository.findById(inserirTurmaRequest.curso())
                .orElseThrow(() -> {
                    log.error("Curso não encontrado");
                    return new NotFoundException("Curso não encontrado");
                });

        DocenteEntity docente = docenteRepository.findById(inserirTurmaRequest.docente())
                .orElseThrow(() -> {
                    log.error("Docente não encontrado");
                    return new NotFoundException("Docente não encontrado");
                });

        validateDocenteRole(docente);

        TurmaEntity turma = new TurmaEntity();
        turma.setNome(inserirTurmaRequest.nome());
        turma.setDataInicio(inserirTurmaRequest.dataInicio());
        turma.setDataTermino(inserirTurmaRequest.dataTermino());
        turma.setHorario(inserirTurmaRequest.horario());
        turma.setDocente(docente);
        turma.setCurso(curso);

        TurmaEntity turmaSalva = repository.save(turma);
        log.info("Turma salva com o nome {}", turmaSalva.getNome());

        return new TurmaResponse(
                turmaSalva.getId(),
                turmaSalva.getNome(),
//                turmaSalva.getDataInicio(),
//                turmaSalva.getDataTermino(),
//                turmaSalva.getHorario(),
                turmaSalva.getDocente(),
                turmaSalva.getCurso()
        );
    }

    public void removerPorid(Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role)) {
            log.error("Apenas um admin pode remover uma turma");
            throw new SecurityException("Apenas um admin pode remover uma turma");
        }

        if (!repository.existsById(id)) {
            log.error("Nenhuma turma encontrada com o id: {}", id);
            throw new NotFoundException("Nenhuma turma encontrada com o id passado");
        }

        log.info("Removendo turma com o id {}", id);
        repository.deleteById(id);
    }


    public TurmaEntity atualizar(AtualizarTurmaRequest atualizarTurmaRequest, Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        TurmaEntity entity = repository.findById(id).orElseThrow(() -> {
            log.error("Nenhuma turma encontrada com o id: {}", id);
            return new NotFoundException("Nenhuma turma encontrada com o id passado");
        });


        CursoEntity curso = cursoRepository.findById(atualizarTurmaRequest.curso())
                .orElseThrow(() -> {
                    log.error("Curso não encontrado");
                    return new NotFoundException("Curso não encontrado");
                });

        DocenteEntity docente = docenteRepository.findById(atualizarTurmaRequest.docente())
                .orElseThrow(() -> {
                    log.error("Docente não encontrado");
                    return new NotFoundException("Docente não encontrado");
                });

        validateDocenteRole(docente);

        entity.setNome(atualizarTurmaRequest.nome());
        entity.setDataInicio(atualizarTurmaRequest.dataInicio());
        entity.setDataTermino(atualizarTurmaRequest.dataTermino());
        entity.setHorario(atualizarTurmaRequest.horario());
        entity.setDocente(docente);
        entity.setCurso(curso);
        log.info("Turma atualizada com o id {}", entity.getId());

        return repository.save(entity);
    }

    private void validateRequestFields(InserirTurmaRequest request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        if (repository.existsByNome(request.nome())) {
            throw new IllegalArgumentException("Uma turma já existe com o nome passado");
        }
    }

    private void validateDocenteRole(DocenteEntity docente) {
        String docenteRole = docente.getUsuario().getPapel().getNome().toString();
        if (!"professor".equalsIgnoreCase(docenteRole)) {
            throw new IllegalArgumentException("Apenas um docente com papel professor pode ser atribuído a uma turma");
        }
    }
}