# InflaçãoApp/API Consumption

Uma aplicação mobile desenvolvida em **Java** para corrigir valores monetários pela inflação utilizando o Índice Nacional de Preços ao Consumidor Amplo (IPCA). A aplicação consome a **API BCData/SGS** do Banco Central do Brasil para acessar séries temporais do IPCA.

<br>

[![IMG-20241127-162636.jpg](https://i.postimg.cc/fL3H6RNk/IMG-20241127-162636.jpg)](https://postimg.cc/34TCG7WH)
[![IMG-20241127-162612.jpg](https://i.postimg.cc/9fy01G4f/IMG-20241127-162612.jpg)](https://postimg.cc/5HNf2QSc)

## 📋 **Sobre o Projeto**

Este projeto tem como objetivo calcular a correção de valores monetários com base nos dados do IPCA fornecidos pelo Banco Central. O usuário pode:  
- Consultar a inflação acumulada em um período específico.  
- Corrigir valores monetários conforme a inflação acumulada no período.  
- Visualizar os últimos índices do IPCA diretamente na aplicação.

---

## 🚀 **Funcionalidades**

- **Correção Monetária**: Permite corrigir valores entre duas datas considerando o IPCA acumulado.  
- **Consulta de Índices**: Mostra os índices do IPCA para o período selecionado.  
- **Consulta Rápida**: Exibe os últimos valores do IPCA disponíveis na API.

---

## 🛠️ **Tecnologias Utilizadas**

- **Linguagem**: Java (SDK mínimo: 21 - Android 5.0)  
- **Framework**: Android SDK  
- **Consumo de API**: Retrofit para requisições HTTP.  
- **Bibliotecas**:  
  - **Gson**: Para parsing de dados JSON.  
  - **Material Components**: Para UI moderna e responsiva.

---

## 🔗 **Documentação da API**

A aplicação consome os dados do Banco Central por meio da API BCData/SGS:  
[Documentação da API BCData/SGS](https://dadosabertos.bcb.gov.br/dataset/20542-saldo-da-carteira-de-credito-com-recursos-livres---total/resource/6e2b0c97-afab-4790-b8aa-b9542923cf88?inner_span=True)

- **URL Base**:
`https://api.bcb.gov.br/dados/serie/bcdata.sgs`

### Parâmetros Suportados:

- **codigo_Serie** (obrigatório): Código da série desejada. Para o IPCA, use o código **433**(O APP já está usando por padrão o 433).  
- **dataInicial** (opcional): Data de início da consulta no formato `dd/MM/aaaa`.  
- **dataFinal** (opcional): Data final da consulta no formato `dd/MM/aaaa`.

### Exemplos de Requisição:

- **Consulta de toda a série IPCA**:  
`https://api.bcb.gov.br/dados/serie/bcdata.sgs.433/dados?formato=json`
- **Consulta do IPCA em um período específico**:  
`https://api.bcb.gov.br/dados/serie/bcdata.sgs.433/dados?formato=json&dataInicial=01/01/2020&dataFinal=31/12/2021`
- **Últimos 10 valores do IPCA**:  
`https://api.bcb.gov.br/dados/serie/bcdata.sgs.433/dados/ultimos/10?formato=json`

---

## 💻 **Como Rodar o Projeto**

### **Pré-requisitos**

1. Instalar o [Android Studio](https://developer.android.com/studio).  
2. Clonar este repositório:  
   ```bash
   git clone https://github.com/usuario/IPCAInflationCalculator.git
   ```
   
### **Configuração no Android Studio**

1. Abra o projeto no Android Studio.  
2. Certifique-se de configurar o emulador ou conectar um dispositivo Android físico.  
3. Compile e execute o aplicativo:  
   ```bash
   Run > Run 'app'
   ```

### ✨ **Contribuindo**
Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto.
2. Crie uma branch para sua funcionalidade/correção:
  ```bash
  git checkout -b sua-branch
  ```
3. Faça suas alterações e envie um pull request.






