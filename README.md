#  Soluciona API

Backend robusto para a plataforma **Soluciona**, um marketplace de serviços locais (estilo OLX de serviços). Esta API gerencia utilizadores, anúncios de serviços, categorização e o fluxo de solicitações de orçamento entre clientes e prestadores.

## Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3** (Web, Data JPA, Validation)
* **Spring Security** (Proteção de rotas e Filtros)
* **PostgreSQL** (Banco de dados hospedado no Supabase)
* **Supabase Auth** (Integração para autenticação via JWT)
* **Lombok** (Produtividade e redução de código boilerplate)
* **Maven** (Gerenciamento de dependências)

## Arquitetura e Segurança

O projeto segue uma arquitetura moderna separando a **Autenticação** da **Lógica de Negócio**:

1.  **Autenticação (O "Porteiro"):** Gerenciada externamente pelo **Supabase Auth**. O frontend obtém o Token JWT diretamente do Supabase.
2.  **Autorização (O "Gerente"):** A API Spring Boot valida o Token JWT recebido em cada requisição privada usando um `JwtAuthFilter` customizado e verifica as permissões de acesso (ex: apenas o dono do anúncio pode editá-lo).

## Modelo de Dados (Supabase)

O banco de dados PostgreSQL foi modelado com as seguintes entidades principais:

* **`profiles`**: Dados públicos dos utilizadores (Nome, Foto, Telefone, Geolocalização). Sincronizado com o Auth.
* **`categories`**: Categorias de serviços (ex: Jardinagem, Aulas, Reformas).
* **`service_post`**: O anúncio em si. Contém título, preço, fotos (JSONB) e localização.
* **`requests`**: Solicitações de serviço feitas por clientes aos prestadores (Status: Pendente, Aceita, Rejeitada).

##  Como Rodar o Projeto Localmente

### Pré-requisitos
* Java 17 SDK instalado.
* Maven instalado.
* Uma conta no Supabase com o banco configurado.

### Configuração de Ambiente
Crie as variáveis de ambiente no seu sistema ou configure no `application.properties` (não comite senhas reais!):

```properties
DB_URL=jdbc:postgresql://[HOST-DO-SUPABASE]:5432/postgres
DB_USER=postgres
DB_PASSWORD=[SUA-SENHA-DO-BANCO]
JWT_SECRET=[SEU-SUPABASE-JWT-SECRET]
```

### 🔌 Endpoints da API

**Base URL:** `http://localhost:8080` 

#### 👤 Perfis (Profiles)
| Método | Endpoint | Autenticação | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/profiles` | **Pública** | Cria o perfil (após registo no Supabase Auth). |
| `GET` | `/api/profiles/{id}` | **Pública** | Vê os dados de um perfil específico. |
| `PUT` | `/api/profiles/{id}` |  **Privada** | Atualiza dados do perfil (apenas o dono). |

#### 📂 Categorias
| Método | Endpoint | Autenticação | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/categories` | **Pública** | Lista todas as categorias disponíveis. |
| `DELETE` | `/api/categories/{id}` |  **Privada** | Apaga uma categoria (Admin/Dev). |

#### 🛠️ Anúncios (Services)
| Método | Endpoint | Autenticação | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/services` | **Pública** | Lista todos os anúncios ativos. |
| `GET` | `/api/services/{id}` | **Pública** | Vê os detalhes de um anúncio. |
| `POST` | `/api/services` |  **Privada** | Cria um novo anúncio. |
| `PUT` | `/api/services/{id}` |  **Privada** | Atualiza um anúncio (apenas o dono). |
| `DELETE` | `/api/services/{id}` |  **Privada** | Remove um anúncio (apenas o dono). |

#### 🤝 Solicitações (Requests)
| Método | Endpoint | Autenticação | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/requests` |  **Privada** | Cliente pede um orçamento para um serviço. |
| `GET` | `/api/requests/me` |  **Privada** | Lista "Pedidos que Fiz" e "Pedidos que Recebi". |
| `PUT` | `/api/requests/{id}/status` |  **Privada** | Prestador Aceita/Rejeita um pedido. |

