package io.github.nivaldosilva.biblioteca_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CampoInvalidoException extends RuntimeException {

	private final String campo;

	public CampoInvalidoException(String campo) {
		super("O valor informado para este campo é inválido.");
		this.campo = campo;
	}

	public CampoInvalidoException(String campo, String message) {
		super(message);
		this.campo = campo;
	}
}