# PRD — AlertaRótulo

> Tipo: PRD inicial · Data: 2026-11-06
> **Status:** Implementada
>
> <!-- Valores possíveis: "Aguardando implementação" | "Implementada". Atualize para "Implementada" quando todas as specs estiverem concluídas. -->

## 1. Visão geral

AlertaRótulo é um app Android que permite à pessoa apontar a câmera para o código de barras de um alimento (ou digitar o número), ler o código **no aparelho**, consultar a base pública Open Food Facts e ver, com prioridade, **alergênicos**, ingredientes e Nutri-Score — em interface com texto grande, contraste e foco pensados para acessibilidade. Não há conta, não há servidor próprio e a imagem do código nunca sai do dispositivo. A interface está disponível em **português (Brasil)** e **inglês**.

## 2. Problema que resolve

Ler rótulos na prateleira ou em casa é lento, visualmente denso e pouco acessível para quem usa leitor de tela, precisa de tipografia maior ou de contraste reforçado. Além disso, muitos apps enviam a imagem do código para a nuvem só para extrair um número que o aparelho já consegue ler — gerando atrito de privacidade, latência e falha quando a rede é ruim.

## 3. Público-alvo

Pessoa que consulta alergênicos e ingredientes de alimentos na prateleira ou em casa, incluindo quem usa TalkBack ou depende de tipografia/contraste reforçados.

## 4. Objetivo do recorte atual

Entregar o MVP completo e usável: captura (câmera ou digitação) → consulta OFF → resultado priorizando alergênicos → erros claros → histórico local das últimas 20 consultas (reabrível offline) → UI em PT-BR e EN → README de acessibilidade → testes mínimos de mapeamento da API e do estado “produto não existe”.

## 5. Funcionalidades

**Essenciais:**

- Ler código de barras pela câmera (reconhecimento no aparelho).
- Digitar o número do código manualmente.
- Buscar produto na Open Food Facts e exibir nome, marca, alergênicos, ingredientes e Nutri-Score (A–E) quando a base tiver.
- Tratar com saída clara: produto não encontrado, falha de rede e permissão de câmera negada.
- Guardar as últimas 20 consultas no aparelho; reabrir o detalhe a partir do snapshot local (sem rede).
- Interface em PT-BR e EN.
- Acessibilidade das telas principais (texto, contraste, ordem de foco); README lista o feito e o que ficou de fora para TalkBack.
- Testes do mapeamento da resposta da API e do ViewModel quando o produto não existe.

**Desejáveis:**

- Não se aplica neste recorte. Tudo listado acima é essencial do MVP; o que não entra está em “Fora do escopo”.

## 6. Fora do escopo

- Lista de compras.
- Conta de usuário / login / perfil.
- Qualquer gravação ou upload de imagem na nuvem.
- Exibição de foto do produto (mesmo que a base tenha URL).
- Backend próprio, analytics invasivo ou sync multi-dispositivo.
- Personalização de alergias do usuário (filtros salvos, alertas push).
- Catálogo offline completo da Open Food Facts (download de base local).
- Botão “atualizar da rede” ao reabrir item do histórico (v1 abre só o snapshot salvo).
- Contribuição/edição de produtos na Open Food Facts.
- Escaneamento de QR Code (apenas códigos de barras de produto / GTIN).

## 7. Regras de negócio

- Regra 1: A leitura do código de barras ocorre **somente no aparelho**. Apenas o identificador numérico (e metadados HTTP necessários) é enviado à Open Food Facts.
- Regra 2: A imagem capturada pela câmera **não é persistida** para upload nem enviada a qualquer servidor.
- Regra 3: Não há conta, autenticação nem servidor próprio do AlertaRótulo.
- Regra 4: Fonte de dados do produto: Open Food Facts, `GET /api/v2/product/{código}`, sem API key. Toda requisição deve identificar o app via User-Agent com **nome do app + contato** (política da OFF).
- Regra 5: Campos ausentes na base devem aparecer como **“Não informado”** (ou equivalente localizado), nunca como se fossem produto inexistente.
- Regra 6: **Produto não encontrado**, **falha de rede** e **permissão de câmera negada** são estados distintos, cada um com mensagem e ação clara.
- Regra 7: O app **não interpreta clinicamente** alergênicos: apresenta os dados da fonte; não inventa alertas médicos. Linguagem cautelosa (não substitui a leitura do rótulo físico).
- Regra 8: Histórico guarda até **20** consultas bem-sucedidas (produto encontrado). Ao exceder, remove a mais antiga. Ordenação: mais recente no topo.
- Regra 9: Se o mesmo código for consultado de novo com sucesso, **atualiza o snapshot** e **sobe o item para o topo** (não duplica entradas).
- Regra 10: Reabrir item do histórico mostra o **snapshot salvo no aparelho**, sem nova chamada de rede.
- Regra 11: Nutri-Score exibido como letra **A–E** quando a base tiver; caso contrário, “Não informado”.
- Regra 12: Interface disponível em **pt-BR** e **en**. O idioma segue o do sistema quando for português ou inglês; caso contrário, fallback para **inglês**.
- Regra 13: Conteúdo textual do produto (nome, ingredientes, alergênicos) é exibido **como retornado pela base**, sem tradução automática pelo app.
- Regra 14: Códigos aceitos na digitação/consulta: identificadores numéricos de produto alimentício usuais (ex.: EAN-8, EAN-13, UPC-A), após normalização (apenas dígitos). Códigos inválidos ou vazios não disparam consulta.
- Regra 15: Na câmera, leituras repetidas do **mesmo** código em sequência curta não disparam múltiplas consultas (debounce / trava após leitura válida até o usuário voltar ou iniciar nova busca).

