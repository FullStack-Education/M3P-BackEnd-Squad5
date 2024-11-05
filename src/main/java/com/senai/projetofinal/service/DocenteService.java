package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.InserirLoginRequest;
import com.senai.projetofinal.controller.dto.request.docente.AtualizarDocenteRequest;
import com.senai.projetofinal.controller.dto.request.docente.InserirDocenteRequest;
import com.senai.projetofinal.controller.dto.response.docente.DocenteResponse;
import com.senai.projetofinal.datasource.entity.AlunoEntity;
import com.senai.projetofinal.datasource.entity.DocenteEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.DocenteRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
import com.senai.projetofinal.infra.exception.error.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
@Slf4j
public class DocenteService {

    private final DocenteRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;


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
        DocenteEntity docente = repository.findById(id).orElseThrow(() -> new NotFoundException("Nenhum docente com o ID passado foi encontrado"));

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

        if (repository.existsByEmail(inserirDocenteRequest.email())) {
            log.error("Um docente já existe com o email: {}", inserirDocenteRequest.email());
            throw new IllegalArgumentException("Um docente já existe com o email passado");
        }

        if (inserirDocenteRequest.senha() == null || inserirDocenteRequest.senha().isBlank()) {
            log.error("Campo senha não pode ser nulo ou vazio");
            throw new IllegalArgumentException("Campo senha não pode ser nulo ou vazio");
        }


        log.info("Criando um novo usuário.");

        UsuarioEntity user = usuarioService.cadastraNovoLogin(new InserirLoginRequest(
                inserirDocenteRequest.email(),
                inserirDocenteRequest.senha(),
                "professor"
        ), token);


        if (("pedagogico".equals(role) || "recruiter".equals(role))) {
            log.error("Usuário pedagogico ou recruiter só pode salvar um docente com o papel professor");
            throw new SecurityException("Usuário pedagogico ou recruiter só pode salvar um docente com o papel professor");
        }


        DocenteEntity docente = new DocenteEntity();
        docente.setUsuario(user);
        docente.setNome(inserirDocenteRequest.nome());
        docente.setDataNascimento(inserirDocenteRequest.dataNascimento());
        docente.setGenero(inserirDocenteRequest.genero());
        docente.setCpf(inserirDocenteRequest.cpf());
        docente.setRg(inserirDocenteRequest.rg());
        docente.setEstadoCivil(inserirDocenteRequest.estadoCivil());
        docente.setTelefone(inserirDocenteRequest.telefone());
        docente.setEmail(inserirDocenteRequest.email());
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

        DocenteEntity docente = buscarPorId(id, token);
        UsuarioEntity user = docente.getUsuario();


        log.info("Removendo docente com o id {}", id);
        repository.deleteById(id);
        log.info("Removendo usuario vinculado ao docente com o id {}", id);
        usuarioRepository.deleteById(user.getId());
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

        DocenteEntity docente = buscarPorId(id, token);
        UsuarioEntity user = docente.getUsuario();

        user.setLogin(atualizarDocenteRequest.email());
        user.setSenha(bCryptPasswordEncoder.encode(atualizarDocenteRequest.senha()));
        usuarioRepository.save(user);


        return repository.save(entity);
    }
}
