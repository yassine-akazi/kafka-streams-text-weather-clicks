# Réponses aux questions du TP 5

## Exercice 1

### 1. Rôle du topic `text-dead-letter`
Le topic `text-dead-letter` sert à stocker les messages invalides ou rejetés. Il permet de garder une trace des erreurs sans bloquer le traitement principal.

### 2. Importance du nettoyage
Le nettoyage garantit un format homogène. Cela évite les erreurs causées par les espaces inutiles, les différences de casse ou les messages vides.

### 3. Pourquoi convertir en majuscules avant de vérifier les mots interdits ?
La conversion en majuscules permet de détecter les mots interdits quelle que soit leur écriture : `spam`, `Spam` et `SPAM` deviennent tous `SPAM`.

### 4. Amélioration avec fichier ou base de données
On peut charger la liste des mots interdits depuis un fichier JSON, un fichier texte, une base de données ou une API. Pour une application plus avancée, on peut rafraîchir cette liste périodiquement sans redémarrer Kafka Streams.

## Exercice 2

### 1. Pourquoi regrouper par station ?
Il faut regrouper les données par station pour calculer une moyenne propre à chaque station. Sans regroupement, on obtiendrait une moyenne globale de toutes les stations.

### 2. Différence entre `KStream` et `KTable`
`KStream` représente un flux continu d'événements indépendants. `KTable` représente une vue mise à jour en continu, souvent utilisée pour représenter le résultat courant d'une agrégation.

### 3. Pourquoi une agrégation donne souvent une `KTable` ?
Une agrégation produit une valeur actualisée à chaque nouvel événement. Cette valeur correspond à un état courant, donc elle est représentée naturellement sous forme de `KTable`.

### 4. Gérer `Station1,error,60`
Il faut parser la ligne dans un bloc `try/catch`. Si la température n'est pas numérique, le message est ignoré ou envoyé vers un topic d'erreurs. Cela évite l'arrêt brutal de l'application.

### 5. Pourquoi Kafka Streams est adapté ?
Kafka Streams est adapté parce qu'il traite les données en temps réel, s'intègre directement avec Kafka, supporte les transformations, les filtres, les regroupements et les agrégations continues.

## Exercice 3

### Architecture réalisée
L'architecture contient trois composants :

1. Une application web Spring Boot productrice de clics.
2. Une application Kafka Streams qui consomme `clicks`, agrège les clics par utilisateur et publie dans `click-counts`.
3. Une API REST Spring Boot qui consomme `click-counts` et expose `GET /clicks/count`.

### Scénario de test
1. Démarrer Docker et Kafka.
2. Créer les topics.
3. Lancer le producteur web.
4. Lancer l'application Kafka Streams.
5. Lancer l'API REST.
6. Cliquer plusieurs fois sur le bouton.
7. Vérifier le résultat avec `curl http://localhost:8081/clicks/count`.

### Résultat attendu
Le nombre total de clics augmente progressivement après chaque clic envoyé depuis l'interface web.
