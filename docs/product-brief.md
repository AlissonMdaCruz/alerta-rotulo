# Product Brief — AlertaRótulo

| | |
|---|---|
| **Produto** | AlertaRótulo |
| **Plataforma** | Android |
| **Versão-alvo** | MVP (v1) |
| **Status** | Brief aprovado para detalhamento |

---

## 1. Visão do produto

AlertaRótulo ajuda a pessoa a entender o que há em um alimento **no momento da compra ou do uso**: aponta a câmera para o código de barras, lê o código **no aparelho**, consulta uma base pública e destaca o que importa primeiro — **alergênicos**, ingredientes e informação nutricional — com interface pensada para **acessibilidade**.

---

## 2. Problema

Ler rótulos é lento, visualmente denso e pouco acessível. Quem depende de leitor de tela, contraste alto ou tipografia maior costuma ser deixado de lado em apps de “scan de produto”.

Além disso, muitos fluxos de leitura de código dependem desnecessariamente da nuvem: a imagem sobe só para extrair um número que o aparelho já consegue ler. Isso gera atrito de privacidade, latência e falha quando a rede é ruim — exatamente no momento em que a pessoa está na prateleira.

Falta um app pequeno, honesto no escopo, que resolva a consulta de rótulo com prioridade a alergênicos, leitura local do código e interface acessível de verdade.

---

## 3. Proposta de valor

Em poucos passos, a pessoa vê nome, marca, alergênicos, ingredientes e nota nutricional (quando a base tiver), com texto grande, contraste e ordem de foco revisados. A leitura do código fica no aparelho; a rede entra só para buscar o produto. O histórico das consultas permanece local, sem conta.

---

## 4. Público e contexto de uso

- **Público:** pessoa que consulta alergênicos/ingredientes na prateleira ou em casa, incluindo quem usa TalkBack ou precisa de tipografia/contraste reforçados.
- **Contexto:** uso pontual; histórico consultável sem rede após a busca original; rede necessária apenas na consulta do produto.

---

## 5. Objetivos da v1

1. Ler código de barras pela câmera **ou** digitar o número.
2. Buscar o produto na Open Food Facts e exibir o essencial com prioridade visual/semântica para alergênicos.
3. Tratar de forma clara: produto não encontrado, falha de rede e permissão de câmera negada.
4. Guardar as últimas consultas **só no aparelho** (sem conta, sem backend próprio).
5. Documentar no README o trabalho de TalkBack (feito vs. fora do escopo).
6. Cobrir com testes o mapeamento da resposta da API e o ViewModel quando o produto não existe.

---

## 6. Escopo da v1 (In)

### 6.1 Captura do código

- Leitura via câmera (código de barras de alimento).
- Entrada manual do número do código.
- Imagem do código **não sai do aparelho**; só o identificador é enviado à API pública.

### 6.2 Consulta e resultado

- Nome, marca, ingredientes, alergênicos e nota nutricional **quando a base fornecer**.
- Tela de resultado priorizando alergênicos → ingredientes → demais dados, com contraste e tipografia adequados.

### 6.3 Estados de erro / edge

Cada um com saída clara e acionável:

- Produto não encontrado na base.
- Falha de rede / indisponibilidade da API.
- Permissão de câmera negada (com caminho para digitar o código ou ajustar permissão).

### 6.4 Histórico local

- Últimas consultas persistidas no aparelho.
- Sem autenticação, sem sincronização, sem servidor próprio.

### 6.5 Acessibilidade (produto)

- Texto grande, contraste e ordem de foco revisados nas telas principais.
- README lista o que foi feito para TalkBack e o que ficou de fora da v1.

### 6.6 Qualidade mínima

- Testes de mapeamento da resposta da API.
- Testes do ViewModel no cenário “produto não existe”.

---

## 7. Fora da v1 (Out)

Explicitamente **não** entram nesta versão:

