#!/bin/sh

java -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$RUN_ENV $RUN_JVM_PARAM $JAVA_AGENT -jar /usr/local/bin/kurlyBatch-0.0.1-SNAPSHOT.jar
