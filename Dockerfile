FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
# Use exec form to ensure proper signal handling for graceful shutdown
ENTRYPOINT ["java","-jar","/app.jar"]
