# API de Simulação de Empréstimos

Este projeto implementa uma API RESTful para gerenciar e simular produtos de empréstimo, utilizando Java e o Framework Quarkus.

## Pré-requisitos

Para compilar e executar este projeto, você precisará de:
* **Java**: JDK 17
* **Maven**: 3.8.2 ou superior.
* **Docker** (para execução em contêiner)

## Executando a Aplicação (Modo de Desenvolvimento)

Para iniciar a aplicação em modo de desenvolvimento, que habilita o live coding, execute o seguinte comando no terminal:
```shell script
./mvnw quarkus:dev
```
A API estará disponível em `http://localhost:8080`.

## Ferramentas de Desenvolvimento

Quando a aplicação está rodando em modo `dev`, as seguintes ferramentas estão disponíveis:

#### Swagger UI (OpenAPI)
Para visualizar e testar interativamente todos os endpoints da API.
* **URL**: `http://localhost:8080/q/swagger-ui/`

#### Console do Banco de Dados H2
Para inspecionar o banco de dados em memória diretamente pelo navegador.
1.  Acesse o link: `http://localhost:8080/h2-console/`
2.  Use as seguintes credenciais para conectar:
    * **JDBC URL**: `jdbc:h2:mem:default`
    * **User Name**: `sa`
    * **Password**: `sa`

## Testes, Cobertura e Qualidade de Código

O projeto está configurado com ferramentas para garantir a qualidade e a segurança do código.

#### Executando Todos os Testes
Para rodar todos os testes, execute:
```shell script
./mvnw clean verify
```

#### Verificando a Cobertura de Código (JaCoCo)
O projeto está configurado para gerar um relatório de cobertura de código a cada build. O objetivo é manter a cobertura acima de 80%.
* **Comando**: `./mvnw clean verify`
* **Relatório**: Abra o arquivo `target/jacoco-report/index.html` em seu navegador.

#### Análise Estática e Segurança do Código
O projeto utiliza duas ferramentas para análise estática:

1.  **SpotBugs + Find Security Bugs** (Segurança do Código)
    * Verifica o código-fonte em busca de padrões de vulnerabilidades.
    * **Comando para checagem**: `./mvnw verify`
    * **Relatório**: `target/spotbugs.html`

## Executando com Docker (Caso necessário)

Para garantir a máxima reprodutibilidade e portabilidade, a aplicação pode ser executada dentro de um contêiner Docker.

Siga os passos abaixo:

**1. Construir o Pacote da Aplicação**
Primeiro, gere os artefatos da aplicação com o Maven.
```shell script
./mvnw package
```

**2. Construir a Imagem Docker**
Use o `Dockerfile` fornecido para criar a imagem da sua aplicação.
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t emprestimo-api/jvm .
```

**3. Executar o Contêiner**
Inicie um contêiner a partir da imagem que você acabou de criar.
```shell script
docker run -i --rm -p 8080:8080 emprestimo-api/jvm
```
A aplicação agora deve estar rodando e acessível em `http://localhost:8080` .
