# Estágio de Build
FROM maven:3.9.15-amazoncorretto-21-al2023 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

RUN mvn clean package -DskipTests


# Estágio de Execução
FROM amazoncorretto:21-al2023
WORKDIR /app

COPY --from=build /build/target/*.jar ./biblioteca-api.jar
EXPOSE 8091

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT ["java", "-jar", "biblioteca-api.jar"]