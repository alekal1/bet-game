FROM openjdk:22-jdk-slim
COPY build/libs/bet-game-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 999