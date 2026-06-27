# Commandes de test

```bash
docker compose up -d
./scripts/create-topics.sh
```

## Exercice 1
```bash
docker exec -it tp5-kafka kafka-console-producer --topic text-input --bootstrap-server localhost:9092
# saisir:
Bonjour Kafka Streams
hello      world
this is spam message
message contenant hack
```
```bash
docker exec -it tp5-kafka kafka-console-consumer --topic text-clean --from-beginning --bootstrap-server localhost:9092
# BONJOUR KAFKA STREAMS
# HELLO WORLD
```
```bash
docker exec -it tp5-kafka kafka-console-consumer --topic text-dead-letter --from-beginning --bootstrap-server localhost:9092
```

## Exercice 2
```bash
docker exec -it tp5-kafka kafka-console-producer --topic weather-data --bootstrap-server localhost:9092
Station1,25.3,60
Station2,35.0,50
Station2,40.0,45
Station1,32.0,70
```
```bash
docker exec -it tp5-kafka kafka-console-consumer --topic station-averages --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property key.separator=" : "
```

## Exercice 3
```bash
mvn -pl ex3-click-producer spring-boot:run
mvn -pl ex3-click-counter spring-boot:run
mvn -pl ex3-click-rest spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
curl http://localhost:8081/clicks/count
```
