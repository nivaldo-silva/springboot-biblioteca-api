package io.github.nivaldosilva.biblioteca_api.service;

import io.github.nivaldosilva.biblioteca_api.dto.AutorDto;
import io.github.nivaldosilva.biblioteca_api.entity.Autor;
import io.github.nivaldosilva.biblioteca_api.exception.OperacaoNaoPermitidaException;
import io.github.nivaldosilva.biblioteca_api.exception.RegistroDuplicadoException;
import io.github.nivaldosilva.biblioteca_api.exception.RegistroNaoEncontradoException;
import io.github.nivaldosilva.biblioteca_api.mappers.AutorMapper;
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
public class AutorService {

	private final AutorRepository autorRepository;
	private final LivroRepository livroRepository;

	@Transactional
	public AutorDto.AutorResponse salvar(AutorDto.AutorRequest request) {
		log.info("Salvando autor: '{}'", request.nome());

		autorRepository
				.findByNomeAndDataNascimentoAndNacionalidade(
						request.nome(), request.dataNascimento(), request.nacionalidade())
				.ifPresent(a -> {
					log.warn("Cadastro duplicado: autor '{}' já existe com ID {}.", request.nome(), a.getId());
					throw new RegistroDuplicadoException();
				});

		Autor salvo = autorRepository.save(AutorMapper.toEntity(request));
		log.info("Autor '{}' salvo com sucesso. ID: {}", salvo.getNome(), salvo.getId());
		return AutorMapper.toDto(salvo);
	}

	@Transactional(readOnly = true)
	public AutorDto.AutorResponse buscarPorId(UUID id) {
		log.debug("Buscando autor ID: {}", id);
		return autorRepository.findById(id)
				.map(AutorMapper::toDto)
				.orElseThrow(() -> {
					log.warn("Autor ID {} não encontrado.", id);
					return new RegistroNaoEncontradoException();
				});
	}

	@Transactional(readOnly = true)
	public Page<AutorDto.AutorResponse> pesquisar(String nome, String nacionalidade, Pageable pageable) {
		log.info("Pesquisando autores. Filtros — Nome: '{}', Nacionalidade: '{}'", nome, nacionalidade);

		Page<Autor> resultado;
		if (nome != null && nacionalidade != null) {
			resultado = autorRepository.findByNomeContainingIgnoreCaseAndNacionalidadeContainingIgnoreCase(nome, nacionalidade, pageable);
		} else if (nome != null) {
			resultado = autorRepository.findByNomeContainingIgnoreCase(nome, pageable);
		} else if (nacionalidade != null) {
			resultado = autorRepository.findByNacionalidadeContainingIgnoreCase(nacionalidade, pageable);
		} else {
			resultado = autorRepository.findAll(pageable);
		}

		log.info("Pesquisa retornou {} resultado(s).", resultado.getTotalElements());
		return resultado.map(AutorMapper::toDto);
	}

	@Transactional
	public AutorDto.AutorResponse atualizar(UUID id, AutorDto.AutorRequest request) {
		log.info("Atualizando autor ID: {}", id);

		Autor autor = autorRepository.findById(id)
				.orElseThrow(RegistroNaoEncontradoException::new);

		autorRepository
				.findByNomeAndDataNascimentoAndNacionalidade(
						request.nome(), request.dataNascimento(), request.nacionalidade())
				.filter(encontrado -> !encontrado.getId().equals(id))
				.ifPresent(a -> {
					log.warn("Falha na atualização: dados já pertencem ao autor ID {}.", a.getId());
					throw new RegistroDuplicadoException();
				});

		autor.setNome(request.nome());
		autor.setDataNascimento(request.dataNascimento());
		autor.setNacionalidade(request.nacionalidade());

		Autor atualizado = autorRepository.save(autor);
		log.info("Autor '{}' (ID: {}) atualizado com sucesso.", atualizado.getNome(), atualizado.getId());
		return AutorMapper.toDto(atualizado);
	}

	@Transactional
	public void deletar(UUID id) {
		log.info("Solicitação de exclusão do autor ID: {}", id);

		Autor autor = autorRepository.findById(id)
				.orElseThrow(RegistroNaoEncontradoException::new);

		boolean possuiLivros = !livroRepository.findByAutor(autor, Pageable.unpaged()).isEmpty();
		if (possuiLivros) {
			log.warn("Exclusão negada: autor '{}' (ID: {}) possui livros vinculados.", autor.getNome(), id);
			throw new OperacaoNaoPermitidaException();
		}

		autorRepository.delete(autor);
		log.info("Autor '{}' (ID: {}) excluído com sucesso.", autor.getNome(), id);
	}
}