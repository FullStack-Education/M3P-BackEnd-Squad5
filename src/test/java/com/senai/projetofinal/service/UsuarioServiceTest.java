package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.InserirLoginRequest;
import com.senai.projetofinal.datasource.entity.PapelEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.PapelRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioServiceTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PapelRepository papelRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UsuarioService usuarioService;

    static UsuarioEntity usuario;

    @BeforeAll
    public static void setUp() {
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setLogin("admin");
        usuario.setSenha("password");
        PapelEntity papel = new PapelEntity();
        papel.setNome(PapelEnum.ADMIN);
        usuario.setPapel(papel);
    }

    @Test
    @Order(1)
    void cadastraNovoLogin() {
        String token = "valid-token";

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(usuarioRepository.findByLogin(usuario.getLogin())).thenReturn(Optional.empty());
        when(papelRepository.findByNome(PapelEnum.ADMIN)).thenReturn(Optional.of(usuario.getPapel()));
        when(bCryptPasswordEncoder.encode(usuario.getSenha())).thenReturn("encoded-password");

        usuarioService.cadastraNovoLogin(new InserirLoginRequest(usuario.getLogin(), usuario.getSenha(), usuario.getPapel().getNome().name()), token);

        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void cadastraNovoLogin_Unauthorized() {
        String token = "invalid-token";

        when(tokenService.buscaCampo(token, "scope")).thenReturn("user");

        assertThrows(SecurityException.class, () -> usuarioService.cadastraNovoLogin(new InserirLoginRequest(usuario.getLogin(), usuario.getSenha(), usuario.getPapel().getNome().name()), token));
    }

    @Test
    void buscarUsuarioPorId() {
        Long userId = 1L;

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        UsuarioEntity found = usuarioService.buscarUsuarioPorId(userId);

        assertNotNull(found);
        assertEquals("admin", found.getLogin());
    }

    @Test
    void buscarUsuarioPorId_NotFound() {
        Long userId = 1L;

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.buscarUsuarioPorId(userId));
    }
}