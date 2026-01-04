# ETAPA 1: BUILD
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copia tudo (incluindo o pom.xml e a pasta src)
COPY . .

# Compila e gera o JAR
RUN mvn clean package -DskipTests

# ETAPA 2: RUN
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Pega o JAR gerado na etapa acima
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Roda a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]