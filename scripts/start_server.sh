#!/bin/bash
docker-compose -f /opt/docker-compose.yml down
HOSTNAME=$HOSTNAME docker-compose -f /opt/docker-compose.yml up -d
for i in $(docker images | grep "<none>" | awk '{print $3}')
do
    docker rmi $i
done

if [[ -f  "/opt/docker-compose-mgmt.yml" ]]; then
	  HOSTNAME=$HOSTNAME docker-compose -f /opt/docker-compose-mgmt.yml up -d
fi

if [[ -f  "/opt/docker-compose-datadog.yml" ]]; then
	  docker-compose -f /opt/docker-compose-datadog.yml up -d
fi
