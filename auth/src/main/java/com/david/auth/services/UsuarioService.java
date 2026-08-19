package com.david.auth.services;

import java.util.Set;

import com.david.auth.dto.UsuarioRequest;
import com.david.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
