#!/bin/sh
java -Xms2048m -Xmx2048m -XX:+UseG1GC -XX:+DisableExplicitGC -XX:+UseStringDeduplication -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -verbose:gc -XX:+PrintGCDetails -jar -Dspring.profiles.active=$RUN_ENV -Duser.timezone=Asia/Seoul $RUN_JVM_PARAM /usr/local/bin/kurlyBatch-0.0.1-SNAPSHOT.jar
