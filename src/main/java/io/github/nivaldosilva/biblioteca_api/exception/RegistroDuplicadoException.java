package io.github.nivaldosilva.biblioteca_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegistroDuplicadoException extends RuntimeException {

	public RegistroDuplicadoException() {
		super("Erro: Já existe um registo com estes dados no sistema.");
	}

	public RegistroDuplicadoException(String message) {
		super(message);
	}
}