# SEC
Sistemas Elevada Confiabilidade 18/19

## How To

compilar:

```sh
cd SEC && mvn clean install
```

correr server & client:

```sh
cd NotaryServer/target   :   java -jar NotaryServer-1.0-SNAPSHOT-jar-with-dependencies.jar 
cd NotaryClient/target   :   java -jar NotaryClient-1.0-SNAPSHOT-jar-with-dependencies.jar

(com cc)
java  -Djava.library.path=/usr/local/lib/ -jar NotaryServer-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

## STAGE 2

Depois de compilar o projecto copiar o modulo NotaryServer N vezes para a pasta servers incrementando o nome da pasta(p.e. : NotaryServer, NotaryServer2, ...)

## DEMO TESTS

f - numero de falhas toleraveis
N - numero de servers, tal que N > 3f

## SERVER DOWN

inicializar N servers
inicializar 2 clients
correr getStateOfGood -> all ok
matar f servers
correr getStateOfGood -> all ok
matar outro server
correr getStateOfGood -> not ok

## ALTERAR GOOD ENTRE BUYERS MANUALMENTE

(NOTA: os servers necessitam de ser reiniciados para fazer load dos ficheiros manipulados manualmente)

inicializar 4 servers para tolerar 1 falha
inicializar 2 clients
(inicialmente good0 pertence ao user0 e nao esta a venda)
correr getsateofgood da good0 -> verificar que pertence ao user0
manulmente alterar o owner da good0 para user1 no server1
correr getsateofgood da good0 -> verificar que pertence ao user0
manulmente alterar o owner da good0 para user1 no server2
correr getsateofgood da good0 -> numero de acks NOT-OK





















