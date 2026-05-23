package io.github.nivaldosilva.biblioteca_api.repository;

import io.github.nivaldosilva.biblioteca_api.entity.Autor;
import io.github.nivaldosilva.biblioteca_api.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

	Page<Livro> findByAutor(Autor autor, Pageable pageable);
	Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
	Page<Livro> findByTituloContainingIgnoreCaseAndAutor(String titulo, Autor autor, Pageable pageable);
	Optional<Livro> findByIsbn(String isbn);
}