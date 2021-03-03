FROM registry.kurlycorp.kr/baseimages/openjdk11:latest

COPY build/libs/kurlyBatch-0.0.1-SNAPSHOT.jar /usr/local/bin/kurlyBatch-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN apt-get update && apt-get install -y fontconfig
RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
