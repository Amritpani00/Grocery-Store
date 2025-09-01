FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
ENV JAVA_OPTS="" \
    MONGODB_URI="mongodb://mongo:27017/grocery" \
    APP_SEED="true" \
    APP_EMBEDDED_MONGO="false" \
    SERVER_PORT=8080 \
    JWT_SECRET="6b0c9a4e2f1d7c8b6a9e3f5c1d2b4a6f7e8d9c0b1a2f3e4d5c6b7a8f9e0d1c2"
EXPOSE 8080
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

