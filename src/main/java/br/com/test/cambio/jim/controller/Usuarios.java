package br.com.test.cambio.jim.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.test.cambio.jim.aut.MeuUser;
import br.com.test.cambio.jim.dao.UsuarioDAO;
import br.com.test.cambio.jim.erros.NaoEncontrado;
import br.com.test.cambio.jim.erros.Proibido;
import br.com.test.cambio.jim.modelo.Usuario;

@RestController
@RequestMapping(path = "/api")
public class Usuarios {
    
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

///////////////// TROCA SENHA: //////////////////////////
    
    public Usuario inserirOld(@RequestBody Usuario usuario) {
        usuario.setId(0);
        usuario.setSenha(PASSWORD_ENCODER.encode(usuario.getNovaSenha()));
        ArrayList<String> permissao = new ArrayList<>();
        permissao.add("usuario");
        usuario.setPermissoes(permissao);
        Usuario usuarioSalvo = usuarioDAO.save(usuario);
        return usuarioSalvo;
    }
    
///////////////// INSERIR USUÁRIO //////////////////////////
    
    @RequestMapping(path = "/usuarios", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario inserir(@AuthenticationPrincipal MeuUser usuarioAut, @RequestBody Usuario usuario) {
        usuario.setId(0);
        usuario.setSenha(PASSWORD_ENCODER.encode(usuario.getNovaSenha()));

        if (usuarioAut == null || !usuarioAut.getUsuario().getPermissoes().contains("administrador")) {
            ArrayList<String> permissao = new ArrayList<String>();
            permissao.add("usuario");
            usuario.setPermissoes(permissao);
        }
        Usuario usuarioSalvo = usuarioDAO.save(usuario);
        return usuarioSalvo;
    }

    @Autowired
    UsuarioDAO usuarioDAO;

    
///////////////// LISTA USUÁRIOS //////////////////////////
    
@PreAuthorize("hasAuthority('administrador')")
@RequestMapping(path = "/usuarios", method = RequestMethod.GET)
public Iterable<Usuario> listar() {
    return usuarioDAO.findAll();
}

/////////////////// LISTA UM USUÁRIO PELA ID //////////////////

@RequestMapping(path = "/usuarios/{id}", method = RequestMethod.GET)
public Usuario recuperar(@AuthenticationPrincipal MeuUser usuarioAut, 
        @PathVariable int id) {
    if (usuarioAut.getUsuario().getId() == id
        || usuarioAut.getUsuario().getPermissoes().contains("administrador")) {
        return usuarioDAO.findById(id).get();
    } else {
        throw new Proibido("Não é permitido acessar dados de outro usuários");
    }
}

////////////////// ATUALIZA UM USUÁRIO PELA ID /////////////////

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        if (usuarioDAO.existsById(id)) {
            usuario.setId(id);
            usuarioDAO.save(usuario);
        }
    }
    
///////////////// DELETA UM USUÁRIO PELA ID //////////////////////    

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagar(@PathVariable int id) {
        if (usuarioDAO.existsById(id)) {
            usuarioDAO.deleteById(id);
        }

    }
    
//////////////// PRA O USUÁRIO LOGAR /////////////////////////    

    @RequestMapping(path = "/usuarios/loginOld/", method = RequestMethod.GET)
    public Usuario login(@RequestParam String usuario, 
            @RequestParam String senha) {
        Usuario usuarioBanco = usuarioDAO.findByLogin(usuario);
        if(usuarioBanco!=null) {
        boolean matches = 
                PASSWORD_ENCODER.matches(senha, usuarioBanco.getSenha());
        if(matches) {
            return usuarioBanco;
        }
        }
        throw  new NaoEncontrado("Usuário e/ou senha incorreto(s)");
    }
    
///////////// DEFINE O SEGREDO DA CRIPTOGRAFIA /////////////////////////    

    public static final String SEGREDO= 
        "não há quem pinte um retrado de um bochincho quando estoura";
    @RequestMapping(path = "/usuarios/login/", method = RequestMethod.GET)
    public ResponseEntity<Usuario> loginToken(@RequestParam String usuario,
            @RequestParam String senha) throws UnsupportedEncodingException {
        Usuario usuarioBanco = usuarioDAO.findByLogin(usuario);
        if (usuarioBanco != null) {
            boolean achou = 
                    PASSWORD_ENCODER.matches(senha, usuarioBanco.getSenha());
            if (achou) {
                Algorithm algorithm = Algorithm.HMAC256(SEGREDO);
                Calendar agora = Calendar.getInstance();
                agora.add(Calendar.MINUTE, 4);
                Date expira = agora.getTime();
                String token = JWT.create()
                        .withClaim("id", usuarioBanco.getId()).
                        withExpiresAt(expira).
                        sign(algorithm);
                HttpHeaders respHeaders = new HttpHeaders();
                respHeaders.set("token", token);
                return new ResponseEntity<>(usuarioBanco, 
                        respHeaders, HttpStatus.OK);
            }
        }
        throw new NaoEncontrado("Usuário e/ou senha incorreto(s)");
    }
    
}
