package io.github.nivaldosilva.biblioteca_api.service;

import io.github.nivaldosilva.biblioteca_api.dto.LivroDto;
import io.github.nivaldosilva.biblioteca_api.entity.Autor;
import io.github.nivaldosilva.biblioteca_api.entity.Livro;
import io.github.nivaldosilva.biblioteca_api.exception.RegistroDuplicadoException;
import io.github.nivaldosilva.biblioteca_api.exception.RegistroNaoEncontradoException;
import io.github.nivaldosilva.biblioteca_api.mappers.LivroMapper;
import io.github.nivaldosilva.biblioteca_api.repository.AutorRepository;
import io.github.nivaldosilva.biblioteca_api.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LivroService {

	private final LivroRepository livroRepository;
	private final AutorRepository autorRepository;

	@Transactional
	public LivroDto.LivroResponse salvar(LivroDto.LivroRequest request) {
		log.info("Salvando livro: '{}', ISBN: {}", request.titulo(), request.isbn());

		Autor autor = autorRepository.findById(request.idAutor())
				.orElseThrow(() -> {
					log.error("Erro ao salvar livro: Autor ID {} não encontrado.", request.idAutor());
					return new RegistroNaoEncontradoException();
				});

		livroRepository.findByIsbn(request.isbn())
				.ifPresent(l -> {
					log.warn("Tentativa de salvar livro com ISBN duplicado: {}", request.isbn());
					throw new RegistroDuplicadoException();
				});

		Livro salvo = livroRepository.save(LivroMapper.toEntity(request, autor));
		log.info("Livro '{}' salvo com sucesso. ID: {}", salvo.getTitulo(), salvo.getId());
		return LivroMapper.toDto(salvo);
	}

	@Transactional(readOnly = true)
	public LivroDto.LivroResponse buscarPorId(UUID id) {
		log.debug("Buscando livro ID: {}", id);
		return livroRepository.findById(id)
				.map(LivroMapper::toDto)
				.orElseThrow(() -> {
					log.warn("Livro ID {} não encontrado.", id);
					return new RegistroNaoEncontradoException();
				});
	}

	@Transactional(readOnly = true)
	public Page<LivroDto.LivroResponse> pesquisar(String titulo, UUID idAutor, Pageable pageable) {
		log.info("Pesquisando livros. Filtros — Título: '{}', ID Autor: {}", titulo, idAutor);

		if (titulo != null && idAutor != null) {
			Autor autor = autorRepository.findById(idAutor)
					.orElseThrow(RegistroNaoEncontradoException::new);
			return livroRepository
					.findByTituloContainingIgnoreCaseAndAutor(titulo, autor, pageable)
					.map(LivroMapper::toDto);
		}

		if (titulo != null) {
			return livroRepository
					.findByTituloContainingIgnoreCase(titulo, pageable)
					.map(LivroMapper::toDto);
		}

		if (idAutor != null) {
			Autor autor = autorRepository.findById(idAutor)
					.orElseThrow(RegistroNaoEncontradoException::new);
			return livroRepository
					.findByAutor(autor, pageable)
					.map(LivroMapper::toDto);
		}

		return livroRepository.findAll(pageable).map(LivroMapper::toDto);
	}

	@Transactional
	public LivroDto.LivroResponse atualizar(UUID id, LivroDto.LivroRequest request) {
		log.info("Atualizando livro ID: {}", id);

		Livro livro = livroRepository.findById(id)
				.orElseThrow(RegistroNaoEncontradoException::new);

		Autor autor = autorRepository.findById(request.idAutor())
				.orElseThrow(() -> {
					log.warn("Falha na atualização: Autor ID {} não encontrado.", request.idAutor());
					return new RegistroNaoEncontradoException();
				});

		livroRepository.findByIsbn(request.isbn())
				.filter(l -> !l.getId().equals(id))
				.ifPresent(l -> {
					log.error("Falha na atualização: ISBN {} já pertence ao livro ID {}.", request.isbn(), l.getId());
					throw new RegistroDuplicadoException();
				});

		livro.setAutor(autor);
		livro.setIsbn(request.isbn());
		livro.setTitulo(request.titulo());
		livro.setGenero(request.genero());
		livro.setSinopse(request.sinopse());
		livro.setEditora(request.editora());
		livro.setDataPublicacao(request.dataPublicacao());
		livro.setPreco(request.preco());

		Livro atualizado = livroRepository.save(livro);
		log.info("Livro '{}' (ID: {}) atualizado com sucesso.", atualizado.getTitulo(), atualizado.getId());
		return LivroMapper.toDto(atualizado);
	}

	@Transactional
	public void deletar(UUID id) {
		log.info("Excluindo livro ID: {}", id);
		Livro livro = livroRepository.findById(id)
				.orElseThrow(RegistroNaoEncontradoException::new);
		livroRepository.delete(livro);
		log.info("Livro '{}' (ID: {}) excluído com sucesso.", livro.getTitulo(), id);
	}
}