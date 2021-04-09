  
#!/bin/bash

docker run --rm -v "$PWD":/home/gradle/project -w /home/gradle/project registry.kurlycorp.kr/baseimages/gradle:jdk11 /home/gradle/project/gradlew clean -a :$1:build -x test
