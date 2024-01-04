# AleGor Flix :movie_camera:
**Tech Challenge - Phase 4 - Graduate/Pós-Graduação**

GitHub Repository: https://github.com/igorgrv/StreamFlix
Swagger: http://localhost:8080/webjars/swagger-ui/index.html

## About :book:

Welcome to **AleGor Flix**! An innovative project that combines the powerful technologies of:

* Java 17;
* Reactive MongoDB;
* Maven; 
* Spring Boot;
* Spring WebFlux
* Spring Validation;
* Lombok;

## Goals/Tasks

| Task                                                         | Done |
| ------------------------------------------------------------ | ---- |
| Criação, atualização, listagem e exclusão de vídeos.         | X    |
| Os vídeos devem conter os seguintes campos: título, descrição, URL e data de publicação. | X    |
| A listagem de vídeos deve ser paginada e ordenável por data de publicação. | X    |
| Implementar filtros de busca por título e data de publicação na listagem. | X    |
| Implementar sistema de marcação de vídeos como favoritos.    | X    |
| Implementar categorias para os vídeos e permitir a filtragem por categoria na listagem. | X    |
| Implementar um sistema de recomendação de vídeos com base nos favoritos do usuário. | X    |
| Implementar um endpoint para estatísticas, mostrando a quantidade total de vídeos, a quantidade de vídeos favoritados e a média de visualizações | X    |
| Utilização do Spring WebFlux para a criação de endpoints reativos. | X    |
| Utilização do Spring Boot para configuração e inicialização da aplicação. | X    |
| Utilização do Spring Data para a camada de persistência com suporte a bancos de dados reativos (por exemplo, MongoDB). | X    |
| Implementar a arquitetura Clean Architecture, separando a aplicação em camadas: Controllers, Services, Use Cases, Repositories. | X    |
| Implementar testes unitários e de integração para as diferentes camadas da aplicação, com cobertura de testes de pelo menos 80% do código. |      |
| Utilizar boas práticas de nomenclatura, organização de código e comentários quando necessário. | X    |
| Utilizar validações de entrada nos endpoints.                | X    |
| Gerenciar dependências utilizando o gerenciador de pacotes Maven ou | X    |
| Código-fonte do projeto no repositório Git (GitHub, GitLab, Bitbucket, etc.). | X    |
| Uma apresentação gravada demonstrando o funcionamento da plataforma, mostrando o código e explicando a arquitetura do projeto. |      |
| Documentação descrevendo a arquitetura escolhida, decisões técnicas relevantes e um guia de uso da |      |





## Working with MongoDB

To get started you need to:
* Install MongoDB Community Server: https://www.mongodb.com/try/download/community
* After that, execute in a terminal: **`mongosh`**
* With the server up and running, execute the following commands:

```bash
use alegorflix
```


### Basic MongoDB Commands

|           **MongoDB Command**            | **Description**                                     |
|:----------------------------------------:|:----------------------------------------------------|
|      **`mongod`** or **`mongosh`**       | Starts mongoDB Server                               |
|           **`show databases`**           | Show all the databases                              |
|          **`use databaseName`**          | Select the Database                                 |
|          **`show collections`**          | Show all the collections for the specific database  |
|       **`db.collectName.drop()`**        | Drop the specified collection                       |
| **`db.nomeCollection.countDocuments()`** | Count the documents given a collection              |
|      **`db.nomeCollection.find()`**      | Shows all the documents of the specified collection |



## Entity-relationship






## Back4App Containers
To test remotely We perform the deployment on back4app.
Swagger: https://alegorflix-e2kw5mlx.b4a.run/swagger-ui/index.html


## Challenges

* Work with WebFlux (Mono/Flux)
* Work with NoSQL databases;
