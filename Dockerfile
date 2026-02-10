FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY a2/ .
RUN mvn clean package -DskipTests

#if smth wrong, check this ln first, 
FROM eclipse-temurin:21-jre 
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]