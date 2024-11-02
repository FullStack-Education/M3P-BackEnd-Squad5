package com.senai.projetofinal.service;

import com.senai.projetofinal.controller.dto.request.InserirLoginRequest;
import com.senai.projetofinal.datasource.entity.PapelEntity;
import com.senai.projetofinal.datasource.entity.PapelEnum;
import com.senai.projetofinal.datasource.entity.UsuarioEntity;
import com.senai.projetofinal.datasource.repository.PapelRepository;
import com.senai.projetofinal.datasource.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void cadastraNovoLogin() {
        InserirLoginRequest request = new InserirLoginRequest("admin", "password", "ADMIN");
        String token = "valid-token";

        when(tokenService.buscaCampo(token, "scope")).thenReturn("admin");
        when(usuarioRepository.findByLogin(request.nomeLogin())).thenReturn(Optional.empty());
        when(papelRepository.findByNome(PapelEnum.ADMIN)).thenReturn(Optional.of(new PapelEntity()));
        when(bCryptPasswordEncoder.encode(request.senha())).thenReturn("encoded-password");

        usuarioService.cadastraNovoLogin(request, token);

        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void cadastraNovoLogin_Unauthorized() {
        InserirLoginRequest request = new InserirLoginRequest("admin", "password", "ADMIN");
        String token = "invalid-token";

        when(tokenService.buscaCampo(token, "scope")).thenReturn("user");

        assertThrows(SecurityException.class, () -> usuarioService.cadastraNovoLogin(request, token));
    }

    @Test
    void buscarUsuarioPorId() {
        Long userId = 1L;
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(userId);
        usuario.setLogin("testuser");

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        UsuarioEntity found = usuarioService.buscarUsuarioPorId(userId);

        assertNotNull(found);
        assertEquals("testuser", found.getLogin());
    }

    @Test
    void buscarUsuarioPorId_NotFound() {
        Long userId = 1L;

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.buscarUsuarioPorId(userId));
    }

    @Test
    void tokenService_BuscaCampo() {
        String token = "valid-token";
        String campo = "scope";
        String expectedValue = "admin";

        when(tokenService.buscaCampo(token, campo)).thenReturn(expectedValue);

        String result = tokenService.buscaCampo(token, campo);

        assertEquals(expectedValue, result);
    }

    @Test
    void tokenService_BuscaCampo_InvalidToken() {
        String token = "invalid-token";
        String campo = "scope";

        when(tokenService.buscaCampo(token, campo)).thenReturn(null);

        String result = tokenService.buscaCampo(token, campo);

        assertNull(result);
    }
}