package io.github.nivaldosilva.biblioteca_api.repository;

import io.github.nivaldosilva.biblioteca_api.entity.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {

	Page<Autor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
	Page<Autor> findByNacionalidadeContainingIgnoreCase(String nacionalidade, Pageable pageable);
	Page<Autor> findByNomeContainingIgnoreCaseAndNacionalidadeContainingIgnoreCase(String nome, String nacionalidade, Pageable pageable);

	Optional<Autor> findByNomeAndDataNascimentoAndNacionalidade(
			String nome, LocalDate dataNascimento, String nacionalidade
	);
}