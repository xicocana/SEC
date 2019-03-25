# SEC
Sistemas Elevada Confiabilidade 18/19

## How To

compilar:

```sh
cd SEC && mvn clean install
```

wsimport caso alteracoes no webservice:

```sh
cd path
wsimport -keep -p serverWS http://localhost:9898/notaryService?wsdl
```


correr server & client:

```sh
cd NotaryServer/target   :   java -jar NotaryServer-1.0-SNAPSHOT-jar-with-dependencies.jar 
cd NotaryClient/target   :   java -jar NotaryClient-1.0-SNAPSHOT-jar-with-dependencies.jar

(com cc a funcionar)
java  -Djava.library.path=/usr/local/lib/ -jar NotaryServer-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

SOURCE JAVA WS ; https://www.codejava.net/java-ee/web-services/create-client-server-application-for-web-service-in-java
