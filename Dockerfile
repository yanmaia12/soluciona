# Etapa 1: Usar Maven para construir (Mantemos este que funciona)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Usar Eclipse Temurin (A CORREÇÃO É AQUI)
# Esta é a imagem mais estável e leve para Java 17 hoje em dia
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]