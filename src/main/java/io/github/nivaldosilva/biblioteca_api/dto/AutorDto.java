package io.github.nivaldosilva.biblioteca_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

public class AutorDto {

	@Builder
	public record AutorRequest(
			@NotBlank(message = "O nome é obrigatório")
			@Size(min = 2, max = 100)
			String nome,

			@JsonProperty("data_nascimento")
			@Past(message = "A data de nascimento não pode ser uma data futura")
			@JsonFormat(pattern = "dd/MM/yyyy")
			LocalDate dataNascimento,

		    @NotBlank(message = "A nacionalidade é obrigatória")
			@Size(max = 50)
			String nacionalidade
	){}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record AutorResponse(
			UUID id,
			String nome,
			@JsonProperty("data_nascimento")
			@JsonFormat(pattern = "dd/MM/yyyy")
			String dataNascimento,
			String nacionalidade
	){}


}
