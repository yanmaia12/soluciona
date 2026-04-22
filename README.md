# Soluciona - Backend API 🛠️

O **Soluciona** é uma plataforma robusta desenvolvida em Spring Boot para conectar clientes a prestadores de serviços domésticos. Este repositório contém o código do backend, responsável pela gestão de utilizadores, anúncios de serviços, geolocalização, notificações push e integração com Inteligência Artificial.

## 🚀 Funcionalidades Principais

* **Autenticação JWT & Sincronização**: Integração com Supabase para autenticação via tokens JWT. O sistema sincroniza automaticamente os perfis de utilizador (`Profiles`) na base de dados local assim que deteta um novo ID no token.
* **Gestão de Anúncios de Serviços**: Criação, edição e remoção de anúncios (`ServicePost`) com suporte para múltiplas categorias, preços, descrições e galerias de fotos (armazenadas como JSONB no PostgreSQL).
* **Busca por Geolocalização**: Sistema de procura de profissionais "próximos de mim" baseado em coordenadas de latitude e longitude, calculando a distância real em quilómetros.
* **Fluxo de Pedidos (Workflow)**: Gestão completa do ciclo de vida de um pedido de serviço, desde o estado `pendente` até ser `aceite`, `rejeitado` ou `concluido`.
* **Notificações Push (FCM)**: Envio automático de notificações para o telemóvel dos utilizadores via Firebase Cloud Messaging quando há novas solicitações ou atualizações de estado.
* **Assistente Virtual IA**: Chatbot inteligente integrado com a API do **Google Gemini**, configurado para responder com a personalidade da marca em Português de Portugal.
* **Sistema de Reviews**: Permite que clientes avaliem os serviços prestados com notas e comentários.

## 💻 Tecnologias e Dependências

* **Java 17** & **Spring Boot 3.5.7**.
* **Spring Data JPA**: Abstração da camada de dados para PostgreSQL.
* **Spring Security**: Configuração de segurança stateless com filtros JWT customizados.
* **Firebase Admin SDK**: Para o motor de notificações push.
* **Google Gemini SDK**: Para o processamento de linguagem natural do chatbot.
* **Lombok**: Para redução de código boilerplate.
* **PostgreSQL**: Base de dados relacional com suporte a tipos JSONB.
* **Docker**: Contentorização pronta para deploy.

## 📡 API Endpoints (Resumo)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **`GET`** | `/api/services` | Lista todos os serviços ativos. |
| **`GET`** | `/api/services/search` | Busca por geolocalização (lat, lng, radius). |
| **`POST`** | `/api/requests` | Cria uma nova solicitação de serviço. |
| **`GET`** | `/api/requests/myOrders` | Lista pedidos feitos e recebidos pelo utilizador. |
| **`POST`** | `/api/chat` | Interação com o chatbot IA do Soluciona. |
| **`PUT`** | `/api/profiles/fcm-token` | Regista o token do dispositivo para notificações. |
