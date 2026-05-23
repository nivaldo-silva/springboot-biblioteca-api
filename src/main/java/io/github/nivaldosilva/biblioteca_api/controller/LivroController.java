package io.github.nivaldosilva.biblioteca_api.controller;

import io.github.nivaldosilva.biblioteca_api.dto.LivroDto;
import io.github.nivaldosilva.biblioteca_api.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

	private final LivroService livroService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LivroDto.LivroResponse salvar(@RequestBody @Valid LivroDto.LivroRequest request) {
		return livroService.salvar(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LivroDto.LivroResponse> buscarPorId(@PathVariable UUID id) {
		return ResponseEntity.ok(livroService.buscarPorId(id));
	}

	@GetMapping
	public ResponseEntity<Page<LivroDto.LivroResponse>> pesquisar(
			@RequestParam(required = false) String titulo,
			@RequestParam(value = "id_autor", required = false) UUID idAutor,
			Pageable pageable) {
		return ResponseEntity.ok(livroService.pesquisar(titulo, idAutor, pageable));
	}

	@PutMapping("/{id}")
	public ResponseEntity<LivroDto.LivroResponse> atualizar(
			@PathVariable UUID id,
			@RequestBody @Valid LivroDto.LivroRequest request) {
		return ResponseEntity.ok(livroService.atualizar(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable UUID id) {
		livroService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}