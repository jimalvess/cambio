package br.com.test.cambio.jim.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.test.cambio.jim.modelo.Usuario;


public interface UsuarioDAO extends 
        PagingAndSortingRepository<Usuario, Integer>{
    public Usuario findByLogin(String login);
    
}
