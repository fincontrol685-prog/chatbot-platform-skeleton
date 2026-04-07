# 📊 NGXCHARTS - INSTRUÇÕES DE INSTALAÇÃO E USO

**Status:** ✅ Implementado e Pronto para Usar

---

## 🚀 INSTALAÇÃO RÁPIDA

### Opção 1: Instalação Automática (Recomendado)

```bash
# 1. Navegar até frontend
cd frontend

# 2. Instalar dependências (inclui ngx-charts)
npm install

# 3. Iniciar o servidor
npm start
```

**Pronto!** O ngx-charts já está instalado via `package.json` atualizado.

---

### Opção 2: Instalação Manual

Se você já tem o frontend rodando:

```bash
# 1. Parar o servidor (Ctrl+C)

# 2. Instalar ngx-charts
npm install @swimlane/ngx-charts --save

# 3. Reiniciar o servidor
npm start
```

---

## ✅ VERIFICAR INSTALAÇÃO

```bash
# Verificar se ngx-charts está instalado
npm list @swimlane/ngx-charts

# Saída esperada:
# chatbot-platform-frontend@0.1.0
# └── @swimlane/ngx-charts@20.3.0
```

---

## 📊 USAR OS GRÁFICOS

### Passo 1: Acessar Dashboard

```
1. Abra http://localhost:4200
2. Faça login
3. Menu → Analytics Avançado
4. Ou acesse: http://localhost:4200/analytics-advanced/dashboard
```

### Passo 2: Carregar Métricas

```
1. Selecione um filtro:
   - Bot, Equipe ou Departamento
   
2. Insira um ID (número)

3. Clique em "Buscar"

4. Aguarde carregar os dados
```

### Passo 3: Ver os 4 Gráficos

```
Após carregar métricas, você verá:

1. 📈 GRÁFICO DE LINHA
   └─ Evolução das métricas ao longo do tempo

2. 📊 GRÁFICO DE BARRAS
   └─ Média de cada tipo de métrica

3. 🥧 GRÁFICO DE PIZZA
   └─ Proporção e distribuição dos valores

4. 📉 GRÁFICO DE ÁREA
   └─ Tendências com enchimento visual
```

---

## 🎯 INTERAGIR COM GRÁFICOS

### Ações Disponíveis:

| Ação | Como Fazer |
|------|-----------|
| **Ver Valor Exato** | Posicione mouse sobre o dado |
| **Ativar/Desativar Série** | Clique no item da legenda |
| **Ampliar Área** | Clique e arraste no gráfico |
| **Salvar Imagem** | Click direito → Salvar imagem |
| **Copiar Imagem** | Click direito → Copiar imagem |

---

## 🖥️ ESTRUTURA DOS ARQUIVOS

Novos arquivos criados:

```
frontend/src/app/features/advanced-analytics/
├── metrics-charts/
│   ├── metrics-charts.component.ts         ← Lógica dos gráficos
│   ├── metrics-charts.component.html       ← Templates
│   └── metrics-charts.component.css        ← Estilos
```

Arquivos modificados:

```
frontend/
├── package.json                            ← Adicionou ngx-charts
└── src/app/features/advanced-analytics/
    ├── advanced-analytics.module.ts        ← Importa NgxChartsModule
    └── metrics-dashboard/
        └── metrics-dashboard.component.html ← Exibe gráficos
```

---

## 🎨 PERSONALIZAR CORES

Para mudar as cores dos gráficos, edite:

**Arquivo:** `frontend/src/app/features/advanced-analytics/metrics-charts/metrics-charts.component.ts`

```typescript
colorScheme = {
  domain: [
    '#1976d2',  // Azul
    '#388e3c',  // Verde
    '#f57c00',  // Laranja
    '#c2185b',  // Rosa
    '#7b1fa2',  // Roxo
    '#0097a7'   // Ciano
  ]
};
```

**Exemplos de cores:**
```
Material Design:
  Azul: #1976d2
  Vermelho: #d32f2f
  Verde: #388e3c
  Amarelo: #f9a825
  Laranja: #f57c00

Cores RGB Comuns:
  Vermelho: #ff0000
  Verde: #00ff00
  Azul: #0000ff
  Preto: #000000
  Branco: #ffffff
```

---

## 📱 RESPONSIVIDADE

Os gráficos se adaptam automaticamente:

| Dispositivo | Comportamento |
|------------|--------------|
| **Desktop** | Gráficos grandes e espaçosos |
| **Tablet** | Gráficos redimensionam |
| **Mobile** | Gráficos em coluna única |

