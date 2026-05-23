package io.github.nivaldosilva.biblioteca_api.controller;

import io.github.nivaldosilva.biblioteca_api.dto.AutorDto;
import io.github.nivaldosilva.biblioteca_api.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

	private final AutorService autorService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AutorDto.AutorResponse salvar(@RequestBody @Valid AutorDto.AutorRequest request) {
		return autorService.salvar(request);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AutorDto.AutorResponse> buscarPorId(@PathVariable UUID id) {
		return ResponseEntity.ok(autorService.buscarPorId(id));
	}

	@GetMapping
	public ResponseEntity<Page<AutorDto.AutorResponse>> pesquisar(
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String nacionalidade,
			Pageable pageable) {
		return ResponseEntity.ok(autorService.pesquisar(nome, nacionalidade, pageable));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AutorDto.AutorResponse> atualizar(
			@PathVariable UUID id,
			@RequestBody @Valid AutorDto.AutorRequest request) {
		return ResponseEntity.ok(autorService.atualizar(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable UUID id) {
		autorService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}