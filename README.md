# API CRUD - Clientes e Pedidos

Esta é uma aplicação Spring Boot que implementa um sistema CRUD completo para gerenciamento de **Clientes** e **Pedidos** com documentação Swagger/OpenAPI.

## 🚀 Tecnologias Utilizadas

- **Spring Boot 3.2.1**
- **Java 21**
- **Spring Data JPA**
- **H2 Database** (banco em memória)
- **Swagger/OpenAPI 3** (documentação da API)
- **Lombok** (redução de boilerplate)
- **Maven** (gerenciamento de dependências)

## 📋 Funcionalidades

### Clientes
- ✅ Criar cliente
- ✅ Listar todos os clientes
- ✅ Buscar cliente por ID
- ✅ Buscar cliente por email
- ✅ Buscar clientes por nome (case-insensitive)
- ✅ Pesquisar clientes por termo (nome ou email)
- ✅ Atualizar cliente
- ✅ Deletar cliente
- ✅ Verificar se email já existe

### Pedidos
- ✅ Criar pedido
- ✅ Listar todos os pedidos
- ✅ Buscar pedido por ID
- ✅ Listar pedidos por cliente
- ✅ Listar pedidos por status
- ✅ Listar pedidos por cliente e status
- ✅ Listar pedidos por período
- ✅ Listar pedidos por valor mínimo
- ✅ Contar pedidos por cliente
- ✅ Atualizar pedido
- ✅ Atualizar status do pedido
- ✅ Deletar pedido (com regras de negócio)

## 🏗️ Arquitetura

```
src/main/java/br/com/poc/
├── controller/          # Controllers REST
├── entity/             # Entidades JPA
├── repository/         # Repositories JPA
├── service/            # Camada de serviço
├── exception/          # Tratamento de exceções
├── config/             # Configurações
└── PadraoApplication.java
```

## 🗄️ Modelo de Dados

### Cliente
- `id` (Long) - Chave primária
- `nome` (String) - Nome obrigatório
- `email` (String) - Email único obrigatório
- `dataCriacao` (LocalDateTime) - Data de criação automática

### Pedido
- `id` (Long) - Chave primária
- `descricao` (String) - Descrição obrigatória
- `valor` (BigDecimal) - Valor positivo obrigatório
- `dataPedido` (LocalDateTime) - Data do pedido automática
- `status` (Enum) - Status do pedido (PENDENTE, PROCESSANDO, CONCLUIDO, CANCELADO)
- `cliente_id` (Long) - Referência ao cliente

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.6+

### Passos
1. Clone o repositório
2. Execute o comando:
```bash
mvn clean package -DskipTests
```

3. Execute a aplicação:
```bash
java -jar target/poc-0.0.1-SNAPSHOT.jar
```

4. A aplicação estará disponível em: `http://localhost:4000`

## 📖 Documentação da API

### Swagger UI
Acesse a documentação interativa da API em:
```
http://localhost:4000/swagger-ui/index.html
```

### Console H2
Acesse o console do banco H2 em:
```
http://localhost:4000/h2-console
```
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** `password`

## 🔗 Endpoints da API

### Clientes (`/api/clientes`)
- `GET /api/clientes` - Listar todos
- `GET /api/clientes/{id}` - Buscar por ID
- `GET /api/clientes/email/{email}` - Buscar por email
- `GET /api/clientes/buscar?nome={nome}` - Buscar por nome
- `GET /api/clientes/pesquisar?termo={termo}` - Pesquisar por termo
- `POST /api/clientes` - Criar cliente
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Deletar cliente
- `GET /api/clientes/existe-email/{email}` - Verificar email

### Pedidos (`/api/pedidos`)
- `GET /api/pedidos` - Listar todos
- `GET /api/pedidos/{id}` - Buscar por ID
- `GET /api/pedidos/cliente/{clienteId}` - Por cliente
- `GET /api/pedidos/status/{status}` - Por status
- `GET /api/pedidos/cliente/{clienteId}/status/{status}` - Por cliente e status
- `GET /api/pedidos/periodo?inicio={inicio}&fim={fim}` - Por período
- `GET /api/pedidos/valor-minimo?valorMinimo={valor}` - Por valor mínimo
- `GET /api/pedidos/cliente/{clienteId}/count` - Contar por cliente
- `POST /api/pedidos` - Criar pedido
- `POST /api/pedidos/cliente/{clienteId}` - Criar para cliente
- `PUT /api/pedidos/{id}` - Atualizar pedido
- `PATCH /api/pedidos/{id}/status?status={status}` - Atualizar status
- `DELETE /api/pedidos/{id}` - Deletar pedido

## 💾 Dados de Exemplo

A aplicação é inicializada com dados de exemplo:

**Clientes:**
- João Silva (joao.silva@email.com)
- Maria Santos (maria.santos@email.com)
- Pedro Oliveira (pedro.oliveira@email.com)

**Pedidos:**
- Pedido de teste 1 (R$ 150,50 - PENDENTE)
- Pedido de teste 2 (R$ 299,99 - PROCESSANDO)
- Pedido de teste 3 (R$ 89,90 - CONCLUIDO)
- Pedido de teste 4 (R$ 45,00 - PENDENTE)
- Pedido de teste 5 (R$ 199,99 - CANCELADO)

## 🔒 Regras de Negócio

### Clientes
- Email deve ser único
- Nome e email são obrigatórios
- Data de criação é definida automaticamente

### Pedidos
- Cliente é obrigatório
- Valor deve ser positivo
- Status inicial é PENDENTE
- Transições de status seguem regras:
  - PENDENTE → PROCESSANDO ou CANCELADO
  - PROCESSANDO → CONCLUIDO ou CANCELADO
  - CONCLUIDO/CANCELADO → não podem ser alterados
- Só é possível deletar pedidos PENDENTES ou CANCELADOS

## 🛠️ Tratamento de Erros

A aplicação possui um sistema global de tratamento de exceções que retorna:
- Mensagens de erro padronizadas
- Códigos HTTP apropriados
- Validações de campos com detalhes específicos
- Timestamp das ocorrências

## 📝 Validações

- **Bean Validation** com anotações Jakarta
- Validação de email formato
- Campos obrigatórios
- Valores positivos para preços
- Unicidade de email

## 🔧 Configurações

- **Porta:** 4000
- **Profile ativo:** h2
- **Banco:** H2 em memória
- **JPA:** Hibernate com DDL create-drop
- **Logs SQL:** Habilitados
