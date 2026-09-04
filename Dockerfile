# ============================================================
# Stage 1: Build the Spring Boot application with Maven
# Uses Maven wrapper (mvnw) bundled with the project to ensure
# the correct Maven version is used regardless of the host.
# ============================================================
FROM eclipse-temurin:17-jdk-jammy AS build

# Set working directory inside the build container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (leveraging Docker layer caching)
# These files change less frequently than source code, so Docker will
# reuse the cached dependency layer unless pom.xml changes.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the Maven wrapper executable and download all dependencies.
# Running `dependency:go-offline` ensures all JARs are cached before
# we copy source code, so subsequent builds with code-only changes are fast.
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Now copy the full source code
COPY src src

# Build the application.
# -B  : batch mode (no interactive prompts, cleaner CI/Docker logs)
# -DskipTests : skip unit tests during image build (run them separately in CI)
# The final artifact will be: target/ai-reporting-studio-0.0.1-SNAPSHOT.jar
RUN ./mvnw clean package -B -DskipTests

# ============================================================
# Stage 2: Create the minimal runtime image
# Uses a JRE (not JDK) since we only need to run the compiled JAR.
# This significantly reduces the final image size.
# ============================================================
FROM eclipse-temurin:17-jre-jammy AS runtime

# Set working directory in the runtime container
WORKDIR /app

# Create a non-root user for security best practices
# Running as root inside a container is a security anti-pattern.
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Install required system fonts for JasperReports
# JasperReports uses AWT/font libraries for PDF generation and chart rendering.
# Without these, you will see "java.lang.InternalError: java.awt.Font" errors.
RUN apt-get update && apt-get install -y --no-install-recommends \
    fonts-dejavu-core \
    fonts-dejavu-extra \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

# Rebuild font cache so Java can discover the installed fonts
RUN fc-cache -f -v

# Copy only the built Spring Boot JAR from the build stage
# We use a wildcard for the version so you don't need to update the Dockerfile
# when you bump the project version in pom.xml.
COPY --from=build /app/target/ai-reporting-studio-*.jar app.jar

# Switch to non-root user before running the application
USER appuser

# Expose the default Spring Boot port
EXPOSE 8080

# ============================================================
# Configure JVM and application entrypoint
# JAVA_OPTS can be overridden at runtime via:
#   docker run -e JAVA_OPTS="-Xmx512m -Xms256m" ...
# ============================================================
ENV JAVA_OPTS=""

# Launch the Spring Boot application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
