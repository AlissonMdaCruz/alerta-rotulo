---
name: NutriClara Acessibilidade
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#3c4a42'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#6c7a71'
  outline-variant: '#bbcabf'
  surface-tint: '#006c49'
  primary: '#006c49'
  on-primary: '#ffffff'
  primary-container: '#10b981'
  on-primary-container: '#00422b'
  inverse-primary: '#4edea3'
  secondary: '#855300'
  on-secondary: '#ffffff'
  secondary-container: '#fea619'
  on-secondary-container: '#684000'
  tertiary: '#b91a24'
  on-tertiary: '#ffffff'
  tertiary-container: '#ff7a73'
  on-tertiary-container: '#79000e'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#6ffbbe'
  primary-fixed-dim: '#4edea3'
  on-primary-fixed: '#002113'
  on-primary-fixed-variant: '#005236'
  secondary-fixed: '#ffddb8'
  secondary-fixed-dim: '#ffb95f'
  on-secondary-fixed: '#2a1700'
  on-secondary-fixed-variant: '#653e00'
  tertiary-fixed: '#ffdad7'
  tertiary-fixed-dim: '#ffb3ad'
  on-tertiary-fixed: '#410004'
  on-tertiary-fixed-variant: '#930013'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  headline-lg:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 30px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 26px
  body-lg:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 16px
    fontWeight: '700'
    lineHeight: 22px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Atkinson Hyperlegible Next
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

O design system estabelece uma experiência visual centrada em transparência, acolhimento, segurança alimentar e legibilidade extrema. Projetado para usuários que navegam por restrições dietéticas severas, alergias alimentares críticas (glúten, lactose, nozes, frutos do mar) e necessidades de baixa visão, a interface prioriza o processamento imediato da informação através de semiótica cromática sem ambiguidades.

O estilo funde o **Minimalismo Funcional** ao **Design Universal (WCAG AAA)**. Elementos visuais não competem com os dados nutricionais: o layout remove ruídos ornamentais em favor de blocos estruturais nítidos, tipografia balanceada e superfícies táteis bem delimitadas. A experiência transmite confiança clínica combinada ao calor humano de um assistente de bem-estar cotidiano.

## Colors

A paleta é orientada por uma semântica de triagem nutricional instantânea, atendendo rigorosamente à taxa de contraste WCAG AAA (7:1 para texto normal, 4.5:1 para elementos gráficos e texto ampliado) contra o fundo neutro off-white.

- **Verde Esmeralda Vivo (`#10B981`) & Menta Suave (`#ECFDF5`)**: Semáforo positivo; indica conformidade com restrições alimentares, produtos certificados e limites seguros de ingestão.
- **Âmbar Atenção (`#F59E0B`) & Âmbar Claro (`#FFFBEB`)**: Semáforo intermediário; alerta sobre presença moderada de açúcares, sódio, gorduras saturadas ou traços não confirmados de alérgenos.
- **Vermelho Carmim Suave (`#EF4444`) & Rosa Alerta (`#FEF2F2`)**: Sinalização crítica e restrição severa; demarca alérgenos fatais incompatíveis com o perfil do usuário ou índices extremos de nutrientes nocivos.
- **Fundo Off-White (`#F8FAFC`)**: Base suave para reduzir o estresse ocular e o efeito de ofuscamento em ambientes externos (ex.: supermercados).
- **Cinza Escuro / Preto Textual (`#0F172A`)**: Tinta estrutural dominante para textos e ícones vetoriais de alta prioridade.

## Typography

Adota-se a família **Atkinson Hyperlegible Next** integralmente através de todos os níveis tipográficos. Desenvolvida para maximizar a distinção entre caracteres tradicionalmente ambíguos (como `I`, `l`, `1` e `0`, `O`), sua anatomia garante reconhecimento instantâneo mesmo sob iluminação desfavorável ou em dispositivos de resolução reduzida.

- Os pesos concentram-se estritamente em **Regular (400)**, **SemiBold (600)** e **Bold (700)**, eliminando variações ultra-finas inacessíveis.
- As alturas de linha (`lineHeight`) são ampliadas para conferir ritmo calmo e mitigar fadiga visual na leitura prolongada de tabelas nutricionais e listas de ingredientes.

## Layout & Spacing

O layout adota uma grade fluida mobile-first com colunamento de 4 colunas em telas de smartphone, evoluindo para 8 colunas em tablets. A prioridade estrutural é garantir alvos de toque generosos (mínimo de 48x48px conforme diretrizes de acessibilidade física).

