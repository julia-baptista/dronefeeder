FROM openjdk:11.0-jdk as build-image
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM openjdk:11.0-jre
COPY --from=build-image /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]