# Biblioteca API

REST API para gerenciamento de acervo bibliográfico, com suporte a cadastro de autores e livros, pesquisa paginada e validação de integridade referencial.

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Data JPA | — |
| Spring Validation | — |
| PostgreSQL | — |
| Lombok | — |

---

## Pré-requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL em execução (local ou via Docker)

---

## Configuração

A aplicação utiliza variáveis de ambiente para as credenciais do banco de dados. Crie um arquivo `.env` na raiz do projeto:

```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/biblioteca
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha
```

Os valores acima também são os defaults caso o arquivo `.env` não exista, permitindo subir a aplicação localmente sem configuração adicional.

A aplicação sobe na porta `8091` por padrão.

---

## Executando

```bash
# Clonar o repositório
git clone https://github.com/nivaldosilva/biblioteca-api.git
cd biblioteca-api

# Compilar e executar
./mvnw spring-boot:run
```

Ou gerando o JAR:

```bash
./mvnw clean package
java -jar target/biblioteca-api-0.0.1-SNAPSHOT.jar
```

---

## Perfis

A aplicação possui dois perfis Spring:

**default** — usado em desenvolvimento. Exibe SQL formatado no console, logging detalhado para os pacotes da aplicação e do Spring Web.

**production** — ativado por padrão via `spring.profiles.active: production` no `application.yaml`. Desabilita exibição de SQL, reduz o nível de log para `WARN` na maioria dos pacotes.

---

## Endpoints

### Autores — `/autores`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/autores` | Cadastra um novo autor |
| `GET` | `/autores/{id}` | Busca autor por ID |
| `GET` | `/autores` | Lista/pesquisa autores (paginado) |
| `PUT` | `/autores/{id}` | Atualiza dados do autor |
| `DELETE` | `/autores/{id}` | Remove o autor |

**Parâmetros de pesquisa (`GET /autores`):**

- `nome` — filtro parcial, case-insensitive
- `nacionalidade` — filtro parcial, case-insensitive
- Parâmetros de paginação padrão do Spring (`page`, `size`, `sort`)

**Body — `POST` / `PUT`:**

```json
{
  "nome": "Machado de Assis",
  "data_nascimento": "21/06/1839",
  "nacionalidade": "Brasileira"
}
```

**Resposta:**

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "nome": "Machado de Assis",
  "data_nascimento": "21/06/1839",
  "nacionalidade": "Brasileira"
}
```

---

### Livros — `/livros`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/livros` | Cadastra um novo livro |
| `GET` | `/livros/{id}` | Busca livro por ID |
| `GET` | `/livros` | Lista/pesquisa livros (paginado) |
| `PUT` | `/livros/{id}` | Atualiza dados do livro |
| `DELETE` | `/livros/{id}` | Remove o livro |

**Parâmetros de pesquisa (`GET /livros`):**

- `titulo` — filtro parcial, case-insensitive
- `id_autor` — UUID do autor
- Parâmetros de paginação padrão do Spring (`page`, `size`, `sort`)

**Body — `POST` / `PUT`:**

```json
{
  "id_autor": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "isbn": "978-85-359-0277-5",
  "titulo": "Dom Casmurro",
  "genero": "ROMANCE",
  "sinopse": "Narrativa de Bentinho e sua obsessão por Capitu.",
  "editora": "Garnier",
  "data_publicacao": "01/01/1899",
  "preco": 39.90
}
```

**Gêneros disponíveis:** `FICCAO`, `FANTASIA`, `MISTERIO`, `ROMANCE`, `BIOGRAFIA`, `CIENCIA`

**Resposta:**

```json
{
  "id": "...",
  "id_autor": "...",
  "autor": "Machado de Assis",
  "isbn": "978-85-359-0277-5",
  "titulo": "Dom Casmurro",
  "genero": "ROMANCE",
  "sinopse": "...",
  "editora": "Garnier",
  "data_publicacao": "01/01/1899",
  "preco": "39.90"
}
```

---

## Tratamento de erros

A API utiliza o formato `ProblemDetail` (RFC 9457) em todas as respostas de erro.

| Situação | Status |
|---|---|
| Recurso não encontrado | `404 Not Found` |
| Dados duplicados (nome+nascimento+nacionalidade para autores, ISBN para livros) | `409 Conflict` |
| Operação não permitida (ex: excluir autor com livros vinculados) | `400 Bad Request` |
| Campo com valor inválido | `422 Unprocessable Entity` |
| Falha de validação do bean (`@Valid`) | `422 Unprocessable Entity` |

Exemplo de resposta de validação:

```json
{
  "type": "about:blank",
  "title": "Unprocessable Entity",
  "status": 422,
  "detail": "Erro de validação nos campos.",
  "invalid-params": [
    {
      "campo": "nome",
      "mensagem": "O nome é obrigatório"
    }
  ]
}
```

---

## Regras de negócio

- Um autor não pode ser excluído enquanto possuir livros vinculados.
- ISBN deve ser único entre todos os livros cadastrados.
- Autores são considerados duplicados quando nome, data de nascimento e nacionalidade coincidem.
- A data de nascimento do autor deve ser uma data passada.
- A data de publicação do livro deve ser uma data passada.

---

## Estrutura do projeto

```
src/main/java/io/github/nivaldosilva/biblioteca_api/
├── controller/
│   ├── AutorController.java
│   └── LivroController.java
├── service/
│   ├── AutorService.java
│   └── LivroService.java
├── repository/
│   ├── AutorRepository.java
│   └── LivroRepository.java
├── entity/
│   ├── Autor.java
│   └── Livro.java
├── dto/
│   ├── AutorDto.java
│   └── LivroDto.java
├── mappers/
│   ├── AutorMapper.java
│   └── LivroMapper.java
└── exception/
    ├── CampoInvalidoException.java
    ├── GlobalExceptionHandler.java
    ├── OperacaoNaoPermitidaException.java
    ├── RegistroDuplicadoException.java
    └── RegistroNaoEncontradoException.java
```

---