### Decisão confirmada — reabrir histórico (pesquisa)

**Decisão:** opção A — abrir o detalhe já salvo no aparelho.

**Por quê (recomendado neste cenário):**

- O próprio brief exige histórico consultável **sem rede** após a consulta original (uso típico: mercado com sinal ruim).
- Apps de lookup por código de barras e clientes OFF costumam tratar resultado bem-sucedido como **cache local** e o histórico como lista sobre esse cache; refetch sempre online quebra o caso offline e gera latência/limites desnecessários.
- Catálogo offline completo da OFF é pesado demais para este MVP; snapshot das últimas 20 consultas entrega o valor offline sem essa complexidade.
- “Atualizar da rede” ao abrir o histórico fica **fora do escopo** da v1 (pode ser evolução posterior).

## 8. Fluxos principais

### Fluxo 1 — Consulta pela câmera (sucesso)

1. Pessoa abre o app na tela inicial de captura.
2. Concede permissão de câmera (se ainda não concedida).
3. Aponta para o código de barras; o app lê o código no aparelho.
4. App consulta a Open Food Facts com o código.
5. Em loading, a pessoa vê indicação de busca em andamento.
6. Com produto encontrado, navega para o resultado (alergênicos em destaque).
7. A consulta bem-sucedida entra/atualiza o histórico local.

### Fluxo 2 — Consulta por digitação (sucesso)

1. Pessoa escolhe digitar o código.
2. Informa o número e confirma.
3. App valida o formato; se inválido, impede a busca e explica.
4. Segue como nos passos 4–7 do Fluxo 1.

### Fluxo 3 — Produto não encontrado

1. Após captura ou digitação válida, a API indica ausência do produto.
2. App mostra estado específico “produto não encontrado”, com opção de tentar outro código (câmera ou digitação).
3. **Não** grava no histórico de consultas bem-sucedidas.

### Fluxo 4 — Falha de rede

1. Após captura ou digitação válida, a requisição falha (sem conexão, timeout, erro de servidor).
2. App mostra estado específico de falha de rede, com opção de tentar de novo e/ou voltar à captura/digitação.
3. **Não** grava no histórico.

### Fluxo 5 — Permissão de câmera negada

1. Pessoa tenta usar a câmera sem permissão (ou nega no diálogo do sistema).
2. App explica que a câmera é necessária para escanear e oferece: abrir configurações do sistema **ou** digitar o código.
3. Digitação permanece disponível independentemente da permissão.

### Fluxo 6 — Reabrir histórico

1. Pessoa abre a lista das últimas consultas.
2. Seleciona um item.
3. App abre o detalhe com os dados do snapshot local, **sem** chamar a rede.
4. Funciona com o aparelho offline.

## 9. Critérios de aceite

- O usuário consegue obter resultado (ou erro claro) a partir da **câmera** e da **digitação**.
- Em resultado com dados, alergênicos aparecem antes de ingredientes e demais informações.
- Nutri-Score aparece como A–E quando existir; senão, “Não informado” (localizado).
- Produto não encontrado, falha de rede e permissão negada têm UX distinta e acionável.
- Histórico lista até 20 sucessos; reabrir um item funciona offline com o snapshot salvo.
- Mesmo código consultado de novo atualiza e sobe no histórico (sem duplicar).
- UI responde em pt-BR e en conforme a regra de idioma.
- README documenta o que foi feito para TalkBack e o que ficou de fora.
- Existe cobertura de teste do mapeamento da resposta da API e do comportamento quando o produto não existe.
- Requisições à OFF enviam User-Agent com nome do app e contato.
- O sistema não deve enviar imagem do código para nenhum servidor.
- O sistema não deve exigir conta.

## 10. Stack

Em alto nível, alinhada a [`docs/architecture.md`](../architecture.md) e às recomendações atuais de arquitetura Android da Google. O template do projeto já usa Kotlin + Compose + Material 3.

- **Plataforma:** Android, single-Activity, minSdk 26
- **UI:** Jetpack Compose, Material 3
- **Navegação:** Jetpack Navigation 3 (não Navigation Compose / Nav2)
- **Estado / async:** ViewModel (nível de tela), StateFlow, Coroutines + Flow; coleta com `collectAsStateWithLifecycle`
- **Arquitetura:** UI layer + Data layer (repositories); **sem** domain layer no MVP
- **DI:** Hilt
- **Rede:** Retrofit, OkHttp (User-Agent com nome do app + contato), kotlinx.serialization
- **Local:** Room (histórico / snapshot)
- **Scan on-device:** CameraX + ML Kit Barcode Scanning
- **Testes:** JUnit, Turbine, kotlinx-coroutines-test; preferir **fakes**; MockK quando necessário
- **Imagens:** Coil **não** na v1 (sem foto de produto)
- **Módulos / pacotes:** um módulo `:app`; pacotes por função/feature

Detalhes de camadas, pacotes, DI e integrações: ver `docs/architecture.md`.

