//LOCALHOST:8080/api/clientes/

package br.com.test.cambio.jim.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.test.cambio.jim.dao.ClienteDAO;
import br.com.test.cambio.jim.erros.NaoEncontrado;
import br.com.test.cambio.jim.erros.RequisicaoInvalida;
import br.com.test.cambio.jim.modelo.Cliente;

@RestController
@RequestMapping(path = "/api")
public class Clientes {

    @Autowired
    ClienteDAO clienteDAO;
        
///////////// VALIDAÇÕES ////////////////////////       
    
    public void validaCliente(Cliente cliente){
        if (cliente.getNome() == null || cliente.getNome().isEmpty()){
            throw new RequisicaoInvalida("NOME é obrigatório");
        }
        if (cliente.getCelular() == null || cliente.getCelular().isEmpty()){
            throw new RequisicaoInvalida("CELULAR é obrigatório");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()){
            throw new RequisicaoInvalida("E-MAIL é obrigatório");
        }
        if (cliente.getPix() == null || cliente.getPix().isEmpty()){
            throw new RequisicaoInvalida("PIX é obrigatório");
        }
        if (cliente.getLogin()== null || cliente.getLogin().isEmpty()){
            throw new RequisicaoInvalida("LOGIN é obrigatório");
        }
        if (cliente.getSenha() == null || cliente.getSenha().isEmpty()){
            throw new RequisicaoInvalida("SENHA é obrigatória");
        }

    }
    
///////////// LISTAR CLIENTES ////////////////////////       

    @RequestMapping(path = "/clientes/", method = RequestMethod.GET)
    public Iterable<Cliente> listar() {
        return clienteDAO.findAll();
    }
    
///////////// INSERIR CLIENTE ////////////////////////       

    @RequestMapping(path = "/clientes/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente inserir(@RequestBody Cliente cliente) {
        cliente.setId(0);
        validaCliente(cliente);
        return clienteDAO.save(cliente);   
    }
    
///////////// RECUPERAR CLIENTE PELA ID ////////////////////////       

    @RequestMapping(path = "/clientes/{id}", method = RequestMethod.GET)
    public Cliente recuperar(@PathVariable int id) {
        Optional<Cliente> findById = clienteDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("CLIENTE não encontrado");
        }
    }
    
///////////// ATUALIZAR CLIENTE PELA ID ////////////////////////       

    @RequestMapping(path = "/clientes/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Cliente cliente) {
        if (clienteDAO.existsById(id)) {
            cliente.setId(id);
            validaCliente(cliente);
            clienteDAO.save(cliente);
        } else {
            throw new NaoEncontrado("CLIENTE não encontrado");
        }
    }

///////////// APAGAR CLIENTE PELA ID ////////////////////////               
    
    @RequestMapping(path = "/clientes/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id){

        if (clienteDAO.existsById(id)){
            clienteDAO.deleteById(id);
        }else {
            throw new NaoEncontrado("CLIENTE não encontrado");
        }
    } 

}