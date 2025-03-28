# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install
RUN apt-get update && apt-get install -y rabbitmq-server
RUN apt-get update && apt-get install -y supervisor && rm -rf /var/lib/apt/lists/*

# Create Supervisor config directory
RUN mkdir -p /etc/supervisor/conf.d

# Copy the built JAR file
COPY target/*.jar app.jar
COPY .env .env
# Copy the Supervisor config
COPY supervisord.conf /etc/supervisor/supervisord.conf

# Expose the application port
EXPOSE 8080 5672 15672

# Start Supervisor
CMD ["supervisord", "-c", "/etc/supervisor/supervisord.conf"]
