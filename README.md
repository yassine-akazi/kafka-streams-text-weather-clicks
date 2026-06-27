# TP 5 – Traitement de flux avec Kafka Streams

**Module :** Big Data – 2026  
**Établissement :** ENSET Mohammedia  
**Enseignant :** Mr. Abdelmajid BOUSSELHAM

Ce dépôt contient une réalisation complète du TP 5 : nettoyage de texte, analyse météo et comptage de clics avec Kafka Streams et Spring Boot.

## Architecture du projet

```text
tp5-kafka-streams-complet/
├── docker-compose.yml
├── pom.xml
├── scripts/
│   ├── create-topics.sh
│   └── test-commands.md
├── docs/
│   └── reponses-questions.md
├── ex1-text-cleaner/
├── ex2-weather-streams/
├── ex3-click-producer/
├── ex3-click-counter/
└── ex3-click-rest/
```

## Démarrage rapide

```bash
docker compose up -d
./scripts/create-topics.sh
mvn clean package
```

## Exercice 1 : nettoyage de texte

Application : `ex1-text-cleaner`

Traitements réalisés :
- lecture depuis `text-input` ;
- suppression des espaces inutiles ;
- conversion en majuscules ;
- rejet des messages vides, trop longs ou contenant `HACK`, `SPAM`, `XXX` ;
- écriture des messages valides dans `text-clean` ;
- écriture des messages invalides dans `text-dead-letter`.

Lancement :

```bash
mvn -pl ex1-text-cleaner spring-boot:run
```

## Exercice 2 : analyse météo

Application : `ex2-weather-streams`

Traitements réalisés :
- lecture depuis `weather-data` ;
- parsing du format `station,temperature,humidity` ;
- filtrage des températures supérieures à 30°C ;
- conversion Celsius vers Fahrenheit ;
- regroupement par station ;
- calcul des moyennes température/humidité ;
- publication dans `station-averages`.

Lancement :

```bash
mvn -pl ex2-weather-streams spring-boot:run
```

## Exercice 3 : comptage de clics

Applications :
- `ex3-click-producer` : interface web avec bouton ;
- `ex3-click-counter` : Kafka Streams pour compter les clics par utilisateur ;
- `ex3-click-rest` : API REST de consultation.

Lancement :

```bash
mvn -pl ex3-click-producer spring-boot:run
mvn -pl ex3-click-counter spring-boot:run
mvn -pl ex3-click-rest spring-boot:run
```

Ouvrir : `http://localhost:8080`  
Tester l'API :

```bash
curl http://localhost:8081/clicks/count
```

Exemple de réponse :

```json
{
  "totalClicks": 10,
  "byUser": {
    "user1": 7,
    "user2": 3
  }
}
```

## Livrables inclus

- Code source complet des applications.
- Commandes de création des topics Kafka.
- Commandes de test.
- Réponses aux questions théoriques.
- Architecture et explications.

## Difficultés rencontrées et solutions

1. **Messages mal formés :** utilisation de `try/catch` pour éviter l'arrêt de l'application.
2. **Mots interdits en minuscules :** conversion en majuscules avant validation.
3. **Agrégations météo :** utilisation de `KGroupedStream` et `KTable` pour conserver une vue mise à jour.
4. **Synchronisation REST :** stockage local en mémoire des résultats consommés depuis `click-counts`.
