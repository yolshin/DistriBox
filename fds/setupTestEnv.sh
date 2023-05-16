#!/bin/zsh
docker-compose -f ../docker/docker-compose.fds.yml -f ../docker/docker-compose.zookeeper.yml up postgresdb1 postgresdb2 postgresdb3 zoo1 zoo2 zoo3