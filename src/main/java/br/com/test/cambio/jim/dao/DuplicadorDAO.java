package br.com.test.cambio.jim.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.test.cambio.jim.modelo.Duplicador;

@Repository
public interface DuplicadorDAO extends CrudRepository <Duplicador, Integer> {
	
Iterable<Duplicador> findByNomeStartingWith(String nome);
    
    Iterable<Duplicador> findByNomeContaining(String nome);
    
}