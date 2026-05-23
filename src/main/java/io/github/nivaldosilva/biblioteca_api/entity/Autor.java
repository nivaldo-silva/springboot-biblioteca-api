package io.github.nivaldosilva.biblioteca_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autores", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"livros"})
@Builder
public class Autor {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "nome",length = 100, nullable = false)
	private String nome;

	@Column(name = "data_nascimento", nullable = false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;

	@Column(name = "nacionalidade",length = 50, nullable = false)
	private String nacionalidade;

	@OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
	private List<Livro> livros;

	@CreationTimestamp
	@Column(name = "data_cadastro")
	private LocalDateTime dataCadastro;

	@UpdateTimestamp
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;

}