**Referência visual:** [`docs/design/style-reference.md`](../design/style-reference.md) orienta cores, tipografia e linguagem visual na criação das telas. É um guia de apoio — não precisa ser seguido à risca; prevalecem acessibilidade do PRD, Material 3 e decisões do `architecture.md` quando houver conflito.

## 11. Justificativa da stack

Segue o guia oficial de arquitetura Android (UI + Data, UDF, repositories, Compose). Navigation 3 é a recomendação atual para apps Compose novos com mais de uma tela. CameraX + ML Kit mantêm o scan no aparelho. Room cobre o histórico sem backend. Retrofit/OkHttp permitem User-Agent explícito exigido pela OFF. Hilt cobre DI com várias telas e ViewModels. Domain layer fica de fora por ser MVP pequeno. Testes de mapper e ViewModel cobrem os pontos de risco sem inflar o escopo.

## 12. Fases de construção

### Fase 1 — Consulta do produto

Objetivo: buscar na OFF por código, mapear campos essenciais e distinguir sucesso, não encontrado e falha de rede.  
Specs:

- Spec 01 — Consultar produto por código
- Spec 02 — Estados de falha da consulta

### Fase 2 — Entrada do código

Objetivo: obter o código via digitação ou câmera on-device, com permissão tratada.  
Specs:

- Spec 03 — Digitar código manualmente
- Spec 04 — Ler código pela câmera
- Spec 05 — Permissão de câmera negada

### Fase 3 — Resultado acessível

Objetivo: apresentar o produto com prioridade a alergênicos, Nutri-Score e acessibilidade das telas principais.  
Specs:

- Spec 06 — Exibir resultado do produto
- Spec 07 — Acessibilidade das telas principais

### Fase 4 — Histórico local

Objetivo: persistir últimas consultas e reabrir offline.  
Specs:

- Spec 08 — Listar e manter histórico local
- Spec 09 — Reabrir consulta do histórico

### Fase 5 — Idiomas e documentação

Objetivo: PT-BR/EN na interface e transparência de acessibilidade no README.  
Specs:

- Spec 10 — Idiomas da interface (PT-BR e EN)
- Spec 11 — Documentação TalkBack no README

## 13. Specs funcionais detalhadas

> Cada spec deve ser autossuficiente: um agente de codificação vai ler SÓ esta spec (mais as dependências) para montar o plano técnico e implementar. Preencha todos os campos; se um não se aplica, escreva "Não se aplica" e o porquê.

### Spec 01 — Consultar produto por código

- **Fase:** 1 — Consulta do produto
- **Objetivo (o quê):** Dado um código de barras válido, o app busca o produto na Open Food Facts e obtém nome, marca, alergênicos, ingredientes e Nutri-Score (quando existirem), pronto para exibição.
- **Intenção (por quê):** É o coração do valor do app: transformar um número de código em informação útil de rótulo, usando a rede só para o dado do produto.
- **Contexto:** Projeto novo (template Android). Fonte: OFF `GET /api/v2/product/{código}`. Sem chave. User-Agent obrigatório (nome do app + contato). Sem foto na v1.
- **Atores:** Pessoa usuária (indiretamente, após captura/digitação); sistema ao receber um código válido.
- **Descrição do comportamento:** O sistema normaliza o código (apenas dígitos), dispara a consulta à OFF, interpreta a resposta e produz um resultado de domínio: produto encontrado (com campos possivelmente parciais) ou produto inexistente. Em sucesso parcial (produto existe mas alguns campos vazios), os campos faltantes ficam como não informados — não como erro. Toda requisição identifica o cliente conforme a política da OFF.
- **Entradas e saídas:**
  - Entrada: código de produto normalizado (string numérica).
  - Saída sucesso: produto com nome, marca, lista/texto de alergênicos, ingredientes, Nutri-Score (A–E ou ausente).
  - Saída “não existe”: sinalização explícita de produto não encontrado.
  - Saída falha técnica: sinalização de erro de rede/serviço (detalhada na Spec 02).
- **Dados/entidades envolvidos (conceitual):** ProdutoConsultado: código, nome, marca, alergênicos, ingredientes, nutriScore (A–E ou nulo), momento da consulta.
- **Estados e transições:** Idle → Carregando → Sucesso | Não encontrado | Erro de rede. De Erro, nova tentativa pode voltar a Carregando.
- **Regras de negócio:** Regras 1, 4, 5, 7, 13, 14.
- **Validações:** Código deve passar na validação de formato antes da chamada (detalhe na Spec 03; esta spec assume código já válido).
- **Fluxo do usuário (passo a passo):**
  1. Um código válido é fornecido (câmera ou digitação).
  2. Usuário vê estado de carregamento.
  3. Sistema consulta a OFF.
  4. Em sucesso, os dados ficam disponíveis para a tela de resultado (Spec 06) e para o histórico (Spec 08).
- **Casos de borda e erros:** Campos nulos/vazios → “Não informado”. Resposta de produto inexistente → Spec 02. Timeout/HTTP de erro/sem conexão → Spec 02. Não deve tratar campos vazios como “não encontrado”.
- **Impacto no existente:** Nenhum (produto novo).
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado um código existente na OFF com alergênicos e Nutri-Score, Quando a consulta é feita, Então o sistema obtém esses campos preenchidos.
  - Dado um código existente sem Nutri-Score, Quando a consulta é feita, Então nutriScore fica ausente (não informado), sem falhar a consulta.
  - Dado qualquer consulta à OFF, Quando a requisição é enviada, Então o User-Agent contém o nome do app e um contato.
  - Dado o mapeamento da resposta da API, Quando os testes de mapeamento rodam, Então cobrem produto completo, campos parciais e produto inexistente.
