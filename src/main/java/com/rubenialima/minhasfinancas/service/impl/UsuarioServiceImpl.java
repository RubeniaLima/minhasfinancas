package com.rubenialima.minhasfinancas.service.impl;

import com.rubenialima.minhasfinancas.model.entity.Usuario;
import com.rubenialima.minhasfinancas.model.repository.UsuarioRepository;
import com.rubenialima.minhasfinancas.service.UsuarioService;
import com.rubenialima.minhasfinancas.service.exception.ErroAutenticacao;
import com.rubenialima.minhasfinancas.service.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository){
        super();
        this.repository =repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()){
            throw new ErroAutenticacao("Usuario não encontrado para o e-mail informado.");
        }

        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha inválida.");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if(existe){
            throw new RegraNegocioException("Já existe  um usuário cadastrado com este e-mail.");
        }

    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }
}