---

## 🐛 TROUBLESHOOTING

### Problema: "Cannot find module '@swimlane/ngx-charts'"

**Solução:**
```bash
# Limpar cache do npm
rm -rf node_modules package-lock.json

# Reinstalar tudo
npm install

# Reiniciar servidor
npm start
```

### Problema: Gráficos não aparecem

**Verificar:**
1. [ ] Métricas foram carregadas? (Veja tabela abaixo)
2. [ ] Abra DevTools (F12) → Console → há erros?
3. [ ] Página está completamente carregada?
4. [ ] Tente fazer refresh (F5)

### Problema: Gráficos aparecem mas vazios

**Verificar:**
1. [ ] Os dados têm `metricType` e `metricValue`?
2. [ ] As datas estão em formato correto (YYYY-MM-DD)?
3. [ ] Há pelo menos 2 pontos de dados?

---

## 📖 EXEMPLOS DE USO

### Exemplo 1: Gráfico de Linha

```html
<ngx-charts-line-chart
  [view]="[900, 400]"
  [scheme]="colorScheme"
  [results]="lineChartData"
  [xAxis]="true"
  [yAxis]="true"
  [legend]="true"
>
</ngx-charts-line-chart>
```

**Dados esperados:**
```typescript
lineChartData = [
  {
    name: 'CONVERSATION_COUNT',
    series: [
      { name: '2026-04-01', value: 42 },
      { name: '2026-04-02', value: 55 },
      { name: '2026-04-03', value: 48 }
    ]
  }
]
```

### Exemplo 2: Gráfico de Pizza

```html
<ngx-charts-pie-chart
  [view]="[500, 400]"
  [scheme]="colorScheme"
  [results]="pieChartData"
  [legend]="true"
  [labels]="true"
>
</ngx-charts-pie-chart>
```

**Dados esperados:**
```typescript
pieChartData = [
  { name: 'CONVERSATION_COUNT', value: 150 },
  { name: 'RESPONSE_TIME', value: 120 },
  { name: 'SATISFACTION_SCORE', value: 200 }
]
```

---

## 🔧 CONFIGURAÇÕES AVANÇADAS

### Mudar Tamanho do Gráfico

```html
<!-- Padrão: 900x400 -->
[view]="[900, 400]"

<!-- Maior -->
[view]="[1200, 500]"

<!-- Menor -->
[view]="[600, 300]"
```

### Mostrar/Ocultar Elementos

```html
<!-- Legenda -->
[legend]="true"      <!-- Mostra legenda -->
[legend]="false"     <!-- Esconde legenda -->

<!-- Eixos -->
[xAxis]="true"       <!-- Mostra eixo X -->
[yAxis]="true"       <!-- Mostra eixo Y -->

<!-- Labels -->
[labels]="true"      <!-- Mostra valores nos gráficos -->
```

### Animações

```html
<!-- Ativar animações -->
[animations]="true"

<!-- Desativar para performance -->
[animations]="false"
```

---

## 📚 DOCUMENTAÇÃO OFICIAL

Para mais opções e gráficos:

**ngx-charts Documentation:**
- https://swimlane.gitbook.io/ngx-charts/

**Exemplos Interativos:**
- https://swimlane.github.io/ngx-charts/

**GitHub:**
- https://github.com/swimlane/ngx-charts

---

## 🎉 PRÓXIMOS PASSOS

1. **Agora:** Use os gráficos no dashboard
2. **Depois:** Customize cores conforme sua marca
3. **Depois:** Adicione mais tipos de gráficos (se desejar)
4. **Depois:** Configure refresh automático

---

## ✅ CHECKLIST

- [ ] Executou `npm install`
- [ ] Iniciou servidor com `npm start`
- [ ] Acessou `/analytics-advanced/dashboard`
- [ ] Carregou métricas
- [ ] Viu os 4 gráficos aparecer
- [ ] Interagiu com legenda e dados
- [ ] Leu a documentação oficial (opcional)

---

## 📞 SUPORTE

Se encontrar problemas:

1. **Verifique logs:**
   ```bash
   # No terminal do npm start
   # Procure por erros de importação
   ```

2. **Verifique console:**
   ```
   F12 → Console → procure por erros
   ```

3. **Reinstale dependências:**
   ```bash
   rm -rf node_modules
   npm install
   npm start
   ```

---

**Status:** ✅ **PRONTO PARA USAR**

Gráficos interativos e responsivos agora estão disponíveis no seu dashboard!

Defrute da visualização de dados profissional! 📊📈🎉