- **Definição de pronto:** Consulta por código válido retorna modelo de produto ou “não encontrado”; User-Agent conforme política; testes de mapeamento da API passando.
- **Dependências:** Nenhuma.
- **Fora do escopo desta spec:** UI de resultado, câmera, histórico, foto, contribuição OFF.

### Spec 02 — Estados de falha da consulta

- **Fase:** 1 — Consulta do produto
- **Objetivo (o quê):** Distinguir e comunicar “produto não encontrado” versus “falha de rede/serviço”, com ações claras para a pessoa continuar.
- **Intenção (por quê):** Na prateleira, misturar “não está na base” com “sem internet” gera desconfiança e abandono. Erros distintos permitem a ação certa (outro código vs. tentar de novo).
- **Contexto:** Depende do resultado da Spec 01. Permissão de câmera é Spec 05 (não misturar aqui).
- **Atores:** Pessoa usuária.
- **Descrição do comportamento:** Quando a OFF indica ausência do produto, o app mostra estado de não encontrado, explica em linguagem simples e oferece voltar a escanear/digitar. Quando a requisição falha por conectividade, timeout ou erro de servidor, mostra estado de falha de rede com opção de **tentar novamente** (mesmo código) e de voltar à entrada. Nenhum desses estados grava histórico de sucesso.
- **Entradas e saídas:** Entrada: resultado “não encontrado” ou erro técnico da Spec 01. Saída: UI/estado de erro correspondente + ações (nova busca, retry).
- **Dados/entidades envolvidos (conceitual):** TipoDeFalhaDaConsulta: NaoEncontrado | RedeOuServico; código tentado (para retry).
- **Estados e transições:** Carregando → Não encontrado | Erro de rede. De Erro de rede + “Tentar novamente” → Carregando. De qualquer falha + “Nova busca” → fluxo de entrada (Specs 03/04).
- **Regras de negócio:** Regra 6; não gravar histórico nesses casos (Regra 8).
- **Validações:** Não se aplica (não há formulário novo).
- **Fluxo do usuário (passo a passo):**
  1. Consulta termina sem sucesso.
  2. Pessoa lê a mensagem específica do tipo de falha.
  3. Escolhe retry (só rede) ou nova busca.
- **Casos de borda e erros:** Retry com rede ainda indisponível → permanece/retorna ao estado de falha de rede. Não encontrado não oferece “retry idêntico” como se fosse rede (pode oferecer nova busca). Mensagens localizadas (Spec 10).
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado código válido inexistente na OFF, Quando a consulta termina, Então o usuário vê estado de produto não encontrado (não o de rede).
  - Dado ausência de conexão, Quando a consulta é tentada, Então o usuário vê estado de falha de rede com ação de tentar novamente.
  - Dado produto não encontrado, Quando o fluxo termina, Então nada é adicionado ao histórico de sucessos.
  - Dado o ViewModel/estado de apresentação no cenário “produto não existe”, Quando os testes desse cenário rodam, Então o estado exposto é o de não encontrado (não sucesso, não rede).
- **Definição de pronto:** Dois estados de falha distintos verificáveis na UI e no teste de estado “produto não existe”.
- **Dependências:** Spec 01.
- **Fora do escopo desta spec:** Permissão de câmera; layout final de acessibilidade profunda (Spec 07).

### Spec 03 — Digitar código manualmente

- **Fase:** 2 — Entrada do código
- **Objetivo (o quê):** Permitir informar o número do código sem câmera e iniciar a consulta.
- **Intenção (por quê):** Cobertura quando a câmera é negada, o código está danificado ou a pessoa prefere digitar; é o fallback obrigatório do MVP.
- **Contexto:** Alternativa à Spec 04; alimenta Spec 01.
- **Atores:** Pessoa usuária.
- **Descrição do comportamento:** A pessoa acessa a entrada manual, digita o código, confirma. O app aceita apenas dígitos (normaliza removendo espaços/hífens). Valida comprimento/formato aceito para códigos de produto alimentício usuais. Se inválido, não chama a rede e explica o problema. Se válido, inicia a consulta (Spec 01).
- **Entradas e saídas:** Entrada: texto digitado. Saída: código normalizado válido → consulta; ou mensagem de validação.
- **Dados/entidades envolvidos (conceitual):** CodigoProduto (string numérica normalizada).
- **Estados e transições:** Editando → Inválido (inline) | Submetendo/Carregando (Spec 01).
- **Regras de negócio:** Regra 14; digitação disponível mesmo sem permissão de câmera (Regra 6 / Fluxo 5).
- **Validações:** Vazio → inválido. Não numérico após normalização → inválido. Comprimento fora dos formatos aceitos → inválido. Checksum inválido (quando aplicável ao formato) → inválido com mensagem clara.
- **Fluxo do usuário (passo a passo):**
  1. Abre entrada manual.
  2. Digita o código.
  3. Confirma.
  4. Se válido, vê loading e segue para resultado ou falha (Specs 01/02/06).
