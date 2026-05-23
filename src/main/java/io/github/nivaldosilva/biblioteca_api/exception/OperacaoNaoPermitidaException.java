package io.github.nivaldosilva.biblioteca_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class OperacaoNaoPermitidaException extends RuntimeException {

	public OperacaoNaoPermitidaException() {
		super("Esta operação não pode ser realizada pois viola regras de integridade.");
	}

	public OperacaoNaoPermitidaException(String message) {
		super(message);
	}
}