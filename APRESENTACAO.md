# Apresentação do Projeto: Journey Planner com Alta Disponibilidade e Gateway

> **Documento de Apresentação e Arquitetura**  
> **Disciplina:** Backend / Arquitetura de Software — PUC Minas  
> **Integrantes:** Chiardelli & Rafael  

---

## 1. Visão Geral da Aplicação
O **Journey Planner** é uma API REST desenvolvida em **Java com Spring Boot** voltada para o planejamento e organização de viagens em grupo.

### Principais Funcionalidades:
- **Gestão de Viagens (`/trips`):** Criação, consulta de detalhes, atualização de datas/destinos e confirmação da viagem.
- **Participantes (`/participants`):** Envio de convites por e-mail para confirmação de presença no evento.
- **Atividades (`/activities`):** Cronograma de passeios e atividades vinculadas a uma viagem específica.
- **Links Úteis (`/links`):** Armazenamento de links importantes (reservas de hospedagem, passagens, atrações).


### Kong
Como gateway e load balancer vamos utilizar do Kong, que pode ser iniciado na sua máquina através do arquivo docker `kong-docker-compose.yaml`

---

## 2. O Cenário de Arquitetura: Por que rodar 2 instâncias?

Para simular um ambiente de produção resiliente e escalável, a aplicação não roda como um monólito isolado em uma única porta.

### Topologia com API Gateway (Kong & Konga)
```
                [ Cliente / Bruno / Frontend ]
                               │
                               ▼
                   ┌───────────────────────┐
                   │   API Gateway (Kong)  │
                   │   Gerenciado via Konga│
                   └───────────────────────┘
                               │
             ┌─────────────────┴─────────────────┐
             │ (Balanceamento Round-Robin / Rota) │
             ▼                                   ▼
  ┌─────────────────────┐             ┌─────────────────────┐
  │ Instância 1 (Porta) │             │ Instância 2 (Porta) │
  │ Spring Boot (App A) │             │ Spring Boot (App B) │
  └─────────────────────┘             └─────────────────────┘
             │                                   │
             └─────────────────┬─────────────────┘
                               ▼
                  ┌─────────────────────────┐
                  │   Banco H2 Compartilhado│
                  │   (Modo Arquivo / File) │
                  └─────────────────────────┘
```

1. **Balanceamento de Carga (Load Balancing):** O Gateway recebe as requisições públicas e distribui o tráfego entre a **Instância 1** e a **Instância 2**.
2. **Desacoplamento e Segurança:** O cliente externo nunca conhece as portas internas reais das instâncias; ele consome apenas a rota exposta pelo Kong.
3. **Observabilidade com Logs Estruturados:** Foram adicionados logs detalhados nos Controllers e Services. Assim, ao disparar requisições pelo Gateway, é possível observar no terminal qual das duas instâncias atendeu a chamada.

---

## 3. Ponto Crítico de Arquitetura: H2 em Modo Arquivo (`file-based`) vs Memória (`in-memory`)

> **Por que NÃO usamos `jdbc:h2:mem` (em memória)?**

* **O Problema da Memória Isolada:**  
  Se utilizássemos `jdbc:h2:mem:planner`, cada instância Spring Boot subiria seu próprio banco de dados isolado na memória RAM do seu processo.  
  Se um usuário fizesse um `POST /trips` atendido pela **Instância 1**, o registro seria gravado apenas na memória da Instância 1. Na requisição seguinte (`GET /trips/{id}`), caso o Gateway balanceasse para a **Instância 2**, ela retornaria **404 Not Found**, pois a memória dela estaria vazia!

* **A Solução (H2 File-based com Auto-Server):**  
  Configuramos a URL de conexão para apontar para um arquivo compartilhado em disco:
  ```properties
  spring.datasource.url=jdbc:h2:file:./data/planner;AUTO_SERVER=TRUE
  ```
  O parâmetro `AUTO_SERVER=TRUE` permite que múltiplos processos da JVM (Instância 1 e Instância 2) se conectem e leiam/escrevam concorrentemente no mesmo arquivo físico de banco de dados, garantindo a **consistência de estado** entre as duas instâncias sem exigir a subida de um servidor PostgreSQL/MySQL externo.

---

## 4. Roteiro Sugerido para a Apresentação (Script de Fala)

Você pode seguir esta ordem durante a fala:

1. **Abertura (30 seg):**
   > *"Vamos apresentar a evolução do nosso projeto Journey Planner, focando não apenas nas regras de negócio, mas nos desafios de arquitetura de backend e distribuição de carga."*

2. **Apresentação do Negócio (1 min):**
   > *"O sistema permite cadastrar viagens, convidar participantes, adicionar links e cronograma de atividades com validação de contratos e datas."*

3. **Arquitetura Distribuída & Gateway (1 min 30 seg):**
   > *"Para este laboratório, subimos a aplicação em duas instâncias simultâneas atrás do API Gateway Kong, configurado visualmente pelo Konga. O Gateway fica responsável por receber o tráfego e fazer o proxy/balanceamento das rotas para as instâncias."*

4. **O desafio do Banco H2 (1 min):**
   > *"Um desafio clássico ao rodar duas instâncias locais é a persistência: com banco in-memory, uma instância não enxerga os dados gravados pela outra. Por isso, configuramos o H2 em modo arquivo (`file-based`) com a flag `AUTO_SERVER=TRUE`. Dessa forma, se a Instância 1 recebe o cadastro da viagem, a Instância 2 consegue consultar e listar essa mesma viagem sem perda de estado."*

5. **Demonstração Prática**
---

## 5. Relação com os Conceitos da Disciplina (Estudo de Caso & ADR-002)

- **202 Accepted & Protocolos:** Requisições assíncronas desacoplam o processamento pesado da resposta ao cliente.
- **Idempotência & Chave de Negócio:** Em ambientes com múltiplas instâncias e retentativas de rede, chaves de idempotência evitam criar registros duplicados.
- **Transparência de Localização:** O Gateway isola a topologia da rede; o consumidor consome rotas unificadas enquanto as instâncias podem escalar horizontalmente.