- **Casos de borda e erros:** Colar texto com espaços; zeros à esquerda relevantes ao formato; código muito longo; confirmação repetida enquanto carrega (ignorar segunda submissão).
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado um EAN-13 válido, Quando a pessoa confirma, Então a consulta é iniciada.
  - Dado campo vazio ou código inválido, Quando confirma, Então não há chamada de rede e a validação é anunciada/visível.
  - Dado permissão de câmera negada, Quando a pessoa usa digitação, Então o fluxo funciona normalmente.
- **Definição de pronto:** Entrada manual valida, consulta e integra com estados de sucesso/falha.
- **Dependências:** Spec 01 (e Spec 02 para falhas).
- **Fora do escopo desta spec:** Câmera; histórico.

### Spec 04 — Ler código pela câmera

- **Fase:** 2 — Entrada do código
- **Objetivo (o quê):** Ler códigos de barras de alimento pela câmera, exclusivamente no aparelho, e iniciar a consulta com o valor lido.
- **Intenção (por quê):** É o caminho principal de uso na prateleira, com privacidade (imagem não sobe) e menor atrito.
- **Contexto:** Requer permissão (Spec 05). Após leitura válida → Spec 01. Sem QR. Sem persistir frame para upload.
- **Atores:** Pessoa usuária.
- **Descrição do comportamento:** Com permissão concedida, a pessoa vê o preview da câmera e orientação simples para enquadrar o código. Ao detectar um código de barras de produto válido, o app feedbacka a leitura (ex.: confirmação breve), aplica debounce para não repetir o mesmo código em rajada, e inicia a consulta. A imagem/frame usado na detecção não é enviada pela rede nem salva como upload.
- **Entradas e saídas:** Entrada: frames da câmera processados no device. Saída: código normalizado → Spec 01.
- **Dados/entidades envolvidos (conceitual):** CodigoProduto; evento de leitura (código + instante).
- **Estados e transições:** Aguardando leitura → Código detectado → Carregando consulta. Após sair do resultado e voltar, volta a Aguardando leitura.
- **Regras de negócio:** Regras 1, 2, 15; apenas códigos de barras de produto (não QR).
- **Validações:** Código lido passa pela mesma normalização/validação conceitual da Spec 03 antes da consulta; leitura inválida/não suportada é ignorada ou avisada sem chamar a rede.
- **Fluxo do usuário (passo a passo):**
  1. Abre captura com permissão ok.
  2. Enquadra o código.
  3. App lê no aparelho e confirma.
  4. Segue para loading → resultado ou falha.
- **Casos de borda e erros:** Baixa luz / código danificado → continua aguardando sem erro falso de rede. Leituras duplicadas do mesmo código → uma consulta. Troca rápida para outro produto → nova consulta do novo código após liberar a trava de debounce conforme regra.
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado permissão concedida e um código legível, Quando o código é detectado, Então a consulta inicia com esse código e nenhum frame é enviado a servidor.
  - Dado o mesmo código detectado várias vezes seguidas, Quando o preview continua ativo, Então não são disparadas múltiplas consultas idênticas.
  - Dado um QR Code, Quando aparece na câmera, Então não inicia consulta de produto.
- **Definição de pronto:** Scan on-device estável o bastante para o fluxo feliz + debounce; privacidade da imagem respeitada.
- **Dependências:** Spec 01; Spec 05 para o caminho sem permissão.
- **Fora do escopo desta spec:** Foto do produto; galeria; flash avançado como feature.

### Spec 05 — Permissão de câmera negada

- **Fase:** 2 — Entrada do código
- **Objetivo (o quê):** Quando a câmera não pode ser usada, explicar o motivo e oferecer caminhos claros (configurações ou digitação).
- **Intenção (por quê):** Negação de permissão é comum; sem saída clara o app parece quebrado. Digitação garante que o MVP continue útil.
- **Contexto:** Complementa Spec 04; digitação é Spec 03.
- **Atores:** Pessoa usuária; sistema Android de permissões.
- **Descrição do comportamento:** Se a permissão ainda não foi pedida, o app solicita no momento de usar a câmera. Se negada (ou negada em definitivo), a área de câmera não fica em branco silencioso: mostra mensagem clara, CTA para abrir as configurações do app no sistema, e CTA/atalho para digitar o código. Não bloquear o app inteiro.
- **Entradas e saídas:** Entrada: status da permissão. Saída: UI de bloqueio da câmera + navegação para settings ou digitação.
- **Dados/entidades envolvidos (conceitual):** StatusPermissaoCamera: Concedida | Negada | NegadaPermanentemente (conceitual).
- **Estados e transições:** Sem permissão → Diálogo do sistema → Concedida (Spec 04) | Negada (esta spec). De Negada → Settings → retorno ao app → reavaliar.
- **Regras de negócio:** Regra 6.
- **Validações:** Não se aplica.
- **Fluxo do usuário (passo a passo):**
  1. Tenta usar câmera sem permissão.
  2. Vê pedido do sistema ou tela de negação.
  3. Escolhe configurações ou digitação.
- **Casos de borda e erros:** Negação permanente (“não perguntar de novo”) → enfatizar caminho para configurações. Retorno das settings com permissão concedida → câmera passa a funcionar sem reinstalar.
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado permissão negada, Quando a pessoa tenta a câmera, Então vê explicação + opção de digitar e opção de abrir configurações.
  - Dado permissão negada, Quando usa digitação, Então consegue consultar produto normalmente.