- Lista de compras.
- Conta de usuário / login / perfil.
- Qualquer gravação ou upload de imagem na nuvem.
- Backend próprio, analytics invasivo ou sync multi-dispositivo.
- Personalização avançada de alergias do usuário (filtros salvos, alertas push, etc.) — pode ser candidato a versões futuras, mas não é compromisso da v1.

---

## 8. Princípios de produto

1. **On-device first para o scan** — a câmera e o reconhecimento do código ficam no aparelho.
2. **Rede só para o dado do produto** — Open Food Facts; sem chave; User-Agent identificável (nome do app + contato), conforme a política deles.
3. **Privacidade por desenho** — sem conta; histórico local; imagem do código não deixa o device.
4. **Acessibilidade como requisito, não polish** — prioridade de conteúdo e foco fazem parte da definição de “pronto”.
5. **Escopo fechado** — preferir um MVP completo e testável a features extras incompletas.

---

## 9. Critérios de sucesso (MVP)

O MVP é considerado bem-sucedido quando:

- [ ] Um usuário consegue obter resultado (ou erro claro) a partir da câmera **e** da digitação.
- [ ] Alergênicos e ingredientes são compreensíveis com TalkBack nas telas principais.
- [ ] Os três estados de falha acima têm UX distinta e compreensível.
- [ ] Histórico das últimas consultas funciona sem rede após a consulta original.
- [ ] README documenta TalkBack: feito vs. fora.
- [ ] Suite mínima de testes cobre mapper da API + ViewModel “produto inexistente”.
- [ ] User-Agent da Open Food Facts está conforme a política (identificação do app + contato).

---

## 10. Dependências e restrições (nível brief)

| Tema | Decisão |
|---|---|
| **Fonte de dados** | Open Food Facts — `GET /api/v2/product/{código}` (sem API key). |
| **Identificação HTTP** | User-Agent com nome do app e contato (obrigatório pela política da OFF). |
| **Stack (direção)** | Kotlin, Jetpack Compose, Material 3, Navigation 3, ViewModel, StateFlow, Coroutines, Hilt, Retrofit, OkHttp, kotlinx.serialization, Room, CameraX, ML Kit Barcode Scanning; Coil fora da v1; testes com JUnit, Turbine, kotlinx-coroutines-test (fakes preferíveis; MockK se necessário). Detalhes em `docs/architecture.md`. |
| **Estrutura** | Um módulo de app; organização por função (packages). |
| **Imagens** | Foto do produto (URL da base), se existir; imagem capturada do código **nunca** sobe. |

Detalhamento de comportamento de telas, fluxos e contratos fica no **PRD**. Escolhas de camadas, DI e pacotes ficam no **documento de arquitetura** — ambos fora deste brief.

---

## 11. Riscos e premissas

| Risco / premissa | Impacto | Mitigação de produto |
|---|---|---|
| Base pública incompleta ou campos vazios | Resultado parcial frustra expectativa | UI honestamente distingue “não informado” de “não encontrado”. |
| Dependência de rede na consulta | Falha frequente em mercado/loja | Mensagem clara + histórico local para reconsulta do que já foi visto. |
| Qualidade variável de alergênicos na OFF | Risco de interpretação errada | Apresentar dados da fonte; não inventar alerta clínico; linguagem cautelosa. |
| Escopo de acessibilidade infinito | Atraso do MVP | README delimita feito vs. fora; focar telas e fluxos críticos. |

---

## 12. Próximos artefatos

| Artefato | Conteúdo | Status |
|---|---|---|
| **Product Brief** (este arquivo) | Visão, escopo, sucesso, restrições de produto | Atual |
| **PRD** | Comportamento detalhado por tela/fluxo, copy, estados, critérios de aceite | Atual (`docs/prd/alerta-rotulo.md`) |
| **Arquitetura** | Camadas, pacotes, DI, decisões técnicas | Atual (`docs/architecture.md`) |

---

## 13. Resumo em uma frase

**AlertaRótulo** é um MVP Android acessível que lê o código de barras no aparelho, consulta a Open Food Facts só para o produto, destaca alergênicos e ingredientes e guarda o histórico localmente — sem conta e sem enviar a imagem do código para a nuvem.
