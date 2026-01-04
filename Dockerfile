# Etapa 1: Build da aplicação
# Usamos uma imagem do Maven para compilar o código
FROM maven:3.8.4-openjdk-17 AS build
COPY src/main/java/com/nimbus/nimbusWebServer .
RUN mvn clean package -DskipTests

# Etapa 2: Execução da aplicação
# Usamos uma imagem leve do JDK apenas para rodar o .jar gerado
FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]