
# ---- Build Stage ----
FROM mcr.microsoft.com/openjdk/jdk:17-azurelinux AS builder
WORKDIR /app
# Install tar (required for Maven builds on Azure Linux)
RUN microdnf install tar && rm -rf /var/cache/microdnf
# Copy Maven wrapper and source
COPY mvnw pom.xml .
COPY .mvn .mvn
COPY src src
# Build the application
RUN ./mvnw clean package -DskipTests

# ---- Runtime Stage ----
FROM mcr.microsoft.com/openjdk/jdk:17-azurelinux AS runtime
WORKDIR /app
# Create non-root user for security
RUN useradd -u 1001 -g 0 appuser && mkdir /app && chown appuser:0 /app
USER appuser
# Copy built jar from builder stage
COPY --from=builder /app/target/bank-api-0.0.1-SNAPSHOT.jar bank-api.jar
EXPOSE 8080
# Healthcheck for container
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java","-jar","/app/bank-api.jar"]
