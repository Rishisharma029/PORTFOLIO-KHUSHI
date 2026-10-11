# ==============================================================================
# Multi-Stage Production Dockerfile for Khushi Singh's Portfolio
# Stage 1: Build executable JAR using JDK 21
# Stage 2: Minimal, secure JRE 21 runtime container
# ==============================================================================

# --- STAGE 1: Build Application ---
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper & POM to leverage Docker layer caching for dependencies
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies offline to optimize cache
RUN ./mvnw dependency:go-offline -B || true

# Copy source code and build production jar
COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

# --- STAGE 2: Secure Production Runtime ---
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Create a non-root system user and group for security
RUN addgroup -S -g 1001 kushu && \
    adduser -S -u 1001 -G kushu kushu

# Copy executable jar from builder stage
COPY --from=builder --chown=kushu:kushu /build/target/kushu-portfolio-*.jar app.jar

# Switch to non-root user
USER kushu:kushu

# Expose standard application port
EXPOSE 8080

# Environment variables
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Built-in Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD wget -q --spider http://localhost:8080/ || exit 1

# Launch application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