- **Margens Externas (`margin`)**: 16px no mobile para otimizar a área útil; 24px no tablet.
- **Espaçamento entre Componentes (`space-md`, `space-lg`)**: Utilizado para isolar cartões de diagnóstico alimentar, evitando toques acidentais entre opções de filtros de alérgenos.
- **Áreas Seguras**: A interface respeita áreas de entalhe da câmera e barras de navegação do sistema operacional, mantendo a zona de escaneamento de código de barras desimpedida de comandos táteis densos.

## Elevation & Depth

A profundidade nesta interface apoia-se em camadas tonais contrastantes e contornos suaves, evitando sombras projetadas escuras que possam criar borrões perceptivos ou dificultar a nitidez do texto para usuários com astigmatismo.

- **Nível 0 (Superfície Base)**: `#F8FAFC`, plano de fundo geral.
- **Nível 1 (Cartões e Blocos Interativos)**: Superfície branca pura (`#FFFFFF`) delimitada por contorno de baixo contraste (`#E2E8F0`, 1.5px sólido). 
- **Nível 2 (Focal / Badges de Alerta Ativos)**: Semáforos contextuais reforçados por preenchimentos claros correspondentes (`#ECFDF5`, `#FFFBEB`, `#FEF2F2`) e bordas sólidas espessas (2px) na cor-chave da gravidade.
- **Elevação Tátil**: O foco de acessibilidade via leitor de tela ou navegação por teclado/switch utiliza uma borda externa proeminente de 3px em `#0F172A` com deslocamento (`outline-offset`) de 2px.

## Shapes

O sistema utiliza a classe **Rounded (`2`)**, proporcionando cantos com raio base de 8px (0.5rem), `rounded-lg` de 16px (1rem) e `rounded-xl` de 24px (1.5rem).

Essa curvatura equilibrada humaniza o aplicativo sem torná-lo infantilizado. Elementos de contorno curvo guiam naturalmente os olhos para o centro dos contêineres de informação, demarcando com precisão os limites físicos de botões de escaneamento e cartões de advertência nutricional.

## Components

### Botões
- **Primário (Escanear / Ação Segura)**: Fundo `#10B981`, texto em `#FFFFFF` (peso 700), altura mínima de 52px, cantos com 16px de raio. Suporta ícone à esquerda com proporção de 24x24px.
- **Secundário / Neutro**: Fundo `#FFFFFF`, borda de 2px em `#0F172A`, texto `#0F172A`.
- **Alerta / Destrutivo**: Fundo `#EF4444`, texto `#FFFFFF`.

### Badges de Alérgenos e Alertas Nutricionais
- **Estrutura**: Dimensões ampliadas (mínimo de 36px de altura) com ícone temático à esquerda (ex.: espiga de trigo riscada para glúten, gota de leite riscada para lactose), texto em caixa alta e peso 700.
- **Variante Segura**: Fundo `#ECFDF5`, borda e texto `#065F46` (Verde escuro para AAA).
- **Variante Atenção (Sódio/Açúcar Moderado)**: Fundo `#FFFBEB`, borda e texto `#92400E`.
- **Variante Crítica (Contém Glúten/Lactose/Alérgeno Declarado)**: Fundo `#FEF2F2`, borda e texto `#991B1B`.

### Campos de Entrada (Busca e Código Manual)
- Borda neutra de 2px em `#CBD5E1`, fundo branco puro, texto `#0F172A`. Estado de foco ativa borda de 2px em `#10B981` com halo externo translúcido. Altura de 52px com texto explicativo sempre fixo acima do campo (labels estáticos, sem placeholders flutuantes que possam comprometer a leitura).

### Checkboxes e Seletores de Restrição Alimentar
- Dimensão do controle de toque de 28x28px inserido em uma linha de clique mínima de 56px de altura. Estado ativo preenchido com `#10B981` exibindo ícone de check espesso (stroke de 3px) em `#FFFFFF`.

### Cartões de Produto Escaneado
- Superfície branca com borda de 1.5px em `#E2E8F0`. O topo do cartão exibe uma faixa de status proeminente (mínimo de 8px de espessura) na cor correspondente ao veredito de compatibilidade com a dieta do usuário (Verde, Âmbar ou Vermelho), acompanhada de texto explícito (ex.: "PRODUTO INCOMPATÍVEL: CONTÉM LACTOSE").

### Visor de Câmera / Scanner
- Moldura de mira centralizada com bordas espessas de 4px nos quatro cantos em `#10B981`, alto contraste com a máscara escura (overlay com 60% de opacidade) e feedback sonoro/tátil imediato ao capturar o código.