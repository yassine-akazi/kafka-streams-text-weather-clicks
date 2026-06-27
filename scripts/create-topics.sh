#!/usr/bin/env bash
set -e
KAFKA="docker exec -it tp5-kafka kafka-topics --bootstrap-server localhost:9092"
for topic in text-input text-clean text-dead-letter weather-data station-averages clicks click-counts; do
  $KAFKA --create --if-not-exists --topic $topic --partitions 1 --replication-factor 1
done
$KAFKA --list
