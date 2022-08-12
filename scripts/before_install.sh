#!/bin/bash
docker run -v /opt:/opt -v /etc/zabbix:/etc/zabbix registry.kurlycorp.kr/baseimages/docker-initialize:dev

for image in $(grep -iw registry /opt/docker-compose.yml  | awk '{print $2}' | awk -F: '{print $1}' ); do
  docker pull $image:TAG
  docker tag $image:TAG $image:latest
done

