package br.com.test.cambio.jim.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.test.cambio.jim.modelo.Cliente;

@Repository
public interface ClienteDAO extends CrudRepository<Cliente,Integer >{
    
Iterable<Cliente> findByNomeStartingWith(String nome);
    
    Iterable<Cliente> findByNomeContaining(String nome);
    
}
