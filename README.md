# AlertaRótulo

App Android que lê o código de barras de um alimento **no aparelho**, consulta a [Open Food Facts](https://world.openfoodfacts.org/) e destaca **alergênicos**, ingredientes e Nutri-Score — com interface pensada para acessibilidade (TalkBack, tipografia e contraste).

Sem conta, sem backend próprio e sem enviar a imagem do código para a nuvem. A rede entra só para buscar o produto. As últimas consultas ficam salvas localmente e podem ser reabertas offline.

## Status

MVP em preparação. O repositório contém a estrutura inicial do Android Studio e a documentação de produto/arquitetura.

## Documentação

| Documento | Conteúdo |
|---|---|
| [`docs/product-brief.md`](docs/product-brief.md) | Visão, escopo e critérios de sucesso |
| [`docs/prd/alerta-rotulo.md`](docs/prd/alerta-rotulo.md) | Comportamento, fluxos, specs e aceite |
| [`docs/architecture.md`](docs/architecture.md) | Camadas, stack, pacotes e integrações |

## Stack (direção)

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

**Fora da v1:** lista de compras, conta de usuário, upload de imagens, foto do produto.

## Requisitos

- Android Studio recente (AGP/Kotlin do projeto)
- minSdk 26

## Branches

| Branch | Uso |
|---|---|
| `main` | Linha estável / releases |
| `develop` | Integração do desenvolvimento |

## Licença de dados

Os dados de produto vêm da Open Food Facts (ODbL). O cliente HTTP deve identificar o app com User-Agent (nome + contato), conforme a política deles.