- **Definição de pronto:** Estado de permissão negada distinto, acionável e localizado.
- **Dependências:** Spec 03 (fallback); Spec 04 (caminho feliz).
- **Fora do escopo desta spec:** Outras permissões (armazenamento, etc.).

### Spec 06 — Exibir resultado do produto

- **Fase:** 3 — Resultado acessível
- **Objetivo (o quê):** Mostrar o produto encontrado com ordem de prioridade: alergênicos → ingredientes → demais dados (nome/marca/Nutri-Score), sem foto.
- **Intenção (por quê):** Na decisão de “posso comer/usar?”, alergênicos importam primeiro; a hierarquia visual/semântica reduz risco de a informação crítica ficar enterrada.
- **Contexto:** Consome ProdutoConsultado da Spec 01. Sem Coil/foto (decisão de produto da v1). Orientação visual em `docs/design/style-reference.md` (flexível).
- **Atores:** Pessoa usuária.
- **Descrição do comportamento:** Após sucesso, a tela de resultado apresenta: identificação (nome e marca), bloco destacado de alergênicos, ingredientes, Nutri-Score A–E ou “Não informado”. Campos ausentes usam o rótulo de não informado. Deve ser possível voltar para nova busca. Conteúdo do produto como veio da base (sem traduzir). Pode inspirar-se no style reference para hierarquia e contraste, sem copiar literalmente.
- **Entradas e saídas:** Entrada: ProdutoConsultado. Saída: tela de detalhe compreensível.
- **Dados/entidades envolvidos (conceitual):** ProdutoConsultado (Spec 01).
- **Estados e transições:** Sucesso da consulta → Resultado. Voltar → Entrada (câmera/digitação).
- **Regras de negócio:** Regras 5, 7, 11, 13.
- **Validações:** Não se aplica.
- **Fluxo do usuário (passo a passo):**
  1. Consulta bem-sucedida.
  2. Pessoa vê alergênicos em destaque, depois ingredientes e demais.
  3. Pode voltar para nova consulta.
- **Casos de borda e erros:** Só nome sem alergênicos → alergênicos “Não informado”, sem parecer erro de app. Textos muito longos de ingredientes → rolagem legível, sem cortar silenciosamente o sentido para leitores de tela (ver Spec 07).
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado produto com alergênicos, ingredientes e Nutri-Score B, Quando o resultado abre, Então a ordem prioriza alergênicos antes de ingredientes, e o Nutri-Score mostra B.
  - Dado produto sem Nutri-Score, Quando o resultado abre, Então mostra “Não informado” para a nota.
  - Dado produto com URL de imagem na base, Quando o resultado abre, Então nenhuma foto de produto é exibida.
- **Definição de pronto:** Hierarquia de conteúdo verificável; campos ausentes honestos; sem foto.
- **Dependências:** Spec 01.
- **Fora do escopo desta spec:** Histórico; i18n completa das strings de UI (Spec 10); checklist TalkBack do README (Spec 11).

### Spec 07 — Acessibilidade das telas principais

- **Fase:** 3 — Resultado acessível
- **Objetivo (o quê):** Garantir tipografia legível, contraste adequado e ordem de foco coerente nas telas principais (entrada, loading/erros, resultado, histórico), usáveis com TalkBack.
- **Intenção (por quê):** Acessibilidade é requisito do produto, não polish. Sem isso, o app falha para parte do público-alvo.
- **Contexto:** Aplica-se às UIs das Specs 02–06 e 08–09. Escopo finito: telas do fluxo principal do MVP. Style reference (`docs/design/style-reference.md`) pode informar contraste/tipografia, sem ser contrato rígido.
- **Atores:** Pessoa usuária, inclusive com TalkBack.
- **Descrição do comportamento:** Textos de interface usam tamanhos confortáveis e respeitam escala de fonte do sistema na medida do suportado pelas telas. Contraste de texto/fundo adequado. Elementos interativos têm rótulos acessíveis. A ordem de navegação por foco segue a prioridade de conteúdo (no resultado: alergênicos antes de blocos secundários). Estados de erro são anunciáveis (não só cor).
- **Entradas e saídas:** Não se aplica (qualidade transversal da UI).
- **Dados/entidades envolvidos (conceitual):** Não se aplica — trata apresentação e semântica de UI.
- **Estados e transições:** Não se aplica além dos estados já definidos nas outras specs.
- **Regras de negócio:** Princípio de acessibilidade como requisito; Regra 7 (não depender só de cor para alergênicos).
- **Validações:** Não se aplica.
- **Fluxo do usuário (passo a passo):**
  1. Pessoa percorre o fluxo com TalkBack e/ou fonte ampliada.
  2. Consegue entender ações, erros e o bloco de alergênicos sem informação só visual.
- **Casos de borda e erros:** Fonte do sistema muito grande → layouts não devem quebrar a ponto de impedir ação primária. Ícones sem texto → precisam de label.
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado TalkBack ativo na tela de resultado, Quando o foco percorre o conteúdo, Então alergênicos são alcançados antes de informações secundárias e possuem rótulo compreensível.
  - Dado estado de erro (não encontrado ou rede), Quando anunciado, Então a mensagem não depende apenas de cor.
  - Dado tipografia ampliada do sistema, Quando as telas principais são usadas, Então as ações primárias permanecem alcançáveis.
