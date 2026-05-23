package io.github.nivaldosilva.biblioteca_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.biblioteca_api.entity.Livro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import org.hibernate.validator.constraints.ISBN;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class LivroDto {

	@Builder
	public record LivroRequest(
			@NotNull(message = "O id do autor é obrigatorio")
			@JsonProperty("id_autor")
			UUID idAutor,

			@ISBN
			@NotBlank(message = "O isbn é obrigatorio")
			String isbn,

			@NotBlank(message = "O titulo é obrigatorio")
			String titulo,

			@NotNull(message = "O genero do livro é obrigatorio")
			Livro.GeneroLivro genero,

			@NotBlank(message = "A sinopse é obrigatoria")
			String sinopse,

			@NotBlank(message = "A editora é obrigatorio")
			String editora,

			@NotNull(message = "A data de publicacao é obrigatoria")
			@JsonProperty("data_publicacao")
			@JsonFormat(pattern = "dd/MM/yyyy")
			@Past LocalDate dataPublicacao,

			@NotNull(message = "O preco do livro é obrigatorio")
			BigDecimal preco
	){}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record LivroResponse(
			UUID id,
			@JsonProperty("id_autor")
			UUID idAutor,
			String autor,
			String isbn,
			String titulo,
			String genero,
			String sinopse,
			String editora,
			@JsonProperty("data_publicacao")
			@JsonFormat(pattern = "dd/MM/yyyy")
			String dataPublicacao,
			String preco
	){}
}



