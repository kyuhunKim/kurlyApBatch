#!/bin/sh
java -jar -Dspring.profiles.active=$RUN_ENV -Duser.timezone=Asia/Seoul $RUN_JVM_PARAM /usr/local/bin/kurlyBatch-0.0.1-SNAPSHOT.jar
