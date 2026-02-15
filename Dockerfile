# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Optimasi Java untuk RAM kecil & gunakan PORT environment
ENTRYPOINT ["sh", "-c", "java -Xmx160m -Xms160m -XX:+UseSerialGC -XX:MaxRAM=256m -Dserver.address=0.0.0.0 -Dserver.port=${PORT:-8080} -jar app.jar"]
