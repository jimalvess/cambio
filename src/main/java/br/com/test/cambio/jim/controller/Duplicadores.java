//LOCALHOST:8080/api/duplicadores/
package br.com.test.cambio.jim.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.test.cambio.jim.dao.ClienteDAO;
import br.com.test.cambio.jim.dao.DuplicadorDAO;
import br.com.test.cambio.jim.erros.NaoEncontrado;
import br.com.test.cambio.jim.erros.RequisicaoInvalida;
import br.com.test.cambio.jim.modelo.Cliente;
import br.com.test.cambio.jim.modelo.Duplicador;

@RestController
@RequestMapping(path = "/api")
public class Duplicadores {

	@Autowired
	DuplicadorDAO duplicadorDAO;
	@Autowired
	ClienteDAO clienteDAO;

///////////// VALIDAÇÕES ////////////////////////       

	public void validaDuplicador(Duplicador duplicador) {
		if (duplicador.getNome() == null || duplicador.getNome().isEmpty()) {
			throw new RequisicaoInvalida("NOME é obrigatório");
		}
		if (duplicador.getCelular() == null || duplicador.getCelular().isEmpty()) {
			throw new RequisicaoInvalida("CELULAR é obrigatório");
		}
		if (duplicador.getEmail() == null || duplicador.getEmail().isEmpty()) {
			throw new RequisicaoInvalida("E-MAIL é obrigatório");
		}
		if (duplicador.getPix() == null || duplicador.getPix().isEmpty()) {
			throw new RequisicaoInvalida("PIX é obrigatório");
		}
		if (duplicador.getLogin() == null || duplicador.getLogin().isEmpty()) {
			throw new RequisicaoInvalida("LOGIN é obrigatório");
		}
		if (duplicador.getSenha() == null || duplicador.getSenha().isEmpty()) {
			throw new RequisicaoInvalida("SENHA é obrigatória");
		}

	}

/////////////////// PESQUISAR DUPLICADOR POR NOME ///////////////////

	@RequestMapping(path = "/duplicadores/pesquisar/nome/", method = RequestMethod.GET)
	public Iterable<Duplicador> pesquisaPorNome(@RequestParam(required = false) String inicia,
			@RequestParam(required = false) String contem) {
		if (inicia != null) {
			return duplicadorDAO.findByNomeStartingWith(inicia);
		}
		if (contem != null) {
			return duplicadorDAO.findByNomeContaining(contem);
		}
		throw new RequisicaoInvalida("Indique um dos 2 valores");
	}

//////////////////// LISTAR DUPLICADORES /////////////////////   

	@RequestMapping(path = "/duplicadores/", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Iterable<Duplicador> listar() {
		return duplicadorDAO.findAll();
	}

/////////////////// LISTAR DUPLICADOR PELA ID ///////////////////    

	@RequestMapping(path = "/duplicadores/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Duplicador recuperar(@PathVariable int id) {
		Optional<Duplicador> optDuplicador = duplicadorDAO.findById(id);
		if (optDuplicador.isPresent()) {
			return optDuplicador.get();
		} else {
			throw new NaoEncontrado("ID não encontrada");
		}
	}

///////////////// INSERIR DUPLICADOR ///////////////////////////    

	@RequestMapping(path = "/duplicadores/", method = RequestMethod.POST)
	public Duplicador inserir(@RequestBody Duplicador duplicador) {
		duplicador.setId(0);
		validaDuplicador(duplicador);
		duplicadorDAO.save(duplicador);
		return duplicador;
	}

//////////////////// APAGAR DUPLICADOR PELA ID ///////////////////////////

	@RequestMapping(path = "/duplicadores/{id}", method = RequestMethod.DELETE)
	public void apagar(@PathVariable int id) {
		duplicadorDAO.deleteById(id);
	}

///////////////// ALTERAR DUPLICADOR PELA ID ///////////////////////////            

	@RequestMapping(path = "/duplicadores/{id}", method = RequestMethod.PUT)
	public void atualizar(@PathVariable int id, @RequestBody Duplicador duplicadorNovo) {

		Duplicador duplicadorAntigo = this.recuperar(id);
		duplicadorAntigo.setNome(duplicadorNovo.getNome());
		duplicadorAntigo.setCelular(duplicadorNovo.getCelular());
		duplicadorAntigo.setEmail(duplicadorNovo.getEmail());
		duplicadorAntigo.setPix(duplicadorNovo.getPix());
		duplicadorAntigo.setLogin(duplicadorNovo.getLogin());
		duplicadorAntigo.setSenha(duplicadorNovo.getSenha());
		
		validaDuplicador(duplicadorAntigo);
		duplicadorDAO.save(duplicadorAntigo);
	}

////////////// LISTAR CLIENTES DUM DUPLICADOR //////////////////////    

	@RequestMapping(path = "/duplicadores/{idDuplicador}/clientes/", method = RequestMethod.GET)
	public Iterable<Cliente> listarCliente(@PathVariable int idDuplicador) {
		return this.recuperar(idDuplicador).getClientes();

	}

//////////////// INSERIR CLIENTE NUM DUPLICADOR ////////////////////////    

	@RequestMapping(path = "/duplicadores/{idDuplicador}/clientes/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente inserirCliente(@PathVariable int idDuplicador, @RequestBody Cliente cliente) {
		cliente.setId(0);
		Cliente clienteSalvo = clienteDAO.save(cliente);
		Duplicador duplicador = this.recuperar(idDuplicador);
		duplicador.getClientes().add(clienteSalvo);
		duplicadorDAO.save(duplicador);
		return clienteSalvo;
	}

///////////////////// LISTAR UM CLIENTE DUM DUPLICADOR PELA ID DO DUPLICADOR E DO CLIENTE     

	@RequestMapping(path = "/duplicadores/{idDuplicador}/clientes/{id}", method = RequestMethod.GET)
	public Cliente recuperarCliente(@PathVariable int idDuplicador, @PathVariable int id) {
		Optional<Cliente> findById = clienteDAO.findById(id);
		if (findById.isPresent())
			return findById.get();
		else
			throw new NaoEncontrado("Não encontrado");
	}

///////////////////// ALTERAR UM CLIENTE DUM DUPLICADOR PELA ID DO DUPLICADOR E DO CLIENTE     

	@RequestMapping(path = "/duplicadores/{idDuplicador}/clientes/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void atualizarCliente(@PathVariable int idDuplicador, @PathVariable int id, @RequestBody Cliente cliente) {
		if (clienteDAO.existsById(id)) {
			cliente.setId(id);
			clienteDAO.save(cliente);
		} else
			throw new NaoEncontrado("Não encontrado");

	}

///////////////////// APAGAR UM CLIENTE DUM DUPLICADOR PELA ID DO DUPLICADOR E DO CLIENTE     

	@RequestMapping(path = "/duplicadores/{idDuplicador}/clientes/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void apagarCliente(@PathVariable int idDuplicador, @PathVariable int id) {

		Cliente clienteAchada = null;
		Duplicador duplicador = this.recuperar(idDuplicador);
		List<Cliente> clientes = duplicador.getClientes();
		for (Cliente clienteLista : clientes) {
			if (id == clienteLista.getId())
				clienteAchada = clienteLista;
		}
		if (clienteAchada != null) {
			duplicador.getClientes().remove(clienteAchada);
			duplicadorDAO.save(duplicador);
		} else
			throw new NaoEncontrado("Não encontrado");
	}

}