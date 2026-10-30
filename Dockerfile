FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY sample-java-app/pom.xml .
COPY sample-java-app/src ./src

RUN mvn -B clean package


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup \
    && adduser -S appuser -G appgroup

COPY --from=builder \
    /build/target/ci-demo-1.0.0-SNAPSHOT.jar \
    /app/app.jar

USER appuser

EXPOSE 3000

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget -q -O /dev/null http://127.0.0.1:3000/health || exit 1

CMD ["java", "--add-modules", "jdk.httpserver", "-cp", "/app/app.jar", "com.devops.lab7.Application"]