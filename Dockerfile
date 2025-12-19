# Estágio 1: Build
# Usa uma imagem do JDK 21 para compilar o código
FROM eclipse-temurin:21-jdk-jammy AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os ficheiros de configuração do Maven e o wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw

# Descarrega as dependências (camada de cache)
RUN ./mvnw dependency:go-offline

# Copia o código fonte e gera o ficheiro .jar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Run
# Usa uma imagem mais leve (JRE) apenas para execução
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia o .jar gerado no estágio anterior
# O nome do jar baseia-se no <artifactId> e <version> do seu pom.xml
COPY --from=build /app/target/prg04-rodrigo-back-end-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Render vai usar (8080 por padrão no seu código)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]