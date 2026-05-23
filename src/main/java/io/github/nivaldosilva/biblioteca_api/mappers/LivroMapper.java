package io.github.nivaldosilva.biblioteca_api.mappers;

import io.github.nivaldosilva.biblioteca_api.entity.Autor;
import io.github.nivaldosilva.biblioteca_api.entity.Livro;
import io.github.nivaldosilva.biblioteca_api.dto.LivroDto;
import lombok.experimental.UtilityClass;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class LivroMapper {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static Livro toEntity(LivroDto.LivroRequest request, Autor autor) {
		return Livro.builder()
				.autor(autor)
				.isbn(request.isbn())
				.titulo(request.titulo())
				.sinopse(request.sinopse())
				.editora(request.editora())
				.dataPublicacao(request.dataPublicacao())
				.genero(request.genero())
				.preco(request.preco())
				.build();
	}

	public static LivroDto.LivroResponse toDto(Livro entity) {
		return LivroDto.LivroResponse.builder()
				.id(entity.getId())
				.idAutor(entity.getAutor().getId())
				.autor(entity.getAutor().getNome())
				.isbn(entity.getIsbn())
				.titulo(entity.getTitulo())
				.genero(entity.getGenero().name())
				.sinopse(entity.getSinopse())
				.editora(entity.getEditora())
				.dataPublicacao(entity.getDataPublicacao().format(DATE_FORMATTER))
				.preco(entity.getPreco().toString())
				.build();
	}
}