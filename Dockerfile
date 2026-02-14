# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
# Tentukan folder kerja agar file tidak berantakan
WORKDIR /app
# Salin semua file ke folder /app
COPY . .
# Jalankan build
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Ambil file jar dari folder build sebelumnya secara spesifik
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Jalankan aplikasi
ENTRYPOINT ["java","-jar","app.jar"]