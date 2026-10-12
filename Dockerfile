# ---------- Estágio de build ------------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

# ---------- Estágio de execução ---------
FROM tomcat:9.0-jre25
# Remove a aplicação padrão para servir o sistema na raiz (path "/")
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /build/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
