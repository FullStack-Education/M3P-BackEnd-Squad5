package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.InserirLoginRequest;
import com.senai.projetofinal.controller.dto.request.aluno.AtualizarAlunoRequest;
import com.senai.projetofinal.controller.dto.request.aluno.InserirAlunoRequest;
import com.senai.projetofinal.controller.dto.response.aluno.AlunoResponse;
import com.senai.projetofinal.datasource.entity.AlunoEntity;
import com.senai.projetofinal.datasource.entity.MateriaEntity;
import com.senai.projetofinal.datasource.entity.TurmaEntity;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.AlunoRepository;
import com.senai.projetofinal.datasource.repository.MateriaRepository;
import com.senai.projetofinal.datasource.repository.TurmaRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AlunoService {

    private final AlunoRepository repository;

    private final UsuarioRepository usuarioRepository;

    private final TurmaRepository turmaRepository;

    private final TokenService tokenService;
    private final MateriaRepository materiaRepository;
    private final UsuarioService usuarioService;

    public AlunoService(AlunoRepository repository, UsuarioRepository usuarioRepository, TurmaRepository turmaRepository, TokenService tokenService, MateriaRepository materiaRepository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.turmaRepository = turmaRepository;
        this.tokenService = tokenService;
        this.materiaRepository = materiaRepository;
        this.usuarioService = usuarioService;
    }

    public List<AlunoEntity> listarTodos(String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        List<AlunoEntity> alunos = repository.findAll();

        if (alunos.isEmpty()) {
            log.info("Nenhum aluno encontrado");
            throw new NotFoundException("Nenhum aluno encontrado");
        }

        log.info("Todos os alunos listados");
        return repository.findAll();
    }

    public AlunoEntity buscarPorId(Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        log.info("Aluno com id {} encontrado", id);
        return repository.findById(id).orElseThrow(() -> {
            log.error("Aluno não encontrado");
            return new NotFoundException("Aluno não encontrado");
        });
    }

    public AlunoResponse salvar(InserirAlunoRequest inserirAlunoRequest, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        if (inserirAlunoRequest.nome() == null || inserirAlunoRequest.nome().isBlank()) {
            log.error("Nome não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        if (repository.existsByNome(inserirAlunoRequest.nome())) {
            log.error("Um aluno já existe com o nome: {}", inserirAlunoRequest.nome());
            throw new IllegalArgumentException("Um aluno já existe com o nome passado");
        }

        TurmaEntity turma = turmaRepository.findById(inserirAlunoRequest.turma())
                .orElseThrow(() -> {
                    log.error("Turma não encontrada");
                    return new NotFoundException("Turma não encontrada");
                });

        AlunoEntity aluno = new AlunoEntity();

        aluno.setNome(inserirAlunoRequest.nome());
        aluno.setEmail(inserirAlunoRequest.email());
        aluno.setSenha(inserirAlunoRequest.senha());
        aluno.setDataNascimento(inserirAlunoRequest.dataNascimento());
        aluno.setGenero(inserirAlunoRequest.genero());
        aluno.setCpf(inserirAlunoRequest.cpf());
        aluno.setRg(inserirAlunoRequest.rg());
        aluno.setEstadoCivil(inserirAlunoRequest.estadoCivil());
        aluno.setTelefone(inserirAlunoRequest.telefone());
        aluno.setNaturalidade(inserirAlunoRequest.naturalidade());
        aluno.setCep(inserirAlunoRequest.cep());
        aluno.setLogradouro(inserirAlunoRequest.logradouro());
        aluno.setNumero(inserirAlunoRequest.numero());
        aluno.setComplemento(inserirAlunoRequest.complemento());
        aluno.setBairro(inserirAlunoRequest.bairro());
        aluno.setPontoReferencia(inserirAlunoRequest.pontoReferencia());
        aluno.setTurma(turma);

        UsuarioEntity user =  usuarioService.cadastraNovoLogin(new InserirLoginRequest(
                inserirAlunoRequest.email(),
                inserirAlunoRequest.senha(),
                "Aluno"
        ), token);

        aluno.setUsuario(user);

        AlunoEntity alunoSalvo = repository.save(aluno);

        log.info("Salvando aluno com o nome {}", inserirAlunoRequest.nome());

        return new AlunoResponse(
                alunoSalvo.getId(),
                alunoSalvo.getNome(),
                alunoSalvo.getDataNascimento(),
                alunoSalvo.getUsuario(),
                alunoSalvo.getTurma());
    }

    public void removerPorId(Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role)) {
            log.error("Apenas um admin pode remover um aluno");
            throw new SecurityException("Apenas um admin pode remover um aluno");
        }

        if (!repository.existsById(id)) {
            log.error("Nenhum aluno encontrado com o id passado");
            throw new NotFoundException("Nenhum aluno encontrado com o id passado");
        }

        log.info("Removendo aluno com id {}", id);
        repository.deleteById(id);
    }

    public AlunoEntity atualizar(AtualizarAlunoRequest atualizarAlunoRequest, Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        AlunoEntity entity = buscarPorId(id, token);

        if (atualizarAlunoRequest.nome() == null || atualizarAlunoRequest.nome().isBlank()) {
            log.error("Nome não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        if (repository.existsByNome(atualizarAlunoRequest.nome())) {
            log.error("Um aluno já existe com o nome: {}", atualizarAlunoRequest.nome());
            throw new IllegalArgumentException("Um aluno já existe com o nome passado");
        }

        TurmaEntity turma = turmaRepository.findById(atualizarAlunoRequest.turma())
                .orElseThrow(() -> {
                    log.error("Turma não encontrada");
                    return new NotFoundException("Turma não encontrada");
                });

        log.info("Atualizando aluno com o id {}", id);
        entity.setNome(atualizarAlunoRequest.nome());
        entity.setDataNascimento(atualizarAlunoRequest.dataNascimento());
        entity.setTurma(turma);
        return repository.save(entity);
    }
}
