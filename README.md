# AlertaRótulo

App Android que lê o código de barras de um alimento **no aparelho**, consulta a [Open Food Facts](https://world.openfoodfacts.org/) e destaca **alergênicos**, ingredientes e Nutri-Score — com interface pensada para acessibilidade (TalkBack, tipografia e contraste).

Sem conta, sem backend próprio e sem enviar a imagem do código para a nuvem. A rede entra só para buscar o produto. As últimas consultas ficam salvas localmente e podem ser reabertas offline.

## Status

MVP implementado (fases 0–5). Fluxos principais: captura/digitação → consulta OFF → resultado → erros distintos → histórico local offline → UI em pt-BR/en.

## Documentação

| Documento | Conteúdo |
|---|---|
| [`docs/product-brief.md`](docs/product-brief.md) | Visão, escopo e critérios de sucesso |
| [`docs/prd/alerta-rotulo.md`](docs/prd/alerta-rotulo.md) | Comportamento, fluxos, specs e aceite |
| [`docs/architecture.md`](docs/architecture.md) | Camadas, stack, pacotes e integrações |
| [`docs/design/style-reference.md`](docs/design/style-reference.md) | Referência visual para telas (orientação, não contrato rígido) |

## Stack

- Kotlin, Jetpack Compose, Material 3
- Navigation 3, ViewModel, StateFlow, Coroutines, Hilt
- Retrofit, OkHttp, kotlinx.serialization
- Room (histórico local)
- CameraX + ML Kit Barcode Scanning (leitura on-device)

Detalhes em [`docs/architecture.md`](docs/architecture.md).

## Escopo da v1 (resumo)

- Escanear ou digitar o código de barras
- Mostrar nome, marca, alergênicos, ingredientes e Nutri-Score (quando a base tiver)
- Tratar produto não encontrado, falha de rede e permissão de câmera negada
- Histórico das últimas 20 consultas no aparelho
- Interface em **pt-BR** e **en**

**Fora da v1:** lista de compras, conta de usuário, upload de imagens, foto do produto, refresh de rede ao reabrir histórico, personalização de alergias, QR Code.

## Idiomas

- Recursos em `values` (inglês, fallback) e `values-pt` / `values-pt-rBR` (português)
- Locale do sistema: português → UI em português; inglês → UI em inglês; demais → inglês
- Texto do produto vindo da Open Food Facts **não** é traduzido pelo app

## Acessibilidade (TalkBack)

### Feito na v1

- Hierarquia do resultado com **alergênicos antes** de ingredientes e demais campos
- Headings semânticos nas seções principais (resultado, histórico, digitação, erros, scan)
- Labels / `contentDescription` em blocos críticos (alergênicos, Nutri-Score, preview da câmera)
- Itens do histórico com descrição acessível unificada (um nó acionável por linha)
- Estados de erro e loading anunciáveis (`liveRegion`), sem depender só de cor; sem double-announce título+corpo
- Botões de ação com altura mínima generosa (≥ 52 dp), shape 16 dp e ícones decorativos rotulados pelo texto do botão
- TopAppBar com navegação back nas telas secundárias (manual, produto, histórico)
- Permissão de câmera negada com texto claro + atalho para digitação e configurações
- Layouts com scroll nas telas principais para tipografia ampliada do sistema
- Tipografia Atkinson Hyperlegible Next; contraste Material 3 + badges semânticos (texto + cor, nunca só cor)
- Nutri-Score com cores A–E e rótulo textual; faixa de status informativa (alergênicos declarados / não informado), sem veredito clínico

### Fora da v1

- Auditoria TalkBack de 100% dos edge cases do sistema Android
- Foto do produto / Coil
- Atualizar item do histórico pela rede
- Seletor in-app de idioma
- Temas customizados além do necessário para contraste no MVP
- Personalização de restrições alimentares do usuário / veredito “pode comer”
- Feedback sonoro de captura além do háptico
- Macrobenchmark / Baseline Profiles / testes de UI de navegação automatizados

## Requisitos

- Android Studio recente (AGP/Kotlin do projeto)
- minSdk 26

## Branches

| Branch | Uso |
|---|---|
| `main` | Linha estável / releases |
| `develop` | Integração do desenvolvimento |

## Licença de dados

Os dados de produto vêm da Open Food Facts (ODbL). O cliente HTTP identifica o app com User-Agent (nome + contato), conforme a política deles.
