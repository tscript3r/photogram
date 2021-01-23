FROM openjdk:11
ARG JAR_FILE=backend/target/*.jar
COPY ${JAR_FILE} photogram.jar
ENTRYPOINT ["java","-jar","/photogram.jar"]
EXPOSE 8080