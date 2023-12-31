# AleGor Flix :movie_camera:
**Tech Challenge - Phase 4 - Graduate/Pós-Graduação**

GitHub Repository: https://github.com/igorgrv/StreamFlix
Swagger: http://localhost:8080/swagger-ui/index.html

## About :book:

Welcome to **AleGor Flix**! An innovative project that combines the powerful technologies of:

* Java 17;
* Reactive MongoDB;
* Maven; 
* Spring Boot;
* Spring WebFlux
* Spring Validation;
* Lombok;

## Working with MongoDB

To get started you need to:
* Install MongoDB Community Server: https://www.mongodb.com/try/download/community
* After that, execute in a terminal:  **`mongosh`**
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
