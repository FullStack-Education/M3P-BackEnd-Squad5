package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.InserirLoginRequest;
import com.senai.projetofinal.controller.dto.request.docente.AtualizarDocenteRequest;
import com.senai.projetofinal.controller.dto.request.docente.InserirDocenteRequest;
import com.senai.projetofinal.controller.dto.response.docente.DocenteResponse;
import com.senai.projetofinal.datasource.entity.DocenteEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DocenteService {

    private final DocenteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public DocenteService(DocenteRepository docenteRepository, UsuarioRepository usuarioRepository, TokenService tokenService, UsuarioService usuarioService) {
        this.repository = docenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    public List<DocenteEntity> listarTodos(String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role) && !"recruiter".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        List<DocenteEntity> docentes;

        if ("pedagogico".equals(role) || "recruiter".equals(role)) {
            log.info("Todos os professores listados");
            docentes = repository.findByUsuario_Papel_Nome(PapelEnum.PROFESSOR);
            if (docentes.isEmpty()) {
                log.info("Não há professores cadastrados");
                throw new NotFoundException("Não há professores cadastrados");
            }
        } else {
            log.info("Todos os docentes listados");
            docentes = repository.findAll();
        }

        if (docentes.isEmpty()) {
            log.info("Não há docentes cadastrados");
            throw new NotFoundException("Não há docentes cadastrados");
        }

        return docentes;
    }

    public DocenteEntity buscarPorId(Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role) && !"recruiter".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }
        DocenteEntity docente = repository.findById(id).orElseThrow(() -> new NotFoundException("Docente não encontrado"));

        if ("pedagogico".equals(role) || "recruiter".equals(role)) {
            if (docente.getUsuario().getPapel().getNome() == PapelEnum.PROFESSOR) {
                log.info("Professor com id {} encontrado", id);
                return docente;
            } else {
                log.error("Usuários pedagogicos ou recruiters só podem encontrar docente que sejam profressores");
                throw new SecurityException("Usuários pedagogicos ou recruiters só podem encontrar docente que sejam profressores");
            }
        } else {
            log.info("Docente com o id {} encontrado", id);
            return docente;
        }
    }

    public DocenteResponse salvar(InserirDocenteRequest inserirDocenteRequest, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role) && !"recruiter".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Usuário não autorizado");
        }

        if (inserirDocenteRequest.nome() == null || inserirDocenteRequest.nome().isBlank()) {
            log.error("Nome não pode ser nulo ou vazioooo");
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        if (repository.existsByNome(inserirDocenteRequest.nome())) {
            log.error("Um docente já existe com o nome: {}", inserirDocenteRequest.nome());
            throw new IllegalArgumentException("Um docente já existe com o nome passado");
        }

        if (inserirDocenteRequest.email() == null || inserirDocenteRequest.email().isBlank()) {
            log.error("Email não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }

        if (repository.existsByEmail(inserirDocenteRequest.nome())) {
            log.error("Um docente já existe com o email: {}", inserirDocenteRequest.email());
            throw new IllegalArgumentException("Um docente já existe com o email passado");
        }

        if (inserirDocenteRequest.senha() == null || inserirDocenteRequest.senha().isBlank()) {
            log.error("Campo senha não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Campo senha não pode ser nulo ou vazio");
        }


        UsuarioEntity newDocenteUsuario = usuarioRepository.findById(inserirDocenteRequest.usuario())
                .orElseGet(() -> {
                            log.warn("Usuário não encontrado com o id: {}. Criando um novo usuário.", inserirDocenteRequest.usuario());


                            // Criar um request padrão para o novo login
                            InserirLoginRequest inserirLoginRequest = new InserirLoginRequest(
                                    inserirDocenteRequest.email(),
                                    inserirDocenteRequest.senha(),
                                    "professor"
                            );

                            usuarioService.cadastraNovoLogin(inserirLoginRequest, token);


                            return usuarioRepository.findByLogin(inserirDocenteRequest.email())
                                    .orElseThrow(() -> new RuntimeException("Falha ao criar novo usuário"));

                });
        String newDocentePapel = newDocenteUsuario.getPapel().getNome().toString();


        if (("pedagogico".equals(role) || "recruiter".equals(role)) && !"professor".equals(newDocentePapel)) {
            log.error("Usuário pedagogico ou recruiter só pode salvar um docente com o papel professor");
            throw new SecurityException("Usuário pedagogico ou recruiter só pode salvar um docente com o papel professor");
        }

        if (repository.existsByUsuarioId(inserirDocenteRequest.usuario())) {
            log.debug("Um docente já existe com o Id de usuário: {}", inserirDocenteRequest.usuario());
            throw new RuntimeException("Um docente já existe com o Id de usuário passado");
        }

        DocenteEntity docente = new DocenteEntity();
        UsuarioEntity user = new UsuarioEntity();
        user.setId(inserirDocenteRequest.usuario());
        docente.setUsuario(user);
        docente.setNome(inserirDocenteRequest.nome());
        docente.setDataNascimento(inserirDocenteRequest.dataNascimento());
        docente.setGenero(inserirDocenteRequest.genero());
        docente.setCpf(inserirDocenteRequest.cpf());
        docente.setRg(inserirDocenteRequest.rg());
        docente.setEstadoCivil(inserirDocenteRequest.estadoCivil());
        docente.setTelefone(inserirDocenteRequest.telefone());
        docente.setEmail(inserirDocenteRequest.email());
        docente.setSenha(inserirDocenteRequest.senha());
        docente.setNaturalidade(inserirDocenteRequest.naturalidade());
        docente.setCep(inserirDocenteRequest.cep());
        docente.setCidade(inserirDocenteRequest.cidade());
        docente.setEstado(inserirDocenteRequest.estado());
        docente.setLogradouro(inserirDocenteRequest.logradouro());
        docente.setNumero(inserirDocenteRequest.numero());
        docente.setComplemento(inserirDocenteRequest.complemento());
        docente.setBairro(inserirDocenteRequest.bairro());
        docente.setPontoReferencia(inserirDocenteRequest.pontoReferencia());
        docente.setMaterias(inserirDocenteRequest.materias());


        DocenteEntity docenteSalvo = repository.save(docente);

        log.info("Salvando docente com o nome {}", inserirDocenteRequest.nome());
        return new DocenteResponse(
                docenteSalvo.getId(),
                docenteSalvo.getNome(),
                docenteSalvo.getDataNascimento(),
                docenteSalvo.getGenero(),
                docenteSalvo.getCpf(),
                docenteSalvo.getRg(),
                docenteSalvo.getEstadoCivil(),
                docenteSalvo.getTelefone(),
                docenteSalvo.getEmail(),
                docenteSalvo.getSenha(),
                docenteSalvo.getNaturalidade(),
                docenteSalvo.getCep(),
                docenteSalvo.getCidade(),
                docenteSalvo.getEstado(),
                docenteSalvo.getLogradouro(),
                docenteSalvo.getNumero(),
                docenteSalvo.getComplemento(),
                docenteSalvo.getBairro(),
                docenteSalvo.getPontoReferencia(),
                docenteSalvo.getMaterias(),
                docenteSalvo.getUsuario()
        );
    }

    public void removerPorId(Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");
        if (!"admin".equals(role)) {
            throw new SecurityException("Apenas um usuário admin pode deletar docentes");
        }

        if (!repository.existsById(id)) {
            throw new NotFoundException("Nenhum docente encontrado com o id passado");
        }

        log.info("Removendo docente com o id {}", id);
        repository.deleteById(id);
    }

    public DocenteEntity atualizar(AtualizarDocenteRequest atualizarDocenteRequest, Long id, String token) {
        String role = tokenService.buscaCampo(token, "scope");

        if (!"admin".equals(role) && !"pedagogico".equals(role) && !"recruiter".equals(role)) {
            log.error("Usuário não autorizado: {}", role);
            throw new SecurityException("Tentativa de atualizar não autorizada");
        }

        DocenteEntity entity = buscarPorId(id, token);

        if (atualizarDocenteRequest.nome() == null || atualizarDocenteRequest.nome().isBlank()) {
            log.error("Nome não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }

        if (atualizarDocenteRequest.email() == null || atualizarDocenteRequest.email().isBlank()) {
            log.error("Email não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }

        if (repository.existsByEmail(atualizarDocenteRequest.nome())) {
            log.error("Um docente já existe com o email: {}", atualizarDocenteRequest.email());
            throw new IllegalArgumentException("Um docente já existe com o email passado");
        }

        if (atualizarDocenteRequest.senha() == null || atualizarDocenteRequest.senha().isBlank()) {
            log.error("Campo senha não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Campo senha não pode ser nulo ou vazio");
        }



        log.info("Atualizando docente com o id {}", entity.getId());
        entity.setNome(atualizarDocenteRequest.nome());
        entity.setDataNascimento(atualizarDocenteRequest.dataNascimento());
        entity.setGenero(atualizarDocenteRequest.genero());
        entity.setCpf(atualizarDocenteRequest.cpf());
        entity.setRg(atualizarDocenteRequest.rg());
        entity.setEstadoCivil(atualizarDocenteRequest.estadoCivil());
        entity.setTelefone(atualizarDocenteRequest.telefone());
        entity.setEmail(atualizarDocenteRequest.email());
        entity.setSenha(atualizarDocenteRequest.senha());
        entity.setNaturalidade(atualizarDocenteRequest.naturalidade());
        entity.setCep(atualizarDocenteRequest.cep());
        entity.setCidade(atualizarDocenteRequest.cidade());
        entity.setEstado(atualizarDocenteRequest.estado());
        entity.setLogradouro(atualizarDocenteRequest.logradouro());
        entity.setNumero(atualizarDocenteRequest.numero());
        entity.setComplemento(atualizarDocenteRequest.complemento());
        entity.setBairro(atualizarDocenteRequest.bairro());
        entity.setPontoReferencia(atualizarDocenteRequest.pontoReferencia());
        entity.setMaterias(atualizarDocenteRequest.materias());
        return repository.save(entity);
    }
}
