# SEC
Sistemas Elevada Confiabilidade 18/19

## How To

compilar webservice:

```sh
javac sec/notary/server/NotaryWebService.java
```

compilar notary server:

```sh
javac sec/notary/server/NotaryServer.java
```

criar client usando o wsdl criado:

```sh
wsimport -keep -p sec.notary.client http://localhost:9898/notaryWebService?wsdl
```


correr server & client:

```sh
java sec.notary.server.NotaryServer.java
java sec.notary.client.NotaryClient.java
```