- **Definição de pronto:** Revisão manual com TalkBack nas telas principais sem bloqueios; pendências conscientes vão para o README (Spec 11).
- **Dependências:** Specs de UI 02–06 (e 08–09 quando existirem).
- **Fora do escopo desta spec:** Acessibilidade de 100% de edge cases do sistema; múltiplos temas customizados além do necessário para contraste.

### Spec 08 — Listar e manter histórico local

- **Fase:** 4 — Histórico local
- **Objetivo (o quê):** Persistir no aparelho até 20 consultas bem-sucedidas, listáveis da mais recente para a mais antiga, sem conta.
- **Intenção (por quê):** Reconsultar o que já foi visto na loja/casa sem nova busca e sem rede; reforça privacidade (dados ficam no device).
- **Contexto:** Grava apenas sucesso da Spec 01/06. Sem sync. Limite 20. Deduplicação por código (Regra 9).
- **Atores:** Pessoa usuária; sistema após sucesso.
- **Descrição do comportamento:** Após cada produto encontrado, o app salva/atualiza um item de histórico com o snapshot dos dados exibidos e o momento. A lista mostra as entradas (pelo menos código, nome/marca se houver, e recência). Capacidade máxima 20; ao inserir a 21ª distinta, remove a mais antiga. Sem login. Dados só no aparelho.
- **Entradas e saídas:** Entrada: ProdutoConsultado bem-sucedido. Saída: lista local ordenada.
- **Dados/entidades envolvidos (conceitual):** ItemHistorico: código, nome, marca, alergênicos, ingredientes, nutriScore, consultadoEm.
- **Estados e transições:** Histórico vazio → com itens após sucessos. Atualização de item existente → sobe ao topo.
- **Regras de negócio:** Regras 3, 8, 9, 10 (preparação para Spec 09).
- **Validações:** Não gravar não-encontrado nem erro de rede.
- **Fluxo do usuário (passo a passo):**
  1. Conclui uma ou mais consultas com sucesso.
  2. Abre o histórico.
  3. Vê até 20 itens, mais recente no topo.
- **Casos de borda e erros:** Histórico vazio → estado vazio amigável com CTA para escanear/digitar. Reconsulta do mesmo código → um item atualizado no topo. Desinstalação/limpeza de dados → histórico some (esperado; sem nuvem).
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado um sucesso de consulta, Quando o usuário abre o histórico, Então o item aparece no topo com identificação compreensível.
  - Dado o mesmo código consultado de novo, Quando o histórico atualiza, Então não há duplicata e o item está no topo com snapshot atualizado.
  - Dado 20 itens distintos e um 21º sucesso novo, Quando grava, Então o mais antigo é removido.
  - Dado falha de rede ou não encontrado, Quando o fluxo termina, Então o histórico não ganha item novo.
- **Definição de pronto:** Persistência local correta com limite, ordem e dedupe; estado vazio tratado.
- **Dependências:** Spec 01 (sucesso); Spec 06 (dados exibidos alinhados ao snapshot).
- **Fora do escopo desta spec:** Reabrir detalhe (Spec 09); exportar histórico; limpar tudo (não exigido na v1).

### Spec 09 — Reabrir consulta do histórico

- **Fase:** 4 — Histórico local
- **Objetivo (o quê):** Ao tocar um item do histórico, abrir o detalhe com o snapshot salvo, sem chamar a Open Food Facts.
- **Intenção (por quê):** Uso offline e imediato; alinhado à pesquisa de cache/histórico em apps de barcode lookup e ao critério de sucesso do brief.
- **Contexto:** Depende da Spec 08. Decisão de produto: sem refresh de rede na v1.
- **Atores:** Pessoa usuária.
- **Descrição do comportamento:** Na lista, a pessoa seleciona um item. O app abre a mesma hierarquia de resultado (Spec 06) usando os dados persistidos. Não inicia loading de rede. Funciona em modo avião se o item já existir. Opcionalmente pode indicar de forma discreta que é uma consulta salva (sem obrigar timestamp complexo na UI, desde que o comportamento offline esteja claro na experiência).
- **Entradas e saídas:** Entrada: seleção de ItemHistorico. Saída: tela de resultado preenchida offline.
- **Dados/entidades envolvidos (conceitual):** ItemHistorico → apresentação como ProdutoConsultado.
- **Estados e transições:** Lista → Detalhe (snapshot). Voltar → Lista.
- **Regras de negócio:** Regra 10.
- **Validações:** Se o item não existir mais (ex.: corrida rara ao limpar dados), voltar à lista/estado vazio com mensagem breve.
- **Fluxo do usuário (passo a passo):**
  1. Abre histórico.
  2. Seleciona item.
  3. Vê detalhe imediatamente, offline.
- **Casos de borda e erros:** Aparelho offline → sucesso igual. Dados parciais no snapshot → mesmos “Não informado” da Spec 06.
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado um item no histórico e o aparelho offline, Quando a pessoa abre o item, Então o detalhe é exibido sem erro de rede.
  - Dado abertura pelo histórico, Quando o detalhe carrega, Então nenhuma requisição à OFF é necessária para essa ação.
