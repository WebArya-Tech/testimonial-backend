# ==============================
# Stage 1: Build the Spring Boot JAR
# ==============================
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Pre-download dependencies (faster rebuilds)
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN ./mvnw clean package -DskipTests

# ==============================
# Stage 2: Run the built app
# ==============================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Install curl (for healthcheck)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy JAR
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# REMOVE insecure defaults (keep empty)
ENV MONGODB_URI=""
ENV MAIL_USERNAME=""
ENV MAIL_PASSWORD=""
ENV ADMIN_USERNAME=""
ENV ADMIN_PASSWORD=""
ENV FRONTEND_URL=""
ENV JWT_SECRET=""
ENV JWT_EXPIRY_HOURS="24"
ENV CLOUDINARY_URL=""

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]