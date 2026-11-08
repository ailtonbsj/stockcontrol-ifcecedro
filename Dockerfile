# ---------- Estágio de build ------------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

# ---------- Estágio de execução ---------
FROM eclipse-temurin:25-jre
ENV CATALINA_HOME=/usr/local/tomcat
ENV PATH=$CATALINA_HOME/bin:$PATH
WORKDIR $CATALINA_HOME
RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl ca-certificates \
	&& curl -fsSL https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.40/bin/apache-tomcat-10.1.40.tar.gz -o /tmp/tomcat.tar.gz \
	&& tar -xzf /tmp/tomcat.tar.gz --strip-components=1 -C "$CATALINA_HOME" \
	&& rm /tmp/tomcat.tar.gz \
	&& rm -rf "$CATALINA_HOME/webapps/"* \
	&& apt-get purge -y --auto-remove curl \
	&& rm -rf /var/lib/apt/lists/*
COPY --from=build /build/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
