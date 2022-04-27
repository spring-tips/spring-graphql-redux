# Spring GraphQL 1.0 

* Queries 
  * Batch Mapping
  * Subscriptions
* Mutations
* Client
* Testing
* [RSocket support](https://spring.io/blog/2022/04/20/spring-for-graphql-1-0-rc1-released)
* Spring Data 
* Spring Security 

## Using CURL 


We can try out an unauthenticated request like this:

```shell 
 curl -v  http://localhost:8080/graphql \
     -H 'Content-Type: application/json' \
     --data-raw '{"query":"mutation { insert(name: \"Josh\") { id, name } }" }'
```
You can authenticate with CURL like this: 

```shell 

We can try out an unauthenticated request like this: 

```shell 
 curl -v -u jlong:pw  http://localhost:8080/graphql \
     -H 'Content-Type: application/json' \
     --data-raw '{"query":"mutation { insert(name: \"Josh\") { id, name } }" }'
```
```

