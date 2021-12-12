package br.com.test.cambio.jim.erros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NaoEncontrado extends RuntimeException{
    
	private static final long serialVersionUID = 1L;

	public NaoEncontrado(String msg){
        super(msg);
    }
}