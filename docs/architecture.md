# Arquitetura — AlertaRótulo

| | |
|---|---|
| **Produto** | AlertaRótulo |
| **Referência de produto** | [`docs/product-brief.md`](product-brief.md), [`docs/prd/alerta-rotulo.md`](prd/alerta-rotulo.md) |
| **Referência visual** | [`docs/design/style-reference.md`](design/style-reference.md) (orientação para telas; não seguir à risca) |
| **Status** | Diretrizes técnicas do MVP |
| **Base normativa** | [Guide to app architecture](https://developer.android.com/topic/architecture) e [Architecture recommendations](https://developer.android.com/topic/architecture/recommendations) (Android Developers, atualizado 2026-10) |

Este documento define **como** o MVP é estruturado. Comportamento de telas e regras de negócio ficam no PRD. Aparência e tokens visuais usam o style reference como guia flexível.

---

## 1. Plataforma e runtime

| Item | Decisão |
|---|---|
| **Plataforma** | Android (telefone; layout phone-first) |
| **Linguagem** | Kotlin |
| **UI toolkit** | Jetpack Compose + Material 3 |
| **Entry point** | Single-Activity (`MainActivity`) |
| **applicationId** | `com.amdcruz.alertarotulo` |
| **minSdk** | 26 |
| **targetSdk / compileSdk** | 37 (conforme template atual do projeto) |
| **Backend próprio** | Nenhum |
| **Conta / auth** | Nenhum |

---

## 2. Princípios (Google — strongly recommended)

1. **Separação em camadas** — UI layer + Data layer claros; UI não acessa data sources diretamente.
2. **Repository como porta da data layer** — mesmo com uma única fonte por tipo de dado.
3. **Unidirectional Data Flow (UDF)** — ViewModel expõe estado; UI envia ações via métodos; **não** enviar eventos one-shot do ViewModel para a UI (tratar o evento no VM e refletir no `uiState`).
4. **Coroutines + Flow** — comunicação entre camadas.
5. **Compose + collectAsStateWithLifecycle** — coleta lifecycle-aware do estado.
6. **ViewModels em nível de tela** — não em componentes reutilizáveis; preferir `ViewModel` (não `AndroidViewModel`).
7. **Single-Activity + Navigation 3** — navegação entre telas do MVP.
8. **Injeção de dependências** — constructor injection; **Hilt** (app com várias telas e ViewModels).
9. **SSOT** — para histórico, a fonte da verdade é o armazenamento local; para consulta fresca, a OFF via repository.

### Domain layer

**Não incluir domain layer no MVP**, alinhado à orientação da Google (“recommended in big apps”). Regras simples (validação de código, debounce de scan, limite de 20 no histórico) ficam no **repository** ou no **ViewModel de tela**, conforme o caso. Introduzir use cases só se a lógica passar a ser reutilizada por vários ViewModels ou ficar complexa demais neles.

---

## 3. Visão das camadas

```text
┌─────────────────────────────────────────────┐
│                 UI layer                    │
│  Screens (Compose) · Navigation 3           │
│  Screen ViewModels · UiState (StateFlow)    │
└─────────────────────▲───────────────────────┘
                      │ ações / observação
┌─────────────────────┴───────────────────────┐
│                Data layer                   │
│  Repositories (porta única)                 │
│  Remote DS (Open Food Facts)                │
│  Local DS (Room — histórico)                │
│  On-device barcode (CameraX + ML Kit)*      │
└─────────────────────────────────────────────┘

* O pipeline de câmera/barcode vive na UI/device API;
  o valor lido (código) entra na data layer via repository de produto.
```

### 3.1 UI layer

- Telas Compose + Material 3.
- Visual: usar [`docs/design/style-reference.md`](design/style-reference.md) como **referência** (cores, tipografia, hierarquia). Não é especificação fechada — adaptar ao Material 3 e às restrições do MVP quando fizer sentido.
- Um **ViewModel por tela/destino** relevante.
- Expõe preferencialmente um `uiState: StateFlow<…UiState>` (data class ou sealed class quando estados forem exclusivos: Loading / Success / NotFound / NetworkError / …).
- Coleta com `collectAsStateWithLifecycle()`.
- Sem referência a `Activity`/`Context`/`Resources` dentro do ViewModel.
- Componentes reutilizáveis usam state holders simples (não ViewModel), com state hoisting.

### 3.2 Data layer

| Responsabilidade | Componente |
|---|---|
| Consulta de produto na OFF | `ProductRepository` + remote data source (Retrofit/OkHttp) |
| Histórico local (≤ 20, dedupe, snapshot) | `HistoryRepository` + local data source (Room) |
| Mapeamento API → modelo de app | Mapper na remote/data layer (testável em unidade) |

Regras:

- ViewModels dependem de **repositories**, nunca de Retrofit/DAO/ML Kit diretamente.
- Remote e local são **data sources** distintos; o repository orquestra (ex.: após sucesso remoto, persiste snapshot no histórico).
- Modelos de rede/DB não vazam para a UI sem necessidade: mapear para modelos de app / incluir no `UiState` só o que a tela precisa.

---

## 4. Módulo e pacotes

| Decisão | Detalhe |
|---|---|
| **Módulos Gradle** | Um único módulo `:app` (proporcional ao MVP) |
| **Organização** | Pacotes **por função/feature**, com subpacotes de camada quando útil |

Sugestão de árvore (conceitual — nomes podem ajustar na implementação):

```text
com.amdcruz.alertarotulo
├── AlertaRotuloApplication          // @HiltAndroidApp
├── MainActivity                     // single activity
├── core/
│   ├── di/                          // módulos Hilt
│   ├── network/                     // OkHttp, User-Agent, Retrofit setup
│   ├── database/                    // Room database
│   └── ui/theme/                    // Material 3 theme existente
├── scan/                            // câmera, permissão, digitação
│   ├── ui/
│   └── ...
├── product/                         // consulta OFF + resultado
│   ├── data/                        // repository, remote, mapper, models
│   └── ui/
├── history/                         // lista + reopen snapshot
│   ├── data/
│   └── ui/
└── navigation/                      // back stack / keys Navigation 3
```

Essa organização respeita “pacotes por função” do brief e mantém a fronteira UI/Data exigida pela Google.

---

## 5. Navegação

| Item | Decisão |
|---|---|
| **Biblioteca** | **Jetpack Navigation 3** (`navigation3-runtime` + `navigation3-ui`) |
| **Padrão** | Back stack explícito + `NavDisplay` / `entryProvider` |
| **Rotas** | Type-safe (`NavKey` quando fizer sentido para persistir back stack) |

Destinos previstos no MVP (conceitual):

1. Captura / entrada (câmera + atalho para digitar)
2. Digitação manual (pode ser destino ou modo da mesma tela)
3. Resultado do produto
4. Estados de falha podem ser **estados da mesma tela/ViewModel** (preferível a rotas extras), alinhado a UDF
5. Histórico
6. Detalhe a partir do histórico (mesmo UI de resultado, fonte = snapshot local)

> **Nota:** Navigation 3 é a escolha oficial do projeto (PRD + este documento + brief). Substitui a menção antiga a Navigation Compose (Nav2).

---

## 6. Bibliotecas e papéis

### 6.1 Obrigatórias no MVP

| Biblioteca | Papel |
|---|---|
| Kotlin + Coroutines | Linguagem e concorrência |
| Jetpack Compose + Material 3 | UI |
| Navigation 3 | Navegação multi-tela |
| Lifecycle ViewModel + `runtime-compose` | State holders de tela; `collectAsStateWithLifecycle` |
| StateFlow / Flow | Estado e streams |
| Hilt | DI (Application, ViewModels, repositories, Retrofit, Room) |
| Retrofit | Cliente HTTP tipado da OFF |
| OkHttp | Interceptor de **User-Agent** (nome do app + contato), timeouts, logging de debug se necessário |
| kotlinx.serialization | Serialização JSON da resposta OFF |
| Room | Persistência do histórico local |
| CameraX | Preview/análise de frames |
| ML Kit Barcode Scanning | Decodificação **on-device** (sem enviar imagem) |
| JUnit | Testes unitários |
| kotlinx-coroutines-test + Turbine | Teste de coroutines/Flow/StateFlow |
| MockK **ou fakes** | Doubles de teste — Google recomenda **preferir fakes**; MockK quando inevitável |

### 6.2 Explicitamente fora do MVP

| Biblioteca / recurso | Motivo |
|---|---|
| Coil / carregamento de imagem de produto | PRD: sem foto na v1 |
| Backend, Auth, Sync, Analytics invasivo | Fora do escopo |
| Catálogo offline completo da OFF | Fora do escopo |

---

## 7. Integrações

### 7.1 Open Food Facts (única integração de rede)

| Item | Decisão |
|---|---|
| **Endpoint** | `GET /api/v2/product/{código}` |
| **Auth** | Sem API key |
| **Identificação** | Header/User-Agent com **nome do app + contato** (política OFF), configurado no OkHttp |
| **O que sobe** | Apenas o código (path) + metadados HTTP |
| **O que não sobe** | Frames/imagens da câmera |
| **Mapeamento** | Resposta → modelo de app (nome, marca, alergênicos, ingredientes, Nutri-Score A–E ou ausente) |
| **Erros de domínio** | Produto inexistente ≠ falha de rede/HTTP (tipos distintos até o `UiState`) |

### 7.2 Leitura de código on-device

```text
CameraX (ImageAnalysis) → ML Kit Barcode → código normalizado
        → validação → ProductRepository.lookup(code)
```

- Imagem permanece no aparelho; não há upload nem persistência de frame para nuvem.
- Debounce / trava após leitura válida (regra de produto) antes de nova consulta do mesmo código.
- Apenas códigos de barras de produto (não QR).

### 7.3 Persistência local (histórico)

- Room como local data source.
- Snapshot dos campos exibidos + código + timestamp.
- Limite 20, mais recente no topo, dedupe por código (atualiza e sobe).
- Reabrir item = ler local **sem** chamada à OFF.

---

## 8. Fluxo de dados (UDF)

### Consulta nova (câmera ou digitação)

```text
UI action (código válido)
  → ViewModel
    → ProductRepository.lookup
      → RemoteDataSource (OFF)
    → em sucesso: HistoryRepository.saveOrUpdate(snapshot)
  → uiState (Loading | Success | NotFound | NetworkError)
UI observa uiState
```

### Reabrir histórico

```text
UI action (item selecionado)
  → ViewModel
    → HistoryRepository.getByCode / getById
  → uiState Success a partir do snapshot (sem remote)
```

---

## 9. DI (Hilt)

Escopos típicos:

| Componente | Escopo |
|---|---|
| `OkHttpClient`, `Retrofit`, API service | Singleton |
| Room `Database` / DAOs | Singleton |
| Repositories | Singleton (ou unscoped se stateless) |
| ViewModels | `@HiltViewModel` + escopo de navegação/tela |

Constructor injection em repositories e ViewModels. Módulos Hilt em `core/di`.

---

## 10. Concorrência e threading

- Chamadas de rede e IO de Room em dispatchers apropriados (via coroutines; Room/Retrofit suspend).
- `viewModelScope` para trabalho ligado à tela.
- Evitar bloquear Main thread com decode ML Kit: pipeline CameraX/ML Kit no fluxo de análise recomendado pelas libs (callback/executor de análise).

---

## 11. Internacionalização

- Resources `values` (en, fallback) e `values-pt-rBR` (ou `values-pt`).
- Idioma segue o do sistema: pt* → pt-BR; en* → en; demais → en.
- Conteúdo textual vindo da OFF **não** é traduzido pelo app.

---

## 12. Acessibilidade (impacto técnico)

- Semantics / content descriptions nos elementos interativos e blocos críticos (alergênicos).
- Ordem de foco coerente com a hierarquia do PRD (alergênicos antes do restante no resultado).
- Contraste e tipografia Material 3; respeitar font scale do sistema nas telas principais.
- Estados de erro não dependem só de cor.
- Detalhamento “feito vs. fora” permanece no README (spec de produto), não aqui.

---

## 13. Testes (mínimo alinhado ao PRD + Google)

| Área | O que cobrir |
|---|---|
| **Mapper / remote parsing** | Produto completo, campos parciais, produto inexistente |
| **ViewModel** | Estado quando produto não existe; loading → erro de rede; sucesso |
| **History repository** (recomendado) | Limite 20, dedupe, ordenação |
| **Doubles** | Preferir **fakes** de repository/data source; MockK pontual |
| **StateFlow** | Assert em `value` / Turbine conforme o caso; política `WhileSubscribed` quando usar `stateIn` |

UI navigation / Macrobenchmark / Baseline Profiles são recomendações Google de maturidade — **opcionais no MVP**, não bloqueiam a v1.

---

## 14. Build e qualidade de release

| Item | Decisão |
|---|---|
| **R8 / otimização** | Manter habilitado em release (template já aponta optimization) |
| **Segredos** | Nenhum API key; contato do User-Agent pode ser constante/build config não secreta |
| **Permissões** | `CAMERA` (+ `INTERNET`); fluxo de negação tratado na UI |

---

## 15. Decisões explícitas e trade-offs

| Decisão | Escolha | Razão |
|---|---|---|
| Camadas | UI + Data (sem Domain) | MVP pequeno; Google reserva Domain para apps maiores / reuso |
| Módulos | Só `:app` | Evitar over-engineering; feature packages bastam |
| Navegação | Navigation 3 | Recomendação atual para apps Compose novos |
| Histórico offline | Snapshot Room | Critério de produto; sem catálogo OFF offline |
| Imagens | Sem Coil na v1 | Escopo fechado do PRD |
| Scan | CameraX + ML Kit on-device | Privacidade + latência; rede só para o produto |
| Estado de UI | StateFlow + UDF | Recommendations oficiais; eventos VM→UI evitados |

---

## 16. Referências

- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [Architecture recommendations](https://developer.android.com/topic/architecture/recommendations)
- [UI layer](https://developer.android.com/topic/architecture/ui-layer)
- [Data layer](https://developer.android.com/topic/architecture/data-layer)
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3)
- [Hilt in Android](https://developer.android.com/training/dependency-injection/hilt-android)
- Open Food Facts API v2 — product endpoint + política de User-Agent
- [`docs/design/style-reference.md`](design/style-reference.md) — referência visual das telas (flexível)
