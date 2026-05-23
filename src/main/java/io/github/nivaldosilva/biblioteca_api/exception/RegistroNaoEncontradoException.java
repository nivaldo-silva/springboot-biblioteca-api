package io.github.nivaldosilva.biblioteca_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RegistroNaoEncontradoException extends RuntimeException {

	public RegistroNaoEncontradoException() {
		super("Registro não encontrado em nossa base de dados.");
	}

	public RegistroNaoEncontradoException(String message) {
		super(message);
	}
}
