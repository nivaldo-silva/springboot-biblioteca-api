package io.github.nivaldosilva.biblioteca_api.mappers;

import io.github.nivaldosilva.biblioteca_api.entity.Autor;
import io.github.nivaldosilva.biblioteca_api.dto.AutorDto;
import lombok.experimental.UtilityClass;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class AutorMapper {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static Autor toEntity(AutorDto.AutorRequest request) {
		return Autor.builder()
				.nome(request.nome())
				.dataNascimento(request.dataNascimento())
				.nacionalidade(request.nacionalidade())
				.build();
	}

	public static AutorDto.AutorResponse toDto(Autor entity) {
		return AutorDto.AutorResponse.builder()
				.id(entity.getId())
				.nome(entity.getNome())
				.dataNascimento(entity.getDataNascimento().format(DATE_FORMATTER))
				.nacionalidade(entity.getNacionalidade())
				.build();
	}
}