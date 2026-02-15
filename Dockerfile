# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Download dependency offline
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code dan build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy jar hasil build
COPY --from=build /app/target/*.jar app.jar

# Copy entrypoint script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Gunakan entrypoint script untuk memastikan port dan RAM aman
ENTRYPOINT ["/app/entrypoint.sh"]
