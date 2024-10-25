FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN ls -la /app/target

FROM openjdk:17-jdk-alpine AS final
WORKDIR /app

COPY --from=builder /app/target/projeto-final-0.0.1-SNAPSHOT.jar /app/projeto-final.jar
EXPOSE 8081
CMD ["java", "-jar", "projeto-final.jar"]
