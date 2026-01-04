# ETAPA 1: BUILD
# Usando JDK 22 para compilar
FROM maven:3.9-eclipse-temurin-22 AS build
WORKDIR /app

# Copia tudo para o container
COPY . .

# Compila o projeto com Java 22
RUN mvn clean package -DskipTests

# ETAPA 2: RUN
# Usando o JRE leve do Java 22 para rodar
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app

# Pega o JAR gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Comando para iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]