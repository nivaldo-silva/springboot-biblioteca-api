package io.github.nivaldosilva.biblioteca_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "autor")
@Builder
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "isbn", length = 20, nullable = false)
	private String isbn;

	@Column(name = "titulo", length = 150, nullable = false)
	private String titulo;

	@Enumerated(EnumType.STRING)
	@Column(name = "genero", length = 30, nullable = false)
	private GeneroLivro genero;

	@Column(name = "descricao", columnDefinition = "TEXT")
	private String sinopse;

	@Column(name = "editora", length = 100)
	private String editora;

	@Column(name = "data_publicacao")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPublicacao;

	@Column(name = "preco", precision = 18, scale = 2)
	private BigDecimal preco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_autor")
	private Autor autor;

	@CreationTimestamp
	@Column(name = "data_cadastro")
	private LocalDateTime dataCadastro;

    @UpdateTimestamp
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;

	public enum GeneroLivro {
		FICCAO,
		FANTASIA,
		MISTERIO,
		ROMANCE,
		BIOGRAFIA,
		CIENCIA
	}

}
