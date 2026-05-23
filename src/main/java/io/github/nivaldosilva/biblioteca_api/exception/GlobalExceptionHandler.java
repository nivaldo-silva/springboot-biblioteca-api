package io.github.nivaldosilva.biblioteca_api.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RegistroNaoEncontradoException.class)
	public ProblemDetail handleRegistroNaoEncontrado(RegistroNaoEncontradoException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(RegistroDuplicadoException.class)
	public ProblemDetail handleRegistroDuplicado(RegistroDuplicadoException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(OperacaoNaoPermitidaException.class)
	public ProblemDetail handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(CampoInvalidoException.class)
	public ProblemDetail handleCampoInvalido(CampoInvalidoException ex) {
		ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
		pd.setProperty("campo", ex.getCampo());
		return pd;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, "Erro de validação nos campos.");

		List<Map<String, String>> erros = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> Map.of("campo", f.getField(), "mensagem", f.getDefaultMessage()))
				.collect(Collectors.toList());

		pd.setProperty("invalid-params", erros);
		return ResponseEntity.status(status).body(pd);
	}
}