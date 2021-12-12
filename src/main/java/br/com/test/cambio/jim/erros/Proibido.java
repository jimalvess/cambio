package br.com.test.cambio.jim.erros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class Proibido extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public Proibido(String erro) {
        super(erro);
    }
    
}
