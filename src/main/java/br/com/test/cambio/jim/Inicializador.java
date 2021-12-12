package br.com.test.cambio.jim;

import static br.com.test.cambio.jim.controller.Usuarios.PASSWORD_ENCODER;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.test.cambio.jim.dao.UsuarioDAO;
import br.com.test.cambio.jim.modelo.Usuario;

@Component
public class Inicializador {
    @Autowired
    UsuarioDAO usuarioDAO;
    // Executa o método logo após a aplicação spring inicializar por completo 
    @PostConstruct
    public void init() {
        Usuario usuarioRoot = usuarioDAO.findByLogin("admin");
        if (usuarioRoot == null) {
            usuarioRoot = new Usuario();
            usuarioRoot.setNome("admin");
            usuarioRoot.setLogin("admin");
            usuarioRoot.setSenha(PASSWORD_ENCODER.encode("12345"));
            usuarioRoot.setPermissoes(Arrays.asList("administrador" ));
            usuarioDAO.save(usuarioRoot);
        }
    }
}