- **Definição de pronto:** Reabertura offline verificada manualmente (modo avião) e alinhada ao resultado da Spec 06.
- **Dependências:** Spec 08; Spec 06 (paridade de apresentação).
- **Fora do escopo desta spec:** Botão atualizar da rede; edição do snapshot.

### Spec 10 — Idiomas da interface (PT-BR e EN)

- **Fase:** 5 — Idiomas e documentação
- **Objetivo (o quê):** Disponibilizar todas as strings de interface em português (Brasil) e inglês, com seleção automática pela regra de idioma do sistema.
- **Intenção (por quê):** Amplia o alcance do público e atende à decisão de produto da v1 sem exigir seletor manual complexo.
- **Contexto:** Aplica-se a labels, botões, erros, estados vazios, permissões explicadas, “Não informado”, etc. Conteúdo do produto (OFF) não é traduzido pelo app (Regra 13).
- **Atores:** Pessoa usuária.
- **Descrição do comportamento:** O app inclui recursos de idioma pt-BR e en. Se o idioma do sistema for português, usa pt-BR; se inglês, usa en; caso contrário, fallback en. Não é obrigatório na v1 um seletor in-app de idioma.
- **Entradas e saídas:** Entrada: locale do sistema. Saída: UI no idioma correspondente.
- **Dados/entidades envolvidos (conceitual):** Não se aplica (recursos de interface).
- **Estados e transições:** Não se aplica.
- **Regras de negócio:** Regra 12 e 13.
- **Validações:** Não se aplica.
- **Fluxo do usuário (passo a passo):**
  1. Usa o aparelho em pt-BR ou en.
  2. Vê a interface no idioma correspondente.
- **Casos de borda e erros:** Sistema em espanhol/outro → UI em inglês. Mistura de dados OFF em outro idioma no meio da UI traduzida → esperado (Regra 13).
- **Impacto no existente:** Nenhum.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado sistema em pt-BR, Quando o app abre, Então textos de UI principais estão em português.
  - Dado sistema em en, Quando o app abre, Então textos de UI principais estão em inglês.
  - Dado sistema em idioma terceiro, Quando o app abre, Então a UI cai para inglês.
- **Definição de pronto:** Fluxos principais (entrada, erros, resultado, histórico, vazio) cobertos nos dois idiomas.
- **Dependências:** Telas das fases 1–4.
- **Fora do escopo desta spec:** Tradução automática de ingredientes/alergênicos da OFF; seletor manual de idioma.

### Spec 11 — Documentação TalkBack no README

- **Fase:** 5 — Idiomas e documentação
- **Objetivo (o quê):** No README do projeto, listar o que foi feito para TalkBack/acessibilidade e o que ficou de fora da v1.
- **Intenção (por quê):** Torna o escopo de acessibilidade auditável e honesto — evita parecer que “100% a11y” foi prometido quando o MVP delimita telas principais.
- **Contexto:** Fecha o critério de sucesso do brief. Complementa a Spec 07.
- **Atores:** Quem lê o repositório (incluindo a própria equipe).
- **Descrição do comportamento:** O README contém uma seção de acessibilidade com duas listas explícitas: (1) feito na v1 (ex.: ordem de foco no resultado, labels, contraste, estados de erro anunciáveis, suporte a fonte ampliada nas telas X); (2) fora da v1 (ex.: refresh no histórico, foto, fluxos não existentes). Linguagem factual, sem prometer conformidade completa irrestrita.
- **Entradas e saídas:** Entrada: decisões reais após implementar Spec 07. Saída: seção no README.
- **Dados/entidades envolvidos (conceitual):** Não se aplica.
- **Estados e transições:** Não se aplica.
- **Regras de negócio:** Transparência do escopo de a11y.
- **Validações:** As listas devem refletir o app entregue (não inventário genérico).
- **Fluxo do usuário (passo a passo):** Não se aplica (artefato de documentação).
- **Casos de borda e erros:** Se algo planejado na Spec 07 não couber no prazo, move-se explicitamente para “fora” no README em vez de omitir.
- **Impacto no existente:** Atualiza README do projeto.
- **Critérios de aceite (Dado/Quando/Então):**
  - Dado o MVP implementado, Quando alguém abre o README, Então encontra seções “feito” e “fora” para TalkBack/acessibilidade.
  - Dado um item não implementado, Quando aparece no README, Então está em “fora”, não em “feito”.
- **Definição de pronto:** README atualizado e consistente com o app.
- **Dependências:** Spec 07 (conteúdo factual).
- **Fora do escopo desta spec:** Site de documentação externo; vídeos.

## 14. Ordem recomendada de implementação

1. Spec 01 — Consultar produto por código  
2. Spec 02 — Estados de falha da consulta  
3. Spec 03 — Digitar código manualmente  
4. Spec 05 — Permissão de câmera negada  
5. Spec 04 — Ler código pela câmera  
6. Spec 06 — Exibir resultado do produto  
7. Spec 07 — Acessibilidade das telas principais  
8. Spec 08 — Listar e manter histórico local  
9. Spec 09 — Reabrir consulta do histórico  
10. Spec 10 — Idiomas da interface (PT-BR e EN)  
11. Spec 11 — Documentação TalkBack no README  

Seguir essa ordem evita UI de câmera antes da consulta estar testável (digitação desbloqueia o fluxo cedo), trata permissão antes/em paralelo ao scan, e só então fecha histórico, i18n e documentação — respeitando as dependências entre specs.
