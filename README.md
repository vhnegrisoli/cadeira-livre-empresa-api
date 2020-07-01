# Projeto Cadeira Livre Empresa API

Projeto de Back-end do aplicativo Cadeira Livre Empresa.

## Resumo

O projeto é subdividido em módulos, utiliza arquitetura de API REST e microsserviços e conta com testes automatizados.

### Tecnologias

* **Java 11**
* **Spring Boot 2**
* **Spring Security**
* **OAuth 2.0**
* **Spring Data JPA**
* **Spring Cloud Open Feign**
* **RabbitMQ**
* **QueryDSL**
* **Redis**
* **PostgreSQL 11**
* **REST API**

### Pré-requisitos

É necessário ter as seguintes ferramentas para inicializar o projeto:

```
Java 11.0.3
Maven
mvn
RabbitMQ
Redis Server
PostgreSQL 11
```

### Code Style

O projeto utiliza o checkstyle da Google e o plugin PMD, sendo assim, cada comando do maven irá rodar o checkstyle.

### Instalação

Primeiramente, rode a instalação através da mvn, sem os testes:

```
mvn clean install -DskipTests
```

Para realizar a instalação das dependências com os testes, execute apenas:

```
mvn clean install
```

Para construir o jar de execução, execute:

```
mvn package
```

## Iniciando a aplicação

Após instalar a aplicação, dar o build e gerar o jar, basta, na raiz, executar:

```
mvn spring-boot:run
```

Ou então:

```
cd target/java -jar nome_do_jar.jar
```

A aplicação estará disponível em:

```
http://localhost:8091
```

## Executando testes automatizados

Para rodar apenas os testes:

```
mvn test
```

## Deployment

O deploy da aplicação está sendo realizado no Heroku

## Autores

* **Victor Hugo Negrisoli** - *Desenvolvedor Back-End Pleno* - [vhnegrisoli](https://github.com/vhnegrisoli)

## Licença

Este projeto possui a licença do MIT. Veja mais em: [LICENSE.txt](LICENSE.txt)

