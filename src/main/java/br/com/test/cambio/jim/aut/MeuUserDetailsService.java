package br.com.test.cambio.jim.aut;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.test.cambio.jim.dao.UsuarioDAO;
import br.com.test.cambio.jim.modelo.Usuario;

@Component
public class MeuUserDetailsService implements UserDetailsService {
    @Autowired
    UsuarioDAO usuarioDAO;
    @Override
    public UserDetails loadUserByUsername(String login) 
            throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.findByLogin(login);
        if (usuario == null) {
            throw new UsernameNotFoundException(login + " não existe!");
        }
        try {
        MeuUser meuUser = new MeuUser(usuario);    
        return meuUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
                return null;
        
    }
}
