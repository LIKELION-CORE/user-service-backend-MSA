FROM openjdk:11
COPY ./target/user-service-0.0.1-SNAPSHOT.jar application.jar
ENV TZ=Asia/Seoul

ENTRYPOINT ["java","-jar","-DSpring.profiles.active=prod","/application.jar"]
# ENTRYPOINT ["java","-jar", "-Djasypt.encryptor.password=${JASYPT_KEY}", "/application.jar"]
