package br.com.test.cambio.jim.aut;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import br.com.test.cambio.jim.modelo.Usuario;

public class MeuUser extends User {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Usuario usuario;
    public MeuUser(Usuario usuario) {
        super(usuario.getLogin(),
                usuario.getSenha(),
                AuthorityUtils.createAuthorityList(
                    usuario.getPermissoes().toArray(new String[]{})));
        this.usuario=usuario;
    }
    public Usuario getUsuario() {
        return usuario;
    }
}
